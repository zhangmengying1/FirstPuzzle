package com.example.mypuzzle;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.nio.charset.MalformedInputException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by DoubleJ on 2017/7/3.
 */

public class PuzzleActivity extends Activity {
    private ArrayAdapter<String> mAdapter;
    private String[] mStringArray;

    private SoundPool soundPool;//声明一个SoundPool
    private int soundID;//创建某个声音对应的音频ID
    private int soundID_success;
    int[] picturesR = {R.drawable.meinvyuyeshou,R.drawable.huiguniang,
            R.drawable.nanguache,R.drawable.minimiqi,R.drawable.weinixiong,
            R.drawable.bingxueqiyuan,R.drawable.photogirl,R.drawable.meishaonvzhanshi,
            R.drawable.add};
    int[] crazyPic = {R.drawable.guruone,R.drawable.gurutwo,
    R.drawable.guruthree,R.drawable.gurufore};

    TextView stepView;
    //TextView timeView;
    Chronometer timerView;

    Spinner selectGrade;
    Dialog dialog;
    String curUserName;
    private MyDatabaseHelper helper;



    ImageView[][] imageSum = new ImageView[100][100];
    Bitmap[][] pictures = new Bitmap[100][100];
    ImageView small;
    TableLayout tableLayout;
    Bitmap bitmap;
    ImageView emptyImageView = null;
    int steps = 0;//步数
    int challengeSteps = 0;//记录挑战步数
    int times = 0;//时间
    public static int grade = 0;//记录游戏等级
    int pattern = 0;//记录游戏模式，挑战模式1还是正常入口0，2为骨灰级玩家
    String challengeGrade = null;//记录挑战游戏等级
    ArrayList<Integer> arrayList = new ArrayList<Integer>();//记录移动顺序
    int emptyx = 0,emptyy = 0;//记录空块的行列
    //自动拼图
    Handler handler = new Handler();
    int length = 0;
    Runnable myRunnable = new Runnable() {
        @Override
        public void run() {
            if(length>=0){
                Bitmap imagexy = ((BitmapDrawable) imageSum[emptyx-1][emptyy-1].
                        getDrawable()).getBitmap();
                int move = arrayList.get(length);
                switch (move){
                    case 1:
                        bottom(imagexy,emptyx,emptyy);
                        emptyx+=1;
                        break;
                    case 2:
                        top(imagexy,emptyx,emptyy);
                        emptyx-=1;
                        break;
                    case 3:
                        right(imagexy,emptyx,emptyy);
                        emptyy+=1;
                        break;
                    case 4:
                        left(imagexy,emptyx,emptyy);
                        emptyy-=1;
                        break;
                }
                length-=1;
                if(pattern==1){
                    steps = steps - 1;
                }
                else {
                    steps = steps + 1;
                }

                stepView.setText(String.valueOf(steps));
                handler.postDelayed(this,100);
            }
            else {
                dialog = new Dialog(PuzzleActivity.this,R.style.dialog);
                LayoutInflater inflater = LayoutInflater.from(PuzzleActivity.this);//把布局扩展进来
                View dialogView = inflater.inflate(R.layout.dialog_over,null);

                TextView message = (TextView)dialogView.findViewById(R.id.message);
                timerView.stop();
                emptyImageView.setVisibility(View.VISIBLE);
                message.setText("游戏结束！共走了"+ String.valueOf(steps)+"步，耗时"+timerView.getText()+"秒!");
                if(pattern==0||pattern==1){
                saveData();}
                Button btSure = (Button)dialogView.findViewById(R.id.btSure);
                btSure.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(PuzzleActivity.this,RankingListActivity.class);
                        startActivity(intent);
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        }

    };

    protected void onCreate(final Bundle savedInstanceState){

        super.onCreate(savedInstanceState);

        //全屏操作，一般放在加载视图之前
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        //获取屏幕宽高
        WindowManager manager = this.getWindowManager();
        final int width = manager.getDefaultDisplay().getWidth();
        final int heigh = manager.getDefaultDisplay().getHeight();
       // width = width - 20;

        setContentView(R.layout.activity_puzzle);
        init();
        helper = new MyDatabaseHelper(this,"PuzzleGame.db",null,1);
        Button back = (Button)findViewById(R.id.back);
        final Button help = (Button)findViewById(R.id.help);
        final Button originalMap = (Button)findViewById(R.id.originalMap);
        final Button start = (Button)findViewById(R.id.start);
        stepView = (TextView)findViewById(R.id.steps);
        timerView = (Chronometer)findViewById(R.id.timer);
        selectGrade = (Spinner)findViewById(R.id.selectGrade);

        initMusic();


        small = (ImageView)findViewById(R.id.smallview);
        tableLayout = (TableLayout)findViewById(R.id.table);
        curUserName = FirstView.curUserName;

        //获取传过来的消息
        Intent intent = getIntent();
        final Bundle bundle = intent.getExtras();
        pattern = bundle.getInt("pattern");
        curUserName = bundle.getString("curUserName");
        final TextView test = (TextView)findViewById(R.id.test);
        test.setText(curUserName);

        //获取传过来的挑战信息
        Intent getChallengeIntent = getIntent();
        Bundle getChallengeBundle = getChallengeIntent.getExtras();
        pattern = getChallengeBundle.getInt("pattern");
        challengeSteps = getChallengeBundle.getInt("challengeSteps");
        steps = getChallengeBundle.getInt("challengeSteps");
        stepView.setText(String.valueOf(steps));
        challengeGrade = bundle.getString("challengeGrade");
        curUserName = bundle.getString("curUserName");
        //Toast.makeText(PuzzleActivity.this,"pattern"+pattern,Toast.LENGTH_LONG).show();

        if (pattern ==0){
            byte [] bitMessage=intent.getByteArrayExtra(MainActivity.BITMAP);
            String message = bundle.getString("data");//通过键来取值
            if(bitMessage!=null){
                bitmap=BitmapFactory.decodeByteArray(bitMessage, 0, bitMessage.length);//byte转bitmap
                small.setImageBitmap(bitmap);
            }
            else {
                if(message!=null){
                    int imgid = Integer.parseInt(message);//Integer.parseInt(message)
                    bitmap = BitmapFactory.decodeResource(getResources(), imgid);//参数：资源和资源id
                    small.setImageBitmap(bitmap);
                }
            }
        }
        else if(pattern ==2){//疯狂玩家设置
            selectGrade.setEnabled(false);
            selectGrade.setAlpha(0.5f);
            help.setEnabled(false);
            help.setAlpha(0.5f);
            originalMap.setEnabled(false);
            originalMap.setAlpha(0.5f);
            //随机数
            int num = crazyPic.length;
            Random random = new Random();
            int k = random.nextInt(num);//产生0-num-1的随机数
            int imgId = crazyPic[k];
            //设置图片
            bitmap = BitmapFactory.decodeResource(getResources(),imgId);
            small.setImageBitmap(bitmap);
//            chooseLevel(6,6);
        }
        else {
            Random random = new Random();
            int k = random.nextInt(8);
            int imgId = picturesR[k];
            selectGrade.setEnabled(false);
            bitmap = BitmapFactory.decodeResource(getResources(),imgId);
            if(challengeGrade.equals("EasyRankingList")){
                grade = 0;
                emptyx = 3;
                emptyy = 3;
                chooseLevel(3,3);
                selectGrade.setSelection(0);
            }
            if(challengeGrade.equals("MiddleRankingList")){
                grade = 1;
                emptyx = 4;
                emptyy = 4;
                chooseLevel(4,4);
                selectGrade.setSelection(1);
            }
            if(challengeGrade.equals("HardRankingList")){
                grade = 2;

                emptyx = 5;
                emptyy = 5;
                chooseLevel(5,5);
                selectGrade.setSelection(2);
            }

        }







//        按屏幕宽度得到全新位图
        bitmap = zoomBitmap(bitmap,width - 70,width - 70);
        small.setImageBitmap(bitmap);

        selectGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(pattern==2){
                    grade=2;
                    emptyx = 10;
                    emptyy = 10;
                    chooseLevel(10,10);
                }else {
                    if(position ==0){
                        grade = 0;
                        emptyx = 3;
                        emptyy = 3;
                        chooseLevel(3,3);
                        //Toast.makeText(PuzzleActivity.this,"zhixingle",Toast.LENGTH_LONG).show();
                    }
                    if (position == 1){
                        grade = 1;
                        emptyx = 4;
                        emptyy = 4;
                        chooseLevel(4,4);
                    }
                    if (position == 2) {
                        grade = 2;
                        emptyx = 5;
                        emptyy = 5;
                        chooseLevel(5,5);

                    }
                }


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        //返回
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((pattern==0)||(pattern==1)){
                    Intent intentBack = new Intent(PuzzleActivity.this,MainActivity.class);
                    intentBack.putExtra("curUserNameFromPuzzle",curUserName);
                    startActivity(intentBack);
                }else {
                    Intent intentBack = new Intent(PuzzleActivity.this,FirstView.class);
                    intentBack.putExtra("curUserNameFromPuzzle",curUserName);
                    startActivity(intentBack);
                }

            }
        });
        //自动拼图
        help.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                length = arrayList.size()-1;
                handler.post(myRunnable);
            }
        });
        //开始拼图
        start.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                selectGrade.setEnabled(false);
                selectGrade.setAlpha(0.5f);
                start.setEnabled(false);
                start.setAlpha(0.5f);
                if(pattern == 0){
                    help.setEnabled(true);
                    help.setAlpha(1.0f);
                    originalMap.setEnabled(true);
                    originalMap.setAlpha(1.0f);
                }
                if (pattern == 1){
                    originalMap.setEnabled(true);
                    originalMap.setAlpha(1.0f);
                }
                sort(emptyx,emptyy);
                timerView.setBase(SystemClock.elapsedRealtime());
                timerView.start();
            }
        });
        //偷看一眼
        originalMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                small.setVisibility(View.VISIBLE);
                originalMap.setEnabled(false);
            }
        });
        //点击图片原图消失
        small.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                small.setVisibility(View.INVISIBLE);
                originalMap.setEnabled(true);
            }
        });

    }

    /*
    选择难度等级
     */
    public void chooseLevel(final int rows, final int colums){
        arrayList.clear();//清空可变数组
        tableLayout.removeAllViewsInLayout();//清空布局
        TableRow.LayoutParams lpBlock = new TableRow.LayoutParams(
                TableRow.LayoutParams.WRAP_CONTENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        lpBlock.setMargins(2,2,0,0);
        final int blockWidth = bitmap.getWidth()/colums;
        final int blockHeight = bitmap.getHeight()/rows;
        int sum = 0;
        for(int i=0;i<rows;i++){

            TableRow curRow = new TableRow(this);
            curRow.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            ));
            //表单行中增加列数
            for(int j=0;j<colums;j++){
                final ImageView curView = new ImageView(this);
                curView.setScaleType(ImageView.ScaleType.FIT_XY);
                curView.setLayoutParams(lpBlock);
                curView.setImageBitmap(Bitmap.createBitmap(bitmap,blockWidth*j,
                        blockHeight*i,blockWidth,blockHeight));
                imageSum[i][j] = curView;
                pictures[i][j] = ((BitmapDrawable) imageSum[i][j].getDrawable()).getBitmap();
                sum = sum + 1;

                if(i == rows-1 && j == colums-1){
                    emptyImageView = curView;


                }
                curRow.addView(curView);
                curView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if(pattern==0||pattern==2){
                            if(changePosition(emptyImageView,curView,blockHeight)==1) {
                                bgm_click();
                                emptyImageView.setVisibility(View.VISIBLE);
                                curView.setVisibility(View.INVISIBLE);
                                emptyImageView = curView;
                                steps = steps + 1;
                                stepView.setText(String.valueOf(steps));
                            }


                        }else {
                            if(changePosition(emptyImageView,curView,blockHeight)==1) {
                                bgm_click();
                                emptyImageView.setVisibility(View.VISIBLE);
                                curView.setVisibility(View.INVISIBLE);
                                emptyImageView = curView;
                                steps = steps - 1;
                                stepView.setText(String.valueOf(steps));

                            }

                        }
                        isSuccess(rows, colums);


                    }
                });

            }
            tableLayout.addView(curRow);//增加表单行

        }

    }
    /*
    判断是否成功
     */
    public void isSuccess(int rows,int colums){
        int x = 0;
        int sum = 0;
        for(int i = 0;i < rows;i++ ){
            for(int j=0;j < colums;j++){
                if(pictures[i][j]==((BitmapDrawable) imageSum[i][j].getDrawable()).getBitmap()){
                    sum = sum +1;
                }
                else {
                    break;
                }
            }
        }

        if (sum == rows * colums){
            bgm_success();
            //timerView.stop();
            dialog = new Dialog(this,R.style.dialog);
            LayoutInflater inflater = LayoutInflater.from(this);//把布局扩展进来
            View dialogView = inflater.inflate(R.layout.dialog_over,null);

            TextView message = (TextView)dialogView.findViewById(R.id.message);
            //times = Integer.parseInt(timerView.getText().toString());
            //times = Integer.parseInt(String.valueOf(timerView.getBase()));
            timerView.stop();
            if (pattern ==0 ||pattern == 2){
                message.setText("恭喜您！游戏结束！共走了"
                        + String.valueOf(steps)+"步，耗时"+timerView.getText()+"秒!");
            }
            else {
                x = challengeSteps - Integer.valueOf(stepView.getText().toString());
                message.setText("恭喜您！挑战成功！共走了"
                        +String.valueOf(x)+"步，耗时"+timerView.getText()+"秒!");
                steps = x;
            }


            Button btSure = (Button)dialogView.findViewById(R.id.btSure);
            btSure.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    Intent intent = new Intent(PuzzleActivity.this,RankingListActivity
                    .class);
                    startActivity(intent);

                }
            });
            dialog.setContentView(dialogView);
            dialog.show();
            if(pattern == 0 || pattern == 1){
            saveData();
            }
            emptyImageView.setVisibility(View.VISIBLE);

        }
        else {
            if (steps == 0 && pattern == 1){
                dialog = new Dialog(this,R.style.dialog);
                LayoutInflater inflater = LayoutInflater.from(this);//把布局扩展进来
                View dialogView = inflater.inflate(R.layout.dialog_over,null);
                TextView message = (TextView)dialogView.findViewById(R.id.message);
                timerView.stop();
                message.setText("       很遗憾！挑战失败!       ");
                Button btSure = (Button)dialogView.findViewById(R.id.btSure);
                btSure.setOnClickListener(new View.OnClickListener(){
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        Intent intent = new Intent(PuzzleActivity.this,RankingListActivity.class);
                        startActivity(intent);
                    }
                });

                dialog.setContentView(dialogView);
                dialog.show();
            }
        }
    }
    /*
    步数存入数据库
     */
    public void saveData(){
        String table = "";
            int stepsx = 0;//提取数据库中的步数，用于比较
            SQLiteDatabase dbSearch = helper.getWritableDatabase();
        switch (grade){
            case 0:
                table = "EasyRankingList";
                break;
            case 1:
                table = "MiddleRankingList";
                break;
            case 2:
                table = "HardRankingList";
                break;
        }
            Cursor cursor = dbSearch.query(table,new String[]{"steps"},
                    "userName = ?",new String[]{curUserName},null,null,null);

                 if(cursor.moveToFirst()){
                     stepsx = cursor.getInt(0);
                 }
                 //更新数据库
                 if(steps<stepsx || stepsx==0){
                     Toast.makeText(PuzzleActivity.this,"最佳纪录已更新！",Toast.LENGTH_LONG).show();
                     SQLiteDatabase dbUpdate = helper.getWritableDatabase();
                     ContentValues values = new ContentValues();
                     values.put("steps",steps);
                     values.put("times",timerView.getText().toString());
                     dbUpdate.update(table,values,"userName = ?",
                             new String[]{curUserName});
                 }
                  else {
                     Toast.makeText(PuzzleActivity.this,"不是最佳纪录，记录未被更换！",Toast.LENGTH_LONG).show();

                 }
    }

    /*
    打乱
     */
    public void sort(int rows,int colums){
        int s = 0;
        arrayList.clear();
        int rowsx = rows;
        int columsx = colums;
        switch (grade){
            case 0:
                s = 100;
                break;
            case 1:
                s = 200;
                break;
            case 2:
                s = 500;
                break;
        }
        for(int i = 0;i<s;i++){
            Random random = new Random();
            int k = random.nextInt(4)+1;
            Bitmap imagexy = ((BitmapDrawable) imageSum[rowsx-1][columsx-1].
                    getDrawable()).getBitmap();
            switch (k){
                case 1://向上
                    if(rowsx-1>0){
                        rowsx = top(imagexy,rowsx,columsx);
                        arrayList.add(1);
                        emptyx = emptyx - 1;
                    }
                    break;
                case 2://向下
                    if(rowsx+1<rows+1){
                        rowsx = bottom(imagexy,rowsx,columsx);
                        arrayList.add(2);
                        emptyx = emptyx + 1;

                    }
                    break;
                case 3://向左
                    if(columsx-1>0){
                        columsx = left(imagexy,rowsx,columsx);
                        arrayList.add(3);
                        emptyy = emptyy - 1;

                    }
                    break;
                case 4://向右
                    if (columsx+1<colums+1){
                        columsx =right(imagexy,rowsx,columsx);
                        arrayList.add(4);
                        emptyy = emptyy + 1;
                    }
                    break;
                default:
                    break;
            }
        }
    }
    /*
    向上移动
     */
    public int top(Bitmap imagexy,int rowsx,int columsx){
        Bitmap imagetopy = ((BitmapDrawable) imageSum[rowsx-2][columsx-1].
                getDrawable()).getBitmap();
        imageSum[rowsx-1][columsx-1].setImageBitmap(imagetopy);
        imageSum[rowsx-2][columsx-1].setImageBitmap(imagexy);
        imageSum[rowsx-1][columsx-1].setVisibility(View.VISIBLE);
        imageSum[rowsx-2][columsx-1].setVisibility(View.INVISIBLE);
        emptyImageView = imageSum[rowsx-2][columsx-1];
        return rowsx-1;
    }
    /*
    向下移动
     */
    public int bottom(Bitmap imagexy,int rowsx,int columsx){
        Bitmap imagebottomy = ((BitmapDrawable) imageSum[rowsx][columsx-1].
                getDrawable()).getBitmap();
        imageSum[rowsx-1][columsx-1].setImageBitmap(imagebottomy );
        imageSum[rowsx][columsx-1].setImageBitmap(imagexy);
        imageSum[rowsx-1][columsx-1].setVisibility(View.VISIBLE);
        imageSum[rowsx][columsx-1].setVisibility(View.INVISIBLE);
        emptyImageView = imageSum[rowsx][columsx-1];
        return rowsx+1;

    }
    /*
    向左移动
     */
    public int left(Bitmap imagexy,int rowsx,int columsx){
        Bitmap imagexleft = ((BitmapDrawable) imageSum[rowsx-1][columsx-2].
                getDrawable()).getBitmap();
        imageSum[rowsx-1][columsx-1].setImageBitmap(imagexleft );
        imageSum[rowsx-1][columsx-2].setImageBitmap(imagexy);
        imageSum[rowsx-1][columsx-1].setVisibility(View.VISIBLE);
        imageSum[rowsx-1][columsx-2].setVisibility(View.INVISIBLE);
        emptyImageView = imageSum[rowsx-1][columsx-2];
        return columsx-1;

    }
    /*
    向右移动
     */
    public int right(Bitmap imagexy,int rowsx,int columsx){
        Bitmap imagexright = ((BitmapDrawable) imageSum[rowsx-1][columsx].
                getDrawable()).getBitmap();
        imageSum[rowsx-1][columsx-1].setImageBitmap(imagexright );
        imageSum[rowsx-1][columsx].setImageBitmap(imagexy);
        imageSum[rowsx-1][columsx-1].setVisibility(View.VISIBLE);
        imageSum[rowsx-1][columsx].setVisibility(View.INVISIBLE);
        emptyImageView = imageSum[rowsx-1][columsx];
        return columsx+1;

    }
    /*
    移动
     */
    public int changePosition(ImageView target,ImageView dest,int length){
        //空格子
        int[] locationt = new int[2];
        target.getLocationOnScreen(locationt);
        int tx = locationt[0];
        int ty = locationt[1];
        //待移动格子
        int[] locationd = new int[2];
        dest.getLocationOnScreen(locationd);
        int dx = locationd[0];
        int dy = locationd[1];
        if((Math.abs(tx-dx)==(length+2)&&ty==dy)||(Math.abs(ty-dy)==(length+2)&&tx==dx)) {
            moveDirection(tx,ty,dx,dy);
            Bitmap image0 = ((BitmapDrawable) dest.getDrawable()).getBitmap();
            Bitmap image1 = ((BitmapDrawable) target.getDrawable()).getBitmap();
            dest.setImageBitmap(image1);
            target.setImageBitmap(image0);
            return 1;
        }
        else {
            return 0;
        }
    }
    /*
    判断用户移动方向
     */
    public void moveDirection(int tx,int ty,int dx,int dy){
        //空白块向左交换位置，数组记为向左移动
        if(tx>dx&&ty==dy){
            arrayList.add(3);
            emptyy = emptyy - 1;

        }
        //空白块向右交换位置，数组记为向右移动
        if(tx<dx&&ty==dy){
            arrayList.add(4);
            emptyy = emptyy +1;

        }
        //空白块向上交换位置，数组记为向上移动
        if(ty>dy&&tx==dx){
            arrayList.add(1);
            emptyx = emptyx - 1;
        }
        //空白块向下交换位置，数组记为向下移动
        if(ty<dy&&tx==dx){
            arrayList.add(2);
            emptyx = emptyx + 1;
        }

    }
    /*
    交换音效
     */
    private void initMusic(){
        //指定声音池的最大音频流数目为10，声音品质为5
        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 5);
        //载入音频流，返回在池中的id
        soundID = soundPool.load(this, R.raw.dianji, 1);
        soundID_success = soundPool.load(this,R.raw.success,1);
    }
    public void bgm_click(){
        if(FirstView.isSoundEffect == true){
            //Toast.makeText(this,""+FirstView.isYinxiao,Toast.LENGTH_LONG).show();
            soundPool.play(soundID,1,1,0,0,1);
        }
    }
    public void bgm_success(){
        if(FirstView.isSoundEffect == true) {
            soundPool.play(soundID_success,1,1,0,0,1);
        }
    }

    /*
    位图缩放
     */
    public Bitmap zoomBitmap(Bitmap bitmap,int w,int h){
        //得到图片原始的宽高，wh为设定图片的新的宽高
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        //计算缩放因子
        float scaleWidth = ((float)w/width);
        float scaleHeight = ((float)h/height);
        //新建立矩阵
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth,scaleHeight);
        //得到新的位图
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,0,0,
                width,height,matrix,true);
        return newBitmap;

    }
    /*
    位图截取
     */
    private Bitmap cutBitmap(Bitmap bitmap,int x,int y,int w,int h){
        //x,y为切割位置，w，h为切割后的宽高
        Bitmap newBitmap = Bitmap.createBitmap(bitmap,x,y,w,h);

        return newBitmap;
    }
    /*
    修改Spinner字体颜色
     */
    private void init(){
        selectGrade = (Spinner)findViewById(R.id.selectGrade);
        mStringArray = getResources().getStringArray(R.array.game_grades);
        mAdapter = new TestArrayAdapter(PuzzleActivity.this,mStringArray);
        selectGrade.setAdapter(mAdapter);
    }
}
