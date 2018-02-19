import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by olena.kolesnyk on 10/02/2018.
 */
class PropertiesHelper {

    private static final String PROPERTIES_FILE_NAME = "config.properties";
    private static final String REQUEST_URL_DATA = "request_url";
    private static final String API_KEY_DATA = "api_key";

    private static final String SOL_URL = "sol=%s";
    private static final String DATE_URL = "earth_date=%s";
    private static final String API_KEY = "api_key=%s";
    private static final String CAMERA_URL = "camera=%s";

    String buildRequestPhotos(String date, DateType dateType) throws IOException {
        Properties properties = getPropertiesFile();
        StringBuilder request = new StringBuilder();
        request.append(properties.getProperty(REQUEST_URL_DATA));
        request.append("?");
        request.append(API_KEY);
        request.append("&");

        switch (dateType) {
            case SOL:
                request.append(SOL_URL);
                break;
            case DATE:
                request.append(DATE_URL);
                break;
        }
        return String.format(request.toString(), properties.getProperty(API_KEY_DATA), date);
    }

    String buildRequestPhotosWithCamera(String request, CameraNames camera) {
        String newRequest = request + "&" + CAMERA_URL;
        return String.format(newRequest, camera.name());
    }

    private Properties getPropertiesFile() throws IOException {
        Properties properties = null;
        InputStream inputStream = null;
        try {
            properties = new Properties();
            inputStream = getClass().getClassLoader().getResourceAsStream(PROPERTIES_FILE_NAME);

            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new FileNotFoundException("Property file '" + PROPERTIES_FILE_NAME + "' is not found");
            }
        } catch (Exception e) {
            System.out.println("Exception: " + e);
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return properties;
    }
}
