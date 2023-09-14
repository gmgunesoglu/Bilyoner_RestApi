package com.softtech.gateway.kafka;

public class JwtCacheDto {
    private String jwt;
    private String role;
    private String userName;

    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return "JwtCacheDto{" +
                "jwt='" + jwt + '\'' +
                ", role='" + role + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }
}

