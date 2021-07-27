
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.nio.file.Path;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import controller.Routes;
import controller.WebRoute;

public class Test {

    public static void main(String[] args) throws Exception {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/home", new MyHandler());
        server.createContext("/sign-in", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            for (Method method: Routes.class.getMethods()) {
                if (method.isAnnotationPresent(WebRoute.class)) {
                    String path = method.getAnnotation(WebRoute.class).path();
                    System.out.println(path);
                    if (t.getRequestURI().toString().equals(path)) {
                        String response = method.toGenericString();
                        t.sendResponseHeaders(200, response.getBytes().length);
                        OutputStream os = t.getResponseBody();
                        os.write(response.getBytes());
                        os.close();
                    }
                }
            }
        }
    }
}
