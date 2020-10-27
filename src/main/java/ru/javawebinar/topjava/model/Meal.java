package ru.javawebinar.topjava.model;

import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@NamedQueries({
        @NamedQuery(name = "delete", query = "DELETE FROM Meal m WHERE m.id=:mID AND m.user.id=:uID"),
        @NamedQuery(name = "update", query = "UPDATE Meal SET description=?1, calories=?2, dateTime=?3 WHERE user.id=?4"),
        @NamedQuery(name = "get", query = "SELECT m FROM Meal m WHERE m.id=?1 AND m.user.id=?2"),
        @NamedQuery(name = "getAllSorted", query = "SELECT m FROM Meal m WHERE m.user.id=:uID ORDER BY m.dateTime DESC"),
        @NamedQuery(name = "getAllSortedAndBounded", query = "SELECT m FROM Meal m WHERE m.user.id=:uID AND m.dateTime<=:startDateTime AND m.dateTime<:endDateTime ORDER BY m.dateTime DESC")
})
@Entity
@Table(name = "meals", uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "date_time"}, name = "meals_unique_user_datetime_idx"))
@Transactional(readOnly = true)
public class Meal extends AbstractBaseEntity {

    @Column(name = "date_time", nullable = false)
    private LocalDateTime dateTime;

    @Column(name = "description")
    private String description;

    @Column(name = "calories", nullable = false)
    private int calories;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Meal() {
    }

    public Meal(LocalDateTime dateTime, String description, int calories) {
        this(null, dateTime, description, calories);
    }

    public Meal(Integer id, LocalDateTime dateTime, String description, int calories) {
        super(id);
        this.dateTime = dateTime;
        this.description = description;
        this.calories = calories;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public String getDescription() {
        return description;
    }

    public int getCalories() {
        return calories;
    }

    public LocalDate getDate() {
        return dateTime.toLocalDate();
    }

    public LocalTime getTime() {
        return dateTime.toLocalTime();
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setCalories(int calories) {
        this.calories = calories;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", dateTime=" + dateTime +
                ", description='" + description + '\'' +
                ", calories=" + calories +
                '}';
    }
}
