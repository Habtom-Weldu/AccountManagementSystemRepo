package app.util;
/**
 * Represents the result of a validation operation.
 * <p>
 * Contains a boolean flag indicating whether the input is valid,
 * and an optional message describing the reason for invalidity.
 * <p>
 * This class is intended to be used by input validators (e.g., email, name, account number)
 * to return both the validation status and a human-readable message in a single object.
 * </p>
 * Example usage:
 * <pre>
 *     ValidationResult result = InputValidator.isValidEmail("test@example.com");
 *     if (!result.isValid()) {
 *         System.out.println(result.getMessage());
 *     }
 * </pre>
 */
public class ValidationResult {
    private final boolean valid;
    private final String message;
    /**
     * Creates a ValidationResult.
     * @param valid   true if the input is valid, false otherwise
     * @param message error message if invalid; can be null if valid
     */
    public ValidationResult(boolean valid, String message) {
        this.valid = valid;
        this.message = message;
    }
    /**
     * Returns whether the input is valid
     * @return true if valid, false otherwise
     */
    public boolean isValid() {
        return valid;
    }
    /**
     * Returns the validation message.
     * @return the error message if invalid, or null if valid
     */
    public String getMessage() {
        return message;
    }
    // Optional: static helpers for convenience
    public static ValidationResult ok() { /** Convenience method to return a valid result with no message. */
        return new ValidationResult(true, null);
    }
    public static ValidationResult fail(String message) { /** Convenience method to return an invalid result with a message. */
        return new ValidationResult(false, message);
    }
}
