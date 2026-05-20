package org.nguyenlinhchi.dogiadung.DTO;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserDTO {
    private Integer userId;
    private String username;
    private String fullName;
    private String email;
    private String phone;
    private String role;
    private String status;
    private String avatarUrl;
}
