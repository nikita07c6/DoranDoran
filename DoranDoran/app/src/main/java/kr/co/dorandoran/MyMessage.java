package kr.co.dorandoran;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;

import java.util.ArrayList;

/**
 * Created by  HanAYeon  on 2016-07-26.
 */
//내 쪽지함 메인
public class MyMessage extends AppCompatActivity {

    ViewPager viewPager;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mymessage_layout);

        final Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        viewPager = (ViewPager) findViewById(R.id.viewpager_all);
        if (viewPager != null) {
            setupMessagePager(viewPager);
        }
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
    }


    private void setupMessagePager(ViewPager viewPager) {
        MessageAdapter messageAdapter = new MessageAdapter(getSupportFragmentManager());
        messageAdapter.appendFragment(MyMessageRecFragment.newInstance(1), "받은쪽지");
        messageAdapter.appendFragment(MyMessageSendFragment.newInstance(1), "보낸쪽지");

        viewPager.setAdapter(messageAdapter);
    }

    private static class MessageAdapter extends FragmentPagerAdapter {
        private final ArrayList<Fragment> regdit_fragment = new ArrayList<>();
        private final ArrayList<String> tabTitles = new ArrayList<String>();

        public MessageAdapter(FragmentManager fm) {
            super(fm);
        }

        public void appendFragment(Fragment fragment, String title) {
            regdit_fragment.add(fragment);
            tabTitles.add(title);
        }

        @Override
        public Fragment getItem(int position) {
            return regdit_fragment.get(position);
        }

        @Override
        public int getCount() {
            return regdit_fragment.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return tabTitles.get(position);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {

        if (viewPager.getCurrentItem() == 1) {
            viewPager.setCurrentItem(0, true);
        } else {
            super.onBackPressed();
        }

    }
}
