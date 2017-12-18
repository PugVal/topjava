package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.stream.Collectors;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private class MyResultSetExtractor implements ResultSetExtractor<List<User>>
    {
        @Nullable
        @Override
        public List<User> extractData(ResultSet resultSet) throws SQLException, DataAccessException {
            Map<Integer, User> usersMap = new HashMap<>();
            while (resultSet.next())
            {
                int id = resultSet.getInt("id");
                User user = usersMap.get(id);
                if (user==null)
                {
                    user = new User();
                    user.setId(resultSet.getInt("id"));
                    user.setName(resultSet.getString("name"));
                    user.setEmail(resultSet.getString("email"));
                    user.setPassword(resultSet.getString("password"));
                    user.setRegistered(resultSet.getDate("registered"));
                    user.setEnabled(resultSet.getBoolean("enabled"));
                    user.setCaloriesPerDay(resultSet.getInt("calories_per_day"));
                    Set<Role> roles = new HashSet<>();
                    Role userRole = Role.valueOf(resultSet.getString("role"));
                    if (resultSet.getString("role").equalsIgnoreCase("ROLE_ADMIN"))
                    {
                        roles.add(userRole);
                        roles.add(Role.ROLE_USER);
                    }
                    roles.add(userRole);
                    user.setRoles(roles);
                    usersMap.put(id, user);
                }
                else
                    user.getRoles().add(Role.valueOf(resultSet.getString("role")));
            }
            return new ArrayList<>(usersMap.values().stream()
                    .sorted(Comparator.comparing(User::getName).thenComparing(User::getEmail))
                    .collect(Collectors.toList()));
        }
    }

    @Autowired
    public JdbcUserRepositoryImpl(DataSource dataSource, JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(dataSource)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE id=?", new MyResultSetExtractor(), id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id WHERE email=?", new MyResultSetExtractor(), email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query("SELECT * FROM users LEFT JOIN user_roles ON users.id = user_roles.user_id ORDER BY name, email", new MyResultSetExtractor());
    }
}


