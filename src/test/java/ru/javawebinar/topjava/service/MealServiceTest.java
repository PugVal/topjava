package ru.javawebinar.topjava.service;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.SqlConfig;
import org.springframework.test.context.junit4.SpringRunner;
import ru.javawebinar.topjava.MealTestData;
import ru.javawebinar.topjava.UserTestData;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.repository.mock.InMemoryMealRepositoryImpl;
import ru.javawebinar.topjava.util.exception.NotFoundException;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static org.junit.Assert.*;

@ContextConfiguration({
        "classpath:META-INF/applicationContext-web-test.xml",
        "classpath:spring/spring-db.xml"
})
@RunWith(SpringRunner.class)
@Sql(scripts = "classpath:db/populateDB.sql", config = @SqlConfig(encoding = "UTF-8"))
public class MealServiceTest {

    static {
        SLF4JBridgeHandler.install();
    }

    @Autowired
    private MealService mealService;

    @Autowired
    private InMemoryMealRepositoryImpl inMemoryMealRepository;

    @Before
    public void setUp() throws Exception {
        inMemoryMealRepository.init();
    }

    @Test
    public void get() throws Exception
    {
        Meal meal = mealService.get(100002, 100000);
        MealTestData.assertMatch(meal, MealTestData.MEAL);
    }

    @Test
    public void delete() throws Exception
    {
        mealService.delete(100002, 100000);
        Assert.assertEquals(mealService.getAll(100000).size(), 2);
    }

    /*@Test
    public void getBetweenDates() throws Exception {
    }
*/
    @Test
    public void getBetweenDateTimes() throws Exception
    {
      List<Meal> meals =  mealService.getBetweenDateTimes(LocalDateTime.of(2017, Month.JUNE, 1, 7, 0), LocalDateTime.of(2017, Month.JUNE, 1, 17, 0), UserTestData.USER_ID);
      Assert.assertEquals(meals.size(),3);
    }

    @Test
    public void update() throws Exception
    {
        MealTestData.MEAL.setDescription("super breakfast");
        mealService.update(MealTestData.MEAL, UserTestData.USER_ID);
        Assert.assertEquals(MealTestData.MEAL.getDescription(), "super breakfast");
    }

    @Test
    public void create() throws Exception
    {
        Meal createdMeal = new Meal(null, LocalDateTime.of(2017, Month.MAY, 1, 17, 0), "supper", 500);
        mealService.create(createdMeal, UserTestData.USER_ID);
        Assert.assertEquals(mealService.getAll(UserTestData.USER_ID).size(), 4);
    }

    @Test(expected = NotFoundException.class)
    public void getNotFoundByMealId() throws Exception {
        mealService.get(100007,100000);
    }

    @Test(expected = NotFoundException.class)
    public void deleteNotFoundByMealId() throws Exception {
        mealService.delete(100002, 100001);
        Assert.assertEquals(mealService.getAll(100001).size(), 2);
    }

    @Test(expected = NotFoundException.class)
    public void updateNotFoundByMealId() throws Exception
    {
        MealTestData.MEAL.setDescription("super breakfast");
        mealService.update(MealTestData.MEAL, UserTestData.ADMIN_ID);
        Assert.assertEquals(MealTestData.MEAL.getDescription(), "super breakfast");
    }

}