/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

/**
 *
 * @author Dragos
 */
public enum ActionType {
    SHIFT, REDUCE, TRANSITION, ERROR, ACCEPT;
    
    @Override
    public String toString(){
        return this.name().charAt(0)+"";
    }
}
