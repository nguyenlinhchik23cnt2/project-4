package org.nguyenlinhchi.dogiadung.ENTITY;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.*;
import java.util.Date;

@Entity
@Table(name = "nga_customers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NgaCustomer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "customer_id")
    private Integer customerId;

    @Column(name = "username", length = 60)
    private String username;

    @Column(name = "full_name", nullable = false, length = 150)
    private String fullName;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "email", length = 150)
    private String email;

    @Column(name = "address", length = 200)
    private String address;

    @Column(name = "dob")
    @Temporal(TemporalType.DATE)
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date dob;

    @Column(name = "gender", length = 10)
    private String gender;

    @Column(name = "created_at", updatable = false, insertable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
}