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

import java.util.Locale;

import it.lanos.eventbuddy.R;

public class PreferencesSettingsActivity extends AppCompatActivity {
    private Spinner languageSpinner;
    private SharedPreferences sharedPreferences;
    private Locale myLocale;
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

        sharedPreferences = getSharedPreferences("Settings", MODE_PRIVATE);
        languageSpinner = findViewById(R.id.spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.languages));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        languageSpinner.setAdapter(adapter);

        int languagePosition = sharedPreferences.getInt("languagePosition", 0);
        languageSpinner.setSelection(languagePosition);

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
}