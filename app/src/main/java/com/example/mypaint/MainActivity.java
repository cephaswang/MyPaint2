package com.example.mypaint;

// https://www.ssaurel.com/blog/learn-to-create-a-paint-application-for-android/
// https://www.youtube.com/watch?v=uJGcmGXaQ0o
// Learn to create a Paint Application with Android Studio

//  Swift 4.2 Draw Something with CGContext and Canvas View
//  https://www.youtube.com/watch?v=E2NTCmEsdSE
//  https://www.youtube.com/watch?v=7vDfL0K6Jm8

import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.HashSet;
import java.util.Set;


public class MainActivity extends AppCompatActivity {

    private SeekBar sb_normal;

    private PaintView paintView;
    private final static int REQ_PERMISSIONS = 0;
    Button  buttonBack;

    Bitmap  bmScreen;
    View    screen;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        paintView = (PaintView) findViewById(R.id.paintView);
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        paintView.init(metrics);

        screen = (View) findViewById(R.id.paintView);
        sb_normal  = (SeekBar) findViewById(R.id.seekBar);
        buttonBack = (Button) findViewById(R.id.buttonBack);

        sb_normal.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // txt_cur.setText("当前进度值:" + progress + "  / 100 ");
                paintView.setStrokeWidth(progress);
                buttonBack.setText( String.valueOf(progress + 1) );
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(mContext, "触碰SeekBar", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
               // Toast.makeText(mContext, "放开SeekBar", Toast.LENGTH_SHORT).show();
                buttonBack.setText("Undo");
            }
        });

        askPermissions();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.normal:
                paintView.normal();
                return true;
            case R.id.emboss:
                paintView.emboss();
                return true;
            case R.id.blur:
                paintView.blur();
                return true;
            case R.id.clear:
                paintView.clear();
                return true;
            case R.id.save:

                screen.setDrawingCacheEnabled(true);
                bmScreen = screen.getDrawingCache();
                paintView.saveImage(bmScreen);

                screen.setDrawingCacheEnabled(false);
                bmScreen = null;
                return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void askPermissions() {

        String[] PERMISSION_S = {
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                android.Manifest.permission.READ_EXTERNAL_STORAGE
        };


        Set<String> permissionsRequest = new HashSet<>();
        for (String permission : PERMISSION_S) {
            int result = ContextCompat.checkSelfPermission(this, permission);
            if (result != PackageManager.PERMISSION_GRANTED) {
                permissionsRequest.add(permission);
            }
        }

        if (!permissionsRequest.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    permissionsRequest.toArray(new String[permissionsRequest.size()]),
                    REQ_PERMISSIONS);
        }
        System.out.println("permissionsRequest:" + permissionsRequest.size());
    }

    @SuppressLint("Override")
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQ_PERMISSIONS:
                for (int result : grantResults) {
                    if (result != PackageManager.PERMISSION_GRANTED) {
                       // String text = getString( R.string.text_ShouldGrant) + " : "+ result ;
                       // Toast.makeText(this, text, Toast.LENGTH_LONG).show();
                       // handler.postDelayed(GotoMenu, 4000);
                        return;
                    }
                }
                break;
        }
    }


    public void onBack(View view) {
        paintView.onBack(screen);

    }

    public void onColorYellow (View view){
        paintView.setStrokeColor(Color.YELLOW);
    }
    public void onColorRed (View view){
        paintView.setStrokeColor(Color.RED);
    }
    public void onColorBlue (View view){
        paintView.setStrokeColor(Color.BLUE);
    }
    public void onColorBlack (View view){
        paintView.setStrokeColor(Color.BLACK);
    }

}