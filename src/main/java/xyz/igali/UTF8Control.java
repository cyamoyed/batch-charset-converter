package xyz.igali;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

class UTF8Control extends ResourceBundle.Control {
    @Override
    public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload)
            throws IllegalAccessException, InstantiationException, java.io.IOException {
        String bundleName = toBundleName(baseName, locale);
        String resourceName = toResourceName(bundleName, "properties");
        InputStream stream = loader.getResourceAsStream(resourceName);
        if (stream == null) {
            return null;
        }
        try (InputStreamReader reader = new InputStreamReader(new BufferedInputStream(stream), StandardCharsets.UTF_8)) {
            return new PropertyResourceBundle(reader);
        }
    }
}