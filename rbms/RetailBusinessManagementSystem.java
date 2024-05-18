// usage:  1. compile: javac -cp /usr/lib/oracle/18.3/client64/lib/ojdbc8.jar RetailBusinessManagementSystem.java
//         2. execute: java -cp /usr/lib/oracle/18.3/client64/lib/ojdbc8.jar RetailBusinessManagementSystem.java

import java.sql.*;
import java.math.*;
import java.io.*;
import oracle.jdbc.*;
import java.awt.*;
import oracle.jdbc.pool.OracleDataSource;

// Define a public class named RetailBusinessManagementSystem
public class RetailBusinessManagementSystem {
    // Define the main method which throws a SQLException
    public static void main(String args[]) throws SQLException {

        // Initialize a boolean variable to keep track of whether to exit the program or
        // not
        boolean exit = false;
        try {
            // Establish a connection with an Oracle database using the OracleDataSource
            // class
            OracleDataSource ds = new oracle.jdbc.pool.OracleDataSource();
            ds.setURL("jdbc:oracle:thin:@castor.cc.binghamton.edu:1521:acad111");
            Connection conn = ds.getConnection("sgunukula", "gskr19995");

            // Loop until exit is true
            while (!exit) {

                // Print the menu options
                System.out.println();
                String input;
                BufferedReader userInput = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Enter the number from the below menu");
                System.out.println("1.Display all tables");
                System.out.println("2.Monthly Sales Activity of an Employee");
                System.out.println("3.Add Employee");
                System.out.println("4.Add purchase");
                System.out.println("5.exit");

                // Read the user input
                input = userInput.readLine();

                // Initialize another boolean variable to keep track of whether to exit a
                // sub-menu or not
                boolean exit1 = false;

                // Check if the user selected option 1
                if (input.equals("1")) {
                    // Loop until exit1 is true
                    while (!exit1) {
                        System.out.println();
                        String tableselected;

                        // Print the sub-menu options
                        System.out.println("Choose the table to displayed from the below menu");
                        System.out.println("1.Employees");
                        System.out.println("2.Customers");
                        System.out.println("3.Products");
                        System.out.println("4.Product Discount");
                        System.out.println("5.Purchases");
                        System.out.println("6.Logs");

                        // Read the user input
                        tableselected = userInput.readLine();

                        // Check the user input and call the corresponding method to display the table
                        switch (tableselected) {
                            case "1":
                                show_employees(conn); // call the show_employees() method with conn as parameter
                                exit1 = true;
                                break;
                            case "2":
                                show_customers(conn); // call the show_customers() method with conn as parameter
                                exit1 = true;
                                break;
                            case "3":
                                show_products(conn); // call the show_products() method with conn as parameter
                                exit1 = true;
                                break;
                            case "4":
                                show_prod_discnt(conn); // call the show_prod_discnt() method with conn as parameter
                                exit1 = true;
                                break;
                            case "5":
                                show_purchases(conn); // call the show_purchases() method with conn as parameter
                                exit1 = true;
                                break;
                            case "6":
                                show_logs(conn); // call the show_logs() method with conn as parameter
                                exit1 = true;
                                break;
                            default:
                                System.out.println("Invalid input!Please select correct option");
                                break;

                        }
                    }

                    // Check if the user selected option 2
                } else if (input.equals("2")) {
                    String e_id;
                    System.out.println("Enter Employee ID of the Employee: ");
                    e_id = userInput.readLine();
                    // Calling method to retrieve monthly sale activities of the Employee with given
                    // Employee ID
                    getMonthlySaleActivities(e_id, conn);
                } else if (input.equals("3")) {
                    String e_id;
                    String employeeName;
                    String telephoneNumber;
                    String email;
                    // Prompt the user to enter employee details
                    System.out.println("Enter eid :");
                    e_id = userInput.readLine();
                    System.out.println("Enter Employee Name :");
                    employeeName = userInput.readLine();
                    System.out.println("Enter Telephone Number of Employee :");
                    telephoneNumber = userInput.readLine();
                    System.out.println("Enter Employee email :");
                    email = userInput.readLine();
                    // Call the add_employee method to add the employee to the database
                    add_employee(e_id, employeeName, telephoneNumber, email, conn);
                } else if (input.equals("4")) {
                    String e_id;
                    String p_id;
                    String c_id;
                    int pur_qty;
                    double pur_unit_price;
                    System.out.print("Enter eid : ");
                    e_id = userInput.readLine();
                    System.out.print("Enter pid : ");
                    p_id = userInput.readLine();
                    System.out.print("Enter cid : ");
                    c_id = userInput.readLine();
                    System.out.print("Enter purchase quantity : ");
                    pur_qty = Integer.parseInt(userInput.readLine());
                    System.out.print("Enter unit price : ");
                    pur_unit_price = Double.parseDouble(userInput.readLine());
                    // Call the add_purchase() method to add the purchase details to the database
                    add_purchase(e_id, p_id, c_id, pur_qty, pur_unit_price, conn);
                }

                // exit terminates the program
                else if (input.equals("5")) {
                    System.exit(0);
                } else {
                    System.out.println("Invalid option selection, please choose any of the above options from 1 to 9");
                }

            }

            // closes the database connection
            conn.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** other Exception caught ***\n");
        }
    }

    public static void getMonthlySaleActivities(String employeeId, Connection conn) {

        try {
            // Prepare the statement to call the stored procedure
            CallableStatement cs = conn.prepareCall("{ call rbms_package.monthly_sale_activities(?, ?) }");

            // Set the input parameter
            cs.setString(1, employeeId);

            // Register the output parameter
            cs.registerOutParameter(2, OracleTypes.CURSOR);

            // Execute the stored procedure
            cs.execute();

            // Retrieve the result set from the output parameter
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getCursor(2);

            // Print the column headers
            System.out.println("EID\tNAME\tMONTH\tYEAR\tSALES MADE\tQUANTITY\tSOLD");
            System.out.println("---\t----\t-----\t----\t----------\t--------\t----");

            // Print the results
            while (rs.next()) {
                // Print the employee ID, name, month, year, sales made, quantity sold and total
                // amount
                System.out.println(rs.getString("EID") + "\t" + rs.getString("EMPLOYEE_NAME") + "\t"
                        + rs.getString("MONTH") + "\t" + rs.getString("YEAR") + "\t" + rs.getInt("TOTAL_SALES") + "\t\t"
                        + rs.getInt("TOTAL_QUANTITY") + "\t\t" + rs.getDouble("TOTAL_AMOUNT"));
            }

            // Close the result set and statement
            rs.close();
            cs.close();

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 20001) {
                // Handle the exception if the employee ID does not exist
                System.out.println("\nEmployee ID does not exist.\n");
            } else {
                // Handle other SQL exceptions
                System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
            }
        } catch (Exception e) {
            // Handle other exceptions
            System.out.println("\n*** other Exception caught ***\n");
        }
    }

    public static void add_employee(String e_id, String employeeName, String telephoneNumber, String email,
            Connection conn) {

        try {
            // Prepare the statement to call the stored procedure
            CallableStatement cs = conn.prepareCall("{call rbms_package.add_employee(?, ?, ?, ?, ?)}");

            // Set the input parameters
            cs.setString(1, e_id);
            cs.setString(2, employeeName);
            cs.setString(3, telephoneNumber);
            cs.setString(4, email);
            // Register the output parameter
            cs.registerOutParameter(5, OracleTypes.CURSOR);

            // Execute the stored procedure
            cs.execute();

            // Retrieve the result set from the output parameter
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getObject(5);

            System.out.println("\nBelow Employee details are added to the table\n");

            // Print the header of the table
            System.out.printf("%-4s%-15s%-14s%s%n", "EID", "NAME", "TELEPHONE#", "EMAIL");
            System.out.println("--- -------------- ------------ ---------------------");

            // print the results
            while (rs.next()) {
                System.out.printf("%-4s%-15s%-14s%s%n", rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4));
            }

            // Close the result set and statement
            rs.close();
            cs.close();

        } catch (SQLException ex) {
            if (ex.getErrorCode() == 20002) {
                System.out.println("\nEmployee ID already exists.\n");
            } else {
                System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println("\n*** other Exception caught ***\n");
        }

    }

    public static void add_purchase(String e_id, String p_id, String c_id, int pur_qty, double pur_unit_price,
            Connection conn) {

        try {
            // call to stored procedure
            CallableStatement cs = conn.prepareCall("{call rbms_package.add_purchase(?, ?, ?, ?, ?, ?)}");

            // Set input parameters for the add_purchase procedure
            cs.setString(1, e_id);
            cs.setString(2, p_id);
            cs.setString(3, c_id);
            cs.setInt(4, pur_qty);
            cs.setDouble(5, pur_unit_price);
            // Register output parameter for the cursor
            cs.registerOutParameter(6, OracleTypes.CURSOR);

            // execute the stored procedure
            cs.execute();

            // get the result set from the out parameter
            try {
                ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getObject(6);

                System.out.println("\nBelow Purchase details are added to the table\n");

                // print the table header
                System.out.printf("%-11s%-4s%-5s%-5s%-12s%-10s%-12s%-12s%-12s%n", "PUR#", "EID", "PID", "CID",
                        "PUR_TIME", "QUANTITY", "UNIT_PRICE", "PAYMENT", "SAVING");
                System.out.println("---------- --- ---- ---- ---------- ---------- ---------- ---------- ----------");

                // iterate over the result set and print the purchase details
                while (rs.next()) {
                    System.out.printf("%-10s %-3s %-4s %-4s %tF %10d %12.2f %10.2f %10.2f%n", rs.getInt("pur#"),
                            rs.getString("eid"), rs.getString("pid"), rs.getString("cid"), rs.getDate("pur_time"),
                            rs.getInt("quantity"), rs.getDouble("unit_price"), rs.getDouble("payment"),
                            rs.getDouble("saving"));
                }
                System.out.println();
                rs.close();
            } catch (Exception e) {
                System.out.println("\n*** expected Exception caught ***\n");
            }

            // create a new CallableStatement to retrieve DBMS_OUTPUT
            CallableStatement cs1 = conn.prepareCall("{CALL DBMS_OUTPUT.GET_LINE(?, ?)}");
            cs1.registerOutParameter(1, java.sql.Types.VARCHAR);
            cs1.registerOutParameter(2, java.sql.Types.NUMERIC);

            int status = 0;
            // iterate until there is no more output to read
            while (status == 0) {
                cs1.execute();
                String line = cs1.getString(1);
                if (line != null) {
                    System.out.println(line);
                }
                status = cs1.getInt(2);
            }

            // close the statement
            cs.close();
            cs1.close();

        } catch (SQLException ex) {
            // handle specific error codes
            if (ex.getErrorCode() == 20003) {
                System.out.println("\nEmployee ID does not exist.\n");
            } else if (ex.getErrorCode() == 20004) {
                System.out.println("\nProduct ID does not exist.\n");
            } else if (ex.getErrorCode() == 20005) {
                System.out.println("\nCustomer ID does not exist.\n");
            } else if (ex.getErrorCode() == 20006) {
                System.out.println("\nInsufficient quantity in stock.\n");
            } else if (ex.getErrorCode() == 20007) {
                System.out.println("\nUnit price cannot be greater than original price.\n");
            } else {
                System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
            }
        } catch (Exception e) {
            System.out.println("\n*** other Exception caught ***\n");
        }

    }

    public static void show_employees(Connection conn) {
        try {
            // Call the show_employees stored procedure
            CallableStatement cs = conn.prepareCall("{call rbms_package.show_employees(?)}");
            // Register output parameter for the cursor
            cs.registerOutParameter(1, OracleTypes.CURSOR);
            // execute the stored procedure
            cs.execute();
            // Retrieve the result set from the output parameter
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getCursor(1);

            // Display the column headers
            System.out.printf("%-4s%-15s%-14s%s%n", "EID", "NAME", "TELEPHONE#", "EMAIL");
            System.out.println("--- -------------- ------------ ---------------------");

            // Display the employee information
            while (rs.next()) {
                System.out.printf("%-4s%-15s%-14s%s%n", rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4));
            }
            // close the result set, statement
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** other Exception caught ***\n");
        }
    }

    public static void show_customers(Connection conn) {
        try {

            // call to stored procedure to retrieve customer data
            CallableStatement cs = conn.prepareCall("{call rbms_package.show_customers(?)}");
            cs.registerOutParameter(1, OracleTypes.CURSOR);

            // execute the stored procedure and retrieve the result set
            cs.execute();
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getCursor(1);

            // print table headers
            System.out.printf("%-4s %-10s %-10s %-12s %-12s %s %n", "CID", "FIRST_NAME", "LAST_NAME", "PHONE#",
                    "VISITS_MADE", "LAST_VISIT");
            System.out.println("---- ---------- ---------- ------------ ----------- ----------");

            // iterate through result set and print the data
            while (rs.next()) {

                System.out.printf("%-4s %-10s %-10s %-12s %11d %s%n", rs.getString(1), rs.getString(2), rs.getString(3),
                        rs.getString(4), rs.getInt(5), rs.getDate(6));
            }

            // close the result set, statement
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** other Exception caught ***\n");
        }
    }

    public static void show_products(Connection conn) {

        try {

            // call to stored procedure
            CallableStatement cs = conn.prepareCall("{call rbms_package.show_products(?)}");
            cs.registerOutParameter(1, OracleTypes.CURSOR);

            // execute and retrieve the result set
            cs.execute();
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getCursor(1);

            // print the table header
            System.out.printf("%-4s %-20s %10s %14s %10s %16s\n", "PID", "NAME", "QOH", "QOH_THRESHOLD", "ORIG_PRICE",
                    "DISCNT_CATEGORY");
            System.out.println("---- -------------------- ------------ ------------- ---------- ---------------");

            // print the results
            while (rs.next()) {

                System.out.printf("%-4s %-20s %12d %13d %11.2f %14d\n", rs.getString(1), rs.getString(2), rs.getInt(3),
                        rs.getInt(4), rs.getDouble(5), rs.getInt(6));
            }

            // close the result set, statement
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            System.out.println("\n*** other Exception caught ***\n");
        }

    }

    public static void show_prod_discnt(Connection conn) {

        try {

            // Prepare a CallableStatement to execute the show_prod_discnt stored procedure.
            CallableStatement cs = conn.prepareCall("{call rbms_package.show_prod_discnt(?)}");
            // Register an output parameter to retrieve the returned cursor.
            cs.registerOutParameter(1, OracleTypes.CURSOR);

            // Execute the stored procedure.
            cs.execute();

            // Retrieve the returned cursor as a ResultSet.
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getCursor(1);

            // print the table header
            System.out.println("DISCNT_CATEGORY DISCNT_RATE");
            System.out.println("--------------- -----------");

            // print the results
            while (rs.next()) {
                // print each row of the table
                System.out.printf("%15s %10.2f%n", rs.getInt(1), rs.getDouble(2));
            }

            // Close the ResultSet and CallableStatement.
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            // Print SQLException details if caught.
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            // Print other Exception details if caught.
            System.out.println("\n*** other Exception caught ***\n");
        }

    }

    // This method is used to retrieve and print the purchase records stored in a
    // database.
    public static void show_purchases(Connection conn) {

        try {

            // Prepare a CallableStatement to execute the show_purchases stored procedure.
            CallableStatement cs = conn.prepareCall("{call rbms_package.show_purchases(?)}");

            // Register an output parameter to retrieve the returned cursor.
            cs.registerOutParameter(1, OracleTypes.CURSOR);

            // Execute the stored procedure.
            cs.execute();

            // Retrieve the returned cursor as a ResultSet.
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getCursor(1);

            // Print the table header.
            System.out.printf("%-11s%-4s%-5s%-5s%-12s%-10s%-12s%-12s%-12s%n", "PUR#", "EID", "PID", "CID", "PUR_TIME",
                    "QUANTITY", "UNIT_PRICE", "PAYMENT", "SAVING");
            System.out.println("---------- --- ---- ---- ---------- ---------- ---------- ---------- ----------");

            // Loop through each row in the ResultSet and print the corresponding values.
            while (rs.next()) {
                System.out.printf("%-10s %-3s %-4s %-4s %tF %10d %12.2f %10.2f %10.2f%n", rs.getInt(1), rs.getString(2),
                        rs.getString(3), rs.getString(4), rs.getDate(5), rs.getInt(6), rs.getDouble(7), rs.getDouble(8),
                        rs.getDouble(9));
            }

            // Close the ResultSet and CallableStatement.
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            // Print SQLException details if caught.
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            // Print other Exception details if caught.
            System.out.println("\n*** other Exception caught ***\n");
        }

    }

    // This method is used to retrieve and print the log records stored in a
    // database.
    public static void show_logs(Connection conn) {
        try {
            // Prepare a CallableStatement to execute the show_logs stored procedure.
            CallableStatement cs = conn.prepareCall("{call rbms_package.show_logs(?)}");

            // Register an output parameter to retrieve the returned cursor.
            cs.registerOutParameter(1, OracleTypes.CURSOR);

            // Execute the stored procedure.
            cs.execute();

            // Retrieve the returned cursor as a ResultSet.
            ResultSet rs = (ResultSet) ((OracleCallableStatement) cs).getCursor(1);

            // check if the result set is empty
            if (!rs.next()) {
                System.out.println("Table is empty.");
                return;
            }

            // Print the table header.
            System.out.println("ID\tUSERNAME\tOPERATION\tTIMESTAMP\t\tRESULT\t\tDESCRIPTION");
            System.out.println("--\t--------\t---------\t---------\t\t------\t\t-----------");

            // Loop through each row in the ResultSet and print the corresponding values.
            do {
                System.out.println(rs.getInt(1) + "\t" + rs.getString(2) + "\t" + rs.getString(3) + "\t\t"
                        + rs.getTimestamp(4) + "\t" + rs.getString(5) + "\t" + rs.getString(6));
            } while (rs.next());

            // Close the ResultSet and CallableStatement.
            rs.close();
            cs.close();
        } catch (SQLException ex) {
            // Print SQLException details if caught.
            System.out.println("\n*** SQLException caught ***\n" + ex.getMessage());
        } catch (Exception e) {
            // Print other Exception details if caught.
            System.out.println("\n*** other Exception caught ***\n" + e.toString());
        }
    }

}