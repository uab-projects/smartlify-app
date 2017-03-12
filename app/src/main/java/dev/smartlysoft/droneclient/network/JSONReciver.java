package dev.smartlysoft.droneclient.network;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

public class JSONReciver {

    private String address = "192.168.2.200";
    private int port = 3000;


    public JSONReciver(){

    }

    public String getAddress() {
        return address;
    }

    public JSONReciver setAddress(String address) {
        this.address = address;
        return this;
    }

    public int getPort() {
        return port;
    }

    public JSONReciver setPort(int port) {
        this.port = port;
        return this;
    }

    public JSONObject recive() throws IOException, Exception{

        Socket client = new Socket();
        client.connect(new InetSocketAddress(address,port),1000);
        DataInputStream in = new DataInputStream(client.getInputStream());
        DataOutputStream out = new DataOutputStream(client.getOutputStream());

        byte[] opcode = new byte[] {0x00};
        out.write(opcode);

        byte size_json[] = new byte[8];
        in.read(size_json);
        long value = 0;
        for (int i = 0; i < size_json.length; i++) {
            value += ((long) size_json[i] & 0xffL) << (8 * i);
        }

        int val = (int)value;
        byte json_data[] = new byte[val];

        in.read(json_data);
        String json_str = new String(json_data, "UTF-8");

        client.close();

        return new JSONObject(json_str);
    }
}
