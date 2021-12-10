package connectio;




import DataProviders.Data;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ua.test.db.connector.ConnectionFactoryImpl;
import ua.test.db.enums.DbName;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DBTest implements Data {

    ConnectionFactoryImpl connectionFactoryImp;
    @BeforeMethod
    public void setAndCheckConnection() throws SQLException {
         connectionFactoryImp = new ConnectionFactoryImpl(DbName.POSTGRESSDB);
        connectionFactoryImp.initConnection();
        connectionFactoryImp.checkConnection();
    }
    @Test(dataProvider = "RandomInsertDataProvider")
    public void insertData(String randomFirstName,String randomLastName, String randomEmail) throws SQLException {
        String sql = "INSERT INTO contacts2 (first_name, last_name, email)" + "VALUES (?,?,?)";
        connectionFactoryImp.prereareInsertStatement(sql,randomFirstName,randomLastName,randomEmail);
    }
    @Test
    public void selectAllData() throws SQLException {
        String selectQuery = "Select * FROM contacts2";
        connectionFactoryImp.selectQuery(selectQuery);
    }
    @AfterMethod
    public void close() throws SQLException {
        connectionFactoryImp.closeConnection();
    }
}
