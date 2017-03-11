package dev.smartlysoft.droneclient.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.parrot.arsdk.ARSDK;
import com.parrot.arsdk.ardiscovery.ARDISCOVERY_PRODUCT_ENUM;
import com.parrot.arsdk.ardiscovery.ARDiscoveryDeviceService;
import com.parrot.arsdk.ardiscovery.ARDiscoveryService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dev.smartlysoft.droneclient.R;
import dev.smartlysoft.droneclient.discovery.DroneDiscoverer;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSIONS_REQUEST = 1;

    public DroneDiscoverer droneDiscoverer;
    private List<ARDiscoveryDeviceService> dronesList = new ArrayList<>();
    Intent intent = null;

    static {
        ARSDK.loadSDKLibs();
    }

    private static final String[] PERMISSIONS_NEEDED = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.ACCESS_COARSE_LOCATION,
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        droneDiscoverer = new DroneDiscoverer(this);
        droneDiscoverer.addListener(discovererListener);

        Set<String> permissionsToRequest = new HashSet<>();
        for (String permission : PERMISSIONS_NEEDED) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                    Toast.makeText(this, "Please allow permission " + permission, Toast.LENGTH_LONG).show();
                    finish();
                    return;
                } else {
                    permissionsToRequest.add(permission);
                }
            }
        }
        if (permissionsToRequest.size() > 0) {
            ActivityCompat.requestPermissions(this,
                    permissionsToRequest.toArray(new String[permissionsToRequest.size()]),
                    REQUEST_CODE_PERMISSIONS_REQUEST);
        }

    }

    private final DroneDiscoverer.Listener discovererListener = new DroneDiscoverer.Listener() {

        @Override
        public void onDronesListUpdated(List<ARDiscoveryDeviceService> list) {
            Log.d("D","Call Update");
            dronesList.clear();
            dronesList.addAll(list);
            Log.d("D", Integer.toBinaryString(dronesList.size()));

            if(dronesList.size() == 1){
                Log.d("D","Updated");
                ARDISCOVERY_PRODUCT_ENUM product = ARDiscoveryService.getProductFromProductID(dronesList.get(0).getProductID());
                Intent intent = null;

                switch (product) {
                    case ARDISCOVERY_PRODUCT_JS:
                    case ARDISCOVERY_PRODUCT_JS_EVO_LIGHT:
                    case ARDISCOVERY_PRODUCT_JS_EVO_RACE:
                        intent = new Intent(MainActivity.this, JSActivity.class);
                        break;
                }
            }
        }
    };

    @Override
    protected void onResume()
    {
        super.onResume();

        // setup the drone discoverer and register as listener
        droneDiscoverer.setup();
        droneDiscoverer.addListener(discovererListener);

        // start discovering
        droneDiscoverer.startDiscovering();
    }

    @Override
    protected void onPause()
    {
        super.onPause();

        // clean the drone discoverer object
        droneDiscoverer.stopDiscovering();
        droneDiscoverer.cleanup();
        droneDiscoverer.removeListener(discovererListener);
    }


}
