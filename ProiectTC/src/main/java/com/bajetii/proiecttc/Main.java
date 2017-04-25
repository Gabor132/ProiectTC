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
                if(to.equals("~")){
                    to = "";
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
            parseInputStrings(table);
        }catch(FileNotFoundException ex) {
            System.out.println("Du-te acasa");
        }
    }

    public static void parseInputStrings(ParserTable table) {
        try (Scanner reader = new Scanner(new File("strings.txt"))) {
            Parser parser = new Parser(table);
            System.out.println("Parsing input strings...");
            while (reader.hasNextLine()) {
                System.out.println("======================");
                String s = reader.nextLine();
                System.out.println("String: " + s);
                if (parser.accept(s))
                    System.out.println("RESULT: ACCEPTED");
                else
                    System.out.println("RESULT: REJECTED");
            }
        } catch (FileNotFoundException ex) {
            System.out.println("Du-te acasa2");
        }
    }
    
    public static ParserTable verificareLR1(List<ProductionRule> rules){
        Config start = rules.get(rules.size()-1).getConfig();
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
            Utils.closure(I, rules, automat);
            //mergeConfig(I);
            System.out.println("After closure: \n" + I);
            for(Config config : I.configs){
                try{
                    char cForTransition = config.getMarkedByDot();
                    LinkedList<Config> similarConfigs = new LinkedList<>();
                    similarConfigs.add(config);
                    for(Config config2 : I.configs){
                        if(!config.equals(config2) && config.hasSameTransitionChar(config2)){
                            similarConfigs.add(config2);
                        }
                    }
                    LinkedList<Config> newListConfig = new LinkedList<>();
                    Set<Pair<Character, Configs>> tranzitionsThatExist = new HashSet<>();
                    for(Config config2 : similarConfigs){
                        Config newConfig;
                        for(ProductionRule rule : rules){
                            if(rule.equals(config2)){
                                newConfig = rule.getConfig();
                                newConfig.index = config2.index+1;
                                newConfig.lookAhead = config2.lookAhead;
                                Configs configsThatAlreadyContains = Utils.configAlreadyExistsInAutomat(automat, newConfig);
                                if(configsThatAlreadyContains != null){
                                    tranzitionsThatExist.add(new Pair<>(cForTransition, configsThatAlreadyContains));
                                    continue;
                                }
                                newListConfig.add(newConfig);
                            }
                        }
                    }
                    I.tranzitions.addAll(tranzitionsThatExist);
                    if(!newListConfig.isEmpty()){
                        Configs newConfigs = new Configs(newListConfig);
                        queueConfigs.addLast(newConfigs);
                        automat.add(newConfigs);
                        Pair<Character, Configs> tranzition = new Pair<>(cForTransition, newConfigs);
                        I.tranzitions.add(tranzition);
                    }
                }catch(IndexOutOfBoundsException ex){
                }
            }
        }
        System.out.println("Automatul: \n" + automat);
        //automat = reduceAutomat(automat);
        return generateParserTable(automat, rules);
    }
    
    public static List<Configs> reduceAutomat(List<Configs> automat){
        List<Configs> reducedAutomat = new LinkedList<>();
        //luam un configs de la sfarsit
        for(int i = automat.size()-1; i>=0; i--){
            Configs config1 = automat.get(i);
            //luam urmatoarele configs
            for(int j = i-1; j>=0; j--){
                Configs config2 = automat.get(j);
                if(config1.canMerge(config2)){
                    List<Config> configs = new LinkedList<>();
                    for(Config c : config1.configs){
                        c.lookAhead.addAll(config2.configs.get(0).lookAhead);
                        configs.add(c);
                    }
                    Configs merged = new Configs(configs);
                    merged.tranzitions = config1.tranzitions;
                    merged.tranzitions.addAll(config2.tranzitions);
                    for(Configs c : automat){
                        List<Character> lc = new LinkedList<>();
                        List<Pair<Character, Configs>> tranzitionsToRemove = new LinkedList<>();
                        for(Pair<Character, Configs> t : c.tranzitions){
                            if(t.getValue().equals(config1) || t.getValue().equals(config2)){
                                lc.add(t.getKey());
                                tranzitionsToRemove.add(t);
                            }
                        }
                        for(Pair<Character, Configs> t : tranzitionsToRemove){
                            c.tranzitions.remove(t);
                        }
                        for(Character cc : lc){
                            if(!c.tranzitions.contains(new Pair<>(cc, merged))){
                                c.tranzitions.add(new Pair<>(cc, merged));
                            }
                        }
                    }
                    reducedAutomat.add(merged);
                    i--;
                    automat.remove(config1);
                    automat.remove(config2);
                }
            }
        }
        System.out.println(reducedAutomat);
        automat.addAll(reducedAutomat);
        int i = 0;
        for(Configs c : automat){
            c.index = i;
            i++;
        }
        Configs.nextIndex = automat.size();
        System.out.println(automat);
        return automat;
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
    
    public static void mergeConfig(Configs I){
    }
    
}
