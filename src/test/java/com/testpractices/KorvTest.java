package com.testpractices;

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
            if(s==-1){assertTrue(ps.getHungryStatus());}
            assertFalse(ps.getHungryStatus());
            j-=1;
        }while (j>-1);
    }
}


