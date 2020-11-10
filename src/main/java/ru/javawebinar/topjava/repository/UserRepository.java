package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;

import java.util.List;

public interface UserRepository {
    // null if not found, when updated
    User save(User user);

    // false if not found
    boolean delete(int id);

    // null if not found
    User get(int id);

    // null if not found
    User getByEmail(String email);

    List<User> getAll();

    default List<Meal> getUserMeals(int id) {
        throw new UnsupportedOperationException("This method only for Data JPA");
    }

    default List<Meal> getUserMealsJOIN(int id) {
        throw new UnsupportedOperationException("This method only for Data JPA");
    }
}