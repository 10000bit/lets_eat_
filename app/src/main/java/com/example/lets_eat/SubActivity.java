package com.example.lets_eat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lets_eat.databinding.ActivityMainBinding;
import com.example.lets_eat.databinding.ActivitySubBinding;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.Locale;

import uk.co.senab.photoview.PhotoViewAttacher;

public class SubActivity extends AppCompatActivity {
    private static ActivitySubBinding binding;
    private ChipNavigationBar chipnavigationbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySubBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        ImageView imgView = binding.imageView;
        chipnavigationbar = binding.chipbar;
        chipnavigationbar.setItemSelected(R.id.chipbar,true);
        bottonMenu();

        PhotoViewAttacher photoView = new PhotoViewAttacher(imgView);
        photoView.update();
        String imageUrl = "https://www.hansung.ac.kr/portlet-repositories/fckeditor/images/7qHyuRAiKcc=/1595204995118.png";
        Glide.with(this).load(imageUrl).into(imgView);

        binding.facultiesMenu.setMovementMethod(new ScrollingMovementMethod()); //스크롤

        JsoupAsyncTask jsoupAsyncTask = new JsoupAsyncTask();
        jsoupAsyncTask.execute();


    }

    private void bottonMenu() {
        chipnavigationbar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            final Intent intent6 = new Intent(SubActivity.this, Recommendation.class);
            final Intent intent5 = new Intent(SubActivity.this, confusion.class);
            final Intent intent3 = new Intent(SubActivity.this, notification_list.class);
            final Intent intent4 = new Intent(SubActivity.this, Rating.class);
            final Intent intent8 = new Intent(SubActivity.this, Person.class);
            final Intent intent9 = new Intent(SubActivity.this, SubActivity.class);
            @Override
            public void onItemSelected(int i) {
                switch(i){
                    case R.id.item0:
                        Toast.makeText(SubActivity.this, "홈", Toast.LENGTH_SHORT).show();
                        startActivity(intent9);
                        break;
                    case R.id.item1:
                        Toast.makeText(SubActivity.this, "추천메뉴", Toast.LENGTH_SHORT).show();
                        startActivity(intent6);
                        break;
                    case R.id.item2:
                        Toast.makeText(SubActivity.this, "한줄 건의함", Toast.LENGTH_SHORT).show();
                        startActivity(intent5); //혼잡도 창으로 가기
                        break;
                    case R.id.item3:
                        Toast.makeText(SubActivity.this, "알림", Toast.LENGTH_SHORT).show();
                        startActivity(intent3); //알림 창으로 가기
                        break;
                    case R.id.item4:
                        Toast.makeText(SubActivity.this, "리뷰", Toast.LENGTH_SHORT).show();
                        startActivity(intent4); //리뷰 창으로 가기
                        break;
                    case R.id.item5:
                        Toast.makeText(SubActivity.this, "계정 정보", Toast.LENGTH_SHORT).show();
                        startActivity(intent8); //계정 정보 창으로 가기
                        break;

                }
            }
        });
    }

    static class JsoupAsyncTask extends AsyncTask<Void, Void, Void> {
        private String htmlPageUrl = "https://www.hansung.ac.kr/web/www/life_03_01_t2"; //파싱할 홈페이지의 URL주소
        private String htmlContentInStringFormat = "";
        Date currentTime = Calendar.getInstance().getTime();
        SimpleDateFormat weekdayFormat = new SimpleDateFormat("EE", Locale.ENGLISH);

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {

            Document doc = null;
            try {
                doc = Jsoup.connect(htmlPageUrl).get();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //테스트1
            // Elements titles = doc.select("td"); //2: 월요일 3: 화요일

            // for (Element e : titles) {
            String weekday = weekdayFormat.format(currentTime);


            if (weekday.equals("Mon")) {
                htmlContentInStringFormat =doc.select("td").get(0).text();
                System.out.println(htmlContentInStringFormat);
            } else if (weekday.equals("Tue")) {
                htmlContentInStringFormat = doc.select("td").get(1).text();
                System.out.println(htmlContentInStringFormat);
            } else if (weekday.equals("Wed")) {
                htmlContentInStringFormat = doc.select("td").get(2).text();
                System.out.println(htmlContentInStringFormat);
            } else if (weekday.equals("Thu")) {
                htmlContentInStringFormat = doc.select("td").get(3).text();
                System.out.println(htmlContentInStringFormat);
            } else if (weekday.equals("Fri")) {
                htmlContentInStringFormat = doc.select("td").get(4).text();
                System.out.println(htmlContentInStringFormat);
            } else if (weekday.equals("Sat")) {
                htmlContentInStringFormat = "운영하지 않습니다.";
                System.out.println("운영하지 않습니다.");
            } else if (weekday.equals("Sun")) {
                htmlContentInStringFormat = "운영하지 않습니다.";
                System.out.println("운영하지 않습니다.");
            }

            return null;

        }
        @Override
        protected void onPostExecute(Void result) {

            binding.facultiesMenu.setText(htmlContentInStringFormat);
        }
    }

}