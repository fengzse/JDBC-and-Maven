package com.testpractices;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.*;

public class KorvTest {
    @ParameterizedTest
    @ValueSource(ints = {5,10,15,20,55})
    void testKorv(int s){
        int j=s;
        Person ps;
        Korvmojj kvm=new Korvmojj(s);
        do{
            ps=new Person();
            kvm.feedSausageToPerson(ps);
            j-=1;
        }while (j>=-1);
        System.out.println(ps.getHungryStatus());
        assertTrue(ps.getHungryStatus());
    }

    @ParameterizedTest
    @ValueSource(ints = {5,10,15,20,55})
    void testKrov_2(int s){
        int t=s;
        Person ps_2;
        Korvmojj kvm_2=new Korvmojj(s);
        do{
            ps_2=new Person();
            kvm_2.feedSausageToPerson(ps_2);
            t-=1;
        }while (t>-1);
        assertFalse(ps_2.getHungryStatus());
    }

    @Test
    void testKrov_2(){
        Person ps;
        Korvmojj kvm=new Korvmojj();
        int k=55;
        do{
            ps=new Person();
            kvm.feedSausageToPerson(ps);
            k-=1;
        }while (k>=-1);
        assertTrue(ps.getHungryStatus());
    }
}


