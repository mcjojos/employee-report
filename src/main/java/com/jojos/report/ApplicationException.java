package com.jojos.report;

/**
 * Generic application-specific exception containing the message with the reason for which the exception was thrown.
 *
 * @author karanikasg@gmail.com
 */
public class ApplicationException extends RuntimeException {

    public ApplicationException(String message) {
        super(message);
    }
}
