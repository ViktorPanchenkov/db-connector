package ua.test.db.utils;

import ua.test.db.enums.DbName;

import java.util.ResourceBundle;

public class PropertyLoader {

    private String properyFileName;

    public PropertyLoader(final DbName dbName) {
        switch (dbName) {
            case TST_METRICS:
                properyFileName = "mysql";
                break;
            case TEST_RAIL:
                properyFileName = "testrail";
                break;
            case POSTGRESSDB:
                properyFileName = "posgresql";
                break;
            default:
                properyFileName = "mysql";
                break;
        }
    }

    public Object load(final String property) {
        ResourceBundle rb = ResourceBundle.getBundle(properyFileName);
        return rb.getObject(property);
    }
}
