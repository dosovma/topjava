package ru.javawebinar.topjava.model;

import ru.javawebinar.topjava.util.MyBoolean;

import java.time.LocalDateTime;

public class UserMealWithExcess {
    private final LocalDateTime dateTime;

    private final String description;

    private final int calories;

    private MyBoolean excess;

    public UserMealWithExcess(UserMeal userMeal, MyBoolean excess) {
        this.dateTime = userMeal.getDateTime();
        this.description = userMeal.getDescription();
        this.calories = userMeal.getCalories();
        this.excess = excess;
    }

    public int getCalories() {
        return calories;
    }

    @Override
    public String toString() {
        return "UserMealWithExcess{" +
                "dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                ", excess=" + excess.getValue() +
                '}';
    }
}
