package libs.arusoft.com.aruservices;

/**
 * Created by jose.ramos on 01/03/2016.
 */
public interface Parser {
    String stringify(Object object);
    <T>T converToFromString(String data, Class clazz);
}
