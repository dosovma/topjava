package ru.javawebinar.topjava.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static ru.javawebinar.topjava.MealTestData.*;

@ContextConfiguration({
        "classpath:spring/spring-app.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    @Autowired
    MealService service;

    @Test
    public void create() {
        Meal newMeal = getNew();
        Meal mealDB = service.create(newMeal, USER_ID);
        int newId = mealDB.getId();
        newMeal.setId(newId);

        assertThat(mealDB).usingRecursiveComparison().ignoringFields("id").isEqualTo(getNew());
        assertThat(service.get(newId, USER_ID)).usingRecursiveComparison().ignoringFields("id").isEqualTo(getNew());
//        assertEquals(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void delete() {
        service.delete(userMeal1.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(userMeal1.getId(), USER_ID));
    }

    @Test
    public void get() {
        Meal actual = service.get(MEAL_ID, USER_ID);
        Meal expected = userMeal1;
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    public void update() {
        Meal updated = getUpdated(userMeal1);
        service.update(updated, USER_ID);
        assertThat(service.get(userMeal1.getId(), USER_ID)).usingRecursiveComparison().isEqualTo(getUpdated(userMeal1));
    }

    @Test
    public void getNotOwner() {
        assertThrows(NotFoundException.class, () -> {
            service.get(userMeal1.getId(), NOT_OWNER_USER_MEAL);
        });
    }

    @Test
    public void deleteNotOwner() {
        assertThrows(NotFoundException.class, () -> {
            service.delete(userMeal1.getId(), NOT_OWNER_USER_MEAL);
        });
    }

    @Test
    public void getBetweenInclusiveNull() {
        List<Meal> expected = new ArrayList<>(Arrays.asList(userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6, userMeal7, userMeal8));
//        expected.add(userMeal1);
        expected.sort(Comparator.comparing(Meal::getDate).thenComparing(Meal::getTime).reversed());
        List<Meal> actual = service.getBetweenInclusive(null, null, USER_ID);
//        actual.sort(Comparator.comparing(Meal::getId));
        assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        List<Meal> expected = new ArrayList<>(Arrays.asList(userMeal1, userMeal2, userMeal3, userMeal4, userMeal5, userMeal6, userMeal7, userMeal8));
//        expected.add(MEAL);
        expected.sort(Comparator.comparing(Meal::getDate).thenComparing(Meal::getTime).reversed());
        List<Meal> actual = service.getAll(USER_ID);
        assertEquals(expected, actual);
    }

    @Test
    public void updateNotOwner() {
        assertThrows(NotFoundException.class, () -> {
            service.update(userMeal1, NOT_OWNER_USER_MEAL);
        });
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () -> {
            service.create(new Meal(userMeal1.getDateTime(), "Завтрак на ту же дату", 500), USER_ID);
        });
    }
}