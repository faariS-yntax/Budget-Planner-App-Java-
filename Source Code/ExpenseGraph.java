import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ExpenseGraph extends JFrame {
    private List<Double> data;
    private List<String> months;
    private int barWidth = 20;

    public ExpenseGraph() {
        setTitle("Expenses per Day");
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int screenWidth = screenSize.width;
        int screenHeight = screenSize.height;
        setSize(screenWidth, screenHeight);

        data = new ArrayList<>();
        months = new ArrayList<>();

        try {
            readDataFromFile("expense.tsv");
        } catch (IOException e) {
            e.printStackTrace();
        }

        JPanel chartPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawHistogram(g);
            }
        };

        getContentPane().add(chartPanel);
    }

    private void readDataFromFile(String fileName) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(fileName));

        String line;
        while ((line = reader.readLine()) != null) {
            String[] values = line.split("\t");
            double value = Double.parseDouble(values[1]); // Assuming the value is in the second column
            String month = values[0]; // Assuming the month is in the first column
            data.add(value);
            months.add(month);
        }

        reader.close();
    }

    private void drawHistogram(Graphics g) {
        int padding = 40;
        int width = getWidth() - (2 * padding);
        int height = getHeight() - (2 * padding);

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int barSpacing = (width - (barWidth * data.size())) / (data.size() + 1);

        // Find the maximum value in the data
        double maxValue = getMaxDataValue();

        // Draw y-axis line
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, padding, padding, getHeight() - padding);

        // Draw x-axis line
        int xAxisY = getHeight() - padding - 20;
        g2.setColor(Color.BLACK);
        g2.drawLine(padding, xAxisY, getWidth() - padding, xAxisY);

        // Draw y-axis label
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Expenses", padding - 30, padding - 10);

        // Draw x-axis label
        g2.setColor(Color.BLACK);
        g2.setFont(new Font("Arial", Font.BOLD, 12));
        g2.drawString("Day", getWidth() - padding + 10, xAxisY + 40);

        // Draw grid lines
        g2.setColor(Color.LIGHT_GRAY);
        int numGridLines = 5;
        double yInterval = maxValue / numGridLines;
        for (int i = 0; i <= numGridLines; i++) {
            int y = (int) (getHeight() - padding - i * (height - (2 * padding)) / numGridLines);
            g2.drawLine(padding, y, getWidth() - padding, y);
        }

        // Draw bars
        for (int i = 0; i < data.size(); i++) {
            double value = data.get(i);
            int barHeight = (int) (value / maxValue * (height - (2 * padding))); // Subtract 2*padding

            int x = padding + (i * (barWidth + barSpacing));
            int y = getHeight() - padding - barHeight;

            // Define bar color
            Color barColor = new Color(55, 122, 183);

            // Draw bar
            g2.setColor(barColor);
            g2.fillRect(x, y, barWidth, barHeight);

            // Draw bar border
            g2.setColor(Color.BLACK);
            g2.drawRect(x, y, barWidth, barHeight);

            // Draw the month labels
            g2.setColor(Color.BLACK);
            g2.setFont(new Font("Arial", Font.PLAIN, 10));
            g2.drawString(months.get(i), x + barWidth / 2 - 10, xAxisY + 20);
        }
    }

    private double getMaxDataValue() {
        double maxValue = 0.0;

        for (double value : data) {
            if (value > maxValue) {
                maxValue = value;
            }
        }

        return maxValue;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            ExpenseGraph app = new ExpenseGraph();
            app.setVisible(true);
        });
    }
}
