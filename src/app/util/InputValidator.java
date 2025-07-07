package app.util;

import java.util.Scanner;

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
    // ############
    public static String readValidName(String prompt) {
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            // Basic check: not empty and long enough
            if (input.isEmpty() || input.length() < 2) {
                System.out.println("❗ Name must be at least 2 characters.");
                continue; // continue while loop
            }
            // Regex Pattern(regular expression): letters, spaces, hyphens, apostrophes
            String basicPattern = "^[A-Za-z][A-Za-z\\-' ]{1,49}$"; // Disallowed patterns (consecutive symbols)
            String[] invalidSequences = {"--", "''", "  ", "-'", "'-", "' ", " -", "''", "--"};
            if (!input.matches(basicPattern)) {
                System.out.println("❗ Invalid characters. Start with letters only. Then you can use spaces, hyphens (-), or apostrophes (').");
                continue; // continue while loop
            }
            boolean hasConsecutiveInvalids = false;
            for (String invalid : invalidSequences) {
                if (input.contains(invalid)) {
                    hasConsecutiveInvalids = true;
                    break;
                }
            }
            if (hasConsecutiveInvalids) {
                System.out.println("❗ Name contains invalid consecutive symbols.");
                continue; // continue while loop
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
