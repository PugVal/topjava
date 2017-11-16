package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.to.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<MealWithExceed> getAll(){
        log.info("getAll");
        return getAll(LocalDate.MIN,LocalTime.MIN,LocalDate.MAX,LocalTime.MAX);
    }

    public List<MealWithExceed> getAll(LocalDate startDate, LocalTime startTime, LocalDate endDate, LocalTime endTime){
        log.info("getAll with exceeded");
        List<Meal> meals = service.getAll(AuthorizedUser.id(),startDate,endDate);
        return MealsUtil.getFilteredWithExceeded(meals,startTime,endTime,MealsUtil.DEFAULT_CALORIES_PER_DAY);
    }

    public Meal get (int id)
    {
        log.info("get {}", id);
        return service.get(id, AuthorizedUser.id());
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        //ValidationUtil.checkNew(meal);
        return service.create(meal, AuthorizedUser.id());
    }

     public void delete (int id)
     {
         log.info("delete {}", id);
         service.delete(id, AuthorizedUser.id());
     }

    public void update(Meal meal) {
        log.info("update {} with id={}", meal, meal.getId());
        //ValidationUtil.assureIdConsistent(meal, AuthorizedUser.id());
        service.update(meal, AuthorizedUser.id());
    }

}