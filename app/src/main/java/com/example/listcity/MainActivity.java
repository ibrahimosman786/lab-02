package com.example.listcity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private ListView cityList;
    private ArrayAdapter<String> cityAdapter;
    private ArrayList<String> dataList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        // Header title
        setTitle("ListyCity");

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // ListView
        cityList = findViewById(R.id.city_list);

        // Initial cities
        String[] cities = {"Edmonton", "Vancouver", "Montreal"};
        dataList = new ArrayList<>(Arrays.asList(cities));

        // Adapter with MULTI-CHOICE rows ( I Added this as a feature )
        cityAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_multiple_choice,
                dataList
        );

        cityList.setAdapter(cityAdapter);

        // Enable multi-selection ( I Added this as a feature )
        cityList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // ADD CITY popup ( I Added this as a feature )
        findViewById(R.id.btn_add_city).setOnClickListener(v -> showAddCityDialog());

        // DELETE CITY (multi-delete) ( I added this to make user experience easier while deleting )
        findViewById(R.id.btn_delete_city).setOnClickListener(v -> deleteSelectedCities());
    }

    private void showAddCityDialog() {
        EditText input = new EditText(this);
        input.setHint("Enter city name");

        new AlertDialog.Builder(this)
                .setTitle("Add City")
                .setView(input)
                .setPositiveButton("CONFIRM", (dialog, which) -> {
                    String city = input.getText().toString().trim();

                    if (city.isEmpty()) {
                        Toast.makeText(this, "City name cannot be empty.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (dataList.contains(city)) {
                        Toast.makeText(this, "City already exists.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    dataList.add(city);
                    cityAdapter.notifyDataSetChanged();
                })
                .setNegativeButton("CANCEL", null)
                .show();
    }

    // Delete ALL selected cities at once
    private void deleteSelectedCities() {
        boolean deletedAny = false;

        for (int i = cityList.getCount() - 1; i >= 0; i--) {
            if (cityList.isItemChecked(i)) {
                dataList.remove(i);
                deletedAny = true;
            }
        }

        cityList.clearChoices();
        cityAdapter.notifyDataSetChanged();

        if (!deletedAny) {
            Toast.makeText(this, "Select one or more cities to delete.", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Selected cities deleted.", Toast.LENGTH_SHORT).show();
        }
    }
}
