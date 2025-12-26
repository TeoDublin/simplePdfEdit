package app.classes;

import java.net.URL;

public class ResourceUtils {

    private ResourceUtils() {}

    public static URL getResource(String path) {
        String fixed = path.startsWith("/") ? path.substring(1) : path;

        URL url = Thread.currentThread()
                .getContextClassLoader()
                .getResource(fixed);

        if (url == null) {
            throw new IllegalArgumentException("Resource not found: " + fixed);
        }

        return url;
    }

    public static String getResourceAsExternalForm(String path) {
        return getResource(path).toExternalForm();
    }
}
