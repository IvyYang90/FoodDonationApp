
package model;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

import org.json.JSONArray;
import org.json.JSONObject;

import persistence.JsonReader;
import persistence.JsonWriter;
import persistence.Writable;

//Represents a shelter that contains list of food items
public class Shelter implements Writable {

    private List<Food> fooditems;

    // EFFECTS: constructs a shelter
    public Shelter() {
        this.fooditems = new ArrayList<Food>();
    }

    // MODIFIES: this
    // EFFECTS: adds a food item to the shelter and log it as an event
    public void addFood(Food food) {
        this.fooditems.add(food);
        EventLog.getInstance().logEvent(new Event("Added food: " + food.getName()));

    }

    // MODIFIES: this
    // EFFECTS: removes a food item from the shelter and log it as an event
    public void removeFood(Food food) {
        this.fooditems.remove(food);
        EventLog.getInstance().logEvent(new Event("Removed food: " + food.getName()));

    }

    // MODIFIES: this
    // EFFECTS: removes the food items that are past
    // their expire date from the shelter and log it as an event
    public void clearFood() {
        for (int i = fooditems.size() - 1; i >= 0; i--) {
            Food nextfood = fooditems.get(i);
            if (nextfood.isExpired()) {
                fooditems.remove(i); // Remove item by index
                EventLog.getInstance().logEvent(new Event("Cleared food: " + nextfood.getName()));
            }

        }

    }

    // EFFECTS: returns a list of food items in the shelter
    public List<Food> getFoodItems() {
        return this.fooditems;
    }

    // EFFECTS: returns the number of food items in the shelter and log it as an event
    public int getNumOfFood() {
        int foodcount = this.fooditems.size();

        EventLog.getInstance().logEvent(new Event("Num of food: " + foodcount));
        return foodcount;
    }

    // EFFECTS: returns the number of food items that are taken in the shelter
    // and log it as an event 
    public int getNumOfTakenFood() {
        int count = 0;
        for (Food food : fooditems) {
            if (food.isTaken()) {
                count++;
            }
        }
        EventLog.getInstance().logEvent(new Event("Num of taken food: " + count));

        return count;
    }

    // EFFECTS: saves the shelter to a file and log it as an event
    public void saveShelter(JsonWriter jsonWriter) {
        try {
            jsonWriter.open();
            jsonWriter.write(this); // Save the Shelter object to file
            jsonWriter.close();
            EventLog.getInstance().logEvent(new Event("Shelter data saved to file"));
        } catch (FileNotFoundException e) {
            EventLog.getInstance().logEvent(new Event("Failed to save shelter data"));
        }
    }

    // EFFECTS: loads the shelter from a file and log it as an event
    public void loadShelter(JsonReader jsonReader) {
        try {
            Shelter loadedShelter = jsonReader.read(); // Read the shelter from the file
            this.fooditems = loadedShelter.getFoodItems();
            EventLog.getInstance().logEvent(new Event("Shelter data loaded from file"));
        } catch (FileNotFoundException e) {
            EventLog.getInstance().logEvent(new Event("Failed to load shelter data"));
        } catch (IOException e) {
            EventLog.getInstance().logEvent(new Event("Error loading shelter from file: " + e.getMessage()));

        }

    }

   

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("foods", foodsToJson());
        return json;
    }

    // EFFECTS: returns foods in this shelter as a JSON array
    private JSONArray foodsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Food t : fooditems) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

}
