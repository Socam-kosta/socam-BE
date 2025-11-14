package org.example.socam_be.dto.user;

import lombok.Getter;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;

@Getter
public class UserResDto {

    private final String email;
    private final String name;
    private final String nickname;
    private final Role role;

    public UserResDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.role = user.getRole();
    }
}
