package ui;

import model.*;
import model.Event;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

//Represents the graphical Food Donation Application
public class FoodDonationGUI extends JFrame {

    private static final String JSON_STORE = "./data/shelter.json";
    private Shelter shelter;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    private static final int WIDTH = 600;
    private static final int HEIGHT = 400;

    private JTabbedPane tabbedPane;

    // EFFECTS: run the FoodDonationGUI program
    public static void main(String[] args) {
        new FoodDonationGUI();
    }

    // EFFECTS: constructs FoodDonationGUI, sets up buttons for managing food items
    // on the shelter; close the window by printing all the events and saving the food items on the shelter
    public FoodDonationGUI() {
        super("FoodDonation Application");

        shelter = new Shelter();
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);

        setSize(WIDTH, HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        ImagePanel backgroundPanel = new ImagePanel("/Applications/foodDonationImage/FoodDonations_PatinaCatering.jpg");
        setContentPane(backgroundPanel); // Set the background panel

        loadTabs();
        promptLoadShelter(); // Prompt to load data when the application starts

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                printLog(EventLog.getInstance());
                promptSaveShelter(); // Prompt to save data when closing the window

            }
        });

        setVisible(true);

    }

    // EFFECTS: adds home tab, report tab and settings tab
    private void loadTabs() {
        tabbedPane = new JTabbedPane();

        JPanel homeTab = createHomeTab();
        tabbedPane.addTab("Home", homeTab);

        JPanel reportTab = createReportTab();
        tabbedPane.addTab("Report", reportTab);

        JPanel settingsTab = createSettingsTab();
        tabbedPane.addTab("Settings", settingsTab);

        add(tabbedPane, BorderLayout.CENTER);
    }

    // EFFECTS: creates the Home tab with buttons
    private JPanel createHomeTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        // Display the list of food items
        JTextArea foodListArea = new JTextArea(10, 30);
        foodListArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(foodListArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JPanel northAndSouthPanels = createInputAndButtonPanels(foodListArea);
        panel.add(northAndSouthPanels, BorderLayout.NORTH);

        return panel;
    }

    // EFFECTS: create View, Add, Remove, Mark, Clear button
    private JPanel createInputAndButtonPanels(JTextArea foodListArea) {
        JPanel combinedPanel = new JPanel(new BorderLayout());

        JPanel inputPanel = new JPanel(new GridLayout(3, 2));
        inputPanel.add(new JLabel("Food Name:"));
        JTextField foodNameField = new JTextField();
        inputPanel.add(foodNameField);

        inputPanel.add(new JLabel("Expiration Date (YYYY-MM-DD):"));
        JTextField expirationDateField = new JTextField();
        inputPanel.add(expirationDateField);

        combinedPanel.add(inputPanel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(1, 5));
        buttonPanel.add(createButton("Add Food", e -> addFood(foodNameField, expirationDateField, foodListArea)));
        buttonPanel.add(createButton("Remove Food", e -> removeFood(foodNameField, expirationDateField, foodListArea)));
        buttonPanel.add(createButton("Mark as Taken", e -> markAsTaken(foodNameField, foodListArea)));
        buttonPanel.add(createButton("Clear Expired", e -> clearExpiredFood(foodListArea)));

        combinedPanel.add(buttonPanel, BorderLayout.SOUTH);

        return combinedPanel;
    }

    // EFFECTS: creates a JButton with the given text and action
    private JButton createButton(String text, ActionListener action) {
        JButton button = new JButton(text);
        button.addActionListener(action);
        return button;
    }

    // EFFECTS: creates the Report tab with the numbers of all of the Food items and
    // taken food items
    private JPanel createReportTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JTextArea statsArea = new JTextArea(10, 30);
        statsArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(statsArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        JButton statsButton = new JButton("Show Stats");
        statsButton.addActionListener(e -> showStats(statsArea));
        panel.add(statsButton, BorderLayout.SOUTH);

        return panel;
    }

    // EFFECTS: creates the Settings tab with filter button
    private JPanel createSettingsTab() {
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        JButton filterButton = new JButton("Filter Taken Food");
        filterButton.addActionListener(e -> filterTakenFood());

        panel.add(filterButton, BorderLayout.SOUTH);

        JTextArea foodList = new JTextArea(10, 30);
        foodList.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(foodList);
        panel.add(scrollPane, BorderLayout.CENTER); // JTextArea for filter

        return panel;
    }

    // EFFECTS: adds food to the shelter with name and expiration date
    private void addFood(JTextField foodNameField, JTextField expirationDateField, JTextArea foodListArea) {
        String name = foodNameField.getText();
        String date = expirationDateField.getText();
        LocalDate expireDate = LocalDate.parse(date); // add new food with provided text

        Food food = new Food(name, expireDate, false);
        food.setAllergenic();
        shelter.addFood(food);

        listFood(); // update the list

    }

    // EFFECTS: removes food from the shelter with the same name and the same
    // expiration date; if there is no food items, leave the message "Food item not
    // found."
    private void removeFood(JTextField foodNameField, JTextField expirationDateField, JTextArea foodListArea) {
        String name = foodNameField.getText();
        String date = expirationDateField.getText();
        LocalDate expireDate = LocalDate.parse(date);

        Food foodToRemove = null;
        for (Food food : shelter.getFoodItems()) {
            if (food.getName().equalsIgnoreCase(name) && food.getExpireDate().equals(expireDate)) {
                foodToRemove = food;
                break;
            }
        }

        if (foodToRemove != null) {
            shelter.removeFood(foodToRemove);
            listFood(); // Refresh food list
        } else {
            JOptionPane.showMessageDialog(this, "Food item not found.");
        }
    }

    // EFFECTS: marks food as taken; if there is no given food item, leave the
    // message "Food item not found."
    private void markAsTaken(JTextField foodNameField, JTextArea foodListArea) {
        String name = foodNameField.getText();
        Food foodToMark = null;

        for (Food food : shelter.getFoodItems()) {
            if (food.getName().equalsIgnoreCase(name)) {
                foodToMark = food;
                break;
            }
        }

        if (foodToMark != null) {
            foodToMark.markAsTaken();
            listFood(); // Refresh food list
        } else {
            JOptionPane.showMessageDialog(this, "Food item not found.");
        }
    }

    // EFFECTS: clears expired food from the shelter
    private void clearExpiredFood(JTextArea foodListArea) {
        shelter.clearFood();
        listFood(); // Refresh food list
    }

    // EFFECTS: shows the number of total food and taken food items
    private void showStats(JTextArea statsArea) {
        int totalFood = shelter.getNumOfFood();
        int takenFood = shelter.getNumOfTakenFood();

        statsArea.setText(""); // Clear stats area
        statsArea.append(String.format("Total food items in shelter: %d\n", totalFood));
        statsArea.append(String.format("Total taken food items: %d\n", takenFood));
    }

    // EFFECTS: returns the JTextArea in the Settings tab for displaying taken food
    private JTextArea getFoodListForSettings() {
        JPanel settingsTab = (JPanel) tabbedPane.getComponentAt(2); // Settings tab at index 2
        JScrollPane scrollPane = (JScrollPane) settingsTab.getComponent(1); // JScrollPane at index 1
        JTextArea foodList = (JTextArea) scrollPane.getViewport().getView();
        return foodList;
    }

    // EFFECTS: returns the JTextArea that displays the list of food items
    private JTextArea getFoodList() {
        JPanel homeTab = (JPanel) tabbedPane.getComponentAt(0); // Home tab
        JTextArea foodList = (JTextArea) ((JScrollPane) homeTab.getComponent(0)).getViewport().getView();
        return foodList;
    }

    // EFFECTS: filters and display only the taken food items
    private void filterTakenFood() {
        List<Food> availableFood = new ArrayList<>();
        for (Food food : shelter.getFoodItems()) {
            if (!food.isTaken()) {
                availableFood.add(food);
            }
        }
        listTakenFood(availableFood);
    }

    // EFFECTS: lists only the taken food items in the shelter; if there is no food
    // items, leave the message "No food items have been taken."
    private void listTakenFood(List<Food> foodItems) {
        JTextArea foodList = getFoodListForSettings();
        foodList.setText(""); // Clear existing content

        if (foodItems.isEmpty()) {
            foodList.append("No food items available.");
        } else {
            for (Food food : foodItems) {
                foodList.append(String.format("Name: %s, Expiry Date: %s, Taken: %s, Allergic: %s\n",
                        food.getName(),
                        food.getExpireDate(),
                        food.isTaken() ? "Yes" : "No",
                        food.getisAllergenic() ? "Yes" : "No"));
            }
        }
    }

    // EFFECTS:lists all food items in the shelter
    private void listFood() {
        JTextArea foodList = getFoodList();
        foodList.setText(""); // Clear existing list
        for (Food food : shelter.getFoodItems()) {
            foodList.append(String.format("Name: %s, Expiry Date: %s, Taken: %s, Allergic: %s\n",
                    food.getName(),
                    food.getExpireDate(),
                    food.isTaken() ? "Yes" : "No",
                    food.getisAllergenic() ? "Yes" : "No"));
        }
    }

    // EFFECTS: prompts user to load shelter when the app starts; if choose yes,
    // then load the shelter; otherwise do not load
    private void promptLoadShelter() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to load data from file?",
                "Load Shelter Data", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            shelter.loadShelter(jsonReader);
            listFood();
        }
    }

    // EFFECTS: prompts user to save shelter data when the app ends; if choose yes,
    // then save the shelter; otherwise do not save
    private void promptSaveShelter() {
        int choice = JOptionPane.showConfirmDialog(this, "Do you want to save data to file?",
                "Save Shelter Data", JOptionPane.YES_NO_OPTION);
        if (choice == JOptionPane.YES_OPTION) {
            shelter.saveShelter(jsonWriter);
        }
    }

    //EFFECTS: prints the list of all the events
    public void printLog(EventLog el) {
        for (Event next : el) {
            System.out.println(next.toString());
        }
    }

}
