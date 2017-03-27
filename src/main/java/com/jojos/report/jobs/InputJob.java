package com.jojos.report.jobs;

import com.jojos.report.ApplicationException;
import com.jojos.report.data.Department;
import com.jojos.report.data.Employee;
import com.jojos.report.data.Genre;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Scanner;
import java.util.logging.Logger;

import static com.jojos.report.Util.AGES_FILE;
import static com.jojos.report.Util.DEPARTMENTS_FILE;
import static com.jojos.report.Util.EMPLOYEES_FILE;

/**
 * The class is responsible for extracting from a folder all three files
 * needed for the program to calculate the statistics:
 *
 * ages.csv
 * departments.csv
 * employees.csv
 *
 * @author karanikasg@gmail.com
 */
public class InputJob {

    private final Logger log = Logger.getLogger(getClass().getName());

    private final File ages;
    private final File departments;
    private final File employees;
    private final Loader loader;

    public InputJob(String path) {
        File tmpAges = null;
        File tmpDepartments = null;
        File tmpEmployees = null;

        File[] files = new File(path).listFiles();

        if (files == null) {
            String msg = String.format("This path %s does not denote a directory", path);
            log.severe(msg);
            throw new ApplicationException(msg);
        }

        for (File file : files) {
            if (file.isFile()) {
                switch (file.getName()) {
                    case AGES_FILE:
                        tmpAges = file;
                        break;
                    case DEPARTMENTS_FILE:
                        tmpDepartments = file;
                        break;
                    case EMPLOYEES_FILE:
                        tmpEmployees = file;
                        break;
                    default:
                        log.warning(String.format("skipping %s, not a valid file", file.getName()));
                        break;
                }
            }
        }

        if (Objects.isNull(tmpAges) || Objects.isNull(tmpDepartments) || Objects.isNull(tmpEmployees)) {
            throw new ApplicationException(String.format("Make sure all required files " +
                    "(%s, %s, %s) are in the provided path %s", AGES_FILE, DEPARTMENTS_FILE, EMPLOYEES_FILE, path));
        }

        this.ages = tmpAges;
        this.departments = tmpDepartments;
        this.employees = tmpEmployees;
        this.loader = new Loader();

    }

    public Loader parseAndLoad() {
        // departments first
        try (Scanner scanner = new Scanner(departments)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                validateLineOrThrow(line);
                loader.load(new Department(line));
            }
            log.info(String.format("Loaded %d departments", loader.departmentsSize()));
        } catch (FileNotFoundException e) {
            // highly unlikely
            throw new ApplicationException(e.getMessage());
        }

        Map<String, Integer> namesAges = new HashMap<>();
        // secondly store the ages
        try (Scanner scanner = new Scanner(this.ages)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                validateLineOrThrow(line);
                String[] nameAge = line.split("\\s*,\\s*");
                if (isInvalidSplitOfLine(nameAge, 2)) {
                    log.severe(String.format("Wrong format in input %s at line %d", ages.getName(), line));
                    continue;
                }
                namesAges.put(nameAge[0], Integer.parseInt(nameAge[1]));
            }
        } catch (FileNotFoundException e) {
            // highly unlikely
            throw new ApplicationException(e.getMessage());
        }

        // at last create the employees objects
        try (Scanner scanner = new Scanner(this.employees)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                validateLineOrThrow(line);
                String[] employeeData = line.split("\\s*,\\s*");
                if (isInvalidSplitOfLine(employeeData, 4)) {
                    log.severe(String.format("Wrong format in input %s at line %s", employees.getName(), line));
                    continue;
                }
                Employee employee = extractEmployeeFromArray(employeeData, namesAges);
                if (Objects.isNull(employee)) {
                    log.severe("Skipping employee " + line);
                } else {
                    loader.load(employee);
                }
            }
            log.info(String.format("Loaded %d employees", loader.getAllEmployees().size()));
        } catch (FileNotFoundException e) {
            // highly unlikely
            throw new ApplicationException(e.getMessage());
        }

        return loader;
    }

    private Employee extractEmployeeFromArray(String[] employeeData, Map<String, Integer> ages) {
        try {
            int departmentId = Integer.parseInt(employeeData[0]);
            String name = employeeData[1];
            Genre genre = Genre.forCaseInsensitiveAbbreviation(employeeData[2]);
            double income = Double.parseDouble(employeeData[3]);
            int age = ages.getOrDefault(name, -1);
            return new Employee(departmentId, name, genre, income, age);
        } catch (NumberFormatException e) {
            log.severe(e.getMessage());
            return null;
        }
    }

    private void validateLineOrThrow(String line) {
        if (Objects.isNull(line) || line.equals("")) {
            throw new ApplicationException("Invalid line " + line);
        }
    }

    private boolean isInvalidSplitOfLine(String[] strings, int expectedLength) {
        return Objects.isNull(strings) || strings.length != expectedLength;

    }
}
