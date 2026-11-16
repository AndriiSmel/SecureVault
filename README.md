# SecureVault

**SecureVault** is a Java-based console application for user authentication and personal account management.

This project is being developed as part of my university coursework in **Cybersecurity**. Its primary goal is to develop practical skills in Java programming, object-oriented programming (OOP), database management, and secure software development.

## Features

### Implemented
- [x] Initial Maven project setup
- [x] Basic console application

### Planned
- [ ] Interactive console menu
- [ ] User registration and login
- [ ] Secure password hashing with PBKDF2
- [ ] Password strength evaluation
- [ ] Cryptographically secure password generator
- [ ] User-specific account records
- [ ] Create, read, update, and delete records
- [ ] SQLite database integration
- [ ] Input validation and error handling
- [ ] Authentication attempt limiting
- [ ] Automated tests

## Technologies

| Technology | Purpose |
|---|---|
| Java 27 | Core programming language |
| Apache Maven | Build and dependency management |
| SQLite | Local relational database (planned) |
| JDBC | Database connectivity (planned) |
| JUnit | Automated testing (planned) |
| Git & GitHub | Version control |

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 27
- Apache Maven
- Git

### Installation

Clone the repository:

```bash
git clone https://github.com/AndriiSmel/SecureVault.git
```

Navigate to the project directory:

```bash
cd SecureVault
```

Compile the application:

```bash
mvn compile
```

Run the application:

```bash
java -cp target/classes com.securevault.App
```

## Project Structure

```text
SecureVault/
├── src/
│   ├── main/
│   │   └── java/
│   │       └── com/securevault/
│   │           └── App.java
│   └── test/
│       └── java/
├── pom.xml
├── .gitignore
└── README.md
```

The project structure will expand as new features are implemented.

## Development Roadmap

- [ ] **Week 1:** Java fundamentals, conditions, loops, methods, and console navigation
- [ ] **Week 2:** Object-oriented programming, collections, and user authentication
- [ ] **Week 3:** SQL, SQLite, JDBC, and account management
- [ ] **Week 4:** Security improvements, testing, refactoring, and documentation

## Security Considerations

SecureVault is an educational project and is **not intended for storing real credentials** in its current state.

Planned security measures include:

- Password hashing using PBKDF2 with unique random salts
- Secure random password generation
- Input validation
- Protection against SQL injection through prepared statements
- Access controls separating user records
- Authentication attempt limiting

Account passwords will be hashed, not reversibly encrypted. If storage of third-party passwords is implemented, it will require a separate encryption mechanism.

## Project Status

**In Development — Week 1**

The project is currently focused on establishing the Java development environment and implementing the console interface.

## Author

**Andrii**

GitHub: [@AndriiSmel](https://github.com/AndriiSmel)