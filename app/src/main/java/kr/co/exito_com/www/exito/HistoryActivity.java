package kr.co.exito_com.www.exito;

import android.support.design.widget.TabLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import kr.co.exito_com.www.exito.fragment.HeadFragment;
import kr.co.exito_com.www.exito.fragment.LastFragment;

public class HistoryActivity extends AppCompatActivity {

    private HeadFragment headFragment;
    private LastFragment lastFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        Toolbar toolbar = findViewById(R.id.history_toolbar);
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setTitleTextColor(0xAAFFFFFF);
        toolbar.setTitle("이용 내역");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        headFragment = new HeadFragment();
        lastFragment = new LastFragment();

        getSupportFragmentManager().beginTransaction().replace(R.id.container, headFragment).commit();

        final String head = "진행중인 내역";
        final String last = "지난 내역";

        TabLayout tabs = findViewById(R.id.history_tabs);
        tabs.addTab(tabs.newTab().setText(head));
        tabs.addTab(tabs.newTab().setText(last));

        tabs.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getText().equals(head)) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, headFragment).commit();
                }else {
                    getSupportFragmentManager().beginTransaction().replace(R.id.container, lastFragment).commit();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    // 메뉴 백버튼(왼쪽) 활성화
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home: {
                finish();
                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }
}
