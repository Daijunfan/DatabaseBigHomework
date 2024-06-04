package implementation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.SQLException;

public class MainGUI {
    Runner.Phone phone;
    JFrame mainJFrame;
    JPanel mainPanel;

    JPanel jPanelCourses;
    JPanel jPanelTeachers;
    JPanel jPanelLaboratory;
    JPanel jPanelStudents;

    JButton jButtonTeachers;
    JButton jButtonCourses;
    JButton jButtonLaboratory;
    JButton jButtonStudents;

    public MainGUI(Runner.Phone phone) throws SQLException {
        this.phone = phone;
        initAll();
    }

    public void initAll() throws SQLException {
        initMainJFrame();
        initMenu();
        initMainPanel();
        initJPanels();
        mainJFrame.setVisible(true);
    }

    public void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("Function");
        JMenuItem secretaryItem = new JMenuItem("Secretary");
        JMenuItem teacherItem = new JMenuItem("Teacher");

        teacherItem.addActionListener(e -> {
            try {
                phone.guiManager.createTeacherArrangementSelectionGUI(phone.guiManager.userName);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        secretaryItem.addActionListener(e -> {
            try {
                phone.guiManager.createArrangementInformationGUI();
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        });

        fileMenu.add(secretaryItem);
        fileMenu.add(teacherItem);
        menuBar.add(fileMenu);
        mainJFrame.setJMenuBar(menuBar);
    }

    public void initMainJFrame() {
        mainJFrame = new JFrame("Main GUI");
        mainJFrame.setSize(600, 500);
        mainJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainJFrame.setLocationRelativeTo(null);
        mainJFrame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    phone.sqlExecuter.closeDatabaseSource();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }

    public void initMainPanel() {
        mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainJFrame.add(mainPanel);
    }
    public JPanel initJPanel(String labelText, JButton jButton) {
        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BorderLayout());
        jPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));

        JLabel label = new JLabel(labelText);
        label.setFont(new Font("Arial", Font.BOLD, 14));
        jPanel.add(label, BorderLayout.WEST);

        // Customize JButton appearance
        jButton.setText("View");
        jButton.setFont(new Font("Arial", Font.BOLD, 12));
        jButton.setBackground(new Color(70, 130, 180)); // Steel Blue color
        jButton.setForeground(Color.WHITE);
        jButton.setFocusPainted(false);
        jButton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(Color.GRAY, 1),
                BorderFactory.createEmptyBorder(5, 15, 5, 15)
        ));

        jButton.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                jButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
                jButton.setBackground(new Color(100, 149, 237)); // Lighter Steel Blue
            }

            @Override
            public void mouseExited(MouseEvent e) {
                jButton.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
                jButton.setBackground(new Color(70, 130, 180)); // Back to original color
            }

            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    if (e.getSource() == jButtonLaboratory) {
                        phone.guiManager.createLaboratoryInformationGUI();
                    } else if (e.getSource() == jButtonCourses) {
                        phone.guiManager.createCourseInformationGUI();
                    } else if (e.getSource() == jButtonTeachers) {
                        phone.guiManager.createTeacherInformationGUI();
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        jPanel.add(jButton, BorderLayout.EAST);
        return jPanel;
    }

    public void initJPanels() {
        jButtonCourses = new JButton();
        jPanelCourses = initJPanel("Courses Information", jButtonCourses);

        jButtonTeachers = new JButton();
        jPanelTeachers = initJPanel("Teachers Information", jButtonTeachers);

        jButtonLaboratory = new JButton();
        jPanelLaboratory = initJPanel("Classrooms Information", jButtonLaboratory);

        jButtonStudents = new JButton();
        jPanelStudents = initJPanel("Students Information", jButtonStudents);

        mainPanel.add(jPanelCourses);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(jPanelTeachers);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(jPanelLaboratory);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(jPanelStudents);
    }

}
