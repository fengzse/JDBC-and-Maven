package com.testpractices;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class Employees{

    private static List<Employees> employees = new ArrayList<>();
    private String fistName;
    private String lastName;
    private int age;
    private double salary;
    private static int temp_id=0;
    private int companyID;

    Employees(String fistName, String lastName, int age, double salary){
        this.fistName=fistName;
        this.lastName=lastName;
        this.age=age;
        this.salary=salary;
        temp_id+=1;
        this.companyID=temp_id;
    }
    Employees(){

        this(null,null,0,0);
    }



    public String getFistName() {

        return fistName;
    }

    public void setFistName(String fistName) {

        this.fistName = fistName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public int getAge() {

        return age;
    }

    public void setAge(int age) {

        this.age = age;
    }

    public double getSalary() {

        return salary;
    }

    public void setSalary(double salary) {

        this.salary = salary;
    }

    public int getTemp_id(){
        return temp_id;
    }

    public int getCompanyID() {

        return companyID;
    }

    public List<Employees> getList(){
        return employees;
    }

    public void addEmployee(Employees...emps){

        employees.addAll(Arrays.asList(emps));
    }

    public void removeFromList(Employees emp) {

        employees.remove(emp);
    }



    public void toCleanTestStaticData(){
        employees.clear();
        temp_id=0;
    }

    public static void SalaryIncr(Employees sepcificEmployee, double percentage) {
        if(percentage>0 && percentage<=1){
            sepcificEmployee.setSalary(sepcificEmployee.salary+=sepcificEmployee.salary*percentage);
        }else {
            System.out.println("Accepted salary increase can not exceed 100% or decrease");
        }
    }

    public static void SalaryIncrSpecificEplee(Employees SpecificEmployee, double percentage) {
        for (Employees employee : employees) {
            if (employee == SpecificEmployee) {
                SalaryIncr(SpecificEmployee,percentage);
            }
        }
    }
}
