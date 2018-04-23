package com.example.mypuzzle;

import android.app.Activity;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends Activity {
   int[] pictures = {R.drawable.meinvyuyeshou,R.drawable.huiguniang,
   R.drawable.nanguache,R.drawable.minimiqi,R.drawable.weinixiong,
   R.drawable.bingxueqiyuan,R.drawable.photogirl,R.drawable.meishaonvzhanshi,
   R.drawable.add};
    TextView curUserNametv;
    String curUserName;
    private MyDatabaseHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        GridView gridView = (GridView)findViewById(R.id.grid);
        MyAdapter adapter = new MyAdapter(this);
        Button button = (Button)findViewById(R.id.bt);
        gridView.setAdapter(adapter);
        LayoutInflater inflater = LayoutInflater.from(this);//把布局扩展进来

        //显示登录的用户名
        curUserNametv = (TextView)findViewById(R.id.curUserName);
        curUserName = FirstView.curUserName;
        curUserNametv.setText(curUserName);
        //点击用户名返回登录页面
        curUserNametv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this,FirstView.class);
                startActivity(intent);
            }
        });

        //排行榜按钮
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent intentRankingList = new Intent(MainActivity.this,
                        RankingListActivity.class);
                startActivity(intentRankingList);
            }
        });

        //GridView监听
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if(position != 8){
                String location = String.valueOf(pictures[position]);
                Intent intent = new Intent(MainActivity.this,PuzzleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("pattern",0);
                bundle.putString("data",location);
                bundle.putString("curUserName",curUserName);
                intent.putExtras(bundle);
                startActivity(intent);}
                else {
                    album();
                }
            }
        });
    }
    class MyAdapter extends BaseAdapter{
        private Context context;
        public MyAdapter(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return pictures.length;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int id) {
            return id;
        }

        @Override
        public View getView(int position, View convertview, ViewGroup viewGroup) {
            ImageView img = new ImageView(context);
            img.setAdjustViewBounds(true);//适应边框
            img.setImageResource(pictures[position]);
            img.setScaleType(ImageView.ScaleType.FIT_XY);//延XY轴适应
            img.setLayoutParams(new GridView.LayoutParams(335,335));
            img.setPadding(10,10,10,10);

            return img;
        }
    }
    public static final int TAKEPHOTO = 1;
    public static final int CROP_PHOTO =2;
    public static final int ALBUM =3;
    private Uri imageUri;
    private ImageView picture;
    Bitmap bitmap;
    public void takePhoto(View view){
        //Button takePhoto = (Button)findViewById(R.id.btCamera);
        //picture = (ImageView) findViewById(R.id.test);
        //创建File对象，存储拍照后的图片
        File outputImage = new File(Environment.getExternalStorageDirectory(),"output_image.jpg");//File存储图片，图片命名并放在SD卡根目录
        try {
            if (outputImage.exists()) {
                outputImage.delete();
            }
            outputImage.createNewFile();
        }catch (IOException e) {
            e.printStackTrace();
        }
        //imageUri = Uri
        imageUri = Uri.fromFile(outputImage);//将File对象转换成Uri，标志着图片的唯一地址
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);//指定输出图片的地址
        startActivityForResult(intent,TAKEPHOTO);//启动相机程序，照片传输到output_image.jpg；结果返回到onActivityResult
    }
    @Override
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        switch (requestCode){
            case TAKEPHOTO:
                if(resultCode ==RESULT_OK){
                    Intent intent = new Intent("com.android.camera.action.CROP");//对拍出照片进行剪裁
                    //给intent设置必要的属性
                    intent.setDataAndType(imageUri,"image/*");
                    intent.putExtra("scale",true);
                    intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
                    startActivityForResult(intent,CROP_PHOTO);//启动剪裁程序
                }
                break;
            case CROP_PHOTO:
                if(resultCode ==RESULT_OK){
                    try {
                        bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));//将照片解析成Bitmap对象
//                        picture.setImageBitmap(bitmap);//将剪裁后的图片显示出来
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case ALBUM:
                //picture = (ImageView)findViewById(R.id.test);
                if(Build.VERSION.SDK_INT>=19){
                    handleImageOnKitKat(data);
                    bitmap = BitmapFactory.decodeFile(imagePath);//将照片解析成Bitmap对象
//                    picture.setImageBitmap(bitmap);
                }else {
                    Toast.makeText(this,"手机系统版本号低于19",Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
        //跳转到拼图
        getImgId();
    }
    public void getImgId(){
        Intent intent = new Intent(MainActivity.this,PuzzleActivity.class);
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte [] bitmapByte =baos.toByteArray();//bitmap转byte
        intent.putExtra(BITMAP, bitmapByte);
        startActivity(intent);
        Toast.makeText(this,"1:"+BITMAP,Toast.LENGTH_LONG).show();
    }

    //以下三个方法为调用相册的实现
    /*需要修改版本号为19或以上,不然直接改成21好了：Project下，app-->src-->build.gradle*/
    public static final String BITMAP="";
    public void album(){
        Intent intent=new Intent("android.intent.action.GET_CONTENT");
        intent.setType("image/*");
        startActivityForResult(intent,ALBUM);//打开相册
    }
    //获取真实的图片路径
    private String getImagePath(Uri uri,String selection) {
        String path = null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }
    //相册功能中处理图片的方法
    String imagePath =null;
    private void handleImageOnKitKat(Intent data){
        Bitmap bitmap;
        Uri uri = data.getData();
        //如果是document类型的Uri，通过DocumentId处理
        if(DocumentsContract.isDocumentUri(this,uri)){
            String docId = DocumentsContract.getDocumentId(uri);
            if("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];//解析出数字格式的id
                String selection = MediaStore.Images.Media._ID+"="+id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(contentUri,null);
            }
            if(picture==null){
                //picture = (ImageView)findViewById(R.id.test);
            }
        }else {
            imagePath = getImagePath(uri,null);
        }
    }


}
