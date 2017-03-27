package com.jojos.report.data;

/**
 * Represent an employee. Having
 * a relation with a department,
 * a name,
 * genre information,
 * income
 * and age
 *
 * @author karanikasg@gmail.com
 */
public class Employee {

    private final int departmentId;
    private final String name;
    private final Genre genre;
    private final double income;
    private final int age;

    public Employee(int departmentId, String name, Genre genre, double income, int age) {
        this.departmentId = departmentId;
        this.name = name;
        this.genre = genre;
        this.income = income;
        this.age = age;
    }

    public int getDepartmentId() {
        return departmentId;
    }

    public String getName() {
        return name;
    }

    public Genre getGenre() {
        return genre;
    }

    public double getIncome() {
        return income;
    }

    public int getAge() {
        return age;
    }
}
