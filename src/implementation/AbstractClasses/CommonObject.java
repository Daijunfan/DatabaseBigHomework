package implementation.AbstractClasses;

import implementation.*;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public abstract class CommonObject {

    public class StringUtils {
        public static String capitalizeFirstLetter(String str) {
            if (str == null || str.isEmpty()) {
                return str;
            }
            return str.substring(0, 1).toUpperCase() + str.substring(1);
        }
    }


    protected Runner.Phone phone;
    protected JFrame objectInformationJFrame;
    protected JPanel mainPanel;
    protected List<ObjectPanel> objectPanels;
    protected ArrayList<String> columnNames;
    protected int currentPage;
    protected String userInput = "";

    public CommonObject(Runner.Phone phone) throws SQLException {
        this.phone = phone;
    }

    protected void initAll() throws SQLException {
        initObjectInformationJFrame();
        initMenu();
        initMainPanel();
        initObjectPanel();
        initPageButton();
        objectInformationJFrame.setVisible(true);
    }

    protected void initAllWithoutMenu() throws SQLException {
        initObjectInformationJFrame();
        initMainPanel();
        initObjectPanel();
        initPageButton();
        objectInformationJFrame.setVisible(true);
    }

    public void initMenu() {
        JMenuBar menuBar = new JMenuBar();
        JMenu fileMenu = new JMenu("File");
        JMenuItem addItem = new JMenuItem("add");
        JMenuItem deleteItem = new JMenuItem("delete");
        JMenuItem updateItem = new JMenuItem("update");
        JMenuItem queryItem = new JMenuItem("search");
        JMenuItem refresh = new JMenuItem("Refresh");

        fileMenu.add(addItem);
        fileMenu.add(deleteItem);
        fileMenu.add(updateItem);
        fileMenu.add(queryItem);
        fileMenu.add(refresh);
        menuBar.add(fileMenu);

        refresh.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    loadObjectListFromMySQL();
                    showCurrentPageOfObjectPanels();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        queryItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    showSearchDialog();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        addItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showAddDialog();
            }
        });

        deleteItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showDeleteDialog();
            }
        });

        updateItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                showUpdateDialog();
            }
        });

        objectInformationJFrame.setJMenuBar(menuBar);
    }

    protected abstract void setUserInput(String userInput);// choose to override

    protected String getCondition() {
        if (userInput == null || userInput.trim().isEmpty()) {
            return "";
        }

        StringBuilder condition = new StringBuilder(" WHERE ");
        for (int i = 0; i < columnNames.size(); i++) {
            if (i > 0) {
                condition.append(" OR ");
            }
            condition.append(columnNames.get(i)).append(" LIKE '%").append(userInput).append("%'");
        }
        return condition.toString();
    }

    public void initMainPanel() {
        mainPanel = new JPanel(new GridLayout(20, 1));
        objectInformationJFrame.add(mainPanel, BorderLayout.CENTER);
    }

    public void initObjectPanel() throws SQLException {
        currentPage = 0;
        loadObjectListFromMySQL();
        showCurrentPageOfObjectPanels();
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
                    showCurrentPageOfObjectPanels();
                }
            }
        });

        nextButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if ((currentPage + 1) * 10 < objectPanels.size()) {
                    currentPage++;
                    showCurrentPageOfObjectPanels();
                }
            }
        });

        buttonPanel.add(prevButton);
        buttonPanel.add(nextButton);
        objectInformationJFrame.add(buttonPanel, BorderLayout.SOUTH);
    }

    private void getColumnNames() throws SQLException {
        columnNames = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + " LIMIT 1";  // 只取一行数据
        ResultSet result = (ResultSet) this.phone.sqlExecuter.execute(sql);
        if (result == null) {
            System.out.println("Result set is null");
            return;
        }
        ResultSetMetaData metaData = result.getMetaData();
        int columnCount = metaData.getColumnCount();
        columnNames.clear();  // 清空之前的列名
        for (int i = 1; i <= columnCount; i++) {
            columnNames.add(metaData.getColumnName(i));
        }
        for (int i = 0; i < columnCount; i++) {
            System.out.println("Column " + i + ": " + columnNames.get(i));
        }
    }

    public void initObjectInformationJFrame() throws SQLException {
        objectInformationJFrame = new JFrame("Object Information");
        objectInformationJFrame.setSize(800, 600);
        objectInformationJFrame.setLocationRelativeTo(null);
        objectInformationJFrame.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
    }

    protected void loadObjectListFromMySQL() throws SQLException {
        getColumnNames();
        objectPanels = new ArrayList<>();
        String sql = "SELECT * FROM " + getTableName() + getCondition() + " ORDER BY " + getOrderName();
        ResultSet result = (ResultSet) this.phone.sqlExecuter.execute(sql);
        if (result == null) {
            System.out.println("Result set is null");
            return;
        }
        while (result.next()) {
            ObjectPanel objectPanel = createObjectPanel(result);
            addObjectPanel(objectPanel);
        }
    }

    public abstract String getTableName();

    protected abstract String getOrderName();

    public ObjectPanel createObjectPanel(ResultSet result) throws SQLException {
        return new ObjectPanel(result);
    }

    public void addObjectPanel(ObjectPanel panel) {
        objectPanels.add(panel);
    }

    protected void showSearchDialog() throws SQLException {
        String input = JOptionPane.showInputDialog(objectInformationJFrame, "Enter search query:", "Search", JOptionPane.PLAIN_MESSAGE);
        if (input != null) {
            String tableName = getTableName();
            String methodName = "create" + StringUtils.capitalizeFirstLetter(tableName) + "SelectionGUI";

            try {
                Method method = phone.guiManager.getClass().getMethod(methodName, String.class);
                method.invoke(phone.guiManager, input);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }

            try {
                loadObjectListFromMySQL();
                showCurrentPageOfObjectPanels();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void showAddDialog() {
        JPanel panel = new JPanel(new GridLayout(columnNames.size(), 2));
        List<JTextField> textFields = new ArrayList<>();

        for (String columnName : columnNames) {
            panel.add(new JLabel(columnName + ": "));
            JTextField textField = new JTextField();
            panel.add(textField);
            textFields.add(textField);
        }

        int result = JOptionPane.showConfirmDialog(null, panel, "Add New Record", JOptionPane.OK_CANCEL_OPTION);
        if (result == JOptionPane.OK_OPTION) {
            try {
                StringBuilder columns = new StringBuilder();
                StringBuilder values = new StringBuilder();

                for (int i = 0; i < columnNames.size(); i++) {
                    columns.append(columnNames.get(i));
                    values.append("'").append(textFields.get(i).getText()).append("'");
                    if (i < columnNames.size() - 1) {
                        columns.append(", ");
                        values.append(", ");
                    }
                }

                String sql = "INSERT INTO " + getTableName() + " (" + columns.toString() + ") VALUES (" + values.toString() + ")";
                phone.sqlExecuter.execute(sql);

                loadObjectListFromMySQL();
                showCurrentPageOfObjectPanels();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void showDeleteDialog() {
        String columnName = JOptionPane.showInputDialog(objectInformationJFrame, "Enter the column name of the record to delete:", "Delete Record", JOptionPane.PLAIN_MESSAGE);
        if (columnName == null || columnName.trim().isEmpty()) {
            return; // 用户未输入列名，直接返回
        }

        String value = JOptionPane.showInputDialog(objectInformationJFrame, "Enter the value of the record to delete:", "Delete Record", JOptionPane.PLAIN_MESSAGE);
        if (value == null || value.trim().isEmpty()) {
            return; // 用户未输入值，直接返回
        }

        if (columnName != null && value != null) {
            try {
                // 构建 SQL 删除语句，注意处理值的引号问题
                String sql = "DELETE FROM " + getTableName() + " WHERE " + columnName + " = '" + value + "'";
                phone.sqlExecuter.execute(sql);

                loadObjectListFromMySQL();
                showCurrentPageOfObjectPanels();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    protected void showUpdateDialog() {
        // Create search panel for inputting search criteria
        JPanel searchPanel = new JPanel(new GridLayout(columnNames.size(), 2));
        List<JTextField> searchFields = new ArrayList<>();
        for (String colName : columnNames) {
            searchPanel.add(new JLabel(colName + ": "));
            JTextField searchField = new JTextField();
            searchPanel.add(searchField);
            searchFields.add(searchField);
        }

        int searchResult = JOptionPane.showConfirmDialog(null, searchPanel, "Identify Record to Update", JOptionPane.OK_CANCEL_OPTION);
        if (searchResult != JOptionPane.OK_OPTION) {
            return; // 用户取消操作
        }

        // Create update panel for inputting new values
        JPanel updatePanel = new JPanel(new GridLayout(columnNames.size(), 2));
        List<JTextField> updateFields = new ArrayList<>();
        for (String colName : columnNames) {
            updatePanel.add(new JLabel(colName + ": "));
            JTextField updateField = new JTextField();
            updatePanel.add(updateField);
            updateFields.add(updateField);
        }

        int updateResult = JOptionPane.showConfirmDialog(null, updatePanel, "Update Record", JOptionPane.OK_CANCEL_OPTION);
        if (updateResult != JOptionPane.OK_OPTION) {
            return; // 用户取消操作
        }

        try {
            // Construct the WHERE clause based on search criteria
            StringBuilder whereClause = new StringBuilder(" WHERE ");
            boolean isFirstCondition = true;
            for (int i = 0; i < columnNames.size(); i++) {
                String searchValue = searchFields.get(i).getText().trim();
                if (!searchValue.isEmpty()) {
                    if (!isFirstCondition) {
                        whereClause.append(" AND ");
                    }
                    whereClause.append(columnNames.get(i)).append(" = '").append(searchValue).append("'");
                    isFirstCondition = false;
                }
            }

            if (isFirstCondition) {
                // 如果用户没有输入任何搜索条件，则直接返回
                return;
            }

            // Construct the SET clause based on update values
            StringBuilder setClause = new StringBuilder();
            boolean isFirstUpdate = true;
            for (int i = 0; i < columnNames.size(); i++) {
                String updateValue = updateFields.get(i).getText().trim();
                if (!updateValue.isEmpty()) {
                    if (!isFirstUpdate) {
                        setClause.append(", ");
                    }
                    setClause.append(columnNames.get(i)).append(" = '").append(updateValue).append("'");
                    isFirstUpdate = false;
                }
            }

            if (isFirstUpdate) {
                // 如果用户没有输入任何更新值，则直接返回
                return;
            }

            // Construct and execute the SQL UPDATE statement
            String sql = "UPDATE " + getTableName() + " SET " + setClause.toString() + whereClause.toString();
            phone.sqlExecuter.execute(sql);

            loadObjectListFromMySQL();
            showCurrentPageOfObjectPanels();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    protected void showCurrentPageOfObjectPanels() {
        mainPanel.removeAll();
        int start = currentPage * 10;
        int end = Math.min((currentPage + 1) * 10, objectPanels.size());
        for (int i = start; i < end; i++) {
            mainPanel.add(objectPanels.get(i));
            mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        }
        mainPanel.revalidate();
        mainPanel.repaint();
    }

    public class ObjectPanel extends JPanel {
        public ObjectPanel(ResultSet resultSet) throws SQLException {
            initializeComponents(resultSet);
        }

        public void initializeComponents(ResultSet resultSet) throws SQLException {
            int columnCount = columnNames.size();
            // Create a panel for the labels and values
            JPanel panel = new JPanel(new GridLayout(1, columnCount * 2));

            // Iterate through each column and add a label and value to the panel
            for (int i = 1; i <= columnCount; i++) {
                String columnName = columnNames.get(i - 1);
                String columnValue = resultSet.getString(i);
                panel.add(new JLabel(columnName + ": "));
                panel.add(new JLabel(columnValue));
            }

            // Add the panel to the main panel
            setLayout(new BorderLayout());
            add(panel, BorderLayout.CENTER);
        }
    }
}