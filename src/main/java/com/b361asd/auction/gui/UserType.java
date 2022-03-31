package com.b361asd.auction.gui;

public enum UserType {
    ADMIN("1", 1, "admin"),
    REP("2", 2, "rep"),
    USER("3", 3, "user");

    private final String sessionUserType;
    private final int databaseUserType;
    private final String userTypePath;

    UserType(String sessionUserType, int databaseUserType, String userTypePath) {
        this.sessionUserType = sessionUserType;
        this.databaseUserType = databaseUserType;
        this.userTypePath = userTypePath;
    }

    public String getSessionUserType() {
        return sessionUserType;
    }

    public int getDatabaseUserType() {
        return databaseUserType;
    }

    public String getUserTypePath() {
        return userTypePath;
    }
}
