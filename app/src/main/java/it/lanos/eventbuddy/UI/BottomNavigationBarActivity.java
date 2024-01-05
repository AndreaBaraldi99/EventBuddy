package it.lanos.eventbuddy.UI;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.os.Bundle;
import android.view.ViewGroup;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.Objects;

import it.lanos.eventbuddy.R;
import it.lanos.eventbuddy.data.IEventsRepository;
import it.lanos.eventbuddy.data.IUserRepository;
import it.lanos.eventbuddy.data.source.models.Event;
import it.lanos.eventbuddy.util.ServiceLocator;

public class BottomNavigationBarActivity extends AppCompatActivity {

    private EventViewModel eventViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EdgeToEdge.enable(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);

        IEventsRepository iEventsRepository =
                ServiceLocator.getInstance().getEventsRepository(getApplication());

        eventViewModel = new ViewModelProvider(
                this,
                new EventViewModelFactory(iEventsRepository)).get(EventViewModel.class);

        NavHostFragment navHostFragment = (NavHostFragment) getSupportFragmentManager().
                findFragmentById(R.id.fragment_container_view);
        NavController navController = navHostFragment.getNavController();
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);

        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.eventFragment, R.id.activeFragment,
                R.id.friendsFragment, R.id.settingsFragment).build();

        // For the BottomNavigationView
        NavigationUI.setupWithNavController(bottomNav, navController);


    }


}
