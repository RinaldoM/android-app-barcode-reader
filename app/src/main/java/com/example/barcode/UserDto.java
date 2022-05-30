package com.example.barcode;

public class UserDto {
    private final String id;
    private final String userName;

    public UserDto(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }

    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    @Override
    public String toString() {
        return userName;
    }
}
