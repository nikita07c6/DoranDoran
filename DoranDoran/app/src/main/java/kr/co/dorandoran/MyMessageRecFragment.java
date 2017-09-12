package kr.co.dorandoran;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dorandoran.Network.MymessageEntityObject;
import kr.co.dorandoran.Network.NetworkDefineConstant;
import kr.co.dorandoran.Network.NetworkDialog;
import kr.co.dorandoran.Network.ParseDataParseHandler;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by HanAYeon on 2016-09-06.
 */

//내 쪽지함 -> 받은 쪽지
public class MyMessageRecFragment extends Fragment {
    public static int increment;
    public static int checkCount;
    public static boolean checkBoxVisible; //체크박스 보이기 유무
    static MyMessage owner;
    public RecyclerView rv;
    public SwipeRefreshLayout swipeRefreshLayout;
    int revev;
    RelativeLayout relativeLayout;
    String nicknameR;
    String sendUserKey;
    String receiveUserKey;
    String sendNick;
    String receiveNick;
    String sendSchool;
    String revceiveSchool;
    public int k;


    public MyMessageRecFragment() {
    }

    public static MyMessageRecFragment newInstance(int initValue) {
        MyMessageRecFragment message_bor_fragment = new MyMessageRecFragment();
        return message_bor_fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        View v = inflater.inflate(R.layout.messagefragment, container, false);
        rv = (RecyclerView)v.findViewById(R.id.recyclerview);

        LinearLayoutManager layoutManager = new LinearLayoutManager(MyApplication.getMyContext());
        rv.setLayoutManager(layoutManager);

        Bundle initBundle = getArguments();
        owner = (MyMessage) getActivity();

        new AsyncRecive().execute();
        return v;

    }



    //받은 쪽지를 불러오는 async
    public class AsyncRecive extends AsyncTask<Boolean, Integer, ArrayList<MymessageEntityObject>> {
        NetworkDialog networkDialog = new NetworkDialog(owner);

        @Override
        protected ArrayList<MymessageEntityObject> doInBackground(
                Boolean ... params) {
            int userId = PropertyManager.getInstance().getUser();
            String targetURL = String.format(NetworkDefineConstant.SERVER_URL_MYMESSAGE_RECEIVE)+"/" + userId;
            Log.i("target", targetURL);
            Response response = null;
            try{
                //OKHttp3사용
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(targetURL)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();
                ResponseBody responseBody = response.body();
                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getJSONReceiveMessagetList(
                            new StringBuilder(responseBody.string()));
                }
            }catch (UnknownHostException une) {
                e("abc", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("def", uee.toString());
            } catch (Exception e) {
                e("gih", e.toString());
            }finally{
                if(response != null){
                    response.close();
                }
            }
            return null;
        }
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            networkDialog.show();
        }


        @Override
        protected void onPostExecute(ArrayList<MymessageEntityObject> result) {
           networkDialog.dismiss();

            if(result != null && result.size() >= 0){
                SubActivity_Myinfo_Message_RecAdapter subActivity_myinfo_message_recAdapter =
                        new SubActivity_Myinfo_Message_RecAdapter(owner, result);
                rv.setAdapter(subActivity_myinfo_message_recAdapter);
                subActivity_myinfo_message_recAdapter.notifyDataSetChanged();
            } else {

            }
        }
    }

    //삭제를 위한 어댑터
    public class RegditDriverDelete extends AsyncTask<String, Void, String> {

        ProgressDialog dialog;
        boolean flag;
        int sort;


        @Override
        protected String doInBackground(String... writeId) {
            Response response = null;
            String targetURL = String.format(NetworkDefineConstant.SERVER_URL_MYMESSAGE_RECEIVE_DELETE);

            Log.i("msgId", "writeId[0] : " + writeId[0]);

            try {
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody requestBody = new FormBody.Builder()
                        .add("message_id", writeId[0])
                        .build();

                //동기 방식

                Request request = new Request.Builder()
                        .url(targetURL)
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
//            relativeLayout.setVisibility(View.VISIBLE);

            // dialog = ProgressDialog.show(MainActivity_Cerify.this, "", "잠시만 기다려 주세요 ...", true);
        }

        @Override
        protected void onPostExecute(String s) {
            //  relativeLayout.setVisibility(View.GONE);

         //   Toast.makeText(getActivity(), "삭제완료", Toast.LENGTH_SHORT).show();

        }
    }

    //async 에서 받아온 것들을 붙이는 어댑터
    public class SubActivity_Myinfo_Message_RecAdapter extends RecyclerView.Adapter<SubActivity_Myinfo_Message_RecAdapter.ViewHolder> {
        public ArrayList<MymessageEntityObject> messageEntityRecObjects;
        Context context;
        View popupView;

        String nickname;
        Intent intent = new Intent(owner, MessageForm.class);

        public SubActivity_Myinfo_Message_RecAdapter(Context context, ArrayList<MymessageEntityObject> resources) {
            this.messageEntityRecObjects = resources;
            popupView = LayoutInflater.from(context).inflate(R.layout.messagepopup_layout, null);
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mymessagerec_layout, parent, false);
            popupView = LayoutInflater.from(parent.getContext()).inflate(R.layout.messagepopup2_layout, parent, false);

            return new ViewHolder(view);
        }


        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {
            final MymessageEntityObject messageEntityRecObject = messageEntityRecObjects.get(position);
            k = messageEntityRecObject.messageid;

            holder.borDelete.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {

                        showPopup(view);
                }
            });


            holder.borReply.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sendUserKey = String.valueOf(messageEntityRecObject.senduserkey);   //상대방
                    receiveUserKey = String.valueOf(messageEntityRecObject.receiveruserkey);        //나
                    sendNick = String.valueOf(messageEntityRecObject.send_nick);        // 상대방
                    receiveNick = String.valueOf(messageEntityRecObject.receiver_nick);     //나
                    sendSchool = String.valueOf(messageEntityRecObject.school);     //상대방
                    revceiveSchool = String.valueOf(messageEntityRecObject.receive_school);     //나
                    showPopupReply(view);
                }
            });

            holder.borNickName.setText(messageEntityRecObject.send_nick);
            holder.Text.setText(messageEntityRecObject.message);
            holder.Date.setText(messageEntityRecObject.date);

            int schoolNum = messageEntityRecObject.school;

            Log.i("school", String.valueOf(schoolNum));

            if (schoolNum == 10) {
                Glide.with(owner).load(R.drawable.chungbuk).into(holder.NickImg);
            } else if (schoolNum == 11) {
                Glide.with(owner).load(R.drawable.cheongju).into(holder.NickImg);
            } else if (schoolNum == 12) {
                Glide.with(owner).load(R.drawable.seowon).into(holder.NickImg);
            } else if (schoolNum == 13) {
                Glide.with(owner).load(R.drawable.cheongju_education).into(holder.NickImg);
            } else if (schoolNum == 14) {
                Glide.with(owner).load(R.drawable.knue).into(holder.NickImg);
            } else if (schoolNum == 15) {
                Glide.with(owner).load(R.drawable.kkott).into(holder.NickImg);
            } else if (schoolNum == 16) {
                Glide.with(owner).load(R.drawable.transportation).into(holder.NickImg);
            } else if (schoolNum == 17) {
                Glide.with(owner).load(R.drawable.konkuk).into(holder.NickImg);
            } else if (schoolNum == 18) {
                Glide.with(owner).load(R.drawable.semyung).into(holder.NickImg);
            } else if (schoolNum == 19) {
                Glide.with(owner).load(R.drawable.u1).into(holder.NickImg);
            } else if (schoolNum == 20) {
                Glide.with(owner).load(R.drawable.jungwon).into(holder.NickImg);
            } else if (schoolNum == 21) {
                Glide.with(owner).load(R.drawable.far_east).into(holder.NickImg);
            } else if (schoolNum == 22) {
                Glide.with(owner).load(R.drawable.daewon).into(holder.NickImg);
            } else if (schoolNum == 23) {
                Glide.with(owner).load(R.drawable.gangdong).into(holder.NickImg);
            } else if (schoolNum == 24) {
                Glide.with(owner).load(R.drawable.chunkbuk_provincial).into(holder.NickImg);
            } else if (schoolNum == 25) {
                Glide.with(owner).load(R.drawable.chungbuk_health_science).into(holder.NickImg);
            } else if (schoolNum == 26) {
                Glide.with(owner).load(R.drawable.chungcheong).into(holder.NickImg);
            } else if (schoolNum == 27) {
                Glide.with(owner).load(R.drawable.woosuk).into(holder.NickImg);
            } else if (schoolNum == 28) {
                Glide.with(getContext()).load(R.drawable.openuniv).into(holder.NickImg);
            } else if (schoolNum == 00) {
                Glide.with(getContext()).load(R.drawable.whenull).into(holder.NickImg);
            } else {
                Glide.with(owner).load(R.drawable.write).into(holder.NickImg);
            }


        }

        @Override
        public int getItemCount() {
            return messageEntityRecObjects.size();
        }

        public void showPopupReply(View v) {
            final PopupWindow popupWindow = new PopupWindow(popupView, WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.MATCH_PARENT);
            TextView msgSendText = (TextView) popupView.findViewById(R.id.msg_send_text);
            Button btnDelete = (Button) popupView.findViewById(R.id.msgDelete_d);
            Button btnAnswer = (Button) popupView.findViewById(R.id.msgRespon_d);
            msgSendText.setText("쪽지를 보내시겠습니까?");
            btnDelete.setText("취소");
            btnAnswer.setText("보내기");
            btnDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    popupWindow.dismiss();
                }
            });

            btnAnswer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*CommonClass.reveiver = revev;
                    CommonClass.nick = nicknameR;
                    CommonClass.beforeAct = "Y";*/

                    intent.putExtra("receiveNick", sendNick);
                    intent.putExtra("sendNick", receiveNick);
                    intent.putExtra("receiveUserKey", sendUserKey);
                    intent.putExtra("sendUserKey", receiveUserKey);
                    intent.putExtra("sendSchool", revceiveSchool);
                    intent.putExtra("receiveSchool", sendSchool);

                    Log.i("nickname3", receiveNick);
                    owner.startActivity(intent);
                    popupWindow.dismiss();
                    //    getActivity().finish();
                    // intent.putExtra("name", nicknameR);
                }
            });

            // popupWindow.showAsDropDown(popupView, 50, -30);
            popupWindow.showAtLocation(v, Gravity.CENTER_VERTICAL, 0, 0);
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView borNickName;
            public final TextView Text;
            public final TextView Date;
            public final CircleImageView NickImg;
            public final ImageView borReply;
            public final ImageView borDelete;

            public ViewHolder(View view) {
                super(view);

                mView = view;
                NickImg = (CircleImageView) view.findViewById(R.id.bor_profile_img);
                borNickName = (TextView) view.findViewById(R.id.bor_nickname);
                Text = (TextView) view.findViewById(R.id.bor_stext);
                Date = (TextView) view.findViewById(R.id.bor_date);
                borReply = (ImageView) view.findViewById(R.id.bor_reply);
                borDelete = (ImageView) view.findViewById(R.id.msg_delbtn);
            }
        }
    }


    Button backBtn;

    public void showPopup(View view) {
        LayoutInflater inflater = (LayoutInflater)owner.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout poprl = (RelativeLayout)inflater.inflate(R.layout.deletepopup_layout,null);
        final PopupWindow popupWindow = new PopupWindow(poprl, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        backBtn = (Button)poprl.findViewById(R.id.msgDelete_d);
        Button close = (Button)poprl.findViewById(R.id.msgRespon_d);
        close.setText("삭제");
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

                // recTextGet = recText.getText().toString();
                popupWindow.dismiss();
                showSendPop(view);
                new RegditDriverDelete().execute(String.valueOf(k));

            }
        });

        // popupWindow.showAsDropDown(popupView, 50, -30);
    }


    public void showSendPop(View view) {
        LayoutInflater inflater = (LayoutInflater)owner.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout popll = (RelativeLayout)inflater.inflate(R.layout.messagepopup_layout,null);
        final PopupWindow popupWindow = new PopupWindow(popll, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button closeSend = (Button)popll.findViewById(R.id.btn_close_popup);
        TextView popText = (TextView)popll.findViewById(R.id.popup_text);
        popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
        popText.setText("삭제되었습니다.");
        closeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                new AsyncRecive().execute();
            }
        });


    }

}
