package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;

import static ru.javawebinar.topjava.model.AbstractBaseEntity.START_SEQ;

public class MealTestData {

    public static final int MEAL_ID = START_SEQ + 2;

    public static final int USER_ID = UserTestData.USER_ID;

    public static final int NOT_OWNER_USER_MEAL = UserTestData.ADMIN_ID;

    public static Meal userMeal1 = new Meal(100002, LocalDateTime.of(2020, Month.OCTOBER, 19, 10, 0), "Завтрак", 500);
    public static Meal userMeal2 = new Meal(100003, LocalDateTime.of(2020, Month.OCTOBER, 19, 13, 0), "Обед", 1000);
    public static Meal userMeal3 = new Meal(100004, LocalDateTime.of(2020, Month.OCTOBER, 19, 20, 0), "Ужин", 500);
    public static Meal userMeal4 = new Meal(100005, LocalDateTime.of(2020, Month.OCTOBER, 20, 0, 0), "Еда на граничное значение", 100);
    public static Meal userMeal5 = new Meal(100006, LocalDateTime.of(2020, Month.OCTOBER, 20, 10, 0), "Завтрак", 1000);
    public static Meal userMeal6 = new Meal(100007, LocalDateTime.of(2020, Month.OCTOBER, 20, 13, 0), "Обед", 500);
    public static Meal userMeal7 = new Meal(100008, LocalDateTime.of(2020, Month.OCTOBER, 20, 20, 0), "Обед", 410);
    public static Meal userMeal8 = new Meal(100009, LocalDateTime.of(2020, Month.OCTOBER, 19, 10, 30), "Завтрак", 500);

    public static Meal getNew() {
        return new Meal(null, LocalDateTime.of(2020, Month.OCTOBER, 19, 20, 30), "Новая еда для теста", 500);
    }

    public static Meal getUpdated(Meal meal) {
        Meal updated = new Meal(meal);
        updated.setDescription("Updated meal");
        updated.setDateTime(LocalDateTime.of(2020, Month.OCTOBER, 24, 10, 0));
        updated.setCalories(1750);
        return updated;
    }
}
