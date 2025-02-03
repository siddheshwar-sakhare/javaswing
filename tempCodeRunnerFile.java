//  Class.forName("oracle.jdbc.driver.OracleDriver");
    
        //     // Establish a connection to the Oracle database
        //     Connection conn = DriverManager.getConnection(
        //             "jdbc:oracle:thin:@//localhost:1521/orclpdb",  // Replace with your Oracle connection details
        //             "your_oracle_username",  // Replace with your Oracle username
        //             "your_oracle_password"); // Replace with your Oracle password
    
        //     // Prepare the SQL insert statement
        //     PreparedStatement pstm = conn.prepareStatement(
        //             "INSERT INTO orders (customer_name, size, pizza_count, toppings, total_price) VALUES (?, ?, ?, ?, ?)");
    
        //     String size = small.isSelected() ? "Small" : medium.isSelected() ? "Medium" : "Large";
        //     String toppings = "";
        //     if (garlicButter.isSelected()) toppings += "Garlic Butter ";
        //     if (mushroom.isSelected()) toppings += "Mushroom ";
        //     if (extraCheese.isSelected()) toppings += "Extra Cheese ";
        //     if (pepperoni.isSelected()) toppings += "Pepperoni ";
        //     if (greenPepper.isSelected()) toppings += "Green Pepper ";
    
        //     double totalPrice = pizzaCount * (price + toppingsPrice);
    
        //     // Set the values for the SQL insert
        //     pstm.setString(1, customerName);
        //     pstm.setString(2, size);
        //     pstm.setInt(3, pizzaCount);
        //     pstm.setString(4, toppings);
        //     pstm.setDouble(5, totalPrice);
    
        //     // Execute the insert query
        //     int rowsInserted = pstm.executeUpdate();
        //     if (rowsInserted > 0) {
        //         JOptionPane.showMessageDialog(this, "Order submitted successfully!");
        //         resetOrder();
        //     }
    
        //     // Close the connection
        //     pstm.close();
        //     conn.close();
        // } catch (SQLException | ClassNotFoundException e) {
        //     e.printStackTrace();
        //     JOptionPane.showMessageDialog(this, "Error submitting order. Please try again.");
        // }