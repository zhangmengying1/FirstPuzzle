package com.example.mypuzzle;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Switch;
import android.widget.TableLayout;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.Random;

/**
 * Created by DoubleJ on 2017/7/10.
 */

public class FirstView extends Activity {
    //public static int isMusic = 3;//1表示开音乐，0表示未开音乐

    //int isEffect = 1;//1表示开音效，0表示未开音效
    int patternx = 0;//模式,0为普通玩家，2位骨灰级玩家，1位挑战
    public static String curUserName = "";
    private MyDatabaseHelper helper;
    LinearLayout backGroundTotal;
    Button setting;
    Button pattern;
    Button rule;
    Button login;
    Button register;
    Button back;
    Dialog dialog;
    private Switch st_music;
    Switch st_soundEffect;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(R.style.Color1SwitchStyle);
        setContentView(R.layout.activity_first);
        helper = new MyDatabaseHelper(this, "PuzzleGame.db", null, 1);
        backGroundTotal = (LinearLayout)findViewById(R.id.backGroundTotal);
        mediaPlayer= MediaPlayer.create(this,R.raw.huanchang);
        st_music = (Switch) findViewById(R.id.music1);
        st_soundEffect = (Switch)findViewById(R.id.musicEffect);
        if(curUserName == ""){
            music();
            soundEffect();
        }
        if (mediaPlayer.isPlaying()){
            st_music.setChecked(true);
        }
        else {
            st_music.setChecked(false);
        }




        //插入数据
        SQLiteDatabase dbInsert = helper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("userName", "Cindy");
        values.put("steps", 50);
        values.put("times", "01:21");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();
        values.put("userName", "Cici");
        values.put("steps", 44);
        values.put("times", "00:55");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();
        values.put("userName", "Bob");
        values.put("steps", 66);
        values.put("times", "02:10");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();
        values.put("userName", "Mary");
        values.put("steps", 55);
        values.put("times", "01:00");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();
        values.put("userName", "Lisa");
        values.put("steps", 120);
        values.put("times", "03:10");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();
        values.put("userName", "Mike");
        values.put("steps", 89);
        values.put("times", "01:25");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();
        values.put("userName", "Chock");
        values.put("steps", 71);
        values.put("times", "02:18");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();
        values.put("userName", "Baby");
        values.put("steps", 151);
        values.put("times", "03:20");
        dbInsert.insert("EasyRankingList", null, values);
        values.clear();

        values.put("userName", "Mary");
        values.put("steps", 255);
        values.put("times", "04:00");
        dbInsert.insert("MiddleRankingList", null, values);
        values.clear();
        values.put("userName", "Lisa");
        values.put("steps", 182);
        values.put("times", "03:10");
        dbInsert.insert("MiddleRankingList", null, values);
        values.clear();
        values.put("userName", "Mike");
        values.put("steps", 143);
        values.put("times", "02:25");
        dbInsert.insert("MiddleRankingList", null, values);
        values.clear();
        values.put("userName", "Chock");
        values.put("steps", 264);
        values.put("times", "03:18");
        dbInsert.insert("MiddleRankingList", null, values);
        values.clear();
        values.put("userName", "Baby");
        values.put("steps", 331);
        values.put("times", "05:20");
        dbInsert.insert("MiddleRankingList", null, values);
        values.clear();

        values.put("userName", "Mike");
        values.put("steps", 551);
        values.put("times", "07:25");
        dbInsert.insert("HardRankingList", null, values);
        values.clear();
        values.put("userName", "Chock");
        values.put("steps", 613);
        values.put("times", "08:18");
        dbInsert.insert("HardRankingList", null, values);
        values.clear();
        values.put("userName", "Baby");
        values.put("steps", 256);
        values.put("times", "03:20");
        dbInsert.insert("HardRankingList", null, values);
        values.clear();


        //插入数据

        helper.getWritableDatabase();
        setting = (Button) findViewById(R.id.setting);
        pattern = (Button) findViewById(R.id.pattern);
        rule = (Button) findViewById(R.id.rule);
        login = (Button) findViewById(R.id.login);
        register = (Button) findViewById(R.id.register);

        Typeface typeface = Typeface.createFromAsset(getAssets(),
                "Satisfy-Regular.ttf");
        login.setTypeface(typeface);

        back = (Button) findViewById(R.id.back);
        dialog = new Dialog(this, R.style.dialog);
        LayoutInflater inflater = LayoutInflater.from(this);//把布局扩展进来
        View dialogView = inflater.inflate(R.layout.dialog_info, null);
        dialog.setContentView(dialogView);
        //dialog中的确定按钮
        Button btSure = (Button) dialogView.findViewById(R.id.btSure);
        btSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        //rule点击事件
        rule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.show();
            }
        });


        //设置点击事件
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st_music.setVisibility(View.VISIBLE);
                st_soundEffect.setVisibility(View.VISIBLE);
                back.setVisibility(View.VISIBLE);
                //backGroundTotal.setAlpha(0.5f);
            }
        });
        //音效背景音乐

        //返回按钮监听事件
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                st_music.setVisibility(View.INVISIBLE);
                st_soundEffect.setVisibility(View.INVISIBLE);
                back.setVisibility(View.INVISIBLE);
                //backGroundTotal.setAlpha(1.0f);
            }
        });


        //骨灰级玩家入口
        pattern.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patternx = 2;
                //判断登录状态
                if(curUserName!=""){
                    Intent intent = new Intent(FirstView.this,PuzzleActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pattern",2);
                    bundle.putString("curUserName",curUserName);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else {
                    Toast.makeText(FirstView.this,"别急，先登录嘛",Toast.LENGTH_LONG).show();
                    //登录
                    funLogin();
                }
            }
        });




        //普通玩家登录
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                patternx = 0;
                //判断登录状态
                if(curUserName!=""){
                    Intent intent = new Intent(FirstView.this,MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("pattern",0);
                    bundle.putString("curUserName",curUserName);
                    intent.putExtras(bundle);
                    startActivity(intent);

                }else {
                    funLogin();
                }
            }
        });
        //注册监听事件
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                funRegister();

            }
        });


    }

    //用户名是否存在，true存在，false不存在
    public boolean isExist(String table, String name) {
        boolean is = false;
        SQLiteDatabase dbsearch = helper.getWritableDatabase();
        Cursor cursor = dbsearch.query(table, null, null, null, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String nameExist = cursor.getString(cursor.getColumnIndex("userName"));
                if (name.equals(nameExist)) {
                    is = true;
                    break;
                }

            }
            cursor.close();

        } else {
            is = false;
        }
        return is;

    }

    //验证登录,true验证成功，false验证失败
    public boolean isChecked(String name, String password) {
        String table = "User";
        boolean is = true;
        SQLiteDatabase dbCheck = helper.getWritableDatabase();
        if (isExist(table, name)) {
            Cursor cursor = dbCheck.query("User", new String[]{"userPassword"}, "userName = ?",
                    new String[]{name}, null, null, null);
            if (cursor.moveToFirst()) {
                String passwordExist = cursor.getString(0);
                if (!password.equals(passwordExist)) {
                    is = false;
                }

            }

        } else {
            is = false;
        }
        return is;
    }

    //游戏音效


    public void music() {
        st_music = (Switch) findViewById(R.id.music1);
        mediaPlayer.setLooping(true);
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
        st_music.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {//开关打开
                    mediaPlayer.setLooping(true);
                        mediaPlayer.start();
                } else {

                    mediaPlayer.pause();
                }
            }
        });
    }

    public static boolean isSoundEffect = true;
    private void soundEffect(){
        st_soundEffect.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(!isChecked){//音效关闭
                    isSoundEffect = false;
                     }
                else {
                    isSoundEffect = true;
                    }
            }
        });
    }

    //代码优化——登录
    private void funLogin(){
        TableLayout loginForm = (TableLayout) getLayoutInflater().
                inflate(R.layout.dialog_login, null);
        final EditText loginName = (EditText) loginForm.findViewById(R.id.loginName);
        final EditText loginPassword = (EditText) loginForm.findViewById(R.id.loginPassword);
        final AlertDialog dialogr = new AlertDialog.Builder(FirstView.this,R.style.dialog).
                setTitle("登录").setView(loginForm).setPositiveButton("登录", null).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create();
        dialogr.show();
        dialogr.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = loginName.getText().toString();
                String password = loginPassword.getText().toString();
                if (loginName.getText().toString().length() == 0
                        || loginPassword.getText().toString().length() == 0) {
                    Toast.makeText(FirstView.this, "用户名和密码不能为空！", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (isChecked(name, password)) {
                        curUserName = name;
                        //把用户名存入数据库中
                        SQLiteDatabase dbInsertName = helper.getWritableDatabase();
                        ContentValues valuesInsert = new ContentValues();
                        valuesInsert.put("userName", curUserName);
                        valuesInsert.put("steps", 0);
                        valuesInsert.put("times", "00:00");
                        if (!isExist("EasyRankingList", curUserName)) {
                            dbInsertName.insert("EasyRankingList", null, valuesInsert);
                        }
                        if (!isExist("MiddleRankingList", curUserName)) {
                            dbInsertName.insert("MiddleRankingList", null, valuesInsert);
                        }
                        if (!isExist("HardRankingList", curUserName)) {
                            dbInsertName.insert("HardRankingList", null, valuesInsert);
                        }
                        //跳转到拼图界面
                        if(patternx == 2){
                            Intent intent = new Intent(FirstView.this,PuzzleActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("pattern",2);
                            bundle.putString("curUserName",curUserName);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        }
                        if (patternx == 0){
                            Intent intent1 = new Intent(FirstView.this, MainActivity.class);
                            Bundle bundle1 = new Bundle();
                            bundle1.putInt("pattern",0);
                            bundle1.putString("curUserName",curUserName);
                            intent1.putExtras(bundle1);
                            startActivity(intent1);
                        }
                        if (patternx == 1){
                            Intent intent2 = new Intent(FirstView.this, MainActivity.class);
                            Bundle bundle2 = new Bundle();
                            bundle2.putInt("pattern",1);
                            bundle2.putString("curUserName",curUserName);
                            intent2.putExtras(bundle2);
                            startActivity(intent2);
                        }

                        dialogr.dismiss();
                    } else {
                        loginName.setText(null);
                        loginPassword.setText(null);
                        Toast.makeText(FirstView.this, "用户名或密码错误！请重新输入！", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }
    //注册
    private void funRegister(){
        TableLayout registerForm = (TableLayout) getLayoutInflater().
                inflate(R.layout.dialog_register, null);
        final EditText registerName = (EditText) registerForm.findViewById(R.id.name);
        final EditText registerPassword = (EditText) registerForm.findViewById(R.id.password);
        final AlertDialog dialogr = new AlertDialog.Builder(FirstView.this,R.style.dialog).
                setTitle("注册").setView(registerForm).setPositiveButton("注册", null).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                }).create();
        dialogr.show();
        dialogr.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = registerName.getText().toString();
                String password = registerPassword.getText().toString();
                String table = "User";

                if (registerName.getText().toString().length() == 0 ||
                        registerPassword.getText().toString().length() == 0) {
                    Toast.makeText(FirstView.this, "用户名和密码不能为空！", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    if (isExist(table, name)) {
                        registerName.setText(null);
                        Toast.makeText(FirstView.this, "用户名已存在，请重新输入用户名！", Toast.LENGTH_LONG).show();
                    } else {
                        SQLiteDatabase dbInsert = helper.getWritableDatabase();
                        ContentValues valuesInsert = new ContentValues();

                        valuesInsert.put("userName", name);
                        valuesInsert.put("userPassword", password);
                        dbInsert.insert("User", null, valuesInsert);
                        Toast.makeText(FirstView.this, "Register Success", Toast.LENGTH_LONG).show();
                        dialogr.dismiss();
                    }

                }

            }
        });

    }

}
