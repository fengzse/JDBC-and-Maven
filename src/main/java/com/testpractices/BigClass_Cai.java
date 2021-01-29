package com.testpractices;

public class BigClass_Cai {
    private String text;
    private int number;

    public BigClass_Cai(int number, String text) {
      this.number = number;
      this.text = text;
    }
    public BigClass_Cai(String text) {
        this(0,text);
    }
    public BigClass_Cai(int number) {
        this(number, null);
    }
    public BigClass_Cai() {
        this(null);
    }
    public void setNumber(int number) {
        if (number >= 0) {
            this.number = number;
        }else {
            System.out.println("Fel input. The number cannot be negative.");
        }
    }
    public int getNumber() {
        return number;
    }
    public void setText(String text) {
        this.text = text;
    }
    public String getText() {
        return text;
    }
    public void uppercase() {
        this.text = this.text.toUpperCase();
    }
    public void changToNull() {
        this.text = null;
    }
    public int add(int newNumber) {
        if (newNumber >= 0) {
            this.number = newNumber + this.number;
        }else {
            System.out.println("Fel input. The new number cannot be negative.");
        }
        return this.number;
    }
    @Override
    public String toString() {
        return "BigClass{" +
                "text='" + text + '\'' +
                ", number=" + number +
                '}';
    }


}

