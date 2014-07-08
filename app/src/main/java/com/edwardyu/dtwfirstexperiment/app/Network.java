package com.edwardyu.dtwfirstexperiment.app;

import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by edwardyu on 08/07/14.
 */

public class Network {


    private static String httpRequest(int indicator, String url, List<NameValuePair> param) {
        String response = "";
        HttpPost post;
        HttpGet get;

        try {
            DefaultHttpClient client = new DefaultHttpClient();
            Log.d("url", "url=" + url);
            HttpResponse httpResponse = null;
            if (indicator == 0) {
                post = new HttpPost(url);
                if (param != null)
                    post.setEntity(new UrlEncodedFormEntity(param, "UTF-8"));
                httpResponse = client.execute(post);
            } else if (indicator == 1) {
                get = new HttpGet(url);
                httpResponse = client.execute(get);
            }

            if (httpResponse.getStatusLine().getStatusCode() == 200)
                response = EntityUtils.toString(httpResponse.getEntity());

        } catch (Exception e) {
            e.printStackTrace();
        }
        if (response.startsWith("\uFEFF"))
            response = response.substring(1);
        return response;
    }


    public static String addToAccelerometerDatabase(String systemTime, String x, String y, String z, String humanTime, String initialTime) {
        return sendSQL("INSERT INTO asensor (st, x, y, z, ht, description)\n" +
                "VALUES ('" + systemTime + "','"
                + x + "','"
                + y + "','"
                + z + "','"
                + humanTime + "','Accelerometer Reading Beginning At " + initialTime + "');");
    }

    public static String addToGyroscopeDatabase(String systemTime, String x, String y, String z, String humanTime, String initialTime) {
        return sendSQL("INSERT INTO gsensor (st, x, y, z, ht, description)\n" +
                "VALUES ('" + systemTime + "','"
                + x + "','"
                + y + "','"
                + z + "','"
                + humanTime + "','Gyroscope Reading Beginning At " + initialTime + "');");
    }

    public static String sendSQL(String sqlCommand) {
        JSONObject jObject;
        String response;
        String code, errMsg;
        String result = "";
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sql", sqlCommand));


        response = httpRequest(0, "http://cms.draggablemedia.com/ust/selectSensor.php", params);
        Log.i("url", "sendsql response=" + response);
        if (response.length() > 0) {
            try {
//                jObject = new JSONObject(response);
//                code = jObject.getString("code"); //success = 0 , fail = 1
//                if (code.equals("1")) {
//                    Log.e("url", "sendsql Error, Code = " + code);
//                }
                JSONArray jsonArray = new JSONArray(response);
                Log.d("url", "survived till here");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);
                    Log.i("url", "id: " + obj.getString("id"));
                    Log.i("url", obj.toString());
                }
            } catch (Exception e) {
//                if(Constants.LOG) e.printStackTrace();
                jObject = null;
                Log.e("url", "Error: " + e.getMessage());
                return "Error:\n" + response;
            }
        } else {
            Log.e("url", "sendsql Error Connecting.");
        }
        jObject = null;
        response = null;
        return result;
    }

    public static JSONArray sendSQLExpectJSONArray(String sqlCommand) {
        String response;
        JSONArray jsonArray = null;

        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("sql", sqlCommand));

        response = httpRequest(0, "http://cms.draggablemedia.com/ust/selectSensor.php", params);
        Log.i("url", "sendsql response=" + response);

        if (response.length() > 0 && !response.contains("{\"code\":\"1\"}")) {
            try {
                jsonArray = new JSONArray(response);
            } catch (Exception e) {
                Log.e("url", "Error: " + e.getMessage());
            }
        } else {
            Log.e("url", "sendsql Error Connecting/Getting Data.");
        }
        return jsonArray;
    }

}