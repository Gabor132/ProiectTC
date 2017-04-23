/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.util.List;

/**
 *
 * @author Dragos
 */
public class ProductionRule {
    public Character from;
    public String to;

    public ProductionRule(Character from, String to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return  from + " -> " + to;
    }
    
    
}
