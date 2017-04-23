/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 *
 * @author Dragos
 */
public class Config {
    public Character from;
    public String to;
    public Set<Character> lookAhead;
    public int index;

    public Config(Character from, String to) {
        this.from = from;
        this.to = to;
        this.lookAhead = new HashSet<>();
        this.lookAhead.add('$');
        this.index = 0;
    }
    
    public Config(Character from, String to, Set<Character> lookAhead) {
        this.from = from;
        this.to = to;
        this.lookAhead = lookAhead;
        this.index = 0;
    }
    
    public char getAfterDot() throws IndexOutOfBoundsException{
        return to.charAt(index+1);
    }
    
    public char getMarkedByDot() throws IndexOutOfBoundsException{
        return to.charAt(index);
    }
    
    public boolean hasSameTransitionChar(Config c){
        try{
            char a = getMarkedByDot();
            char b = c.getMarkedByDot();
            return a == b;
        }catch(IndexOutOfBoundsException ex){
            return false;
        }
    }
    
    public boolean isMarkedOut(){
        return index == to.length();
    }
    
    @Override
    public String toString(){
        String s = from + " -> ";
        int i = 0;
        for(Character auxC : to.toCharArray()){
            if(i == index)
                s += ".";
            i++;
            s += auxC + " ";
        }
        if(i == index)
            s += ".";
        s += ",";
        for(Character c : lookAhead){
            s += c+" ";
        }
        return s;
    }
    
    public boolean equals(ProductionRule r){
        return this.from.equals(r.from) && this.to.equals(r.to);
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 31 * hash + Objects.hashCode(this.from);
        hash = 31 * hash + Objects.hashCode(this.to);
        hash = 31 * hash + Objects.hashCode(this.lookAhead);
        hash = 31 * hash + this.index;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Config other = (Config) obj;
        if (this.index != other.index) {
            return false;
        }
        if (!Objects.equals(this.to, other.to)) {
            return false;
        }
        if (!Objects.equals(this.from, other.from)) {
            return false;
        }
        return Objects.equals(this.lookAhead, other.lookAhead);
    }
    
    
}
