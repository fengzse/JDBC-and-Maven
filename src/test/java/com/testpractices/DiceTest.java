package com.testpractices;
import org.junit.jupiter.params.ParameterizedTest; // 依赖junit-jupiter-params
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class DiceTest {
    @ParameterizedTest
    @ValueSource(ints = {4,6,8,10,12,20,100})
    void dieT(int s){
        Dice de=new Dice(s);
        de.roll();
        int actual;
        actual=de.getValue();
        assertTrue(actual>0 && actual<=s);

    }
}
