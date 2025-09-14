package BankingManagementSystem;

import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;


public class BankingApp {
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String username = "root";
    private static final String password = "root";
    private static final String dbName = "banking_system";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }


        try {
            Connection connection = DriverManager.getConnection(url, username, password);


            String createUser = "CREATE TABLE IF NOT EXISTS `user` ("
                    + "  full_name VARCHAR(255) NOT NULL,"
                    + "  email VARCHAR(255) NOT NULL PRIMARY KEY,"
                    + "  password VARCHAR(255) NOT NULL"
                    + ");";


            String createAccount = "CREATE TABLE IF NOT EXISTS `accounts` ("
                    + "  account_number BIGINT PRIMARY KEY,"
                    + "  full_name VARCHAR(255) NOT NULL,"
                    + "  email VARCHAR(255) NOT NULL,"
                    + "  balance DOUBLE NOT NULL,"
                    + "  hashed_pin VARCHAR(255) NOT NULL,"
                    + "  FOREIGN KEY (email) REFERENCES user(email)"
                    + ");";

            Statement stmt = connection.createStatement();


            stmt.executeUpdate(createUser);
            stmt.executeUpdate(createAccount);


            Scanner scanner = new Scanner(System.in);
            User user = new User(connection, scanner);
            Accounts accounts = new Accounts(connection, scanner);
            AccountManager accountManager = new AccountManager(connection, scanner);

            String email;
            long account_number;


            String[] colors = {
                    "\u001B[31m", // RED
                    "\u001B[33m", // YELLOW
                    "\u001B[32m", // GREEN
                    "\u001B[36m", // CYAN
                    "\u001B[34m", // BLUE
                    "\u001B[35m", // PURPLE
            };
            String RESET = "\u001B[0m";

            String asciiRaw =
                    "                                                                                                 \n" +
                            "     ,---.  ,------. ,-----.,--. ,--.,------. ,------.    ,-----.    ,---.  ,--.  ,--.,--. ,--. \n" +
                            "    '   .-' |  .---''  .--./|  | |  ||  .--. '|  .---'    |  |) /_  /  O  \\ |  ,'.|  ||  .'   / \n" +
                            "    `.  `-. |  `--, |  |    |  | |  ||  '--'.'|  `--,     |  .-.  \\|  .-.  ||  |' '  ||  .   '  \n" +
                            "    .-'    ||  `---.'  '--'\\'  '-'  '|  |\\  \\ |  `---.    |  '--' /|  | |  ||  | `   ||  |\\   \\ \n" +
                            "    `-----' `------' `-----' `-----' `--' '--'`------'    `------' `--' `--'`--'  `--'`--' '--' \n" +
                            "                                                                                                 \n";

            StringBuilder asciiRainbow = new StringBuilder();
            int colorIndex = 0;

            for (char c : asciiRaw.toCharArray()) {
                if (c == '\n' || c == ' ') {
                    asciiRainbow.append(c); // keep spaces/newlines uncolored
                } else {
                    asciiRainbow.append(colors[colorIndex % colors.length]).append(c).append(RESET);
                    colorIndex++;
                }
            }

            String ascii = asciiRainbow.toString();
            System.out.println(ascii);
            while (true) {

                System.out.println("*** WELCOME TO BANKING SYSTEM ***");
                System.out.println("-------------------------------------------");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("-------------------------------------------");
                System.out.println("Enter your choice: ");
                int choice1 = scanner.nextInt();
                switch (choice1) {
                    case 1:
                        user.register();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User Logged In!");
                            if (!accounts.account_exist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new Bank Account");
                                System.out.println("2. Exit");
                                if (scanner.nextInt() == 1) {
                                    account_number = accounts.open_account(email);
                                    System.out.println("Account Created Successfully");
                                    System.out.println("Your Account Number is: " + account_number);
                                } else {
                                    break;
                                }

                            }
                            account_number = accounts.getAccount_number(email);
                            int choice2 = 0;
                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                System.out.println("Enter your choice: ");
                                choice2 = scanner.nextInt();
                                switch (choice2) {
                                    case 1:
                                        accountManager.debit_money(account_number);
                                        break;
                                    case 2:
                                        accountManager.credit_money(account_number);
                                        break;
                                    case 3:
                                        accountManager.transfer_money(account_number);
                                        break;
                                    case 4:
                                        accountManager.getBalance(account_number);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Enter Valid Choice!");
                                        break;
                                }
                            }

                        } else {
                            System.out.println("Incorrect Email or Password!");
                        }
                        break;  // Added missing break statement
                    case 3:
                        System.out.println("THANK YOU FOR USING BANKING SYSTEM!!!");
                        System.out.println("Exiting System!");
                        return;
                    default:
                        System.out.println("Enter Valid Choice");
                        break;
                }
            }
        } catch (SQLException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }
}
