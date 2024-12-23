package model;

import java.time.LocalDate;

import org.json.JSONObject;

import persistence.Writable;

//Represent a food item with name, expire date,
//allergy status and availability status
public class Food implements Writable {
    private String name;
    private LocalDate expireDate;
    private boolean isAllergenic;
    private boolean isTaken;


    // EFFECTS: Constructs a food item with a default status that it does not cause allergies.
    public Food(String name, LocalDate expireDate, boolean isTaken) {
        this.name = name;
        this.expireDate = expireDate;
        this.isAllergenic = false;
        this.isTaken = isTaken;
    }
    
    //EFFECTS: returns the name of a food item
    public String getName() {
        return this.name;
    }
  
    
    //EFFECTS: returns true if a food item is expired,
    //otherwise returns false
    public boolean isExpired() {
        LocalDate today = LocalDate.now();
        return today.isAfter(expireDate);
    } 

    //EFFECTS: return the expire date of a food item
    public LocalDate getExpireDate() {
        return this.expireDate;
    }

    //MODIFIES: this
    //EFFECTS: Sets the allergenic status of a food item. 
    //Returns true if the name contains "peanut"; 
    //otherwise, returns false.
    public void setAllergenic() {
        this.isAllergenic = this.name.contains("peanut");
        
    }

    //EFFECTS: returns the allergenic status of a food item
    public boolean getisAllergenic() {
        return this.isAllergenic;
    }

    //EFFECTS: returns the availability status
    public boolean isTaken() {
        return this.isTaken; 
    }

    //MODIFIES: this
    //EFFECTS: mark the food item as taken; 
    //if the food item is taken, return "unavailable", 
    //otherwise, return "in stock"
    public String markAsTaken() {
        if (this.isTaken()) {
            return "unavailable";
        } else {
            this.isTaken = true;
            return "in stock";
        }
        
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("name", name);
        json.put("isTaken", isTaken);
        json.put("expireDate", expireDate);
        json.put("isAllergenic", getisAllergenic());
        return json;
    }

}







