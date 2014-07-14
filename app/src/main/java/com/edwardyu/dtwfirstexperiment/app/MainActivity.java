package com.edwardyu.dtwfirstexperiment.app;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RadioButton;
import android.widget.TextView;

import net.sf.javaml.distance.fastdtw.dtw.DTW;
import net.sf.javaml.distance.fastdtw.timeseries.TimeSeries;
import net.sf.javaml.distance.fastdtw.timeseries.TimeSeriesPoint;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.TreeSet;


public class MainActivity extends ActionBarActivity {

    private static String TAG = "DTW1stExp-MainActivity";

    private Hashtable<String, TimeSeries> hashtable;

    private RadioButton gesture1TemplateButton;
    private RadioButton gesture2TemplateButton;
    private RadioButton gesture3TemplateButton;

    private RadioButton gesture1Sample1Button;
    private RadioButton gesture1Sample2Button;
    private RadioButton gesture1Sample3Button;
    private RadioButton gesture1Sample4Button;
    private RadioButton gesture2Sample1Button;
    private RadioButton gesture2Sample2Button;
    private RadioButton gesture2Sample3Button;
    private RadioButton gesture2Sample4Button;
    private RadioButton gesture3Sample1Button;
    private RadioButton gesture3Sample2Button;
    private RadioButton gesture3Sample3Button;
    private RadioButton gesture3Sample4Button;
    private TextView answerText;


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

                hashtable = new Hashtable<String, TimeSeries>();

                // loop over all runs, for each run
                for (String description: accelDescriptions) {

                    // request the data for the run
                    JSONArray jsonArray = Network.sendSQLExpectJSONArray(
                            "SELECT * FROM `asensor` WHERE `description` = \""+ description +"\";\n");
                    Log.i(TAG, "Logging " + description);

                    // make sure there is data for the run
                    if (jsonArray != null) {
                        try {

                            // create list for run which contains entries
                            ArrayList<TimeDataContainer> list = new ArrayList<TimeDataContainer>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                // pick out the JSONObject entry
                                JSONObject obj = jsonArray.getJSONObject(i);
                                // create a container and add it to the list
                                TimeDataContainer container = new TimeDataContainer(obj);
                                list.add(container);
                            }

                            list = new ArrayList<TimeDataContainer>(new TreeSet<TimeDataContainer>(list));

                            // sort the list for the run
                            Collections.sort(list);

//                            for (TimeDataContainer container : list) {
//                                Log.d(TAG, container.toString());
//                            }

//                            for (int i  = 1; i < list.size(); i++) {
//                                if (list.get(i).compareTo(list.get(i-1)) < 0) {
//                                    Log.d(TAG, "i:" + list.get(i) + " i-1:" + list.get(i-1) + "i="+ i);
//                                }
//                            }

                            // loop over the run's list to add data to TimeSeries for Java ML
                            TimeSeries series = new TimeSeries(3);
                            for (TimeDataContainer container : list) {
                                Log.d(TAG, "Adding " + container + " to TimeSeries");
                                series.addLast(container.getTime(), new TimeSeriesPoint(container.getData()));
                            }
                            hashtable.put(description, series);

                        } catch (JSONException e) {
                            Log.w(TAG, "Exception: " + e.getMessage());
                        }
                    } else {
                        Log.w(TAG, "Array not returned.");

                    }

                }

                Log.i(TAG, "Finished loading data");

            }
        }.start();


        gesture1TemplateButton = (RadioButton) findViewById(R.id.gesture1TemplateButton);
        gesture2TemplateButton = (RadioButton) findViewById(R.id.gesture2TemplateButton);
        gesture3TemplateButton = (RadioButton) findViewById(R.id.gesture3TemplateButton);

        gesture1Sample1Button  = (RadioButton) findViewById(R.id.gesture1Sample1Button);
        gesture1Sample2Button  = (RadioButton) findViewById(R.id.gesture1Sample2Button);
        gesture1Sample3Button  = (RadioButton) findViewById(R.id.gesture1Sample3Button);
        gesture1Sample4Button  = (RadioButton) findViewById(R.id.gesture1Sample4Button);
        gesture2Sample1Button  = (RadioButton) findViewById(R.id.gesture2Sample1Button);
        gesture2Sample2Button  = (RadioButton) findViewById(R.id.gesture2Sample2Button);
        gesture2Sample3Button  = (RadioButton) findViewById(R.id.gesture2Sample3Button);
        gesture2Sample4Button  = (RadioButton) findViewById(R.id.gesture2Sample4Button);
        gesture3Sample1Button  = (RadioButton) findViewById(R.id.gesture3Sample1Button);
        gesture3Sample2Button  = (RadioButton) findViewById(R.id.gesture3Sample2Button);
        gesture3Sample3Button  = (RadioButton) findViewById(R.id.gesture3Sample3Button);
        gesture3Sample4Button  = (RadioButton) findViewById(R.id.gesture3Sample4Button);
        answerText             = (TextView) findViewById(R.id.answerText);
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

    public void calculate(View view) {
        TimeSeries template, sample;

        if (gesture1TemplateButton.isChecked()){
            template = hashtable.get("gesture1TemplateAccel");
        } else if (gesture2TemplateButton.isChecked()){
            template = hashtable.get("gesture2TemplateAccel");
        } else if (gesture3TemplateButton.isChecked()){
            template = hashtable.get("gesture3TemplateAccel");
        } else {
            template = hashtable.get("gesture1TemplateAccel");
        }

        if (gesture1Sample1Button.isChecked()){
            sample = hashtable.get("gesture1Sample1Accel");
        } else if (gesture1Sample2Button.isChecked()){
            sample = hashtable.get("gesture1Sample2Accel");
        } else if (gesture1Sample3Button.isChecked()){
            sample = hashtable.get("gesture1Sample3Accel");
        } else if (gesture1Sample4Button.isChecked()){
            sample = hashtable.get("gesture1Sample4Accel");
        } else if (gesture2Sample1Button.isChecked()){
            sample = hashtable.get("gesture2Sample1Accel");
        } else if (gesture2Sample2Button.isChecked()){
            sample = hashtable.get("gesture2Sample2Accel");
        } else if (gesture2Sample3Button.isChecked()){
            sample = hashtable.get("gesture2Sample3Accel");
        } else if (gesture2Sample4Button.isChecked()){
            sample = hashtable.get("gesture2Sample4Accel");
        } else if (gesture3Sample1Button.isChecked()){
            sample = hashtable.get("gesture3Sample1Accel");
        } else if (gesture3Sample2Button.isChecked()){
            sample = hashtable.get("gesture3Sample2Accel");
        } else if (gesture3Sample3Button.isChecked()){
            sample = hashtable.get("gesture3Sample3Accel");
        } else if (gesture3Sample4Button.isChecked()){
            sample = hashtable.get("gesture3Sample4Accel");
        } else {
            sample = hashtable.get("gesture1Sample1Accel");
        }

        String answer = "Answer: " + DTW.getWarpDistBetween(template, sample);
        answerText.setText(answer);
    }

}
