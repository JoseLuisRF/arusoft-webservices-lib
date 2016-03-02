package libs.arusoft.com.aruservices;

import org.junit.Test;

import java.lang.Exception;
import java.util.HashMap;

import dalvik.annotation.TestTargetClass;
import libs.arusoft.com.aruservices.AruWebService;
import libs.arusoft.com.aruservices.GsonParser;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {

    public static class Usuario {

    }
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void consumeWebServiceGet() throws Exception {
        AruWebService.Builder builder = new AruWebService.Builder();
        builder.requestMethod(AruWebService.RequestMethodEnum.GET);
        builder.url("http://www.omdbapi.com/");
        builder.queryParameter("t", "the martian");
        builder.queryParameter("y", "2015");
        builder.queryParameter("plot", "full");
        builder.queryParameter("r", "json");
        builder.parser(new GsonParser());
        AruWebService aruWebService = builder.build();
        aruWebService.execute();
    }


}