package com.b361asd.auction.gui;

public enum UserType {
    ADMIN("1", 1),
    REP("2", 2),
    USER("3", 3);

    private final String sessionUserType;
    private final int databaseUserType;

    UserType(String sessionUserType, int databaseUserType) {
        this.sessionUserType = sessionUserType;
        this.databaseUserType = databaseUserType;
    }

    public String getSessionUserType() {
        return sessionUserType;
    }

    public int getDatabaseUserType() {
        return databaseUserType;
    }
}
