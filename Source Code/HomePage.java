import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import javax.swing.*;

public class HomePage extends JFrame implements ActionListener {

    JButton button1, button2, button3, button4;
    private HeaderPanel headerPanel;
    private FooterPanel footerPanel;
    private JLabel pictureLabel;

    public HomePage() throws IOException {
        //-----------------------------------Frame Template-----------------------------------//
        int frameWidth = 440;
        int frameHeight = 860;
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
        headerPanel = new HeaderPanel("Home"); // Change the frame header depend on the content
        this.add(headerPanel, BorderLayout.NORTH);

        // Create and add the footer panel
        footerPanel = new FooterPanel();
        this.add(footerPanel, BorderLayout.SOUTH);

        //-----------------------------------Content Template-----------------------------------//
        // Create a panel for the buttons
        JPanel buttonPanel = new JPanel(new GridBagLayout());
        buttonPanel.setBackground(new Color(255, 209, 220));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // Add padding

        ImageIcon originalIcon = new ImageIcon("budgetLogo.png");
        Image originalImage = originalIcon.getImage();
        Image resizedImage = originalImage.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        ImageIcon resizedIcon = new ImageIcon(resizedImage);

        pictureLabel = new JLabel("Budget Planner App", resizedIcon, JLabel.CENTER);

        // Set up GridBagConstraints for pictureLabel
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.fill = GridBagConstraints.BOTH;
        buttonPanel.add(pictureLabel, gbc);
        
        // Reset gridwidth to default
        gbc.gridwidth = 1;

        // Create buttons for the menu options
        button1 = new JButton("Add Transaction");
        button2 = new JButton("Transaction History");
        button3 = new JButton("Summary");
        button4 = new JButton("Visualization");

        // Set up GridBagConstraints for buttons
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.ipady = 80;
        gbc.weightx = 0.5;
        buttonPanel.add(button1, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.ipady = 80;
        gbc.weightx = 0.5;
        buttonPanel.add(button2, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.ipady = 80;
        gbc.weightx = 0.5;
        buttonPanel.add(button3, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.ipady = 80;
        gbc.weightx = 0.5;
        buttonPanel.add(button4, gbc);

        // Add the ActionListener to the buttons
        button1.addActionListener(this);
        button2.addActionListener(this);
        button3.addActionListener(this);
        button4.addActionListener(this);

        // Add content into the frame
        add(buttonPanel, BorderLayout.CENTER);
        setVisible(true);
    }

    @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource() == button1) {
                // Option 1 clicked
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem addIncomeItem = new JMenuItem("Add Income");
                JMenuItem addExpenseItem = new JMenuItem("Add Expense");

                // ActionListener for "Add Income" option
                addIncomeItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AddIncome addIncome = new AddIncome();
                        addIncome.setVisible(true);
                    }
                });

                // ActionListener for "Add Expense" option
                addExpenseItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        AddExpense addExpense = new AddExpense();
                        addExpense.setVisible(true);
                    }
                });

                popupMenu.add(addIncomeItem);
                popupMenu.add(addExpenseItem);

                popupMenu.show(button1, 0, button1.getHeight());
            } else if (e.getSource() == button2) {
                // Option 2 clicked
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem incomeHistoryItem = new JMenuItem("Income History");
                JMenuItem expenseHistoryItem = new JMenuItem("Expense History");

                // ActionListener for "Income History" option
                incomeHistoryItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        IncomeHistory incomeHistory = new IncomeHistory();
                        incomeHistory.setVisible(true);
                    }
                });

                // ActionListener for "Expense History" option
                expenseHistoryItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ExpenseHistory expenseHistory = new ExpenseHistory();
                        expenseHistory.setVisible(true);
                    }
                });

                popupMenu.add(incomeHistoryItem);
                popupMenu.add(expenseHistoryItem);

                popupMenu.show(button2, 0, button2.getHeight());
            } else if (e.getSource() == button3) {
                // Option 3 clicked
                Summary summary = new Summary();
                summary.setVisible(true);
            } else if (e.getSource() == button4) {
                // Option 4 clicked
                JPopupMenu popupMenu = new JPopupMenu();
                JMenuItem incomeGraphItem = new JMenuItem("Income Graph");
                JMenuItem expenseGraphItem = new JMenuItem("Expense Graph");

                // ActionListener for "Income History" option
                incomeGraphItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        IncomeGraph incomeGraph = new IncomeGraph();
                        incomeGraph.setVisible(true);
                    }
                });

                // ActionListener for "Expense History" option
                expenseGraphItem.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        ExpenseGraph expenseGraph = new ExpenseGraph();
                        expenseGraph.setVisible(true);
                    }
                });

                popupMenu.add(incomeGraphItem);
                popupMenu.add(expenseGraphItem);

                popupMenu.show(button2, 0, button2.getHeight());
            }
        }

    public static void main(String[] args) throws IOException {
        new HomePage();
    }
}