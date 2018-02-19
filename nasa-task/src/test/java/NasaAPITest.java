import org.json.JSONArray;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by olena.kolesnyk on 09/02/2018.
 */
public class NasaAPITest {

    /**
     * <b>Check photos by sol and by earth date time are equal</b>
     *
     * @param numberOfPhotos number of photos to be compared
     * @param sol            sol index
     *                       <p>
     */
    @Test
    @Parameters({"numberOfPhotos", "sol"})
    public void checkPhotosAreEqual(String numberOfPhotos, String sol) throws IOException {
        PhotosService service = new PhotosService();
        JSONArray solPhotosInfo = service.getPhotosInfo(sol, DateType.SOL);
        String earthDate = service.getEarthDateFromResponse(solPhotosInfo.getJSONObject(0));
        JSONArray datePhotosInfo = service.getPhotosInfo(earthDate, DateType.DATE);
        for (int i = 0; i < Integer.valueOf(numberOfPhotos); i++) {
            JSONAssert.assertEquals(
                    solPhotosInfo.getJSONObject(i), datePhotosInfo.getJSONObject(i), JSONCompareMode.NON_EXTENSIBLE);
        }
    }

    /**
     * <b>Check difference in photos count took by different cameras</b>
     *
     * @param sol sol index
     *            <p>
     */
    @Test
    @Parameters("sol")
    public void checkPhotosCountDifference(String sol) throws IOException {
        List<Integer> allPhotosNumber = new ArrayList<>();
        for (CameraNames item : CameraNames.values()) {
            int oneCameraPhotos = new PhotosService().getPhotosNumberByCamera(sol, DateType.SOL, item);
            allPhotosNumber.add(oneCameraPhotos);
        }
        Collections.sort(allPhotosNumber);
        Assert.assertTrue(allPhotosNumber.get(0) * 10 > allPhotosNumber.get(allPhotosNumber.size() - 1),
                "One of the cameras makes 10 time more photos than the other; ");
    }

    /**
     * <b>Second option of the above test using 1 request and collecting info into Map</b>
     *
     * @param sol sol index
     *            <p>
     */
    @Test
    @Parameters("sol")
    public void checkPhotosCountDifference2Option(String sol) throws IOException {
        PhotosService service = new PhotosService();
        JSONArray photosList = service.getPhotosInfo(sol, DateType.SOL);
        Map<String, Integer> stats = service.getCamerasStatsFromPhoto(photosList);
        List<Integer> values = new ArrayList<>(stats.values());
        Collections.sort(values);

        Assert.assertTrue(values.get(0) * 10 > values.get(values.size() - 1),
                "One of the cameras makes 10 time more photos than the other; ");
    }

}
