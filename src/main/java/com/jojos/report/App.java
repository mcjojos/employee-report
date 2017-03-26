package com.jojos.report;

import com.jojos.report.jobs.InputJob;
import com.jojos.report.jobs.Loader;
import com.jojos.report.jobs.OutputJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Entry point of the application
 *
 * @author karanikasg@gmail.com
 */
public class App {
    private final Logger log = LoggerFactory.getLogger(getClass());


    private final String directoryPath;

    public App(String directoryPath) {
        this.directoryPath = directoryPath;
    }

    public void start() {
        log.info("Application started with input file directory: \"{}\"", directoryPath);
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
        System.out.println("Hi There! Input folder is " + inputPath);

        App app = new App(inputPath);
        app.start();

//        Stream<String> stream = null;
//
//        try {
//            stream = Files.lines(Paths.get("inputFolder"));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
