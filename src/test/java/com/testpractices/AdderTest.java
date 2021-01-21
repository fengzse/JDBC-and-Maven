package com.testpractices;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*; // 一定要static导入

public class AdderTest {
    @Test
    void adderTest(){
        Adder ad=new Adder();
        int result=ad.addN(3,6);
        int expected=10;
        assertEquals(expected, result);
    }
}
