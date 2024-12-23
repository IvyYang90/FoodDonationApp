
package modeltest;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Food;
import model.Shelter;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDate;


public class ShelterTest {
    private Shelter sheltertest;

    @BeforeEach
    void runBefore() {
        sheltertest = new Shelter();
    }

    @Test
    void testConstructor() {
        assertEquals(0, sheltertest.getNumOfFood());
        assertEquals(0, sheltertest.getNumOfTakenFood());
    }
    
    @Test
    void testaddSingleFood() {
        Food food1 = new Food("pepperoni pizza",LocalDate.of(2025, 12, 02), false);
        sheltertest.addFood(food1);
        assertEquals(1, sheltertest.getNumOfFood());
        assertEquals(0, sheltertest.getNumOfTakenFood());

    }

    @Test
    void testaddMultipleFood() {
        Food food1 = new Food("pepperoni pizza",LocalDate.of(2025, 12, 02), false);
        Food food2 = new Food("peanut bread",LocalDate.of(2025,11,02), true);
        sheltertest.addFood(food1);
        sheltertest.addFood(food2);
        assertEquals(2, sheltertest.getNumOfFood());
        assertEquals(1, sheltertest.getNumOfTakenFood());

    }

    @Test
    void testRemoveSingleFood() {
        Food food1 = new Food("pepperoni pizza",LocalDate.of(2025, 12, 02), false);
        sheltertest.addFood(food1);
        sheltertest.removeFood(food1);
        assertEquals(0, sheltertest.getNumOfFood());
        assertEquals(0, sheltertest.getNumOfTakenFood());

    }

    @Test
    void testRemoveMultipleFood() {
        Food food1 = new Food("pepperoni pizza",LocalDate.of(2025, 12, 02), false);
        Food food2 = new Food("peanut bread",LocalDate.of(2025,11,02), true);
        sheltertest.addFood(food1);
        sheltertest.addFood(food2);
        sheltertest.removeFood(food1);
        assertEquals(1, sheltertest.getNumOfFood());
        assertEquals(1, sheltertest.getNumOfTakenFood());

    }

    @Test
    void testclearFood() {
        Food food1 = new Food("pepperoni pizza",LocalDate.of(2025, 12, 02), false);
        Food food2 = new Food("peanut bread",LocalDate.of(2025,11,02), true);
        Food food3 = new Food("hot dog",LocalDate.of(2023,11,02),true);
        Food food4 = new Food("apple juice",LocalDate.of(2023,01,12),true);

        sheltertest.addFood(food1);
        sheltertest.addFood(food2);
        sheltertest.addFood(food3);
        sheltertest.addFood(food4);
        sheltertest.clearFood();
        assertEquals(2,sheltertest.getNumOfFood());
        assertEquals("pepperoni pizza", sheltertest.getFoodItems().get(0).getName());
        assertEquals("peanut bread", sheltertest.getFoodItems().get(1).getName());

    }

}
