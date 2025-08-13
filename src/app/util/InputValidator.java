package app.util;

import app.exceptions.GoBackToMainMenuException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InputValidator {
    private static Scanner sc = new Scanner(System.in); // Default for real usage
    // Allow injecting custom Scanner for testing
    public static void setScanner(Scanner customScanner) { // will be used from the test class
        sc = customScanner;
    }
    public static void resetScanner() {
        sc = new Scanner(System.in);
    }

    public static int readIntInRange(int min, int max, String prompt) {
        int input;
        // <orderEntry type="library" name="junit.jupiter" level="project" />
        while (true) {
            System.out.print(prompt);
            try {
                input = Integer.parseInt(sc.nextLine().trim());
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
    public static String readValidAccNumber(String prompt) throws GoBackToMainMenuException {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException();
            }
            // Length and digits only check
            if (!input.matches("\\d{10}")) { // must be digits only and 10 digits long
                System.out.println("⚠️ Invalid format. Account number must be 10 digits.");
                continue;
            }
            return input; // ✅ Valid input
        }
    }
    // ############ Validate name
    public static String readValidName(String prompt) throws GoBackToMainMenuException {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException();
            }
            if (isValidName(input)) {
                return input;
            }
        }
    }
    public static String readValidNameOrDefault(String prompt, String currentName) throws GoBackToMainMenuException {
        while (true) {
            System.out.print(prompt + " (" + currentName + "): ");
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException();
            }
            if (input.isEmpty()) {
                return currentName; // Keep current if no new input
            }
            if (isValidName(input)) {
                return input;
            }
        }
    }
    public static boolean isValidName(String name) {
        if(name == null){
            System.out.println("❗ Name can't be null.");
            return false;
        }
        // Unicode-aware regex: first char is a letter, rest are letters, space, hyphen, apostrophe
        Pattern namePattern = Pattern.compile("^\\p{L}[\\p{L}\\-' ]*$", Pattern.UNICODE_CHARACTER_CLASS);
        // Disallowed consecutive symbols
        String[] invalidSequences = { "--", "''", "  ", "-'", "'-", "' ", " -", " -", "''", "--" };
        // Length check
        if (name.length() < 2 || name.length() > 70) {
            System.out.println("❗ Name must be between 2 and 70 characters.");
            return false;
        }
        // Check for consecutive invalid patterns
        boolean hasInvalid = false;
        for (String invalid : invalidSequences) {
            if (name.contains(invalid)) {
                hasInvalid = true;
                break;
            }
        }
        if (hasInvalid) {
            System.out.println("❗ Name contains invalid consecutive symbols like '--', `''`, or double space.");
            return false;
        }
        if (name.endsWith("-") || name.endsWith("'") || name.endsWith(" ")) {
            System.out.println("⚠️ Name cannot end with a hyphen, apostrophe, or space.");
            return false;
        }
        // Regex check
        Matcher inputMatcher = namePattern.matcher(name);
        if (!inputMatcher.matches()) {
            System.out.println("❗ Name must start with a letter and contain only letters, spaces, hyphens (-), or apostrophes (').");
            return false;
        }
        return true;
    }

    // ############ Read Valid Email
       /* validating code for strict and realistic email i.e.
          - No leading/trailing dot - No consecutive dots - Only valid characters in the local part.
          - Realistic domain and TLD structure - Case-insensitive - Friendly to real-world email.
        Length, structure, and character limits are all handled before running regex*/
    public static String readValidEmail(String prompt) throws GoBackToMainMenuException {
        // to be used for feature if there is a need for method overloading
        return readValidEmail(prompt, null); // Delegates to the overloaded method
    }
    public static String readValidEmail(String prompt, Function<String,
            Boolean> isDuplicateCheck) throws GoBackToMainMenuException {
        while (true) {
            System.out.print(prompt);
            String input;
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException();
            }
            if (!isValidEmail(input)) {
                continue;
            }
            if (isDuplicateCheck.apply(input)){//this will apply to "checkIfEmailExists" method in ExistenceChecker.java.
                System.out.println("⚠️ Email already used by other customer.");
                continue;
            }
            return input; // valid and not duplicated
        }
    }
    public static String readValidEmailOrDefault(String prompt, String currentEmail) throws GoBackToMainMenuException {
        // to be used for feature if there is a need for method overloading
        return readValidEmailOrDefault(prompt, currentEmail, null); // Delegates to the overloaded method
    }
    public static String readValidEmailOrDefault(String prompt, String currentEmail, Function<String,
            Boolean> isEmailDuplicatedCheck) throws GoBackToMainMenuException {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException();
            }
            if (input.isEmpty()){
                return currentEmail;
            }
            if (!isValidEmail(input)) {
                continue;
            }
            // Only perform duplicate check if input is different from currentEmail
            if (!input.equalsIgnoreCase(currentEmail) && isEmailDuplicatedCheck != null && isEmailDuplicatedCheck.apply(input)) {
                System.out.println("⚠️ Email already used by other customer.");
                continue;
            }
            return input; // valid and not duplicated
        }
    }
    public static boolean isValidEmail(String email) {
        // Strict but realistic regex
        //String emailRegex = "^(?!\\.)[A-Za-z0-9+_.-]{1,64}(?<!\\.)@(?!-)([A-Za-z0-9-]+\\.)+[A-Za-z]{2,}$";
        String emailRegex = "^[A-Za-z0-9][A-Za-z0-9._+-]*@[A-Za-z0-9-]+(\\.[A-Za-z]{2,})+$";
        Pattern pattern = Pattern.compile(emailRegex, Pattern.CASE_INSENSITIVE);
        if (email.length() < 5) {
            System.out.println("⚠️ Email cannot be less than 5 characters.");
            return false;
        }
        if (email.length() > 254) {
            System.out.println("⚠️ Email is too long. Must be 254 characters or fewer.");
            return false;
        }
        String[] partsArr = email.split("@",3);
        if (partsArr.length != 2) {
            System.out.println("⚠️ Email must contain a single '@' character.");
            return false;
        }
        String emailLocalPart = partsArr[0]; //String domainPart = partsArr[1]; // for domain part
        // Local part
        if (emailLocalPart.length() > 64) {
            System.out.println("⚠️ Local part (before @) must be 64 characters or fewer.");
            return false;
        }
        char firstChar = emailLocalPart.charAt(0);
        if (!Character.isLetterOrDigit(firstChar)) {
            System.out.println("⚠️ Local part must start with a letter or digit.");
            return false;
        }
        char lastLocalChar = emailLocalPart.charAt(emailLocalPart.length() - 1);
        if (lastLocalChar == '.' || lastLocalChar == '-' || lastLocalChar == '_' || lastLocalChar == '+') {
            System.out.println("⚠️ Email Local part cannot end with special characters (., -, _, +).");
            return false;
        }
        if (email.contains("..") || email.contains("--") || email.contains("@@") || email.contains("''") ||
                email.contains("++")) {
            System.out.println("⚠️ Email cannot contain consecutive special characters like .., --, @@ or ++");
            return false;
        }
        // Domain parts of the email
        String domain = email.substring(email.indexOf('@') + 1);
        String[] domainPartArr = domain.split("\\.");
        boolean invalidDomainLabel = false;
        for (String part : domainPartArr) {
            if (part.isEmpty() || part.startsWith("-") || part.endsWith("-")) {
                invalidDomainLabel = true;
                break;
            }
        }
        if (invalidDomainLabel) {
            System.out.println("⚠️ Domain labels cannot be empty or start/end with hyphens.");
            return false;
        }
        // Regex matching checking
        if (!pattern.matcher(email).matches()) {
            //return email;
            System.out.println("⚠️ Invalid email format.");
            return false;
        }
        return true;
    }
    // ############ Read Valid Phone number
    public static String readValidPhoneNumber(String prompt, Function<String, Boolean> isDuplicateCheck)
           throws GoBackToMainMenuException {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
            if (!input.startsWith("+") || !input.substring(1).matches("\\d+")) {
                System.out.println("⚠️ Invalid format. Must start with '+' and contain only digits.");
                continue;
            }
            int length = input.length() - 1;
            if (length < 10 || length > 15) {
                System.out.println("⚠️ Phone number must be between 10 and 15 digits.");
                continue;
            }
            /* the following code will call the isPhoneNumberExists() method in AccountService — indirectly — through
             a lambda function (or method reference) that passed in from Main.java "Main::checkIfPhoneExists".
             */
            if (isDuplicateCheck.apply(input)){//this will apply to "checkIfPhoneExists" method in ExistenceChecker.java.
                System.out.println("⚠️ Phone number already exists in app.database.");
                continue;
            }
            return input;
        }
    }
    public static String readValidPhoneNumberOrDefault(String prompt, String currentPhone,
        Function<String, Boolean> isDuplicateCheck) throws GoBackToMainMenuException {
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
            int length = input.length() - 1;
            if (input.isEmpty()) {
                return currentPhone;  // Keep existing
            }
            else if (length < 10 || length > 15) {
                System.out.println("⚠️ Phone number must be between 10 and 15 digits.");
                continue;
            }
            // Validate format (reuse your existing validator logic if you have)
            if (!input.startsWith("+") || !input.substring(1).matches("\\d+")) {
                System.out.println("⚠️ Invalid format. Must start with '+' and contain only digits.");
                continue;
            }
            // Only check DB existence if different from current
            if (!input.equals(currentPhone)) {
                if (isDuplicateCheck.apply(input)) {
                    System.out.println("⚠️ Phone number already exists in another account.");
                    continue;
                }
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
    public static String readValidAccountType(String prompt) throws GoBackToMainMenuException {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
            // Normalize: Capitalize a first letter only
            input = input.substring(0, 1).toUpperCase() + input.substring(1).toLowerCase();
            if (hmAccTypeMinBalances.containsKey(input)) {
                return input;
            } else {
                System.out.println("⚠️ Invalid account type. Please enter Savings, Checking, or Business.");
            }
        }
    }
    // ############ Read Valid Double balance value
    public static double readBalanceForAccType(String prompt,String accountType) throws GoBackToMainMenuException {
        double min = hmAccTypeMinBalances.getOrDefault(accountType, 0.0);
        double max = 10_000_000.00;
        String fullPrompt = String.format("%s (%.2f - %,.2f): ", prompt, min, max);
        while (true) {
            System.out.print(fullPrompt);
            String input = sc.nextLine().trim();

            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
            if (input.isEmpty()) {
                System.out.println("⚠️ Input cannot be empty. Please enter a number.");
                continue;
            }
            if(!isValidCurrencyFormat(input)){
                System.out.println("⚠️ Please enter a valid amount (numeric, and up to 2 decimal places).");
                continue;
            }
            if (!isDoubleInRange(input, min,max)){
                System.out.printf("⚠️ Please enter a value between %.2f and %.2f.%n", min, max);
                continue;
            }
            return Double.parseDouble(input);
        }
    }
    public static double readMoneyAmount(String prompt, double min, double max) throws GoBackToMainMenuException {
        double value;
        String fullPrompt = String.format(prompt + "(%.2f - %.2f) or press Enter to keep " +
                "current (%.2f): ", min, max);
        while (true) {
            System.out.print(fullPrompt);
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
            if (input.isEmpty()) {
                System.out.println("⚠️ Input cannot be empty. Please enter a number.");
                continue;
            }
            if(!isValidCurrencyFormat(input)){
                System.out.println("⚠️ Please enter a valid amount (numeric, and up to 2 decimal places).");
                continue;
            }
            if (!isDoubleInRange(input, min,max)){
                System.out.printf("⚠️ Please enter a value between %.2f and %.2f.%n", min, max);
                continue;
            }
            return Double.parseDouble(input);
        }
    }
    public static boolean isDoubleInRange(String input, double min, double max) {
        try {
            double value = Double.parseDouble(input);
            return value >= min && value <= max;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    public static boolean isValidCurrencyFormat(String input){
        if (input == null || input.trim().isEmpty()) {
            return false;
        }
        try {
            BigDecimal bd = new BigDecimal(input);
            return bd.scale() <=2;
        } catch (NumberFormatException e){
            System.out.println(e.getMessage());
            return false;
        }
    }
    public static double readBalanceOrDefaultForAccType(String accountType, double currentBalance) throws GoBackToMainMenuException {
        double min = hmAccTypeMinBalances.getOrDefault(accountType, 0.0);
        double max = 10_000_000;
        String prompt = String.format("Enter new balance (%.2f - %.2f) or press Enter to keep " +
                "current (%.2f): ", min, max, currentBalance);
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
            if (input.isEmpty()) {
                return currentBalance; // Accept current balance as default if entry is empty.
            }
            if(!isValidCurrencyFormat(input)){
                System.out.println("❌ Please enter a valid amount (numeric, and up to 2 decimal places).");
                continue;
            }
            if (!isDoubleInRange(input, min,max)){
                System.out.printf("⚠️ Please enter a value between %.2f and %.2f.%n", min, max);
                continue;
            }
            return Double.parseDouble(input);
        }
    }
    /* public static double readDoubleInRange(String prompt, double min, double max) throws GoBackToMainMenuException {
        double value;
        while (true) {
            System.out.print(prompt);
            String input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
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
    } */

    // ############ Read Valid Positive Double value
    public static double readPositiveDouble(String prompt) {
        double input;
        while (true) {
            System.out.print(prompt);
            try {
                input = Double.parseDouble(sc.nextLine().trim());
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

    public static String readNonEmptyString(String prompt) throws GoBackToMainMenuException {
        String input;
        while (true) {
            System.out.print(prompt);
            input = sc.nextLine().trim();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
            if (!input.isEmpty()) {
                return input;
            } else {
                System.out.println("❗ This field cannot be empty.");
            }
        }
    }

    public static boolean readYesOrNo(String prompt) throws GoBackToMainMenuException {
        String input;
        while (true) {
            System.out.print(prompt + " (y/n): ");
            input = sc.nextLine().trim().toLowerCase();
            if (input.equalsIgnoreCase("back")) {
                throw new GoBackToMainMenuException(); // this will be handled and used to go back to the main menu option in Main.java
            }
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
