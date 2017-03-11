package dev.smartlysoft.droneclient.activity;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import dev.smartlysoft.droneclient.R;
import dev.smartlysoft.droneclient.network.JSONReciver;


public class ClientActivity extends AppCompatActivity {

    //UI ELEMENTS
    Button connectBtn;
    ListView wifiList;

    //CORE ELEEMENTS
    ArrayList wifiArrayList = new ArrayList();
    ArrayAdapter<String> arrayAdapter;
    Activity activity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_client);



        //Load UI;
        connectBtn = (Button)findViewById(R.id.ConnectBtn);
        wifiList = (ListView)findViewById(R.id.WifiList);

        wifiArrayList.add("Empty");

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTcpConnection().execute();
            }
        });

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, wifiArrayList);
        wifiList.setAdapter(arrayAdapter);
    }

    public void invalidate(){
        wifiList.invalidateViews();
    }

    private class AsyncTcpConnection extends AsyncTask<Void,Void,Void>{

        private String data;
        private JSONObject json = new JSONObject();


        @Override
        protected Void doInBackground(Void... params) {

            JSONReciver rcv = new JSONReciver();
            try {
                json = rcv.recive();


            } catch (Exception e) {
                wifiArrayList.clear();
                wifiArrayList.add("Net Error");

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            wifiArrayList.clear();
            Iterator x = json.keys();
            while (x.hasNext()){
                String key = (String) x.next();
                    wifiArrayList.add(key);
            }
            Log.d("si", wifiArrayList.toString());
            invalidate();
        }

        @Override
        protected void onProgressUpdate(Void... values) {
            super.onProgressUpdate(values);
        }

        @Override
        protected void onCancelled(Void aVoid) {
            super.onCancelled(aVoid);
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();
        }
    }


}
