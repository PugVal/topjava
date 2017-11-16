package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static ru.javawebinar.topjava.util.ValidationUtil.checkNotFoundWithId;

@Service
public class MealServiceImpl implements MealService {

    private final MealRepository repository;

    @Autowired
    public MealServiceImpl (MealRepository repository)
    {
        this.repository = repository;
    }

    @Override
    public Meal create(Meal meal, int userId) {

        return repository.save(meal, userId);
    }

    @Override
    public Meal update(Meal meal, int userId) {

         return checkNotFoundWithId(repository.save(meal, userId), meal.getId());
    }

    @Override
    public void delete(int id, int userId)  throws NotFoundException
    {
          checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException
    {
            return checkNotFoundWithId(repository.get(id, userId), id);
    }

    @Override
    public List<Meal> getAllByDate(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getAll(userId).stream()
                .filter(m -> m.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetweenByDate(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }

    @Override
    public List<Meal> getAllByTime(int userId, LocalTime startTime, LocalTime endTime) {
        return repository.getAll(userId).stream()
                .filter(m -> m.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetweenByTime(meal.getTime(), startTime, endTime))
                .sorted(Comparator.comparing(Meal::getTime))
                .collect(Collectors.toList());
    }


    @Override
    public List<Meal> getAllByUserId(int userId) {
        return null;
    }
}