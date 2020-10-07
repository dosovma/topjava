package ru.javawebinar.topjava.controller;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import java.util.concurrent.CopyOnWriteArrayList;

public class memoryCRUDimp implements memoryCRUD {

    private CopyOnWriteArrayList<Meal> meals = new CopyOnWriteArrayList<>(MealsUtil.mealsList);

    @Override
    public void create() {

    }

    @Override
    public void read() {

    }

    @Override
    public void update() {

    }

    @Override
    public void delete() {

    }
}
