import java.awt.*;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Summary extends JFrame {
    private HeaderPanel headerPanel;
    private FooterPanel footerPanel;

    public Summary() {
        //-----------------------------------Frame Template-----------------------------------//
        int frameWidth = 540;
        int frameHeight = 960;
        ImageIcon img2 = new ImageIcon("budgetLogo.png");
        this.setIconImage(img2.getImage());
        this.setTitle("UNI Budget Planner");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setResizable(true);
        this.setSize(frameWidth, frameHeight);

        // Set the frame location to the center of the screen
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;

        int x = (screenWidth - frameWidth) / 2;
        int y = (screenHeight - frameHeight) / 2;

        this.setLocation(x, y);

        // Create and add the header panel
        headerPanel = new HeaderPanel("Summary");
        this.add(headerPanel, BorderLayout.NORTH);

        // Create and add the footer panel
        footerPanel = new FooterPanel();
        this.add(footerPanel, BorderLayout.SOUTH);

        //-----------------------------------Content Template-----------------------------------//
        // Create and add the content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(255, 209, 220));
        contentPanel.setLayout(new GridBagLayout());

        // Read income data from file
        double totalIncome = 0;
        Map<String, Double> incomeMap = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("income.tsv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                double amount = Double.parseDouble(data[1]);
                String type = data[2];

                totalIncome += amount;
                incomeMap.put(type, incomeMap.getOrDefault(type, 0.0) + amount);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Read expense data from file
        double totalExpense = 0;
        Map<String, Double> expenseMap = new HashMap<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader("expense.tsv"));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                double amount = Double.parseDouble(data[1]);
                String type = data[2];

                totalExpense += amount;
                expenseMap.put(type, expenseMap.getOrDefault(type, 0.0) + amount);
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        // Total Expense label
        JLabel totalExpenseLabel = new JLabel("Total Expense: $" + totalExpense);
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(totalExpenseLabel, gbc);

       // Type of Expense labels
        int row = 1;
        int column_expense = 0;
        for (Map.Entry<String, Double> entry : expenseMap.entrySet()) {
            JLabel typeLabel = new JLabel(entry.getKey() + ": $" + entry.getValue());
            gbc.gridx = column_expense;
            gbc.gridy = row;
            gbc.ipady = 40;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            contentPanel.add(typeLabel, gbc);
            
            column_expense++;
            if (column_expense == 3) {
                column_expense = 0;
                row++;
            }
        }

        // Total Income label
        row++;
        JLabel totalIncomeLabel = new JLabel("Total Income: $" + totalIncome);
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(totalIncomeLabel, gbc);

        // Type of Income labels
        row++;
        int column_income = 0;
        for (Map.Entry<String, Double> entry : incomeMap.entrySet()) {
            JLabel typeLabel = new JLabel(entry.getKey() + ": $" + entry.getValue());
            gbc.gridx = column_income;
            gbc.gridy = row;
            gbc.ipady = 40;
            gbc.gridwidth = 1;
            gbc.anchor = GridBagConstraints.CENTER;
            contentPanel.add(typeLabel, gbc);

            column_income++;
            if (column_income == 3) {
                column_income = 0;
                row++;
            }
        }

        // Back button
        row++;
        JButton backButton = new JButton("Back");
        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose(); // Dispose the current frame
            }
        });

        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.ipady = 20;
        gbc.ipadx = 40;
        gbc.gridwidth = 3;
        gbc.anchor = GridBagConstraints.CENTER;
        contentPanel.add(backButton, gbc);

        add(contentPanel, BorderLayout.CENTER);
        // Set frame to visible
        setVisible(true);
    }

    public static void main(String[] args) {
        new Summary();
    }
}
