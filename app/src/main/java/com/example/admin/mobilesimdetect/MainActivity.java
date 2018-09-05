package com.example.admin.mobilesimdetect;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SubscriptionInfo;
import android.telephony.SubscriptionManager;
import android.telephony.TelephonyManager;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private TextView mData;
    private String data;
    private static final int REQUEST_FOR_READ_PHONE_STATE = 12021;
    List<String> carrierNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mData = findViewById(R.id.text);
        requestPermissionForReadPhoneState();
    }

    private void setDataToViews() {
        StringBuilder stringBuilder = new StringBuilder();
        for (int i = 0; i < carrierNames.size(); i++) {
            stringBuilder.append(carrierNames.get(i)+"    ");
        }
        String data = stringBuilder.toString();
        mData.setText(data);
    }

    public void requestPermissionForReadPhoneState() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_PHONE_STATE)
                != PackageManager.PERMISSION_GRANTED) {
            // not granted, explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.READ_PHONE_STATE)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_FOR_READ_PHONE_STATE);
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_PHONE_STATE},
                        REQUEST_FOR_READ_PHONE_STATE);
            }
        } else {
            //permission already granted
            getNetworkOperator(MainActivity.this);
        }
    }

    //not called for now, it is not working WIP
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            // permission was granted
            getNetworkOperator(MainActivity.this);
        } else {
            //permission denied
            Toast.makeText(MainActivity.this, "Please give phone permission in Settings", Toast.LENGTH_SHORT).show();
        }
    }

    private void getNetworkOperator(Context context) {
        // If android os version less than 22 then get NetworkOperatorName using TelephonyManager  else use SubscriptionManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
            List<SubscriptionInfo> subscriptionInfos = SubscriptionManager.from(context).getActiveSubscriptionInfoList();
            if (subscriptionInfos != null && subscriptionInfos.size() > 0) {
                for (int i = 0; i < subscriptionInfos.size(); i++) {
                    carrierNames.add(subscriptionInfos.get(i).getCarrierName().toString());
                }
                setDataToViews();
                }
            } else {
                TelephonyManager telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
                data = telephonyManager.getNetworkOperatorName();
                mData.setText(data);
            }
        }
    }
