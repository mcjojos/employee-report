package com.jojos.report.data;

/**
 * Represent an employee. Having a relation with a department, a name, genre information and salary
 *
 * @author karanikasg@gmail.com
 */
public class Employee {

    private final int departmentId;
    private final String name;
    private final Genre genre;
    private final double salary;
    private final int age;

    public Employee(int departmentId, String name, Genre genre, double salary, int age) {
        this.departmentId = departmentId;
        this.name = name;
        this.genre = genre;
        this.salary = salary;
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

    public double getSalary() {
        return salary;
    }

    public int getAge() {
        return age;
    }
}
