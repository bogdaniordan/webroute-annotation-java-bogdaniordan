package controller;

public class Routes {

    @WebRoute(path = "/home")
    public String routeOne() {
        return "one";
    }

    @WebRoute(path = "/sign-in")
    public String routeTwo() {
        return "two";
    }
}
