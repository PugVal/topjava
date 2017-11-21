package ru.javawebinar.topjava;

import ru.javawebinar.topjava.model.Meal;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class MealTestData
{
    public static final Meal MEAL = new Meal(100002, LocalDateTime.of(2017, Month.JUNE, 1, 8, 0), "breakfast", 500);
    public static final Meal MEAL2 = new Meal(100003, LocalDateTime.of(2017, Month.JUNE, 1, 12, 0), "dinner", 1500);
    public static final Meal MEAL3 = new Meal(100004, LocalDateTime.of(2017, Month.JUNE, 1, 17, 0), "supper", 500);
    public static final Meal MEAL4 = new Meal(100005, LocalDateTime.of(2017, Month.JUNE, 1, 8, 0), "breakfast", 500);
    public static final Meal MEAL5 = new Meal(100006, LocalDateTime.of(2017, Month.JUNE, 1, 12, 0), "dinner", 500);
    public static final Meal MEAL6 = new Meal(100007, LocalDateTime.of(2017, Month.JUNE, 1, 17, 0), "supper", 500);

        public static void assertMatch(Meal actual, Meal expected) {
        assertThat(actual).isEqualToIgnoringGivenFields(expected, "datetime");
    }

    public static void assertMatch(Iterable<Meal> actual, Meal... expected) {
        assertMatch(actual, Arrays.asList(expected));
    }

    public static void assertMatch(Iterable<Meal> actual, Iterable<Meal> expected) {
        assertThat(actual).usingElementComparatorIgnoringFields("datetime").isEqualTo(expected);
    }
}
