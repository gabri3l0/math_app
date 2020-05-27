package com.example.mathapp;

import java.util.Random;

public class MathOperations {
    private float result;
    private int first_number;
    private int second_number;
    private String operation;
    public String operation_string;

    public MathOperations(){
        first_number = choose_number();
        second_number = choose_number();
        operation = choose_operation();
        result = make_operation();
        operation_string = text_operation();
    }

    public float getResult(){
        return this.result;
    }


    private int choose_number(){
        return new Random().nextInt(10);
    }

    private String choose_operation(){
        String [] operations = {"SUM", "SUB", "PRODUCT", "DIVIDE"};
        return operations[new Random().nextInt(operations.length)];
    }

    private float make_operation(){
        switch (operation){
            case("SUM"):
                checkSum();
                return first_number + second_number;
            case("SUB"):
                checkSub();
                return first_number - second_number;
            case("PRODUCT"):
                checkProduct();
                return first_number * second_number;
            case("DIVIDE"):
                checkDivide();
                return first_number / second_number;
        }
        return -1;
    }


    private void checkSum(){
        do{
            first_number *= (choose_number() + 1);
            second_number *= (choose_number() + 1);
        }while(first_number + second_number >= 100);
    }

    private void checkSub(){
        do{
            first_number *= (choose_number() + 1);
            second_number *= (choose_number() + 1);

            if (second_number > first_number){
                int tmp = first_number;
                first_number = second_number;
                second_number = tmp;
            }
        }while(first_number - second_number  < 0);
    }

    private void checkProduct(){
        do{
            first_number *= (choose_number() + 1);
            second_number *= (choose_number() + 1);
        }while(first_number * second_number >= 100);
    }

    private void checkDivide(){
        if (second_number > first_number){
            int tmp = first_number;
            first_number = second_number;
            second_number = tmp;
        }

        first_number *= (choose_number() + 1);
        second_number += 1;

        while(((float) first_number/(float)second_number) % 1 != 0.0 ){
            second_number ++;
        }
    }

    private String text_operation(){
        String fn = String.valueOf(first_number);
        String sn = String.valueOf(second_number);
        switch (operation){
            case("SUM"):
                return fn+" + "+ sn;
            case("SUB"):
                return fn+" - "+ sn;
            case("PRODUCT"):
                return fn+" x "+ sn;
            case("DIVIDE"):
                return fn+" / "+ sn;
        }
        return "Error";
    }


}
