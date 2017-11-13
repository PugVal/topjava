package ru.javawebinar.topjava.util;

import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;

import java.util.*;

public class UsersUtil {
    public static final  List<User> USERS = Arrays.asList(
            new User(1, "Ivan", "van@", "123", Role.ROLE_ADMIN),
            new User(2, "Kate", "kat@", "kkk", Role.ROLE_USER),
            new User(3, "Alex", "al@", "pug", Role.ROLE_ADMIN),
            new User(5, "Val", "zalval@", "vvvv", Role.ROLE_USER),
            new User(4, "Val", "valval@", "vvvv", Role.ROLE_USER)

    );

    public static List<User> orderedUsersList(List<User> users) {
        class UserNameComparator implements Comparator<User> {

            public int compare(User a, User b) {

                return a.getName().compareTo(b.getName());
            }
        }
        class UserEmailComparator implements Comparator<User> {

            public int compare(User a, User b) {

                return a.getEmail().compareTo(b.getEmail());
            }
        }
        Comparator<User> comp = new UserNameComparator().thenComparing(new UserEmailComparator());

        TreeSet<User> comparedUser = new TreeSet<>(comp);
        comparedUser.addAll(users);

        List <User> result = new ArrayList<>();
        result.addAll(comparedUser);

        return result;
    }
}
