package test;

import data.DataHelper;
import data.SQLHelper;
import org.junit.jupiter.api.*;
import page.DashboardPage;
import page.LoginPage;
import page.VerificationPage;

import static com.codeborne.selenide.Selenide.open;
import static data.SQLHelper.*;
import static java.awt.SystemColor.info;

public class BankTests {
    DashboardPage dashboardPage;

    @AfterEach
    void tearDown() {
        cleanAuthCode();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999", LoginPage.class);
    }

    @Test
    void shouldSuccessfullLogin() {
        var authInfo = DataHelper.getAuthInfo();
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = SQLHelper.getVerificationCode();
        dashboardPage = verificationPage.validVerify(verificationCode.getCode());

    }

    @Test
    void invalidLogin() {
        var randomUser = DataHelper.generateRandomUser();
        var loginPage = new LoginPage();
        loginPage.invalidLogin(randomUser);
        loginPage.verifyErrorNotification("Ошибка\nОшибка! Неверно указан логин или пароль");
    }

    @Test
    void invalidCode() {
        var authInfo = DataHelper.getAuthInfo();
        var loginPage = new LoginPage();
        var verificationPage = loginPage.validLogin(authInfo);
        var verificationCode = DataHelper.getVerificationCode(authInfo);
        verificationPage.verify(verificationCode.getCode());
        verificationPage.verifyErrorNotification("Ошибка\nОшибка! Неверно указан код! Попробуйте ещё раз.");
    }
}
