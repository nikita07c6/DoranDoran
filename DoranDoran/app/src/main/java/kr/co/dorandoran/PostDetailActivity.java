package kr.co.dorandoran;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.transition.Explode;
import android.transition.Transition;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dorandoran.Network.NetworkDefineConstant;
import kr.co.dorandoran.Network.NetworkDialog;
import kr.co.dorandoran.Network.ParseDataParseHandler;
import kr.co.dorandoran.Network.PostEntityObject;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by hayoung on 2016-09-06
 * 게시글 상세 화면
 */
public class PostDetailActivity extends AppCompatActivity {
    public static String SERVER_URL_DETAIL;


    private ImageView posterImage;
    private Button sendMessage;
    private TextView title;
    private TextView detail;
    private TextView user;
    private TextView saySomething;
    private CircleImageView schoolimg;
    private int postKey;
    private int category;
    private String userKey;
    public Toolbar toolbar;
     CardView image;
     CardView usercard;
    String sendUserKey;
    String receiveUserKey;
    String sendNick;
    String receiveNick;
    String sendSchool;
    String revceiveSchool;
    String imgUrl;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_detail);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        posterImage = (ImageView) findViewById(R.id.poster_image);
        sendMessage = (Button) findViewById(R.id.send_message);

        title = (TextView) findViewById(R.id.title);
        detail = (TextView) findViewById(R.id.detail);
        user = (TextView) findViewById(R.id.user);
        saySomething = (TextView) findViewById(R.id.say_something);
        user = (TextView) findViewById(R.id.user);
        image = (CardView) findViewById(R.id.image_card);
        schoolimg = (CircleImageView) findViewById(R.id.school);
        usercard = (CardView)findViewById(R.id.user_card);


        sendMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApplication.getMyContext(), MessageForm.class);

                intent.putExtra("receiveNick", receiveNick);
                intent.putExtra("sendNick", sendNick);
                intent.putExtra("receiveUserKey", receiveUserKey);
                intent.putExtra("sendUserKey", sendUserKey);
                intent.putExtra("sendSchool", sendSchool);
                intent.putExtra("receiveSchool", revceiveSchool);

                Log.i("nickname3", receiveNick);

                startActivity(intent);
            }
        });

        //목록에서 intent로 넘겨준 게시글 번호, 카테고리 get 해서 url 만들어 줌
         Intent intent = getIntent();

        //userKey = String.valueOf(PropertyManager.getInstance().getUser());
        postKey = intent.getIntExtra("postkey", -1);
        Log.e("postkey", postKey+"");
        // category = intent.getIntExtra("category", -1);

       // postKey = 21;
       // category = 200;




        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            Transition exitTrans = new Explode();

            Transition reenterTrans = new Explode();

            window.setExitTransition(exitTrans);
            window.setEnterTransition(reenterTrans);
            window.setReenterTransition(reenterTrans);

        }

        new AsyncPostDetail().execute("");

    }


    public class AsyncPostDetail extends AsyncTask<String, Integer,
            PostEntityObject> {
        NetworkDialog networkDialog = new NetworkDialog(PostDetailActivity.this);
        //  NetworkDialog dialog = new NetworkDialog(ScholInfoDetailActivity.this);

        @Override
        protected PostEntityObject doInBackground(
                String... params) {
            try {
                //OKHttp3사용ㄴ
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_POST_DETAIL+"/"+postKey)
                        .build();
                //동기 방식
                Response response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                //  responseBody.string(); // json으로 파싱
                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    return ParseDataParseHandler.getJSONExDetailList(
                            new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("fileUpLoad", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("fileUpLoad", uee.toString());
            } catch (Exception e) {
                e("fileUpLoad", e.toString());
            }
            return null;

        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            networkDialog.show();
        }

        @Override
        protected void onPostExecute(PostEntityObject
                                             result) {
            networkDialog.dismiss();

            if (result != null) {
                sendNick = PropertyManager.getInstance().getNick();//prefernce에서 불러와야됨
                receiveNick = result.nick;
                sendUserKey = String.valueOf(PropertyManager.getInstance().getUser());
                receiveUserKey = String.valueOf(result.userkey);
                sendSchool = String.valueOf(PropertyManager.getInstance().getSchool());
                revceiveSchool = String.valueOf(result.school);


                Log.e("detail", result.img);

                if(!result.img.equals("null")){
                Glide.with(MyApplication.getMyContext()).load(result.img).into(posterImage);}else{
                    image.setVisibility(View.GONE);
                }
                title.setText(result.title);
                detail.setText(result.contents);

                Log.e("comment", result.comment);
                if (!TextUtils.isEmpty(result.comment) || result.comment != null || !result.comment.equals("")) {
                    saySomething.setText(result.comment);
                }else{
                    saySomething.setText("내용이 없습니다.");
                }

                user.setText(result.nick);
                Log.e("school", String.valueOf(result.school));
                if (result.school == 10) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.chungbuk).into(schoolimg);
                } else if (result.school == 11) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.cheongju).into(schoolimg);
                } else if (result.school == 12) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.seowon).into(schoolimg);
                } else if (result.school == 13) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.cheongju_education).into(schoolimg);
                } else if (result.school == 14) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.knue).into(schoolimg);
                } else if (result.school == 15) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.kkott).into(schoolimg);
                } else if (result.school == 16) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.transportation).into(schoolimg);
                } else if (result.school == 17) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.konkuk).into(schoolimg);
                } else if (result.school == 18) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.semyung).into(schoolimg);
                } else if (result.school == 19) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.u1).into(schoolimg);
                } else if (result.school == 20) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.jungwon).into(schoolimg);
                } else if (result.school == 21) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.far_east).into(schoolimg);
                } else if (result.school == 22) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.daewon).into(schoolimg);
                } else if (result.school == 23) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.gangdong).into(schoolimg);
                } else if (result.school == 24) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.chunkbuk_provincial).into(schoolimg);
                } else if (result.school == 25) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.chungbuk_health_science).into(schoolimg);
                } else if (result.school == 26) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.chungcheong).into(schoolimg);
                } else if (result.school == 27) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.woosuk).into(schoolimg);
                }  else if (result.school == 28) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.openuniv).into(schoolimg);
                }  else if (result.school == 00) {
                    Glide.with(MyApplication.getMyContext()).load(R.drawable.whenull).into(schoolimg);
                }

            } else{

            }
        }


    }
}
