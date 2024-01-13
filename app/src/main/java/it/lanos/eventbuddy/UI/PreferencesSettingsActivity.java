package it.lanos.eventbuddy.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NavUtils;
import androidx.core.os.LocaleListCompat;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.materialswitch.MaterialSwitch;

import it.lanos.eventbuddy.R;

public class PreferencesSettingsActivity extends AppCompatActivity {
    private Spinner languageSpinner;
    private MaterialSwitch themeSwitch;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences_settings);
        MaterialToolbar toolbar = findViewById(R.id.toolbarPreferencesSettings);

        // FInd views
        languageSpinner = findViewById(R.id.spinner);
        themeSwitch = findViewById(R.id.themeSwitch);

        toolbar.setNavigationOnClickListener(v ->
                NavUtils.navigateUpFromSameTask(PreferencesSettingsActivity.this));

        // Set the correct theme
        initTheme();


        // Set the language the user chose when the activity is loaded
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        int languagePosition = sharedPreferences.getInt("languagePosition", 0);
        languageSpinner.setSelection(languagePosition);

        // Change language using the spinner
        languageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("languagePosition", position);
                editor.apply();

                String selectedLanguage = languageSpinner.getSelectedItem().toString();
                if (selectedLanguage.equals("Italiano")) {
                    LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("it");
                    AppCompatDelegate.setApplicationLocales(appLocale);

                } else if (selectedLanguage.equals("English")) {
                    LocaleListCompat appLocale = LocaleListCompat.forLanguageTags("en");
                    AppCompatDelegate.setApplicationLocales(appLocale);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Non fare nulla quando l'utente non seleziona nulla
            }
        });
    }

    // Read the
    private void initTheme() {
        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        boolean isDarkThemeEnabled = sharedPreferences.getBoolean("isDarkThemeEnabled", false);
        themeSwitch.setChecked(isDarkThemeEnabled);

        themeSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("isDarkThemeEnabled", isChecked);
            editor.apply();
            setAppTheme(isChecked);
        });

        setAppTheme(isDarkThemeEnabled);
    }


    public static void setAppTheme(boolean isDarkThemeEnabled) {
        if (isDarkThemeEnabled) {
            // Dark theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        } else {
            // White theme
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }
    }
}