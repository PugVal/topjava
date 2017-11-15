package ru.javawebinar.topjava.service;

import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import java.time.LocalTime;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class MealServiceImpl implements MealService {

    private MealRepository repository;

    @Override
    public Meal save(Meal meal, int userId) {
        if  (meal.getUserId() == userId || meal.isNew())
             repository.save(meal, userId);
          return meal;
    }

    @Override
    public void delete(int id, int userId)
    {
        int userNumber = repository.get(id, userId).getUserId(); // id пользователя из еды
        if  (userNumber==userId)  repository.delete(id, userId);
    }

    @Override
    public Meal get(int id, int userId) {
        int userNumber = repository.get(id, userId).getUserId(); // id пользователя из еды
        if  (userNumber==userId)
        return repository.get(id, userId);
        return null;
    }

    @Override
    public Collection<Meal> getAll(int userId) {
        return repository.getAll(userId).stream()
                .filter(m -> m.getUserId() == userId)
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }
}