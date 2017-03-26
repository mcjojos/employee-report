package com.jojos.report.jobs;

import com.jojos.report.data.AgeRange;
import com.jojos.report.data.Department;
import com.jojos.report.data.Employee;
import com.jojos.report.data.Genre;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Set;

/**
 * Test to load all employees
 *
 * @author karanikasg@gmail.com
 */
public class LoadEmployeesTest {

    private Loader loader;
    private final Department department1 = new Department("A");
    private final Department department2 = new Department("B");
    private final Department department3 = new Department("C");
    private final Department department4 = new Department("D");
    private final Department department5 = new Department("E");
    private final Department department6 = new Department("F");
    private final Department department7 = new Department("G");
    private final Department department8 = new Department("H");
    private final Department department9 = new Department("I");
    private final Department department10 = new Department("J");

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
        Employee employee4 = new Employee(7, "Maria Kalas", Genre.FEMALE, 5880.00, 29);

        loader.load(employee1);
        loader.load(employee2);
        loader.load(employee3);
        loader.load(employee4);

        Assert.assertEquals(loader.getEmployeesForDepartment(department6).size(), 1);
        Assert.assertEquals(loader.getEmployeesForDepartment(department7).size(), 3);

        Statistics statsIncome7 = Statistics.calculate(loader.getEmployeesIncomeForDepartment(department7));
        Assert.assertEquals(2880.00, statsIncome7.getMedian(), 0d);
        Assert.assertEquals(2650.50, statsIncome7.getMin(), 0d);
        Assert.assertEquals(5880.00, statsIncome7.getMax(), 0d);

        Statistics statsIncome6 = Statistics.calculate(loader.getEmployeesIncomeForDepartment(department6));
        Assert.assertEquals(4350.00, statsIncome6.getMedian(), 0d);
        Assert.assertEquals(4350.00, statsIncome6.getMin(), 0d);
        Assert.assertEquals(4350.00, statsIncome6.getMax(), 0d);

        Statistics statsAge7 = Statistics.calculate(loader.getEmployeesAgeForDepartment(department7));
        Assert.assertEquals(29, statsAge7.getMedian(), 0d);
        Assert.assertEquals(28, statsAge7.getMin(), 0d);
        Assert.assertEquals(35, statsAge7.getMax(), 0d);

        Statistics statsAge6 = Statistics.calculate(loader.getEmployeesAgeForDepartment(department6));
        Assert.assertEquals(23, statsAge6.getMedian(), 0d);
        Assert.assertEquals(23, statsAge6.getMin(), 0d);
        Assert.assertEquals(23, statsAge6.getMax(), 0d);

        Statistics statsIncomeByAgeRange20_30 = Statistics.calculate(loader.getEmployeesIncomeForAgeRange(AgeRange.YEAR_20_30));
        Assert.assertEquals(4350.00, statsIncomeByAgeRange20_30.getMedian(), 0d);
        Assert.assertEquals(2880.00, statsIncomeByAgeRange20_30.getMin(), 0d);
        Assert.assertEquals(5880.00, statsIncomeByAgeRange20_30.getMax(), 0d);

        Statistics statsIncomeByAgeRange30_40 = Statistics.calculate(loader.getEmployeesIncomeForAgeRange(AgeRange.YEAR_30_40));
        Assert.assertEquals(2650.50, statsIncomeByAgeRange30_40.getMedian(), 0d);
        Assert.assertEquals(2650.50, statsIncomeByAgeRange30_40.getMin(), 0d);
        Assert.assertEquals(2650.50, statsIncomeByAgeRange30_40.getMax(), 0d);

        Assert.assertTrue(loader.getAgeRanges().size() == AgeRange.values().length);
    }

    @Test
    public void loadEmployeesWithInvalidDepartment() {
        Employee employee1 = new Employee(0, "Opal Ballard", Genre.FEMALE, 4350.00, 23);

        loader.load(employee1);

        Set<Employee> employees = loader.getAllEmployees();
        Assert.assertTrue(employees.isEmpty());
    }

    @Test
    public void loadEmployeesWithInvalidLargeDepartment() {
        Employee employee1 = new Employee(11, "Opal Ballard", Genre.FEMALE, 4350.00, 23);

        loader.load(employee1);

        Set<Employee> employees = loader.getAllEmployees();
        Assert.assertTrue(employees.isEmpty());
    }

}
