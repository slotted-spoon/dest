package com.slottedspoon.indoornav;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class EnterDestActivity extends AppCompatActivity {

    private AutoCompleteTextView editText;
    private String[] destinations;
    public static final String DESTINATION = "com.slottedspoon.indoornav.DESTINATION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_dest);

        destinations = getResources().getStringArray(R.array.acRooms);
        editText = (AutoCompleteTextView) findViewById(R.id.dest_input);
        makeDropdown();
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    public void showDropDown(View view) {
        editText.showDropDown();
    }

    public void inputDest(View view) {
        Intent i = new Intent(getApplicationContext(), RouteOptionsActivity.class);
        String dest = editText.getText().toString();
        i.putExtra(DESTINATION, dest);
        startActivity(i);
    }

    private void makeDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,android.R.layout.select_dialog_singlechoice, destinations);
        //Find TextView control
        AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.dest_input);
        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(0);
        //Set the adapter
        acTextView.setAdapter(adapter);
    }
}
