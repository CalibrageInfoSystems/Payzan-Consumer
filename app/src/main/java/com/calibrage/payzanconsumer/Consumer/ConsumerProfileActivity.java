package com.calibrage.payzanconsumer.Consumer;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Toast;
import android.widget.Toolbar;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;

public class ConsumerProfileActivity extends BaseActivity {
android.support.v7.widget.Toolbar toolbar;
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_consumer_profile);
        initToolBar();

    }

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.bottom_navigation_main, menu);
        return true;
    }*/
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public void initToolBar() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Profile ");

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setNavigationOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Toast.makeText(ConsumerProfileActivity.this, "clicking the Back!", Toast.LENGTH_SHORT).show();
                    }
                }
        );
    }
}
