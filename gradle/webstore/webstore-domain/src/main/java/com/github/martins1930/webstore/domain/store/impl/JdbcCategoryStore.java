package com.github.martins1930.webstore.domain.store.impl;

import com.github.martins1930.webstore.domain.Category;
import com.github.martins1930.webstore.domain.store.CategoryStore;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcDaoSupport;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.ParameterizedRowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by martin on 2/22/14.
 */
@Component
@Transactional
public class JdbcCategoryStore extends NamedParameterJdbcDaoSupport implements CategoryStore {

    public static final String QUERY_FINDBY_NAME =
            "select " +
                    "category_id, category_name, category_description " +
            "from " +
                    "category " +
             "where " +
                    "category_name = :p_name";

    public static final String INSERT =
            "insert into category (category_name, category_description) " +
                    "values (:p_name, :p_description)";

    //it is important to UPPER the column name
    public static final String[] KEY_COLUMN_NAME = new String[]{"CATEGORY_ID"};

    @Override
    public void save(Category category) {
        SqlParameterSource params = createMapParameterQuery(category);
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getNamedParameterJdbcTemplate().update(INSERT, params, keyHolder, KEY_COLUMN_NAME);
        category.setId((Integer) keyHolder.getKey());
    }

    private SqlParameterSource createMapParameterQuery(Category category) {
        Map<String, Object> queryParams = new HashMap<String, Object>();
        queryParams.put("p_name", category.getName());
        queryParams.put("p_description", category.getDescription());
        SqlParameterSource queryParamsSource = new MapSqlParameterSource(queryParams);
        return queryParamsSource;
    }

    @Transactional(readOnly = true)
    @Override
    public Category findById(Integer name) {
        return null;
    }

    @Transactional(readOnly = true)
    @Override
    public Category findByName(String name) {
        Map<String, String> params = new HashMap<String, String>();
        params.put("p_name", name);
        try {
            return getNamedParameterJdbcTemplate().queryForObject(QUERY_FINDBY_NAME, params, new CategoryRowMapper());
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    private static class CategoryRowMapper implements ParameterizedRowMapper<Category> {

        @Override
        public Category mapRow(ResultSet rs, int rowNum) throws SQLException {
            Category category = new Category();
            category.setId(rs.getInt("category_id"));
            category.setName(rs.getString("category_name"));
            category.setDescription(rs.getString("category_description"));
            return category;
        }
    }
}
