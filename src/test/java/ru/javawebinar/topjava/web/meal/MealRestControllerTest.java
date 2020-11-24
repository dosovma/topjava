package ru.javawebinar.topjava.web.meal;

import org.junit.jupiter.api.Assertions;
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
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.javawebinar.topjava.MealTestData.*;
import static ru.javawebinar.topjava.TestUtil.*;

class MealRestControllerTest extends AbstractControllerTest {

    private final String REST_URL = MealRestController.REST_URL + "/";

    @Autowired
    private MealService service;

    @Test
    void getById() {
        Assertions.assertAll(
                () -> perform(delete(REST_URL + MEAL1_ID))
                        .andExpect(status().isUnsupportedMediaType()),
                () -> perform(get(REST_URL + MEAL1_ID).contentType("application/json"))
                        .andDo(print())
                        .andExpect(status().isOk())
                        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                        .andExpect(MEAL_MATCHER.contentJson(meal1))
        );
    }

    @Test
    void getByIdNotFound() throws Exception {
        perform(get(REST_URL + NOT_FOUND).contentType("application/json"))
                .andExpect(status().isNotFound());
    }

    @Test
    void deleteById() {
        Assertions.assertAll(
                () -> perform(delete(REST_URL + MEAL1_ID))
                        .andExpect(status().isUnsupportedMediaType()),
                () -> perform(delete(REST_URL + MEAL1_ID).contentType("application/json"))
                        .andExpect(status().isOk()),
                () -> perform(delete(REST_URL + MEAL1_ID).contentType("application/json"))
                        .andExpect(status().isNotFound())
        );
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
    void createBadRequest() throws Exception {
        Meal newMeal = MealTestData.getUpdated();
        perform(post(REST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(newMeal)))
                .andExpect(status().isBadRequest());
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
    void updateNotFound() throws Exception {
        perform(put(REST_URL + ADMIN_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(adminMeal1)))
                .andExpect(status().isNotFound());
    }

    @Test
    void updateBadRequest() throws Exception {
        perform(put(REST_URL + ADMIN_MEAL_ID)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtil.writeValue(MealTestData.getUpdated())))
                .andExpect(status().isBadRequest());
    }

    @Test
    void getAll() throws Exception {
        ResultActions action = perform(get(REST_URL).contentType("application/json"))
                .andExpect(status().isOk());

        List<MealTo> actual = readListFromJsonMvcResult(action.andReturn(), MealTo.class);
        List<MealTo> expected = MealsUtil.getTos(meals, SecurityUtil.authUserCaloriesPerDay());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void getBetween() throws Exception {
        ResultActions action = perform(get(REST_URL
                + "filter?"
                + "startDate=2020-01-31T10:15:30&endTime=2020-01-31T20:20:30")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andDo(print());

        List<MealTo> actual = readListFromJsonMvcResult(action.andReturn(), MealTo.class);
        List<MealTo> expected = MealsUtil.getTos(List.of(meal7, meal6, meal5, meal4), SecurityUtil.authUserCaloriesPerDay());
        assertThat(actual).usingRecursiveComparison().isEqualTo(expected);
    }
}