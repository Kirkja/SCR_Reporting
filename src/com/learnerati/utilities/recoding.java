/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.learnerati.utilities;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author User1
 */
public class recoding {
    
    /**
     * 
     * @param str
     * @return 
     */
    public static String GradelevelLabel(String str) {
        
        str = str.toUpperCase();
        
        Map<String, String>labels = new HashMap<>();
        labels.put("0", "Kindergarten");
        labels.put("K", "Kindergarten");
        labels.put("1", "First Grade");
        labels.put("2", "Second Grade");
        labels.put("3", "Third Grade");
        labels.put("4", "Fourth Grade");
        labels.put("5", "Fifth Grade");
        labels.put("6", "Sixth Grade");
        labels.put("7", "Seventh Grade");
        labels.put("8", "Eighth Grade");
        labels.put("9", "Ninth Grade");
        labels.put("10", "Tenth Grade");
        labels.put("11", "Eleventh Grade");
        labels.put("12", "Twelveth Grade");
        labels.put("101", "Junior High");
        
        if (labels.containsKey(str)) {
            return labels.get(str);
        }
       
        return str;        
    }
    
    /**
     * 
     * @param gradelevel
     * @return 
     */
    public static String GradelevelLabel(Integer gradelevel) {
                        
        Map<Integer, String>labels = new HashMap<>();
        labels.put(gradelevel, "Kindergarten");
        labels.put(gradelevel, "Kindergarten");
        labels.put(gradelevel, "First Grade");
        labels.put(gradelevel, "Second Grade");
        labels.put(gradelevel, "Third Grade");
        labels.put(gradelevel, "Fourth Grade");
        labels.put(gradelevel, "Fifth Grade");
        labels.put(gradelevel, "Sixth Grade");
        labels.put(gradelevel, "Seventh Grade");
        labels.put(gradelevel, "Eighth Grade");
        labels.put(gradelevel, "Ninth Grade");
        labels.put(gradelevel, "Tenth Grade");
        labels.put(gradelevel, "Eleventh Grade");
        labels.put(gradelevel, "Twelveth Grade");
        labels.put(gradelevel, "Junior High");
        
        if (labels.containsKey(gradelevel)) {
            return labels.get(gradelevel);
        }
       
        return gradelevel.toString();        
    }    
    
}
