package DataProviders;

import org.apache.commons.lang3.RandomStringUtils;
import org.testng.annotations.DataProvider;

public interface Data {

    String randomFirstName = "FirstName" + RandomStringUtils.randomAlphabetic(4);
    String randomLastName = "LastNama" + RandomStringUtils.randomAlphabetic(4);
    String randomEmail = "Email" + RandomStringUtils.randomAlphabetic(3) + "@gmail.com";

    @DataProvider(name = "RandomInsertDataProvider")
    public static Object[][] ContactData() {
        Object[][] ContactData = {{randomFirstName,randomLastName,randomEmail}};
        return ContactData;
    }
}
