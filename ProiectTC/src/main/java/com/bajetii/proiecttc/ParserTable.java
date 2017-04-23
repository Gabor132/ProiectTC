/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import javafx.util.Pair;

/**
 *
 * @author Dragos
 */
public class ParserTable {
    
    public boolean hasConflicts = false;
    
    public class ParserRow{
        public HashMap<Character, Action> actions;
        public Configs I;
        
        public ParserRow(List<Character> elements, Configs I){
            this.I = I;
            this.actions = new HashMap<>();
            for(Character c : elements){
                actions.put(c, new Action(I.index));
            }
        }
        
        @Override
        public String toString(){
            String s = "I"+I.index + ": ";
            for(Character c : actions.keySet()){
                s += c + ":" + actions.get(c)+" ";
            }
            return s;
        }
    }
    
    public List<ParserRow> rows;
    
    public ParserTable(List<Character> elements, List<Configs> automat, List<ProductionRule> rules){
        Character[] auxE = new Character[elements.size()];
        Arrays.sort(elements.toArray(auxE), new Comparator<Character>() {
            @Override
            public int compare(Character o1, Character o2) {
                if(Character.isLowerCase(o1) && o2.equals('$')){
                    return -1;
                }else if(Character.isLowerCase(o2) && o1.equals('$')){
                    return 1;
                }else if(Character.isUpperCase(o2) && o1.equals('$')){
                    return -1;
                }else if(Character.isUpperCase(o1) && o2.equals('$')){
                    return 1;
                }else if(Character.isLowerCase(o1) && Character.isLowerCase(o2) ||
                        Character.isUpperCase(o1) && Character.isUpperCase(o2)){
                    return o1 - o2;
                }else if(Character.isLowerCase(o1) && Character.isUpperCase(o2) ||
                        Character.isUpperCase(o1) && Character.isLowerCase(o2)){
                    return o2 - o1;
                }
                return 0;
            }
        });
        elements.clear();
        elements.addAll(Arrays.asList(auxE));
        System.out.println("Elementele: " + elements);
        rows = new LinkedList<>();
        for(Configs currentI : automat){
            rows.add(new ParserRow(elements, currentI));
        }
        LinkedList<Configs> queueConfigs = new LinkedList<>();
        queueConfigs.addFirst(automat.get(0));
        System.out.println("Filling table");
        while(queueConfigs.size() > 0){
            Configs currentI = queueConfigs.removeFirst();
            for(Config config : currentI.configs){
                if(config.isMarkedOut()){
                    for(Character c : config.lookAhead){
                        if(rows.get(currentI.index).actions.get(c).type == ActionType.REDUCE ||
                                rows.get(currentI.index).actions.get(c).type == ActionType.SHIFT){
                            hasConflicts = true;
                        }
                        rows.get(currentI.index).actions.get(c).type = ActionType.REDUCE;
                        for(int i = 0; i < rules.size(); i++){
                            if(rules.get(i).equals(config)){
                                if(i == 0){
                                    rows.get(currentI.index).actions.get(c).type = ActionType.ACCEPT;
                                }
                                rows.get(currentI.index).actions.get(c).stateIndex = i;
                                break;
                            }
                        }
                    }
                }
            }
            for(Pair<Character, Configs> tranzition : currentI.tranzitions){
                if(Character.isLowerCase(tranzition.getKey()) || tranzition.getKey().equals('$')){
                    rows.get(currentI.index).actions.get(tranzition.getKey()).type = ActionType.SHIFT;
                    rows.get(currentI.index).actions.get(tranzition.getKey()).stateIndex = tranzition.getValue().index;
                }else{
                    rows.get(currentI.index).actions.get(tranzition.getKey()).type = ActionType.TRANSITION;
                    rows.get(currentI.index).actions.get(tranzition.getKey()).stateIndex = tranzition.getValue().index;
                }
                queueConfigs.addFirst(tranzition.getValue());
            }
        }
    }
    
    @Override
    public String toString(){
        String s = "";
        for(ParserRow row : rows){
            s += row + "\n";
        }
        return s;
    }
}
