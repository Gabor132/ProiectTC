package com.bajetii.proiecttc;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;

/**
 * Created by alexandru on 23.04.2017.
 */
public class Parser {
    private ParserTable parserTable;
    private List<ProductionRule> rules;

    public Parser(ParserTable parserTable) {
        this.parserTable = parserTable;
        rules = parserTable.getProductionRules();
    }

    public boolean accept(String s) {
        int state = 0, i = 0;
        Deque<Character> stack = new ArrayDeque<>();

        Action action = new Action(ActionType.ERROR, -1);
        while (i < s.length()) {
            char currentChar = s.charAt(i);
            System.out.println("We are at state "+ state + " and we read "+currentChar);
            System.out.println(stack);
            action = parserTable.getAction(state, currentChar);
            System.out.println(action);
            if (action == null || action.type == ActionType.ERROR)
                return false;
            else if (action.type == ActionType.TRANSITION)
                state = action.stateIndex;
            else if (action.type == ActionType.SHIFT) {
                stack.addLast(s.charAt(i));
                state = action.stateIndex;
                i++;
            } else if (action.type == ActionType.REDUCE) {
                reduceStack(action.stateIndex, stack);
                state = recomputeState(stack);
            }
        }
        System.out.println("We are at state "+ state);
        System.out.println(stack);
        action = parserTable.getAction(state, '$');
        System.out.println(action);
        while (action.type != ActionType.ACCEPT && action.type != ActionType.ERROR) {
            
            if (action.type == ActionType.TRANSITION)
                state = action.stateIndex;
            else if (action.type == ActionType.REDUCE) {
                reduceStack(action.stateIndex, stack);
                state = recomputeState(stack);
            }
            action = parserTable.getAction(state, '$');
            System.out.println("We are at state "+ state);
            System.out.println(stack);
            System.out.println(action);
            if (stack.size() == 1 && stack.getFirst() == rules.get(0).from)
                return true;
        }
        return parserTable.getAction(state, '$').type == ActionType.ACCEPT;
    }

    private void reduceStack(int ruleNumber, Deque<Character> stack) {
        ProductionRule rule = rules.get(ruleNumber - 1);
        
        StringBuilder sb = new StringBuilder();
        for (Character c: stack)
            sb.append(c);

        String s = sb.toString();
        String reducedS;
        reducedS = s.replace(rule.to, rule.from + "");
        if(!rule.to.equals("")){
            while (!s.equals(reducedS)) {
                s = reducedS;
                reducedS = s.replace(rule.to, rule.from + "");
            }
        }else{
            s += rule.from;
        }
        stack.clear();
        for (int i = 0; i < s.length(); i++)
            stack.addLast(s.charAt(i));
    }

    private int recomputeState(Deque<Character> stack) {
        int state = 0;
        for (Character c: stack) {
            Action action = parserTable.getAction(state, c);
            if (action.type == ActionType.TRANSITION || action.type == ActionType.SHIFT)
                state = action.stateIndex;
        }

        return state;
    }
}
