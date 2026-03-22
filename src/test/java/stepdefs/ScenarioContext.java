package stepdefs;

import io.restassured.response.Response;

public class ScenarioContext {

    private static final ThreadLocal<Response> response = new ThreadLocal<>();

    public static void setResponse(Response r) {
        response.set(r);
    }

    public static Response getResponse() {
        return response.get();
    }

    public static void clear() {
        response.remove();
    }
}
