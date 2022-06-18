package com.coolweather.gofun.fragment.Mine.apply;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.widget.TextView;

import com.coolweather.gofun.GoFunApplication;
import com.coolweather.gofun.R;
import com.coolweather.gofun.fragment.Mine.Adapter.ActivityAdapter;
import com.coolweather.gofun.fragment.Mine.bean.ActivityStatus;
import com.coolweather.gofun.net.HttpRequest;
import com.coolweather.gofun.net.PersonService;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*
    我申请的
    TabLayout分类
 */
public class PersonActivity extends AppCompatActivity {

    private TextView title;
    private TabLayout tabLayout;
    private ViewPager2 viewPager2;
    final ArrayList<String> tabName = new ArrayList<>();
    final ArrayList<Fragment> fragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_person_activity);
        PersonService personService = HttpRequest.create(PersonService.class);

        title = findViewById(R.id.PersonActivity_title);
        title.setText("我申请的");
        tabLayout = (TabLayout) findViewById(R.id.PersonActivity_TabLayout);
        viewPager2 = findViewById(R.id.PersonActivity_ViewPager2);

        request(personService);
    }

    private void request(PersonService personService) {
        personService.getApplyStatus("Bearer " + GoFunApplication.token).enqueue(new Callback<List<ActivityStatus>>() {
            @Override
            public void onResponse(Call<List<ActivityStatus>> call, Response<List<ActivityStatus>> response) {
                List<ActivityStatus> list = response.body();
                for (ActivityStatus activity : list) {
                    tabName.add(activity.getState());
                    fragmentList.add(new StateFragment(activity.getId(),personService));

                }
                viewPager2.setAdapter(new ActivityAdapter(getSupportFragmentManager(),getLifecycle(),fragmentList));
                new TabLayoutMediator(tabLayout, viewPager2, new TabLayoutMediator.TabConfigurationStrategy() {
                    @Override
                    public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                        tab.setText(tabName.get(position));
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