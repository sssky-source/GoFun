package com.coolweather.gofun.fragment.Mine;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.util.Log;

import com.coolweather.gofun.LocalDb.SqliteUtil;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.bean.ActivityStatus;
import com.coolweather.gofun.fragment.Recommend.Adapter.FragmentAdapter;
import com.coolweather.gofun.util.HttpRequest;
import com.coolweather.gofun.util.PersonService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonActivity extends AppCompatActivity {

    private String token;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    final ArrayList<String> tabName = new ArrayList<>();
    final ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_activity);
        PersonService personService = HttpRequest.create(PersonService.class);
        SqliteUtil sqliteUtil = new SqliteUtil(PersonActivity.this);
        token = sqliteUtil.getToken();

        tabLayout = (TabLayout) findViewById(R.id.PersonActivity_TabLayout);
        viewPager2 = findViewById(R.id.PersonActivity_ViewPager2);

        request(personService);
    }

    private void request(PersonService personService) {
        personService.getActivityStatus("Bearer " + token).enqueue(new Callback<List<ActivityStatus>>() {
            @Override
            public void onResponse(Call<List<ActivityStatus>> call, Response<List<ActivityStatus>> response) {
                List<ActivityStatus> list = response.body();
                for (ActivityStatus activity : list) {
                    tabName.add(activity.getState());
                    Log.d("111",activity.getState());
                }
                Log.d("111", "size:" + tabName.size());
                Log.d("111",tabName.get(0));
                Log.d("111",tabName.get(1));
                Log.d("111",tabName.get(2));
                Log.d("111", "size11111:" + tabName.size());
                Log.d("111", "size11111:" + fragmentList.size());
                viewPager2.setAdapter(new ActivityAdapter(getSupportFragmentManager(),getLifecycle(),fragmentList));
                new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        Log.d("111", "222222222:" + tabName.size());
                        tab.setText(tabName.get(1));
                    }
                }).attach();
            }

            @Override
            public void onFailure(Call<List<ActivityStatus>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}