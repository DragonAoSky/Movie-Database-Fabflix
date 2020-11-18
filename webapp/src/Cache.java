import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class Cache {
    private JsonArray jsonArray;
    private String title;

    public Cache() {
        this.title = "";
        this.jsonArray = new JsonArray();

    }

    public JsonArray getJsonArray() {
        return jsonArray;
    }

    public void setJsonArray(JsonArray jsonArray) {
        this.jsonArray = jsonArray;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void addOBJ (JsonObject OBJ){
        jsonArray.add(OBJ);
    }
}
