package libs.arusoft.com.libraries;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.google.gson.JsonObject;

import java.io.IOException;

import libs.arusoft.com.aruservices.AruWebService;
import libs.arusoft.com.aruservices.GsonParser;
import libs.arusoft.com.libraries.dto.MovieDTO;

/**
 * Created by jose.ramos on 02/03/2016.
 */
public class MainActivity extends AppCompatActivity {

    private TextView titleView;
    private TextView genreView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);
        titleView = (TextView)findViewById(R.id.tv_title);
        genreView = (TextView)findViewById(R.id.tv_genre);

        findViewById(R.id.btn_call_ws_get).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, MovieDTO>(){
                    @Override
                    protected MovieDTO doInBackground(Void... params) {
                        return callWebService();
                    }

                    @Override
                    protected void onPostExecute(MovieDTO movieDTO) {
                        super.onPostExecute(movieDTO);
                        if( movieDTO != null){
                            titleView.setText(movieDTO.getTitle());
                            genreView.setText(movieDTO.getGenre());
                        }
                    }
                }.execute();
            }
        });

        findViewById(R.id.btn_call_ws_post).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Void, JsonObject>(){
                    @Override
                    protected JsonObject doInBackground(Void... params) {
                        return callWebServicePost();

                    }
                }.execute();
            }
        });
    }

    private MovieDTO callWebService() {
        AruWebService.Builder builder = new AruWebService.Builder();
        builder.requestMethod(AruWebService.RequestMethodEnum.GET);
        builder.url("http://www.omdbapi.com/");
        builder.queryParameter("t", "the martian");
        builder.queryParameter("y", "2015");
        builder.queryParameter("plot", "full");
        builder.queryParameter("r", "json");
        builder.responseType(MovieDTO.class);
        builder.parser(new GsonParser());

        AruWebService aruWebService = builder.build();
        try {
            MovieDTO movieDTO = (MovieDTO) aruWebService.execute();
            return movieDTO;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    private JsonObject callWebServicePost(){
        AruWebService.Builder builder = new AruWebService.Builder();
        builder.requestMethod(AruWebService.RequestMethodEnum.POST);
        builder.url("http://200.36.107.164/API/api/Login/LoginApp");
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("tokenUsuario", "NqykIx");
        jsonObject.addProperty("tokenApp", "2YVGFHMf9U");
        jsonObject.addProperty("latitud", 3.1);
        jsonObject.addProperty("longitud", 4.1);
        builder.dataTransferObject(jsonObject);
        builder.headers("Authorization", "NqykIx");
        builder.headers("Content-type", "application/json");
        builder.responseType(JsonObject.class);
        builder.parser(new GsonParser());

        AruWebService aruWebService = builder.build();
        try {
            JsonObject dto = (JsonObject) aruWebService.execute();
            return dto;

        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
