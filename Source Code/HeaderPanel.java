import javax.swing.*;
import java.awt.*;

public class HeaderPanel extends JPanel {
    private JLabel headerLabel;

    public HeaderPanel(String headerText) {
        this.setLayout(new FlowLayout());
        headerLabel = new JLabel(headerText);
        headerLabel.setFont(new Font("Dialog", Font.BOLD, 30));
        this.add(headerLabel);
        this.setBackground(new Color(252, 149, 173));
    }

    public void addButton(JButton backButton) {
    }
}
