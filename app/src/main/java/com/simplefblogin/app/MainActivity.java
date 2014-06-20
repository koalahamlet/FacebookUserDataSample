package com.simplefblogin.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;

import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    TextView tvName;
    LoginButton loginButton;
    private Button logoutButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loginButton = (LoginButton) findViewById(R.id.view);
        tvName = (TextView) findViewById(R.id.textView);

        logoutButton = (Button) findViewById(R.id.logout);



        loginButton.setReadPermissions(Arrays.asList("user_location", "user_birthday", "user_id", "user_name"));

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Session.getActiveSession().closeAndClearTokenInformation();
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click", "facebook button was clicked");

                Session.openActiveSession(MainActivity.this, true, new Session.StatusCallback() {

                    // callback when session changes state
                    @Override
                    public void call(Session session, SessionState state, Exception exception) {
                        Log.d("Session info:", "session was attempted");
                        if (session.isOpened()) {
                            Log.d("Session info:", "session was opened");
                            // make request to the /me API

                            Request.newMeRequest(session, new Request.GraphUserCallback() {

                                // callback after Graph API response with user object
                                @Override
                                public void onCompleted(GraphUser user, Response response) {
                                    if (user != null) {

                                        tvName.setText(user.getName());
                                        Log.d("User info:",user.getName() );
                                        Log.d("User info:",user.getId() );
                                        Log.d("User info:", user.getLink());
                                        Log.d("Yes!", "we got the user");

                                    } else {

                                        Toast.makeText(MainActivity.this, "Could not get user", Toast.LENGTH_SHORT).show();
                                        Log.d("Noooo!", "user was null...");
                                        Log.d("Noooo!", response.getError().toString());


                                    }
                                }
                            }).executeAsync();
                        } else if (session.isClosed()) {
                            Log.d("Session info:", "session was not opened");
                            Toast.makeText(MainActivity.this, "Unable to reach Facebook", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Session.getActiveSession().onActivityResult(this, requestCode, resultCode, data);
    }
}
