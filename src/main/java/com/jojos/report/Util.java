package com.jojos.report;

import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentNavigableMap;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.TimeUnit;

/**
 * Utility classes
 *
 * @author karanikasg@gmail.com
 */
public class Util {

    // input files
    public static final String AGES_FILE = "ages.csv";
    public static final String DEPARTMENTS_FILE = "departments.csv";
    public static final String EMPLOYEES_FILE = "employees.csv";

    // output files
    public static final String INCOME_BY_DEPARTMENT = "income-by-department.csv";
    public static final String INCOME_95_BY_DEPARTMENT = "income-95-by-department.csv";
    public static final String INCOME_AVERAGE_BY_AGE_RANGE = "income-average-by-age-range.csv";
    public static final String EMPLOYEE_AGE_BY_DEPARTMENT = "employee-age-by-department.csv";

    public static final String DELIMITER = ",";


    public static String longDuration(long start) {
        long millis = System.currentTimeMillis() - start;
        long minutes = TimeUnit.MILLISECONDS.toMinutes(millis);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(minutes);
        long secondsToMillis = TimeUnit.SECONDS.toMillis(seconds);
        long minutesToMillis = TimeUnit.MINUTES.toMillis(minutes);
        long mills = millis - secondsToMillis - minutesToMillis;
        return String.format("%02dmin%02dsec%02dms", minutes, seconds, mills);
    }

    /**
     * This class will get the command line arguments of the form
     * -name1 value1 -name2 value2 -name3 value3
     *
     * @param commandLineArguments an array containing all the command line arguments
     * @param argName the name of the arg you want to extract
     * @return the value that corresponds to the argument requested
     */
    public static String getArgument(String[] commandLineArguments, String argName) {

        if (commandLineArguments != null) {
            for (int i = 0; i < commandLineArguments.length - 1; i++) {
                if (commandLineArguments[i] == null ||
                        commandLineArguments[i].length() == 0 ||
                        commandLineArguments[i].charAt(0) != '-' ||
                        commandLineArguments[i + 1] == null) {
                    continue;
                }
                String tmpArg = commandLineArguments[i].substring(1);
                if (tmpArg.equalsIgnoreCase(argName)) {
                    String input = commandLineArguments[i + 1];
                    if (input != null && !input.equals("")) {
                        return input;
                    }
                }
            }
        }
        return null;
    }

    /**
     * For a map containing a set for the specified {@code mapKey}, adds the provided value to the set.
     * If there is no set for the {@code mapKey}, one will be created first. As we are using a {@link ConcurrentNavigableMap},
     * we will assume that we want a concurrent set as well, so one will be created by way of {@link ConcurrentSkipListMap}
     * and {@link Collections#newSetFromMap(Map)}
     *
     * @param map    the map of sets
     * @param mapKey the key that indicates a set in the map
     * @param value  the value to be added to the set
     * @return {@code true} if the value could be added to the set or {@code false} otherwise.
     * @see Set#add(Object)
     */
    public static <K, V> boolean addToContainedSet(ConcurrentNavigableMap<K, Set<V>> map, K mapKey, V value) {
        Set<V> existingSet = map.get(mapKey);
        if (existingSet == null) {
            // It might look like checking for null and then creating something means that we need a lock.
            // this isn't the case, as the ACTUAL point of synchronization is the map.putIfAbsent() below.
            // it's perfectly possible to have multiple threads enter this block at the same time.
            // this is fine, as the only "true" value added is added by the putIfAbsent() call.
            // this race will only be an issue in the beginning. Once putIfAbsent() has succeeded,
            // the outer if-statement will always be false, which means we can avoid creating the
            // inner container and calling putIfAbsent() again.
            // This replaces this more legible but slower pattern:
            // map.putIfAbsent(mapKey, Collections.newSetFromMap(new ConcurrentSkipListMap<V, Boolean>())); // ensure that we have something
            // map.get(mapKey).add(value);
            // See slides 54 and 55 of this presentation regarding the speed of this: http://www.slideshare.net/marakana/effective-java-still-effective-after-all-these-years
            Set<V> newSet = Collections.newSetFromMap(new ConcurrentSkipListMap<V, Boolean>());
            existingSet = map.putIfAbsent(mapKey, newSet);
            if (existingSet == null) {
                // we've added a new set
                existingSet = newSet;
            }
        }
        return existingSet.add(value);
    }

}
