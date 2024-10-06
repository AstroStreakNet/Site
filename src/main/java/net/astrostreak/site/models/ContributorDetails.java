package net.astrostreak.site.models;

import org.apache.commons.validator.EmailValidator;

import java.util.regex.Pattern;

public class ContributorDetails {
    private String username;
    private String email;
    private String password;

    public ContributorDetails(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public boolean isValid() {
        if (username == null || email == null || password == null) {
            return false;
        }
        var regex = "^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";
        var pattern = Pattern.compile(regex);
        var matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
