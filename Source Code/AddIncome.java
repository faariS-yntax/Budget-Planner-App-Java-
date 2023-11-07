import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;


public class AddIncome extends JFrame implements ActionListener {

    private HeaderPanel headerPanel;
    private FooterPanel footerPanel;
    private JLabel amountLabel;
    private JTextField amountField;
    private JLabel typeLabel;
    private JComboBox<String> typeBox;
    private JLabel noteLabel;
    private JTextField noteField;
    private JLabel dateLabel;
    private JFormattedTextField dateField;
    private JLabel pictureLabel;
    private JButton pictureButton;
    private JButton addButton;
    private JButton cancelButton;
    private JLabel imagePreviewLabel;

    private byte[] selectedImageBytes;

    public AddIncome() {
        // -----------------------------------Frame Template-----------------------------------//
        // Set up the frame
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
        headerPanel = new HeaderPanel("Add Income"); // Change the frame header depending on the content
        this.add(headerPanel, BorderLayout.NORTH);

        // Create and add the footer panel
        footerPanel = new FooterPanel();
        this.add(footerPanel, BorderLayout.SOUTH);

        // -----------------------------------Content Template-----------------------------------//
        // Create and add the components to the content pane
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(new Color(255, 209, 220));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.anchor = GridBagConstraints.WEST;
        gbc.insets = new Insets(5, 5, 5, 5);

        amountLabel = new JLabel("Amount (RM): ");
        amountField = new JTextField(10);
        typeLabel = new JLabel("Type: ");
        String[] types = {"Scholarship", "Allowance", "Part-time job", "Others"};
        typeBox = new JComboBox<>(types);
        noteLabel = new JLabel("Note: ");
        noteField = new JTextField(10);
        dateLabel = new JLabel("Date (dd/mm/yyyy): ");
        DateFormat format = new SimpleDateFormat("dd/MM/yyyy");
        dateField = new JFormattedTextField(format);
        // Set the preferred size for the date input field
        dateField.setPreferredSize(new Dimension(120, dateField.getPreferredSize().height));
        pictureLabel = new JLabel("Picture: ");
        pictureButton = new JButton("Upload Picture");
        cancelButton = new JButton("Back");
        addButton = new JButton("Add");
        imagePreviewLabel = new JLabel();

        // Add action listener to the buttons
        cancelButton.addActionListener(this);
        addButton.addActionListener(this);
        pictureButton.addActionListener(this);

        // Add components to the frame
        // First Row: Amount Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 0;
        contentPanel.add(amountLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally
        contentPanel.add(amountField, gbc);

        // Second Row: Type Label and Type Box
        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1; // Reset to default
        gbc.fill = GridBagConstraints.NONE; // Reset to default
        contentPanel.add(typeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally
        contentPanel.add(typeBox, gbc);

        // Third Row: Note Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1; // Reset to default
        gbc.fill = GridBagConstraints.NONE; // Reset to default
        contentPanel.add(noteLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally
        contentPanel.add(noteField, gbc);

        // Fourth Row: Date Label and Text Field
        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 1; // Reset to default
        gbc.fill = GridBagConstraints.NONE; // Reset to default
        contentPanel.add(dateLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 3;
        gbc.gridwidth = 3; // Span across 3 columns
        gbc.fill = GridBagConstraints.HORIZONTAL; // Stretch horizontally
        contentPanel.add(dateField, gbc);

        // Fifth Row: Picture Label, Picture Button, and Preview
        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 1; // Reset to default
        gbc.fill = GridBagConstraints.NONE; // Reset to default
        contentPanel.add(pictureLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 4;
        contentPanel.add(pictureButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 4;
        contentPanel.add(imagePreviewLabel, gbc);

        // Last Row: Cancel and Add Button
        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 2; // Span across 2 columns
        gbc.fill = GridBagConstraints.NONE; // Reset to default
        contentPanel.add(cancelButton, gbc);

        gbc.gridx = 2;
        gbc.gridy = 5;
        contentPanel.add(addButton, gbc);

        // Add content into the frame
        this.add(contentPanel, BorderLayout.CENTER);

        // Set the frame visible
        this.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == addButton) {
            // Get the input values
            String transactionType = "income";
            String amountText = amountField.getText();
            String type = (String) typeBox.getSelectedItem();
            String note = noteField.getText();
            String date = dateField.getText();

            // Format the amount text
            NumberFormat format = new DecimalFormat("#0.00");
            try {
                double amount = format.parse(amountText).doubleValue();
                amountText = format.format(amount);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }

            // Create a transaction object
            Transaction transaction = new Transaction(Transaction.getLastId(transactionType) + 1, amountText, type, note, date, selectedImageBytes);

            // Write the transaction to the file
            try {
                File file = new File("income.tsv");
                if (!file.exists()) {
                    file.createNewFile();
                }
                FileWriter fw = new FileWriter(file, true);
                BufferedWriter bw = new BufferedWriter(fw);
                bw.write(transaction.toTSVString());
                bw.newLine();
                bw.close();
                fw.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            // Clear the input fields
            amountField.setText("");
            noteField.setText("");
            dateField.setText("");

            JOptionPane.showMessageDialog(this, "Transaction added successfully!");

            // Read transactions from file and store them in an ArrayList
            ArrayList<Transaction> transactions = readTransactionsFromFile("income.tsv");

            System.out.println(transactions); // Print the transactions for testing purposes
        } else if (e.getSource() == pictureButton) {
            JFileChooser fileChooser = new JFileChooser();
            fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));
            int option = fileChooser.showOpenDialog(this);
            if (option == JFileChooser.APPROVE_OPTION) {
                try {
                    File selectedFile = fileChooser.getSelectedFile();
                    selectedImageBytes = Files.readAllBytes(selectedFile.toPath());
                    System.out.println("Image selected: " + selectedFile.getName());
                    // Display the image preview
                    ImageIcon imageIcon = new ImageIcon(selectedImageBytes);
                    Image image = imageIcon.getImage().getScaledInstance(-1, 200, Image.SCALE_SMOOTH);
                    imagePreviewLabel.setIcon(new ImageIcon(image));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        } else if (e.getSource() == cancelButton) {
            this.dispose(); // Close the panel
        }
    }

    private ArrayList<Transaction> readTransactionsFromFile(String filename) {
        ArrayList<Transaction> transactions = new ArrayList<>();
        try {
            BufferedReader br = Files.newBufferedReader(Paths.get(filename));
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split("\t");
                byte[] imageBytes = Base64.getDecoder().decode(values[5]);
                System.out.println(imageBytes);
                int id = Integer.parseInt(values[0]);
                Transaction t = new Transaction(id, values[1], values[2], values[3], values[4], imageBytes);
                transactions.add(t);
            }
            br.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return transactions;
    }

    public static void main(String[] args) {
        new AddIncome();
    }
}
