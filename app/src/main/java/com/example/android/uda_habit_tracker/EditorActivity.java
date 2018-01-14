package com.example.android.uda_habit_tracker;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.android.uda_habit_tracker.HabitDbHelper;
import com.example.android.uda_habit_tracker.HabitContract.HabitEntry;


/**
 * Allows user to create a new habit or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {
    private EditText mNameEditText;


    private EditText mPlaceEditText;

    private EditText mDurationEditText;

    private Spinner mNumberSpinner;


    private String mNumber = HabitEntry.NUMBER_SINGLE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mPlaceEditText = (EditText) findViewById(R.id.edit_place);
        mDurationEditText = (EditText) findViewById(R.id.edit_duration);
        mNumberSpinner = (Spinner) findViewById(R.id.spinner_number);

        setupSpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the number of people participating in the habit.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter numberSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_number_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        numberSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mNumberSpinner.setAdapter(numberSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mNumberSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.depends))) {
                        mNumber = HabitEntry.NUMBER_DEPENDS;
                    } else if (selection.equals(getString(R.string.group))) {
                        mNumber = HabitEntry.NUMBER_GROUP;
                    } else {
                        mNumber = HabitEntry.NUMBER_SINGLE;
                    }
                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mNumber = HabitEntry.NUMBER_SINGLE;
            }
        });
    }

    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String placeString = mPlaceEditText.getText().toString().trim();
        String durationString = mDurationEditText.getText().toString().trim();
        int duration = Integer.parseInt(durationString);

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habit attributes from the editor are the values.
        ContentValues values = new ContentValues();
        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_PLACE, placeString);
        values.put(HabitEntry.COLUMN_HABIT_NUMBER, mNumber);
        values.put(HabitEntry.COLUMN_HABIT_DURATION, duration);

        // Insert a new row for habit in the database, returning the ID of that new row.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id: " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                // Save habit to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            // Respond to a click on the "Delete" menu option
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case android.R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}