package implementation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginGUI {
    Runner.Phone phone;
    JFrame loginJFrame;
    JTextField passwordTextField, nameTextField;
    JButton loginButton;
    boolean hide = false;
    private final Object lock = new Object();

    public LoginGUI(Runner.Phone phone) throws SQLException {
        this.phone = phone;
        initAll();
    }

    public void initAll() throws SQLException {
        initMainJFrame();
        initTextInput();
        initLoginButton();
        loginJFrame.setVisible(true);
    }

    public void initMainJFrame() {
        loginJFrame = new JFrame("Login");
        loginJFrame.setSize(400, 200); // 适当调整窗口大小
        loginJFrame.setLocationRelativeTo(null);
        loginJFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginJFrame.setLayout(new GridBagLayout()); // 使用GridBagLayout
    }

    public void initTextInput() {
        nameTextField = new JTextField(15);
        passwordTextField = new JPasswordField(15); // 使用JPasswordField而不是JTextField

        // 为文本输入框添加键盘监听器
        KeyListener enterKeyListener = new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                        loginButton.doClick();
                    }
                } catch (Exception ex) {
                    // 静默处理异常
                }
            }
        };

        nameTextField.addKeyListener(enterKeyListener);
        passwordTextField.addKeyListener(enterKeyListener);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 增加边距

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.WEST;
        loginJFrame.add(new JLabel("UserName: "), gbc);

        gbc.gridx = 1;
        loginJFrame.add(nameTextField, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        loginJFrame.add(new JLabel("Password: "), gbc);

        gbc.gridx = 1;
        loginJFrame.add(passwordTextField, gbc);
    }

    public void initLoginButton() {
        loginButton = new JButton("Login");
        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String password = new String(((JPasswordField) passwordTextField).getPassword()); // 获取密码
                String name = nameTextField.getText();
                String sql = "SELECT * FROM users WHERE password = '" + password + "' AND name = '" + name + "'";
                ResultSet resultSet;
                try {
                    resultSet = (ResultSet) phone.sqlExecuter.execute(sql);
                    if (resultSet.next()) {
                        System.out.println("success!");
                        phone.guiManager.setUserName(resultSet.getString("name"));
                        phone.guiManager.setAuthority(resultSet.getInt("authority"));
                        phone.guiManager.setLogSuccess(true);
                    } else {
                        JOptionPane.showMessageDialog(loginJFrame, "Wrong user ID or name!");
                    }
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10); // 增加边距
        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        loginJFrame.add(loginButton, gbc);
    }

    public void hide() {
        loginJFrame.setVisible(false);
    }
}