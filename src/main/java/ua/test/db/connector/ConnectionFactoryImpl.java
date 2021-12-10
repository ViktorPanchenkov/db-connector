package ua.test.db.connector;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ua.test.db.enums.DbName;
import ua.test.db.utils.PropertyLoader;

import java.sql.*;

/**
 * Класс для подключения к бд.
 */
 public class ConnectionFactoryImpl implements ConnectionFactoryBatchApproach {

    static final Logger LOG = LoggerFactory.getLogger(ConnectionFactoryImpl.class);
    static final String CONNECTION_IS_CLOSED = "Connection is closed!";

    private Connection connection;
    private String dbDriverName;
    private String dbConnectionString;
    private String user;
    private String password;

    /**
     * Создает подключение к указанной БД.
     */
    public ConnectionFactoryImpl(DbName dbName) {
        loadProperty(dbName);
        initConnection();

    }

    /**
     * Возвращает ссылку на подключение.
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * Проверяет подключение.
     *
     * @throws IllegalArgumentException если подключение закрыто
     */
    public void checkConnection() throws SQLException {
        if (this.connection.isClosed()) {
            throw new IllegalArgumentException(CONNECTION_IS_CLOSED);
        }
    }

    /**
     * Закрывает подключение.
     */
    public void closeConnection() throws SQLException {
        if (this.connection != null && !this.connection.isClosed()) {
            this.connection.close();
            this.connection = null;
        }
    }

    public void loadProperty(final DbName dbName) {
        final PropertyLoader propertyLoader;

        if (dbName.equals(DbName.TEST_RAIL)) {
            propertyLoader = new PropertyLoader(DbName.TEST_RAIL);
        } else if (dbName.equals(DbName.TST_METRICS)) {
            propertyLoader = new PropertyLoader(DbName.TST_METRICS);
        } else {
            propertyLoader = new PropertyLoader(DbName.POSTGRESSDB);
        }

      //  this.dbDriverName = propertyLoader.load("deriver").toString();
        this.dbConnectionString = propertyLoader.load("connection_string").toString();
        this.user = propertyLoader.load("user").toString();
        this.password = propertyLoader.load("pass").toString();

    }

    /*
    public Connection initConnection() {
        try {
            Class.forName(dbDriverName);
            return DriverManager.getConnection(dbConnectionString, this.user, this.password);
        } catch (SQLException e) {
            LOG.error("DB connection error!", e);
            LOG.error(e.getMessage());
        } catch (ClassNotFoundException e) {
            LOG.error("Error during JDBC driver initialization!", e);
            LOG.error(e.getMessage());
        }

        return null;
    }
*/
    public void initConnection() {
        try {
            connection = DriverManager.getConnection(dbConnectionString, user, password);
            System.out.println("Connection to  server");
        } catch (SQLException throwables) {
            System.err.println("Error in connection to  server");
            throwables.printStackTrace();
        }
    }
    public void prereareInsertStatement(String sql,String randomFirstName,String randomLastName,String randomEmail) throws SQLException {
        PreparedStatement statement = connection.prepareStatement(sql);

        statement.setString(1,randomFirstName);
        statement.setString(2,randomLastName);
        statement.setString(3,randomEmail);

        statement.execute();
    }
    public void selectQuery(String selectQuery) throws SQLException {
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(selectQuery);
        while (resultSet.next()){
            int id = resultSet.getInt("id");
            String firstname = resultSet.getString("first_name");
            String lastname = resultSet.getString("last_name");
            String email = resultSet.getString("email");

            System.out.printf("%d - %s -%s - %s\n", id,firstname,lastname,email);
        }
    }

}
