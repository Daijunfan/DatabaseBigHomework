package implementation;

import java.sql.SQLException;

public class Runner {

    Phone phone;
    SQLExecuter sqlExecuter;
    GUIManager guiManager;

    public Runner() throws SQLException {
        phone = new Phone();
        sqlExecuter = new SQLExecuter(phone);
        guiManager = new GUIManager(phone);
        phone.setGuijFrame(guiManager);
        phone.setSqlExecuter(sqlExecuter);
    }

    public class Phone {
        public GUIManager guiManager;
        public SQLExecuter sqlExecuter;

        public void setGuijFrame(GUIManager guijFrame) {
            this.guiManager = guijFrame;
        }
        public void setSqlExecuter(SQLExecuter sqlExecuter) {
            this.sqlExecuter = sqlExecuter;
        }
    }

    public void start() throws SQLException {
        guiManager.run();
    }
}