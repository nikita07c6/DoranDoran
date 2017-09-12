package kr.co.dorandoran;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kr.co.dorandoran.Network.NetworkDefineConstant;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;



/*
 * Created by HanAYeon on 2016-09-06.
 */

//쪽지를 작성하는 폼.
//Commonclass 에서 쪽지를 보내려는 대상의 userid와 nickname 을 받아온다.
public class MessageForm extends AppCompatActivity {

    Button closeBtn;
    TextView recNick, sendBtn, recNickname, recText;
    String sendUser, smsDate, smsTime, smsTimeAll, recTextGet;
    String sendNick, receiveNick, sendUserKey, receiveUserKey, userKey, sendShcool, receiveSchool;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messageform_layout);

        recText = (TextView)findViewById(R.id.rec_text);


        Intent intent = getIntent();

        userKey = intent.getStringExtra("sendUserKey" +
                "");
        receiveNick = intent.getStringExtra("receiveNick");
        sendNick = intent.getStringExtra("sendNick");
        receiveUserKey = intent.getStringExtra("receiveUserKey");
        sendUserKey = intent.getStringExtra("sendUserKey");
        sendShcool = intent.getStringExtra("sendSchool");
        receiveSchool = intent.getStringExtra("receiveSchool");

        Log.i("nickname2", "recn : " + receiveNick);            //받는 사람  상대방
        Log.i("nickname2", "sendn : " + sendNick);              // 나
        Log.i("nickname2", "recuk : " + receiveUserKey);
        Log.i("nickname2", "sendu : " + sendUserKey);
        Log.i("nickname2", "sc : " + sendShcool);
        Log.i("nickname2", "rc : " + receiveSchool);


        recNick= (TextView)findViewById(R.id.rec_nick_text);
        recNick.setText(receiveNick);

        sendUser = CommonClass.reveiver;
        SimpleDateFormat sdg = new SimpleDateFormat("yyyy-MM-dd");
        smsDate = sdg.format(new Date());
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        smsTime = sdf.format(new Date());
        smsTimeAll = smsDate + " " + smsTime;


        closeBtn = (Button)findViewById(R.id.ans_close);
        sendBtn = (TextView) findViewById(R.id.ans_send);


        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                recTextGet = recText.getText().toString();
                if(recTextGet.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "내용이 없습니다!", Toast.LENGTH_SHORT).show();
                } else {
                showPopup(view);
            }
            }
        });



    }

    Button backBtn;

    public void showPopup(View view) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout poprl = (RelativeLayout)inflater.inflate(R.layout.messagepopup2_layout,null);
        final PopupWindow popupWindow = new PopupWindow(poprl, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        backBtn = (Button)poprl.findViewById(R.id.msgDelete_d);
        Button close = (Button)poprl.findViewById(R.id.msgRespon_d);
        close.setText("전송");
        popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                recTextGet = recText.getText().toString();
                popupWindow.dismiss();
                showSendPop(view);
                new MessageSendAsync().execute();

            }
        });

        // popupWindow.showAsDropDown(popupView, 50, -30);
    }


    public void showSendPop(View view) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout popll = (RelativeLayout)inflater.inflate(R.layout.messagepopup_layout,null);
        final PopupWindow popupWindow = new PopupWindow(popll, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button closeSend = (Button)popll.findViewById(R.id.btn_close_popup);
        TextView popText = (TextView)popll.findViewById(R.id.popup_text);
        popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
        popText.setText("쪽지를 보냈습니다.");
        closeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                String act = CommonClass.beforeAct;
                if (act.equals("Y")) {
                    Intent intent = new Intent(MessageForm.this, MyMessage.class);
                    startActivity(intent);
                    finish();
                } else {
                    finish();
                }
            }
        });


    }

    public class MessageSendAsync extends AsyncTask<Void, Void, String> {

        ProgressDialog dialog;
        boolean flag;
        int sort;
      //  String userId = PropertyManager.getInstance().getUser();


        @Override
        protected String doInBackground(Void... params) {
            Response response = null;
            Log.i("nickname2", "recn2 : " + receiveNick);            //받는 사람  상대방
            Log.i("nickname2", "sendn2 : " + sendNick);              // 나
            Log.i("nickname2", "recuk2 : " + receiveUserKey);
            Log.i("nickname2", "sendu2 : " + sendUserKey);
            Log.i("nickname2", "sc2 : " + sendShcool);
            Log.i("nickname2", "rc2 : " + receiveSchool);


            try {
                OkHttpClient okHttpClient = new OkHttpClient();


                RequestBody requestBody = new FormBody.Builder()
                        .add("user_key", String.valueOf(userKey))
                        .add("send_user_key", String.valueOf(sendUserKey))
                        .add("receive_user_key", String.valueOf(receiveUserKey))
                        .add("message", String.valueOf(recTextGet))
                        .add("date", String.valueOf(smsTimeAll))
                        .add("send_nick", String.valueOf(sendNick))
                        .add("receiver_nic", String.valueOf(receiveNick))
                        .add("school", String.valueOf(sendShcool))
                        .add("school_receive", String.valueOf(receiveSchool))
                        .build();

                //동기 방식

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_MESSAGE_SEND)

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
        }
    }

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

}

