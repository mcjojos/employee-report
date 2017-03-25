package com.jojos.report.job;

import com.jojos.report.data.Department;
import com.jojos.report.data.Employee;
import com.jojos.report.data.Genre;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test to load all employees
 *
 * @author karanikasg@gmail.com
 */
public class LoadEmployeesTest {

    private Loader loader;
    Department department1 = new Department("A");
    Department department2 = new Department("B");
    Department department3 = new Department("C");
    Department department4 = new Department("D");
    Department department5 = new Department("E");
    Department department6 = new Department("F");
    Department department7 = new Department("G");
    Department department8 = new Department("H");
    Department department9 = new Department("I");
    Department department10 = new Department("J");


    @Before
    public void setUp() {
        loader = new Loader();

        // load the departments first
        loader.load(department10);
        loader.load(department9);
        loader.load(department8);
        loader.load(department7);
        loader.load(department6);
        loader.load(department6);
        loader.load(department5);
        loader.load(department4);
        loader.load(department3);
        loader.load(department2);
        loader.load(department1);

    }

    @Test
    public void loadEmployees() {
        Employee employee1 = new Employee(6, "Opal Ballard", Genre.FEMALE, 4350.00, 23);
        Employee employee2 = new Employee(7, "Otis Bell", Genre.MALE, 2650.50, 35);
        Employee employee3 = new Employee(7, "Lynne Ortiz", Genre.FEMALE, 2880.00, 28);

        loader.load(employee1);
        loader.load(employee2);
        loader.load(employee3);

        Assert.assertEquals(loader.getEmployeesForDepartment(department6).size(), 1);
        Assert.assertEquals(loader.getEmployeesForDepartment(department7).size(), 2);
    }
}
