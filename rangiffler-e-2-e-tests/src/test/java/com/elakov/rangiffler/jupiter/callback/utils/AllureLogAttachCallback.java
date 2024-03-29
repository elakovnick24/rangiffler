package com.elakov.rangiffler.jupiter.callback.utils;

import com.elakov.rangiffler.listener.AllureLogAppender;
import io.qameta.allure.Allure;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;

public class AllureLogAttachCallback implements BeforeEachCallback, AfterEachCallback {

    @Override
    public void afterEach(ExtensionContext extensionContext) {
        String log = AllureLogAppender.getInstace().getLog();
        AllureLogAppender.getInstace().clearLog();
        Allure.addAttachment("Logs", "text/html", log, "html");
    }

    @Override
    public void beforeEach(ExtensionContext extensionContext) {
        AllureLogAppender.getInstace().clearLog();
    }

}