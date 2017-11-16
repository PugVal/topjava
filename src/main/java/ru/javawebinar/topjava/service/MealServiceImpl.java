package ru.javawebinar.topjava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDate;
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
        if (meal.getUserId()==userId)
        {
            return repository.save(meal, userId);
        }
        return null;
    }

    @Override
    public void update(Meal meal, int userId) {
        if (meal.getUserId()==userId)
        {
            repository.save(meal, userId);
        }
    }

    @Override
    public void delete(int id, int userId)  throws NotFoundException
    {
        int userNumber = repository.get(id, userId).getUserId(); // id пользователя из еды
        if  (userNumber==userId)
            checkNotFoundWithId(repository.delete(id, userId), id);
    }

    @Override
    public Meal get(int id, int userId) throws NotFoundException
    {
        int userNumber = repository.get(id, userId).getUserId(); // id пользователя из еды
        if  (userNumber==userId)
            return checkNotFoundWithId(repository.get(id, userId), id);
        return null;
    }

    @Override
    public List<Meal> getAll(int userId, LocalDate startDate, LocalDate endDate) {
        return repository.getAll(userId).stream()
                .filter(m -> m.getUserId() == userId)
                .filter(meal -> DateTimeUtil.isBetweenByDate(meal.getDate(), startDate, endDate))
                .sorted(Comparator.comparing(Meal::getDateTime))
                .collect(Collectors.toList());
    }
}