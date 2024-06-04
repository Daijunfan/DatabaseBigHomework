package implementation;

import implementation.AbstractClasses.CommonObject;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ArrangementInformationGUI extends CommonObject {
    Runner.Phone phone;

    public ArrangementInformationGUI(Runner.Phone phone) throws SQLException {
        super(phone);
        this.phone = phone;
        initAll();
    }

    public ArrangementInformationGUI(String userInput, Runner.Phone phone) throws SQLException {
        super(phone);
        this.phone = phone;
        setUserInput(userInput);
        initAllWithoutMenu();
    }

    @Override
    protected void setUserInput(String userInput) {
        super.userInput = userInput;
    }

    @Override
    public String getTableName() {
        return "arrangement";
    }

    @Override
    protected String getOrderName() {
        return "teacher_id ASC";
    }

    @Override
    protected void showAddDialog() {

    }

    @Override
    protected void showDeleteDialog() {

    }

    @Override
    protected void showUpdateDialog() {

    }

}

