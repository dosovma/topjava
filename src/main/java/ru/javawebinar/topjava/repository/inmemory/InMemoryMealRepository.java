package ru.javawebinar.topjava.repository.inmemory;

import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Repository
public class InMemoryMealRepository implements MealRepository {
    private final Map<Integer, Meal> repository = new ConcurrentHashMap<>();
    private final AtomicInteger counter = new AtomicInteger(0);

    {
        MealsUtil.meals.forEach(meal -> save(meal, 1));
    }

    @Override
    public Meal save(Meal meal, Integer userId) {
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            repository.put(meal.getId(), meal);
            return meal;
        }
        // handle case: update, but not present in storage
        return checkUserId(meal.getId(), userId)
                ? repository.computeIfPresent(meal.getId(), (id, oldMeal) -> meal)
                : null;
    }

    @Override
    public boolean delete(int id, Integer userId) {
        return checkUserId(id, userId) && repository.remove(id) != null;
    }

    @Override
    public Meal get(int id, Integer userId) {
        return checkUserId(id, userId)
                ? repository.get(id)
                : null;
    }

    @Override
    public List<Meal> getAll(Integer userId) {
        return repository.values().stream()
                .filter(meal -> meal.getUserId().equals(userId))
                .sorted((meal1, meal2) -> (-1)*meal1.getDate().compareTo(meal2.getDate()))
                .collect(Collectors.toList());
    }

    public List<Meal> getAllByFilter(Integer userId, LocalDate startDate, LocalDate endDate, LocalTime startTime, LocalTime endTime) {
        Predicate<Meal> filterByDate = meal -> DateTimeUtil.isBetweenHalfOpenDate(meal.getDate(), startDate, endDate);
        Predicate<Meal> filterByTime = meal -> DateTimeUtil.isBetweenHalfOpenTime(meal.getDateTime().toLocalTime(), startTime, endTime);
        return getAll(userId).stream()
                .filter(filterByDate)
                .filter(filterByTime)
                .collect(Collectors.toList());
    }

    private boolean checkUserId(int id, Integer userId) {
        return repository.get(id) != null && repository.get(id).getUserId().equals(userId);
    }
}

