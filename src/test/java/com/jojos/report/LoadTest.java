package com.jojos.report;

import com.jojos.report.data.Department;
import com.jojos.report.job.Loader;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests correct loading of the data elements
 *
 * @author karanikasg@gmail.com
 */
public class LoadTest {

    @Test
    public void loadDepartments() {
        Department department1 = new Department("Accounting");
        Department department2 = new Department("Human Resources");

        Loader loader = new Loader();

        loader.load(department1);
        loader.load(department2);

        Assert.assertTrue("departments count expected to be 2", loader.departmentsSize() == 2);
    }


    @Test
    public void loadDepartmentsInOrder10Elements() {
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

        Loader loader = new Loader();

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

        Assert.assertTrue("departments count expected to be 10", loader.departmentsSize() == 10);
        Assert.assertEquals(loader.getDepartments().get(0), department1);
        Assert.assertEquals(loader.getDepartments().get(1), department2);
        Assert.assertEquals(loader.getDepartments().get(2), department3);
        Assert.assertEquals(loader.getDepartments().get(3), department4);
        Assert.assertEquals(loader.getDepartments().get(4), department5);
        Assert.assertEquals(loader.getDepartments().get(5), department6);
        Assert.assertEquals(loader.getDepartments().get(6), department7);
        Assert.assertEquals(loader.getDepartments().get(7), department8);
        Assert.assertEquals(loader.getDepartments().get(8), department9);
        Assert.assertEquals(loader.getDepartments().get(9), department10);
    }

}
