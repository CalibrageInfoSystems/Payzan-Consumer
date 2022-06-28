package com.calibrage.payzanconsumer.activity;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.fragement.AllOffersFragment;
import com.calibrage.payzanconsumer.fragement.BillsFragment;
import com.calibrage.payzanconsumer.fragement.RechargesFragment;
import com.calibrage.payzanconsumer.framework.controls.BaseActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Calibrage10 on 1/3/2018.
 */

public class OffersTabActivity extends BaseActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private ViewPagerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab_actvity);

        ShowActionBar(getString(R.string.offer_sname));

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        viewPager.setOffscreenPageLimit(3);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
    }



    /**
     * * Layout manager that allows the user to flip left and right
     * through pages of data.  You supply an implementation of a
     * {PagerAdapter} to generate the pages that the view shows.
     *
     * @param viewPager
     */
    private void setupViewPager(ViewPager viewPager) {
        adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(new AllOffersFragment(), "");
        adapter.addFragment(new RechargesFragment(), "");
        adapter.addFragment(new BillsFragment(), "");
        viewPager.setAdapter(adapter);
    }

    /**
     * * Implementation of {PagerAdapter} that
     * represents each page as a {@link Fragment} that is persistently
     * kept in the fragment manager as long as the user can return to the page.
     */
    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();
        String tabTitles[] = new String[]{"AllOffers", "Recharges", "DTH/Bills"};

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

        /**
         * You must call {FragmentActivity#getSupportFragmentManager} to get the
         * {@link FragmentManager}
         *
         * @param fragment
         * @param title
         */
        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }

        /**
         * @param position
         * @return The current instance for call chaining
         */
        public View getTabView(int position) {
            View tab = LayoutInflater.from(OffersTabActivity.this).inflate(R.layout.custom_tab, null);
            TextView tv = (TextView) tab.findViewById(R.id.custom_text);
            tv.setText(tabTitles[position]);
            return tab;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
    }



}

