package com.example.agefromname;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {
    private EditText editTextName;
    private Button buttonGreet;
    private TextView textViewGreeting;
    private Button buttonShare;
    public String age;
    final String url = "https://api.agify.io?name=";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Basic code needed for App
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Connect the private fields to their respective UI components
        editTextName = findViewById(R.id.editTextName);
        buttonGreet = findViewById(R.id.buttonGreet);
        textViewGreeting = findViewById(R.id.textViewGreeting);
        buttonShare = findViewById(R.id.buttonShare);

        // Respond to user clicking the get age Button
        buttonGreet.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            getData(name);
        });

        // Respond to the user clicking the share button
        buttonShare.setOnClickListener(v -> {
            clickOnShare();
        });
    }


    /**
     * Sets the age field given a person's name. Fetches this age using an API call.
     * @param name - Name of the Person
     */
    public void getData(String name) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + name,
                response -> {
                    try {
                        JSONObject apiResult = new JSONObject(response);
                        age = String.valueOf(apiResult.getInt("age"));
                        updateGreeting(name, age);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }, error -> Log.e("api", "onErrorResponse" + error.getLocalizedMessage()));

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    /**
     * Updates the text greeting field with the person's name and corresponding age.
     * @param name - Name of the Person
     * @param age  - Age of the person according to the API
     */
    public void updateGreeting(String name, String age) {
        String greeting = "Hi " + name + "! You sound like you are " + age;
        textViewGreeting.setText(greeting);
        createShareButton();
    }


    /**
     * Creates a share button.
     */
    public void createShareButton() {
        buttonShare.setVisibility(View.VISIBLE);
    }


    /**
     * Allows the user to share their age.
     */
    public void clickOnShare() {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, "Turns out I am " + age + "years old?");
        sendIntent.setType("text/plain");

        Intent shareIntent = Intent.createChooser(sendIntent, null);
        startActivity(shareIntent);
    }
}