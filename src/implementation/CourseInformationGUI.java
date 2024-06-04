package implementation;

import implementation.AbstractClasses.CommonObject;

import java.sql.SQLException;

public class CourseInformationGUI extends CommonObject {

    public CourseInformationGUI(Runner.Phone phone) throws SQLException {
        super(phone);
        initAll();
    }

    public CourseInformationGUI(String userInput, Runner.Phone phone) throws SQLException {
        super(phone);
        setUserInput(userInput);
        initAllWithoutMenu();
    }

    @Override
    public String getTableName() {
        return "course";
    }

    @Override
    protected String getOrderName() {
        return "course_id ASC";
    }

    @Override
    protected void setUserInput(String userInput) {
        super.userInput = userInput;
    }

}
