# SecureVault

SecureVault is a simple console password manager written in Java. It lets users store their passwords in a local SQLite database.

## Features

- Register an account and log in.
- Add a password entry with a service name, login, password, and optional website URL.
- View and delete saved entries.
- Generate random passwords and passphrases.
- Log out of the account.

Each user has their own vault. Account passwords are hashed, and saved passwords are encrypted using AES-GCM. The program also checks account passwords for strength and known data breaches during registration.


Registration password length is minimum 15, maximum 128.
