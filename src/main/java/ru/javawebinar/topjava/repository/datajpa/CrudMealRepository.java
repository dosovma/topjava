package ru.javawebinar.topjava.repository.datajpa;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.util.List;

@Transactional(readOnly = true)
public interface CrudMealRepository extends JpaRepository<Meal, Integer> {

    @Query("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC")
    List<Meal> findAll(@Param("userId") int userId);


    @Query("""
            SELECT m FROM Meal m
            WHERE m.user.id=?3 AND m.dateTime >= ?1 AND m.dateTime < ?2 ORDER BY m.dateTime DESC
            """)
    List<Meal> findAllByUserIdBetweenDate(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId);

    @Transactional
    @Query("DELETE FROM Meal WHERE id=:id AND user.id=:userId")
    @Modifying
    int delete(@Param("id") int id, @Param("userId") int userId);
}
