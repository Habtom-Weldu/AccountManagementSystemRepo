package app.util;

import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    public static int readIntInRange(int min, int max, String prompt) {
        int input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(scanner.nextLine().trim());
                //scanner.nextLine(); // clear buffer
                if (input >= min && input <= max) {
                    return input;
                } else {
                    System.out.println("❗ Please enter a number between " + min + " and " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("❗ Invalid input. Please enter a valid integer.");
            }
        }
    }
    // ############ Validate Account number

    // ############ Validate name
    public static String readValidName(String prompt) {
        String input;
        // Unicode-aware regex: first char is a letter, rest are letters, space, hyphen, apostrophe
        Pattern namePattern = Pattern.compile("^\\p{L}[\\p{L}\\-' ]*$", Pattern.UNICODE_CHARACTER_CLASS);
        // Disallowed consecutive symbols
        String[] invalidSequences = { "--", "''", "  ", "-'", "'-", "' ", " -", " -", "''", "--" };

        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            // Length check
            if (input.length() < 2 || input.length() > 70) {
                System.out.println("❗ Name must be between 2 and 70 characters.");
                continue;
            }
            // Regex check
            Matcher matcher = namePattern.matcher(input);
            if (!matcher.matches()) {
                System.out.println("❗ Name must start with a letter and contain only letters, spaces, hyphens (-), or apostrophes (').");
                continue;
            }
            // Check for consecutive invalid patterns
            boolean hasInvalid = false;
            for (String invalid : invalidSequences) {
                if (input.contains(invalid)) {
                    hasInvalid = true;
                    break;
                }
            }
            if (hasInvalid) {
                System.out.println("❗ Name contains invalid consecutive symbols like '--', `''`, or double space.");
                continue;
            }
            return input;
        }
    }
    // ############
    public static double readPositiveDouble(String prompt) {
        double input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Double.parseDouble(scanner.nextLine().trim());
                if (input >= 0) {
                    return input;
                } else {
                    System.out.println("❗ Amount must be a positive number.");
                }
            } catch (NumberFormatException e) {
                System.out.println("❗ Invalid number. Please enter a valid amount.");
            }
        }
    }

    public static String readNonEmptyString(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("❗ This field cannot be empty.");
            }
        }
    }

    public static boolean readYesOrNo(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt + " (y/n): ");
            input = scanner.nextLine().trim().toLowerCase();
            if (input.equals("y") || input.equals("yes")) {
                return true;
            } else if (input.equals("n") || input.equals("no")) {
                return false;
            } else {
                System.out.println("❗ Please enter 'y' or 'n'.");
            }
        }
    }
}
