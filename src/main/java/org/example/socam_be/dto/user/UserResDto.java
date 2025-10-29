package org.example.socam_be.dto.user;

import lombok.Getter;
import org.example.socam_be.domain.user.ApprovalStatus;
import org.example.socam_be.domain.user.Role;
import org.example.socam_be.domain.user.User;

@Getter
public class UserResDto {
    private Long id;
    private String email;
    private String name;
    private String nickname;
    private Role role;
    private boolean isApproved;
    private boolean locked;
    private ApprovalStatus approvalStatus;
    private String orgName;
    private String contact;
    private String certificatePath;

    public UserResDto(User user) {
        this.id = user.getId();
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
