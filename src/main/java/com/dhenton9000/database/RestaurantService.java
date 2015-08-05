/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

/**
 *
 * @author dhenton
 */
public class RestaurantService extends JdbcDaoSupport implements RestaurantDao {

    private Logger LOG = LoggerFactory.getLogger(RestaurantService.class);
    private PlatformTransactionManager transactionManager;

    public void setTransactionManager(PlatformTransactionManager txManager) {

        this.transactionManager = txManager;

    }

    /**
     * @return the transactionManager
     */
    public PlatformTransactionManager getTransactionManager() {
        return transactionManager;
    }

    private class RestaurantRowMapper implements RowMapper {

        @Override
        public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
            HashMap<String, Object> h = new HashMap<String, Object>();
            h.put("id", rs.getInt("id"));
            h.put("name", rs.getInt("name"));
            
            return h;
        }

    }

    @Override
    public void sampleTransaction(String info) {

        TransactionDefinition txDef = new DefaultTransactionDefinition();

        TransactionStatus txStatus = transactionManager.getTransaction(txDef);

        try {
            String deleteAction1 = "";
            String deleteAction2 = "";
           
            this.getJdbcTemplate().execute(deleteAction1);
            this.getJdbcTemplate().execute(deleteAction2);
             

            transactionManager.commit(txStatus);

        } catch (Exception e) {

            transactionManager.rollback(txStatus);

            LOG.error("problem with delete "
                    + e.getClass().getName() + " " + e.getMessage());

        }

    }

    

    @Override
    public List<Map<String, Object>> getRestaurants(int id ) {
        String sql = "select * from restaurants where id > ? limit 10";
        RowMapper mapper = new RestaurantRowMapper();

        return this.getJdbcTemplate().query(sql, mapper, new Object[]{id});

    }
}
