//package com.example.mee526;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v7.app.AppCompatActivity;
//import android.util.Log;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.Toast;
//
//public class DPad extends Fragment {
//    private static final String TAG = "DPad";
//
//    private Button btnBluetooth;
//    private Button btnDpad;
//    private Button btnJoyStick;
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup containter, @Nullable final Bundle savedInstanceState){
//        super.onCreate(savedInstanceState);
//        //setContentView(R.layout.dpad_layout);
//
//        Button btnBluetooth = (Button) getView().findViewById(R.id.btnBluetooth);
//
//        Button btnDpad = (Button) getView().findViewById(R.id.btnDpad);
//
//        Button btnJoyStick = (Button) getView().findViewById(R.id.btnJoyStick);
//
//
//
//        // Navigation Buttons
//        // On Bluetooth NAV btn click change active screen to DATA
//        btnBluetooth.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                Log.d(TAG,"onClick: Setting DATA as screen. onCreate");
//                // DATA SCREEN
//                BluetoothNAV();
//
//            }
//
//        });
//
//        // On Dpad NAV btn click change active screen to MAIN
//        btnDpad.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                Log.d(TAG,"onClick: Setting MAIN as screen. onCreate");
//                DpadNAV();
//            }
//
//        });
//
//        // On JoyStick NAV btn click change active screen to PLOT
//        btnJoyStick.setOnClickListener(new View.OnClickListener(){
//
//            @Override
//            public void onClick(View view){
//                Log.d(TAG,"onClick: Setting PLOT as screen. onCreate");
//                JoyStickNAV();
//            }
//        });
//        return view;
//    }
//
//
//    // NAVIGATION
//
//    public void BluetoothNAV() {
//        // On Nav_MAIN btn click change active screen to MAIN
//
//        Log.d(TAG,"onClick: Setting DATA as screen.");
//        // BLUETOOTH SCREEN
//        // called activity_main b/c it is default screen
//        Toast.makeText(getActivity(), "Going to Bluetooth", Toast.LENGTH_SHORT);
//        ((MainActivity)getActivity().setViewPager(0));
//    }
//
//
//
//    public void DpadNAV() {
//        // On Nav_DATA btn click change active screen to DATA
//
//        Log.d(TAG,"onClick: Setting DATA as screen.");
//        // D-PAD SCREEN
//        setContentView(R.layout.dpad_layout);
//    }
//
//
//
//    public void JoyStickNAV() {
//        // On Nav_PLOT btn click change active screen to PLOT
//
//        Log.d(TAG,"onClick: Setting DATA as screen.");
//        // JoyStickNAV
//        setContentView(R.layout.joystick_layout);
//    }
//}
//
//
//
//
//
//
//
