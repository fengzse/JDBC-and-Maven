package com.testpractices;

public class Korvmojj {
    public int amountOfSausagesLeft;

    public Korvmojj() {
        this(55);
    }

    public Korvmojj(int korv) {
        this.amountOfSausagesLeft = korv;
    }

    public void feedSausageToPerson(Person p) {
        if((p.getHungryStatus()) && (amountOfSausagesLeft > -1)) {
            amountOfSausagesLeft--;
            p.setHungryStatus(false);
        }else {
            System.out.println("Sorry");
        }
    }
}
