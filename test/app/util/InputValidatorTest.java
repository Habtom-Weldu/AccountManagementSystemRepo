package app.util;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import java.util.Scanner;
import static org.junit.jupiter.api.Assertions.*;
public class InputValidatorTest {
    @AfterEach
    void restoreScanner() {
        InputValidator.resetScanner();
    }
    @Test
    public void testReadIntInRange_validInput() {
        String simulatedInput = "abc\n4\n"; // First input invalid, second valid
        Scanner testScanner = new Scanner(simulatedInput);
        InputValidator.setScanner(testScanner);

        int result = InputValidator.readIntInRange(1, 5, "Enter a number: ");
        assertEquals(4, result);
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
}
