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
            if (action.type == ActionType.TRANSITION)
                state = action.stateIndex;
            else if (action.type == ActionType.SHIFT) {
                stack.addLast(s.charAt(i));
                state = action.stateIndex;
                i++;
            } else if (action.type == ActionType.REDUCE) {
                reduceStack(action.stateIndex, stack);
                state = recomputeState(stack);
            } else if (action.type == ActionType.ERROR)
                return false;
        }

        action = parserTable.getAction(state, '$');
        while (action.type != ActionType.ACCEPT && action.type != ActionType.ERROR) {
            if (action.type == ActionType.TRANSITION)
                state = action.stateIndex;
            else if (action.type == ActionType.REDUCE) {
                reduceStack(action.stateIndex, stack);
                state = recomputeState(stack);
            }
        }

        return parserTable.getAction(state, '$').type == ActionType.ACCEPT;
    }

    private void reduceStack(int ruleNumber, Deque<Character> stack) {
        ProductionRule rule = rules.get(ruleNumber - 1);
        
        StringBuilder sb = new StringBuilder();
        for (Character c: stack)
            sb.append(c);

        String s = sb.toString();
        String reducedS = s.replaceAll(rule.to, rule.from + "");
        while (!s.equals(reducedS)) {
            s = reducedS;
            reducedS = s.replaceAll(rule.to, rule.from + "");
        }

        stack.clear();
        for (int i = 0; i < s.length(); i++)
            stack.addLast(s.charAt(i));
    }

    private int recomputeState(Deque<Character> stack) {
        int state = 0;
        for (Character c: stack) {
            Action action = parserTable.getAction(state, c);
            if (action.type == ActionType.TRANSITION)
                state = action.stateIndex;
        }

        return state;
    }
}
