package dev.smartlysoft.droneclient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by ccebr on 12/03/2017.
 */

public class SSIDListAdapter extends ArrayAdapter<String[]> {
    public SSIDListAdapter(Context context, ArrayList<String[]> nets) {
        super(context, 0, nets);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        String[] network = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.wifi_list_item, parent, false);
        }
        // Lookup view for data population
        TextView ssidTextView = (TextView) convertView.findViewById(R.id.SSID);
        TextView detailsTextView = (TextView) convertView.findViewById(R.id.details);
        // Populate the data into the template view using the data object
        ssidTextView.setText(network[0]);
        detailsTextView.setText(network[1]+" ("+network[2]+" dBm)");
        // Return the completed view to render on screen
        return convertView;
    }
}