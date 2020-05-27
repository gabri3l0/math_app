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

    public int getFirst_number(){
        return first_number;
    }

    public int getSecond_number() {
        return second_number;
    }

    public String getOperation() {
        return operation;
    }

    private int choose_number(){
        return new Random().nextInt(10);
    }

    private String choose_operation(){
        String [] operations = {"+", "-", "x", "/"};
        return operations[new Random().nextInt(operations.length)];
    }

    private float make_operation(){
        switch (operation){
            case("+"):
                checkSum();
                return first_number + second_number;
            case("-"):
                checkSub();
                return first_number - second_number;
            case("x"):
                checkProduct();
                return first_number * second_number;
            case("/"):
                checkDivide();
                return first_number / second_number;
        }
        return -1;
    }


    private void checkSum(){
        first_number *= (choose_number() + 1);
        do{
            if (first_number + second_number >= 100)
                second_number = 1;
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
            case("+"):
                return fn+" + "+ sn;
            case("-"):
                return fn+" - "+ sn;
            case("x"):
                return fn+" x "+ sn;
            case("/"):
                return fn+" / "+ sn;
        }
        return "Error";
    }


}
