package model;

public class Nga_Customer {
    private int customerId;
    private String fullName;
    private String phone;
    private String email;
    private String address;
    private String password;

    public Nga_Customer() {}

    // Constructor khong co password (dung o getAll, search)
    public Nga_Customer(int customerId, String fullName, String phone, String email, String address) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
    }

    // Constructor day du (dung o register neu can)
    public Nga_Customer(int customerId, String fullName, String phone, String email, String address, String password) {
        this.customerId = customerId;
        this.fullName = fullName;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.password = password;
    }

    public int getCustomerId() { return customerId; }
    public String getFullName() { return fullName; }
    public String getPhone() { return phone; }
    public String getEmail() { return email; }
    public String getAddress() { return address; }
    public String getPassword() { return password; }

    public void setCustomerId(int customerId) { this.customerId = customerId; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public void setPhone(String phone) { this.phone = phone; }
    public void setEmail(String email) { this.email = email; }
    public void setAddress(String address) { this.address = address; }
    public void setPassword(String password) { this.password = password; }
}