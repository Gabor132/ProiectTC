package com.bajetii.proiecttc;

import com.sun.xml.internal.bind.v2.runtime.reflect.opt.Const;

import java.util.HashSet;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.List;
/**
 * Created by alexandru on 22.04.2017.
 */
public class Utils {
    public static Map<String, HashSet<String>> firstMap;
    private static List<ProductionRule> rules;

    static {
        firstMap = new HashMap<>();
    }

    public static void initFirstMap(List<ProductionRule> rules) {
        Utils.rules = rules;
        for (ProductionRule rule: rules) {
            processElem(rule.from, rules);
            for (String s: rule.to)
                for (int i = 0; i < s.length(); i++)
                    processElem(s.charAt(i), rules);
        }
    }

    private static void processElem(char c, List<ProductionRule> rules) {
        String index = c + "";
        if (c == Constants.LAMBDA.getValue() || Character.isLowerCase(c)) {
            Utils.first(index).add(index);
            Utils.first(index).remove(Constants.LAMBDA.getValue());
        }
        else
            for (ProductionRule rule: rules) {
                if (rule.from == c) {
                    for (String aux: rule.to)
                        for (int i = 0; i < aux.length(); i++)
                            Utils.first(index).addAll(first(aux.charAt(i) + ""));
                    if (Utils.first(index).size() > 1)
                        Utils.first(index).remove(Constants.LAMBDA.getValue());
                }
            }
    }


    public static Set<String> first(String s) {
        if (firstMap.get(s) == null) {
            firstMap.put(s, new HashSet<>());
            if (Character.isLowerCase(s.charAt(0)))
                firstMap.get(s).add(s);
            else
                processElem(s.charAt(0), rules);
                //firstMap.get(s).add(Constants.LAMBDA.getValue() + "");
        }
        return firstMap.get(s);
    }


}
