package ru.javawebinar.topjava.dao;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.slf4j.LoggerFactory.getLogger;

public class MemoryCRUDimpl implements MemoryCRUD {

    private CopyOnWriteArrayList<Meal> meals = MealsUtil.mealsList;

    private static final Logger log = getLogger(MemoryCRUD.class);

    @Override
    public void create(Meal meal) {
        meals.add(meal);
        log.debug(String.format("create new meal with id = %d", meal.getId()));
    }

    @Override
    public void update(Long id, LocalDateTime dateTime, String description, int calories) {
        delete(id);
        Meal meal = new Meal(id, dateTime, description, calories);
        meals.add(meal);
        log.debug(String.format("update meal with id = %d", meal.getId()));
    }

    @Override
    public void delete(Long id) {
        Meal mealToDelete = read(id);
        if (mealToDelete != null) {
            meals.remove(mealToDelete);
            log.debug(String.format("delete meal with id = %d", id));
        }
    }

    public Meal read (Long id) {
        for (Meal meal : meals) {
            if (meal.getId() == id) return meal;
        }
        return null;
    }

    public CopyOnWriteArrayList<Meal> getMeals() {
        return meals;
    }
}
