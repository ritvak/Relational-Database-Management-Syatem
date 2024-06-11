package org.example;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * This class is called for a new user registration
 */
public class Registration {
    //Initialized a hashmap to store userId and hashed password
    public  HashMap<Integer, String> hashmap;

    public Registration() {
        //hashmap initialization
        hashmap = new HashMap<>();
    }

    /**
     *
     * @param userId1 this parameter is used to define user's ID
     * @param password1 this parameter is used to define user's password
     * @param question1 this parameter is used to define user's security question
     * @param answer1 this parameter is used to define user's security answer
     * @throws IOException when an I/O operation has failed or is interrupted
     * @throws NoSuchAlgorithmException requested cryptographic algorithm is not available in the environment
     */
    public void register(int userId1, String password1, String question1, String answer1) throws IOException, NoSuchAlgorithmException {
        //used SHA-256 for user's password
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password1.getBytes());
        byte[] digest = md.digest();
        String hashedpass = String.format("%064x", new java.math.BigInteger(1, digest));

        // check the userId is already exists in database or text file
        if (hashmap.containsKey(userId1)) {
            System.out.println("User already exists");
            return;
        }
        // read the userInfo.txt file to check that userId already exists
        BufferedReader br = new BufferedReader(new FileReader("C:\\ass5408\\src\\main\\java\\org\\example\\userInfo.txt"));
        String line;
        while ((line = br.readLine()) != null) {
            String[] fields = line.split(",");
            int userId = Integer.parseInt(fields[0]);
            if (userId == userId1) {
                System.out.println("User already exists");
                br.close();
                return;
            }
        }
        br.close();

        //userId and hashed password will be added in the hashmap
        hashmap.put(userId1, hashedpass);

        //add the user information in userInfo.txt file (database)
        FileWriter fw = new FileWriter("C:\\ass5408\\src\\main\\java\\org\\example\\userInfo.txt", true);
        BufferedWriter bw = new BufferedWriter(fw);
        bw.write(userId1 + "," + hashedpass + "," + question1 + "," + answer1);
        bw.newLine();
        bw.close();
        // Registration successfully message will be showed on console
        System.out.println("Registration successfully");
    }
}
