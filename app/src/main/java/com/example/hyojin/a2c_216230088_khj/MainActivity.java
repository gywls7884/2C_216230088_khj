package com.example.hyojin.a2c_216230088_khj;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.icu.text.SimpleDateFormat;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.google.android.gms.maps.MapView;

import java.util.Date;

// 웹뷰
public class MainActivity extends Activity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedlnstanceState) {
        super.onCreate(savedlnstanceState);
        setContentView(R.layout.activity_main);

        webView = (WebView) findViewById(R.id.webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webView.getSettings().setLightTouchEnabled(true);
        webView.getSettings().setSavePassword(true);
        webView.getSettings().setSaveFormData(true);

        webView.setWebViewClient(new MyWebViewClient());

        webView.loadUrl("http://m.naver.com");
    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);

            return true;
        }
    }


//DB
    final DBHelper dbHelper = new DBHelper(getApplicationContext(), "MoneyBook.db", null, 1);

    // 테이블에 있는 모든 데이터 출력
    final TextView result = (TextView) findViewById(R.id.result);

    final EditText etDate = (EditText) findViewById(R.id.date);
    final EditText etItem = (EditText) findViewById(R.id.item);
    final EditText etPrice = (EditText) findViewById(R.id.price);

    // 날짜는 현재 날짜로 고정
    // 현재 시간 구하기
    long now = System.currentTimeMillis();
    Date date = new Date(now);
    // 출력될 포맷 설정
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy년 MM월 dd일");
        etDate.setText(simpleDateFormat.format(date));

    // DB에 데이터 추가
    Button insert = (Button) findViewById(R.id.insert);
        insert.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        String date = etDate.getText().toString();
        String item = etItem.getText().toString();
        int price = Integer.parseInt(etPrice.getText().toString());

        dbHelper.insert(date, item, price);
        result.setText(dbHelper.getResult());
    }

    });
    // DB에 있는 데이터 수정
    Button update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        String item = etItem.getText().toString();
        int price = Integer.parseInt(etPrice.getText().toString());

        dbHelper.update(item, price);
        result.setText(dbHelper.getResult());
    }
    });

    // DB에 있는 데이터 삭제
    Button delete = (Button) findViewById(R.id.delete);
        delete.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        String item = etItem.getText().toString();

        dbHelper.delete(item);
        result.setText(dbHelper.getResult());
    }
    });

    // DB에 있는 데이터 조회
    Button select = (Button) findViewById(R.id.select);
        select.setOnClickListener(new View.OnClickListener()

    {
        @Override
        public void onClick (View v){
        result.setText(dbHelper.getResult());
    }
    });

    //스레드
    private TextView textView;
    private ProgressBar progressBar;
    Handler handler = new Handler() {

        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            textView.setText("" + msg.arg1);
            progressBar.setProgress(msg.arg1);
        }

    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thread);

        textView = (TextView) findViewById(R.id.textView);
        progressBar = (ProgressBar) findViewById(R.id.progress_bar);

        new Thread(new Runnable() {
            public void run() {
                int i = 0;
                while (true) {
                    if (i > 100) {
                        break;
                    } else {
                        Message msg = handler.obtainMessage();
                        msg.arg1 = i;
                        handler.sendMessage(msg);
                    }
                    try {
                        Thread.sleep(1000);
                        i += 10;
                    } catch (InterruptedException ie) {
                        ie.printStackTrace();
                    }
                }
            }
        }).start();
    }
}








