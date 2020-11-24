package ru.javawebinar.topjava.web.meal;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.to.MealTo;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping(value = MealRestController.REST_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
public class MealRestController extends AbstractMealController {

    static final String REST_URL = "/rest/meals";

    @GetMapping("/{id}")
    public ResponseEntity<Meal> getRest(@PathVariable int id) {
        Meal meal;
        try {
            meal = super.get(id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(meal, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Meal> deleteRest(@PathVariable int id) {
        try {
            super.delete(id);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Meal> createRest(@RequestBody Meal meal) {
        Meal createdMeal;
        try {
            createdMeal = super.create(meal);
        } catch (IllegalArgumentException | DataIntegrityViolationException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(createdMeal, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Meal> updateRest(@RequestBody Meal meal, @PathVariable int id) {
        try {
            super.update(meal, id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<MealTo>> getAllRest() {
        return new ResponseEntity<>(super.getAll(), HttpStatus.OK);
    }

    @GetMapping("/filter")
    public ResponseEntity<List<MealTo>> getBetweenRest(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startTime,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endTime) {
        LocalDate startDateLocal = startDate == null ? null : startDate.toLocalDate();
        LocalDate endDateLocal = endDate == null ? null : endDate.toLocalDate();
        LocalTime startTimeLocal = startTime == null ? null : startTime.toLocalTime();
        LocalTime endTimeLocal = endTime == null ? null : endTime.toLocalTime();
        return new ResponseEntity<>(super.getBetween(startDateLocal, startTimeLocal, endDateLocal, endTimeLocal), HttpStatus.OK);
    }
}