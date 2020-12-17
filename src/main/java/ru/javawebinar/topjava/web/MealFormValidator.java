package ru.javawebinar.topjava.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;

import java.util.List;

@Component
public class MealFormValidator implements Validator {

    @Autowired
    private MealService service;

    @Override
    public boolean supports(Class<?> aClass) {
        return Meal.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Meal mealFromWeb = (Meal) o;
        if (mealFromWeb.getDateTime() == null) {
            return;
        }

        Integer userId = SecurityUtil.authUserId();

        List<Meal> mealList = service.getBetweenInclusive(mealFromWeb.getDate(), mealFromWeb.getDate(), userId);
        if (!mealList.isEmpty()) {
            for (Meal meal : mealList) {
                if (meal.getDateTime().equals(mealFromWeb.getDateTime())) {
                    errors.rejectValue("dateTime", "duplicate dateTime", "You have already had meal with the same dateTime");
                }
            }
        }
    }
}
