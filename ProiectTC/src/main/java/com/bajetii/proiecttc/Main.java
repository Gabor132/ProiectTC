/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.bajetii.proiecttc;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;

/**
 *
 * @author Dragos
 */
public class Main {
    
    public static void main(String[] args){
        try(Scanner s = new Scanner(new File("input.txt"))){
            List<ProductionRule> reguli = new LinkedList<>();
            while(s.hasNextLine()){
                String from = s.next();
                String to = s.next();
                reguli.add(new ProductionRule(from.charAt(0), to));
            }
            System.out.println(reguli);
            Utils.initUtils(reguli);
            Utils.generateAndPrintFirstMap();
        }catch(FileNotFoundException ex) {
            System.out.println("Du-te acasa");
        }
    }
    
    public static void verificareLR1(List<ProductionRule> reguli){
        List<String> to = new LinkedList<>();
        to.add("S");
        Config start = new Config('T', to);
        List<Configs> automat = new LinkedList<>();
        Configs I0 = new Configs(Arrays.asList(start));
    }
}
