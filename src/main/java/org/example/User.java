package org.example;

import java.util.Objects;

class User {
    private String username;
    private String password;
    private Wallet wallet;

    public User(String username, String password, Wallet wallet) {
        this.username = username;
        this.password = password;
        this.wallet = wallet;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public Wallet getWallet() {
        return wallet;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(username, user.username) && Objects.equals(password, user.password) &&
                Objects.equals(wallet, user.wallet);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, password, wallet);
    }
}
