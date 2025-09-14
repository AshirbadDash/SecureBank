package BankingManagementSystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;

public class AccountManager {
    private final Connection connection;
    private final Scanner scanner;

    AccountManager(Connection connection, Scanner scanner) {
        this.connection = connection;
        this.scanner = scanner;
    }

    private String encoder(String input) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(input.getBytes());
            StringBuilder sb = new StringBuilder();
            for (byte b : array) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void credit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        security_pin=encoder(security_pin);

        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and hashed_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                    PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
                    preparedStatement1.setDouble(1, amount);
                    preparedStatement1.setLong(2, account_number);
                    int rowsAffected = preparedStatement1.executeUpdate();
                    if (rowsAffected > 0) {
                        System.out.println("Rs." + amount + " credited Successfully");
                        connection.commit();
                        connection.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction Failed!");
                        connection.rollback();
                        connection.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public void debit_money(long account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        security_pin=encoder(security_pin);
        try {
            connection.setAutoCommit(false);
            if (account_number != 0) {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM Accounts WHERE account_number = ? and hashed_pin = ? ");
                preparedStatement.setLong(1, account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {

                        String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                        preparedStatement1.setDouble(1, amount);
                        preparedStatement1.setLong(2, account_number);
                        int rowsAffected = preparedStatement1.executeUpdate();
                        if (rowsAffected > 0) {
                            System.out.println("Rs." + amount + " debited Successfully");
                            connection.commit();
                            connection.setAutoCommit(true);
                            return;
                        } else {
                            System.out.println("Transaction Failed!");
                            connection.rollback();
                            connection.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    System.out.println("Invalid Security Pin!");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public void transfer_money(long sender_account_number) throws SQLException {
        scanner.nextLine();
        System.out.print("Enter Receiver Account Number: ");
        long receiver_account_number = scanner.nextLong();
        System.out.print("Enter Amount: ");
        double amount = scanner.nextDouble();
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        security_pin=encoder(security_pin);
        try {
            connection.setAutoCommit(false);
            if (sender_account_number != 0 && receiver_account_number != 0) {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("SELECT * FROM Accounts WHERE account_number = ? AND hashed_pin = ? ");
                preparedStatement.setLong(1, sender_account_number);
                preparedStatement.setString(2, security_pin);
                ResultSet resultSet = preparedStatement.executeQuery();

                if (resultSet.next()) {
                    double current_balance = resultSet.getDouble("balance");
                    if (amount <= current_balance) {
                        // Check if receiver account exists
                        PreparedStatement receiverCheck = connection
                                .prepareStatement("SELECT account_number FROM Accounts WHERE account_number = ?");
                        receiverCheck.setLong(1, receiver_account_number);
                        ResultSet receiverResult = receiverCheck.executeQuery();

                        if (receiverResult.next()) {
                            // Debit from sender
                            String debit_query = "UPDATE Accounts SET balance = balance - ? WHERE account_number = ?";
                            PreparedStatement preparedStatement1 = connection.prepareStatement(debit_query);
                            preparedStatement1.setDouble(1, amount);
                            preparedStatement1.setLong(2, sender_account_number);

                            // Credit to receiver
                            String credit_query = "UPDATE Accounts SET balance = balance + ? WHERE account_number = ?";
                            PreparedStatement preparedStatement2 = connection.prepareStatement(credit_query);
                            preparedStatement2.setDouble(1, amount);
                            preparedStatement2.setLong(2, receiver_account_number);

                            int rowsAffected1 = preparedStatement1.executeUpdate();
                            int rowsAffected2 = preparedStatement2.executeUpdate();
                            if (rowsAffected1 > 0 && rowsAffected2 > 0) {
                                System.out.println("Transaction Successful!");
                                System.out.println("Rs." + amount + " Transferred Successfully");
                                connection.commit();
                                connection.setAutoCommit(true);
                                return;
                            } else {
                                System.out.println("Transaction Failed!");
                                connection.rollback();
                                connection.setAutoCommit(true);
                            }
                        } else {
                            System.out.println("Invalid Receiver Account Number!");
                        }
                    } else {
                        System.out.println("Insufficient Balance!");
                    }
                } else {
                    System.out.println("Invalid Security Pin!");
                }
            } else {
                System.out.println("Invalid Account Number!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connection.rollback();
        connection.setAutoCommit(true);
    }

    public void getBalance(long account_number) {
        scanner.nextLine();
        System.out.print("Enter Security Pin: ");
        String security_pin = scanner.nextLine();
        security_pin=encoder(security_pin);
        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT balance FROM Accounts WHERE account_number = ? AND hashed_pin = ?");
            preparedStatement.setLong(1, account_number);
            preparedStatement.setString(2, security_pin);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                double balance = resultSet.getDouble("balance");
                System.out.println("Balance: " + balance);
            } else {
                System.out.println("Invalid Pin!");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
