
package persistence;

import model.Food;
import model.Shelter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.stream.Stream;

import org.json.*;

// Represents a reader that reads shelter from JSON data stored in file
public class JsonReader {
    private String source;

    // EFFECTS: constructs reader to read from source file
    public JsonReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads shelter from file and returns it;
    // throws IOException if an error occurs reading data from file
    public Shelter read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseWorkRoom(jsonObject);
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses shelter from JSON object and returns it
    private Shelter parseWorkRoom(JSONObject jsonObject) {
        // String name = jsonObject.getString("name");
        Shelter shelter = new Shelter();
        addFoods(shelter, jsonObject);
        return shelter;
    }

    // MODIFIES: shelter
    // EFFECTS: parses food items from JSON object and adds them to shelter
    private void addFoods(Shelter shelter, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("foods");
        for (Object json : jsonArray) {
            JSONObject nextFood = (JSONObject) json;
            addFood(shelter, nextFood);
        }
    }

    // MODIFIES: shelter
    // EFFECTS: parses food item from JSON object and adds it to shelter
    private void addFood(Shelter shelter, JSONObject jsonObject) {
        String name = jsonObject.getString("name");
        LocalDate expireDate = LocalDate.parse(jsonObject.getString("expireDate"));
        boolean isTaken = jsonObject.getBoolean("isTaken");
        boolean isAllergenic = jsonObject.optBoolean("isAllergenic", false);
        Food food = new Food(name, expireDate, isTaken);
        shelter.addFood(food);
        if (isAllergenic) {
            food.setAllergenic();
        }
    }
}
