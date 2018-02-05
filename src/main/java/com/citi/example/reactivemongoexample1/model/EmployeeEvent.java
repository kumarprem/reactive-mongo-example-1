package com.citi.example.reactivemongoexample1.model;

import java.util.Date;

public class EmployeeEvent {



    private  Employee employee;
    private Date date;
    public EmployeeEvent(Employee employee, Date date){
        this.employee =employee;
        this.date =date;
    }
    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
    @Override
    public String toString() {
        return " data: first line \n\n";
    }


}
