/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.dhenton9000.database;

import java.util.List;
import java.util.Map;

/**
 *
 * @author dhenton
 */
public interface RestaurantDao {
    
    List<Map<String, Object>> getRestaurants(int id );
     public void sampleTransaction(String info);
}
