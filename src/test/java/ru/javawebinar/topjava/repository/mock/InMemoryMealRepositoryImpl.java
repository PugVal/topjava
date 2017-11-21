package ru.javawebinar.topjava.repository.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.MealRepository;
import ru.javawebinar.topjava.util.DateTimeUtil;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.javawebinar.topjava.UserTestData.*;

@Repository
public class InMemoryMealRepositoryImpl implements MealRepository {
    private static final Logger log = LoggerFactory.getLogger(InMemoryUserRepositoryImpl.class);

    // Map  userId -> (mealId-> meal)
    private Map<Integer, Map<Integer, Meal>> repository = new ConcurrentHashMap<>();
    private AtomicInteger counter = new AtomicInteger(0);


    private static Map<Integer, Meal> mealsForUser = new ConcurrentHashMap<>();
    private static Map<Integer, Meal> mealsForAdmin = new ConcurrentHashMap<>();

    public void init()
    {   repository.clear();
        mealsForUser.clear();
        mealsForAdmin.clear();
        mealsForUser.put(MealTestData.MEAL.getId(), MealTestData.MEAL);
        mealsForUser.put(MealTestData.MEAL2.getId(), MealTestData.MEAL2);
        mealsForUser.put(MealTestData.MEAL3.getId(), MealTestData.MEAL3);
        mealsForAdmin.put(MealTestData.MEAL4.getId(), MealTestData.MEAL4);
        mealsForAdmin.put(MealTestData.MEAL5.getId(), MealTestData.MEAL5);
        mealsForAdmin.put(MealTestData.MEAL6.getId(), MealTestData.MEAL6);
        repository.put(UserTestData.USER_ID, mealsForUser);
        repository.put(UserTestData.ADMIN_ID, mealsForAdmin);
    }


    @Override
    public Meal save(Meal meal, int userId) {
        Map<Integer, Meal> meals = repository.computeIfAbsent(userId, ConcurrentHashMap::new);
        if (meal.isNew()) {
            meal.setId(counter.incrementAndGet());
            meals.put(meal.getId(), meal);
            return meal;
        }
        return meals.computeIfPresent(meal.getId(), (id, oldMeal) -> meal);
    }

    @PostConstruct
    public void postConstruct() {
        log.info("+++ PostConstruct");
    }

    @PreDestroy
    public void preDestroy() {
        log.info("+++ PreDestroy");
    }

    @Override
    public boolean delete(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals != null && meals.remove(id) != null;
    }

    @Override
    public Meal get(int id, int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ? null : meals.get(id);
    }

    @Override
    public List<Meal> getAll(int userId) {
        return getAllAsStream(userId).collect(Collectors.toList());
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDateTime, LocalDateTime endDateTime, int userId) {
        return getAllAsStream(userId)
                .filter(meal -> DateTimeUtil.isBetween(meal.getDateTime(), startDateTime, endDateTime))
                .collect(Collectors.toList());
    }

    private Stream<Meal> getAllAsStream(int userId) {
        Map<Integer, Meal> meals = repository.get(userId);
        return meals == null ?
                Stream.empty() :
                meals.values().stream()
                        .sorted(Comparator.comparing(Meal::getDateTime).reversed());
    }
}