package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public interface MealService
{
    Meal create(Meal meal, int userId);
    Meal update(Meal meal, int userId);
    void delete(int id, int userId);
    Meal get(int id, int userId);
    List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate);
    List<Meal> getAllByTime(int userId, LocalTime startTime, LocalTime endTime);
    List<Meal> getAllByUserId(int userId);
}