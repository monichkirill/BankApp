package bank;

import bank.database.DBConnection;
import bank.service.AccountService;

import java.sql.Connection;
import java.sql.SQLException;

public class BankApp {
    public static void main(String[] args) {
        try (Connection conn = DBConnection.getConnection()) {
            AccountService.createAccount(conn, "123456789", 500.00);
            AccountService.createAccount(conn, "987654321", 300.00);
            System.out.println("Initial Balance:");
            System.out.println("Account 123456789: " + AccountService.getBalance(conn, "123456789"));
            System.out.println("Account 987654321: " + AccountService.getBalance(conn, "987654321"));

            AccountService.transferFunds(conn, "123456789", "987654321", 200.00);

            System.out.println("Balance After Transfer:");
            System.out.println("Account 123456789: " + AccountService.getBalance(conn, "123456789"));
            System.out.println("Account 987654321: " + AccountService.getBalance(conn, "987654321"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
