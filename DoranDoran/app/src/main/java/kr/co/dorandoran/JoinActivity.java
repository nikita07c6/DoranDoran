package kr.co.dorandoran;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.UUID;

import kr.co.dorandoran.Network.NetworkDefineConstant;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;

/**
 * Created by LeeHaNeul on 2016-09-07.
 * * Modified by HanAYeon on 2016-09-24.
 * Modified by hayoung on 2016-09-25.
 */
public class JoinActivity extends AppCompatActivity {

    String nicknameText;
    String schoolText;
    String uuid;
    int schoolNum;
    boolean nickCheckok;
    RelativeLayout joinActivityLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.join_activity);
        final EditText nickText = (EditText)findViewById(R.id.nickname_edit);
        final Spinner school = (Spinner)findViewById(R.id.school_spinner);
        Button join_check = (Button)findViewById(R.id.register_check);
        Button nickCheck = (Button)findViewById(R.id.nick_check);
        joinActivityLayout = (RelativeLayout)findViewById(R.id.join_activity_layout);
        Spinner school_choice;
        ArrayAdapter<CharSequence> adapterSchool;

        school_choice = (Spinner) findViewById(R.id.school_spinner);
        adapterSchool = ArrayAdapter.createFromResource(getApplicationContext(), R.array.school_list, R.layout.spinner_layout);
        adapterSchool.setDropDownViewResource(R.layout.sippner_dropdown_layout);
        school_choice.setAdapter(adapterSchool);

        nickCheckok = false;

        nickCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard();
                nicknameText = nickText.getText().toString();
                Log.i("nickname", nicknameText);
                new NickCheck().execute();
            }
        });

        join_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nicknameText = nickText.getText().toString();
                schoolText = school.getSelectedItem().toString();
                Log.i("nickname", nicknameText);
                Log.i("school", schoolText);

                if (schoolText.equals("충북대학교")) {
                    schoolNum =10;
                }else if (schoolText.equals("청주대학교")) {
                    schoolNum =11;
                }else if (schoolText.contains("서원")) {
                    schoolNum =12;
                }else if (schoolText.contains("청주교육")) {
                    schoolNum =13;
                }else if (schoolText.contains("한국교원")) {
                    schoolNum =14;
                }else if (schoolText.contains("꽃동네")) {
                    schoolNum =15;
                }else if (schoolText.contains("한국교통")) {
                    schoolNum =16;
                }else if (schoolText.contains("건국")) {
                    schoolNum =17;
                }else if (schoolText.contains("세명")) {
                    schoolNum =18;
                }else if (schoolText.contains("유원")) {
                    schoolNum =19;
                }else if (schoolText.contains("중원")) {
                    schoolNum =20;
                }else if (schoolText.contains("극동")) {
                    schoolNum =21;
                }else if (schoolText.contains("대원")) {
                    schoolNum =22;
                }else if (schoolText.contains("강동")) {
                    schoolNum =23;
                }else if (schoolText.contains("충북도립대학")) {
                    schoolNum =24;
                }else if (schoolText.contains("충북보건과학")) {
                    schoolNum =25;
                }else if (schoolText.contains("충청")) {
                    schoolNum =26;
                }else if (schoolText.contains("우석")) {
                    schoolNum =27;
                }else if (schoolText.contains("방송")) {
                    schoolNum =28;
                }
                else {
                    schoolNum=0;
                }

                if(nickCheckok) {
                    new LoginAsync().execute();
                } else {
                    Snackbar.make(joinActivityLayout, "닉네임 중복체크를 해주세요.", Snackbar.LENGTH_SHORT).show();
                }



            }
        });

    }


    //회원가입을 위한 async
    public class LoginAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;
        boolean flag;
        int sort;
       // String userId = "101";

        @Override
        protected String doInBackground(Void... params) {
            Response response = null;
            uuid = UUID.randomUUID().toString();
            Log.i("data :", "uuid :" + uuid + "nick: " + nicknameText + "schoolnum :" + schoolNum);
            try {
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("uuid", String.valueOf(uuid))
                        .add("nick", String.valueOf(nicknameText))
                        .add("school", String.valueOf(schoolNum))
                        .build();

                //동기 방식

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_USER)
                        .post(requestBody)
                        .build();

                response = okHttpClient.newCall(request).execute();

                flag = response.isSuccessful();

                //응답 코드 200등등
                int responseCode = response.code();

                if (!response.isSuccessful()) {
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신
                }
                return response.body().string();
            } catch (UnknownHostException une) {
                e("A", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("B", uee.toString());
            } catch (Exception e) {
                e("C", e.toString());
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
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (s == null) {
                Snackbar.make(joinActivityLayout, "네트워크 연결을 확인해주세요.", Snackbar.LENGTH_SHORT).show();
                //Toast.makeText(getApplicationContext(), "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            } else {
                if (s.contains("Success")) {
                    PropertyManager.getInstance().setSCHOOL(schoolNum);
                    PropertyManager.getInstance().setUUID(uuid);
                    PropertyManager.getInstance().setNick(nicknameText);

                    Log.i("data2 :", "uuid :" + PropertyManager.getInstance().getUUID() + "nick: " + PropertyManager.getInstance().getNick() + "schoolnum :" + PropertyManager.getInstance().getSchool());
                    Intent intent = new Intent(JoinActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();

                } else {
                    Snackbar.make(joinActivityLayout, "다시 입력해주세요.", Snackbar.LENGTH_SHORT).show();
                   // Toast.makeText(getApplicationContext(), "다시입력해주세요", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public class NickCheck extends AsyncTask<Void, Void, String> {

        String result;
        ProgressDialog dialog;
        boolean flag;
        int sort;
        String userId = "101";

        @Override
        protected String doInBackground(Void... params) {

            Response response = null;
            Log.i("nickname", nicknameText);

            try {
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                        .add("nick", String.valueOf(nicknameText))
                        .build();

                //동기 방식

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_NICKCHECK)
                        .post(requestBody)
                        .build();

                response = okHttpClient.newCall(request).execute();

                flag = response.isSuccessful();

                //응답 코드 200등등
                int responseCode = response.code();

                if (!response.isSuccessful()) {
                    e("response결과", response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신
                }
                return response.body().string();
            } catch (UnknownHostException une) {
                e("A", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("B", uee.toString());
            } catch (Exception e) {
                e("C", e.toString());
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
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.i("nick2", s);
                if (s == null) {
                    Snackbar.make(joinActivityLayout, "네트워크 연결을 확인해주세요.", Snackbar.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "네트워크 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } else if (s.contains("success")) {
                    Snackbar.make(joinActivityLayout, "사용가능한 닉네임입니다", Snackbar.LENGTH_SHORT).show();
                    nickCheckok = true;
                        //Toast.makeText(getApplicationContext(), "중복된 닉네임입니다.", Toast.LENGTH_SHORT).show();
                } else if(s.contains("overlap")){
                    Snackbar.make(joinActivityLayout, "중복된 닉네임입니다.", Snackbar.LENGTH_SHORT).show();
                    nickCheckok = false;
                        // Toast.makeText(getApplicationContext(), "사용가능한 닉네임입니다.", Toast.LENGTH_SHORT).show();

                        //nickok = true;
                    }
            }
        }

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
