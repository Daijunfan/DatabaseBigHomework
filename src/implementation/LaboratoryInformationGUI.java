package implementation;

import implementation.AbstractClasses.CommonObject;

import java.sql.SQLException;

public class LaboratoryInformationGUI extends CommonObject {

    public LaboratoryInformationGUI(Runner.Phone phone) throws SQLException {
        super(phone);
        initAll();
    }

    public LaboratoryInformationGUI(String userInput, Runner.Phone phone) throws SQLException {
        super(phone);
        setUserInput(userInput);
        initAllWithoutMenu();
    }

    @Override
    public String getTableName() {
        return "laboratory";
    }

    @Override
    protected String getOrderName() {
        return "lab_id ASC";
    }

    @Override
    protected void setUserInput(String userInput) {
        super.userInput = userInput;
    }

}
