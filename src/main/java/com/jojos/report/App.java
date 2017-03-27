package com.jojos.report;

import com.jojos.report.jobs.InputJob;
import com.jojos.report.jobs.Loader;
import com.jojos.report.jobs.OutputJob;

import java.util.logging.Logger;

/**
 * Entry point of the application
 *
 * @author karanikasg@gmail.com
 */
public class App {
    private static final Logger log = Logger.getLogger(App.class.getName());
    private final String directoryPath;

    public App(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void start() {
        log.info(String.format("Application started with input file directory: \"%s\"", directoryPath));
        long start = System.currentTimeMillis();


        InputJob inputJob = new InputJob(directoryPath);
        Loader loader = inputJob.parseAndLoad();
        OutputJob outputJob = new OutputJob(loader, directoryPath);
        outputJob.generateReports();

        String time = Util.longDuration(start);
        String processingCompleted = String.format("Application completed in %s", time);
        log.info(processingCompleted);

    }

    public static void main(String[] args) {
        String inputPath = Util.getArgument(args, "input");

        App app = new App(inputPath);
        app.start();

    }
}
