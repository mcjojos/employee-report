package com.jojos.report.jobs;

import com.jojos.report.ApplicationException;
import com.jojos.report.Util;
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
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The purpose of the loader class is to load departments, employees and it's attributes
 *
 * The internal representation remains hidden from the outside world by providing only the API
 * necessary to expose retrieving elements and keeping them in order.
 *
 * @implNote The departments are kept sorted in an indexed based collection in alphabetic order. There is
 * also a sorted map (which is also concurrent although not strictly needed with our provided implementation) that maps
 * departments to Sets of employees. Also a similar mapping of {@link AgeRange}s to Set of employees exist and
 * is implemented as an {@link EnumMap} since the AgeRange is just an enumeration. The latter is initialized at
 * construction time.
 *
 * @author karanikasg@gmail.com
 */
public class Loader {

    private final Logger log = Logger.getLogger(getClass().getName());

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

    /**
     * Store the provided employee in memory. A {@link NullPointerException}
     * is thrown if the employee is null.
     *
     * @param employee the employee to store in memory
     */
    public void load(Employee employee) {
        if (Objects.isNull(employee)) {
            throw new NullPointerException("Null values not allowed for employees");
        }

        int departmentId = employee.getDepartmentId();
        if (isInValidDepartmentId(departmentId)) {
            log.severe(String.format("Skipping employee %s. Invalid department id: %d. Departments size is %d", employee.getName(), departmentId, departmentsSize()));
            return;
        }

        Department department = departments.get(employee.getDepartmentId() - 1);
        Util.addToContainedSet(departmentToEmployee, department, employee);
        AgeRange ageRange = AgeRange.forAge(employee.getAge());
        // We've already initialized our EnumMap with empty sets, so this operation is safe.
        ageRanges.get(ageRange).add(employee);
    }

    /**
     * The size of all departments
     * @return departments' size
     */
    public int departmentsSize() {
        return departments.size();
    }

    /**
     * Getter for all departments
     * @return stored departments
     */
    public List<Department> getDepartments() {
        return departments;
    }

    /**
     * Getter for a collection of every employee that belong to the provided department
     * @param department the department in question
     * @return all the employees that belong to the specific department
     */
    public Set<Employee> getEmployeesForDepartment(Department department) {
        return departmentToEmployee.get(department);
    }

    /**
     * Getter for all employees currently stored in memory
     * @return A collection of distinct employees
     */
    public Set<Employee> getAllEmployees() {
        return departmentToEmployee.values()
                .stream()
                .flatMap(Set::stream)
                .collect(Collectors.toSet());
    }

    /**
     * Gets all employees income for a specific department
     * @param department the department in question
     * @return an (unordered) collection of income
     */
    public Collection<Double> getEmployeesIncomeForDepartment(Department department) {
        return getEmployeesForDepartment(department)
                .stream()
                .map(Employee::getIncome)
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
                .map(Employee::getAge)
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
                .map(Employee::getIncome)
                .collect(Collectors.toList());
    }

    /**
     * Getter for the {@link EnumMap} of all the age ranges
     * @return the map of age ranges to set of employees
     */
    public EnumMap<AgeRange, Set<Employee>> getAgeRanges() {
        return ageRanges;
    }

    /**
     * A valid department id must correspond to an existing department. If it's not
     * then an {@link ApplicationException} is thrown.
     * Have in mind that the department ids start from 1.
     *
     * @param departmentId the id of the department to validate
     */
    private boolean isInValidDepartmentId(int departmentId) throws ApplicationException {
        return departmentId < 1 || departmentId > departmentsSize();
    }
}
