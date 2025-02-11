package bank.service;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountService {
    public static void createAccount(Connection conn, String accountNumber, double balance) throws SQLException {
        String sql = "INSERT INTO accounts (account_number, balance) VALUES (?, ?)";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            pstmt.setDouble(2, balance);
            pstmt.executeUpdate();
        }
    }

    public static double getBalance(Connection conn, String accountNumber) throws SQLException {
        String sql = "SELECT balance FROM accounts WHERE account_number = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, accountNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getDouble("balance");
                }
            }
        }
        throw new SQLException("Account not found");
    }

    public static void transferFunds(Connection conn, String fromAccount, String toAccount, double amount) {
        String withdrawSQL = "UPDATE accounts SET balance = balance - ? WHERE account_number = ? AND balance >= ?";
        String depositSQL = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
        String logTransactionSQL = "INSERT INTO transactions (from_account, to_account, amount, timestamp) VALUES (?, ?, ?, CURRENT_TIMESTAMP)";

        try {
            conn.setAutoCommit(false);

            try (PreparedStatement withdrawStmt = conn.prepareStatement(withdrawSQL);
                 PreparedStatement depositStmt = conn.prepareStatement(depositSQL);
                 PreparedStatement logStmt = conn.prepareStatement(logTransactionSQL)) {

                withdrawStmt.setDouble(1, amount);
                withdrawStmt.setString(2, fromAccount);
                withdrawStmt.setDouble(3, amount);
                int withdrawUpdated = withdrawStmt.executeUpdate();

                if (withdrawUpdated == 0) {
                    throw new SQLException("Insufficient funds or account not found");
                }

                depositStmt.setDouble(1, amount);
                depositStmt.setString(2, toAccount);
                depositStmt.executeUpdate();

                logStmt.setString(1, fromAccount);
                logStmt.setString(2, toAccount);
                logStmt.setDouble(3, amount);
                logStmt.executeUpdate();
            }
            conn.commit();
        } catch (SQLException e) {
            try {
                conn.rollback();
            } catch (SQLException rollbackEx) {
                rollbackEx.printStackTrace();
            }
            e.printStackTrace();
        } finally {
            try {
                conn.setAutoCommit(true);
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }
    }
}
