package com.jojos.report;

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
        Department department = new Department("Accounting");
        Department department = new Department("Human Resources");

        Loader loader = new Loader();

        loader.load(department);

        Assert.assertTrue("departments count expected to be 2", loader.departmentsSize() == 2);
    }

}
