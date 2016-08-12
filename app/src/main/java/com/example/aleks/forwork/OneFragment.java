package com.example.aleks.forwork;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by aleks on 10.07.2016.
 */

public class OneFragment extends Fragment {

    TextView contentView;
    String contentText = null;

    private String URL_FILE_DROPBOX = "https://www.dropbox.com/s/6bcldgdhxa6mlk3/cars.json?dl=1";

    private static final String TAG_CARS = "cars";
    private static final String TAG_MODEL = "model";
    private static final String TAG_YEAR = "year";
    private static final String TAG_COLOR = "color";
    private static final String TAG_PRICE = "price";


    public OneFragment () {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_one, container, false);
        contentView = (TextView) rootView.findViewById(R.id.content);
        if(contentText!=null)
            contentView.setText(contentText);
        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(contentText==null)
            new ProgressTask().execute();
    }

    class ProgressTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... path) {
            String content;
            try{
                content = getContent(URL_FILE_DROPBOX);
            }
            catch (IOException ex){
                content = ex.getMessage();
            }
            return content;
        }

        @Override
        protected void onPostExecute(String content) {
            JSONObject dataJsonObj = null;
            String sModel = "";
            String sYear = "";
            String sColor = "";
            String sPrice = "";
            String sFoolPars = "";

            try {
                dataJsonObj = new JSONObject(content);
                JSONArray cars = dataJsonObj.getJSONArray(TAG_CARS);
                    for(int i=0;i<cars.length();i++) {
                        JSONObject car = cars.getJSONObject(i);
                        sModel = car.getString(TAG_MODEL);
                        sYear = car.getString(TAG_YEAR);
                        sColor = car.getString(TAG_COLOR);
                        sPrice = car.getString(TAG_PRICE);
                        sFoolPars += sModel + "\n" + sYear + "\n" + sColor + "\n" + sPrice + "\n\n";
                    }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            contentView.setText(sFoolPars);
        }

        private String getContent(String path)  throws IOException {
            BufferedReader reader=null;
            try {
                URL url=new URL(path);
                URLConnection c=(URLConnection)url.openConnection();
                c.connect();
                reader= new BufferedReader(new InputStreamReader(c.getInputStream()));
                StringBuilder buf=new StringBuilder();
                String line=null;
                while ((line=reader.readLine()) != null) {
                    buf.append(line + "\n");
                }
                return(buf.toString());
            }
            finally {
                if (reader != null) {
                    reader.close();
                }
            }
        }
    }
}

