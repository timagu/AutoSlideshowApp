package jp.techacademy.yamazawa.kouta.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Timer;
import java.util.TimerTask;

import jp.techacademy.yamazawa.kouta.autoslideshowapp.R;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    Button Return_button;
    Button AutoPlay_button;
    Button MoveOn_button;
    Button Permission_button;
    Timer mTimer;
    Cursor cursor;

    Handler mHandler = new Handler();

    double mTimerSec = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Return_button = (Button) findViewById(R.id.button);
        AutoPlay_button = (Button) findViewById(R.id.button2);
        MoveOn_button = (Button) findViewById(R.id.button3);
        Permission_button = (Button) findViewById(R.id.button5);

        Permission_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //パーミッションの許可状態を確認する
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //AndroidOSバージョン確認。6.0以降のバージョンか？
                    if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //許可されている
                        // 画像の情報を取得する
                        getContentsInfo();
                        Log.d("ANDROID", "許可されている");
                    } else {
                        //拒否されている
                        Log.d("ANDROID", "許可されていない");
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
                    }
                }
            }
        });

        /*AutoPlay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (; mTimerSec == 2.0; mTimerSec+=0.1) {
                    //Timer 用のサブスレッド生成
                    //タイマーの作成
                    mTimer = new Timer();

                    //タイマーの始動
                    mTimer.schedule(new TimerTask() {

                        //次の画像を2秒ごとに自動送りで表示
                        @Override
                        public void run() {
                            mTimerSec += 0.1;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    cursor.moveToNext();
                                }
                            });
                        }
                    }, 100, 100);
                }
            }
        });*/
    }

    // 画像の情報を取得する
    public void getContentsInfo() {

        // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToFirst()) {
            do {
                // indexからIDを取得し、そのIDから画像のURIを取得する
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                Log.d("ANDROID", "URI : " + imageUri.toString());
            } while (cursor.moveToNext());  //次の画像は、ありますか？
        }
        cursor.close();
    }
}
