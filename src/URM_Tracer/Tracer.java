/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package URM_Tracer;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mary'sRose
 */
public class Tracer {

    private static Integer[] URM;
    private static ArrayList<String> instructionSet;
    private static ArrayList<String> outputStates;
    private static int instCtr;
    private static boolean done;
    private static String inputFileName;

    public static void main(String[] args) {
        init();

        Scanner sc = new Scanner(System.in);
        System.out.print("Enter file name: ");
        inputFileName = sc.nextLine();

        readInputFile(inputFileName);
        trace();
        writeOutputFile();
    }

    public static void init() {
        URM = new Integer[10];
        instructionSet = new ArrayList<>();
        outputStates = new ArrayList<>();
        instCtr = 0;
        done = false;
    }

    public static void trace() {
        System.out.println("tracing...");
        while (!done) {
            if (instCtr == instructionSet.size()) {
                done = true;
            } else {
                execute(instructionSet.get(instCtr));
            }
        }
    }

    public static void readInputFile(String fname) {
        System.out.println("reading input file...");
        try {
            /*reads input values from file*/
            FileReader reader = new FileReader(fname);
            BufferedReader br = new BufferedReader(reader);
            String line;
            
            /*
            * first line in file contains initial values for the URM
            * while all the rest are to be taken as part of
            * in the instruction set.
            */
            for (boolean start = true; (line = br.readLine()) != null;) {
                if (start) {
                    String[] ln = line.split("\\s");
                    for (int i = 0; i < ln.length; i++) {
                        URM[i] = Integer.parseInt(ln[i]);
                    }
                    start = false;

                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < URM.length; i++) {
                        sb.append(String.valueOf(URM[i]));
                    }
                    outputStates.add(sb.toString());
                    
                } else {
                    instructionSet.add(line);
                }
            }

        } catch (FileNotFoundException ex) {
            Logger.getLogger(Tracer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Tracer.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NumberFormatException ex) {
            System.out.println("Invalid Input Format");
        }
        /*
        * Add initial values of the URM as 
        * initial state
        */
        outputState();
    }

    public static void successor(int index) {
        URM[index] += 1;
        outputState();
        instCtr++;
    }

    public static void zero(int index) {
        URM[index] = 0;
        outputState();
        instCtr++;
    }

    public static void copy(int m, int n) {
        URM[n] = URM[m];
        outputState();
        instCtr++;
    }

    public static void jump(int m, int n, int line) {
        if (Objects.equals(URM[m], URM[n])) {
            if (line == instructionSet.size()) {
                done = true;
            } else {
                instCtr = line - 1;
            }
            trace();
        } else {
            instCtr++;
        }
    }

    public static void execute(String inst) {
        String[] in = inst.split(" ");
        String cmd = in[0];

        switch (cmd) {
            case "S":
                if (in.length != 2) {
                    System.out.println("Invalid number of parameters!");
                } else {
                    successor(Integer.parseInt(in[1]));

                }
                break;

            case "Z":
                if (in.length != 2) {
                    System.out.println("Invalid number of parameters!");
                } else {
                    zero(Integer.parseInt(in[1]));
                }
                break;

            case "C":
                if (in.length != 3) {
                    System.out.println("Invalid number of parameters!");
                } else {
                    copy(Integer.parseInt(in[1]), Integer.parseInt(in[2]));
                }
                break;

            case "J":
                if (in.length != 4) {
                    System.out.println("Invalid number of parameters!");
                } else {
                    if (instCtr < instructionSet.size()) {
                        jump(Integer.parseInt(in[1]), Integer.parseInt(in[2]), Integer.parseInt(in[3]));
                    }
                }
                break;

            default:
                System.out.println("Invalid Command!");
                break;
        }
    }

    public static void writeOutputFile() {
        System.out.println("writing output file...");
        try {
            PrintWriter writer = new PrintWriter(inputFileName + ".out", "UTF-8");
            for (String s : outputStates) {
                writer.println(s);
            }
            writer.close();
        } catch (IOException ex) {
            Logger.getLogger(Tracer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void outputState() {
        /*
        * Adds current state of URM to 
        * the list of output states
        */
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < URM.length; i++) {
            sb.append(String.valueOf(URM[i]));
        }
        outputStates.add(sb.toString());
    }
}
