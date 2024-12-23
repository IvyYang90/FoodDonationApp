package persistence;

import model.Food;
import model.Shelter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            Shelter shelter = new Shelter();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected");
        } catch (IOException e) {
            // pass
        }

    }
    

    @Test
    void testWriterEmptyShelter() {
        try {
            Shelter shelter = new Shelter();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyShelter.json");
            writer.open();
            writer.write(shelter);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyShelter.json");
            shelter = reader.read();
            assertEquals(0, shelter.getNumOfFood());
            assertEquals(0, shelter.getNumOfTakenFood());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            Shelter shelter = new Shelter();
            shelter.addFood(new Food("apple", LocalDate.of(2025, 12, 27), false));
            shelter.addFood(new Food("peanut icecream", LocalDate.of(2023, 01, 05), true));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralShelter.json");
            writer.open();
            writer.write(shelter);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralShelter.json");
            shelter = reader.read();
            List<Food> foods = shelter.getFoodItems();
            assertEquals(2, foods.size());
            checkFood("apple", LocalDate.of(2025, 12, 27), false, foods.get(0));
            checkFood("peanut icecream", LocalDate.of(2023, 01, 05), true, foods.get(1));
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}