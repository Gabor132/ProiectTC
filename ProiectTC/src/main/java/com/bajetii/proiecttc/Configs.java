/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Dragos
 */
public class Configs {
    public List<Config> configs;
    public List<Pair<Character, Configs>> tranzitions;
    
    public Configs(List<Config> configs){
        this.configs = configs;
    }
    
    public static void follow(){
        
    }
    
    public static void closure(){
        
    }
    
    public static void goTo(){
        
    }
    
    @Override
    public String toString(){
        String s = "";
        for(Config c:configs){
            s += c.toString() + "\n";
        }
        return s;
    }
}
