package br.com.ciceroednilson.repository.entity;

import java.util.Arrays;
import java.util.Optional;

public enum EnumRole {
    ROLE_USER("ROLE_USER"),
    ROLE_MODERATOR("ROLE_MODERATOR"),
    ROLE_ADMIN("ROLE_ADMIN");

    private String value;

    EnumRole(final String value) {
        this.value = value;
    }

    public static Optional<EnumRole> findByValue(final String value) {
        return Arrays.stream(EnumRole.values()).filter(f -> f.value.equals(value)).findFirst();
    }
}
