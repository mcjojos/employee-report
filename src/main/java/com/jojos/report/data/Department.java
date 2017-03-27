package com.jojos.report.data;

/**
 * Data object which is representing a department
 *
 * @author karanikasg@gmail.com
 */
public class Department implements Comparable<Department> {

    private final String name;

    public Department(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Department that = (Department) o;

        return name != null ? name.equals(that.name) : that.name == null;
    }

    public String getName() {
        return name;
    }

    @Override
    public int hashCode() {
        return name != null ? name.hashCode() : 0;
    }

    @Override
    public String toString() {
        return "Department{" +
                name + '}';
    }

    @Override
    public int compareTo(Department that) {
        if (this.name == that.name) {
            return 0;
        }

        if (this.name == null) {
            return -1;
        }

        return this.name.compareTo(that.name);
    }
}
