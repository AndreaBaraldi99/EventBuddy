package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NavUtils;

import android.os.Bundle;
import android.view.View;

import com.google.android.material.appbar.MaterialToolbar;

import it.lanos.eventbuddy.R;

public class PreferencesSettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_settings);
        MaterialToolbar toolbar = findViewById(R.id.toolbarPreferencesSettings);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(PreferencesSettingsActivity.this);

            }
        });


    }
}