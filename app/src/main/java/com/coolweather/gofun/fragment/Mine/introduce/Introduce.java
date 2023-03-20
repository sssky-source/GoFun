package com.coolweather.gofun.fragment.Mine.introduce;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.LocalDb.PersonLitePal;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.MineFragment;
import com.coolweather.gofun.fragment.Mine.bean.Person;
import com.coolweather.gofun.fragment.Mine.bean.PersonChange;
import com.coolweather.gofun.fragment.Mine.bean.UserTag;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.util.BitmapUtils;
import com.coolweather.gofun.util.CameraUtils;
import com.coolweather.gofun.util.ToastUtils;
import com.coolweather.gofun.widget.BottomDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.litepal.LitePal;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import io.reactivex.functions.Consumer;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Introduce extends AppCompatActivity implements View.OnClickListener {

    //个人信息
    private Person person;
    private TextView name;
    private EditText sex, age, brief;
    private ShapeableImageView imageView;

    //请求
    private PersonService personService;

    //头像
    private BottomDialog mBottomPhotoDialog;
    private TextView cancel, takePhoto, selectAlbum;

    //权限申请
    private RxPermissions rxPermissions;

    private String base64Pic;

    //拍照和相册获取图片的Bitmap
    private Bitmap orc_bitmap;

    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    //是否拥有权限
    private boolean hasPermissions = false;

    //存储拍完照后的图片
    private File outputImagePath;

    //启动相机标识
    //启动相册标识
    public static final int TAKE_PHOTO = 1;
    public static final int SELECT_PHOTO = 2;

    private String img;

    //请求按钮
    private Button save;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_introduce);
        personService = HttpRequest.create(PersonService.class);

        //初始化
        initView();

        //获取个人信息
        requestInfo();
    }

    //初始化
    private void initView() {
        name = findViewById(R.id.intro_name);
        sex = findViewById(R.id.intro_sex);
        age = findViewById(R.id.intro_age);
        brief = findViewById(R.id.intro_brief);
        imageView = findViewById(R.id.intro_img);
        imageView.setOnClickListener(this);

        save = findViewById(R.id.intro_save);
        save.setOnClickListener(this);
    }

    //获取个人信息
    private void requestInfo() {
        personService.getUserInfo("Bearer " + GoFunApplication.getToken()).enqueue(new Callback<Person>() {
            @Override
            public void onResponse(Call<Person> call, Response<Person> response) {
                person = response.body();
                img=person.getImage();
                name.setText(person.getUsername());
                name.setTextSize(18);
                name.setTextColor(Color.rgb(0, 0, 0));
                sex.setText(person.getSex());
                //setText()方法不能传int类型参数 会被认为是资源的id int类型的数据需要转为string类型
                age.setText(String.valueOf(person.getAge()));
                brief.setText(person.getBrief());
                Glide.with(Introduce.this).load(person.getImage()).into(imageView);

            }

            @Override
            public void onFailure(Call<Person> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            //保存个人信息
            case R.id.intro_save:
                //上传头像
                if(outputImagePath!=null){
                    uploadHeadImg();
                }
                //修改个人信息
                editInfo();
                finish();
                break;

            //头像
            case R.id.intro_img:
                showPhotoDialog();
                break;

            //拍照
            case R.id.dialog_takephoto:
                takePhotos();
                ToastUtils.show(Introduce.this, "拍照");
                //Toast.makeText(getContext(),"拍照",Toast.LENGTH_SHORT).show();
                mBottomPhotoDialog.cancel();
                break;

            //从相册中选择
            case R.id.dialog_selectalbum:
                openAlbum();
                Toast.makeText(Introduce.this, "相册中选择", Toast.LENGTH_SHORT).show();
                mBottomPhotoDialog.cancel();
                break;

            //取消
            case R.id.dialog_image_clicked_btn_cancel:
                mBottomPhotoDialog.cancel();
                break;
        }
    }


    //修改个人信息
    private void editInfo() {
        PersonChange personChange = new PersonChange();
        personChange.setSex(sex.getText().toString());
        personChange.setAge(Integer.parseInt(age.getText().toString()));
        personChange.setHobby(person.getHobby());
        personChange.setBrief(brief.getText().toString());
        personChange.setX(person.getX());
        personChange.setY(person.getY());
        personChange.setLocation(person.getLocation());

        personService.editUserInfo("Bearer " + GoFunApplication.getToken(), personChange).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                Toast.makeText(Introduce.this, "修改完成", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //上传头像
    private void uploadHeadImg() {

        RequestBody requestBody = RequestBody.create(MediaType.parse("multipart/form-data"), outputImagePath);
        MultipartBody.Part body = MultipartBody.Part.createFormData("image", outputImagePath.getName(), requestBody);

        Log.d("imagePath", "outputImagePath:" + outputImagePath);
        personService.uploadHeadImg("Bearer " + GoFunApplication.getToken(), body).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                ToastUtils.show(Introduce.this, "图片上传成功");
                Log.d("imagePath", "code:" + response.code() + "body:" + response.body());

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                ToastUtils.show(Introduce.this, "图片上传失败");
                Log.d("imagePath", "cause:" + t.getCause());
                t.printStackTrace();
            }
        });
    }

    private void showPhotoDialog() {
        mBottomPhotoDialog = new BottomDialog(Introduce.this, 0, true);
        View view = LayoutInflater.from(Introduce.this).inflate(R.layout.bottom_photo, null, false);
        cancel = view.findViewById(R.id.dialog_image_clicked_btn_cancel);
        takePhoto = view.findViewById(R.id.dialog_takephoto);
        selectAlbum = view.findViewById(R.id.dialog_selectalbum);
        cancel.setOnClickListener(this);
        takePhoto.setOnClickListener(this);
        selectAlbum.setOnClickListener(this);
        mBottomPhotoDialog.setContentView(view);
        // 设置背景为透明色 那么白色的就能呈现出来了
        mBottomPhotoDialog.getDelegate().findViewById(R.id.design_bottom_sheet)
                .setBackgroundColor(getResources().getColor(R.color.transparent));
        mBottomPhotoDialog.show();
    }

    /**
     * 拍照
     */
    private void takePhotos() {
        if (!hasPermissions) {
            ToastUtils.show(Introduce.this, "未获取权限");
            Log.d("Minefragment", "111111111111");
            checkVersion();
            return;
        }
        Log.d("Minefragment", "2222222");
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(Introduce.this.getExternalCacheDir(), filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(Introduce.this, outputImagePath);
        //开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent, TAKE_PHOTO);

    }

    /**
     * 打开相册
     */
    private void openAlbum() {
        if (!hasPermissions) {
            //showMsg("未获取到权限");
            checkVersion();
            return;
        }
        startActivityForResult(CameraUtils.getSelectPhotoIntent(), SELECT_PHOTO);
    }

    private void checkVersion() {
        //android 6.0及以上版本
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            rxPermissions = new RxPermissions(Introduce.this);
            //  rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe()
            rxPermissions.request(Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            Log.d("Minefragment", "333333333");
                            if (granted) {//申请成功
                                ToastUtils.show(Introduce.this, "已经获得权限");
                                hasPermissions = true;
                            } else {//申请失败
                                ToastUtils.show(Introduce.this, "权限未开启");
                                hasPermissions = false;
                            }
                        }
                    });
        } else {
            //android 6.0以下
            ToastUtils.show(Introduce.this, "无需申请权限");
        }
    }


    /**
     * 返回到Activity
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            //拍照后返回
            case TAKE_PHOTO:
                if (resultCode == RESULT_OK) {
                    //显示图片
                    displayImage(outputImagePath.getAbsolutePath());
                }
                break;
            //打开相册后返回
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4及以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, this);
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, this);
                    }

                    Environment.getExternalStorageDirectory();
                    Log.d("imagePath", "img:" + imagePath);
                    outputImagePath = new File(imagePath);


                    //显示图片
                    displayImage(imagePath);

                    //outputImagePath = new File(imagePath);
                    //outputImagePath = new File(Introduce.this.getExternalCacheDir(),imagePath + ".jpg");

                }
                break;
            default:
                break;
        }
    }


    /**
     * 通过图片路径显示图片
     */
    private void displayImage(String imagePath) {
        if (!TextUtils.isEmpty(imagePath)) {
            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(imageView);

            //压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);

        } else {
            ToastUtils.show(Introduce.this, "图片获取失败");
        }
    }
}