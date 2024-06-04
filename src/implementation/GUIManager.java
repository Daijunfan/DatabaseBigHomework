package implementation;

import javax.swing.*;
import java.sql.SQLException;

public class GUIManager {
    boolean programExit;
    boolean logSuccess;
    Runner.Phone phone;

    int authority;
    String userName;

    LaboratoryInformationGUI laboratoryInformationGUI;
    LaboratoryInformationGUI laboratorySelectionGUI;
    CourseInformationGUI courseInformationGUI;
    CourseInformationGUI courseSelectionGUI;
    TeacherInformationGUI teacherInformationGUI;
    TeacherInformationGUI teacherSelectionGUI;
    ArrangementInformationGUI arrangementInformationGUI;
    ArrangementInformationGUI arrangementSelectionGUI;
    TeacherArrangementInformationGUI teacherArrangementInformationGUI;
    SecretarySearchInformationGUI secretarySearchInformationGUI;
    LoginGUI loginGUI;
    MainGUI mainGUI;

    private Object lock = new Object(); // 创建一个对象作为锁

    public GUIManager(Runner.Phone phone) {
        logSuccess = false;
        this.phone = phone;
    }

    public void run() throws SQLException {
       // login();
        createMainGUI();
    }

    public void login() throws SQLException {
        createLoginGUI();
        waitUntilLoginSuccessful();
        hideLoginGUI();
    }

    public void doSomeThing() {
        System.out.print("");
    }

    public void delay() {
        for (int i = 0; i < 10000 ;i++) {
            for (int j = 0; j < 10000; j++)  {
                doSomeThing();
            }
        }
    }

    public void hideLoginGUI() {
        this.loginGUI.hide();
    }

    public void createSecretarySearchInformationGUI(int teacherId, int labId, String courseName, String time, int batch) throws SQLException {
        secretarySearchInformationGUI = new SecretarySearchInformationGUI(teacherId, labId, courseName, time, batch, phone);
    }

    public void createTeacherArrangementSelectionGUI(String name) throws SQLException {
        if (!checkAuthority(2)) return;
        System.out.println(" GUIMANAGER on 50 begin");
        teacherArrangementInformationGUI = new TeacherArrangementInformationGUI(name, this.phone);
    }

    public void createArrangementInformationGUI() throws SQLException {
        if (!checkAuthority(1)) return;
        arrangementInformationGUI = new ArrangementInformationGUI(this.phone);
    }

    public void createMainGUI() throws SQLException {
        mainGUI = new MainGUI(phone);
    }

    public void createCourseInformationGUI() throws SQLException {
        courseInformationGUI = new CourseInformationGUI(phone);
    }

    public void createLoginGUI() throws SQLException {
        loginGUI = new LoginGUI(phone);
    }

    public void createTeacherInformationGUI() throws SQLException {
        teacherInformationGUI = new TeacherInformationGUI(phone);
    }

    public void createArrangementSelectionGUI(String userInput) throws SQLException {
        arrangementSelectionGUI = new ArrangementInformationGUI(userInput, this.phone);
    }

    public void createTeacherSelectionGUI(String userInput) throws SQLException {
        teacherSelectionGUI = new TeacherInformationGUI(userInput, this.phone);
    }

    public void createCourseSelectionGUI(String userInput) throws SQLException {
        courseSelectionGUI = new CourseInformationGUI(userInput, this.phone);
    }

    public void createLaboratorySelectionGUI(String userInput) throws SQLException {
        laboratorySelectionGUI = new LaboratoryInformationGUI(userInput, this.phone);
    }

    public void createLaboratoryInformationGUI() throws SQLException {
        laboratoryInformationGUI = new LaboratoryInformationGUI(phone);
    }

    public void setAuthority(int authority) {
        this.authority = authority;
    }

    public void setUserName(String name) {
        this.userName = name;
    }

    private boolean checkAuthority(int requiredAuthority) {
        if (this.authority > requiredAuthority) {
            JOptionPane.showMessageDialog(null, "权限不足，无法进行此操作", "权限警告", JOptionPane.WARNING_MESSAGE);
            return false;
        }
        System.out.println("权限通过!");
        return true;
    }

    public void waitUntilLoginSuccessful() throws SQLException {
        // 在同步块中等待logSuccess状态改变
        synchronized (lock) {
            while (!logSuccess) {
                try {
                    lock.wait(); // 等待条件满足
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    // 设置logSuccess，并通知等待的线程
    public void setLogSuccess(boolean value) {
        synchronized (lock) {
            logSuccess = value;
            lock.notify(); // 通知等待的线程条件已经满足
        }
    }
}

