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

}
