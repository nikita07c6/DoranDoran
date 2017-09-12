package kr.co.dorandoran;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import kr.co.dorandoran.Network.MypostEntityObject;
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
 * Created by hayoung 2016-09-07 내가 쓴 글 보여주는 fragment
 */
public class MyPostFragment extends android.support.v4.app.Fragment {
    public static int increment;
    static MyPostActivity owner;
    //public static int userkey=1;
    public static int postkey;
    public int userkey = PropertyManager.getInstance().getUser();
    public static RecyclerView rv;

    public MyPostFragment() {
    }

    public static MyPostFragment newInstance(int initValue) {
        MyPostFragment myPostFragment = new MyPostFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        myPostFragment.setArguments(bundle);
        return myPostFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rv = (RecyclerView) inflater.inflate(R.layout.my_post_fragment, container, false);
        Bundle initBundle = getArguments();
        increment += initBundle.getInt("value");
        owner = (MyPostActivity) getActivity();

        rv.setLayoutManager(new LinearLayoutManager(MyApplication.getMyContext()));

        new AsyncMyPostJSONList().execute();

        return rv;
    }


    public class DrawerMyPostRecyclerViewAdapter extends RecyclerView.Adapter<DrawerMyPostRecyclerViewAdapter.ViewHolder> {
        private ArrayList<MypostEntityObject> mypostEntityObject;


        public DrawerMyPostRecyclerViewAdapter(Context context, ArrayList<MypostEntityObject> resource) {
            mypostEntityObject = resource;
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView iconImage;
            public final TextView title;
            public final ImageButton delete;
            public final ImageButton modify;
            public final LinearLayout layout;

            public ViewHolder(View view) {
                super(view);

                mView = view;
                layout = (LinearLayout) view.findViewById(R.id.linear);
                iconImage = (ImageView) view.findViewById(R.id.user_img);
                title = (TextView) view.findViewById(R.id.title_my_post);
                delete = (ImageButton) view.findViewById(R.id.delete_my_post);
                modify = (ImageButton) view.findViewById(R.id.modify_my_post);

            }

        }

        @Override
        public ViewHolder onCreateViewHolder(
                ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_post_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            final ArrayList<MypostEntityObject> mypostEntityObjects = mypostEntityObject;

            holder.title.setText(mypostEntityObjects.get(position).title);

            if (mypostEntityObjects.get(position).school == 10) {
                Glide.with(owner).load(R.drawable.chungbuk).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 11) {
                Glide.with(owner).load(R.drawable.cheongju).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 12) {
                Glide.with(owner).load(R.drawable.seowon).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 13) {
                Glide.with(owner).load(R.drawable.cheongju_education).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 14) {
                Glide.with(owner).load(R.drawable.knue).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 15) {
                Glide.with(owner).load(R.drawable.kkott).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 16) {
                Glide.with(owner).load(R.drawable.transportation).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 17) {
                Glide.with(owner).load(R.drawable.konkuk).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 18) {
                Glide.with(owner).load(R.drawable.semyung).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 19) {
                Glide.with(owner).load(R.drawable.u1).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 20) {
                Glide.with(owner).load(R.drawable.jungwon).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 21) {
                Glide.with(owner).load(R.drawable.far_east).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 22) {
                Glide.with(owner).load(R.drawable.daewon).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 23) {
                Glide.with(owner).load(R.drawable.gangdong).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 24) {
                Glide.with(owner).load(R.drawable.chunkbuk_provincial).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 25) {
                Glide.with(owner).load(R.drawable.chungbuk_health_science).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 26) {
                Glide.with(owner).load(R.drawable.chungcheong).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 27) {
                Glide.with(owner).load(R.drawable.woosuk).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 00) {
                Glide.with(getContext()).load(R.drawable.whenull).into(holder.iconImage);
            } else if (mypostEntityObjects.get(position).school == 28) {
                Glide.with(getContext()).load(R.drawable.openuniv).into(holder.iconImage);
            }


            //delete button 눌렀을 때 이벤트리스너
            holder.delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //삭제 asynck
                    postkey = mypostEntityObjects.get(position).postkey;
                    showPopup(view);
                   // new AsyncPostDelete().execute();
                }
            });
            //modify button 눌렀을 때 이벤트리스너

            holder.modify.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(owner, PostModifyActivity.class);
                    postkey = mypostEntityObjects.get(position).postkey;
                    intent.putExtra("postkeymo", postkey);
                    Log.e("postkeymy", postkey+"");
                    startActivity(intent);

                }
            });

            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(owner, PostDetailActivity.class);
                    postkey = mypostEntityObjects.get(position).postkey;
                    intent.putExtra("postkey", postkey);

                    startActivity(intent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return mypostEntityObject.size();
        }
    }

    public class AsyncMyPostJSONList extends AsyncTask<String, Integer,
            ArrayList<MypostEntityObject>> {
        NetworkDialog networkDialog = new NetworkDialog(owner);

        @Override
        protected ArrayList<MypostEntityObject> doInBackground(
                String... params) {
            Response response = null;
            Log.e("userkey", String.valueOf(userkey));
            try {
                //OKHttp3사용ㄴ
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(15, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_MYPOST + "/" + userkey)
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();

                if (flag) {
                    return ParseDataParseHandler.getJSONMypostList(
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
            networkDialog.show();
        }

        @Override
        protected void onPostExecute(ArrayList<MypostEntityObject>
                                             result) {
             networkDialog.dismiss();
            if (result != null && result.size() > 0) {
                DrawerMyPostRecyclerViewAdapter myPostRecyclerAdapter =
                        new DrawerMyPostRecyclerViewAdapter(owner, result);
                rv.setAdapter(myPostRecyclerAdapter);
                myPostRecyclerAdapter.notifyDataSetChanged();
            } else {

            }
        }
    }


    public class AsyncPostDelete extends AsyncTask<Void, Void, String> {
        boolean flag;


        @Override
        protected String doInBackground(Void... params) {
            Response response = null;

            try {
                OkHttpClient okHttpClient = new OkHttpClient();

                RequestBody requestBody = new FormBody.Builder()
                       /* .add("value", String.valueOf(bookmark))*/
                        .add("user_key", String.valueOf(PropertyManager.getInstance().getUser()))
                        .add("post_key", String.valueOf(postkey))
                        .build();


                //요청 세팅
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_MYPOST_DELETE) //반드시 post로
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
                e("AsyncuuidUnkownHost", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("AsyncuuidEncoding", uee.toString());
            } catch (Exception e) {
                e("Asyncuuid", e.toString());
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
            if (s != null) {


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
                new AsyncPostDelete().execute();

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
                new AsyncMyPostJSONList().execute();
            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncMyPostJSONList().execute();
    }
}


