package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, meal.getUserId()));
    }

    @Override
    public Meal save(Meal meal, int userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            Map<Integer, Meal> map = repository.get(userId);
            int id = meal.getId();
            if (map == null) {
                map = new HashMap<>();
                repository.put(userId, map);
            }
            map.put(id, meal);
            return meal;
        }
        // handle case: update, but not present in storage
        int mealId = meal.getId();
        return isUserOwnerMeal(mealId, userId)
                ? repository.computeIfPresent(userId, (key, value) -> { value.put(mealId, meal); return value;}).get(mealId)
                : null;
    }

    @Override
    public boolean delete(int id, int userId) {
        return isUserOwnerMeal(id, userId) && repository.get(userId).remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        return isUserOwnerMeal(id, userId)
                ? repository.get(userId).get(id)
                : null;
    }

    @Override
    public List<Meal> getAll(int userId) {
        return repository.get(userId).values().stream()
                .sorted(Comparator.comparing(Meal::getDate).thenComparing(Meal::getTime).reversed())
                .collect(Collectors.toList());
    }

    public List<Meal> getAllByFilter(int userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        Predicate<Meal> filterByDate = meal -> DateTimeUtil.isBetweenHalfOpenGenerics(meal.getDate(), startDate, endDate);
        Predicate<Meal> filterByTime = meal -> DateTimeUtil.isBetweenHalfOpenGenerics(meal.getDateTime().toLocalTime(), startTime, endTime);
        return repository.get(userId).values().stream()
                .filter(filterByDate)
                .filter(filterByTime)
                .collect(Collectors.toList());
    }

    private boolean isUserOwnerMeal(int id, int userId) {
        Map<Integer, Meal> userMeals = repository.get(userId);
        if (userMeals == null) return false;
        Meal meal = userMeals.get(id);
        if (meal == null) return false;
        return meal.getUserId() == userId;
    }
}

