package implementation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TeacherArrangementInformationGUI {
    Runner.Phone phone;
    JFrame arrangementJFrame;
    JPanel mainPanel;
    List<ArrangementPanel> arrangementPanels;
    int currentPage;

    JMenuBar menuBar;
    JMenu functionMenu;
    JMenuItem refreshItem;
    int teacherID;
    String teacherName;


    public TeacherArrangementInformationGUI(String teacherName, Runner.Phone phone) throws SQLException {
        this.teacherName = teacherName;
        this.teacherID = -1;
        this.phone = phone;
        initAll();
    }

    public void initAll() throws SQLException {
        getTeacherId();
        initArrangementJFrame();
        initMenu();
        initMainPanel();
        initArrangementPanel();
        initPageButton();
        arrangementJFrame.setVisible(true);
    }

    public void getTeacherId() throws SQLException {
        String sql = "SELECT teacher_id FROM teacher WHERE NAME = '" + this.teacherName + "'";
        ResultSet resultSet = (ResultSet) phone.sqlExecuter.execute(sql);
        if (resultSet.next()) {
            this.teacherID = resultSet.getInt("teacher_id");
        }
    }

    public void initMenu() {
        menuBar = new JMenuBar();
        functionMenu = new JMenu("Function");
        refreshItem = new JMenuItem("refresh");

        functionMenu.add(refreshItem);
        menuBar.add(functionMenu);

        arrangementJFrame.setJMenuBar(menuBar);
    }

    public void initMainPanel() {
        mainPanel = new JPanel(new GridLayout(20, 1));
        arrangementJFrame.add(mainPanel);
    }

    public void initArrangementPanel() throws SQLException {
        arrangementPanels = new ArrayList<>();
        currentPage = 0;
        loadArrangementListFromMySQL();
        showCurrentPageOfArrangementPanels();
    }

    public void initPageButton() {
        JPanel buttonPanel = new JPanel();
        JButton prevButton = new JButton("Previous");
        JButton nextButton = new JButton("Next");

        prevButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (currentPage > 0) {
                    currentPage--;
                    showCurrentPageOfArrangementPanels();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * 10 < arrangementPanels.size()) {
                    currentPage++;
                    showCurrentPageOfArrangementPanels();
                }
            }
        });

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        arrangementJFrame.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void initArrangementJFrame() throws SQLException {
        arrangementJFrame = new JFrame("TeacherArrangement Information");
        arrangementJFrame.setSize(800, 600);
        arrangementJFrame.setLocationRelativeTo(null);
        arrangementJFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    private void loadArrangementListFromMySQL() throws SQLException {
        String sql = "SELECT * FROM Arrangement WHERE teacher_id = " + teacherID;
        ResultSet result = (ResultSet) this.phone.sqlExecuter.execute(sql);
        arrangementPanels = new ArrayList<>();
        while (result.next()) {
            int teacherId = result.getInt("teacher_id");
            int labId = result.getInt("lab_id");
            String courseName = result.getString("coursename");
            String time = result.getString("time");
            int batch = result.getInt("batch");
            ArrangementPanel arrangementPanel = new ArrangementPanel(teacherId, labId, courseName, time, batch);
            addArrangementPanel(arrangementPanel);
        }
    }

    public void addArrangementPanel(ArrangementPanel panel) {
        arrangementPanels.add(panel);
    }

    private void showCurrentPageOfArrangementPanels() {
        mainPanel.removeAll();
        int start = currentPage * 10;
        int end = Math.min((currentPage + 1) * 10, arrangementPanels.size());
        for (int i = start; i < end; i++) {
            mainPanel.add(arrangementPanels.get(i));
            mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public static class ArrangementPanel extends JPanel {
        private int teacherId;
        private int labId;
        private String courseName;
        private String time;
        private int batch;

        public ArrangementPanel(int teacherId, int labId, String courseName, String time, int batch) {
            this.teacherId = teacherId;
            this.labId = labId;
            this.courseName = courseName;
            this.time = time;
            this.batch = batch;

            setLayout(new GridLayout(1, 2));

            add(new JLabel("Teacher ID: " + teacherId));
            add(new JLabel("Course Name: " + courseName));
            add(new JLabel("Lab ID: " + labId));
            add(new JLabel("Time: " + time));
            add(new JLabel("Batch: " + batch));
        }
    }

}
