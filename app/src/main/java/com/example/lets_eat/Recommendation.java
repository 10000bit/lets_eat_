package com.example.lets_eat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lets_eat.databinding.ActivityRecommendationBinding;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;

import java.util.ArrayList;

public class Recommendation extends AppCompatActivity {
    private ActivityRecommendationBinding mBinding;
    FirebaseDatabase rootNode;
    DatabaseReference mDatabase;
    Button btn_search;
    EditText menu;
    private ChipNavigationBar chipnavigationbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mBinding = ActivityRecommendationBinding.inflate(getLayoutInflater());
        setContentView(mBinding.getRoot());
        btn_search = mBinding.search;
        menu = mBinding.menu;
        //if(R.id.ratingBar)

        chipnavigationbar = mBinding.chipbar;
        chipnavigationbar.setItemSelected(R.id.chipbar,true);
        bottonMenu();
        // 빈 데이터 리스트 생성.
        final ArrayList<String> items = new ArrayList<String>();

        // ArrayAdapter 생성. 아이템 View를 선택(single choice)가능하도록 만듦.
        final ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, items);
        final ListView listview = (ListView) findViewById(R.id.listview);
        listview.setAdapter(adapter);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference databaseRef = database.getReference("review");


        // Read from the database
        databaseRef.child("user").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                adapter.clear();
                // 클래스 모델이 필요?
                for (DataSnapshot user : dataSnapshot.getChildren()) {
                    //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                    // reviw 안에 있는 user 안의 review를 listview에 넣기
                    String str = user.child("menuname").getValue(String.class);
                    String strs = user.child("review").getValue(String.class);
                    String strss = user.child("star").getValue(String.class);
                    Log.i("TAG: value is ", str);
                    items.add(str + "\n리뷰: " + strs + "\n별점: " + strss);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });

        menu.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int keyCode, KeyEvent keyEvent) {
                if(keyCode==KeyEvent.KEYCODE_ENTER)
                {
                    btn_search.performClick();
                    return true;
                }
                return false;
            }
        });
        btn_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String name = menu.getText().toString();
                databaseRef.child("user").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        adapter.clear();
                        // 클래스 모델이 필요?
                        for (DataSnapshot user : dataSnapshot.getChildren()) {
                            //MyFiles filename = (MyFiles) fileSnapshot.getValue(MyFiles.class);
                            // reviw 안에 있는 user 안의 review를 listview에 넣기
                            String str = user.child("menuname").getValue(String.class);
                            String strs = user.child("review").getValue(String.class);
                            String strss = user.child("star").getValue(String.class);
                            if (str.contains(name)) {
                                Log.i("TAG: value is ", str);
                                items.add(str + "\n리뷰: " + strs + "\n별점: " + strss);
                            }
                            else if(name.length()==0){
                                items.add(str + "\n리뷰: " + strs + "\n별점: " + strss);
                            }
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
    }
    private void bottonMenu() {
        chipnavigationbar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            final Intent intent6 = new Intent(Recommendation.this, Recommendation.class);
            final Intent intent5 = new Intent(Recommendation.this, confusion.class);
            final Intent intent3 = new Intent(Recommendation.this, notification_list.class);
            final Intent intent4 = new Intent(Recommendation.this, Rating.class);
            final Intent intent8 = new Intent(Recommendation.this, PersonActivity.class);
            final Intent intent9 = new Intent(Recommendation.this, SubActivity.class);
            @Override
            public void onItemSelected(int i) {
                switch(i){
                    case R.id.item0:
                        Toast.makeText(Recommendation.this, "홈", Toast.LENGTH_SHORT).show();
                        startActivity(intent9);
                        break;
                    case R.id.item1:
                        Toast.makeText(Recommendation.this, "추천메뉴", Toast.LENGTH_SHORT).show();
                        startActivity(intent6);
                        break;
                    case R.id.item2:
                        Toast.makeText(Recommendation.this, "한줄 건의함", Toast.LENGTH_SHORT).show();
                        startActivity(intent5); //혼잡도 창으로 가기
                        break;
                    case R.id.item3:
                        Toast.makeText(Recommendation.this, "알림", Toast.LENGTH_SHORT).show();
                        startActivity(intent3); //알림 창으로 가기
                        break;
                    case R.id.item4:
                        Toast.makeText(Recommendation.this, "리뷰", Toast.LENGTH_SHORT).show();
                        startActivity(intent4); //리뷰 창으로 가기
                        break;
                    case R.id.item5:
                        Toast.makeText(Recommendation.this, "계정 정보", Toast.LENGTH_SHORT).show();
                        startActivity(intent8); //계정 정보 창으로 가기
                        break;

                }
            }
        });
    }
}