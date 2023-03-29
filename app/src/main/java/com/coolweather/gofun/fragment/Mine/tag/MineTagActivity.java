package com.coolweather.gofun.fragment.Mine.tag;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.TagFragmentAdapter;
import com.coolweather.gofun.fragment.Mine.bean.TagChange;
import com.coolweather.gofun.fragment.Mine.bean.UserTag;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.coolweather.gofun.widget.NewMineInformationCard1;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import cn.youngkaaa.yviewpager.YViewPager;
import q.rorbin.verticaltablayout.VerticalTabLayout;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MineTagActivity extends AppCompatActivity implements View.OnClickListener {
    private int userID;
    private VerticalTabLayout tabLayout;
    private YViewPager viewPager;
    //用户标签列表
    private List<UserTag> userTags ;
    final ArrayList<String> tabName = new ArrayList<>();
    final ArrayList<Fragment> fragments = new ArrayList<>();
    private TextView title;
    private Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mine_tag);

        PersonService personService = HttpRequest.create(PersonService.class);

        Intent intent = getIntent();
        userID = intent.getIntExtra("userID", 0);

        tabLayout = findViewById(R.id.tag_TabLayout);
        viewPager = findViewById(R.id.tag_ViewPager2);
        title = findViewById(R.id.title);
        save = findViewById(R.id.tag_save);
        title.setText("修改个人标签");

        //获取全部标签
        getAllTagRequest(personService);

        save.setOnClickListener(this);

        //获取用户标签
        //getUserTagRequest(personService, userID);
    }

    //获取全部标签
    private void getAllTagRequest(PersonService personService) {
        personService.getAllTag("Bearer " + GoFunApplication.token).enqueue(new Callback<List<TagChange>>() {
            @Override
            public void onResponse(Call<List<TagChange>> call, Response<List<TagChange>> response) {
                List<TagChange> list = response.body();
                for (TagChange tagChange : list) {
                    tabName.add(tagChange.getName());
                    fragments.add(new TabFragment(tagChange.getTag(),userID));
                }
                TagFragmentAdapter tagFragmentAdapter = new TagFragmentAdapter(getSupportFragmentManager(), fragments, tabName);
                viewPager.setAdapter(tagFragmentAdapter);
                tabLayout.setupWithViewPager(viewPager);
            }

            @Override
            public void onFailure(Call<List<TagChange>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(this, "修改完成", Toast.LENGTH_SHORT).show();
        finish();
    }
}