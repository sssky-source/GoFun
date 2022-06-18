package com.coolweather.gofun.fragment.Map.widget;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Map.adapter.TypeAdapterone;
import com.coolweather.gofun.fragment.Map.bean.TypeItem;
import com.coolweather.gofun.fragment.Recommend.bean.Activity;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.MapService;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSelectDialog extends BottomSheetDialog {

    private RecyclerView recyclerView;
    private Context context;
    private android.app.Activity activity;
    private List<TypeItem> typeItemList = new ArrayList<>();
    private BottomSheetDialog bottomSheetDialog;
    private BottomSheetBehavior mDialogBehavior;
    private TypeAdapterone mainAdapter;
    private List<TypeItem> titleList = new ArrayList<>();

    private BottomSelectDialog mBottomSheetDialog;
    public BottomSelectDialog(@NonNull Context context, android.app.Activity activity) {
        super(context);
        this.context = context;
        this.activity = activity;
        initViews();
    //    initData();
   //     showSheetDialog1();
      //  bottomSheetDialog.show();
    }

    private void initDatas() {
        MapService mapService = HttpRequest.create(MapService.class);
        mapService.getActivityType("Bearer" + GoFunApplication.token).enqueue(new Callback<List<Activity>>() {
            @Override
            public void onResponse(Call<List<Activity>> call, Response<List<Activity>> response) {
                List<Activity> list = response.body();
                Log.d("Bottom1",response.body().toString());
                Log.d("Botton4", String.valueOf(list.size()));
                for(int i = 0; i < list.size(); i++){
                    Log.d("Bottom2","111111111");
                    TypeItem typeItem = new TypeItem();
                    typeItem.setType(list.get(i).getType1());
                    typeItem.setImage(R.drawable.head);
                    Log.d("Bottom","type" + typeItem.getType());
                 //   Log.d("Bottom2",list.get(i))
                  //  typeItemList.get(i).setImage(R.drawable.head);
                    typeItemList.add(typeItem);
//                    activity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//            //                ToastUtils.show(context,"1111111111111");
//                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
//                            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//                            recyclerView.setLayoutManager(linearLayoutManager);
//                            TypeAdapter typeAdapter = new TypeAdapter(R.id.img_type,typeItemList);
//                            recyclerView.setAdapter(typeAdapter);
//                        }
//                    });
                }
            }

            @Override
            public void onFailure(Call<List<Activity>> call, Throwable t) {

            }
        });
    }

    private void initViews() {
        View view = getLayoutInflater().inflate(R.layout.dialog_bottom_select, null);
        setContentView(view);
    }

    private void showSheetDialog1() {
        View view = View.inflate(getContext(), R.layout.dialog_bottom_select, null);
        recyclerView = view.findViewById(R.id.type_recyclerview);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(layoutManager);
        mainAdapter = new TypeAdapterone(titleList,getContext());
        recyclerView.setAdapter(mainAdapter);
        bottomSheetDialog = new BottomSheetDialog(getContext());
        bottomSheetDialog.setContentView(view);
     //   mDialogBehavior = BottomSheetBehavior.from((View) view.getParent());
      //  mDialogBehavior.setPeekHeight(getPeekHeight());
    }


    /**
     * 弹窗高度，默认为屏幕高度的四分之三
     * 子类可重写该方法返回peekHeight
     *
     * @return height
     */
    protected int getPeekHeight() {
        int peekHeight = context.getResources().getDisplayMetrics().heightPixels;
        //设置弹窗高度为屏幕高度的3/4
        return peekHeight - peekHeight*7 / 10;
    }
}
