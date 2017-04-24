/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.util.HashSet;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Dragos
 */
public class Configs {
    public List<Config> configs;
    public HashSet<Pair<Character, Configs>> tranzitions;
    public int index;
    
    public static int nextIndex = 0;
    
    public Configs(List<Config> configs){
        this.configs = configs;
        this.index = nextIndex;
        this.tranzitions = new HashSet<>();
        nextIndex++;
    }
    
    public boolean canMerge(Configs config){
        for(Config c : configs){
            boolean hasPair = false;
            for(Config c2 : config.configs){
                hasPair = (c.from.equals(c2.from) && c.to.equals(c2.to) && c.index == c2.index);
                if(hasPair)
                    break;
            }
            if(!hasPair)
                return false;
        }
        System.out.println(this + " can merge with " + config);
        return true;
    }
    
    @Override
    public String toString(){
        String s = "I"+index+": ";
        for(Config c:configs){
            s += c.toString() + "\n";
        }
        s += "[";
        for(Pair<Character, Configs> tran : tranzitions){
            s += tran.getKey()+":I"+tran.getValue().index+" ";
        }
        s += "]\n";
        return s;
    }
}
