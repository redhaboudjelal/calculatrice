package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    EditText username, password;
    Button login;
    ProgressDialog progressDialog;
    RequestQueue requestQueue;

    private static final String LOGIN_URL = "https://trincal.alwaysdata.net/api/login.php";
    private static final String STATUS = "status";
    private static final int SUCCESS_STATUS = 200;

    public static final String EXTRA_MESSAGE = "com.example.myapplication.extra.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        login = findViewById(R.id.login);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Logging in... Please wait.");
        progressDialog.setCancelable(false);
        requestQueue = Volley.newRequestQueue(this);
        login.setOnClickListener(v -> {
            String userVar = username.getText().toString();
            String passVar = password.getText().toString();
            if (userVar.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Username cannot be blank",
                        Toast.LENGTH_SHORT).show();
            } else if (passVar.isEmpty()) {
                Toast.makeText(getApplicationContext(), "Password cannot be blank",
                        Toast.LENGTH_SHORT).show();
            } else {
                loginRequest(userVar, passVar);
            }
        });
    }

    private void loginRequest(String userVar, String passVar) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    progressDialog.dismiss();
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        if (jsonObject.has(STATUS) && jsonObject.getInt(STATUS) == SUCCESS_STATUS) {
                            Toast.makeText(MainActivity.this, "Login successful", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, PostLoginActivity.class);

                            // Extract user data from the response
                            String utilisateur = jsonObject.toString();
                            // String numero = utilisateur.getString("numero");
                            //String adresse = utilisateur.getString("adresse");
                            //String email = utilisateur.getString("email");

                            // Pass user data to PostLoginActivity
                            //  intent.putExtra("numero", numero);
                            // intent.putExtra("adresse", adresse);
                            intent.putExtra(EXTRA_MESSAGE, utilisateur);

                            startActivity(intent);
                        } else {
                            Toast.makeText(MainActivity.this, "Incorrect username or password",
                                    Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(MainActivity.this, "Failed to parse server response",
                                Toast.LENGTH_LONG).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Failed to connect to server. Please check your internet connection and try again.", Toast.LENGTH_LONG).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("email", userVar);
                params.put("password", passVar);
                return params;
            }
        };
        progressDialog.show();
        requestQueue.add(stringRequest);
    }
}