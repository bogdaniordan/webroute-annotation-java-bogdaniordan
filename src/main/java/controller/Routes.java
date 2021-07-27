package controller;

public class Routes {

    @WebRoute(method = WebRoute.Method.POST, path = "/home")
    public String routeOne() {
        return "one";
    }

    @WebRoute(method = WebRoute.Method.GET, path = "/sign-in")
    public String routeTwo() {
        return "two";
    }
}
