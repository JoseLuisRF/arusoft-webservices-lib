package libs.arusoft.com.aruservices;

import com.google.gson.Gson;

/**
 * Created by jose.ramos on 01/03/2016.
 */
public class GsonParser implements Parser {
    private Gson gson;

    public GsonParser(){
        this.gson = new Gson();
    }

    @Override
    public String stringify(Object object) {
        return gson.toJson(object);
    }

    @Override
    public Object convertToFromString(String data, Class clazz) {
        return  gson.fromJson(data, clazz);
    }
}
