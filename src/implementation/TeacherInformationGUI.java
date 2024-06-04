package implementation;

import implementation.AbstractClasses.CommonObject;

import java.sql.SQLException;

public class TeacherInformationGUI extends CommonObject {

    public TeacherInformationGUI(Runner.Phone phone) throws SQLException {
        super(phone);
        initAll();
    }

    public TeacherInformationGUI(String userInput, Runner.Phone phone) throws SQLException {
        super(phone);
        setUserInput(userInput);
        initAllWithoutMenu();
    }

    @Override
    public String getTableName() {
        return "teacher";
    }

    @Override
    protected String getOrderName() {
        return "teacher_id ASC";
    }

    @Override
    protected void setUserInput(String userInput) {
        super.userInput = userInput;
    }

}
