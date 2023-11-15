import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Settings {
    private static final Properties properties = new Properties();

    static {
        try (FileInputStream fileInputStream = new FileInputStream("settings.properties")) {
            properties.load(fileInputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getRepositoryType() {
        return properties.getProperty("Repository");
    }

    public static String getPatientsFileName() {
        return properties.getProperty("Pacienti");
    }

    public static String getAppointmentsFileName() {
        return properties.getProperty("Programari");
    }
}
