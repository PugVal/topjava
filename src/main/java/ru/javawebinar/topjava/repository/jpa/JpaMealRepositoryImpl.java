package ru.javawebinar.topjava.repository.jpa;

import org.springframework.dao.support.DataAccessUtils;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.MealRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@Transactional(readOnly = true)
public class JpaMealRepositoryImpl implements MealRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    @Transactional
    public Meal save(Meal meal, int userId) {
        User u = em.getReference(User.class, userId);
        if (meal.isNew()) {
            meal.setUser(u);
            em.persist(meal);
            /*int mealId = em.find(Meal.class, meal.getId()).getId();
            meal.setId(mealId);*/
            return meal;
        } else {
            meal.setUser(u);
            return em.merge(meal);
            /*int id = meal.getId();
            String description = meal.getDescription();
            int calories = meal.getCalories();
            LocalDateTime date_time = meal.getDateTime();
            List<Meal> meals = em.createQuery("UPDATE Meal m SET m.description=:description, m.calories=:calories, m.dateTime=:date_time WHERE  m.id=:id AND m.user.id=:userId")
            .setParameter("id", id)
            .setParameter("userId", userId)
            .setParameter("description", description)
            .setParameter("calories", calories)
            .setParameter("date_time", date_time).getResultList();
            return DataAccessUtils.singleResult(meals);*/
        }
    }

    @Override
    @Transactional
    public boolean delete(int id, int userId) {
        Query query = em.createQuery("DELETE FROM Meal m WHERE m.id=:id AND m.user.id=:userId");
        query.setParameter("id", id);
        query.setParameter("userId", userId);
        return query.executeUpdate() != 0;
    }

    @Override
    public Meal get(int id, int userId) {
        List<Meal> meals = em.createNamedQuery(Meal.BY_USER_ID, Meal.class)
                .setParameter("id", id)
                .setParameter("userId", userId)
                .getResultList();
        return DataAccessUtils.singleResult(meals);
    }

    @Override
    public List<Meal> getAll(int userId) {
       /* Query query = em.createQuery("SELECT m FROM Meal m WHERE m.user.id=:userId ORDER BY m.dateTime DESC");
        query.setParameter("userId", userId);
        List<Meal> meals = new ArrayList<>();
         meals.addAll(query.getResultList());
         return meals;*/

        return em.createNamedQuery(Meal.ALL_SORTED, Meal.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    @Override
    public List<Meal> getBetween(LocalDateTime startDate, LocalDateTime endDate, int userId) {
        Query query = em.createQuery("SELECT m FROM Meal m WHERE m.dateTime>=:startDate AND m.dateTime<=:endDate AND m.user.id=:userId ORDER BY m.dateTime DESC");
        query.setParameter("userId", userId);
        query.setParameter("startDate", startDate);
        query.setParameter("endDate", endDate);
        List<Meal> meals = new ArrayList<>();
        meals.addAll(query.getResultList());
        return meals;
    }
}