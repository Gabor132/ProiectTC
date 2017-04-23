package com.bajetii.proiecttc;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.*;

/**
 * Created by alexandru on 22.04.2017.
 */
public class Utils {
    private static Map<Character, HashSet<Character>> firstMap;
    private static Map<Character, List<String>> rulesMap;

    static {
        firstMap = new HashMap<>();
        rulesMap = new HashMap<>();
    }

    public static void initUtils(List<ProductionRule> rules) {
        for (ProductionRule rule: rules) {
            if (rulesMap.get(rule.from) == null)
                rulesMap.put(rule.from, new ArrayList<>());
            rulesMap.get(rule.from).add(rule.to);
        }
    }

    public static Set<Character> first(char c) {
        if (firstMap.get(c) == null) {
            HashSet<Character> set = new HashSet<>();
            if (c == Constants.LAMBDA.getValue() || Character.isLowerCase(c))
                set.add(c);
            else {
                if (rulesMap.get(c) != null)
                    for (String s: rulesMap.get(c))
                        set.addAll(first(s.charAt(0)));
            }
            firstMap.put(c, set);
        }

        return firstMap.get(c);
    }

    /** Should only be used for debugging purposes **/
    public static void generateAndPrintFirstMap() {
        for (char c: rulesMap.keySet())
            first(c);
        System.out.println(firstMap);
    }
}
