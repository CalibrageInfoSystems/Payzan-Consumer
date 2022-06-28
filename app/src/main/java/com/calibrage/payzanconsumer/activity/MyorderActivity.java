package com.calibrage.payzanconsumer.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;


import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.MyOrderFragment;

import java.util.ArrayList;
import java.util.List;

import static com.calibrage.payzanconsumer.framework.util.CommonUtil.buildCounterDrawable;


/**
 * Created by Calibrage11 on 9/20/2017.
 */

public class MyorderActivity extends AppCompatActivity implements SearchView.OnQueryTextListener  {
    private Menu menu;
    private TabLayout tabs;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myorder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        tabs = (TabLayout)findViewById(R.id.tabs);
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);
        viewPager = (ViewPager)findViewById(R.id.viewpager);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setTitle("My Orders");
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(4);
        viewPager.setCurrentItem(0, true);
        tabs.setupWithViewPager(viewPager);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);//Menu Resource, Menu
        this.menu = menu;
        MenuItem menuItem = menu.findItem(R.id.action_cart);
        menuItem.setIcon(buildCounterDrawable(MyorderActivity.this,2,  R.drawable.ic_cart));
        MenuItem item = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
        searchView.setQueryHint("search");
        searchView.setIconifiedByDefault(false);
//        int magId = getResources().getIdentifier("android:id/search_mag_icon", null, null);
//        ImageView magImage = (ImageView) searchView.findViewById(magId);
//        magImage.setLayoutParams(new LinearLayoutCompat.LayoutParams(0,0));
        searchView.setOnQueryTextListener((SearchView.OnQueryTextListener) MyorderActivity.this);

        MenuItemCompat.setOnActionExpandListener(item,
                new MenuItemCompat.OnActionExpandListener() {
                    @Override
                    public boolean onMenuItemActionCollapse(MenuItem item) {
// Do something when collapsed

                        return true; // Return true to collapse action view
                    }

                    @Override
                    public boolean onMenuItemActionExpand(MenuItem item) {
// Do something when expanded
                        return true; // Return true to expand action view
                    }
                });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_search) {

        }else if (item.getItemId() == android.R.id.home) {
            finish(); // close this activity and return to preview activity (if there is any)
        }else if(item.getItemId()==R.id.action_cart){
         //   item.setIcon(buildCounterDrawable(this,20,  R.drawable.ic_birthday));
            Toast.makeText(this, "clicked", Toast.LENGTH_SHORT).show();
        }
        // handle arrow click here

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        return false;
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }


        @Override
        public CharSequence getPageTitle(int position) {

            return mFragmentTitleList.get(position);
        }


    }

    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        String[] strings = new String[]{"All","recharge","Shopping","Billing"};
        for (int i = 0; i <strings.length ; i++) {
            final MyOrderFragment myBottomSheetSort = MyOrderFragment.
                    newInstance("",0);
            adapter.addFragment(myBottomSheetSort,strings[i]);
        }
        viewPager.setAdapter(adapter);
//        adapter.addFragment(myBottomSheetSort,"Billing");
//        adapter.addFragment(myBottomSheetSort,"All");
//        adapter.addFragment(myBottomSheetSort,"Shopping");



    }
}
