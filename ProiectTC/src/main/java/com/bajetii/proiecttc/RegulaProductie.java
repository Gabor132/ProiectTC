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
public class RegulaProductie {
    public Character from;
    public List<String> to;

    public RegulaProductie(Character from, List<String> to) {
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return  from + " -> " + to;
    }
    
    
}
