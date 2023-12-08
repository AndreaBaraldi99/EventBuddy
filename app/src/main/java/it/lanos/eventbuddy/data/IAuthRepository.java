package it.lanos.eventbuddy.data;

public interface IAuthRepository {
    void signIn(String email, String password);
    void register(String email, String password);


}
