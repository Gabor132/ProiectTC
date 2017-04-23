/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author Dragos
 */
public class Configs {
    public List<Config> configs;
    public List<Pair<Character, Configs>> tranzitions;
    public int index;
    
    private static int nextIndex = 0;
    
    public Configs(List<Config> configs){
        this.configs = configs;
        this.index = nextIndex;
        nextIndex++;
    }
    
    public static void follow(){
        
    }
    
    public static void closure(Configs I, List<ProductionRule> rules){
        LinkedList<Config> configurations = new LinkedList<>();
        configurations.push(I.configs.get(0));
        while(configurations.size() > 0){
            Config currentConfig = configurations.pop();
            List<ProductionRule> neededRules = new LinkedList<>();
            for(ProductionRule rule : rules){
                if(Objects.equals(rule.from, currentConfig.getMarkedByDot())){
                    neededRules.add(rule);
                }
            }
            String firstS = currentConfig.getAfterDot();
            Set<String> newLookAhead = Utils.first(firstS);
        }
    }
    
    public static void goTo(){
        
    }
    
    @Override
    public String toString(){
        String s = "I"+index+": ";
        for(Config c:configs){
            s += c.toString() + "\n";
        }
        return s;
    }
}
