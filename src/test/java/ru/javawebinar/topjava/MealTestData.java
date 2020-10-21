package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;
import java.util.List;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static int MEAL_ID = START_SEQ + 2;

    public static final List<Meal> mealsUser = Arrays.asList(
            new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 19, 10, 0), "Завтрак", 500),
            new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 19, 13, 0), "Обед", 1000),
            new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 19, 20, 0), "Ужин", 500),
            new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 20, 0, 0), "Еда на граничное значение", 100),
            new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 20, 10, 0), "Завтрак", 1000),
            new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 20, 13, 0), "Обед", 500),
            new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 20, 20, 0), "Ужин", 410)
    );

    public static Meal MEAL = new Meal(MEAL_ID++, LocalDateTime.of(2020, Month.OCTOBER, 19, 10, 30), "Завтрак", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.OCTOBER, 19, 20, 30), "Новая еда для теста", 500);
    }

    public static Meal getUpdate(Meal meal) {
        Meal updated = new Meal(meal);
        updated.setDescription("Updated meal");
        return updated;
    }

    public static final int USER_ID = START_SEQ;

    public static final int NOT_OWNER_USER_MEAL = 300;

}
