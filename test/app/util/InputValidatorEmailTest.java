package app.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputValidatorEmailTest {
    @ParameterizedTest
    @MethodSource("provideEmailsForValidation")
    void testEmailValidation(String input, boolean expected) {
        ValidationResult result = InputValidator.isValidEmail(input);
        assertEquals(expected, result.isValid(),
                () -> "Failed for input: " + input + " with message: " + result.getMessage());
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> provideEmailsForValidation() {
        String strEmailLocalPart = "A".repeat(64);
        String strEmailDomainPart = "B".repeat(185) + ".com";
        String strEmailMaxValid = strEmailLocalPart + "@" + strEmailDomainPart; // total = 64 + 1 + 189 + 4 = 254
        String domainTooLong = "B".repeat(190) + ".com";
        String emailTooLong = strEmailLocalPart + "@" + domainTooLong;
        return Stream.of(
                // ✅ Valid emails
                org.junit.jupiter.params.provider.Arguments.of("user@example.com", true),
                org.junit.jupiter.params.provider.Arguments.of("user.name+tag@domain.co", true),
                org.junit.jupiter.params.provider.Arguments.of("user_name@sub.domain.org", true),

                // ❌ Invalid emails
                org.junit.jupiter.params.provider.Arguments.of("", false),                        // empty
                org.junit.jupiter.params.provider.Arguments.of(null, false),                      // null
                org.junit.jupiter.params.provider.Arguments.of("a@b.c", false),                   // too short
                org.junit.jupiter.params.provider.Arguments.of("abc", false),
                org.junit.jupiter.params.provider.Arguments.of("missingatsign.com", false),       // missing '@'
                org.junit.jupiter.params.provider.Arguments.of("@nouser.com", false),            // missing local part
                org.junit.jupiter.params.provider.Arguments.of("user@@domain.com", false),       // double @@ is not allowed
                org.junit.jupiter.params.provider.Arguments.of("user@.com", false),              // domain starts with dot
                org.junit.jupiter.params.provider.Arguments.of("user@domain", false),            // missing TLD
                org.junit.jupiter.params.provider.Arguments.of("user..name@example.com", false), // consecutive dots
                org.junit.jupiter.params.provider.Arguments.of("user@domain..com", false),       // consecutive dots in domain
                org.junit.jupiter.params.provider.Arguments.of("user@-domain.com", false),       // domain starts with hyphen
                org.junit.jupiter.params.provider.Arguments.of("user@domain-.com", false),       // domain ends with hyphen
                org.junit.jupiter.params.provider.Arguments.of("user@domain.com.", false),        // domain ends with dot
                org.junit.jupiter.params.provider.Arguments.of("user.name++tag@domain.co", false),

                // Length constraints
                // Valid length
                org.junit.jupiter.params.provider.Arguments.of(strEmailMaxValid, true),
                // Invalid length
                org.junit.jupiter.params.provider.Arguments.of("A", false),         // too short// too long
                org.junit.jupiter.params.provider.Arguments.of(emailTooLong, false)  // too long
        );
    }
    // ----------------------------
    // Tests for isValidEmail
    // ----------------------------
    /*@ParameterizedTest
    @CsvSource({
            "'user@example.com', true",
            "'user.name+tag@domain.co', true",
            "'abc', false",
            "'user@@domain.com', false",
            "'user@.com', false",
            "'', false",
            "'a@b.c', false",
    })
    void testIsValidEmail(String input, boolean expected) {
        ValidationResult result = InputValidator.isValidEmail(input);
        assertEquals(expected, result.isValid(),
                () -> "Failed for input: " + input + " with message: " + result.getMessage());
    } */
}
