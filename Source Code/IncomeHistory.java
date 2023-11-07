import java.awt.*;
import java.util.Base64;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class IncomeHistory extends JFrame implements ActionListener {
    private HeaderPanel headerPanel;
    private FooterPanel footerPanel;
    private JTable dataTable;
    private JButton backButton;
    private JButton deleteButton;
    private JButton editButton;

    public IncomeHistory() {
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
        headerPanel = new HeaderPanel("Income History"); // Change the frame header depending on the content
        this.add(headerPanel, BorderLayout.NORTH);

        // Create and add the footer panel
        footerPanel = new FooterPanel();
        this.add(footerPanel, BorderLayout.SOUTH);

        //-----------------------------------Content Template-----------------------------------//
        // Create and add the content panel
        JPanel contentPanel = new JPanel();
        contentPanel.setBackground(new Color(255, 209, 220));

        // Create a table model with column names
        DefaultTableModel tableModel = new DefaultTableModel();
        tableModel.addColumn("ID");
        tableModel.addColumn("Amount");
        tableModel.addColumn("Type");
        tableModel.addColumn("Note");
        tableModel.addColumn("Date");
        tableModel.addColumn("Image");

        // Create a JTable with the table model
        dataTable = new JTable(tableModel) {
            @Override
            public Class<?> getColumnClass(int column) {
                if (column == 5) {
                    return ImageIcon.class;
                }
                return super.getColumnClass(column);
            }
        };

        // Set the cell renderer for the image column
        dataTable.getColumnModel().getColumn(5).setCellRenderer(new ImageCellRenderer());

        // Set the row height dynamically based on the image height
        dataTable.setRowHeight(100);

        // Add the JTable to a scroll pane
        JScrollPane scrollPane = new JScrollPane(dataTable);

        // Create the delete button and edit button
        backButton = new JButton("Back");
        deleteButton = new JButton("Delete");
        editButton = new JButton("Edit");

        // Add action listener to the buttons
        deleteButton.addActionListener(this);
        backButton.addActionListener(this);
        editButton.addActionListener(this);

        // Add the components to the content panel
        contentPanel.add(scrollPane);
        contentPanel.add(backButton);
        contentPanel.add(editButton);
        contentPanel.add(deleteButton);

        // Add content into the frame
        this.add(contentPanel, BorderLayout.CENTER);

        // Read and display the data from the TSV file
        DataFileReader dataFileReader = new DataFileReader(tableModel);
        dataFileReader.readDataFile("income.tsv");

        // Adjust the column widths
        packColumns();

        // Set frame to visible
        setVisible(true);
    }

    public static void main(String[] args) {
        new IncomeHistory();
    }

    private class DataFileReader {
        private DefaultTableModel tableModel;

        public DataFileReader(DefaultTableModel tableModel) {
            this.tableModel = tableModel;
        }

        public void readDataFile(String filePath) {
            try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split("\t");
                    if (data.length == 6) {
                        String id = data[0];
                        String amount = data[1];
                        String type = data[2];
                        String note = data[3];
                        String date = data[4];
                        String imageBase64 = data[5];

                        // Decode the image from Base64
                        byte[] imageBytes = Base64.getDecoder().decode(imageBase64);

                        // Create an ImageIcon from the decoded image bytes
                        ImageIcon imageIcon = new ImageIcon(imageBytes);

                        // Add the data to the table model
                        tableModel.addRow(new Object[]{id, amount, type, note, date, imageIcon});
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private class ImageCellRenderer extends DefaultTableCellRenderer {
        public ImageCellRenderer() {
            setOpaque(true);
            setHorizontalAlignment(CENTER);
            setVerticalAlignment(CENTER);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                                                       boolean hasFocus, int row, int column) {
            JLabel label = new JLabel();

            // Set the image icon for rendering
            ImageIcon imageIcon = (ImageIcon) value;
            Image image = imageIcon.getImage().getScaledInstance(-1, 100, Image.SCALE_SMOOTH);
            label.setIcon(new ImageIcon(image));

            // Set the text to an empty string
            label.setText("");

            return label;
        }
    }
    
    private void editSelectedRow() {
        // Prompt the user to enter the ID of the row to edit
        String id = JOptionPane.showInputDialog(this, "Enter the ID of the row to edit:", "Edit Row", JOptionPane.QUESTION_MESSAGE);

        // Iterate over the rows and search for the row with the matching ID
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            String rowId = (String) dataTable.getValueAt(i, 0);
            if (rowId.equals(id)) {
                // Create a custom dialog to display entry boxes for editing the data
                JTextField amountField = new JTextField(dataTable.getValueAt(i, 1).toString());
                JTextField typeField = new JTextField(dataTable.getValueAt(i, 2).toString());
                JTextField noteField = new JTextField(dataTable.getValueAt(i, 3).toString());
                JTextField dateField = new JTextField(dataTable.getValueAt(i, 4).toString());

                JPanel panel = new JPanel(new GridLayout(0, 1));
                panel.add(new JLabel("Amount:"));
                panel.add(amountField);
                panel.add(new JLabel("Type:"));
                panel.add(typeField);
                panel.add(new JLabel("Note:"));
                panel.add(noteField);
                panel.add(new JLabel("Date:"));
                panel.add(dateField);
                panel.add(new JLabel(""));

                // Add a "Change Picture" button to let the user edit the image
                JButton changePictureButton = new JButton("Change Picture");
                panel.add(changePictureButton);

                int result = JOptionPane.showConfirmDialog(this, panel, "Edit Row", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
                if (result == JOptionPane.OK_OPTION) {
                    // Get the updated values from the text fields
                    String amount = amountField.getText();
                    String type = typeField.getText();
                    String note = noteField.getText();
                    String date = dateField.getText();

                    // Update the values in the table model
                    dataTable.setValueAt(amount, i, 1);
                    dataTable.setValueAt(type, i, 2);
                    dataTable.setValueAt(note, i, 3);
                    dataTable.setValueAt(date, i, 4);
                    
                    // If the "Change Picture" button was clicked, handle updating the image
                    if (changePictureButton.getModel().isPressed()) {
                        JFileChooser fileChooser = new JFileChooser();
                        fileChooser.setFileFilter(new FileNameExtensionFilter("Images", "jpg", "jpeg", "png"));
                        result = fileChooser.showOpenDialog(this);
                        if (result == JFileChooser.APPROVE_OPTION) {
                            
                            try {
                                // Read the selected image file and convert it to a Base64-encoded string
                                File selectedFile = fileChooser.getSelectedFile();
                                byte[] imageBytes = Files.readAllBytes(selectedFile.toPath());
                                String imageBase64 = Base64.getEncoder().encodeToString(imageBytes);

                                // Update the values in the table model, including the new image
                                dataTable.setValueAt(new ImageIcon(imageBytes), i, 5);

                                // Update the values in the TSV file
                                updateRowInTSVFile(id, amount, type, note, date, imageBase64);

                                // Display success message dialog
                                JOptionPane.showMessageDialog(this, "Row has been successfully updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    } else {
                        // Update the values in the TSV file
                        updateRowInTSVFile(id, amount, type, note, date, null);

                        // Display success message dialog
                        JOptionPane.showMessageDialog(this, "Row has been successfully updated.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    }
                }

                return; // Exit the method after editing the row
            }
        }

        // If no matching ID was found, display an error message
        JOptionPane.showMessageDialog(this, "No row with the specified ID found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void updateRowInTSVFile(String id, String amount, String type, String note, String date, String imageBase64) {
        String filePath = "income.tsv";
        String tempFilePath = "temp.tsv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length > 0 && data[0].equals(id)) {
                    if (imageBase64 != null) {
                        // Update the row with the new image value
                        line = String.format("%s\t%s\t%s\t%s\t%s\t%s", id, amount, type, note, date, imageBase64);
                    } else {
                        // Update the row without modifying the image
                        line = String.format("%s\t%s\t%s\t%s\t%s\t%s", id, amount, type, note, date, data[5]);
                    }
                }
                writer.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename the temporary file to replace the original file
        File file = new File(tempFilePath);
        File renamedFile = new File(filePath);
        if (renamedFile.exists()) {
            renamedFile.delete();
        }
        file.renameTo(renamedFile);
    }

    private void deleteSelectedRow() {
        // Prompt the user to enter the ID of the row to delete
        String id = JOptionPane.showInputDialog(this, "Enter the ID of the row to delete:", "Delete Row", JOptionPane.QUESTION_MESSAGE);

        // Iterate over the rows and search for the row with the matching ID
        for (int i = 0; i < dataTable.getRowCount(); i++) {
            String rowId = (String) dataTable.getValueAt(i, 0);
            if (rowId.equals(id)) {
                // Remove the row from the table model
                ((DefaultTableModel) dataTable.getModel()).removeRow(i);

                // Remove the row from the TSV file
                removeRowFromTSVFile(id);

                return; // Exit the method after deleting the row
            }
        }

        // If no matching ID was found, display an error message
        JOptionPane.showMessageDialog(this, "No row with the specified ID found.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    private void removeRowFromTSVFile(String id) {
        String filePath = "income.tsv";
        String tempFilePath = "temp.tsv";

        try (BufferedReader reader = new BufferedReader(new FileReader(filePath));
             PrintWriter writer = new PrintWriter(new FileWriter(tempFilePath))) {

            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\t");
                if (data.length > 0 && data[0].equals(id)) {
                    // Skip the row to be deleted
                    continue;
                }
                writer.println(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // Rename the temporary file to replace the original file
        File file = new File(tempFilePath);
        File renamedFile = new File(filePath);
        if (renamedFile.exists()) {
            renamedFile.delete();
            JOptionPane.showMessageDialog(this, "Data has been successfully deleted.", "Success", JOptionPane.INFORMATION_MESSAGE);
        }
        file.renameTo(renamedFile);
    }

    private void packColumns() {
        for (int column = 0; column < dataTable.getColumnCount(); column++) {
            TableColumn tableColumn = dataTable.getColumnModel().getColumn(column);
            int preferredWidth = tableColumn.getMinWidth();
            int maxWidth = tableColumn.getMaxWidth();

            for (int row = 0; row < dataTable.getRowCount(); row++) {
                TableCellRenderer cellRenderer = dataTable.getCellRenderer(row, column);
                Component c = dataTable.prepareRenderer(cellRenderer, row, column);
                int width = c.getPreferredSize().width + dataTable.getIntercellSpacing().width;
                preferredWidth = Math.max(preferredWidth, width);

                // We've exceeded the maximum width, no need to check other rows
                if (preferredWidth >= maxWidth) {
                    preferredWidth = maxWidth;
                    break;
                }
            }
            tableColumn.setPreferredWidth(preferredWidth);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == backButton) {
            // Handle back button click
            dispose(); // Close the current frame
        } else if (e.getSource() == deleteButton) {
            // Handle delete button click
            deleteSelectedRow();
        } else if (e.getSource() == editButton) {
            editSelectedRow();
            // Implement the edit functionality here
        }
    }
}



