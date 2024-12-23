package ui;

import java.io.FileNotFoundException;

//Runs the foodDonationApp program
public class Main {
    public static void main(String[] args) {
        try {
            new FoodDonationApp();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to run application: file not found");
        }
    }
}
