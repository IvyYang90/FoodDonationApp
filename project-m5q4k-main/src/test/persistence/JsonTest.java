package persistence;

import model.Food;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;


public class JsonTest {
    protected void checkFood(String name, LocalDate expireDate, boolean isTaken, Food food) {
        assertEquals(name, food.getName());
        assertEquals(expireDate, food.getExpireDate());
        assertEquals(isTaken, food.isTaken());
    }
}
