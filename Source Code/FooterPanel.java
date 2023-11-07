import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

public class FooterPanel extends JPanel implements ActionListener {
    private JButton button1;
    private JButton button2;
    private JButton button3;
    private JButton button4;

    public FooterPanel() {
        this.setLayout(new FlowLayout());
        this.setBackground(new Color(252, 149, 173));

        try {
            ImageIcon homeIcon = createScaledIcon("home.png", 25, 25);
            ImageIcon transactionIcon = createScaledIcon("transaction.png", 25, 25);
            ImageIcon summaryIcon = createScaledIcon("History.png", 25, 25);
            ImageIcon profileIcon = createScaledIcon("Summary.png", 25, 25);

            button1 = new JButton(homeIcon);
            button2 = new JButton(transactionIcon);
            button3 = new JButton(summaryIcon);
            button4 = new JButton(profileIcon);

            // Set button size
            button1.setPreferredSize(new Dimension(30, 30));
            button2.setPreferredSize(new Dimension(30, 30));
            button3.setPreferredSize(new Dimension(30, 30));
            button4.setPreferredSize(new Dimension(30, 30));

            // Add action listeners to the buttons
            button1.addActionListener(this);
            button2.addActionListener(this);
            button3.addActionListener(this);
            button4.addActionListener(this);

            this.add(button1);
            this.add(button2);
            this.add(button3);
            this.add(button4);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private ImageIcon createScaledIcon(String iconName, int width, int height) {
        try {
            ImageIcon icon = new ImageIcon(ImageIO.read(getClass().getResource(iconName)));
            Image scaledIcon = icon.getImage().getScaledInstance(width, height, Image.SCALE_SMOOTH);
            return new ImageIcon(scaledIcon);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

@Override
public void actionPerformed(ActionEvent e) {
    if (e.getSource() == button1) {
    // Navigate to the HomePage
        try {
            HomePage homePage = new HomePage();
            homePage.setVisible(true);
        } catch (IOException ex) {
            ex.printStackTrace(); // or handle the exception as desired
        }

    } else if (e.getSource() == button2) {
                    // Create a submenu for "Transaction" button
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem addIncomeItem = new JMenuItem("Add Income");
            addIncomeItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddIncome addIncome = new AddIncome();
                    addIncome.setVisible(true);
                }
            });
            popupMenu.add(addIncomeItem);

            JMenuItem addExpenseItem = new JMenuItem("Add Expense");
            addExpenseItem.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    AddExpense addExpense = new AddExpense();
                    addExpense.setVisible(true);
                }
            });
            popupMenu.add(addExpenseItem);

            // Show the submenu
            popupMenu.show(button2, 0, button2.getHeight());
    } else if (e.getSource() == button3) {
            // Create a submenu for "history" button
            JPopupMenu popupMenu = new JPopupMenu();

            JMenuItem incomeHistory = new JMenuItem("Income History");
            incomeHistory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    IncomeHistory incomeHistory1 = new IncomeHistory();
                    incomeHistory1.setVisible(true);
                }
            });
            popupMenu.add(incomeHistory);

            JMenuItem expenseHistory = new JMenuItem("Expense History");
            expenseHistory.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    ExpenseHistory expenseHistory1 = new ExpenseHistory();
                    expenseHistory1.setVisible(true);
                }
            });
            popupMenu.add(expenseHistory);

            // Show the submenu
            popupMenu.show(button2, 0, button2.getHeight());
    } else if (e.getSource() == button4) {
            // Navigate to the sumarry
            Summary summary = new Summary();
            summary.setVisible(true);

    }
}}
