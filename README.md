# Task
Task is to implement a bank account management system

Create a Java application that will interact with a database using JDBC. Your task is to implement a bank account management system that supports transactions.

Requirements Database setup: Create a table accounts with fields: id (PRIMARY KEY, BIGSERIAL) account_number (VARCHAR) balance (DECIMAL)

Application functions: Create account: Method to add a new account with an initial balance. View balance: Method to get the current balance by account number. Transfer funds: Method to transfer funds from one account to another. This method must be implemented as a transaction to ensure data integrity. Transactions:

Implement the transfer of funds between two accounts as a transaction. If the transfer cannot be completed (for example, insufficient funds), the transaction must be rolled back. Error handling:

Handle possible exceptions such as insufficient funds for the transfer, and ensure that the transaction is rolled back in case of an error.
