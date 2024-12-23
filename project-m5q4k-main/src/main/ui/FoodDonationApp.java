package ui;

import model.Food;
import model.Shelter;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import java.time.LocalDate;

//Represents an Food Donation Application 
public class FoodDonationApp {
    private static final String JSON_STORE = "./data/shelter.json";
    private Shelter shelter;
    private Scanner input;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    // EFFECTS: runs the food donation application
    public FoodDonationApp() throws FileNotFoundException {
        shelter = new Shelter();
        input = new Scanner(System.in);
        // input.useDelimiter("\r?\n|\r");
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        runApp();
    }

    // MODIFIES: this
    // EFFECTS: processes user input
    private void runApp() {
        boolean keepGoing = true;
        String command;

        while (keepGoing) {
            displayMenu();
            command = input.next().toLowerCase();

            if (command.equals("q")) {
                keepGoing = false;
            } else {
                processCommand(command);
            }
        }

        System.out.println("\nGoodbye!");
    }

    // MODIFIES: this
    // EFFECTS: processes user command
    private boolean processCommand(String command) {
        if (command.equals("add")) {
            addFood();
        } else if (command.equals("remove")) {
            removeFood();
        } else if (command.equals("clear")) {
            clearExpiredFood();
        } else if (command.equals("mark")) {
            markAsTaken();
        } else if (command.equals("save")) {
            saveShelter();
        } else if (command.equals("load")) {
            loadShelter();
        } else if (command.equals("list")) {
            listFood();
        } else if (command.equals("stats")) {
            showStats();
        } else if (command.equals("quit")) {
            System.exit(0);
        } else {
            System.out.println("Selection not valid...");
        }
        return false;
    }

    // EFFECTS: displays menu of options to user
    private void displayMenu() {
        System.out.println("\nSelect from:");
        System.out.println("\tadd -> add food");
        System.out.println("\tremove -> remove food");
        System.out.println("\tmark -> mark food as taken");
        System.out.println("\tclear -> clear expired food");
        System.out.println("\tlist -> list all food items");
        System.out.println("\tstats -> show statistics");
        System.out.println("\tsave -> save shelter  to file");
        System.out.println("\tload -> load shelter from file");
        System.out.println("\tquit -> quit");

    }

    // MODIFIES: this
    // EFFECTS: adds food item to the shelter
    private void addFood() {
        input.nextLine();
        System.out.println("Enter food name: ");
        String name = input.nextLine();

        System.out.println("Enter expiration date (YYYY-MM-DD): ");
        String date = input.next();
        LocalDate expireDate = LocalDate.parse(date);

        // Create a new Food item and add it to the shelter
        Food food = new Food(name, expireDate, false);
        food.setAllergenic();
        shelter.addFood(food);
        System.out.println("Food added: " + name);
    }

    // MODIFIES: this
    // EFFECTS: removes food item from the shelter
    private void removeFood() {
        input.nextLine();
        System.out.print("Enter food name to remove: ");
        String name = input.nextLine();

        System.out.print("Enter expiration date (YYYY-MM-DD): ");
        String date = input.next();
        LocalDate expireDate = LocalDate.parse(date);

        Food foodToRemove = null;

        // Find the food item to remove based on both name and expiration date
        for (Food food : shelter.getFoodItems()) {
            if (food.getName().equalsIgnoreCase(name) && food.getExpireDate().equals(expireDate)) {
                foodToRemove = food;
                break;
            }
        }

        if (foodToRemove != null) {
            shelter.removeFood(foodToRemove);
            System.out.println("Food removed: " + name + " with expiration date: " + date);
        } else {
            System.out.println("Food item not found.");
        }
    }

    // MODIFIES: this
    // EFFECTS: marks a food item as taken
    private void markAsTaken() {
        System.out.print("Enter food name to mark as taken: ");
        String name = input.next();
        Food foodToMark = null;

        // Find the food item to mark
        for (Food food : shelter.getFoodItems()) {
            if (food.getName().equalsIgnoreCase(name)) {
                foodToMark = food;
                break;
            }
        }

        if (foodToMark != null) {
            String status = foodToMark.markAsTaken();
            System.out.println("Food marked as taken: " + status);
        } else {
            System.out.println("Food item not found.");
        }
    }

    // MODIFIES: this
    // EFFECTS: clears expired food items
    private void clearExpiredFood() {
        shelter.clearFood();
        System.out.println("Expired food items cleared.");
    }

    // EFFECTS: lists all food items in the shelter
    private void listFood() {
        if (shelter.getNumOfFood() == 0) {
            System.out.println("No food items in the shelter.");
        } else {
            System.out.println("Food items in the shelter:");
            for (Food food : shelter.getFoodItems()) {
                System.out.printf("Name: %s, Expiry Date: %s, Taken: %s, Allergenic: %s\n",
                        food.getName(),
                        food.getExpireDate(),
                        food.isTaken() ? "Yes" : "No",
                        food.getisAllergenic() ? "Yes" : "No");
            }
        }
    }

    // EFFECTS: shows the number of food items and the number of taken food items
    private void showStats() {
        int totalFood = shelter.getNumOfFood();
        int takenFood = shelter.getNumOfTakenFood();

        System.out.printf("Total food items in shelter: %d\n", totalFood);
        System.out.printf("Total taken food items: %d\n", takenFood);
    }

    // EFFECTS: saves the shelter to file
    private void saveShelter() {
        try {
            jsonWriter.open();
            jsonWriter.write(shelter);
            jsonWriter.close();
            System.out.println("Saved " + "shelter" + " to " + JSON_STORE);
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    // MODIFIES: this
    // EFFECTS: loads shelter from file
    private void loadShelter() {
        try {
            shelter = jsonReader.read();
            System.out.println("Loaded " + "shelter" + " from " + JSON_STORE);
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE);
        }
    }

}