package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMealWithExcess;

import java.util.ArrayList;
import java.util.List;

public class MyList<T extends UserMealWithExcess> {

    private List<T> list;

    private int size;

    private final MyBoolean excess;

    private int maxCalories;

    public MyList(int maxCalories) {
        list = new ArrayList<>();
        size = 0;
        this.maxCalories = maxCalories;
        excess = new MyBoolean();
    }

    public void add(T t) {
        if (list.add(t)) {
            size += t.getCalories();
            if (size > maxCalories) excess.changeMyBooleanValue();
        }
    }

    public MyBoolean getExcess() {
        return excess;
    }

    public List<T> getList() {
        return list;
    }
}
