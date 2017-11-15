package ru.javawebinar.topjava.web.meal;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import ru.javawebinar.topjava.AuthorizedUser;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.service.MealService;
import ru.javawebinar.topjava.util.ValidationUtil;

import java.util.List;

@Controller
public class MealRestController {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private MealService service;

    public List<Meal> getAll()
    {
        log.info("getAll meals");
        return service.getAll(AuthorizedUser.id());
    }

    public Meal get (int id, int userId)
    {
        log.info("get {}", id);
        return service.get(id, userId);
    }

    public Meal create(Meal meal) {
        log.info("create {}", meal);
        ValidationUtil.checkNew(meal);
        return service.create(meal, AuthorizedUser.id());
    }

     public void delete (int id, int userId)
     {
         log.info("delete {}", id);
         service.delete(id, userId);
     }

    public void update(Meal meal, int userId) {
        log.info("update {} with userId={}", meal, userId);
        ValidationUtil.assureIdConsistent(meal, userId);
        service.update(meal, userId);
    }

}