package com.vayam.ichr.dto;

import java.util.List;

public class UserListResponse {
    private List<UserData> users;
    private long totalElements;

    // Getters and Setters
    public List<UserData> getUsers() { return users; }
    public void setUsers(List<UserData> users) { this.users = users; }

    public long getTotalElements() { return totalElements; }
    public void setTotalElements(long totalElements) { this.totalElements = totalElements; }
}
