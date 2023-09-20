package ru.netology.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import ru.netology.data.DataGenerator;

import java.time.Duration;


import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class CardDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @BeforeAll
    static void setUpAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    void shouldOrderCard() { //Успешная отправка формы
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysFirst = 3;
        String First = DataGenerator.generateDate(daysFirst);
        int daysSecond = 6;
        String Second = DataGenerator.generateDate(daysSecond);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.DELETE);
        $("[data-test-id=date] input").setValue(First);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $("[data-test-id=agreement").click();
        $(".button").click();
        $(withText("Успешно")).shouldHave(visible, Duration.ofSeconds(15));
        $("[data-test-id=success-notification] .notification__content")
                .shouldHave(exactText("Встреча успешно запланирована на " + First))
                .shouldBe(visible);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.DELETE);
        $("[data-test-id=date] input").setValue(Second);
        $(".button").click();
        $(withText("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $("[data-test-id=replan-notification] button").click();
        $("[data-test-id=success-notification] .notification__content").shouldHave(text("Встреча успешно запланирована на "+ Second));


    }

    @Test
    void shouldFormSubmissionWithoutCheckbox() { //следует отправить форму без флажка
        DataGenerator.UserInfo validUser = DataGenerator.Registration.generateUser("ru");
        int daysFirst = 3;
        String First = DataGenerator.generateDate(daysFirst);
        $("[data-test-id=city] input").setValue(validUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.DELETE);
        $("[data-test-id=date] input").setValue(First);
        $("[data-test-id=name] input").setValue(validUser.getName());
        $("[data-test-id=phone] input").setValue(validUser.getPhone());
        $(".button").click();
        $(By.cssSelector("[data-test-id=agreement].input_invalid")).shouldBe(visible);
    }

    @Test
    void shouldEnter1Numbers() { //следует ввести 1 цифру
        DataGenerator.UserInfo InvalidUser = DataGenerator.Registration.generateUser("ru");
        int daysFirst = 3;
        String First = DataGenerator.generateDate(daysFirst);
        $("[data-test-id=city] input").setValue(InvalidUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.DELETE);
        $("[data-test-id=date] input").setValue(First);
        $("[data-test-id=name] input").setValue(InvalidUser.getName());
        $("[data-test-id=phone] input").setValue("1");
        $("[data-test-id=agreement").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldEnter10Numbers() { //следует ввести 10 цифр
        DataGenerator.UserInfo InvalidUser = DataGenerator.Registration.generateUser("ru");
        int daysFirst = 3;
        String First = DataGenerator.generateDate(daysFirst);
        $("[data-test-id=city] input").setValue(InvalidUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.DELETE);
        $("[data-test-id=date] input").setValue(First);
        $("[data-test-id=name] input").setValue(InvalidUser.getName());
        $("[data-test-id=phone] input").setValue("1234567891");
        $("[data-test-id=agreement").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldEmptyFieldNumber() { //следует оставить поле пустым
        DataGenerator.UserInfo InvalidUser = DataGenerator.Registration.generateUser("ru");
        int daysFirst = 3;
        String First = DataGenerator.generateDate(daysFirst);
        $("[data-test-id=city] input").setValue(InvalidUser.getCity());
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.SHIFT,Keys.HOME),Keys.DELETE);
        $("[data-test-id=date] input").setValue(First);
        $("[data-test-id=name] input").setValue(InvalidUser.getName());
        $("[data-test-id=phone] input").setValue("");
        $("[data-test-id=agreement").click();
        $(".button").click();
        $("[data-test-id=phone].input_invalid .input__sub").shouldHave(exactText("Поле обязательно для заполнения"));
    }
}