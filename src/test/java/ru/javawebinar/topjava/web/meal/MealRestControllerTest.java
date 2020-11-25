package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.web.AbstractControllerTest;
import ru.javawebinar.topjava.web.SecurityUtil;
import ru.javawebinar.topjava.web.json.JsonUtil;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.*;

class MealRestControllerTest extends AbstractControllerTest {

    private final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService service;

    @Test
    void getById() throws Exception {
        perform(get(REST_URL + MEAL1_ID))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(MEAL_MATCHER.contentJson(meal1));
    }

    @Test
    void deleteById() throws Exception {
        perform(delete(REST_URL + MEAL1_ID))
                .andExpect(status().isNoContent());
    }

    @Test
    void create() throws Exception {
        Meal newMeal = MealTestData.getNew();
        ResultActions action = perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isCreated());

        Meal created = readFromJson(action, Meal.class);
        int newId = created.getId();
        newMeal.setId(newId);
        MEAL_MATCHER.assertMatch(created, newMeal);
        MEAL_MATCHER.assertMatch(service.get(newId, UserTestData.USER_ID), newMeal);
    }

    @Test
    void updateById() throws Exception {
        Meal updatedMeal = MealTestData.getUpdated();
        ResultActions action = perform(put(REST_URL + MEAL1_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(updatedMeal)))
                .andExpect(status().isOk());

        Meal actual = readFromRequestJson(action, Meal.class);
        MEAL_MATCHER.assertMatch(actual, getUpdated());
    }

    @Test
    void getAll() throws Exception {
        ResultActions action = perform(get(REST_URL))
                .andExpect(status().isOk());

        List<MealTo> actual = readListFromJsonMvcResult(action.andReturn(), MealTo.class);
        List<MealTo> expected = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void getBetween() throws Exception {
        ResultActions action = perform(get(REST_URL
                + "filter?"
                + "startDate=2020-01-31&endTime=20:20:30"))
                .andExpect(status().isOk());

        List<MealTo> actual = readListFromJsonMvcResult(action.andReturn(), MealTo.class);
        List<MealTo> expected = MealsUtil.getTos(List.of(meal7, meal6, meal5, meal4), SecurityUtil.authUserCaloriesPerDay());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}