package app.util;
import app.exceptions.GoBackToMainMenuException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
public class InputValidatorGeneralTest {
    @AfterEach
    void restoreScanner() {
        InputValidator.resetScanner();
    }

    //##### Test code for readIntInRange()
    //@DisplayName("Test for readIntInRange")
    @Test
    public void testReadIntInRange_invalidThenValidInput() {
        String simulatedInput = "abc\n4\n"; // First input invalid, second valid
        Scanner testScanner = new Scanner(simulatedInput);
        InputValidator.setScanner(testScanner);

        int result = InputValidator.readIntInRange(1, 5, "Enter a number: ");
        assertEquals(4, result);
    }

    // ###### Test code for readValidAccNumber()
    //@DisplayName("Test for Account Number")
    @ParameterizedTest
    @CsvSource({ // @CsvSource - Supplies multiple arguments per test run
            // format: simulatedInput, expectedOutput
            "'1234567890\n', 1234567890",          // valid on first try
            "'abc\n1234567890\n', 1234567890",     // invalid first, then valid
            "'123\nde89012378\n4567890123\n', 4567890123"    // short first, then invalid, finally valid
    })
    void testReadValidAccNumber(String simulatedInput, String expected) throws GoBackToMainMenuException {
        /* Simulate user entering a valid account number (first try), followed by an invalid account number.
        Note: The method under test will prompt twice — once for the first input and again after printing the
        error message.This is expected behavior. */
        Scanner testScanner = new Scanner(simulatedInput);
        InputValidator.setScanner(testScanner);

        String result = InputValidator.readValidAccNumber("Enter account number: ");
        assertEquals(expected, result);
    }

    // Below is a sample method that can fail tests
    /*@ParameterizedTest
    @ValueSource(strings = { "John" })
    void failingTestExample(String name) {
        assertFalse(InputValidator.isValidName(name),
                () -> "Expected invalid name, but got valid: " + name);
    } */

}
