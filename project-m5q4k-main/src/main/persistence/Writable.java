package persistence;

import org.json.JSONObject;

//interface that Json object
public interface Writable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
