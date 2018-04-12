package jp.techacademy.yamazawa.kouta.autoslideshowapp;

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.util.Log;

import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 100;

    //コンストラクタ
    Timer timer;
    Button button;

    //パーミッションの許可状態を確認する
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        ContentResolver resolver = getContentResolver();
        Cursor cursor = resolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, // データの種類
                null, // 項目(null = 全項目)
                null, // フィルタ条件(null = フィルタなし)
                null, // フィルタ用パラメータ
                null // ソート (null ソートなし)
        );
        /*if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
        //許可されている
            // 画像の情報を取得する
            getContentsInfo();
            //進むボタンを押す。
            if(/*表示画像が最後まで進んだ時*/cursor.moveToLast){
                //先頭の画像を表示
                cursor.moveToFirst();
                int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
                Long id = cursor.getLong(fieldIndex);
                Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

                ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);
            }else{
                //次の画像を表示
                cursor.moveToNext();
            }
            //戻るボタンを押す。
            if(/*表示画像が最初に戻った時*/cursor.moveToFirst()){
                //最後尾の画像を表示
            }else{
                //前の画像を表示
            }

            //　再生/停止ボタンを押す。
            if(//再生中？){
                //停止する
                //戻るボタン、進むボタンを有効にする
            }else{
                //2秒ごとに自動送りを始める
                //戻るボタン、進むボタンを無効にする
            }
        }else{
            //拒否されている
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_CODE);
        }*/
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
            int fieldIndex = cursor.getColumnIndex(MediaStore.Images.Media._ID);
            Long id = cursor.getLong(fieldIndex);
            Uri imageUri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id);

            ImageView imageVIew = (ImageView) findViewById(R.id.imageView);
            imageVIew.setImageURI(imageUri);
        }
        cursor.close();
    }
}
