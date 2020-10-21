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

import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public void create() throws Exception {
        Meal newMeal = getNew();
        Meal mealDB = service.create(newMeal, USER_ID);
        int newId = mealDB.getId();
        newMeal.setId(newId);

        assertEquals(mealDB, newMeal);
        assertEquals(service.get(newId, USER_ID), newMeal);
    }

    @Test
    public void delete() throws Exception {
        service.delete(MEAL.getId(), USER_ID);
        assertThrows(NotFoundException.class, () -> service.get(MEAL.getId(), USER_ID));
    }

    @Test
    public void update() throws Exception {
        Meal updated = getUpdate(MEAL);
        service.update(updated, USER_ID);
        assertEquals(service.get(MEAL.getId(), USER_ID), updated);
    }

    @Test
    public void getNotOwner() {
        assertThrows(NotFoundException.class, () -> { service.get(MEAL.getId(), NOT_OWNER_USER_MEAL); });
    }

    @Test
    public void deleteNotOwner() {
        assertThrows(NotFoundException.class, () -> { service.delete(MEAL.getId(), NOT_OWNER_USER_MEAL); });
    }

    @Test
    public void getBetweenInclusiveNull() {
        List<Meal> expected = new ArrayList<>(mealsUser);
        expected.add(MEAL);
        expected.sort(Comparator.comparing(Meal::getId));
        List<Meal> actual = service.getBetweenInclusive(null, null, USER_ID);
        actual.sort(Comparator.comparing(Meal::getId));
        assertEquals(expected, actual);
    }

    @Test
    public void getAll() {
        List<Meal> expected = new ArrayList<>(mealsUser);
        expected.add(MEAL);
        expected.sort(Comparator.comparing(Meal::getId));
        List<Meal> actual = service.getAll(USER_ID);
        actual.sort(Comparator.comparing(Meal::getId));
        assertEquals(expected, actual);
    }

    @Test
    public void updateNotOwner() {
        assertThrows(NotFoundException.class, () -> { service.update(MEAL, NOT_OWNER_USER_MEAL); });
    }

    @Test
    public void duplicateDateTimeCreate() {
        assertThrows(DataAccessException.class, () -> {
            service.create(new Meal(LocalDateTime.of(2020, Month.OCTOBER, 19, 10, 0), "Завтрак на ту же дату", 500), 100000);
        });
    }
}