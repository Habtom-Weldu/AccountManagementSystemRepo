package app.util;

import java.util.HashMap;
import java.util.Map;
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
    public static String readValidAccNumber(String prompt){
        String input;
        while (true) {
            System.out.print(prompt);
            input = scanner.nextLine().trim();
            // Length check
            if (input.length() < 8 || input.length() > 12) {
                System.out.println("❗ Account number must be between 8 and 12 digits.");
                continue;
            }
            // Digits only check
            if (!input.matches("\\d+")) {
                System.out.println("❗ Account number must contain digits only.");
                continue;
            }
            return input; // ✅ Valid input
        }
    }
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
            Matcher inputMatcher = namePattern.matcher(input);
            if (!inputMatcher.matches()) {
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

    // ############ Read Valid Account type
    private static final HashMap<String, Double> hmAccTypeMinBalances = new HashMap<>();
    static {
        hmAccTypeMinBalances.put("Savings", 100.00);
        hmAccTypeMinBalances.put("Checking", 50.00);
        hmAccTypeMinBalances.put("Business", 1000.00);
    }
    public static String readValidAccountType(String prompt) {
        String type;
        while (true) {
            System.out.print(prompt);
            type = scanner.nextLine().trim();
            // Normalize: Capitalize a first letter only
            type = type.substring(0, 1).toUpperCase() + type.substring(1).toLowerCase();
            if (hmAccTypeMinBalances.containsKey(type)) {
                return type;
            } else {
                System.out.println("⚠️ Invalid account type. Please enter Savings, Checking, or Business.");
            }
        }
    }
    // ############ Read Valid Double balance value
    public static double readBalanceForAccountType(String prompt,String accountType) {
        double min = hmAccTypeMinBalances.getOrDefault(accountType, 0.0);
        double max = 10_000_000.00;
        String fullPrompt = String.format("%s (%.2f - %,.2f): ", prompt, min, max);
        return readDoubleInRange(fullPrompt, min,max);
    }
    public static double readDoubleInRange(String prompt, double min, double max) {
        double value;
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) {
                System.out.println("⚠️ Input cannot be empty. Please enter a number.");
                continue;
            }
            try {
                value = Double.parseDouble(input);
                if (value < min || value > max) {
                    System.out.printf("⚠️ Please enter a number between %.2f and %.2f.%n", min, max);
                } else {
                    return value;
                }
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Invalid number. Please enter a valid numeric value.");
            }
        }
    }

    // ############ Read Valid Positive Double value
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
