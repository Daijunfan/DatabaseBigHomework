import implementation.*;

import java.io.PrintStream;
import java.sql.SQLException;

public class APP {
    public static void main(String[] args) throws SQLException {

        System.setErr(new PrintStream(new java.io.OutputStream() {
            @Override
            public void write(int b) {

            }
        }));

        Runner runner = new Runner();
        runner.start();
    }

}
