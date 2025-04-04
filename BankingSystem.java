import java.io.*;
import java.util.*;

public class BankingSystem {
    static Scanner sc = new Scanner(System.in);
    static final String DATA_FILE = "bank_users.txt";

    static class User {
        String username;
        String password;
        double balance;

        User(String username, String password, double balance) {
            this.username = username;
            this.password = password;
            this.balance = balance;
        }

        @Override
        public String toString() {
            return username + "," + password + "," + balance;
        }
    }

    static Map<String, User> users = new HashMap<>();

    public static void main(String[] args) {
        loadUsers();
        while (true) {
            System.out.println("\n--- Welcome to Banking System ---");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Admin Login");
            System.out.println("4. Exit");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline

            switch (choice) {
                case 1 -> registerUser();
                case 2 -> loginUser();
                case 3 -> adminLogin();
                case 4 -> {
                    saveUsers();
                    System.out.println("Thank you for using Banking System!");
                    return;
                }
                default -> System.out.println("Invalid choice!");
            }
        }
    }

    // --- Register a new user
    static void registerUser() {
        System.out.print("Enter new username: ");
        String username = sc.nextLine();

        if (users.containsKey(username)) {
            System.out.println("Username already exists!");
            return;
        }

        System.out.print("Enter password: ");
        String password = sc.nextLine();

        users.put(username, new User(username, password, 0.0));
        saveUsers();
        System.out.println("Registration successful! Please login.");
    }

    // --- Login as a user
    static void loginUser() {
        System.out.print("Enter username: ");
        String username = sc.nextLine();
        System.out.print("Enter password: ");
        String password = sc.nextLine();

        User user = users.get(username);
        if (user != null && user.password.equals(password)) {
            System.out.println("Login successful! Welcome " + username);
            userMenu(user);
        } else {
            System.out.println("Invalid username or password.");
        }
    }

    // --- Admin login
    // static void adminLogin() {
    //     System.out.print("Enter admin password: ");
    //     String pass = sc.nextLine();

    //     if (!pass.equals("admin123")) {
    //         System.out.println("Wrong admin password.");
    //         return;
    //     }

    //     System.out.println("\n--- Admin Panel ---");
    //     if (users.isEmpty()) {
    //         System.out.println("No users registered yet.");
    //     } else {
    //         for (User u : users.values()) {
    //             System.out.println("User: " + u.username + ", Balance: ₹" + u.balance);
    //         }
    //     }
    // }


    static void adminLogin() {
        System.out.print("Enter admin password: ");
        String pass = sc.nextLine();
    
        if (!pass.equals("admin123")) {
            System.out.println("Wrong admin password.");
            return;
        }
    
        System.out.println("\n✅ Admin login successful!");
    
        while (true) {
            System.out.println("\n--- Admin Panel ---");
            System.out.println("1. View All Accounts Summary");
            System.out.println("2. View Individual Account Details");
            System.out.println("3. Logout Admin Panel");
            System.out.print("Enter choice: ");
            int choice = sc.nextInt();
            sc.nextLine(); // consume newline
    
            switch (choice) {
                case 1 -> {
                    System.out.println("\n--- All Users Summary ---");
                    if (users.isEmpty()) {
                        System.out.println("No users registered.");
                    } else {
                        for (User u : users.values()) {
                            System.out.println("• Username: " + u.username + " | Balance: ₹" + u.balance);
                        }
                    }
                }
    
                case 2 -> {
                    System.out.print("Enter username to view details: ");
                    String name = sc.nextLine();
                    User u = users.get(name);
                    if (u != null) {
                        System.out.println("\n--- Account Details ---");
                        System.out.println("Username : " + u.username);
                        System.out.println("Password : " + u.password);
                        System.out.println("Balance  : ₹" + u.balance);
                    } else {
                        System.out.println("User not found.");
                    }
                }
    
                case 3 -> {
                    System.out.println("Admin panel logged out.");
                    return;
                }
    
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    
    // --- User operations
    static void userMenu(User user) {
        while (true) {
            System.out.println("\n1. Check Balance");
            System.out.println("2. Deposit");
            System.out.println("3. Withdraw");
            System.out.println("4. Logout");
            System.out.print("Enter choice: ");
            int ch = sc.nextInt();

            switch (ch) {
                case 1 -> System.out.println("Your balance: ₹" + user.balance);
                case 2 -> {
                    System.out.print("Enter amount to deposit: ₹");
                    double amt = sc.nextDouble();
                    if (amt > 0) {
                        user.balance += amt;
                        System.out.println("Amount deposited successfully.");
                        saveUsers();
                    } else {
                        System.out.println("Invalid amount.");
                    }
                }
                case 3 -> {
                    System.out.print("Enter amount to withdraw: ₹");
                    double amt = sc.nextDouble();
                    if (amt > 0 && user.balance >= amt) {
                        user.balance -= amt;
                        System.out.println("Amount withdrawn successfully.");
                        saveUsers();
                    } else {
                        System.out.println("Insufficient balance or invalid amount.");
                    }
                }
                case 4 -> {
                    System.out.println("Logging out...");
                    return;
                }
                default -> System.out.println("Invalid choice.");
            }
        }
    }

    // --- Load users from file
    static void loadUsers() {
        try (BufferedReader reader = new BufferedReader(new FileReader(DATA_FILE))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 3) {
                    String username = parts[0];
                    String password = parts[1];
                    double balance = Double.parseDouble(parts[2]);
                    users.put(username, new User(username, password, balance));
                }
            }
        } catch (IOException ignored) {}
    }

    // --- Save users to file
    static void saveUsers() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(DATA_FILE))) {
            for (User user : users.values()) {
                writer.write(user.toString());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error saving data.");
        }
    }
}
