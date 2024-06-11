package org.example;

import java.security.NoSuchAlgorithmException;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.io.IOException;

public class Main {
    /**
     *main method will give choice to the user between registering and logging in
     * @param args defines the command-line arguments passed to the program
     * @throws IOException when an I/O operation has failed or is interrupted
     * @throws NoSuchAlgorithmException requested cryptographic algorithm is not available in the environment
     */
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        Scanner scanner = new Scanner(System.in);

        //use to choose between register and login
        System.out.println("Please choose an option from register or login:");
        System.out.println("1. Register");
        System.out.println("2. Login");

        int choice;

        try {
            choice = scanner.nextInt();
            scanner.nextLine();
        } catch (InputMismatchException e) {
            System.out.println("Invalid input. Please enter a number 1 or 2.");
            return;
        }

        //create authentication and query objects
        Authentication auth = new Authentication();
        QueryCase qc = new QueryCase();
        String query;
        int userId1;

        switch (choice) {
            case 1:
                //register a user when user select choice 1
                Registration registration=new Registration();
                System.out.print("Enter your user ID: ");
                try {
                    userId1 = scanner.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("Invalid input. User ID should be an integer.");
                    return;
                }
                System.out.print("Enter your password: ");
                String password1 = scanner.next();
                scanner.nextLine();

                System.out.print("Enter your security question: ");
                String question1 = scanner.nextLine();

                System.out.print("Enter your security answer: ");
                String answer1 = scanner.nextLine();

                //register a user
                registration.register(userId1,password1,question1,answer1);

                break;

            case 2:

                //user will be able to login when user select choice 2
                //user provide userId, password, question and answer for authentication
                System.out.print("Enter your user ID: ");
                int userId = scanner.nextInt();

                scanner.nextLine();

                System.out.print("Enter your password: ");
                String password = scanner.next();
                scanner.nextLine();

                System.out.print("Enter your security question: ");
                String question = scanner.nextLine();

                System.out.print("Enter your security answer: ");
                String answer = scanner.nextLine();

                //user will be verified
                boolean verified = auth.userverified(userId, password, question, answer);

                // If the user is verified, user can enter a SQL query or exit to quit
                if (verified) {
                    System.out.println("User verified.");
                    do {
                        System.out.println("Enter the  SQL query or exit to quit:");
                        query = scanner.nextLine();
                        if (!query.equals("exit")) {
                            qc.queryCall(query);
                        }
                    } while (!query.equals("exit"));
                } else {
                    System.out.println("User is not verified.");
                }
                //if user's choice is not 1 or 2
            default:
                System.out.println("Choice is not valid. Please choose 1 or 2.");
                break;
        }
    }
}
