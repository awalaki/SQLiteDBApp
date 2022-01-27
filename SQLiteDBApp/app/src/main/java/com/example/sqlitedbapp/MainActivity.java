package com.example.sqlitedbapp;

import android.app.Activity;
import android.os.Bundle;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends Activity implements OnClickListener {

    EditText etStdntID, etStdntName, etStdntProg;
    Button btAdd, btDelete, btSearch, btView;
    SQLiteDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etStdntID = (EditText)findViewById(R.id.etStdntID);
        etStdntName = (EditText)findViewById(R.id.etStdntName);
        etStdntProg = (EditText)findViewById(R.id.etStdntProg);

        btAdd = (Button)findViewById(R.id.btAdd);
        btDelete = (Button)findViewById(R.id.btDelete);
        btSearch = (Button)findViewById(R.id.btSearch);
        btView = (Button)findViewById(R.id.btView);

        db = openOrCreateDatabase("StudentDB", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS student(stdnt_id VARCHAR, stdnt_name VARCHAR, stdnt_prog VARCHAR)");
    }

    private void clearText() {
        etStdntID.setText("");
        etStdntName.setText("");
        etStdntProg.setText("");
        etStdntID.requestFocus();
    }

    private void showMessage(String title, String message) {
        Builder builder = new Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    public void onClick(View view) {
        // For the Add button
        if (view == btAdd) {
            db.execSQL("INSERT INTO student VALUES('" + etStdntID.getText() + "', '" + etStdntName.getText() + "', '" + etStdntProg.getText() + "');");
            showMessage("Success", "Record added.");
            clearText();
        }
        // For the Delete button
        else if (view == btDelete) {
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStdntID.getText() + "'", null);
            if (c.moveToFirst()) {
                db.execSQL("DELETE FROM student WHERE stdnt_id='" + etStdntID.getText() + "'");
                showMessage("Success", "Record deleted.");
            }
            clearText();
        }
        // For the Search button
        else if (view == btSearch) {
            Cursor c = db.rawQuery("SELECT * FROM student WHERE stdnt_id='" + etStdntID.getText() + "'", null);
            StringBuffer buffer = new StringBuffer();
            if (c.moveToFirst()) {
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program: " + c.getString(2) + "\n");
            }
            // Displaying all records
            showMessage("Student Details", buffer.toString());
        }
        // For the View All button
        else if (view == btView) {
            // Retrieving all records
            Cursor c = db.rawQuery("SELECT * FROM student", null);
            // Checking if no records found
            if (c.getCount() == 0) {
                showMessage("Error", "No records found.");
                return;
            }
            // Appending records to a string buffer
            StringBuffer buffer = new StringBuffer();
            while (c.moveToNext()) {
                buffer.append("ID: " + c.getString(0) + "\n");
                buffer.append("Name: " + c.getString(1) + "\n");
                buffer.append("Program: " + c.getString(2) + "\n");
            }
            // Displaying all records
            showMessage("Show details", buffer.toString());
        }
    }
}