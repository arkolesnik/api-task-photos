import org.apache.commons.io.IOUtils;
import org.apache.http.HttpResponse;
import org.json.JSONObject;

import java.io.IOException;

/**
 * Created by olena.kolesnyk on 10/02/2018.
 */
class JSONResponse {
    private String content;

    JSONResponse(HttpResponse response) throws IOException {
        this.content = IOUtils.toString(response.getEntity().getContent());
    }

    JSONObject getJSONObject() {
        return new JSONObject(this.content);
    }
}
