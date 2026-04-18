package org.example.university.builder;

import org.example.university.model.User;

/**
 * Builder для создания {@link User}.
 */
public class UserBuilder {

    private final User user;

    public UserBuilder() {
        this.user = new User();
    }

    public UserBuilder name(String name) {
        user.setName(name);
        return this;
    }

    public UserBuilder email(String email) {
        user.setEmail(email);
        return this;
    }

    public UserBuilder password(String password) {
        user.setPassword(password);
        return this;
    }

    public UserBuilder role(String role) {
        user.setRole(role);
        return this;
    }

    public UserBuilder enabled(boolean enabled) {
        user.setEnabled(enabled);
        return this;
    }

    public UserBuilder bio(String bio) {
        user.setBio(bio);
        return this;
    }

    public UserBuilder avatarUrl(String avatarUrl) {
        user.setAvatarUrl(avatarUrl);
        return this;
    }

    public UserBuilder phoneNumber(String phoneNumber) {
        user.setPhoneNumber(phoneNumber);
        return this;
    }

    public User build() {
        return user;
    }
}

