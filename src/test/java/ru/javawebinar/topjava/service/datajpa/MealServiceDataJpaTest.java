package ru.javawebinar.topjava.service.datajpa;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.test.context.ActiveProfiles;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.Profiles;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.service.MealServiceTest;

@ActiveProfiles(Profiles.DATAJPA)
public class MealServiceDataJpaTest extends MealServiceTest {

    @Override
    @Test
    public void getUserByMeal() {
        User actual = service.getUserByMeal(MealTestData.MEAL1_ID, UserTestData.USER_ID);
        UserTestData.USER_MATCHER.assertMatch(actual, UserTestData.user);
    }

    @Test
    public void getUserByMealNotFound() {
        Assert.assertNull(service.getUserByMeal(MealTestData.MEAL1_ID, UserTestData.ADMIN_ID));
    }
}
