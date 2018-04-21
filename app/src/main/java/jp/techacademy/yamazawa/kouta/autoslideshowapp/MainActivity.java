package jp.techacademy.yamazawa.kouta.autoslideshowapp;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

import jp.techacademy.yamazawa.kouta.autoslideshowapp.R;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    //メンバ変数
    //インスタンス
    Button Return_button;
    Button AutoPlay_button;
    Button MoveOn_button;
    Timer mTimer;
    Cursor cursor;

    // タイマー用の時間のための変数
    double mTimerSec = 0.0;

    //インスタンス
    Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Return_button = (Button) findViewById(R.id.button);
        AutoPlay_button = (Button) findViewById(R.id.button2);
        MoveOn_button = (Button) findViewById(R.id.button3);

        Log.d("ANDROID","始まります。");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //AndroidOSバージョン確認。6.0以降のバージョンか？
            Log.d("Version","6.0以降");
            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                //許可されている
                // 現在の位置の画像を取得する
                getContentsInfo();
            } else {

                //拒否されている
                Log.d("ANDROID", "許可されていない");
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
            }
        }else{

            getContentsInfo();
            Log.d("Version","6.0以前");
        }

        MoveOn_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ANDROID","進むボタンを押しました。");
                //次の画像を取得
                if(cursor.moveToNext()){
                    //画像表示
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                    imageVIew.setImageURI(imageUri);
                }else{
                    cursor.moveToFirst();
                    Log.d("ANDROID","先頭の画像に戻りました。");
                    //画像表示
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                    imageVIew.setImageURI(imageUri);
                }
                Log.d("ANDROID","次の画像です。");
            }
        });

        Return_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("ANDROID","戻るボタンを押しました。");
                if(cursor.moveToPrevious()){
                    //画像表示
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                    imageVIew.setImageURI(imageUri);
                }else{
                    cursor.moveToLast();
                    Log.d("ANDROID","最後尾の画像に戻りました。");
                    //画像表示
                    int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                    Long id = cursor.getLong(fieldIndex);
                    Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                    ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                    imageVIew.setImageURI(imageUri);
                }
            }
        });

        AutoPlay_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mTimer == null){
                    AutoPlay_button.setText("停止");
                    Return_button.setEnabled(false);
                    MoveOn_button.setEnabled(false);
                    // タイマーの作成
                    mTimer = new Timer();
                    // タイマーの始動
                    mTimer.schedule(new TimerTask() {
                        @Override
                        public void run(){
                            mTimerSec += 0.1;

                            mHandler.post(new Runnable() {
                                @Override
                                public void run() {
                                    //次の画像を取得
                                    if(cursor.moveToNext()){
                                        //画像表示
                                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                        Long id = cursor.getLong(fieldIndex);
                                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                                        imageVIew.setImageURI(imageUri);
                                    }else{
                                        cursor.moveToFirst();
                                        Log.d("ANDROID","先頭の画像に戻りました。");
                                        //画像表示
                                        int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                                        Long id = cursor.getLong(fieldIndex);
                                        Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                                        ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
                                        imageVIew.setImageURI(imageUri);
                                    }
                                }
                            });
                        }
                    },2000,2000);
                }else{
                    mTimer.cancel();
                    mTimer = null;
                    AutoPlay_button.setText("再生");
                    Return_button.setEnabled(true);
                    MoveOn_button.setEnabled(true);
                }

            }
        });
    };

    // 画像の情報を取得する
    public void getContentsInfo() {

        // 画像の情報を取得する
        ContentResolver resolver = getContentResolver();
        cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );

        if (cursor.moveToFirst()) { //検索結果が1つも無ければfalseを返すのでif文の{}には入らずすぐに終わります。

            //1枚の画像表示
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getContentsInfo();
                    Log.d("ANDROID", "許可された");
                    AutoPlay_button.setEnabled(true);
                    Return_button.setEnabled(true);
                    MoveOn_button.setEnabled(true);
                } else {
                    Log.d("ANDROID", "許可されなかった");
                    AutoPlay_button.setEnabled(false);
                    Return_button.setEnabled(false);
                    MoveOn_button.setEnabled(false);
                    showAlertDialog();
                }
                break;
            default:
                break;
        }
    }

    public void  showAlertDialog(){
        // AlertDialog.Builderクラスを使ってAlertDialogの準備をする
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("入力エラー");
        alertDialogBuilder.setMessage("再起動して、画像の共有の承諾をお願いします。");
        alertDialogBuilder.setPositiveButton("OK", null);
        alertDialogBuilder.show();
    }
}
