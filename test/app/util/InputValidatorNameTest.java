package app.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputValidatorNameTest {
    // ----------------------------
    // Tests for isValidName
    // ----------------------------
    @ParameterizedTest
    @MethodSource("provideNamesForValidation")
    void testIsValidName(String input, boolean expected) {
        assertEquals(expected, InputValidator.isValidName(input).isValid(),
                "Failed for input: " + input);
    }
    private static Stream<Arguments> provideNamesForValidation() {
        return Stream.of(
                // ✅ Valid names
                org.junit.jupiter.params.provider.Arguments.of("John Doe", true),
                org.junit.jupiter.params.provider.Arguments.of("O'Connor", true),
                org.junit.jupiter.params.provider.Arguments.of("Anne-Marie", true),
                // ❌ Invalid names
                org.junit.jupiter.params.provider.Arguments.of("123John", false),   // starts with number
                org.junit.jupiter.params.provider.Arguments.of("", false),          // empty
                org.junit.jupiter.params.provider.Arguments.of("  John", false),    // leading space
                org.junit.jupiter.params.provider.Arguments.of("John--Doe", false), // double dash
                org.junit.jupiter.params.provider.Arguments.of(" ", false),         // space only
                org.junit.jupiter.params.provider.Arguments.of("'John", false),     // starts with apostrophe
                org.junit.jupiter.params.provider.Arguments.of("@Mary", false),     // special char at start
                org.junit.jupiter.params.provider.Arguments.of("Mary!", false),     // special char at end
                org.junit.jupiter.params.provider.Arguments.of("O''Connor", false), // double apostrophe
                org.junit.jupiter.params.provider.Arguments.of("John    Tesfay", false), // excessive spaces
                // Length constraints
                org.junit.jupiter.params.provider.Arguments.of("A", false),         // too short
                org.junit.jupiter.params.provider.Arguments.of("A".repeat(71), false) // too long
        );
    }
     /* @ParameterizedTest
    @CsvSource({
            "'John Doe', true",
            "'O'Connor', true",
            "'Anne-Marie', true",
            "'123John', false",           // numbers with name
            "'', false",                  // empty
            "'John--Doe', false",         // double dash
            "' ', false",                 // space only
            "''John', false",             // name can't start with apostrophe
            "'@Mary' , false",            // special char at the start
            "'Mary!', false",             // special char at the end
            "'O''Connor', false",         // double apostrophe
            "'John    Tesfay', false",    // excessive internal spaces
            "'A', false",                 //too short (if you require ≥ 2 chars).
    })
    void testIsValidName(String input, boolean expected) {
        ValidationResult result = InputValidator.isValidName(input);
        assertEquals(expected, result.isValid(),
                () -> "Failed for input: " + input + " with message: " + result.getMessage());
    } */
    // ----------------------------
    // Tests for readValidName method
    // ----------------------------
    //@DisplayName("Valid name entered on first try")
    /*@ParameterizedTest
    @CsvSource({
            "'John', John",                   // valid on first try
            "'J8hn\nAlice', Alice",           // invalid then valid
            "'back', exception"               // user chooses back
    })
    void testReadValidName(String simulatedInput, String expected) {
        Scanner testScanner = new Scanner(simulatedInput);
        InputValidator.setScanner(testScanner);

        if ("exception".equals(expected)) {
            assertThrows(GoBackToMainMenuException.class,
                    () -> InputValidator.readValidName("Enter name: "));
        } else {
            String result = assertDoesNotThrow(
                    () -> InputValidator.readValidName("Enter name: "));
            assertEquals(expected, result);
        }
    }
    // ----------------------------
    // Tests for readValidNameOrDefault
    // ----------------------------
    @ParameterizedTest
    @CsvSource({
            "'\n', CurrentName",             // press Enter → keep current name
            "'Alice', Alice",                // valid name
            "'J9hn\nBob', Bob"               // invalid first, then valid
    })
    void testReadValidNameOrDefault(String simulatedInput, String expected) {
        Scanner testScanner = new Scanner(simulatedInput);
        InputValidator.setScanner(testScanner);

        String result = assertDoesNotThrow(
                () -> InputValidator.readValidNameOrDefault("Enter name: ", "CurrentName"));
        assertEquals(expected, result);
    } */
}
