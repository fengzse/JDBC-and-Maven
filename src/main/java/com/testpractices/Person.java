package com.testpractices;

public class Person {
    private String firstName;
    private String lastName;
    private int age;
    private boolean hungry;

    public Person() {
        this("Exempelnamn");
    }

    public Person(String firstName) {
        this(firstName,null);
    }

    public Person(String firstName, String lastName) {
        this(firstName,lastName,0);
    }

    public Person(String firstName, String lastName, int age) {
        this(firstName,lastName,age,true);
    }


    public Person(String fName,String lName, int a, boolean h) {
        firstName = fName;
        lastName = lName;
        age = a;
        hungry = h;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String n) {
        if(!n.equals("Vincent")) {
            firstName = n;
        }else {
            System.out.println("Not allowed first name.");
        }
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String n) {
        if(!n.equals("Adler")) {
            lastName = n;
        }else {
            System.out.println("Not allowed surname.");
        }
    }

    public boolean getHungryStatus() {
        return this.hungry;
    }

    public void setHungryStatus(boolean hungry) {
        this.hungry = hungry;
    }

    public String toString() {
        return firstName +" "+ lastName +" "+ age +" "+ hungry;
    }
}