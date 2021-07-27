
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controller.Routes;
import controller.WebRoute;

public class Main {
    private static final HashMap<String, Method> pathToMethods = new HashMap<>();
    private static final Routes ROUTES = new Routes();

    private static void storeMethodsAndPaths() {
        for (Method method: Routes.class.getMethods()) {
            if (method.isAnnotationPresent(WebRoute.class)) {
                String path = method.getAnnotation(WebRoute.class).path();
                pathToMethods.put(path, method);
            }
        }
    }

    public static void main(String[] args) throws Exception {
        storeMethodsAndPaths();
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/home", new MyHandler());
        server.createContext("/sign-in", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }



    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            String path = t.getRequestURI().toString();
            if (pathToMethods.containsKey(path)) {
                String response = null;
                try {
                    response = (String) pathToMethods.get(path).invoke(ROUTES);
                } catch (IllegalAccessException | InvocationTargetException e) {
                    e.printStackTrace();
                }
                t.sendResponseHeaders(200, response.getBytes().length);
                OutputStream os = t.getResponseBody();
                os.write(response.getBytes());
                os.close();
            }
        }
    }
}
