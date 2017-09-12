package kr.co.dorandoran;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import kr.co.dorandoran.Network.MenuObject;
import kr.co.dorandoran.Network.NetworkDefineConstant;
import kr.co.dorandoran.Network.ParseDataParseHandler;
import kr.co.dorandoran.Network.UserAllObject;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by hayoung on 2016-07-29.
 * Modified by LeeHaNeul on 2016-09-24.
 *  Modified by hayoung on 2016-09-25.
 */
public class SplashActivity extends AppCompatActivity {
    String uuid;
    Handler splashHandler;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_layout);

        checkNetwokState();
        uuid = PropertyManager.getInstance().getUUID();


        splashHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent;

                if (TextUtils.isEmpty(uuid)) {
                    intent = new Intent(SplashActivity.this, JoinActivity.class);
                } else {
                    uuid = PropertyManager.getInstance().getUUID().toString();
                    new AsyncUserKeyList().execute();


                    intent = new Intent(SplashActivity.this, MainActivity.class);
                }
                startActivity(intent);
                finish();
            }
        };

        // Log.i("getuuid", PropertyManager.getInstance().getUUID());

    }

    @Override
    public void onResume() {
        super.onResume();
        if(checkNetwokState()){
        splashHandler.sendEmptyMessageDelayed(0, 2000);}else{}
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }



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

            if (result != null && result.size()>0) {
                Log.i("userkeyclient", uuid);
                for(int i=0; i<result.size() ;i++) {
                    if (uuid.equals(result.get(i).uuid)) {
                        PropertyManager.getInstance().setUser(result.get(i).userkey);
                        PropertyManager.getInstance().setNick(result.get(i).nick);
                        PropertyManager.getInstance().setSCHOOL(result.get(i).school);
                        // Log.i("userkeyclient", PropertyManager.getInstance().getUser());
                    }
                }

            }

        } // postExcute

    }
    public boolean checkNetwokState() {
        ConnectivityManager manager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mobile = manager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        NetworkInfo wifi = manager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        NetworkInfo lte_4g = manager.getNetworkInfo(ConnectivityManager.TYPE_WIMAX);
        boolean blte_4g = false;
        if (lte_4g != null)
            blte_4g = lte_4g.isConnected();
        if (mobile.isConnected() || wifi.isConnected() || blte_4g)
            return true;
        else {
            AlertDialog.Builder dlg = new AlertDialog.Builder(SplashActivity.this);
            dlg.setTitle("네트워크 오류");
            dlg.setMessage("네트워크 연결이 끊겼습니다. 연결 상태를 확인하세요.");
            dlg.setIcon(R.mipmap.ic_launcher);
            dlg.setNegativeButton("종료", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    finish();  // 끝내버렷
                }
            });
            dlg.setCancelable(false);
            dlg.show();
            return false;
        }
    }
}
