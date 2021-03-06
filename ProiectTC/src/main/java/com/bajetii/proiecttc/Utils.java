package com.bajetii.proiecttc;

import java.util.*;

/**
 * Created by alexandru on 22.04.2017.
 */
public class Utils {
    private static Map<Character, HashSet<Character>> firstMap;
    private static Map<Character, List<String>> rulesMap;

    private static Map<Character, HashSet<Character>> followMap;

    private static char startSymbol;

    static {
        firstMap = new HashMap<>();
        rulesMap = new HashMap<>();
        followMap = new HashMap<>();
    }

    public static void initUtils(List<ProductionRule> rules) {
        startSymbol = rules.get(0).from;

        for (ProductionRule rule: rules) {
            if (rulesMap.get(rule.from) == null)
                rulesMap.put(rule.from, new ArrayList<>());
            rulesMap.get(rule.from).add(rule.to);
        }
    }

    public static Set<Character> first(char c) {
        if (firstMap.get(c) == null) {
            HashSet<Character> set = new HashSet<>();
            if (!Character.isUpperCase(c))
                set.add(c);
            else {
                if (rulesMap.get(c) != null)
                    for (String s: rulesMap.get(c)){
                        try{
                            if (s.charAt(0) != c)
                                set.addAll(first(s.charAt(0)));
                        }catch(StringIndexOutOfBoundsException ex){
                            set.add('$');
                        }
                        firstMap.put(c, set);
                    }
            }
            firstMap.put(c, set);
        }

        return firstMap.get(c);
    }

    public static Set<Character> follow(char c) {
        if (followMap.get(c) == null) {
            HashSet<Character> set = new HashSet<>();
            if (c == startSymbol) {
                set.add('$');
                followMap.put(c, set);
            }
            else {

                followMap.put(c, new HashSet<>());
                for (Map.Entry<Character, List<String>> entry : rulesMap.entrySet())
                    for (String to : entry.getValue())
                        for (int i = 0; i < to.length(); i++)
                            if (to.charAt(i) == c)
                                if (i + 1 < to.length()) {
                                    Set<Character> firstSet = first(to.charAt(i + 1));
                                    set.addAll(firstSet);
                                    if (firstSet.contains(Constants.LAMBDA.getValue()))
                                        set.addAll(follow(entry.getKey()));
                                    followMap.get(c).addAll(set);
                                }
                                else {
                                    set.addAll(follow(entry.getKey()));
                                    followMap.get(c).addAll(set);
                                }
                followMap.get(c).remove(Constants.LAMBDA.getValue());
                if (c != startSymbol)
                    followMap.get(c).remove('$');
            }
        }
        return followMap.get(c);
    }

    /** Should only be used for debugging purposes **/
    public static void generateAndPrintFirstMap() {
        for (char c: rulesMap.keySet())
            first(c);
        System.out.println(firstMap);
    }
    
    public static void closure(Configs I, List<ProductionRule> rules, List<Configs> automat){
        LinkedList<Config> configurations = new LinkedList<>();
        configurations.addAll(I.configs);
        Set<Config> vizitedConfigs = new HashSet();
        vizitedConfigs.add(I.configs.get(0));
        while(configurations.size() > 0){
            Config currentConfig = configurations.pop();
            List<Config> newConfig = new LinkedList<>();
            try{
                char cForClosure = currentConfig.getMarkedByDot();
                for(ProductionRule rule : rules){
                    if(Objects.equals(rule.from, cForClosure)){
                        newConfig.add(rule.getConfig());
                    }
                }
            }catch(IndexOutOfBoundsException ex){
                continue;
            }
            try{
                char cForFirst = currentConfig.getAfterDot();
                Set<Character> newLookAhead = Utils.first(cForFirst);
                currentConfig.lookAhead.addAll(newLookAhead);
                for(Config config : newConfig){
                    config.lookAhead.addAll(newLookAhead);
                }
            }catch(IndexOutOfBoundsException ex){
                for(Config config : newConfig){
                    config.lookAhead.addAll(currentConfig.lookAhead);
                }
            }
            for(Config config : newConfig){
                if(!vizitedConfigs.contains(config)){
                    I.configs.add(config);
                    configurations.push(config);
                    vizitedConfigs.add(config);
                }
            }
        }
        System.out.println(I);
        for(int i = 0; i < I.configs.size();i++){
            Config c = I.configs.get(i);
            for(int j = i+1; j < I.configs.size(); j++){
                Config c2 = I.configs.get(j);
                if(c.from.equals(c2.from) && c.to.equals(c2.to) && c.index == c2.index &&
                        !(c.lookAhead.containsAll(c2.lookAhead) && c2.lookAhead.containsAll(c.lookAhead))){
                    c.lookAhead.addAll(c2.lookAhead);
                    I.configs.remove(c2);
                    j--;
                }
            }
        }
        System.out.println(I);
    }
    
    public static Configs configAlreadyExistsInAutomat(List<Configs> automat, Config config) {
        for (Configs configs : automat) {
            for (Config auxConfig : configs.configs) {
                if (auxConfig.equals(config)) {
                    return configs;
                }
            }
        }
        return null;
    }

    public static void generateAndPrintFollowMap() {
        for (char c : rulesMap.keySet())
            follow(c);
        System.out.println(followMap);
    }
}
