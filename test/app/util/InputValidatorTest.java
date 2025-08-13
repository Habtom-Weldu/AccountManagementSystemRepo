package app.util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
public class InputValidatorTest {
    @Test
    public void testReadIntInRange_validInput() {
        String simulatedInput = "abc\n4\n"; // First input invalid, second valid
        Scanner testScanner = new Scanner(simulatedInput);
        InputValidator.setScanner(testScanner);

        int result = InputValidator.readIntInRange(1, 5, "Enter a number: ");
        assertEquals(4, result);
    }
    @AfterEach
    void restoreScanner() {
        InputValidator.resetScanner();
    }

    @Test
    public void testIsValidName(){
        // ✅ Valid names
        assertTrue(InputValidator.isValidName("John"), "Single valid name should pass");
        assertTrue(InputValidator.isValidName("John Doe"), "Full name with space should pass");

        // ❌ Invalid names
        assertFalse(InputValidator.isValidName("John123"), "Name with numbers should fail");
        assertFalse(InputValidator.isValidName(""), "Empty name should fail");
        assertFalse(InputValidator.isValidName(null), "Null name should fail");
        assertFalse(InputValidator.isValidName("John@Doe"), "Name with special characters should fail");
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "John",
            "Mary Jane",
            "O'Connor",
            "Anne-Marie",
            "D'Angelo"
    })
    void validNamesShouldReturnTrue(String strName) {
        assertTrue(InputValidator.isValidName(strName),
                () -> "Expected valid name, but got invalid: " + strName); // this message will display if the
        // assertion failed only.
    }
    @ParameterizedTest
    @ValueSource(strings = {
            "",                 // empty
            " ",                // space only
            "John123",          // numbers aren't allowed
            "'John",            // name can't start with apostrophe
            "@Mary",            // special char at the start
            "Mary!",            // special char at an end
            "O''Connor",        // double apostrophe
            "Anne--Marie",      // double dash
            "   John   "        // excessive spaces
    })
    void invalidNamesShouldReturnFalse(String strName) {
        assertFalse(InputValidator.isValidName(strName),
                () -> "Expected invalid name, but got valid: " + strName); // this message will display if the
                 // assertion failed only.
    }

    // Below is a method that can fail tests
    /*@ParameterizedTest
    @ValueSource(strings = { "John" })
    void failingTestExample(String name) {
        assertFalse(InputValidator.isValidName(name),
                () -> "Expected invalid name, but got valid: " + name);
    } */

}
