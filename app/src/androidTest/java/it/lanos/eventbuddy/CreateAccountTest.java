package it.lanos.eventbuddy;


import org.junit.runner.RunWith;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import it.lanos.eventbuddy.data.UserRepository;
import it.lanos.eventbuddy.data.source.firebase.auth.UserDataSource;

@RunWith(AndroidJUnit4.class)
public class CreateAccountTest {
    private final  String TAG = CreateAccountTest.class.getSimpleName();
    UserRepository authRepository;
    UserDataSource authDataSource;


}
