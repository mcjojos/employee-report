# Employee report
Report generation for employees in a specific format

## Synopsis
There are three commma separated files in data directory:
```
departments.csv - list of departments
employees.csv - first column contains position of department in alphabetically sorted department list, followed by employee name and salary
ages.csv - first column contains employee name, followed by age
```
## What does the application produce?
Provided a collection of input files of the following format, the application
will generate the following reports in corresponding files:
```
income-by-department.csv - median income by department
income-95-by-department.csv - 95-percentile income by department
income-average-by-age-range.csv - average income by age ranges with factor of ten
employee-age-by-department.csv - median employee age by department
```
Reports must be generated in a comma separated format with header columns.


## Requirements

You'll need Java 8 to compile and run the application. You'll also need maven to build it.

## How do I run it?

You can build the JAR file with

```
mvn clean package
```
and after producing the jar file you can run it by typing
```
java -jar target/employee-report-1.0-SNAPSHOT-jar-with-dependencies.jar -input path/to/input/directory
```
The command line argument specifies the folder from which the input files described before shall be parsed.
The output files should also be placed on that folder.

Have Fun!
