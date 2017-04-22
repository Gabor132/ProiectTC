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
            List<RegulaProductie> reguli = new LinkedList<>();
            while(s.hasNextLine()){
                String[] aux = s.nextLine().split(" ");
                Config auxC;
                Character from = aux[0].charAt(0);
                List<String> to = new LinkedList<>();
                for(int i = 1; i<aux.length; i++){
                    to.add(aux[i]);
                }
                auxC = new Config(from, to);
                reguli.add(new RegulaProductie(from, to));
            }
            System.out.println(reguli);
        }catch(FileNotFoundException ex){
            System.out.println("Du-te acasa");
        }
    }
    
    public static void verificareLR1(List<RegulaProductie> reguli){
        List<String> to = new LinkedList<>();
        to.add("S");
        Config start = new Config('T', to);
        List<Configs> automat = new LinkedList<>();
        Configs I0 = new Configs(Arrays.asList(start));
    }
}
