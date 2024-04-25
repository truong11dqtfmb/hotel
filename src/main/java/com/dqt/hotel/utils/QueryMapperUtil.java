package com.dqt.hotel.utils;


import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueryMapperUtil {

    private final JdbcTemplate jdbcTemplate;

    public QueryMapperUtil(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public <T> List<T> mapQueryToList(String sql, Class<T> objectType) {
        return jdbcTemplate.query(sql, BeanPropertyRowMapper.newInstance(objectType));
    }

    public <T> T mapQueryToObject(String sql, Class<T> objectType) {
        List<T> list = this.mapQueryToList(sql, objectType);
        if(list!=null && list.size()>0){
            return list.get(0);
        }
        return null;
    }
}