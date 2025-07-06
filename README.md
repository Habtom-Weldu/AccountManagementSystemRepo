# Simple Account Management System

This is a simple **Java-based Account Management System** that interacts with a **SQLite database** to 
manage user accounts through a console-based interface. The application allows users to create, view, update,
and delete accounts, as well as view all existing accounts. Additional features like deposit and withdrawal 
are planned.

---

## Project Structure
src/
├── app/
│   ├── Main.java # app.Main program (menu & user interaction)
│   └── FileManager.java # Handles file path management
├── models/
│   └── Account.java # Account data model with fields & accessors
├── services/
│   └── AccountService.java # Core logic for managing accounts

---
[Start]
|
Login/Register
|
[Display Main Menu]
|
|--> [1. Create Account] --> [Input User Info] --> [Save to DB/File] --> [Back to Menu]
|
|--> [2. View Account] --> [Enter Account ID] --> [Display Details] --> [Back to Menu]
|
|--> [3. Update Account] --> [Enter Account ID] --> [Edit Info] --> [Save Changes] --> [Back to Menu]
|
|--> [4. Delete Account] --> [Enter Account ID] --> [Confirm Deletion?]
                            | Yes --> [Delete] --> [Back to Menu]
                            | No  --> [Back to Menu]
|
|--> [5. View All Accounts] --> [Display All Accounts] --> [Back to Menu]
|
|--> [6. Deposit] --> [Enter Account ID] --> [Enter Amount] --> [Update Balance] --> [Back to Menu]
|
|--> [7. Withdraw] --> [Enter Account ID] --> [Enter Amount] --> [Check Balance > Amount?]
                                              | Yes --> [Update Balance] --> [Back to Menu]
                                              | No  --> [Show Error] --> [Back to Menu]

---
## ✅ Features Implemented
- Create new bank accounts with required details
- View a single account by account number
- View all stored accounts
- Delete accounts by account number
- Save/load accounts to a database.
- Clean separation between a model, service, and UI layers
---
## 🔄 Planned Features
-  Input validation for all fields
-  Update Account
-  Deposit / Withdraw support
-  Export to CSV (optional)
-  Account login/authentication system
-  Unit testing with JUnit
---

## 🛠️ Technologies Used
- Java 17
- Java Collections (HashMap)
- Object Serialization
- IntelliJ IDEA
- Git + GitHub
- SQLite database

# Tech Stack
| Component      | Description              |
|----------------|--------------------------|
| Language       | Java                     |
| Database       | SQLite                   |
| Tools/IDE      | IntelliJ IDEA / VS Code  |
| Version Control| Git & GitHub             |

---
## Branching Strategy

- `main` – stable, production-ready code
- `dev` – active development
- `feature/*` – separate branches for individual features

---
## How to Run

1. Clone the repo
2. Make sure you have Java and SQLite installed
3. Run `Main.java` in your IDE or terminal
Database file (`accounts.db`) will be created automatically on first run.

Compile and run the project:
If using command line:
bash
javac -d out src/**/*.java
java -cp out app.Main
Or open the project in IntelliJ and run app.Main.java.
---
## 🧑‍💻 Author

Habtom Hailay  
United States  
Simple and clean Java development with a learning spirit 🚀

---
📝 License
MIT License © 2025 Habtom Hailay

---