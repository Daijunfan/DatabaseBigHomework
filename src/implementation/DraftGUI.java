package implementation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;

public class DraftGUI {
    Runner.Phone phone;
    JFrame mainJFrame;
    JPanel mainPanel;

    public DraftGUI(Runner.Phone phone) throws SQLException {
        this.phone = phone;
        initAll();
    }

    public void initAll() throws SQLException {
        initMainJFrame();
        initMainPanel();
        initJpanels();
        mainJFrame.setVisible(true);
    }

    public void initMainJpanel() {

    }

    public void initJpanels() {

    }

    public void initMainJFrame() {
        mainJFrame = new JFrame("Main GUI");
        mainJFrame.setSize(400, 300);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    public void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(4, 1));
        mainJFrame.add(mainPanel);///////////
    }

}
