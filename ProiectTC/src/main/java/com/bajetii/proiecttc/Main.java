/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import javafx.util.Pair;

/**
 *
 * @author Dragos
 */
public class Main {
    
    static Set<Character> elements;
    
    public static void main(String[] args){
        try(Scanner s = new Scanner(new File("input.txt"))){
            elements = new HashSet<>();
            List<ProductionRule> rules = new LinkedList<>();
            while(s.hasNextLine()){
                String from = s.next();
                String to = s.next();
                elements.add(from.toCharArray()[0]);
                for(char c : to.toCharArray()){
                    elements.add(c);
                }
                rules.add(new ProductionRule(from.charAt(0), to));
            }
            rules.add(new ProductionRule('T', rules.get(0).from + ""));
            elements.add('$');
            System.out.println(rules);
            Utils.initUtils(rules);
            Utils.generateAndPrintFirstMap();
            ParserTable table = verificareLR1(rules);
            Utils.generateAndPrintFollowMap();
        }catch(FileNotFoundException ex) {
            System.out.println("Du-te acasa");
        }
    }
    
    public static ParserTable verificareLR1(List<ProductionRule> rules){
        Config start = new Config('T', "S");
        List<Configs> automat = new LinkedList<>();
        LinkedList<Configs> queueConfigs = new LinkedList<>();
        List<Config> startList = new LinkedList<>();
        startList.add(start);
        Configs I0 = new Configs(startList);
        System.out.println(I0);
        boolean new_configs_added = true;
        queueConfigs.push(I0);
        automat.add(I0);
        while(queueConfigs.size() > 0){
            Configs I = queueConfigs.removeFirst();
            System.out.println("Luam I" + I.index);
            System.out.println("Before closure: \n" + I);
            Utils.closure(I, rules);
            System.out.println("After closure: \n" + I);
            for(Config config : I.configs){
                try{
                    char cForTransition = config.getMarkedByDot();
                    LinkedList<Config> newListConfig = new LinkedList<>();
                    Config newConfig;
                    for(ProductionRule rule : rules){
                        if(rule.equals(config)){
                            newConfig = rule.getConfig();
                            newConfig.index = config.index+1;
                            newConfig.lookAhead = config.lookAhead;
                            if(Utils.configAlreadyExistsInAutomat(automat, newConfig))
                                continue;
                            newListConfig.add(newConfig);
                            Configs newConfigs = new Configs(newListConfig);
                            queueConfigs.addLast(newConfigs);
                            automat.add(newConfigs);
                            Pair<Character, Configs> tranzition = new Pair<>(cForTransition, newConfigs);
                            I.tranzitions.add(tranzition);
                        }
                    }
                }catch(IndexOutOfBoundsException ex){
                }
            }
        }
        System.out.println("Automatul: \n" + automat);
        return generateParserTable(automat, rules);
    }
    
    public static ParserTable generateParserTable(List<Configs> automat, List<ProductionRule> rules){
        List<Character> auxElem = new LinkedList<>();
        auxElem.addAll(elements);
        rules.remove(rules.size()-1);
        ParserTable table = new ParserTable(auxElem, automat, rules);
        System.out.println(table);
        System.out.println("Is grammar LR(1)? " + !table.hasConflicts);
        return table;
    }
}
