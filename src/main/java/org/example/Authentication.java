package org.example;//required packages
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

/**
 * This class is called to verify the user's credentials
 */
public class Authentication {

    //initialization of constatnt variables of file path and delimiter
    public static final String FILE_PATH = "C:\\ass5408\\src\\main\\java\\org\\example\\userInfo.txt";
    public static final String DELIMITER = ",";

    //used hashmap to store userId and password
    public HashMap<Integer, String> userMap;


    /**
     *initialize constructor with user information
     * @throws NoSuchAlgorithmException requested cryptographic algorithm is not available in the environment
     */
    public Authentication() throws NoSuchAlgorithmException {
        userMap = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                int userId = Integer.parseInt(data[0]);
                String hashedPassword = data[1];
                userMap.put(userId, hashedPassword);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *this function will verity the user's credentials
     * @param userId this parameter is used to define user's ID
     * @param password this parameter is used to define user's password
     * @param question this parameter is used to define user's security question
     * @param answer this parameter is used to define user's security answer
     * @return it is used to check that user is already exists or not
     *
     */
    public boolean userverified(int userId, String password, String question, String answer) {
        try {
            //it will check that user already exists in hashmap
            if (!userMap.containsKey(userId)) {
                return false;
            }
            //check the user's password
            String newpass = userMap.get(userId);
            if (!verifyPassword(password, newpass)) {
                return false;
            }
            // Check users's question and answer
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(DELIMITER);
                if (Integer.parseInt(data[0]) == userId && data[2].equals(question) && data[3].equals(answer)) {
                    reader.close();
                    return true;
                }
            }
            reader.close();
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     *this method will verify password using SHA-256 hash function
     * @param password this parameter is used to store user's password
     * @param newpass this parameter is used to store password which is converted in to Bytes
     * @return it will return hashed password
     * @throws NoSuchAlgorithmException requested cryptographic algorithm is not available in the environment
     */

    private boolean verifyPassword(String password, String newpass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] digestValue = md.digest();
        String hashedpass = String.format("%064x", new java.math.BigInteger(1, digestValue));
        return hashedpass.equals(newpass);
    }
}
