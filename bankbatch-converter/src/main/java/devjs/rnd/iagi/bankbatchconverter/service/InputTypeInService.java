package devjs.rnd.iagi.bankbatchconverter.service;

import java.util.Scanner;

public class InputTypeInService {

    static Scanner in;


    public static void init(){
        in = new Scanner(System.in);
    }
    public static void close(){
        in.close();
    }


    public static String input() {
        System.out.print("Input: ");
       return  in.nextLine();
    }
}
