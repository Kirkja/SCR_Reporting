/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.grantedsolutions.scr_reporter;

/**
 *
 * @author Ben S. Jones
 */
public class DropdownData {
    public Object value;
    public String title;
    
    
    public DropdownData(Object v, String t) {
        this.value = v;
        this.title = t;
    }
    
    @Override
    public String toString() {
         return title.toString();
    }
}
