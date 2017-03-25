package com.jojos.report.job;

import com.jojos.report.data.Department;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.TreeMap;

/**
 * The purpose of the loader class is to load departments, employees and it's attributes
 *
 * The internal representation remains hidden from the outside world by providing only the API
 * necessary to expose retrieving elements and keeping them in order.
 *
 * TODO:
 * More specifically the departments are kept sorted by alphabetic order.
 *
 * @author karanikasg@gmail.com
 */
public class Loader {

    private final List<Department> departments = new ArrayList<>();

    public void load(Department department) {
        int pos = Collections.binarySearch(departments, department);
        if (pos < 0) {
            departments.add(-pos-1, department);
        }
    }

    public int departmentsSize() {
        return departments.size();
    }

    public List<Department> getDepartments() {
        return departments;
    }
}
