package util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.prefs.Preferences;

public class ThemeManager {

    private static final Preferences prefs = Preferences.userRoot().node("app_theme");
    private static final String THEME_KEY = "theme";

    public static void setTheme(String theme) {
        prefs.put(THEME_KEY, theme);
    }

    public static String getTheme() {
        return prefs.get(THEME_KEY, "System");
    }

    public static boolean isDarkMode() {
        String theme = getTheme();
        if (theme.equals("Dark")) return true;
        if (theme.equals("Light")) return false;

        try {
            String os = System.getProperty("os.name").toLowerCase();
            if (!os.contains("win")) return false;

            ProcessBuilder builder = new ProcessBuilder(
                    "reg", "query",
                    "HKCU\\Software\\Microsoft\\Windows\\CurrentVersion\\Themes\\Personalize",
                    "/v", "AppsUseLightTheme"
            );
            builder.redirectErrorStream(true);
            Process process = builder.start();

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(process.getInputStream())
            );
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.contains("AppsUseLightTheme")) {
                    String[] parts = line.trim().split("\\s+");
                    String value = parts[parts.length - 1];
                    return value.equals("0x0");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }
}
