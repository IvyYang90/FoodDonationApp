package persistence;

import model.Food;
import model.Shelter;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class JsonReaderTest extends JsonTest {

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            Shelter shelter = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyShelter() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyShelter.json");
        try {
            Shelter shelter = reader.read();
            assertEquals(0, shelter.getNumOfFood());
            assertEquals(0, shelter.getNumOfTakenFood());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralShelter() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralShelter.json");
        try {
            Shelter shelter = reader.read();
            List<Food> foods = shelter.getFoodItems();
            assertEquals(2, foods.size());
            checkFood("apple", LocalDate.of(2025, 12, 27), false, foods.get(0));
            checkFood("peanut icecream", LocalDate.of(2023, 01, 05), true, foods.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

}
