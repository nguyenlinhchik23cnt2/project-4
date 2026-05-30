package org.nguyenlinhchi.dogiadung.ENTITY;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Entity khách hàng — liên kết với chi_orderItem qua bảng Order.
 * Hiện tại dùng mối quan hệ đơn giản qua orderId.
 */
@Entity
@Table(name = "nga_customers")
public class NgaCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 60)
    private String username;

    @Column(nullable = false)
    private String password;   // Lưu hash (BCrypt)

    @Column(name = "full_name")
    private String fullName;

    @Column(unique = true, length = 100)
    private String email;

    @Column(length = 20)
    private String phone;

    @Column(length = 300)
    private String address;

    private String gender;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    private Date dob;

    @Column(name = "created_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

    @Column(length = 20)
    private String role = "CUSTOMER";   // CUSTOMER | STAFF | ADMIN

    // ───── Mối quan hệ: 1 Customer → nhiều Order (không load mặc định)
    // Nếu dự án có entity chi_order, bỏ comment đoạn dưới:
    // @OneToMany(mappedBy = "customer", fetch = FetchType.LAZY)
    // @JsonIgnore
    // private List<chi_order> orders;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = new Date();
    }

    // ───── Constructors ─────
    public NgaCustomer() {}

    // ───── Getters & Setters ─────
    public Integer getId()                  { return id; }
    public void    setId(Integer id)        { this.id = id; }

    public String  getUsername()            { return username; }
    public void    setUsername(String v)    { this.username = v; }

    public String  getPassword()            { return password; }
    public void    setPassword(String v)    { this.password = v; }

    public String  getFullName()            { return fullName; }
    public void    setFullName(String v)    { this.fullName = v; }

    public String  getEmail()               { return email; }
    public void    setEmail(String v)       { this.email = v; }

    public String  getPhone()               { return phone; }
    public void    setPhone(String v)       { this.phone = v; }

    public String  getAddress()             { return address; }
    public void    setAddress(String v)     { this.address = v; }

    public String  getGender()              { return gender; }
    public void    setGender(String v)      { this.gender = v; }

    public Date    getDob()                 { return dob; }
    public void    setDob(Date v)           { this.dob = v; }

    public Date    getCreatedAt()           { return createdAt; }
    public void    setCreatedAt(Date v)     { this.createdAt = v; }

    public String  getRole()               { return role; }
    public void    setRole(String v)       { this.role = v; }
}