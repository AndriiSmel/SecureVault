# SecureVault

SecureVault is a Java console application that currently supports in-memory user registration and login. Personal account records and SQLite persistence are planned.

I’m building SecureVault as part of my university Cybersecurity coursework to practice Java, object-oriented programming, database management, and secure software development.

## Features

### Implemented

- [x] Maven project setup and interactive console menu
- [x] In-memory user registration with unique usernames
- [x] Login with password verification
- [x] PBKDF2 password hashing with a unique random salt for each registration
- [x] Username and password validation
- [x] Password strength evaluation and a breach lookup during registration

### Planned

- [ ] Authenticated dashboard with a logout option
- [ ] SQLite persistence with JDBC
- [ ] User-specific account records and CRUD operations
- [ ] Record access controls
- [ ] Cryptographically secure password generator
- [ ] Authentication attempt limiting
- [ ] Automated tests for authentication and record management

## Technologies

| Technology | Purpose |
|---|---|
| Java 27 | Application development |
| Apache Maven | Build and dependency management |
| nbvcxz | Password strength evaluation |
| Git and GitHub | Version control |
| SQLite and JDBC | Local persistence, planned |
| JUnit | Automated testing, planned |

## Getting Started

Install JDK 27, Maven, and Git. Then clone and enter the repository.

```bash
git clone https://github.com/AndriiSmel/SecureVault.git
cd SecureVault
```

Compile and launch the application with Maven so the runtime classpath includes its dependencies.

```bash
mvn compile exec:java -Dexec.mainClass=com.securevault.App
```

Choose **Register** to create a test account, then choose **Login** to try it. Accounts are stored in memory and reset when the application exits.

## Project Structure

```text
SecureVault/
├── src/
│   ├── main/java/com/securevault/
│   │   ├── App.java
│   │   ├── model/User.java
│   │   ├── repository/UserRepository.java
│   │   ├── security/PasswordHasher.java
│   │   ├── service/AuthService.java
│   │   └── ui/MainMenu.java
│   └── test/java/com/securevault/
│       └── AppTest.java
├── pom.xml
├── .gitignore
└── README.md
```

## Development Roadmap

- [x] **Week 1 —** Java fundamentals and console navigation
- [x] **Week 2 —** Object-oriented programming, collections, and in-memory authentication
- [ ] **Week 3 —** SQL, SQLite, JDBC, and user-specific account records
- [ ] **Week 4 —** Security improvements, testing, refactoring, and documentation

## Security Considerations

SecureVault is an educational project. Use test credentials only.

Passwords used to sign in to SecureVault are hashed with PBKDF2-HMAC-SHA256 and random salts. Registration continues if the breach lookup is unavailable.

SQLite-backed records will use parameterized queries and ownership checks. Storing third-party passwords, if added later, would require a separate encryption mechanism.

## Project Status

**In development — Week 2.** In-memory registration and login are implemented. The authenticated dashboard and SQLite persistence are upcoming.

## Author

Andrii · [@AndriiSmel on GitHub](https://github.com/AndriiSmel)
