package com.example.myapplication;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaPlayer;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

import app.akexorcist.bluetotohspp.library.BluetoothSPP;
import app.akexorcist.bluetotohspp.library.BluetoothState;
import app.akexorcist.bluetotohspp.library.DeviceList;

public class MainActivity extends AppCompatActivity {

    private BluetoothSPP bt;
    private GpsInfo gps;
    public String callNum = "01025468559";
    public String policeNum = "1212121212";
    public String SMS = "나";
    public String GPS;
    public double lat, lon;
    public String smsFisrt = "위험에 처했습니다.. 도와주세요";

    private String locationProvider = "Network";
    private Location lastKnownLocation = null;

    private Button btnShowLocation;
    private TextView txtLat;
    private TextView txtLon;

    private boolean isPermission = false;
    private final int PERMISSIONS_ACCESS_FINE_LOCATION = 1000;
    private final int PERMISSIONS_ACCESS_COARSE_LOCATION = 1001;
    private boolean isAccessFineLocation = false;
    private boolean isAccessCoarseLocation = false;
    private Context mContext;
    private static Context baseContext;
    private Fastcall fakeCall;

/*    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bt = new BluetoothSPP(this); //Initializing
  /*      if (!isPermission) {
            callPermission();
            return;
        }
*/

   /*     latSign = (TextView) findViewById(R.id.latSign);
        latSign.setText("GPS 가 잡혀야 좌표가 구해짐");

        // Acquire a reference to the system Location Manager
        final LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        // GPS 프로바이더 사용가능여부
        isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 네트워크 프로바이더 사용가능여부
        isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        Log.d("Main", "isGPSEnabled=" + isGPSEnabled);
        Log.d("Main", "isNetworkEnabled=" + isNetworkEnabled);

        final LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                double lat = location.getLatitude();
                double lng = location.getLongitude();

                latSign.setText("latitude: " + lat + ", longitude: " + lng);
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
                latSign.setText("onStatusChanged");
            }

            public void onProviderEnabled(String provider) {
                latSign.setText("onProviderEnabled");
            }

            public void onProviderDisabled(String provider) {
                latSign.setText("onProviderDisabled");
            }
        };

        // Register the listener with the Location Manager to receive location updates
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);

        // 수동으로 위치 구하기
        String locationProvider = LocationManager.GPS_PROVIDER;
        Location lastKnownLocation = locationManager.getLastKnownLocation(locationProvider);
        if (lastKnownLocation != null) {
            double lng = lastKnownLocation.getLatitude();
            double lat = lastKnownLocation.getLatitude();
            Log.d("Main", "longtitude=" + lng + ", latitude=" + lat);
        }

*/
        btnShowLocation = (Button) findViewById(R.id.btnShowLocation);
        txtLat = (TextView) findViewById(R.id.txtLat);
        txtLon = (TextView) findViewById(R.id.txtLon);

        // GPS 정보를 보여주기 위한 이벤트 클래스 등록
        btnShowLocation.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                // 권한 요청을 해야 함
                if (!isPermission) {
                    callPermission();
                    return;
                }

                gps = new GpsInfo(MainActivity.this);
                // GPS 사용유무 가져오기
                if (gps.isGetLocation()) {

                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();

                    txtLat.setText(String.valueOf(latitude));
                    txtLon.setText(String.valueOf(longitude));

                    Toast.makeText(
                            getApplicationContext(),
                            "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                            Toast.LENGTH_LONG).show();
                } else {
                    // GPS 를 사용할수 없으므로
                    gps.showSettingsAlert();
                }
            }
        });

        callPermission();  // 권한 요청을 해야 함



        gps = new GpsInfo(MainActivity.this);


        if (!bt.isBluetoothAvailable()) { //블루투스 사용 불가
            Toast.makeText(getApplicationContext()
                    , "Bluetooth is not available"
                    , Toast.LENGTH_SHORT).show();
            finish();
        }
//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        final TextView nowNum = findViewById(R.id.nowNum);
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("새로운 전화번호 입력");
        alert.setMessage("010-XXXX-XXXX");


        final EditText name = new EditText(this);
        alert.setView(name);

        alert.setPositiveButton("갱신", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                nowNum.setText(name.getText().toString());
                callNum = name.getText().toString();

            }
        });

         alert.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });

        final AlertDialog.Builder smsUpdate = new AlertDialog.Builder(this);

        smsUpdate.setTitle("보낼 문자메세지를 입력하세요");
        smsUpdate.setMessage("기본 : 위험에 처했습니다.. 도와주세요");

        final EditText smsUp = new EditText(this);
        smsUpdate.setView(smsUp);

        smsUpdate.setPositiveButton("갱신", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int whichButton) {
                smsFisrt = smsUp.getText().toString();
            }
        });

        smsUpdate.setNegativeButton("취소", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
    /*    final AlertDialog.Builder firstAlert = new AlertDialog.Builder(this);

        firstAlert.setTitle("상황을");
        firstAlert.setMessage("010-XXXX-XXXX");


        firstAlert.setView(name);

        firstAlert.setPositiveButton("갱신", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

                nowNum.setText(name.getText().toString());
                callNum = name.getText().toString();

            }
        });


        firstAlert.setNegativeButton("취소",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {

            }
        });*/


        Button callUpdate = findViewById(R.id.callUpdate); //전화번호 갱신
        callUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.show();
                Fastcall tmp = new Fastcall();
                tmp.start();
            }

        });

        Button smsUpdateButton = findViewById(R.id.smsUpdateButton);
        smsUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                smsUpdate.show();
            }
        });


        gps.isGetLocation();

        final MediaPlayer mp;
        mp = MediaPlayer.create(this, R.raw.intro);

        final LinearLayout colorSignal = findViewById(R.id.colorSignal);
        final TextView readSign = findViewById(R.id.readSign);
        final Button bStop = findViewById(R.id.bStop);
        final Timer mpTimer = new Timer();

        final double latitude = gps.getLatitude();
        final double longitude = gps.getLongitude();
        bt.setOnDataReceivedListener(new BluetoothSPP.OnDataReceivedListener() { //데이터 수신
            public void onDataReceived(byte[] data, String message) {

                // String canDo = Arrays.toString(data);
                int rORg = Integer.parseInt(message);
                //readSign.setText(message);

                    switch (rORg) {
                        case 3:
                            mp.start();
                            TimerTask mpTask = new TimerTask() {
                                @Override
                                public void run() {
                                    mp.pause();

                                }
                            };
                            mpTimer.schedule(mpTask,10000);
                            break;
                        case 4:
                            colorSignal.setBackgroundColor(Color.RED);
                            readSign.setTextColor(Color.WHITE);
                            readSign.setText("WARNING!");

                      /*  lat = gps.getLatitude();
                        lon = gps.getLongitude();*/
                            //   txtLat.setText(String.valueOf(latitude));
                            //   txtLon.setText(String.valueOf(longitude));

                            Toast.makeText(
                                    getApplicationContext(),
                                    "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                                    Toast.LENGTH_LONG).show();

                            // txtLat.setText(getAddress(mContext,latitude,longitude));

                            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                            //      SMS = gps.getAddress(null,lat,lon);
                            //  System.out.println(SMS);
                            findAddress(latitude, longitude);
                            SendSMS(policeNum, SMS);
                            break;
                        case 1:
                            colorSignal.setBackgroundColor(Color.RED);
                            readSign.setTextColor(Color.WHITE);
                            readSign.setText("WARNING!");

                      /*  lat = gps.getLatitude();
                        lon = gps.getLongitude();*/
                            //   txtLat.setText(String.valueOf(latitude));
                            //   txtLon.setText(String.valueOf(longitude));

                            Toast.makeText(
                                    getApplicationContext(),
                                    "당신의 위치 - \n위도: " + latitude + "\n경도: " + longitude,
                                    Toast.LENGTH_LONG).show();

                            // txtLat.setText(getAddress(mContext,latitude,longitude));

                            //locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);
                            //      SMS = gps.getAddress(null,lat,lon);
                            //  System.out.println(SMS);
                            findAddress(latitude, longitude);
                            SendSMS(callNum, SMS);
                            break;
                        case 0:
                            colorSignal.setBackgroundColor(Color.GREEN);
                            readSign.setTextColor(Color.WHITE);
                            readSign.setText("SAFETY~");
                       /* bStop.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // 음악 종료

                                mp.stop(); // 멈춤
                                mp.release(); // 자원 해제
                                bStop.setVisibility(View.INVISIBLE);
                            }
                        });*/
                            break;
                    }


            }

        });


//////////////////////////////////////////////////////////////////////////////////////////////////////////////
        bt.setBluetoothConnectionListener(new BluetoothSPP.BluetoothConnectionListener() { //연결됐을 때


            public void onDeviceConnected(String name, String address) {
                Toast.makeText(getApplicationContext()
                        , "Connected to " + name + "\n" + address
                        , Toast.LENGTH_SHORT).show();
            }

            public void onDeviceDisconnected() { //연결해제
                Toast.makeText(getApplicationContext()
                        , "Connection lost", Toast.LENGTH_SHORT).show();
            }

            public void onDeviceConnectionFailed() { //연결실패
                Toast.makeText(getApplicationContext()
                        , "Unable to connect", Toast.LENGTH_SHORT).show();
            }
        });

        Button btnConnect = findViewById(R.id.btnConnect); //연결시도
        btnConnect.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (bt.getServiceState() == BluetoothState.STATE_CONNECTED) {
                    bt.disconnect();
                } else {
                    Intent intent = new Intent(getApplicationContext(), DeviceList.class);
                    startActivityForResult(intent, BluetoothState.REQUEST_CONNECT_DEVICE);
                }
            }
        });

    /*    Button smsUpdate = findViewById(R.id.smsUpdate);
        smsUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });*/

     /*   Button send = findViewById(R.id.send);
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SendSMS(callNum,"test");
        }
        });*/
/////////////////////////////////////////////////////////////////////////////////////////


/////////////////////////////////////////////////////////////////////////////////////////


    }


  /*  public void sendSMS(String smsNumber, String smsText){  // 문자메세지 전송
        PendingIntent sentIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT_ACTION"), 0);
        PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED_ACTION"), 0);

        registerReceiver(new BroadcastReceiver() {
            Context mContext;
            @Override
            public void onReceive(Context context, Intent intent) {
                switch(getResultCode()) {
                    case Activity.RESULT_OK:
                        // 전송 성공

                        Toast.makeText(mContext, "전송 완료", Toast.LENGTH_SHORT).show();
                        break;
                    case SmsManager.RESULT_ERROR_GENERIC_FAILURE:
                        // 전송 실패
                        Toast.makeText(mContext, "전송 실패", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };)
        SmsManager mSmsManager = SmsManager.getDefault();
        mSmsManager.sendTextMessage(smsNumber, null, smsText, sentIntent, deliveredIntent);
    }*/

    public void SendSMS(String number, String msg) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        if (permissionCheck == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, 1);
            Toast.makeText(this, "권한을 허용하고 재전송해주세요", Toast.LENGTH_LONG).show();
        } else {
            try {
                //전송
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, msg, null, null);
                Toast.makeText(getApplicationContext(), "전송 완료!", Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "SMS faild, please try again later!", Toast.LENGTH_LONG).show();
                e.printStackTrace();
            }
        }
    }


    public void onDestroy() {
        super.onDestroy();
        bt.stopService(); //블루투스 중지
    }

    public void onStart() {
        super.onStart();
        if (!bt.isBluetoothEnabled()) { //
            Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(intent, BluetoothState.REQUEST_ENABLE_BT);
        } else {
            if (!bt.isServiceAvailable()) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER); //DEVICE_ANDROID는 안드로이드 기기 끼리
                setup();
            }
        }

    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void setup() {
        Button btnSend = findViewById(R.id.btnSend); //데이터 전송
        btnSend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bt.send("Find Signal!!", true);

            }
        });
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == BluetoothState.REQUEST_CONNECT_DEVICE) {
            if (resultCode == Activity.RESULT_OK)
                bt.connect(data);
        } else if (requestCode == BluetoothState.REQUEST_ENABLE_BT) {
            if (resultCode == Activity.RESULT_OK) {
                bt.setupService();
                bt.startService(BluetoothState.DEVICE_OTHER);
                setup();
            } else {
                Toast.makeText(getApplicationContext()
                        , "Bluetooth was not enabled."
                        , Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {
        if (requestCode == PERMISSIONS_ACCESS_FINE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            isAccessFineLocation = true;

        } else if (requestCode == PERMISSIONS_ACCESS_COARSE_LOCATION
                && grantResults[0] == PackageManager.PERMISSION_GRANTED){

            isAccessCoarseLocation = true;
        }

        if (isAccessFineLocation && isAccessCoarseLocation) {
            isPermission = true;
        }
    }

    // 전화번호 권한 요청
    private void callPermission() {
        // Check the SDK version and whether the permission is already granted or not.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_ACCESS_FINE_LOCATION);

        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
                && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){

            requestPermissions(
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    PERMISSIONS_ACCESS_COARSE_LOCATION);
        } else {
            isPermission = true;
        }
    }
/*    public static String getAddress(Context mContext,double lat, double lng) {
        String nowAddress ="현재 위치를 확인 할 수 없습니다.";
        Geocoder geocoder = new Geocoder(mContext, Locale.KOREA);
        List <Address> address;
        try {
            if (geocoder != null) {
                //세번째 파라미터는 좌표에 대해 주소를 리턴 받는 갯수로
                //한좌표에 대해 두개이상의 이름이 존재할수있기에 주소배열을 리턴받기 위해 최대갯수 설정
                address = geocoder.getFromLocation(lat, lng, 1);

                if (address != null && address.size() > 0) {
                    // 주소 받아오기
                    String currentLocationAddress = address.get(0).getAddressLine(0).toString();
                    nowAddress  = currentLocationAddress;

                }
            }

        } catch (IOException e) {
            Toast.makeText(baseContext, "주소를 가져 올 수 없습니다.", Toast.LENGTH_LONG).show();

            e.printStackTrace();
        }
        return nowAddress;
    }*/
private String findAddress(double lat, double lng) {
    StringBuffer bf = new StringBuffer();
    Geocoder geocoder = new Geocoder(this, Locale.KOREA);
    List<Address> address;
    try {
        if (geocoder != null) {
            // 세번째 인수는 최대결과값인데 하나만 리턴받도록 설정했다
            address = geocoder.getFromLocation(lat, lng, 1);
            // 설정한 데이터로 주소가 리턴된 데이터가 있으면
            if (address != null && address.size() > 0) {
                // 주소
                SMS = smsFisrt + " 위치는 "+address.get(0).getAddressLine(0).toString()+" 입니다";

                // 전송할 주소 데이터 (위도/경도 포함 편집)
                bf.append(SMS).append("#");
                bf.append(lat).append("#");
                bf.append(lng);
            }
        }

    } catch (IOException e) {
        Toast.makeText(baseContext, "주소취득 실패"
                , Toast.LENGTH_LONG).show();

        e.printStackTrace();
    }
    return bf.toString();
}

}





