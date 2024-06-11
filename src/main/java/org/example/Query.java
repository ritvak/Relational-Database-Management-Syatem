package org.example;

import java.io.*;
import java.util.*;

/**
 * This class is called to perform different queries
 */
public class Query {

    public static final String DELIMITER = ",";
    /**
     * This method will create a table based on the string which will get from createTable method
     * @param createQuery this parameter is used to store the input query string (UPDATE query)
     * @throws IOException when an I/O operation has failed or is interrupted
     */
    public static void createTable(String createQuery) throws IOException {

        // Split the createQuery string into an array of tokens
        String[] tokens = createQuery.split("\\s+");

        // Get name of the table from the tokens array
        String tableName = tokens[2];

        // Created file using the table name and a ".txt" extension
        File file = new File("C:\\ass5408\\src\\main\\java\\org\\example\\" + tableName + ".txt");

        // If the file doesn't exist, create it and print a success message
        if (file.createNewFile()) {
            System.out.println("Table is created successfully: " + file.getName());
        }

        //Print an error message when the table already exists
        else {
            System.out.println("Table is already exists.");
        }
    }

    /**
     *This method reads data from a file and prints it on the console.
     *  It takes a string token which represents the name of the file to be read.
     * @param tokens this parameter is used to store the name of the table which will be gotten from SELECT query
     * @throws IOException when an I/O operation has failed or is interrupted
     */
    public static void selectData(String tokens) throws IOException {
        List<String[]> data = null;
        BufferedReader reader = null;
        try {

            // Create a File  using the given token and path.
            File file = new File("C:\\ass5408\\src\\main\\java\\org\\example\\" + tokens + ".txt");

            //it checks that file is already exists
            if (!file.exists()) {
                throw new FileNotFoundException("File not exists: " + file.getPath());
            }

            // Created a BufferedReader object to read the file.
            reader = new BufferedReader(new FileReader(file));
            data = new ArrayList<>();
            String line;

            //Read each line from the file, split it by comma and store in a list
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(",");
                if (fields.length >= 4) {
                    data.add(fields);
                }
            }
        } catch (IOException e) {
            System.err.println("Error while reading data: " + e.getMessage());
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

        //Print the data on to the console, if data exists
        if (data != null) {
            for (String[] record : data) {
                int id = Integer.parseInt(record[0]);
                String name = record[1];
                String dob = record[2];
                String emailid = record[3];
                System.out.println("studentID: " + id);
                System.out.println("studentName: " + name);
                System.out.println("dob: " + dob);
                System.out.println("emailId: " + emailid);
                System.out.println("-----------");
            }
        }

        //Print an error message
        else {
            System.err.println("No data was read.");
        }
    }


    /**
     * tokensInserts data into a table by parsing the input query.
     * @param tokens the name of the table to insert data into
     *@param query the SQL query containing the values to be inserted
     */
    public static void insertData(String tokens,String query) {
        String tableName = tokens;

        String valuesString = query.substring(query.indexOf("VALUES") + 7, query.length() - 1);
        valuesString = valuesString.replaceAll("\\(", "").replaceAll("\\)", ""); // remove parentheses
        String[] values = valuesString.split(",");

        for (int i = 0; i < values.length; i++) {
            values[i] = values[i].trim().replaceAll("'", "");
        }
        HashMap<String, String> rowValues = new HashMap<String, String>();
        rowValues.put("studentId", values[0]);
        rowValues.put("password", values[1]);
        rowValues.put("username", values[2]);
        rowValues.put("dalID", values[3]);

        // Open the file and add data into the new raw
        try {
            File file = new File("C:\\ass5408\\src\\main\\java\\org\\example\\" + tokens + ".txt");
            FileWriter filewriter = new FileWriter(file, true);
            if (file.length() > 0) {
                filewriter.write("\n");
            }

            // Check if the studentId already exists in the table
            int studentId = Integer.parseInt(values[0]);
            if (checkPrimaryKey(tableName, studentId)) {
                System.out.println("Error: Primary key violation on studentId " + studentId);
                return;
            }

            //Add  the values to the new row to the file
            filewriter.write(String.join(",", rowValues.values()));
            filewriter.close();
            System.out.println("Values added to " + tableName + ".txt file");
        } catch (IOException e) {
            System.out.println("Got error while writing to the file.");
            e.printStackTrace();
        }
    }

    /**
     * This method will get a table name and a student ID as input parameters and checks if the given student ID already exists in the table
     * @param tableName this parameter will give the name of the table name to store add  the new data
     * @param studentId this parameter will give the unique identification
     * @return  it will return that user is alreday exists or not
     * @throws IOException when an I/O operation has failed or is interrupted
     */

    public static boolean checkPrimaryKey(String tableName, int studentId) throws IOException {
        File file = new File("C:\\ass5408\\src\\main\\java\\org\\example\\" + tableName + ".txt");
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split(",");
            if (Integer.parseInt(values[0]) == studentId) {
                return true;
            }
        }
        reader.close();
        return false;
    }

    /**
     * Method will get  file path and an SQL query string for updating data
     * @param FILE_PATH it will give the file path where data needs to be updated
     * @param query it gives the UPDATE query to update the data in table(text file)
     */

    public static void updateData(String FILE_PATH, String query) {

        String field = getField(query);
        String newValue = getNewValue(query);

        if (field == null || newValue == null) {
            System.out.println("Give valid update query.");
            return;
        }

        try {
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            String[] lines = reader.lines().toArray(String[]::new);
            reader.close();

            for (int i = 0; i < lines.length; i++) {
                String[] values = lines[i].split(",");
                int studentId = Integer.parseInt(values[0]);

                if (query.contains("studentId = " + studentId)) {
                    int fieldIndex = getIndex(field);
                    if (fieldIndex >= 0) {
                        values[fieldIndex] = newValue;
                    } else {
                        System.out.println("Field name is not valid.");
                        return;
                    }

                    String updatedLine = String.join(",", values);

                    FileWriter writer = new FileWriter(FILE_PATH, false);
                    lines[i] = updatedLine;
                    writer.write(String.join("\n", lines));
                    writer.close();
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     *Method get the field name from the update query string using string manipulation and returns it
     * @param updateQuery it gives the UPDATE query to update the data in table(text file)
     * @return it will return the data that has been upadted in table(text file)
     */
    public static String getField(String updateQuery) {
        int startIndex = updateQuery.indexOf("SET ") + 4;
        int endIndex = updateQuery.indexOf(" = ");
        if (startIndex >= 4 && endIndex > startIndex) {
            return updateQuery.substring(startIndex, endIndex);
        } else {
            return null;
        }
    }

    public static String getNewValue(String updateQuery) {
        int startIndex = updateQuery.indexOf("= ") + 2;
        int endIndex = updateQuery.indexOf(" WHERE ");
        if (startIndex >= 2 && endIndex > startIndex) {
            return updateQuery.substring(startIndex, endIndex).replaceAll("'", "");
        } else {
            return null;
        }
    }

    /**
     *
     * @param field is defined the which filed needs to be updated in table(text file)
     * @return retunr the index of the field to update
     */
    public static int getIndex(String field) {
        switch (field) {
            case "studentId":
                return 0;
            case "username":
                return 1;
            case "emailId":
                return 2;
            case "dob":
                return 3;
            default:
                return -1;
        }
    }

    /**
     *The deleteData method get file path and a delete query as input and deletes the data from the file matching the query
     * @param FILE_PATH it will give the file path where data needs to be deleted
     * @param query it gives the DELETE query to DELETE the data in table(text file)
     */

    public static void deleteData(String FILE_PATH, String query) {

        try {

            //read the data from the file
            BufferedReader reader = new BufferedReader(new FileReader(FILE_PATH));
            List<String> lines = new ArrayList<>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            reader.close();

            // Create a list to hold the updated lines after deletion operation
            List<String> updatedLines = new LinkedList<>();

            for (String currentLine : lines) {
                String[] values = currentLine.split(",");
                int studentId = Integer.parseInt(values[0]);

                if (!query.contains("studentId = " + studentId)) {
                    updatedLines.add(currentLine);
                }

            }

            //write updated data back to the file
            FileWriter writer = new FileWriter(FILE_PATH, false);
            writer.write(String.join("\n", updatedLines));
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}