package org.example;

import java.io.IOException;

public class QueryCase
{
    /**
     *This method will  get a query string as input and call the appropriate method based on the command in the query.
     * @param query this parameter is used to store a input query string
     * @throws IOException when an I/O operation has failed or is interrupted
     */
    public static void queryCall(String query) throws IOException
    {

        //Split the query string into tokens
        String[] tokens = query.split(" ");

        // Get the command and table names from the String array tokens
        String getcommand = tokens[0];
        String createTable=tokens[2];
        String upadteTable=tokens[1];

        // Create a new Query object to handle the different queries
        Query query1 = new Query();

        // Switch statement to handle different commands
        switch (getcommand) {

            //Call the createTable() method in the Query class, If the command is CREATE TABLE
            case "CREATE":
                if (tokens.length >= 3 && tokens[1].equalsIgnoreCase("TABLE")) {
                    query1.createTable(query);
                }
                break;

            //Call the insertData() method in the Query class,If the command is INSERT
        case "INSERT":
                    query1.insertData(createTable,query);
                break;

            //Call the selectData() method in the Query class,If the command is SELECT
            case "SELECT":
                  if (tokens.length >= 3) {
                query1.selectData(createTable);
                }
                break;
            // Call the updateData() method in the Query class,If the command is UPDATE
            case "UPDATE":
                query1.updateData("C:\\ass5408\\src\\main\\java\\org\\example\\" + upadteTable + ".txt", query);
                System.out.println("Data updated successfully.");
                break;

             //Call the deleteData() method in the Query class,If the command is DELETE
            case "DELETE":
                    query1.deleteData("C:\\ass5408\\src\\main\\java\\org\\example\\" + createTable + ".txt",query);
                break;
            //Print an error message,If the command is invalid
            default:
                System.out.println("Invalid command.");
                break;
        }
    }
}
