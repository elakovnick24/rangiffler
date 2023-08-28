package com.elakov.rangiffler.test;

public final class TestContext {

    private static final ThreadLocal<String> username = new ThreadLocal<>();
    private static final ThreadLocal<String> password = new ThreadLocal<>();

    private TestContext() {
    }

    public static String getUsername() {
        return username.get();
    }

    public static void setUsername(String testUsername) {
        username.set(testUsername);
    }

    public static String getPassword() {
        return password.get();
    }

    public static void setPassword(String pass) {
        password.set(pass);
    }

}