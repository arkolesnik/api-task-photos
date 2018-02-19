import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by olena.kolesnyk on 10/02/2018.
 */
class PhotosService {

    private static final String EARTH_DATE_KEY = "earth_date";
    private static final String JSON_ARRAY_KEY = "photos";

    JSONArray getPhotosInfo(String date, DateType dateType) throws IOException {
        return getResponseByType(date, dateType).getJSONObject().getJSONArray(JSON_ARRAY_KEY);
    }

    String getEarthDateFromResponse(JSONObject jsonObject) {
        return jsonObject.toMap().get(EARTH_DATE_KEY).toString();
    }

    int getPhotosNumberByCamera(String date, DateType dateType, CameraNames camera) throws IOException {
        JSONArray photosArray = getResponseByCamera(date, dateType, camera).getJSONObject().getJSONArray(JSON_ARRAY_KEY);
        return photosArray.length();
    }

    Map<String, Integer> getCamerasStatsFromPhoto(JSONArray photosList) {
        Map<String, Integer> camerasPhotos = new HashMap<>();
        String cameraName;
        for (int i = 0; i < photosList.length(); i++) {
            cameraName = photosList.getJSONObject(i).getJSONObject("camera").get("name").toString();
            if (camerasPhotos.containsKey(cameraName)) {
                int number = camerasPhotos.get(cameraName) + 1;
                camerasPhotos.put(cameraName, number);
            } else {
                camerasPhotos.put(cameraName, 1);
            }
        }
        return camerasPhotos;
    }

    private JSONResponse getResponseByType(String date, DateType dateType) throws IOException {
        String requestStr = new PropertiesHelper().buildRequestPhotos(date, dateType);
        return executeRequest(requestStr);
    }

    private JSONResponse getResponseByCamera(String date, DateType dateType, CameraNames camera) throws IOException {
        PropertiesHelper helper = new PropertiesHelper();
        String requestStr = helper.buildRequestPhotos(date, dateType);
        String requestStrWithCamera = helper.buildRequestPhotosWithCamera(requestStr, camera);
        return executeRequest(requestStrWithCamera);
    }

    private JSONResponse executeRequest(String request) throws IOException {
        HttpClient client = HttpClientBuilder.create().build();
        HttpResponse response = client.execute(new HttpGet(request));
        return new JSONResponse(response);
    }

}
