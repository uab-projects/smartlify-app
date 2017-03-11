package dev.smartlysoft.droneclient.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import dev.smartlysoft.droneclient.R;
import dev.smartlysoft.droneclient.network.JSONReciver;


public class ClientActivity extends AppCompatActivity {

    //UI ELEMENTS
    Button connectBtn;
    EditText statusTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_client);

        //Load UI;
        connectBtn = (Button)findViewById(R.id.ConnectBtn);
        statusTxt = (EditText)findViewById(R.id.StatusTxt);

        connectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncTcpConnection().execute(1);
            }
        });
    }


    private class AsyncTcpConnection extends AsyncTask<Integer,Void,Void>{
        private Socket client;
        private DataInputStream in;
        private DataOutputStream out;
        private int lastAction;
        private String exText;

        @Override
        protected Void doInBackground(Integer... params) {

            JSONReciver rcv = new JSONReciver();
            try {
                rcv.recive();
            } catch (Exception e) {
                e.printStackTrace();
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

            statusTxt.setText("Action:" + lastAction);
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
            statusTxt.setText("Cancelled: " + exText);
        }
    }





}
