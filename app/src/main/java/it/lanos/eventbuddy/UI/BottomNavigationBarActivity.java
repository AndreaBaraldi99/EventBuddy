package it.lanos.eventbuddy.UI;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.ViewGroup;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.util.ServiceLocator;

public class BottomNavigationBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);

        IUserRepository iUserRepository = ServiceLocator.getInstance().getUserRepository(getApplication());

        iUserRepository.signIn("test@eventbuddy.it", "eventbuddy1");
    }


}
