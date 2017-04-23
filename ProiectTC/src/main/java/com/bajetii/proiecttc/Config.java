/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author Dragos
 */
public class Config {
    public Character from;
    public String to;
    public List<Character> lookAhead;
    public int index;

    public Config(Character from, String to) {
        this.from = from;
        this.to = to;
        this.lookAhead = new LinkedList<>();
        this.lookAhead.add('$');
        this.index = 0;
    }
    
    public Config(Character from, String to, List<Character> lookAhead) {
        this.from = from;
        this.to = to;
        this.lookAhead = lookAhead;
        this.index = 0;
    }
    
    public String getAfterDot(){
        String s = "";
        s += to.substring(index);
        for(Character c : lookAhead){
            s+=c;
        }
        return s;
    }
    
    public Character getMarkedByDot(){
        return to.charAt(index);
    }
    
    @Override
    public String toString(){
        String s = from + " -> ";
        for(Character auxC : to.toCharArray()){
            s += auxC + " ";
        }
        s += ",";
        for(Character c : lookAhead){
            s += c+" ";
        }
        return s;
    }
}
