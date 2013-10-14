package com.example.mysafety;

import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.concurrent.ExecutionException;

public class Route extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_activity);
        Bundle mybundle = getIntent().getExtras();
        TextView src_txt_view = (TextView) findViewById(R.id.src_route_TxtBox);
        TextView dest_txt_view = (TextView) findViewById(R.id.dest_route_TxtBox);
        src_txt_view.setText(mybundle.getString("source"));
        dest_txt_view.setText(mybundle.getString("destination"));

        TextView jsonTextView=(TextView) findViewById(R.id.jsonTextView);

        String jsonTextFromGoogleDirections=null;
        try {
            jsonTextFromGoogleDirections=String.valueOf(new callGoogleAPI().execute("http://maps.googleapis.com/maps/api/directions/json?origin="+mybundle.getString("source")+"&destination="+mybundle.getString("destination")+"&sensor=false").get());
            jsonTextView.setText(jsonTextFromGoogleDirections);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }




        // Writing JSON data to file for temporary reference.

        File root = new File(Environment.getExternalStorageDirectory(), "Notes");
        if (!root.exists()) {
            root.mkdirs();
        }
        File gpxfile = new File(root, "gdjon.txt");
        FileWriter writer = null;
        try {
            writer = new FileWriter(gpxfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.append(jsonTextFromGoogleDirections);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Toast.makeText(this, "Saved", Toast.LENGTH_SHORT).show();

        // End of writing file.


        /* Parsing JSON from Google Direction */

        JSONObject jsonObjectFromGD = null;
        JSONObject routeJSONString = null;
        JSONObject leg;
        JSONArray stepsArray = null;
        String str = null;
        String legs_str= null;
        String steps_str;
        JSONObject step = null;
        JSONObject start = null;
        JSONObject end=null;
        JSONObject duration =null;
        JSONObject distance =null;
        JSONObject lon = null;
        try {
            jsonObjectFromGD = new JSONObject(jsonTextFromGoogleDirections);
            routeJSONString = jsonObjectFromGD.getJSONArray("routes").getJSONObject(0);
            leg = routeJSONString.getJSONArray("legs").getJSONObject(0);
            legs_str = leg.toString();
            stepsArray = leg.getJSONArray("steps");
            steps_str = stepsArray.toString();
            int numSteps = stepsArray.length();
            for (int i = 0; i < numSteps; i++)
            {
                step = stepsArray.getJSONObject(i);
                end = step.getJSONObject("end_location");
                start = step.getJSONObject("start_location");
                distance = step.getJSONObject("distance");
                duration = step.getJSONObject("duration");
                System.out.println("Start: "+start.toString()+"((((");
                System.out.println("End: "+end.toString()+"((((");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        /* End Parsing JSON from Google Direction. */

        /* Calling the Spot Crime API. */

        String jsonTextFromSpotCrime=null;
        try {
            jsonTextFromSpotCrime=String.valueOf(new callSpotCrimeAPI().execute("http://api.spotcrime.com/crimes.json?at=%2230.02742%22&lon=%22-118.2897%22&radius=1&key=MLC").get());
           // jsonTextView.setText(jsonTextFromSpotCrime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }

        JSONObject jsonObjectFromSpotCrime=null;
        JSONArray crimeJSONArray = null;

        try{
        jsonObjectFromSpotCrime = new JSONObject(jsonTextFromSpotCrime);
        crimeJSONArray = jsonObjectFromSpotCrime.getJSONArray("crimes");
        }
        catch (JSONException e){
            e.printStackTrace();
        }
        System.out.println("(((("+crimeJSONArray.length());
        /* Calling the Spot Crime API ends. */

        /* Fetching length of the JSON from SpotCrimeAPI. */
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.route, menu);
        return true;
    }

    public class callGoogleAPI extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            URL googleURL=null;
            try {
                googleURL=new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection conn=null;
            try {
                conn=googleURL.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is=null;
            try {
                is=conn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader b=new BufferedReader(new InputStreamReader(is));
            String l=null;
            StringBuilder stringBuilder=new StringBuilder();
            try {
                while((l=b.readLine())!=null){
                    stringBuilder.append(l);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
    }

    public class callSpotCrimeAPI extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            URL spotCrimeURL=null;
            try {
                spotCrimeURL=new URL(strings[0]);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            URLConnection conn=null;
            try {
                conn=spotCrimeURL.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            InputStream is=null;
            try {
                is=conn.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            BufferedReader b=new BufferedReader(new InputStreamReader(is));
            String l=null;
            StringBuilder stringBuilder=new StringBuilder();
            try {
                while((l=b.readLine())!=null){
                    stringBuilder.append(l);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return stringBuilder.toString();
        }
    }

}
