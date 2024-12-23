package modeltest;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import model.Food;

public class FoodTest {
    private Food foodtest1;
    private Food foodtest2;
    
    @BeforeEach
    void runBefore() {
        foodtest1 = new Food("pepperoni pizza",LocalDate.of(2025, 12, 02), false);
        foodtest2 = new Food("peanut bread",LocalDate.of(2025,11,02), true);


    }

    @Test
    void testConstructor() {
        assertEquals("pepperoni pizza", foodtest1.getName());
        assertEquals(LocalDate.of(2025,12,02), foodtest1.getExpireDate());
        assertFalse(foodtest1.getisAllergenic());
        assertFalse(foodtest1.isTaken());
        assertTrue(foodtest2.isTaken());

    }

    @Test
    void testsetAllergenic() {
        foodtest1.setAllergenic();
        assertFalse(foodtest1.getisAllergenic());
        foodtest2.setAllergenic();
        assertTrue(foodtest2.getisAllergenic());

    }

    @Test
    void testmarkAsTaken() {
        assertEquals("in stock", foodtest1.markAsTaken());
        assertEquals("unavailable", foodtest2.markAsTaken());
        assertTrue(foodtest1.isTaken());
        assertTrue(foodtest2.isTaken());
    }

}
