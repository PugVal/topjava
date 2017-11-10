package ru.javawebinar.topjava.repository;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class UserMealRepoInMemory implements UserMealRepo
{
    Map<Integer, Meal> mapRepo = new ConcurrentHashMap<>();
    AtomicInteger counter = new AtomicInteger(0);

    @Override
    public Meal save(Meal meal) {
        return null;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Meal get(int id) {
        return null;
    }

    @Override
    public Collection<Meal> getAll() {
        return null;
    }
}
