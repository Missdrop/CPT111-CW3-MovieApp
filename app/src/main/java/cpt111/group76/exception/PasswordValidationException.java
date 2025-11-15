package cpt111.group76.exception;

/**
 * Custom exception for password validation errors.
 * Thrown when a password does not meet the required criteria.
 */
public class PasswordValidationException extends Exception {
    public PasswordValidationException(String message) {
        super(message);
    }
}
