package ru.javawebinar.topjava.web;

import org.slf4j.Logger;
import ru.javawebinar.topjava.model.Meal;
import ru.javawebinar.topjava.model.MealWithExceed;
import ru.javawebinar.topjava.util.MealsUtil;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;

import static org.slf4j.LoggerFactory.getLogger;


public class MealServlet extends HttpServlet
{
    private static final Logger log = getLogger(MealServlet.class);

    List<MealWithExceed> mealsWithExceeded = MealsUtil.getFilteredWithExceededByCycle(MealsUtil.meals, LocalTime.MIN, LocalTime.MAX, 2000);

    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException
    {
       log.debug("redirect to meals");
       req.setAttribute("mealsWithExceeded", mealsWithExceeded);
        req.getRequestDispatcher("/meals.jsp").forward(req, resp);
        //resp.sendRedirect("meals.jsp");
    }
}

