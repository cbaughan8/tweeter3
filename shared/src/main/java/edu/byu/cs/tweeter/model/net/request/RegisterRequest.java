package edu.byu.cs.tweeter.model.net.request;

public class RegisterRequest {
    private String username;
    private String password;
    private String alias;
    private String email;
    private String imageBytesBase64;

    private RegisterRequest() {}

    public RegisterRequest(String username, String password, String alias, String email, String imageBytesBase64) {
        this.username = username;
        this.password = password;
        this.alias = alias;
        this.email = email;
        this.imageBytesBase64 = imageBytesBase64;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageBytesBase64() {
        return imageBytesBase64;
    }

    public void setImageBytesBase64(String imageBytesBase64) {
        this.imageBytesBase64 = imageBytesBase64;
    }
}
