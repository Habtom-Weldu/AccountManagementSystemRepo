# Simple Account Management System

This is a console-based Java application that allows users to create, view, delete, 
and save bank account records using serialization and HashMap collections.
The system is modular and follows clean software architecture principles.

---

## Project Structure
src/
├── Main.java # Main program (menu & user interaction)
├── FileManager.java # Handles file path management
├── models/
│ └── Account.java # Account data model with fields & accessors
├── services/
│ └── AccountService.java # Core logic for managing accounts
---
## ✅ Features Implemented
- Create new bank accounts with required details
- View a single account by account number
- View all stored accounts
- Delete accounts by account number
- Save/load accounts using file serialization (`ObjectOutputStream`)
- Clean separation between model, service, and UI layers
---

## 🚧 Upcoming Features
- 🔒 Input validation for all fields
- ➕ Deposit / Withdraw support
- 📤 Export to CSV (optional)
- 🔐 Account login/authentication system
- 🧪 Unit testing with JUnit

---

## 🛠️ Technologies Used
- Java 21
- Java Collections (HashMap)
- Object Serialization
- IntelliJ IDEA
- Git + GitHub

---

## How to Run

1. **Clone the repository:**
   ```bash
   git clone https://github.com/<your-username>/account-management-system.git
Compile and run the project:
If using command line:

bash
javac -d out src/**/*.java
java -cp out Main
Or open the project in IntelliJ and run Main.java.

📝 License
MIT License © 2025 Habtom Hailay

---