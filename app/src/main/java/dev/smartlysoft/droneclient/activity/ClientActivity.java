package dev.smartlysoft.droneclient.activity;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

import dev.smartlysoft.droneclient.R;


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
            try {
                client = new Socket();
                client.connect(new InetSocketAddress("192.168.2.200",7000),1000);
                in = new DataInputStream(client.getInputStream());
                out = new DataOutputStream(client.getOutputStream());

                switch (params[0]){
                    case 1:
                        Log.e("NET ", "CASE 1");
                        this.exText = "CASE 1";
                        lastAction = 1;
                        byte[] opcode = new byte[] {0x00};
                        out.write(opcode);

                        Log.e("NET ", "SEND...");
                        byte size_json[] = new byte[8];
                        in.read(size_json);                    // read length of incoming message
                        long value = 0;
                        for (int i = 0; i < size_json.length; i++)
                        {
                            value += ((long) size_json[i] & 0xffL) << (8 * i);
                        }
                        Log.e("sockets", String.format("I want to receive a Json of %d bytes", value));
                        int val = (int)value;
                        byte json_data[] = new byte[val];
                        in.read(json_data);
                        String json_str = new String(json_data, "UTF-8");
                        JSONObject json = new JSONObject(json_str);
                        Log.e("RES ", json.toString());
                        break;
                    default:
                        cancel(true);
                        break;
                }

            } catch (IOException e) {
                this.exText = e.toString();
                cancel(true);

            } catch (Exception e){
                this.exText = e.toString();
                cancel(true);
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
