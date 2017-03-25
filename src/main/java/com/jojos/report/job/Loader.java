package com.jojos.report.job;

import com.jojos.report.ApplicationException;
import com.jojos.report.data.Department;
import com.jojos.report.data.Employee;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;

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
    private final ConcurrentNavigableMap<Department, Set<Employee>> departmentToEmployee = new ConcurrentSkipListMap<>();

    /**
     * Store the provided department in memory. Don't allow any duplicates.
     * A {@link NullPointerException} is thrown if the department is null.
     *
     * @param department the department to store in memory
     */
    public void load(Department department) {
        if (Objects.isNull(department)) {
            throw new NullPointerException("Null values not allowed for departments");
        }
        int pos = Collections.binarySearch(departments, department);
        // if it's already found don't store the provided department
        if (pos < 0) {
            departments.add(-pos - 1, department);
            departmentToEmployee.put(department, new HashSet<>());
        }
    }

    public void load(Employee employee) {
        if (Objects.isNull(employee)) {
            throw new NullPointerException("Null values not allowed for employees");
        }

        validateDepartmentIdOrThrow(employee.getDepartmentId());

        Department department = departments.get(employee.getDepartmentId() - 1);
        CollectorsUtil.addToContainedSet(departmentToEmployee, department, employee);

    }

    public int departmentsSize() {
        return departments.size();
    }

    public List<Department> getDepartments() {
        return departments;
    }

    public Set<Employee> getEmployeesForDepartment(Department department) {
        return departmentToEmployee.get(department);
    }

    /**
     * A valid department id must correspond to an existing department. If it's not
     * then an {@link ApplicationException} is thrown.
     * Have in mind that the department ids start from 1.
     *
     * @param departmentId the id of the deprtment to validate
     */
    private void validateDepartmentIdOrThrow(int departmentId) throws ApplicationException {
        if (departmentId < 1 || departmentId > departmentsSize()) {
            throw new ApplicationException(String.format("Invalid department id: %d. Departments size is %d", departmentId, departmentsSize()));
        }
    }
}
