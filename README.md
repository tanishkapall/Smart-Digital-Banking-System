# Smart Digital Banking & Transaction Management System

## Project Overview

The **Smart Digital Banking & Transaction Management System** is a Java-based application that simulates the core functionalities of a modern banking platform. The system enables users to create and manage bank accounts, perform secure financial transactions, monitor balances, and review transaction history in real time.

This project is designed to demonstrate strong implementation of Object-Oriented Programming (OOP) principles along with secure transaction handling, input validation, modular design, and structured workflow management similar to real-world banking systems.

---

## Objectives

* To design a modular banking system using Java
* To implement real-world banking operations digitally
* To demonstrate OOP concepts clearly through structured code
* To ensure secure and validated financial transactions
* To simulate administrative monitoring of banking operations

---

## Key Features

* Account creation (Savings / Current)
* Secure login authentication
* Deposit funds
* Withdraw funds
* Transfer money between accounts
* Balance inquiry
* Transaction history tracking
* Input validation and error handling
* Admin panel for monitoring accounts and transactions
* Automatic account number generation
* Timestamped transaction logs

---

## OOP Concepts Implemented

### Encapsulation

Sensitive data such as account balance, PIN, and account details are declared private and accessed through getter/setter methods.

### Inheritance

Specialized account types inherit from a base `Account` class:

* SavingsAccount
* CurrentAccount

### Polymorphism

Different account types override transaction rules such as withdrawal limits and minimum balance requirements.

### Abstraction

The base `Account` class defines common methods while child classes implement specific logic.

### Interfaces

Interfaces are used to define common transaction behaviors such as:

* deposit()
* withdraw()
* transfer()

---

## System Modules

### User Module

Handles customer-related operations:

* Registration
* Login
* Transaction execution
* Viewing account details

### Account Module

Responsible for:

* Account creation
* Balance updates
* Account validation
* Transaction linkage

### Transaction Module

Manages:

* Deposits
* Withdrawals
* Transfers
* Transaction logging

### Admin Module

Allows administrators to:

* View all accounts
* Monitor transactions
* Freeze or unfreeze accounts

### Authentication Module

Ensures secure system access through login verification.

---

## Technology Stack

* Programming Language: Java
* IDE: VS Code
* Version Control: Git + GitHub
* Runtime: JDK 8 or above

---

## Project Structure

```
Smart-Digital-Banking-System/
│
├── src/
│   ├── model/
│   │   ├── Account.java
│   │   ├── SavingsAccount.java
│   │   ├── CurrentAccount.java
│   │   └── Transaction.java
│   │
│   ├── service/
│   │   └── Bank.java
│   │
│   └── main/
│       └── Main.java
│
├── docs/
│   ├── UML-Diagram.png
│   └── Flowchart.png
│
├── README.md
├── .gitignore
```

---

## Validation & Security Features

* Prevents negative transactions
* Prevents withdrawals exceeding balance
* Input format validation
* Authentication required before transactions
* Error handling using exceptions

---

## Development Methodology

The project follows an **incremental development approach**, where features are implemented module by module and tested individually before integration.

Development phases:

1. Planning & Design
2. Core System Development
3. Feature Enhancement
4. Testing & Debugging
5. Final Deployment

---

## How to Run the Project

1. Clone the repository
2. Open project in IDE
3. Compile all files
4. Run `Main.java`

---

## Future Enhancements

* Graphical User Interface
* Database integration
* OTP verification
* Online payment simulation
* Interest calculation automation
* Mobile banking simulation

---

## Learning Outcomes

Through this project, developers gain practical experience in:

* Object-Oriented Programming
* Software architecture design
* Modular coding practices
* Exception handling
* Version control using Git
* Real-world system simulation

---

## Contributors

* Tanishka 
* Trika 
* Shubhankar
* Tanush

---

## ✅ Project Checklist

### Phase 1

* [ ] Design finalized (features, scope, architecture)
* [ ] UML + Flowchart created
* [ ] Core classes implemented
* [ ] Basic operations working (Create, Deposit, Withdraw, Balance)
* [ ] OOP concepts demonstrated
* [ ] Console interface functional

---

### Phase 2

* [ ] Login system
* [ ] Fund transfer
* [ ] Transaction history
* [ ] Admin module
* [ ] Data storage
* [ ] Exception handling
* [ ] Testing completed

---

### Final Submission

* [ ] Code cleaned & documented
* [ ] Diagrams uploaded
* [ ] System runs without errors

---

## Conclusion

This project successfully demonstrates how core banking operations can be modeled using Java and OOP principles. It serves as a foundational system illustrating how scalable, secure, and modular financial software can be designed.

---

