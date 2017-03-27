package com.jojos.report.jobs;

import com.jojos.report.data.AgeRange;
import com.jojos.report.data.Department;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static com.jojos.report.Util.INCOME_BY_DEPARTMENT;
import static com.jojos.report.Util.INCOME_95_BY_DEPARTMENT;
import static com.jojos.report.Util.INCOME_AVERAGE_BY_AGE_RANGE;
import static com.jojos.report.Util.EMPLOYEE_AGE_BY_DEPARTMENT;
import static com.jojos.report.Util.DELIMITER;

/**
 * The job that outputs the following files
 * income-by-department.csv - median income by department
 * income-95-by-department.csv - 95-percentile income by department
 * income-average-by-age-range.csv - average income by age ranges with factor of ten
 * employee-age-by-department.csv - median employee age by department
 *
 * @author karanikasg@gmail.com
 */
public class OutputJob {
    private final Logger log = Logger.getLogger(getClass().getName());

    private final Loader loader;
    private final String directoryPath;

    public OutputJob(Loader loader, String directoryPath) {
        this.loader = loader;
        this.directoryPath = directoryPath;
    }

    /**
     * The main functionality of the class is reflected in this class which
     * will generate the reports for all described statistics.
     */
    public void generateReports() {
        generateMedianIncomeByDepartment();
        generate95PercentileIncomeByDepartment();
        generateAverageIncomeByAgeRange();
        generateMedianEmployeeAgeByDepartment();
    }

    /**
     * Generates report for median income by department
     */
    private void generateMedianIncomeByDepartment() {
        File medianIncomeByDepartmentFile = new File(directoryPath, INCOME_BY_DEPARTMENT);
        try (FileOutputStream fos = new FileOutputStream(medianIncomeByDepartmentFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            writeLine(bw, "DEPARTMENT", "MEDIAN INCOME");

            for (Department department : loader.getDepartments()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesIncomeForDepartment(department));
                writeLine(bw, department.getName(), String.format("%.2f", stats.getMedian()));
            }
        } catch (IOException e) {
            logExceptionOnFileWrite(e, INCOME_BY_DEPARTMENT);
        }
    }

    /**
     * Generates the report for 95-percentile income by department
     */
    private void generate95PercentileIncomeByDepartment() {
        File income95ByDepartmentFile = new File(directoryPath, INCOME_95_BY_DEPARTMENT);
        try (FileOutputStream fos = new FileOutputStream(income95ByDepartmentFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            writeLine(bw, "DEPARTMENT", "95-PERCENTILE INCOME");

            for (Department department : loader.getDepartments()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesIncomeForDepartment(department));
                writeLine(bw, department.getName(), String.format("%.2f", stats.get95thPercentile()));
            }
        } catch (IOException e) {
            logExceptionOnFileWrite(e, INCOME_95_BY_DEPARTMENT);
        }
    }

    /**
     * Generates the report for average income by age ranges with factor of ten
     */
    private void generateAverageIncomeByAgeRange() {
        File averageIncomeByAgeRangeFile = new File(directoryPath, INCOME_AVERAGE_BY_AGE_RANGE);
        try (FileOutputStream fos = new FileOutputStream(averageIncomeByAgeRangeFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            writeLine(bw, "AGE RANGE", "AVERAGE INCOME");

            for (AgeRange ageRange : AgeRange.values()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesIncomeForAgeRange(ageRange));
                writeLine(bw, ageRange.toString(), String.format("%.2f", stats.getAvg()));
            }
        } catch (IOException e) {
            logExceptionOnFileWrite(e, INCOME_AVERAGE_BY_AGE_RANGE);
        }
    }

    /**
     * Generates the report for median employee age by department
     */
    private void generateMedianEmployeeAgeByDepartment() {
        File medianEmployeeAgeByDepartmentFile = new File(directoryPath, EMPLOYEE_AGE_BY_DEPARTMENT);
        try (FileOutputStream fos = new FileOutputStream(medianEmployeeAgeByDepartmentFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            writeLine(bw, "DEPARTMENT", "MEDIAN EMPLOYEE AGE");

            for (Department department : loader.getDepartments()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesAgeForDepartment(department));
                writeLine(bw, department.getName(), String.format("%.2f", stats.getMedian()));
            }
        } catch (IOException e) {
            logExceptionOnFileWrite(e, EMPLOYEE_AGE_BY_DEPARTMENT);
        }
    }

    private void logExceptionOnFileWrite(Exception ex, String fileName) {
        log.severe("Can't write to output file " + fileName + ". Exception " + ex.getMessage());
    }

    private void writeLine(BufferedWriter bw, String... values) throws IOException {
        String value = Arrays.stream(values).collect(Collectors.joining(DELIMITER));
        bw.write(value);
        bw.newLine();
    }

}
