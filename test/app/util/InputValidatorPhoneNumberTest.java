package app.util;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import java.util.stream.Stream;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class InputValidatorPhoneNumberTest {
    @ParameterizedTest
    @MethodSource("providePhoneNumbers")
    void testIsValidPhoneNumber(String input, boolean expectedValid) {
        ValidationResult result = InputValidator.isValidPhoneNumber(input);
        assertEquals(expectedValid, result.isValid(),
                () -> "Failed for input: [" + input + "] → " + result.getMessage());
    }

    private static Stream<Arguments> providePhoneNumbers() {
        return Stream.of(
                // ✅ Valid numbers
                Arguments.of("+14155552671", true),       // US
                Arguments.of("+251911223344", true),      // Ethiopia
                Arguments.of("+919876543210", true),      // India
                Arguments.of("+4915123456789", true),     // Germany
                Arguments.of("   +14155552671   ", true), // trims (if method trims)
                // ❌ Invalid numbers
                Arguments.of("14155552671", false),       // missing +
                Arguments.of("+", false),                 // only +
                Arguments.of("+0123456789", false),       // leading zero after +
                Arguments.of("+123456", false),           // too short
                Arguments.of("+1234567890123456", false), // too long (16 digits)
                Arguments.of("+251-911223344", false),    // contains dash

                // Edge cases
                Arguments.of(null, false),
                Arguments.of("", false),
                Arguments.of("    ", false)
        );
    }
}
