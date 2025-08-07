package app.util;
import org.junit.jupiter.api.Test;
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
}
