package com.elakov.rangiffler.page.auth;

import com.codeborne.selenide.SelenideElement;
import com.elakov.rangiffler.helper.allure.AllureSoftStepsHelper;
import com.elakov.rangiffler.page.BasePage;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.$;
import static com.elakov.rangiffler.helper.allure.AllureAttachHelper.addStepParameter;
import static com.elakov.rangiffler.helper.allure.AllureStepHelper.step;

public class RegistrationPage extends BasePage<RegistrationPage> {
    private final SelenideElement header = $(".form__header");
    private final SelenideElement usernameInput = $("input[name='username']");
    private final SelenideElement passwordInput = $("input[name='password']");
    private final SelenideElement passwordSubmitInput = $("input[name='passwordSubmit']");
    private final SelenideElement submitBtn = $("button[type='submit']");
    private final SelenideElement redirect = $("a[href*='redirect']");
    private final SelenideElement formError = $(".form__error");
    private final SelenideElement formErrorUsername = $(".form__label:nth-child(4) > .form__error");
    private final SelenideElement formErrorPassword = $(".form__label:nth-child(5) > .form__error");

    @Override
    public RegistrationPage checkThatPageLoaded() {
        AllureSoftStepsHelper softstep = new AllureSoftStepsHelper();
        return step("Check the 'Registration page' was loaded", () -> {

            softstep.add("Check that header visible", () -> header.shouldHave(text("Register to Rangiffler")));
            softstep.add("Check username input visible", () -> usernameInput.should(visible));
            softstep.add("Check password input visible", () -> passwordInput.should(visible));
            softstep.add("Check password submit input visible", () -> passwordSubmitInput.should(visible));

            softstep.execute();
            return this;
        });
    }

    @Step("Input username")
    public RegistrationPage inputUsername(String username) {
        addStepParameter("USERNAME", username);
        usernameInput.val(username);
        return this;
    }

    @Step("Input password")
    public RegistrationPage inputPassword(String password) {
        addStepParameter("PASSWORD", password);
        passwordInput.val(password);
        return this;
    }

    @Step("Submit password")
    public RegistrationPage inputPasswordSubmit(String password) {
        addStepParameter("PASSWORD", password);
        passwordSubmitInput.val(password);
        return this;
    }

    @Step("Click to 'SIGN UP'")
    public <T> T signUpClick(T page) {
        submitBtn.click();
        return (T) page;
    }

    @Step("Check the 'Registration form' should have error message")
    public RegistrationPage errorMessage(String expectedMessage) {
        formError.shouldHave(exactText(expectedMessage));
        return this;
    }

    @Step("Check the 'Registration' should have username error message")
    public RegistrationPage usernameErrorMessage(String expectedMessage) {
        formErrorUsername.shouldHave(exactText(expectedMessage));
        return this;
    }

    @Step("Check the 'Registration' should have username error message")
    public RegistrationPage passwordErrorMessage(String expectedMessage) {
        formErrorPassword.shouldHave(exactText(expectedMessage));
        return this;
    }

}
