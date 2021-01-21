package com.testpractices;
import java.util.Random;
public class Dice {
    private int sides;
    private int value;

    public Dice(int s){
        sides=s;
    }

    public void roll(){
        Random rd=new Random();
        value=rd.nextInt(sides)+1;
    }

    public int getValue() {
        return value;
    }
}

