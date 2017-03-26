package com.jojos.report.jobs;

import com.jojos.report.ApplicationException;
import com.jojos.report.data.AgeRange;
import com.jojos.report.data.Department;
import com.jojos.report.data.Employee;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The purpose of the loader class is to load departments, employees and it's attributes
 *
 * The internal representation remains hidden from the outside world by providing only the API
 * necessary to expose retrieving elements and keeping them in order.
 *
 * TODO Impl Details:
 * More specifically the departments are kept sorted by alphabetic order.
 *
 * @author karanikasg@gmail.com
 */
public class Loader {

    private final List<Department> departments = new ArrayList<>();
    private final ConcurrentNavigableMap<Department, Set<Employee>> departmentToEmployee = new ConcurrentSkipListMap<>();
    private final EnumMap<AgeRange, Set<Employee>> ageRanges;

    public Loader() {
        ageRanges = new EnumMap<>(AgeRange.class);
        Stream.of(AgeRange.values()).forEach(ageRange -> ageRanges.put(ageRange, new HashSet<>()));
    }

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
        AgeRange ageRange = AgeRange.forAge(employee.getAge());
        // We've already initialized our EnumMap with empty sets, so this operation is safe.
        ageRanges.get(ageRange).add(employee);
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
     * Gets all employees income for a specific department
     * @param department the department in question
     * @return an (unordered) collection of income
     */
    public Collection<Double> getEmployeesIncomeForDepartment(Department department) {
        return getEmployeesForDepartment(department)
                .stream()
                .map(employee -> employee.getIncome())
                .collect(Collectors.toList());
    }

    /**
     * Gets all employees age for a specific department
     * @param department the department in question
     * @return an (unordered) collection of ages
     */
    public Collection<Integer> getEmployeesAgeForDepartment(Department department) {
        return getEmployeesForDepartment(department)
                .stream()
                .map(employee -> employee.getAge())
                .collect(Collectors.toList());
    }

    /**
     * Gets all employees income for a specific {@link AgeRange}
     * @param ageRange the range of age in question
     * @return an (unordered) collection of income
     */
    public Collection<Double> getEmployeesIncomeForAgeRange(AgeRange ageRange) {
        return ageRanges.get(ageRange)
                .stream()
                .map(employee -> employee.getIncome())
                .collect(Collectors.toList());
    }

    public EnumMap<AgeRange, Set<Employee>> getAgeRanges() {
        return ageRanges;
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
