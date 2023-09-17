package com.example.agefromname;

import androidx.appcompat.app.AppCompatActivity;
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
    public String age;

    final String url = "https://api.agify.io?name=";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextName = findViewById(R.id.editTextName);
        buttonGreet = findViewById(R.id.buttonGreet);
        textViewGreeting = findViewById(R.id.textViewGreeting);

        buttonGreet.setOnClickListener(v -> {
            String name = editTextName.getText().toString();
            getData(name);
        });
    }

    public void getData(String name) {
        RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url + name,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject apiResult = new JSONObject(response);
                            age = String.valueOf(apiResult.getInt("age"));
                            updateGreeting(name, age);
                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("api", "onErrorResponse" + error.getLocalizedMessage());
                    }
        });

// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void updateGreeting(String name, String age) {
        String greeting = "Hi " + name + "! You are " + age;
        textViewGreeting.setText(greeting);
    }

}