package com.testpractices;


public class BigClass {
    private int number;
    private String text;

    public BigClass(){
        this(0,null);
    }
    public BigClass(int number){

        this(number,null);
    }
    public BigClass(String text){
        this(0,text);
    }
    public BigClass(int number,String text){
        this.number=number;
        this.text=text;
    }

    public int getNumber(){
        return number;
    }
    public void setNumber(int num){
        if(num>=0){
            number=num;
        }
        else {
            System.out.println("Negative number is not allowed.");
        }

    }
    public String getText(){
        return text;
    }
    public void setText(String txt){
        text=txt;
    }

    public String toUpp(){
        return text.toUpperCase();
    }

    public void setToNull(){
        text= null;
    }

    public void addNumber(int num){
        if(num>=0){
            number+=num;
        }else{
            System.out.println("Negative number is not allowed.");
        }
    }
}
