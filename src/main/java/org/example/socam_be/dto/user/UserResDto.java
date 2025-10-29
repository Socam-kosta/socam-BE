package org.example.socam_be.dto.user;

import lombok.Getter;
import org.example.socam_be.domain.user.ApprovalStatus;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;

@Getter
public class UserResDto {
    private final String email;
    private final String name;
    private final String nickname;
    private final Role role;
    private final boolean isApproved;
    private final boolean locked;
    private final ApprovalStatus approvalStatus;
    private final String orgName;
    private final String contact;
    private final String certificatePath;

    public UserResDto(User user) {
        this.email = user.getEmail();
        this.name = user.getName();
        this.nickname = user.getNickname();
        this.role = user.getRole();
        this.isApproved = user.isApproved();
        this.locked = user.isLocked();
        this.approvalStatus = user.getApprovalStatus();
        this.orgName = user.getOrgName();
        this.contact = user.getContact();
        this.certificatePath = user.getCertificatePath();
    }
}
