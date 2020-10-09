package ru.javawebinar.topjava.controller;

import org.slf4j.Logger;
import ru.javawebinar.topjava.dao.MemoryCRUD;
import ru.javawebinar.topjava.dao.MemoryCRUDimpl;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static org.slf4j.LoggerFactory.getLogger;

public class MealServlet extends HttpServlet {
    private static final Logger log = getLogger(MealServlet.class);
    private static final String INSERT_OR_EDIT = "/meal.jsp";
    private static final String LIST_MEAL = "/meals.jsp";
    private final MemoryCRUD dao = new MemoryCRUDimpl();
    private final DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("get GET request");

        String forward = LIST_MEAL;
        String action = request.getParameter("action");

        if (action != null) {
            if (action.equalsIgnoreCase("delete")) {
                long id = Long.parseLong(request.getParameter("id"));
                dao.delete(id);
                forward = LIST_MEAL;
                request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getMeals(), LocalTime.MIN, LocalTime.MAX, MealsUtil.caloriesPerDay));
                //response.sendRedirect("/meals");
            } else if (action.equalsIgnoreCase("edit")) {
                forward = INSERT_OR_EDIT;
                long id = Long.parseLong(request.getParameter("id"));
                Meal meal = dao.read(id);
                request.setAttribute("meal", meal);
            } else if (action.equalsIgnoreCase("add")) {
                forward = INSERT_OR_EDIT;
                request.setAttribute("meal", null);
            } else if (action.equalsIgnoreCase("listMeal")) {
                forward = LIST_MEAL;
                request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getMeals(), LocalTime.MIN, LocalTime.MAX, MealsUtil.caloriesPerDay));
            } else {
                forward = LIST_MEAL;
            }
        } else {
            request.setAttribute("meals", MealsUtil.filteredByStreams(dao.getMeals(), LocalTime.MIN, LocalTime.MAX, MealsUtil.caloriesPerDay));
        }

        log.debug("Forward meals.jps");
        request.getRequestDispatcher(forward).forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        log.debug("get POST request");

        req.setCharacterEncoding("UTF-8");

        String dateTimeStr = req.getParameter("dateTime");
        LocalDateTime dateTime = LocalDateTime.parse(dateTimeStr, formatter);
        String description = req.getParameter("description");
        int calories = Integer.parseInt(req.getParameter("calories"));
        String id = req.getParameter("id");

        if (!id.equals("")) {
            dao.update(Long.parseLong(id), dateTime, description, calories);
        } else {
            Meal meal = new Meal(dateTime, description, calories);
            dao.create(meal);
        }

        req.setAttribute("meals", MealsUtil.filteredByStreams(dao.getMeals(), LocalTime.MIN, LocalTime.MAX, MealsUtil.caloriesPerDay));
        log.debug("Forward meals.jps");
        req.getRequestDispatcher(LIST_MEAL).forward(req, resp);
    }
}
