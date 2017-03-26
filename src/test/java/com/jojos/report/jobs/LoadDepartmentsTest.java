package com.jojos.report.jobs;

import com.jojos.report.data.Department;
import org.junit.Assert;
import org.junit.Test;

/**
 * Tests correct loading of the data elements
 *
 * @author karanikasg@gmail.com
 */
public class LoadDepartmentsTest {

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

    @Test
    public void loadDepartmentsInOrderNoDuplicates() {
        Department department1 = new Department("A");
        Loader loader = new Loader();

        loader.load(department1);
        loader.load(department1);
        loader.load(department1);

        Assert.assertTrue("department list expected to contain no duplicates", loader.getDepartments().size() == 1);

        Department department2 = new Department("A");
        loader.load(department2);

        Assert.assertTrue("department list expected to contain no duplicates", loader.getDepartments().size() == 1);
    }

    @Test(expected = NullPointerException.class)
    public void loadNullDepartments() {
        Department department1 = null;
        Loader loader = new Loader();

        loader.load(department1);
    }

    @Test
    public void loadEmptyDepartments() {
        Department department1 = new Department("");
        Department department2 = new Department("A");
        Department department3 = new Department("Z");
        Department department4 = new Department("");
        Loader loader = new Loader();

        loader.load(department1);
        loader.load(department2);
        loader.load(department3);
        loader.load(department4);

        Assert.assertTrue("department list expected to contain 3 elements", loader.getDepartments().size() == 3);
    }

    @Test
    public void loadCaseInsensitiveDepartments() {
        Department department1 = new Department("A");
        Department department2 = new Department("a");
        Department department3 = new Department("b");
        Department department4 = new Department("B");
        Department department5 = new Department("C");
        Department department6 = new Department("c");
        Department department7 = new Department("d");
        Department department8 = new Department("D");
        Department department9 = new Department("E");
        Department department10 = new Department("e");
        Department department11 = new Department("z");
        Department department12 = new Department("Z");

        Loader loader = new Loader();

        loader.load(department12);
        loader.load(department11);
        loader.load(department10);
        loader.load(department9);
        loader.load(department8);
        loader.load(department7);
        loader.load(department6);
        loader.load(department5);
        loader.load(department4);
        loader.load(department3);
        loader.load(department2);
        loader.load(department1);

        Assert.assertEquals(12, loader.departmentsSize());
        Assert.assertEquals(loader.getDepartments().get(0), department1);
        Assert.assertEquals(loader.getDepartments().get(1), department4);
        Assert.assertEquals(loader.getDepartments().get(2), department5);
        Assert.assertEquals(loader.getDepartments().get(3), department8);
        Assert.assertEquals(loader.getDepartments().get(4), department9);
        Assert.assertEquals(loader.getDepartments().get(5), department12);
        Assert.assertEquals(loader.getDepartments().get(6), department2);
        Assert.assertEquals(loader.getDepartments().get(7), department3);
        Assert.assertEquals(loader.getDepartments().get(8), department6);
        Assert.assertEquals(loader.getDepartments().get(9), department7);
        Assert.assertEquals(loader.getDepartments().get(10), department10);
        Assert.assertEquals(loader.getDepartments().get(11), department11);
    }


}
