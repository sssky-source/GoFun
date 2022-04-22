package com.coolweather.gofun.fragment;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.coolweather.gofun.R;
import com.coolweather.gofun.util.BitmapUtils;
import com.coolweather.gofun.util.CameraUtils;
import com.coolweather.gofun.util.ToastUtils;
import com.coolweather.gofun.widget.BottomDialog;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import io.reactivex.functions.Consumer;

import static android.app.Activity.RESULT_OK;

/**
 * @author cyt on 20122/04/21.
 */


public class Minefragment extends Fragment implements View.OnClickListener{

    public ImageView imageViewhead;
    public View view;
    private BottomDialog mBottomPhotoDialog;
    private TextView cancel,takephoto,selectalbum;
    public String path;
    //权限申请
    private RxPermissions rxPermissions;
    private String base64Pic;
    //拍照和相册获取图片的Bitmap
    private Bitmap orc_bitmap;
    //是否拥有权限
    private boolean hasPermissions =false;

    //存储拍完照后的图片
    private File outputImagePath;
    //启动相机标识
    //启动相册标识
    public static final int SELECT_PHOTO = 2;
    public static final int TAKE_PHOTO = 1;

    //Glide请求图片选项配置
    private RequestOptions requestOptions = RequestOptions.circleCropTransform()
            .diskCacheStrategy(DiskCacheStrategy.NONE)//不做磁盘缓存
            .skipMemoryCache(true);//不做内存缓存

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        setListeners();
        return view;
    }

    public void initView(){
        imageViewhead = (ImageView)view.findViewById(R.id.head);

    }
    public void setListeners(){
        imageViewhead.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.head:
                showPhotoDialog();
             //   Toast.makeText(getContext(),"你点击了头像",Toast.LENGTH_SHORT).show();
                break;
            case R.id.dialog_image_clicked_btn_cancel:
                 mBottomPhotoDialog.cancel();
                 break;
            case R.id.dialog_selectalbum:
                openAlbum();
                Toast.makeText(getContext(),"相册中选择",Toast.LENGTH_SHORT).show();
                mBottomPhotoDialog.cancel();
                break;
            case R.id.dialog_takephoto:
                takePhoto();//拍照
                ToastUtils.show(getContext(),"拍照");
            //    Toast.makeText(getContext(),"拍照",Toast.LENGTH_SHORT).show();
                mBottomPhotoDialog.cancel();
                break;
            default:
                break;
        }
    }

    /*
    拍照
     */
    private void takePhoto() {
        if(!hasPermissions){
           ToastUtils.show(getContext(),"未获取权限");
           Log.d("Minefragment","111111111111");
            checkVersion();
        //    return;
        }
        Log.d("Minefragment","2222222");
        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String filename = timeStampFormat.format(new Date());
        outputImagePath = new File(getActivity().getExternalCacheDir(),filename + ".jpg");
        Intent takePhotoIntent = CameraUtils.getTakePhotoIntent(getContext(),outputImagePath);
        //开启一个带有返回值的Activity，请求码为TAKE_PHOTO
        startActivityForResult(takePhotoIntent,TAKE_PHOTO);
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


    private void checkVersion(){
        //android 6.0及以上版本
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            rxPermissions = new RxPermissions(getActivity());
            //  rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE).subscribe()
            rxPermissions.request(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            Log.d("Minefragment","333333333");
                            if (granted) {//申请成功
                                ToastUtils.show(Minefragment.this.getContext(), "已经获得权限");
                                hasPermissions = true;
                            } else {//申请失败
                                ToastUtils.show(Minefragment.this.getContext(), "权限未开启");
                                hasPermissions = false;
                            }
                        }
                    });
        }else{
            //android 6.0以下
            ToastUtils.show(getContext(),"无需申请权限");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
//                Uri uri = data.getData();
                if (resultCode == RESULT_OK) {
                    displayImage(outputImagePath.getAbsolutePath());
                }
                //  upload(CameraUtils.getImagePath(imageUri,null,getContext()));
                break;
            case SELECT_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    //判断手机系统版本号
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                        //4.4以上系统使用这个方法处理图片
                        imagePath = CameraUtils.getImageOnKitKatPath(data, getContext());
                    } else {
                        imagePath = CameraUtils.getImageBeforeKitKatPath(data, getContext());
                    }
                    //显示图片
                    displayImage(imagePath);
                 //   upload(imagePath);
                }
                break;
            default:
                break;
        }

    }

    /*
    上传图片到服务器
     */
    private void upload(String imagePath) {
    }

    private void displayImage(String imagePath) {
        if(!TextUtils.isEmpty(imagePath)){
            //显示图片
            Glide.with(this).load(imagePath).apply(requestOptions).into(imageViewhead);

            //压缩图片
            orc_bitmap = CameraUtils.compression(BitmapFactory.decodeFile(imagePath));
            //转Base64
            base64Pic = BitmapUtils.bitmapToBase64(orc_bitmap);
        }else
        {
            ToastUtils.show(getContext(),"获取图片失败");
        }

    }


    private void showPhotoDialog(){
        mBottomPhotoDialog = new BottomDialog(getContext(),0,true);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.bottom_photo, null, false);
        cancel = view.findViewById(R.id.dialog_image_clicked_btn_cancel);
        takephoto = view.findViewById(R.id.dialog_takephoto);
        selectalbum = view.findViewById(R.id.dialog_selectalbum);
        cancel.setOnClickListener(this);
        takephoto.setOnClickListener(this);
        selectalbum.setOnClickListener(this);
        mBottomPhotoDialog.setContentView(view);
        // 设置背景为透明色 那么白色的就能呈现出来了
        mBottomPhotoDialog.getDelegate().findViewById(R.id.design_bottom_sheet)
                .setBackgroundColor(getResources().getColor(R.color.transparent));

        mBottomPhotoDialog.show();

    }
}
