package com.jojos.report.jobs;

import com.jojos.report.data.AgeRange;
import com.jojos.report.data.Department;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
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
    private final Logger log = LoggerFactory.getLogger(getClass());

    private final Loader loader;
    private final String directoryPath;

    public OutputJob(Loader loader, String directoryPath) {
        this.loader = loader;
        this.directoryPath = directoryPath;
    }

    public void generateReports() {
        generateMedianIncomeByDepartment();
        generate95PercentileIncomeByDepartment();
        generateAverageIncomeByAgeRange();
        generateMedianEmployeeAgeByDepartment();
    }

    private void generateMedianIncomeByDepartment() {
        File medianIncomeByDepartmentFile = new File(directoryPath, INCOME_BY_DEPARTMENT);
        try (FileOutputStream fos = new FileOutputStream(medianIncomeByDepartmentFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {

            writeHeaders(bw, "DEPARTMENT", "MEDIAN INCOME");

            for (Department department : loader.getDepartments()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesIncomeForDepartment(department));
                writeLine(bw, department.getName(), String.format("%.2f", stats.getMedian()));
            }

        } catch (IOException e) {
            log.error("Can't write to output file {}", INCOME_BY_DEPARTMENT);
        }
    }

    private void generate95PercentileIncomeByDepartment() {
        File income95ByDepartmentFile = new File(directoryPath, INCOME_95_BY_DEPARTMENT);
        try (FileOutputStream fos = new FileOutputStream(income95ByDepartmentFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            writeHeaders(bw, "DEPARTMENT", "95-PERCENTILE INCOME");

            for (Department department : loader.getDepartments()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesIncomeForDepartment(department));
                writeLine(bw, department.getName(), String.format("%.2f", stats.get95thPercentile()));
            }

        } catch (IOException e) {
            log.error("Can't write to output file {}", INCOME_95_BY_DEPARTMENT);
        }
    }

    private void generateAverageIncomeByAgeRange() {
        File averageIncomeByAgeRangeFile = new File(directoryPath, INCOME_AVERAGE_BY_AGE_RANGE);
        try (FileOutputStream fos = new FileOutputStream(averageIncomeByAgeRangeFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            writeHeaders(bw, "AGE RANGE", "AVERAGE INCOME");

            for (AgeRange ageRange : AgeRange.values()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesIncomeForAgeRange(ageRange));
                writeLine(bw, ageRange.toString(), String.format("%.2f", stats.getAvg()));
            }

        } catch (IOException e) {
            log.error("Can't write to output file {}", INCOME_AVERAGE_BY_AGE_RANGE);
        }
    }

    private void generateMedianEmployeeAgeByDepartment() {
        File medianEmployeeAgeByDepartmentFile = new File(directoryPath, EMPLOYEE_AGE_BY_DEPARTMENT);
        try (FileOutputStream fos = new FileOutputStream(medianEmployeeAgeByDepartmentFile);
             BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos))) {
            writeHeaders(bw, "DEPARTMENT", "MEDIAN EMPLOYEE AGE");

            for (Department department : loader.getDepartments()) {
                Statistics stats = Statistics.calculate(loader.getEmployeesAgeForDepartment(department));
                writeLine(bw, department.getName(), String.format("%.2f", stats.getMedian()));
            }

        } catch (IOException e) {
            log.error("Can't write to output file {}", EMPLOYEE_AGE_BY_DEPARTMENT);
        }
    }

    private void writeLine(BufferedWriter bw, String... values) throws IOException {
        String value = Arrays.stream(values).collect(Collectors.joining(DELIMITER));
        bw.write(value);
        bw.newLine();
    }

    private void writeHeaders(BufferedWriter bw, String... headers) throws IOException {
        String header = Arrays.stream(headers).collect(Collectors.joining(DELIMITER));
        bw.write(header);
        bw.newLine();
    }
}
