package com.testpractices;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
public class PersonTest {
    @Test
    void testPerson(){
        Person p5= new Person("HC","Zhu",39,false);
        Person P1=new Person();
        Person p2=new Person("Maria","Lund",20);
        Person p3=new Person("Hanna");
        Person P4=new Person("Kevin","Z");

        String expected_1="Exempelnamn null 0 true";
        assertEquals(expected_1,P1.toString());

        String expected_2, expected_3, expected_4,expected_5,expected_6,expected_7;
        expected_2="Maria Lund 20 true";
        assertEquals(expected_2,p2.toString());

        expected_3="Hanna null 0 true";
        assertEquals(expected_3,p3.toString());

        expected_4="Kevin Z 0 true";
        assertEquals(expected_4, P4.toString());

        expected_5="HC Zhu 39 false";
        assertEquals(expected_5, p5.toString());

        expected_6="Vincent";
        expected_7="Adler";
        p5.setFirstName(expected_6);
        assertNotEquals(expected_6,p5.getFirstName());
        p5.setFirstName(expected_7);
        assertEquals(expected_7,p5.getFirstName());

        p5.setLastName(expected_7);
        assertNotEquals(expected_7,p5.getLastName());
        p5.setLastName(expected_6);
        assertEquals(expected_6,p5.getLastName());

        p2.setHungryStatus(false);
        assertFalse(p2.getHungryStatus());
        p5.setHungryStatus(true);
        assertTrue(p5.getHungryStatus());
    }
}
