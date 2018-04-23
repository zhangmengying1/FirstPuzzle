package com.example.mypuzzle;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by DoubleJ on 2017/7/12.
 */

public class RankingListActivity extends Activity {
    private ArrayAdapter<String> mAdapter;
    private String[] mStringArray;
    private MyDatabaseHelper helper;
    String curUserName ;
    String tablex;
    int grade;
    //int grade = 0;
    Spinner selectGrade;
    TextView ci1;
    TextView ci2;
    TextView ci3;
    TextView ci4;
    TextView ci5;
    TextView ci6;
    TextView ci7;
    TextView ci8;
    TextView userName1;
    TextView userName2;
    TextView userName3;
    TextView userName4;
    TextView userName5;
    TextView userName6;
    TextView userName7;
    TextView userName8;
    TextView steps1;
    TextView steps2;
    TextView steps3;
    TextView steps4;
    TextView steps5;
    TextView steps6;
    TextView steps7;
    TextView steps8;
    TextView times1;
    TextView times2;
    TextView times3;
    TextView times4;
    TextView times5;
    TextView times6;
    TextView times7;
    TextView times8;
    Button challenge1;
    Button challenge2;
    Button challenge3;
    Button challenge4;
    Button challenge5;
    Button challenge6;
    Button challenge7;
    Button challenge8;
    TextView ciSelf;
    TextView userSelf;
    TextView stepsSelf;
    TextView timesSelf;
    TextView fail;
    Button back;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankinglist);
        init();
        curUserName = FirstView.curUserName;
        grade = PuzzleActivity.grade;

        helper = new MyDatabaseHelper(this,"PuzzleGame.db",null,1);
        back = (Button)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentback =new Intent(RankingListActivity.this,MainActivity.class);
                startActivity(intentback);
            }
        });

        ci1 = (TextView)findViewById(R.id.ci1);
        ci2 = (TextView)findViewById(R.id.ci2);
        ci3 = (TextView)findViewById(R.id.ci3);
        ci4 = (TextView)findViewById(R.id.ci4);
        ci5 = (TextView)findViewById(R.id.ci5);
        ci6 = (TextView)findViewById(R.id.ci6);
        ci7 = (TextView)findViewById(R.id.ci7);
        ci8 = (TextView)findViewById(R.id.ci8);

        userName1 = (TextView)findViewById(R.id.userName1);
        userName2 = (TextView)findViewById(R.id.userName2);
        userName3 = (TextView)findViewById(R.id.userName3);
        userName4 = (TextView)findViewById(R.id.userName4);
        userName5 = (TextView)findViewById(R.id.userName5);
        userName6 = (TextView)findViewById(R.id.userName6);
        userName7 = (TextView)findViewById(R.id.userName7);
        userName8 = (TextView)findViewById(R.id.userName8);

        steps1 = (TextView)findViewById(R.id.steps1);
        steps2 = (TextView)findViewById(R.id.steps2);
        steps3 = (TextView)findViewById(R.id.steps3);
        steps4 = (TextView)findViewById(R.id.steps4);
        steps5 = (TextView)findViewById(R.id.steps5);
        steps6 = (TextView)findViewById(R.id.steps6);
        steps7 = (TextView)findViewById(R.id.steps7);
        steps8 = (TextView)findViewById(R.id.steps8);

        times1 = (TextView)findViewById(R.id.times1);
        times2 = (TextView)findViewById(R.id.times2);
        times3 = (TextView)findViewById(R.id.times3);
        times4 = (TextView)findViewById(R.id.times4);
        times5 = (TextView)findViewById(R.id.times5);
        times6 = (TextView)findViewById(R.id.times6);
        times7 = (TextView)findViewById(R.id.times7);
        times8 = (TextView)findViewById(R.id.times8);

        challenge1 = (Button)findViewById(R.id.challenge1);
        challenge2 = (Button)findViewById(R.id.challenge2);
        challenge3 = (Button)findViewById(R.id.challenge3);
        challenge4 = (Button)findViewById(R.id.challenge4);
        challenge5 = (Button)findViewById(R.id.challenge5);
        challenge6 = (Button)findViewById(R.id.challenge6);
        challenge7 = (Button)findViewById(R.id.challenge7);
        challenge8 = (Button)findViewById(R.id.challenge8);

        ciSelf = (TextView)findViewById(R.id.ciSelf);
        userSelf = (TextView)findViewById(R.id.userSelf);
        stepsSelf = (TextView)findViewById(R.id.stepsSelf);
        timesSelf = (TextView)findViewById(R.id.timesSelf);
        fail = (TextView)findViewById(R.id.fail);

        //选择游戏等级
        selectGrade = (Spinner)findViewById(R.id.selectGrade);
        if(grade == 0){
            selectGrade.setSelection(0);
        }
        else if(grade == 1){
            selectGrade.setSelection(1);
        }
        else {
            selectGrade.setSelection(2);
        }
        selectGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                if(position==0){
                    tablex = "EasyRankingList";
                    setInvisible();
                    showRankingList("EasyRankingList");
                }
                if (position==1){
                    tablex = "MiddleRankingList";
                    setInvisible();
                    showRankingList("MiddleRankingList");
                }
                if (position==2){
                    tablex = "HardRankingList";
                    setInvisible();
                    showRankingList("HardRankingList");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


    }
    public void showRankingList(String table){
        SQLiteDatabase dbSort = helper.getWritableDatabase();
        Cursor cursor = dbSort.query(table,null,"steps > ?",
                new String[]{"0"},"steps",null,null);
        int i = 1;
        fail.setText("无记录");
        if (cursor.moveToFirst()){
            do{
                String name = cursor.getString(cursor.getColumnIndex("userName"));
                int steps = cursor.getInt(cursor.getColumnIndex("steps"));
                String times = cursor.getString(cursor.getColumnIndex("times"));
                if(name.equals(curUserName)){
                    userSelf.setVisibility(View.VISIBLE);
                    ciSelf.setVisibility(View.VISIBLE);
                    stepsSelf.setVisibility(View.VISIBLE);
                    timesSelf.setVisibility(View.VISIBLE);
                    fail.setVisibility(View.VISIBLE);
                    ciSelf.setText(String.valueOf(i));
                    userSelf.setText(name);
                    stepsSelf.setText(String.valueOf(steps));
                    timesSelf.setText(times);

                    if(i<=8 && steps != 0){
                        fail.setText("已上榜");
                    }
                    if(steps>0 && i>8){
                        fail.setText("未上榜");
                    }
                }
                switch (i){
                    case 1:
                        ci1.setVisibility(View.VISIBLE);
                        userName1.setVisibility(View.VISIBLE);
                        steps1.setVisibility(View.VISIBLE);
                        times1.setVisibility(View.VISIBLE);
                        challenge1.setVisibility(View.VISIBLE);
                        userName1.setText(name);
                        steps1.setText(String.valueOf(steps));
                        times1.setText(times);
                        break;
                    case 2:
                        ci2.setVisibility(View.VISIBLE);
                        userName2.setVisibility(View.VISIBLE);
                        steps2.setVisibility(View.VISIBLE);
                        times2.setVisibility(View.VISIBLE);
                        challenge2.setVisibility(View.VISIBLE);
                        userName2.setText(name);
                        steps2.setText(String.valueOf(steps));
                        times2.setText(times);
                        break;
                    case 3:
                        ci3.setVisibility(View.VISIBLE);
                        userName3.setVisibility(View.VISIBLE);
                        steps3.setVisibility(View.VISIBLE);
                        times3.setVisibility(View.VISIBLE);
                        challenge3.setVisibility(View.VISIBLE);
                        userName3.setText(name);
                        steps3.setText(String.valueOf(steps));
                        times3.setText(times);
                        break;
                    case 4:
                        ci4.setVisibility(View.VISIBLE);
                        userName4.setVisibility(View.VISIBLE);
                        steps4.setVisibility(View.VISIBLE);
                        times4.setVisibility(View.VISIBLE);
                        challenge4.setVisibility(View.VISIBLE);
                        userName4.setText(name);
                        steps4.setText(String.valueOf(steps));
                        times4.setText(times);
                        break;
                    case 5:
                        ci5.setVisibility(View.VISIBLE);
                        userName5.setVisibility(View.VISIBLE);
                        steps5.setVisibility(View.VISIBLE);
                        times5.setVisibility(View.VISIBLE);
                        challenge5.setVisibility(View.VISIBLE);
                        userName5.setText(name);
                        steps5.setText(String.valueOf(steps));
                        times5.setText(times);
                        break;
                    case 6:
                        ci6.setVisibility(View.VISIBLE);
                        userName6.setVisibility(View.VISIBLE);
                        steps6.setVisibility(View.VISIBLE);
                        times6.setVisibility(View.VISIBLE);
                        challenge6.setVisibility(View.VISIBLE);
                        userName6.setText(name);
                        steps6.setText(String.valueOf(steps));
                        times6.setText(times);
                        break;
                    case 7:
                        ci7.setVisibility(View.VISIBLE);
                        userName7.setVisibility(View.VISIBLE);
                        steps7.setVisibility(View.VISIBLE);
                        times7.setVisibility(View.VISIBLE);
                        challenge7.setVisibility(View.VISIBLE);
                        userName7.setText(name);
                        steps7.setText(String.valueOf(steps));
                        times7.setText(times);
                        break;
                    case 8:
                        ci8.setVisibility(View.VISIBLE);
                        userName8.setVisibility(View.VISIBLE);
                        steps8.setVisibility(View.VISIBLE);
                        times8.setVisibility(View.VISIBLE);
                        challenge8.setVisibility(View.VISIBLE);
                        userName8.setText(name);
                        steps8.setText(String.valueOf(steps));
                        times8.setText(times);
                        break;
                    default:
                        break;
                }
                i = i + 1;
            }while (cursor.moveToNext());

            cursor.close();

        }

    }
    public void setInvisible(){
        ci1.setVisibility(View.INVISIBLE);
        ci2.setVisibility(View.INVISIBLE);
        ci3.setVisibility(View.INVISIBLE);
        ci4.setVisibility(View.INVISIBLE);
        ci5.setVisibility(View.INVISIBLE);
        ci6.setVisibility(View.INVISIBLE);
        ci7.setVisibility(View.INVISIBLE);
        ci8.setVisibility(View.INVISIBLE);
        userName1.setVisibility(View.INVISIBLE);
        userName2.setVisibility(View.INVISIBLE);
        userName3.setVisibility(View.INVISIBLE);
        userName4.setVisibility(View.INVISIBLE);
        userName5.setVisibility(View.INVISIBLE);
        userName6.setVisibility(View.INVISIBLE);
        userName7.setVisibility(View.INVISIBLE);
        userName8.setVisibility(View.INVISIBLE);
        steps1.setVisibility(View.INVISIBLE);
        steps2.setVisibility(View.INVISIBLE);
        steps3.setVisibility(View.INVISIBLE);
        steps4.setVisibility(View.INVISIBLE);
        steps5.setVisibility(View.INVISIBLE);
        steps6.setVisibility(View.INVISIBLE);
        steps7.setVisibility(View.INVISIBLE);
        steps8.setVisibility(View.INVISIBLE);
        times1.setVisibility(View.INVISIBLE);
        times2.setVisibility(View.INVISIBLE);
        times3.setVisibility(View.INVISIBLE);
        times4.setVisibility(View.INVISIBLE);
        times5.setVisibility(View.INVISIBLE);
        times6.setVisibility(View.INVISIBLE);
        times7.setVisibility(View.INVISIBLE);
        times8.setVisibility(View.INVISIBLE);
        challenge1.setVisibility(View.INVISIBLE);
        challenge2.setVisibility(View.INVISIBLE);
        challenge3.setVisibility(View.INVISIBLE);
        challenge4.setVisibility(View.INVISIBLE);
        challenge5.setVisibility(View.INVISIBLE);
        challenge6.setVisibility(View.INVISIBLE);
        challenge7.setVisibility(View.INVISIBLE);
        challenge8.setVisibility(View.INVISIBLE);
        ciSelf.setVisibility(View.INVISIBLE);
        userSelf.setVisibility(View.INVISIBLE);
        stepsSelf.setVisibility(View.INVISIBLE);
        timesSelf.setVisibility(View.INVISIBLE);
    }
    public void onClick(View view){
        String challengeUser = null;
        int challengeSteps = 0;
        SQLiteDatabase dbSearchSteps = helper.getWritableDatabase();
        switch (view.getId()){
            case R.id.challenge1:
                challengeUser = userName1.getText().toString();
                Cursor cursor1 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor1.moveToFirst()){
                    challengeSteps = cursor1.getInt(0);
                }
                break;
            case R.id.challenge2:
                challengeUser = userName2.getText().toString();
                Cursor cursor2 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor2.moveToFirst()){
                    challengeSteps = cursor2.getInt(0);
                }
                break;
            case R.id.challenge3:
                challengeUser = userName3.getText().toString();
                Cursor cursor3 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor3.moveToFirst()){
                    challengeSteps = cursor3.getInt(0);
                }
                break;
            case R.id.challenge4:
                challengeUser = userName4.getText().toString();
                Cursor cursor4 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor4.moveToFirst()){
                    challengeSteps = cursor4.getInt(0);
                }
                break;
            case R.id.challenge5:
                challengeUser = userName5.getText().toString();
                Cursor cursor5 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor5.moveToFirst()){
                    challengeSteps = cursor5.getInt(0);
                }
                break;
            case R.id.challenge6:
                challengeUser = userName6.getText().toString();
                Cursor cursor6 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor6.moveToFirst()){
                    challengeSteps = cursor6.getInt(0);
                }
                break;
            case R.id.challenge7:
                challengeUser = userName7.getText().toString();
                Cursor cursor7 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor7.moveToFirst()){
                    challengeSteps = cursor7.getInt(0);
                }
                break;
            case R.id.challenge8:
                challengeUser = userName8.getText().toString();
                Cursor cursor8 = dbSearchSteps.query(tablex,new String[]{"steps"},"userName = ?",
                        new String[]{challengeUser},null,null,null);
                if(cursor8.moveToFirst()){
                    challengeSteps = cursor8.getInt(0);
                }
                break;
        }
        Intent challengeIntent = new Intent(RankingListActivity.this,
                PuzzleActivity.class);
        Bundle challengeBundle = new Bundle();
        challengeBundle.putInt("pattern",1);
        challengeBundle.putInt("challengeSteps",challengeSteps);
        challengeBundle.putString("challengeGrade",tablex);
        challengeBundle.putString("curUserName",curUserName);
        challengeIntent.putExtras(challengeBundle);
        startActivity(challengeIntent);
    }
    /*
   修改Spinner字体颜色
    */
    private void init(){
        selectGrade = (Spinner)findViewById(R.id.selectGrade);
        mStringArray = getResources().getStringArray(R.array.game_grades);
        mAdapter = new TestArrayAdapterRankingList(RankingListActivity.this,mStringArray);
        selectGrade.setAdapter(mAdapter);
    }

}
