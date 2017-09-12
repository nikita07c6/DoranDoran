package kr.co.dorandoran;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dorandoran.Network.MenuObject;
import kr.co.dorandoran.Network.NetworkDefineConstant;
import kr.co.dorandoran.Network.NetworkDialog;
import kr.co.dorandoran.Network.ParseDataParseHandler;
import kr.co.dorandoran.Network.UserAllObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by LeeHaNeul on 2016-09-06.
 */

public class MainActivity extends AppCompatActivity {
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;
    boolean check=false;

    @Override
    protected void onResume() {
        super.onResume();
    }

    DrawerLayout drawer;
    NavigationView navigationView;
    CircleImageView header_img;
    static TextView header_nick;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.navigation);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_dehaze_black_24dp);
        ab.setDisplayHomeAsUpEnabled(true);

        new AsyncUserKeyList().execute();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        // navigationView.setNavigationItemSelectedListener(this);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawer.closeDrawers();
                        return true;
                    }
                });
        LinearLayout myPost = (LinearLayout) findViewById(R.id.my_post_layout);
        LinearLayout message = (LinearLayout) findViewById(R.id.my_message_layout);
        LinearLayout ask = (LinearLayout) findViewById(R.id.cs_layout);
        ImageButton post_write_btn = (ImageButton) findViewById(R.id.post_write_btn);
        header_img = (CircleImageView) findViewById(R.id.header_image);
        header_nick = (TextView) findViewById(R.id.header_user_name);

        myPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyPostActivity.class);
                startActivity(intent);
            }
        });

        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), MyMessage.class);
                startActivity(intent);
            }
        });

        ask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(Intent.ACTION_SEND);
                it.setType("plain/text");

                String[] tos = {"1113doran@gmail.com"};
                it.putExtra(Intent.EXTRA_EMAIL, tos);
                it.putExtra(Intent.EXTRA_SUBJECT, "도란도란 문의 메일");
                it.putExtra(Intent.EXTRA_TEXT, "문의 내용을 입력해주세요.");
                startActivity(it);
                check=true;
            }
        });

        post_write_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), PostWriteActivity.class);
                startActivity(intent);
            }
        });


        final CircleImageView festival_btn = (CircleImageView) findViewById(R.id.festival_btn);
        final CircleImageView volunteer_btn = (CircleImageView) findViewById(R.id.volunteer_btn);
        final CircleImageView part_time_btn = (CircleImageView) findViewById(R.id.part_time_btn);
        final TextView festival_text = (TextView) findViewById(R.id.festival_text);
        final TextView volunteer_text = (TextView) findViewById(R.id.volunteer_text);
        final TextView part_time_text = (TextView) findViewById(R.id.parttime_text);

        FestivalFragment festivalFragment = FestivalFragment.newInstance(0);
        FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
        ft0.replace(R.id.container, festivalFragment);
        ft0.commit();

        festival_btn.setImageResource(R.drawable.aaa);
        festival_text.setTextColor(getResources().getColor(R.color.colorPrimary));
        volunteer_btn.setImageResource(R.drawable.circle);
        volunteer_text.setTextColor(getResources().getColor(R.color.white));
        part_time_btn.setImageResource(R.drawable.circle);
        part_time_text.setTextColor(getResources().getColor(R.color.white));

        festival_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                festival_btn.setImageResource(R.drawable.aaa);
                festival_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                volunteer_btn.setImageResource(R.drawable.circle);
                volunteer_text.setTextColor(getResources().getColor(R.color.white));
                part_time_btn.setImageResource(R.drawable.circle);
                part_time_text.setTextColor(getResources().getColor(R.color.white));

                FestivalFragment festivalFragment = FestivalFragment.newInstance(0);
                FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
                ft0.replace(R.id.container, festivalFragment);
                ft0.commit();
            }
        });

        volunteer_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                festival_btn.setImageResource(R.drawable.circle);
                festival_text.setTextColor(getResources().getColor(R.color.white));
                volunteer_btn.setImageResource(R.drawable.aaa);
                volunteer_text.setTextColor(getResources().getColor(R.color.colorPrimary));
                part_time_btn.setImageResource(R.drawable.circle);
                part_time_text.setTextColor(getResources().getColor(R.color.white));

                VolunteerFragment volunteerFragment = VolunteerFragment.newInstance(0);
                FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
                ft0.replace(R.id.container, volunteerFragment);
                ft0.commit();
            }
        });

        part_time_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                festival_btn.setImageResource(R.drawable.circle);
                festival_text.setTextColor(getResources().getColor(R.color.white));
                volunteer_btn.setImageResource(R.drawable.circle);
                volunteer_text.setTextColor(getResources().getColor(R.color.white));
                part_time_btn.setImageResource(R.drawable.aaa);
                part_time_text.setTextColor(getResources().getColor(R.color.colorPrimary));

                PartTimeFragment partTimeFragment = PartTimeFragment.newInstance(0);
                FragmentTransaction ft0 = getSupportFragmentManager().beginTransaction();
                ft0.replace(R.id.container, partTimeFragment);
                ft0.commit();
            }
        });

    }// onCreate


    public class AsyncUserKeyList extends AsyncTask<String, Integer, ArrayList<UserAllObject>> {

        @Override
        protected ArrayList<UserAllObject> doInBackground(
                String... params) {
            Response response = null;
            try {
                //OKHttp3사용ㄴ
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getJSONUserList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
         /*   dialog = ProgressDialog.show(getActivity(),
                    "", "잠시만 기다려 주세요 ...", true);*/
        }

        @Override
        protected void onPostExecute(ArrayList<UserAllObject> result) {
            // dialog.dismiss();

            if (result != null && result.size() > 0) {
                String uuid = PropertyManager.getInstance().getUUID();
                for (int i = 0; i < result.size(); i++) {
                    if (uuid.equals(result.get(i).uuid)) {
                        PropertyManager.getInstance().setUser(result.get(i).userkey);

                    }
                }
                new HeaderAsyncTask().execute();
            }


        } // postExcute

    }


    public class HeaderAsyncTask extends AsyncTask<String, Integer, MenuObject> {
        // ProgressDialog dialog;
        NetworkDialog networkDialog;
        int userkey = PropertyManager.getInstance().getUser();

        @Override
        protected MenuObject doInBackground(String... params) {
            Response response = null;

            try {
                //OKHttp3사용
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER + "/" + userkey)
                        .build();

                //동기 방식
                response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getMenuObject(
                            new StringBuilder(responseBody.string()));
                }


            } catch (UnknownHostException une) {
                e("aaa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bbb", uee.toString());
            } catch (Exception e) {
                e("ccc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // dialog = ProgressDialog.show(MainActivity.this, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(MenuObject result) {
            //dialog.dismiss();

            if (result == null) {
                networkDialog = new NetworkDialog(MainActivity.this);
                networkDialog.show();
            } else {
                MainActivity.header_nick.setText(result.nick_name);

                if (result.school == 10) {
                    Glide.with(MainActivity.this).load(R.drawable.chungbuk).into(header_img);
                } else if (result.school == 11) {
                    Glide.with(MainActivity.this).load(R.drawable.cheongju).into(header_img);
                } else if (result.school == 12) {
                    Glide.with(MainActivity.this).load(R.drawable.seowon).into(header_img);
                } else if (result.school == 13) {
                    Glide.with(MainActivity.this).load(R.drawable.cheongju_education).into(header_img);
                } else if (result.school == 14) {
                    Glide.with(MainActivity.this).load(R.drawable.knue).into(header_img);
                } else if (result.school == 15) {
                    Glide.with(MainActivity.this).load(R.drawable.kkott).into(header_img);
                } else if (result.school == 16) {
                    Glide.with(MainActivity.this).load(R.drawable.transportation).into(header_img);
                } else if (result.school == 17) {
                    Glide.with(MainActivity.this).load(R.drawable.konkuk).into(header_img);
                } else if (result.school == 18) {
                    Glide.with(MainActivity.this).load(R.drawable.semyung).into(header_img);
                } else if (result.school == 19) {
                    Glide.with(MainActivity.this).load(R.drawable.u1).into(header_img);
                } else if (result.school == 20) {
                    Glide.with(MainActivity.this).load(R.drawable.jungwon).into(header_img);
                } else if (result.school == 21) {
                    Glide.with(MainActivity.this).load(R.drawable.far_east).into(header_img);
                } else if (result.school == 22) {
                    Glide.with(MainActivity.this).load(R.drawable.daewon).into(header_img);
                } else if (result.school == 23) {
                    Glide.with(MainActivity.this).load(R.drawable.gangdong).into(header_img);
                } else if (result.school == 24) {
                    Glide.with(MainActivity.this).load(R.drawable.chunkbuk_provincial).into(header_img);
                } else if (result.school == 25) {
                    Glide.with(MainActivity.this).load(R.drawable.chungbuk_health_science).into(header_img);
                } else if (result.school == 26) {
                    Glide.with(MainActivity.this).load(R.drawable.chungcheong).into(header_img);
                } else if (result.school == 27) {
                    Glide.with(MainActivity.this).load(R.drawable.woosuk).into(header_img);
                } else if (result.school == 28) {
                    Glide.with(MainActivity.this).load(R.drawable.openuniv).into(header_img);
                }

            }

        }
    }

    /*두번 누르면 종료*/
    @Override
    public void onBackPressed() {
        long currentTime = System.currentTimeMillis();
        long intervalTime = currentTime - backPressedTime;

        if(check) {
            //super.onBackPressed();
            drawer.openDrawer(Gravity.LEFT);
        }else if (drawer.isDrawerOpen(Gravity.LEFT)) {
            drawer.closeDrawer(Gravity.LEFT);
        }else if (0 <= intervalTime && FINSH_INTERVAL_TIME >= intervalTime) {
            super.onBackPressed();
        } else {
            backPressedTime = currentTime;
            Snackbar.make(drawer, "'뒤로' 버튼 한번 더 누르시면 종료됩니다.", Snackbar.LENGTH_LONG)
                    .show();

        }
    }
}
