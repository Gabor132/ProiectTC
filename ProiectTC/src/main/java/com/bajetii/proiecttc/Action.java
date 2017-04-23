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
public class Action {
    public ActionType type;
    public int stateIndex;
    
    public Action(ActionType type, int stateIndex){
        this.type = type;
        this.stateIndex = stateIndex;
    }
    
    public Action(int stateIndex){
        this.stateIndex = stateIndex;
        this.type = ActionType.ERROR;
    }
    
    @Override
    public String toString(){
        if(type == ActionType.ERROR)
            return "  ";
        return type+""+stateIndex;
    }
}
