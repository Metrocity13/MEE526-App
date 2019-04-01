package com.example.mee526;

import android.Manifest;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


import android.support.v4.view.ViewPager;

import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;

import android.util.Log;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.content.IntentFilter;

import android.os.Build;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.View;

import android.widget.AdapterView;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;

import java.nio.charset.Charset;

import java.util.ArrayList;

import java.util.UUID;

import android.Manifest;

import android.bluetooth.BluetoothAdapter;

import android.bluetooth.BluetoothDevice;

import android.content.BroadcastReceiver;

import android.content.Context;

import android.content.Intent;

import android.content.IntentFilter;

import android.os.Build;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;

import android.util.Log;

import android.view.View;

import android.widget.AdapterView;

import android.widget.Button;

import android.widget.EditText;

import android.widget.ListView;
import android.widget.TextView;


import java.nio.charset.Charset;

import java.util.ArrayList;

import java.util.UUID;





public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    private static final String TAG = "MainActivity";



    BluetoothAdapter mBluetoothAdapter;

    Button btnEnableDisable_Discoverable;



    BluetoothConnectionService mBluetoothConnection;



    Button btnStartConnection;

    Button btnSend;

    BottomNavigationView BluetoothLayout;
    BottomNavigationView DpadLayout;
    BottomNavigationView JoyStickLayout;
    BottomNavigationView ConnectedDevice_Display;

    Button btnForward;
    Button btnLeft;
    Button btnRight;
    Button btnReverse;

    EditText etSend;
    TextView Display_Device;
    StringBuilder Device_Name;


    private static final UUID MY_UUID_INSECURE =

            UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66");



    BluetoothDevice mBTDevice;



    public ArrayList<BluetoothDevice> mBTDevices = new ArrayList<>();



    public DeviceListAdapter mDeviceListAdapter;



    ListView lvNewDevices;

    Button btnONOFF;
    Button btnFindUnpairedDevices;



    // Create a BroadcastReceiver for ACTION_FOUND

    private final BroadcastReceiver mBroadcastReceiver1 = new BroadcastReceiver() {

        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();

            // When discovery finds a device

            if (action.equals(mBluetoothAdapter.ACTION_STATE_CHANGED)) {

                final int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, mBluetoothAdapter.ERROR);



                switch(state){

                    case BluetoothAdapter.STATE_OFF:

                        Log.d(TAG, "onReceive: STATE OFF");

                        break;

                    case BluetoothAdapter.STATE_TURNING_OFF:

                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING OFF");

                        break;

                    case BluetoothAdapter.STATE_ON:

                        Log.d(TAG, "mBroadcastReceiver1: STATE ON");

                        break;

                    case BluetoothAdapter.STATE_TURNING_ON:

                        Log.d(TAG, "mBroadcastReceiver1: STATE TURNING ON");

                        break;

                }

            }

        }

    };



    /**

     * Broadcast Receiver for changes made to bluetooth states such as:

     * 1) Discoverability mode on/off or expire.

     */

    private final BroadcastReceiver mBroadcastReceiver2 = new BroadcastReceiver() {



        @Override

        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();



            if (action.equals(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED)) {



                int mode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, BluetoothAdapter.ERROR);



                switch (mode) {

                    //Device is in Discoverable Mode

                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE:

                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Enabled.");

                        break;

                    //Device not in discoverable mode

                    case BluetoothAdapter.SCAN_MODE_CONNECTABLE:

                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Able to receive connections.");

                        break;

                    case BluetoothAdapter.SCAN_MODE_NONE:

                        Log.d(TAG, "mBroadcastReceiver2: Discoverability Disabled. Not able to receive connections.");

                        break;

                    case BluetoothAdapter.STATE_CONNECTING:

                        Log.d(TAG, "mBroadcastReceiver2: Connecting....");

                        break;

                    case BluetoothAdapter.STATE_CONNECTED:

                        Log.d(TAG, "mBroadcastReceiver2: Connected.");

                        break;

                }



            }

        }

    };









    /**

     * Broadcast Receiver for listing devices that are not yet paired

     * -Executed by btnDiscover() method.

     */

    private BroadcastReceiver mBroadcastReceiver3 = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();

            Log.d(TAG, "onReceive: ACTION FOUND.");



            if (action.equals(BluetoothDevice.ACTION_FOUND)){

                BluetoothDevice device = intent.getParcelableExtra (BluetoothDevice.EXTRA_DEVICE);

                mBTDevices.add(device);

                Log.d(TAG, "onReceive: " + device.getName() + ": " + device.getAddress());

                mDeviceListAdapter = new DeviceListAdapter(context, R.layout.device_adapter_view, mBTDevices);

                lvNewDevices.setAdapter(mDeviceListAdapter);

            }

        }

    };



    /**

     * Broadcast Receiver that detects bond state changes (Pairing status changes)

     */

    private final BroadcastReceiver mBroadcastReceiver4 = new BroadcastReceiver() {

        @Override

        public void onReceive(Context context, Intent intent) {

            final String action = intent.getAction();



            if(action.equals(BluetoothDevice.ACTION_BOND_STATE_CHANGED)){

                BluetoothDevice mDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                //3 cases:

                //case1: bonded already

                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDED){

                    Log.d(TAG, "BroadcastReceiver: BOND_BONDED.");

                    //inside BroadcastReceiver4

                    mBTDevice = mDevice;

                }

                //case2: creating a bone

                if (mDevice.getBondState() == BluetoothDevice.BOND_BONDING) {

                    Log.d(TAG, "BroadcastReceiver: BOND_BONDING.");

                }

                //case3: breaking a bond

                if (mDevice.getBondState() == BluetoothDevice.BOND_NONE) {

                    Log.d(TAG, "BroadcastReceiver: BOND_NONE.");

                }

            }

        }

    };







    @Override

    protected void onDestroy() {

        Log.d(TAG, "onDestroy: called.");

        super.onDestroy();

        unregisterReceiver(mBroadcastReceiver1);

        unregisterReceiver(mBroadcastReceiver2);

        unregisterReceiver(mBroadcastReceiver3);

        unregisterReceiver(mBroadcastReceiver4);

        //mBluetoothAdapter.cancelDiscovery();

    }



    @Override

    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Button btnONOFF = (Button) findViewById(R.id.btnONOFF);

        btnEnableDisable_Discoverable = (Button) findViewById(R.id.btnDiscoverable_on_off);

        lvNewDevices = (ListView) findViewById(R.id.lvNewDevices);

        mBTDevices = new ArrayList<>();

        Button btnBluetooth = (Button) findViewById(R.id.btnBluetooth);

        Button btnDpad = (Button) findViewById(R.id.btnDpad);

        Button btnJoyStick = (Button) findViewById(R.id.btnJoyStick);

        btnStartConnection = (Button) findViewById(R.id.btnStartConnection);

        btnSend = (Button) findViewById(R.id.btnSend);

        etSend = (EditText) findViewById(R.id.editText);

        Display_Device = (TextView) findViewById(R.id.Device_Display);

        btnForward = (Button) findViewById(R.id.btnForward);

        btnRight = (Button) findViewById(R.id.btnRight);

        btnLeft = (Button) findViewById(R.id.btnLeft);

        btnReverse = (Button) findViewById(R.id.btnReverse);

        BluetoothLayout = findViewById(R.id.Bluetooth);

        DpadLayout = findViewById(R.id.Dpad);

        JoyStickLayout = findViewById(R.id.JoyStick);

        ConnectedDevice_Display = findViewById(R.id.ConnectedDevice);

        Device_Name = new StringBuilder();

        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiver, new IntentFilter("DeviceName"));

        //Broadcasts when bond state changes (ie:pairing)

        IntentFilter filter = new IntentFilter(BluetoothDevice.ACTION_BOND_STATE_CHANGED);

        registerReceiver(mBroadcastReceiver4, filter);



        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();



        lvNewDevices.setOnItemClickListener(MainActivity.this);





        btnONOFF.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                Log.d(TAG, "onClick: enabling/disabling bluetooth.");

                enableDisableBT();

            }

        });


        // Navigation Buttons
        // On Bluetooth NAV btn click change active screen to DATA
        btnBluetooth.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Log.d(TAG,"onClick: Setting DATA as screen. onCreate");
                // DATA SCREEN
                BluetoothNAV();

            }

        });

        // On Dpad NAV btn click change active screen to MAIN
        btnDpad.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Log.d(TAG,"onClick: Setting MAIN as screen. onCreate");
                DpadNAV();
            }

        });

        // On JoyStick NAV btn click change active screen to PLOT
        btnJoyStick.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Log.d(TAG,"onClick: Setting PLOT as screen. onCreate");
                JoyStickNAV();
            }

        });
        btnStartConnection.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                startConnection();

            }

        });

        // When clicked send value via Bluetooth

        // on button click write "FWD" to invisible textbox etSend, convert Text into bytes using
            // default characters and send broadcast resulting bytes to connected BL device
        btnForward.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {
                etSend.setText("FWD");

                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());

                mBluetoothConnection.write(bytes);
            }
        });

        btnRight.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {
                etSend.setText("RGT");

                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());

                mBluetoothConnection.write(bytes);
            }
        });

        btnReverse.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {
                etSend.setText("REV");

                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());

                mBluetoothConnection.write(bytes);
            }
        });

        btnLeft.setOnClickListener(new View.OnClickListener(){
            @Override

            public void onClick(View view) {
                etSend.setText("LFT");

                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());

                mBluetoothConnection.write(bytes);
            }
        });


        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View view) {

                byte[] bytes = etSend.getText().toString().getBytes(Charset.defaultCharset());

                mBluetoothConnection.write(bytes);

            }

        });



    }

    BroadcastReceiver mReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String text = intent.getStringExtra("DeviceName");

            Device_Name.append(text + "/n");
            Display_Device.setText(Device_Name);
        }
    };

    //create method for starting connection

//***remember the conncction will fail and app will crash if you haven't paired first

    public void startConnection(){

        startBTConnection(mBTDevice,MY_UUID_INSECURE);

    }



    /**

     * starting chat service method

     */

    public void startBTConnection(BluetoothDevice device, UUID uuid){

        Log.d(TAG, "startBTConnection: Initializing RFCOM Bluetooth Connection.");



        mBluetoothConnection.startClient(device,uuid);

    }







    public void enableDisableBT(){

        if(mBluetoothAdapter == null){

            Log.d(TAG, "enableDisableBT: Does not have BT capabilities.");

        }

        if(!mBluetoothAdapter.isEnabled()){

            Log.d(TAG, "enableDisableBT: enabling BT.");

            Intent enableBTIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);

            startActivity(enableBTIntent);



            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

            registerReceiver(mBroadcastReceiver1, BTIntent);

        }

        if(mBluetoothAdapter.isEnabled()){

            Log.d(TAG, "enableDisableBT: disabling BT.");

            mBluetoothAdapter.disable();



            IntentFilter BTIntent = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);

            registerReceiver(mBroadcastReceiver1, BTIntent);

        }



    }





    public void btnEnableDisable_Discoverable(View view) {

        Log.d(TAG, "btnEnableDisable_Discoverable: Making device discoverable for 300 seconds.");



        Intent discoverableIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);

        discoverableIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 300);

        startActivity(discoverableIntent);



        IntentFilter intentFilter = new IntentFilter(mBluetoothAdapter.ACTION_SCAN_MODE_CHANGED);

        registerReceiver(mBroadcastReceiver2,intentFilter);



    }



    public void btnDiscover(View view) {

        Log.d(TAG, "btnDiscover: Looking for unpaired devices.");



        if(mBluetoothAdapter.isDiscovering()){

            mBluetoothAdapter.cancelDiscovery();

            Log.d(TAG, "btnDiscover: Canceling discovery.");



            //check BT permissions in manifest

            checkBTPermissions();



            mBluetoothAdapter.startDiscovery();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);

            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        }

        if(!mBluetoothAdapter.isDiscovering()){



            //check BT permissions in manifest

            checkBTPermissions();



            mBluetoothAdapter.startDiscovery();

            IntentFilter discoverDevicesIntent = new IntentFilter(BluetoothDevice.ACTION_FOUND);

            registerReceiver(mBroadcastReceiver3, discoverDevicesIntent);

        }

    }

    // Navigation sub Routine
    // what happens when each NAV button is pushed

    public void BluetoothNAV() {
        // On Bluetooth btn click change active screen to Bluetooth_Layout

        Log.d(TAG,"onClick: Setting Bluetooth as screen.");
        // BLUETOOTH SCREEN
        BluetoothLayout.setVisibility(View.VISIBLE);

        // D-Pad
        DpadLayout.setVisibility(View.GONE);

        // JoyStick
        JoyStickLayout.setVisibility(View.GONE);

        // Nav menu
        /* If you want to load a different navigation menu put it here */
        ConnectedDevice_Display.setVisibility(View.GONE);
    }



    public void DpadNAV() {
        // On D-Pad btn click change active screen to DATA

        Log.d(TAG,"onClick: Setting D-Pad as screen.");
        // BLUETOOTH SCREEN
        BluetoothLayout.setVisibility(View.GONE);

        // D-Pad
        DpadLayout.setVisibility(View.VISIBLE);

        // JoyStick
        JoyStickLayout.setVisibility(View.GONE);

        // Nav menu
        /* If you want to load a different navigation menu put it here */
        ConnectedDevice_Display.setVisibility(View.VISIBLE);
    }



    public void JoyStickNAV() {
        // On Nav_PLOT btn click change active screen to PLOT

        Log.d(TAG,"onClick: Setting JoyStick as screen.");
        // BLUETOOTH SCREEN
        BluetoothLayout.setVisibility(View.GONE);

        // D-Pad
        DpadLayout.setVisibility(View.GONE);

        // JoyStick
        JoyStickLayout.setVisibility(View.VISIBLE);

        // Nav menu
        /* If you want to load a different navigation menu put it here */
        ConnectedDevice_Display.setVisibility(View.VISIBLE);
    }

    /**

     * This method is required for all devices running API23+

     * Android must programmatically check the permissions for bluetooth. Putting the proper permissions

     * in the manifest is not enough.

     *

     * NOTE: This will only execute on versions > LOLLIPOP because it is not needed otherwise.

     */

    private void checkBTPermissions() {

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP){

            int permissionCheck = this.checkSelfPermission("Manifest.permission.ACCESS_FINE_LOCATION");

            permissionCheck += this.checkSelfPermission("Manifest.permission.ACCESS_COARSE_LOCATION");

            if (permissionCheck != 0) {



                this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1001); //Any number

            }

        }else{

            Log.d(TAG, "checkBTPermissions: No need to check permissions. SDK version < LOLLIPOP.");

        }

    }



    @Override

    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

        //first cancel discovery because its very memory intensive.

        mBluetoothAdapter.cancelDiscovery();



        Log.d(TAG, "onItemClick: You Clicked on a device.");

        String deviceName = mBTDevices.get(i).getName();

        String deviceAddress = mBTDevices.get(i).getAddress();



        Log.d(TAG, "onItemClick: deviceName = " + deviceName);

        Log.d(TAG, "onItemClick: deviceAddress = " + deviceAddress);



        //create the bond.

        //NOTE: Requires API 17+? I think this is JellyBean

        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN_MR2){

            Log.d(TAG, "Trying to pair with " + deviceName);

            mBTDevices.get(i).createBond();



            mBTDevice = mBTDevices.get(i);

            mBluetoothConnection = new BluetoothConnectionService(MainActivity.this);

        }

    }

}