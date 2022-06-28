package com.calibrage.payzanconsumer.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.UserProfileHome;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;

public class AboutUSActivity extends BaseActivity {
    Toolbar toolbar;
    WebView webView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_about_us);
        ShowActionBar(getString(R.string.about_us));
        setView();
    }

    private void setView() {

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_stat_arrow_back);
        toolbar.setTitle(getString(R.string.about_us));
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.white_new));

        webView = (WebView) findViewById(R.id.webview);
        webView.loadUrl("https://developer.android.com/index.html");
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }


}
