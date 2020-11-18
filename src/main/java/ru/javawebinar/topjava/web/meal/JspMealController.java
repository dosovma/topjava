package ru.javawebinar.topjava.web.meal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Map;

import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalDate;
import static ru.javawebinar.topjava.util.DateTimeUtil.parseLocalTime;

@Controller
@RequestMapping("/meals")
public class JspMealController extends AbstractMealController {

    @GetMapping("delete")
    public String deleteJsp(@RequestParam int id) {
        delete(id);
        return "redirect:/meals";
    }

    @PostMapping("*")
    public String createUpdate(@RequestParam(required = false) Map<String, String> mealParam) {
        Integer id = mealParam.get("id").isEmpty() ? null : Integer.parseInt(mealParam.get("id"));
        Meal meal = new Meal(
                id,
                LocalDateTime.parse(mealParam.get("dateTime")),
                mealParam.get("description"),
                Integer.parseInt(mealParam.get("calories")));
        if (id == null) {
            create(meal);
        } else {
            update(meal, id);
        }
        return "redirect:/meals";
    }

    @GetMapping("create")
    public String createForm(Model model) {
        model.addAttribute("meal", new Meal(LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES), "", 1000));
        return "/mealForm";
    }

    @GetMapping("update")
    public String updateForm(@RequestParam Integer id, Model model) {
        model.addAttribute("meal", get(id));
        return "/mealForm";
    }

    @GetMapping
    public String getAllJsp(Model model) {
        model.addAttribute("meals", getAll());
        return "/meals";
    }

    @GetMapping("filter")
    public String getBetween(@RequestParam Map<String, String> filterParam, Model model) {
        LocalDate startDate = parseLocalDate(filterParam.get("startDate"));
        LocalDate endDate = parseLocalDate(filterParam.get("endDate"));
        LocalTime startTime = parseLocalTime(filterParam.get("startTime"));
        LocalTime endTime = parseLocalTime(filterParam.get("endTime"));
        model.addAttribute("meals", getBetween(startDate, startTime, endDate, endTime));
        return "/meals";
    }
}
