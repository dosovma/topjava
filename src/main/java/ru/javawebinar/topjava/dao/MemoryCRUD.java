package ru.javawebinar.topjava.dao;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.concurrent.CopyOnWriteArrayList;

public interface MemoryCRUD {
    void create(Meal meal);

    Meal read(Long id);

    void update(Long id, LocalDateTime dateTime, String description, int calories);

    void delete(Long id);

    CopyOnWriteArrayList<Meal> getMeals();
}
