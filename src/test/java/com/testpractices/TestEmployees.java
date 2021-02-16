package com.testpractices;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestEmployees {

    Employees emp;
    /*
     use @BeforeEach to init instances of Employees for testing
     do not use @AfterEach to clean static data, current tests need to clean all static datas,
     but in further project-construction, it might cause unexpected errors, keep in mind
     */
    @BeforeEach
    void setUp(){                   // TODO: Create new instance for each test method

        emp=new Employees();
    }

    @Test
    void TestSetFirstName(){            // TODO: to test first name setting
        emp.setFistName("Joe");
        String expected= "Joe";
        String actual = emp.getFistName();
        assertEquals(expected,actual);

        emp.toCleanTestStaticData();        // TODO: to clean static data after testing
        assertEquals(0,emp.getTemp_id());
    }

    @Test
    void TestSetLastName(){             // TODO: to test last name setting
        emp.setLastName("Biden");
        String expected="Biden";
        String actual=emp.getLastName();
        assertEquals(expected,actual);

        emp.toCleanTestStaticData();        // TODO: to clean static data after testing
        assertEquals(0,emp.getTemp_id());
    }

    @Test
    void TestSetAge(){                      // TODO: to test age setting
        emp.setAge(75);
        int expected=75;
        int actual=emp.getAge();
        assertEquals(expected,actual);

        emp.toCleanTestStaticData();        // TODO: to clean static data after testing
        assertEquals(0,emp.getTemp_id());
    }

    @Test
    void TestSetSalary(){                      // TODO: to test salary setting
        emp.setSalary(123456.78);
        double expected=123456.78;
        double actual=emp.getSalary();
        assertEquals(expected,actual);

        emp.toCleanTestStaticData();            // TODO: to clean static data after testing
        assertEquals(0,emp.getTemp_id());
    }

    @Test
    void TestCompanyIdAutoIncrease(){       // TODO: to test if the IDs increase automatically while instances created
        assertEquals(1,emp.getCompanyID());

        Employees emp_2=new Employees();
        assertEquals(2,emp_2.getCompanyID());

        Employees emp_3=new Employees();
        assertEquals(3,emp_3.getCompanyID());

        emp.toCleanTestStaticData();        // TODO: to clean static data after testing
        assertEquals(0, emp.getTemp_id());

    }

    @Test
    void TestAddEmp(){                  // TODO: to test instances of Employees can be added into static list
        Employees emp_2=new Employees();
        Employees emp_3=new Employees();
        Employees emp_4=new Employees();
        Employees emp_5=new Employees();
        emp.addEmployee(emp,emp_2,emp_3,emp_4,emp_5);
        assertEquals(5,emp.getList().size());

        emp.toCleanTestStaticData();        // TODO: to clean static data after testing
        assertEquals(0,emp.getList().size());
        assertEquals(0,emp.getTemp_id());
    }

    @Test
    void TestRemoveFromList(){                  // TODO: to test instances of Employees can be removed from list
        Employees emp_2=new Employees();
        Employees emp_3=new Employees();
        Employees emp_4=new Employees();
        Employees emp_5=new Employees();

        emp.addEmployee(emp,emp_2,emp_3,emp_4,emp_5);
        assertEquals(5,emp.getList().size());

        emp.removeFromList(emp_4);
        assertEquals(4,emp.getList().size());

        emp.toCleanTestStaticData();            // TODO: to clean static data after testing
        assertEquals(0,emp.getList().size());
        assertEquals(0,emp.getTemp_id());
    }

    @Test
    void TestSalaryIncrease(){                  // TODO: to test salary can be increased correctly
        emp.setSalary(12345.67);
        double percentage_1=0.25;
        double percentage_2=1.2;
        double percentage_3=(-0.2);

        double expected_1=12345.67*percentage_1+12345.67;
        double expected_2=12345.67*percentage_2+12345.67;
        double expected_3=12345.67*percentage_3+12345.67;

        emp.SalaryIncr(emp,percentage_1);
        double actual_1 = emp.getSalary();
        emp.SalaryIncr(emp,percentage_2);
        double actual_2=emp.getSalary();
        emp.SalaryIncr(emp,percentage_3);
        double actual_3=emp.getSalary();

        assertEquals(expected_1,actual_1);
        assertNotEquals(expected_2,actual_2);
        assertNotEquals(expected_3,actual_3);

        emp.toCleanTestStaticData();            // TODO: to clean static data after testing
        assertEquals(0,emp.getList().size());
        assertEquals(0,emp.getTemp_id());
    }

    @Test
        // TODO: to test any objs stored in the list can be selected and increase salary
    void TestSalaryIncreaseFromList(){
        Employees emp_2=new Employees();
        Employees emp_3=new Employees();
        Employees emp_4=new Employees();
        Employees emp_5=new Employees();

        emp.setSalary(12345.67);
        emp_2.setSalary(23456.78);
        emp_3.setSalary(22222.33);
        emp_4.setSalary(33333.55);
        emp_5.setSalary(11111.99);

        emp.addEmployee(emp,emp_2,emp_3,emp_4,emp_5);

        double percentage=0.3;
        double expected_emp_3=22222.33*percentage+22222.33;
        double expected_emp_5=11111.99*percentage+11111.99;

        emp.SalaryIncrSpecificEplee(emp_3,percentage);
        double actual_emp_3=emp_3.getSalary();
        emp.SalaryIncrSpecificEplee(emp_5,percentage);
        double actual_emp_5=emp_5.getSalary();

        assertEquals(expected_emp_3,actual_emp_3);
        assertEquals(expected_emp_5,actual_emp_5);

        emp.toCleanTestStaticData();                // TODO: to clean static data after testing
        assertEquals(0,emp.getList().size());
        assertEquals(0,emp.getTemp_id());
    }
}
