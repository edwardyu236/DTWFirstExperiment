package com.edwardyu.dtwfirstexperiment.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Hashtable;


public class MainActivity extends ActionBarActivity {
    private static String TAG = "DTW1stExp-MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread() {
            public void run() {

                String[] accelDescriptions = {
                    "gesture1TemplateAccel",
                    "gesture1Sample1Accel",
                    "gesture1Sample2Accel",
                    "gesture1Sample3Accel",
                    "gesture1Sample4Accel",
                    "gesture2TemplateAccel",
                    "gesture2Sample1Accel",
                    "gesture2Sample2Accel",
                    "gesture2Sample3Accel",
                    "gesture2Sample4Accel",
                    "gesture3TemplateAccel",
                    "gesture3Sample1Accel",
                    "gesture3Sample2Accel",
                    "gesture3Sample3Accel",
                    "gesture3Sample4Accel"

                };

                Hashtable<String, JSONArray> jsonArrayHashtable = new Hashtable<String, JSONArray>();
                for (String description: accelDescriptions) {
                    JSONArray jsonArray = Network.sendSQLExpectJSONArray(
                            "SELECT * FROM `asensor` WHERE `description` = \""+ description +"\";\n");
                    logJSONArray(description, jsonArray);
                    if (jsonArray != null) {
                        jsonArrayHashtable.put(description, jsonArray);
                    }
                }

            }

            private void logJSONArray(String description, JSONArray jsonArray) {
                Log.i(TAG, "Logging " + description);
                if (jsonArray != null) {
                    try {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject obj = jsonArray.getJSONObject(i);
                            Log.i(TAG, obj.toString());
                        }
                    } catch (JSONException e) {
                        Log.w(TAG, "Exception: " + e.getMessage());
                    }
                } else {
                    Log.w(TAG, "Array not returned.");
                }
            }
        }.start();

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
