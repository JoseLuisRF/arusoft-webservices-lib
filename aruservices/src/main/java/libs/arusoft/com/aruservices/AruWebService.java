package libs.arusoft.com.aruservices;

import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by jose.ramos on 01/03/2016.
 */
public class AruWebService {

    private static final String TAG = AruWebService.class.getSimpleName();


    private String url;
    private RequestMethodEnum requestMethod;
    private Map<String, String> queryParameter;
    private Object dataTransferObject;
    private Class clazz;
    private Parser parser;

    public AruWebService(String url, RequestMethodEnum requestMethod, Map<String, String> queryParameter, Object dataTransferObject, Class clazz, Parser parser) {
        this.url = url;
        this.requestMethod = requestMethod;
        this.queryParameter = queryParameter;
        this.dataTransferObject = dataTransferObject;
        this.clazz = clazz;
        this.parser = parser;
    }


    private String fetchResponse(HttpURLConnection connection) throws IOException {
        StringBuffer buffer = new StringBuffer();
        InputStream inputStream = null;
        BufferedReader reader = null;
        connection.connect();
        inputStream = connection.getInputStream();
        if (inputStream == null) {
            // Nothing to do.
            return null;
        }
        reader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        while ((line = reader.readLine()) != null) {
            // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
            // But it does make debugging a *lot* easier if you print out the completed
            // buffer for debugging.
            buffer.append(line + "\n");
        }
        reader.close();
        connection.disconnect();
        if (buffer != null && buffer.length() > 0) {
            return buffer.toString();
        } else {
            // Stream was empty.  No point in parsing.
            return null;
        }

    }

    public enum RequestMethodEnum {
        POST, PUT, GET
    }

    public <T> T execute() throws IllegalAccessException, InstantiationException, IOException {
        String response;

        if (queryParameter != null) {
            String getUrl = url + "?";
            Uri.Builder uriBuilder = Uri.parse(getUrl).buildUpon();
            for (Map.Entry<String, String> param : queryParameter.entrySet()) {
                uriBuilder.appendQueryParameter(param.getKey(),
                        param.getValue());
            }
            url = uriBuilder.build().toString();

        }
        HttpURLConnection urlConnection = (HttpURLConnection) new URL(url).openConnection();
        urlConnection.setRequestMethod(requestMethod.toString());
        //urlConnection.setInstanceFollowRedirects(false);
        if (dataTransferObject != null) {
            String request = parser.stringify(dataTransferObject);
            Log.i(TAG, "request::" + request);

            urlConnection.setRequestProperty("Content-type", "application/json");
            urlConnection.setRequestProperty("Content-Length", Integer.toString(request.getBytes().length));
            byte[] outputInBytes = request.getBytes("UTF-8");
            OutputStream os = urlConnection.getOutputStream();
            os.write(outputInBytes);
            os.close();
        }

        urlConnection.connect();
        response = fetchResponse(urlConnection);
        urlConnection.disconnect();
        if (TextUtils.isEmpty(response)) {
            throw new IllegalAccessException("Object cannot be parsed.");
        }
        return parser.converToFromString(response, clazz);
    }

    /***********************************************************************************************
     * Class Builder
     ***********************************************************************************************/
    public static class Builder {
        private RequestMethodEnum requestMethod;
        private String url;
        private Map<String, String> queryParameter;
        private Class clazz;
        private Object dataTransferObject;
        private Parser parser;

        public Builder() {
            queryParameter = new HashMap<>();
        }

        public Builder queryParameter(String key, String value) {
            queryParameter.put(key, value);
            return this;
        }

        public Builder queryParameter(Map map) {
            queryParameter = map;
            return this;
        }

        public Builder url(String url) {
            this.url = url;
            return this;
        }

        public Builder requestMethod(RequestMethodEnum requestMethod) {
            this.requestMethod = requestMethod;
            return this;
        }

        public Builder responseType(Class clazz) {
            this.clazz = clazz;
            return this;
        }

        public Builder dataTransferObject(Object dataTransferObject) {
            this.dataTransferObject = dataTransferObject;
            return this;
        }

        public Builder parser(Parser parser) {
            this.parser = parser;
            return this;
        }

        public AruWebService build() {
            AruWebService aruWebService = new AruWebService(url, requestMethod, queryParameter, dataTransferObject, clazz, parser);
            return aruWebService;
        }
    }
}
