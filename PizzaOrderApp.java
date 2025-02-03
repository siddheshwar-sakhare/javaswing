import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

class Sample extends JFrame {
    private JTextField t1;

    public Sample() {
        setTitle("Pizza Palace");
        setSize(500, 500);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        Font f1 = new Font("Times New Roman", Font.BOLD, 25);
        Font f2 = new Font("Algerian", Font.ITALIC, 35);
        JLabel title = new JLabel("Pizza Palace");
        title.setFont(f2);
        add(title);
        t1 = new JTextField(20);
        t1.setPreferredSize(new Dimension(500, 30));
        t1.setFont(f1);
        add(t1);
        JButton orderBtn = new JButton("Order Pizza");
        orderBtn.setFont(f1);
        add(orderBtn);
        JButton adminBtn = new JButton("Admin Login");
        adminBtn.setFont(f1);
        add(adminBtn);

        orderBtn.addActionListener(e -> {
            String name = t1.getText();
            if (name.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please enter your name.");
            } else {
                PizzaPage pizzaPage = new PizzaPage(name);
                setVisible(false);
                pizzaPage.setVisible(true);
            }
        });

        adminBtn.addActionListener(e -> {
            AdminLogin adminLogin = new AdminLogin();
            adminLogin.setVisible(true);
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new Sample();
    }
}

class PizzaPage extends JFrame {
    private String customerName;
    private JTextField pizzaCountField;
    private JTextArea orderSummary;
    private JRadioButton small, medium, large;
    private JCheckBox garlicButter, mushroom, extraCheese, pepperoni, greenPepper;
    private JButton calcPriceBtn, newOrderBtn, submitOrderBtn;
    private double price, toppingsPrice;
    private int pizzaCount;
    private ButtonGroup sizeGroup;

    public PizzaPage(String customerName) {
        this.customerName = customerName;
        setTitle("Pizza Order Page");
        setSize(900, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        Font customFont = new Font("Times New Roman", Font.BOLD, 24);
        JLabel sizeLabel = new JLabel("Select Pizza Size:");
        sizeLabel.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 0;
        add(sizeLabel, gbc);
        small = new JRadioButton("Small (Rs. 200)");
        medium = new JRadioButton("Medium (Rs. 350)");
        large = new JRadioButton("Large (Rs. 500)");
        sizeGroup = new ButtonGroup();
        sizeGroup.add(small);
        sizeGroup.add(medium);
        sizeGroup.add(large);
        JPanel sizePanel = new JPanel(new GridLayout(1, 3, 10, 0));
        sizePanel.add(small);
        sizePanel.add(medium);
        sizePanel.add(large);
        gbc.gridx = 0;
        gbc.gridy = 1;
        add(sizePanel, gbc);
        JLabel toppingsLabel = new JLabel("Select Toppings (Rs. 30 each):");
        toppingsLabel.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 2;
        add(toppingsLabel, gbc);
        JPanel toppingsPanel = new JPanel(new GridLayout(2, 3, 10, 0));
        garlicButter = new JCheckBox("Garlic Butter");
        mushroom = new JCheckBox("Mushroom");
        extraCheese = new JCheckBox("Extra Cheese");
        pepperoni = new JCheckBox("Pepperoni");
        greenPepper = new JCheckBox("Green Pepper");
        toppingsPanel.add(garlicButter);
        toppingsPanel.add(mushroom);
        toppingsPanel.add(extraCheese);
        toppingsPanel.add(pepperoni);
        toppingsPanel.add(greenPepper);
        gbc.gridx = 0;
        gbc.gridy = 3;
        add(toppingsPanel, gbc);
        JLabel countLabel = new JLabel("Enter Pizza Count:");
        countLabel.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 4;
        add(countLabel, gbc);
        pizzaCountField = new JTextField(5);
        pizzaCountField.setFont(customFont);
        gbc.gridx = 1;
        gbc.gridy = 4;
        add(pizzaCountField, gbc);
        JLabel summaryLabel = new JLabel("Order Summary:");
        summaryLabel.setFont(customFont);
        gbc.gridx = 0;
        gbc.gridy = 5;
        add(summaryLabel, gbc);
        orderSummary = new JTextArea(10, 40);
        orderSummary.setEditable(false);
        orderSummary.setFont(new Font("Monospaced", Font.PLAIN, 16));
        JScrollPane scrollPane = new JScrollPane(orderSummary);
        gbc.gridx = 0;
        gbc.gridy = 6;
        gbc.gridwidth = 2;
        add(scrollPane, gbc);
        JPanel buttonPanel = new JPanel(new GridLayout(1, 3, 10, 0));
        calcPriceBtn = new JButton("Calculate Price");
        newOrderBtn = new JButton("New Order");
        submitOrderBtn = new JButton("Submit Order");
        calcPriceBtn.setFont(customFont);
        newOrderBtn.setFont(customFont);
        submitOrderBtn.setFont(customFont);
        buttonPanel.add(calcPriceBtn);
        buttonPanel.add(newOrderBtn);
        buttonPanel.add(submitOrderBtn);
        gbc.gridx = 0;
        gbc.gridy = 7;
        gbc.gridwidth = 2;
        add(buttonPanel, gbc);

        calcPriceBtn.addActionListener(e -> calculatePrice());
        submitOrderBtn.addActionListener(e -> submitOrderToDB());
        newOrderBtn.addActionListener(e -> resetOrder());

        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
    }

    private void calculatePrice() {
        price = 0;
        toppingsPrice = 0;
        try {
            pizzaCount = Integer.parseInt(pizzaCountField.getText());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Please enter a valid pizza count.");
            return;
        }
        if (small.isSelected()) {
            price = 200;
        } else if (medium.isSelected()) {
            price = 350;
        } else if (large.isSelected()) {
            price = 500;
        } else {
            JOptionPane.showMessageDialog(this, "Please select a pizza size.");
            return;
        }
        if (garlicButter.isSelected()) toppingsPrice += 30;
        if (mushroom.isSelected()) toppingsPrice += 30;
        if (extraCheese.isSelected()) toppingsPrice += 30;
        if (pepperoni.isSelected()) toppingsPrice += 30;
        if (greenPepper.isSelected()) toppingsPrice += 30;
        double totalPrice = pizzaCount * (price + toppingsPrice);
        orderSummary.setText("Customer Name: " + customerName + "\n");
        orderSummary.append("Pizza Size: " + (small.isSelected() ? "Small" : medium.isSelected() ? "Medium" : "Large") + "\n");
        orderSummary.append("Toppings Price: Rs." + toppingsPrice + "\n");
        orderSummary.append("Pizza Count: " + pizzaCount + "\n");
        orderSummary.append("Total Price: Rs." + totalPrice);
    }

    private void submitOrderToDB() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/pizza_order", "root", "");
             PreparedStatement pstm = conn.prepareStatement("INSERT INTO orders (customer_name, size, pizza_count, toppings, total_price) VALUES (?, ?, ?, ?, ?)")) {
            String size = small.isSelected() ? "Small" : medium.isSelected() ? "Medium" : "Large";
            String toppings = "";
            if (garlicButter.isSelected()) toppings += "Garlic Butter ";
            if (mushroom.isSelected()) toppings += "Mushroom ";
            if (extraCheese.isSelected()) toppings += "Extra Cheese ";
            if (pepperoni.isSelected()) toppings += "Pepperoni ";
            if (greenPepper.isSelected()) toppings += "Green Pepper ";
            double totalPrice = pizzaCount * (price + toppingsPrice);
            pstm.setString(1, customerName);
            pstm.setString(2, size);
            pstm.setInt(3, pizzaCount);
            pstm.setString(4, toppings);
            pstm.setDouble(5, totalPrice);
            int rowsInserted = pstm.executeUpdate();
            if (rowsInserted > 0) {
                JOptionPane.showMessageDialog(this, "Order placed successfully!");
                resetOrder();
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error placing order: " + e.getMessage());
        }
    }

    private void resetOrder() {
        pizzaCountField.setText("");
        orderSummary.setText("");
        sizeGroup.clearSelection();
        garlicButter.setSelected(false);
        mushroom.setSelected(false);
        extraCheese.setSelected(false);
        pepperoni.setSelected(false);
        greenPepper.setSelected(false);
    }
}

class AdminLogin extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;

    public AdminLogin() {
        setTitle("Admin Login");
        setSize(400, 300);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new FlowLayout());

        JLabel usernameLabel = new JLabel("Username:");
        usernameField = new JTextField(20);
        JLabel passwordLabel = new JLabel("Password:");
        passwordField = new JPasswordField(20);
        JButton loginButton = new JButton("Login");

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(loginButton);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (usernameField.getText().equals("Siddheshwar") && new String(passwordField.getPassword()).equals("admin123")) {
                    AdminDashboard adminDashboard = new AdminDashboard();
                    setVisible(false);
                    adminDashboard.setVisible(true);
                } else {
                    JOptionPane.showMessageDialog(null, "Invalid credentials. Please try again.");
                }
            }
        });

        setLocationRelativeTo(null);
        setVisible(true);
    }
}

class AdminDashboard extends JFrame {
    private JTable ordersTable;
    private DefaultTableModel tableModel;

    public AdminDashboard() {
        setTitle("Admin Dashboard");
        setSize(800, 600);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        String[] columnNames = {"Order ID", "Customer Name", "Size", "Count", "Toppings", "Total Price"};
        tableModel = new DefaultTableModel(columnNames, 0);
        ordersTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(ordersTable);
        add(scrollPane, BorderLayout.CENTER);
        loadOrders();

        setLocationRelativeTo(null);
        setVisible(true);
    }

    private void loadOrders() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3307/pizza_order", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM orders")) {
            while (rs.next()) {
                int orderId = rs.getInt("id");
                String customerName = rs.getString("customer_name");
                String size = rs.getString("size");
                int count = rs.getInt("pizza_count");
                String toppings = rs.getString("toppings");
                double totalPrice = rs.getDouble("total_price");
                tableModel.addRow(new Object[]{orderId, customerName, size, count, toppings, totalPrice});
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Error loading orders: " + e.getMessage());
        }
    }
}
