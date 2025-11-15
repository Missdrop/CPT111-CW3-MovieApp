package cpt111.group76.exception;

/**
 * Custom exception for username validation errors.
 * Thrown when a username does not meet the required criteria.
 */
public class UsernameValidationException extends Exception {
    public UsernameValidationException(String message) {
        super(message);
    }
}
