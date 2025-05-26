package model;

import lombok.Getter;

import java.util.UUID;

@Getter
public enum Roles {
    ADMIN(UUID.fromString("00000000-0000-0000-0000-000000000000")),
    USER(UUID.fromString("82e4981e-388d-11f0-a6bb-75470132f538"));

    private UUID id;

    Roles(UUID id) {
        this.id = id;
    }

    public static Roles getById(UUID id) {
        for (Roles role : values()) {
            UUID roleId = role.getId();
            if (roleId != null && roleId.equals(id)) {
                return role;
            }
        }
        return null;
    }

    public static void main(String[] args) {
        System.out.println(String.valueOf(Roles.USER.getId()));
    }
}
