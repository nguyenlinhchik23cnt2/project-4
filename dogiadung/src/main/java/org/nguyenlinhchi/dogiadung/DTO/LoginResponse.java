package org.nguyenlinhchi.dogiadung.DTO;

public class LoginResponse {
    private Integer userId;
    private String username;
    private String fullName;
    private String role;

    public LoginResponse(Integer userId, String username, String fullName, String role) {
        this.userId = userId;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
    }

    // Getters
    public Integer getUserId() { return userId; }
    public String getUsername() { return username; }
    public String getFullName() { return fullName; }
    public String getRole() { return role; }
}