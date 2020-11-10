package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.UserServiceTest;

import java.util.List;

@ActiveProfiles(Profiles.DATAJPA)
public class UserServiceDataJpaTest extends UserServiceTest {

    @Test
    @Override
    public void getUserMeals() {
        List<Meal> expected = MealTestData.meals;
        List<Meal> actual = service.getUserMeals(UserTestData.USER_ID);
        MealTestData.MEAL_MATCHER.assertMatch(actual, expected);
    }

    @Test
    public void getUserMealNotFound() {
        List<Meal> actual = service.getUserMeals(UserTestData.NOT_FOUND);
        Assert.assertNull(actual);
    }

    @Test
    @Override
    public void getUserMealsJOIN() {
        List<Meal> expected = MealTestData.meals;
        List<Meal> actual = service.getUserMealsJOIN(UserTestData.USER_ID);
        MealTestData.MEAL_MATCHER.assertMatch(actual, expected);
    }

    @Test
    public void getUserMealsJOINNotFound() {
        List<Meal> actual = service.getUserMealsJOIN(UserTestData.USER_WITHOUT_MEAL_ID);
        Assert.assertNull(actual);
    }
}
