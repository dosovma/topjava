package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import javax.validation.*;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);

    private final ResultSetExtractor<List<User>> resultSetExtractor = resultSet -> {
        Map<Integer, User> data = new HashMap<>();
        while (resultSet.next()) {
            Role role = Role.valueOf(resultSet.getString("role"));
            int id = resultSet.getInt("id");
            data.merge(
                    id,
                    new User(
                            id,
                            resultSet.getString("name"),
                            resultSet.getString("email"),
                            resultSet.getString("password"),
                            resultSet.getInt("calories_per_day"),
                            resultSet.getBoolean("enabled"),
                            resultSet.getDate("registered"),
                            EnumSet.of(role)),
                    (oldVal, newVal) -> {
                        oldVal.getRoles().add(role);
                        return oldVal;
                    });
        }

        return List.copyOf(data.values());
    };

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    private final ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    private final Validator validator = factory.getValidator();

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        Set<ConstraintViolation<User>> violationSet = validator.validate(user);
        if (violationSet.size() != 0) {
            throw new ConstraintViolationException(violationSet);
        }

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else if (namedParameterJdbcTemplate.update("""
                   UPDATE users SET name=:name, email=:email, password=:password, 
                   registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                """, parameterSource) == 0) {
            return null;
        }
        updateRoles(user);
        return user;
    }

    private void updateRoles(User user) {
        jdbcTemplate.update("DELETE FROM user_roles where user_id=?", user.getId());
        List<Role> roleList = List.copyOf(user.getRoles());
        jdbcTemplate.batchUpdate(
                "INSERT INTO user_roles VALUES (?,?)",
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(PreparedStatement preparedStatement, int i) throws SQLException {
                        preparedStatement.setInt(1, user.getId());
                        preparedStatement.setString(2, roleList.get(i).toString());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getRoles().size();
                    }
                });
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE id=?", ROW_MAPPER, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                """
                        SELECT * FROM users 
                        LEFT JOIN user_roles ON users.id = user_roles.user_id ORDER BY users.name, users.email
                        """,
                resultSetExtractor);
        //return jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
    }
}
