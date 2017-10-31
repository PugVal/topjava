package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.UserMeal;
import ru.javawebinar.topjava.model.UserMealWithExceed;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UserMealsUtil {
    public static void main(String[] args) {
        List<UserMeal> mealList = Arrays.asList(
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 10, 0), "Завтрак", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 13, 0), "Обед", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 30, 20, 0), "Ужин", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 10, 0), "Завтрак", 1000),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 13, 0), "Обед", 500),
                new UserMeal(LocalDateTime.of(2015, Month.MAY, 31, 20, 0), "Ужин", 510)
        );
        getFilteredWithExceeded(mealList, LocalTime.of(2, 0), LocalTime.of(12, 0), 2000);
//        .toLocalDate();
//        .toLocalTime();
    }

    public static List<UserMealWithExceed> getFilteredWithExceeded(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        long t0 = System.nanoTime();

        List<UserMealWithExceed> myResultList = new ArrayList<>();
        Map<LocalDate, Integer> calDay = new HashMap<>();

        mealList.stream()
                .map(elt -> calDay.put(elt.getDateTime().toLocalDate(), calDay.merge(elt.getDateTime().toLocalDate(), elt.getCalories(), Integer::sum)))
                .collect(Collectors.toList());

        mealList.stream()
                .filter(s -> TimeUtil.isBetween(s.getDateTime().toLocalTime(), startTime, endTime))
                .map(elt -> myResultList.add(new UserMealWithExceed(elt.getDateTime(), elt.getDescription(), elt.getCalories(), calDay.get(elt.getDateTime().toLocalDate()) > caloriesPerDay)))
                .collect(Collectors.toList());

        long t1 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("stream method took: %d ms", millis));

        return myResultList;
    }

    public static List<UserMealWithExceed> getFilteredWithExceededByCycle(List<UserMeal> mealList, LocalTime startTime, LocalTime endTime, int caloriesPerDay) {

        long t0 = System.nanoTime();

        List<UserMealWithExceed> myResultList = new ArrayList<>();
        Map<LocalDate, Integer> calDay = new HashMap<>();

        for (UserMeal meal : mealList)
        {
            calDay.put(meal.getDateTime().toLocalDate(), calDay.merge(meal.getDateTime().toLocalDate(), meal.getCalories(), Integer::sum));
        }

        for (UserMeal userMeal : mealList) {
            if (TimeUtil.isBetween(userMeal.getDateTime().toLocalTime(), startTime, endTime))
            {
                myResultList.add(new UserMealWithExceed(userMeal.getDateTime(), userMeal.getDescription(), userMeal.getCalories(), calDay.get(userMeal.getDateTime().toLocalDate()) > caloriesPerDay));
            }
        }

        long t1 = System.nanoTime();
        long millis = TimeUnit.NANOSECONDS.toMillis(t1 - t0);
        System.out.println(String.format("forEach method took: %d ms", millis));

        return myResultList;
    }
}

