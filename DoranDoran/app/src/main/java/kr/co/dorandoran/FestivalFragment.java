package kr.co.dorandoran;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import de.hdodenhof.circleimageview.CircleImageView;
import kr.co.dorandoran.Network.FestivalEntityObject;
import kr.co.dorandoran.Network.NetworkDefineConstant;
import kr.co.dorandoran.Network.NetworkDialog;
import kr.co.dorandoran.Network.ParseDataParseHandler;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

import static android.util.Log.e;

/**
 * Created by LeeHaNeul on 2016-09-07.
 *  Modified by hayoung on 2016-09-25.
 */
public class FestivalFragment extends android.support.v4.app.Fragment {
    public static int increment;
    static MainActivity owner;
    RecyclerView rv;
    int postkey;

    public FestivalFragment() {
    }

    public static FestivalFragment newInstance(int initValue) {
        FestivalFragment festivalFragment = new FestivalFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("value", initValue);
        festivalFragment.setArguments(bundle);
        return festivalFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstance) {
        rv = (RecyclerView) inflater.inflate(R.layout.festival_fragment, container, false);
        Bundle initBundle = getArguments();
        increment += initBundle.getInt("value");
        owner = (MainActivity) getActivity();

        rv.setLayoutManager(new LinearLayoutManager(getContext()));

        new AsyncFestivalJSONList().execute();

        return rv;
    }

    public class AsyncFestivalJSONList extends AsyncTask<String, Integer, ArrayList<FestivalEntityObject>> {
        NetworkDialog networkDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            networkDialog = new NetworkDialog(owner);
            networkDialog.show();
        }

        @Override
        protected ArrayList<FestivalEntityObject> doInBackground(String... params) {
            Response response = null;
            try {
                OkHttpClient client = new OkHttpClient.Builder()
                        .connectTimeout(20, TimeUnit.SECONDS)
                        .readTimeout(10, TimeUnit.SECONDS)
                        .build();

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_FESTIVAL)
                        .build();

                //동기 방식
                response = client.newCall(request).execute();
                ResponseBody responseBody = response.body();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();

                if (flag){
                    return ParseDataParseHandler.getJSONFestivalList(new StringBuilder(responseBody.string()));
                }
            } catch (UnknownHostException une) {
                e("축제에러1", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("축제에러2", uee.toString());
            } catch (Exception e) {
                e("축제에러3", e.toString());
            } finally {
                if(response != null){
                    response.close();
                }
            }
            return null;

        }


        @Override
        protected void onPostExecute(ArrayList<FestivalEntityObject> result) {
            networkDialog.dismiss();
            if (result != null && result.size() > 0) {
                FestivalJSONReqListAdapter festivalJSONReqListAdapter = new FestivalJSONReqListAdapter(owner, result);
                rv.setAdapter(festivalJSONReqListAdapter);
                festivalJSONReqListAdapter.notifyDataSetChanged();
            } else {

            }
        }
    }

    public class FestivalJSONReqListAdapter extends RecyclerView.Adapter<FestivalJSONReqListAdapter.ViewHolder> {
        Activity owner;
        public ArrayList<FestivalEntityObject> festivalEntityObjects;

        public FestivalJSONReqListAdapter(Context context, ArrayList<FestivalEntityObject> resources) {
            owner = (Activity) context;
            this.festivalEntityObjects = resources;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.main_item, parent, false);

            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            final FestivalEntityObject festivalEntityObject = festivalEntityObjects.get(position);

            Log.e("축제목록", festivalEntityObject.title);

            holder.title.setText(festivalEntityObject.title);
            holder.nickname.setText(festivalEntityObject.nick);
            if(!(festivalEntityObject.img).equals("null")){
                Glide.with(getContext()).load(festivalEntityObject.img).into(holder.contents);
            } else {
                // 어플 로고로 바꿀 것
                Glide.with(getContext()).load(R.drawable.whenull).into(holder.contents);
            }
            if (festivalEntityObject.school == 10) {
                Glide.with(getContext()).load(R.drawable.chungbuk).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 11) {
                Glide.with(getContext()).load(R.drawable.cheongju).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 12) {
                Glide.with(getContext()).load(R.drawable.seowon).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 13) {
                Glide.with(getContext()).load(R.drawable.cheongju_education).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 14) {
                Glide.with(getContext()).load(R.drawable.knue).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 15) {
                Glide.with(getContext()).load(R.drawable.kkott).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 16) {
                Glide.with(getContext()).load(R.drawable.transportation).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 17) {
                Glide.with(getContext()).load(R.drawable.konkuk).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 18) {
                Glide.with(getContext()).load(R.drawable.semyung).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 19) {
                Glide.with(getContext()).load(R.drawable.u1).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 20) {
                Glide.with(getContext()).load(R.drawable.jungwon).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 21) {
                Glide.with(getContext()).load(R.drawable.far_east).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 22) {
                Glide.with(getContext()).load(R.drawable.daewon).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 23) {
                Glide.with(getContext()).load(R.drawable.gangdong).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 24) {
                Glide.with(getContext()).load(R.drawable.chunkbuk_provincial).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 25) {
                Glide.with(getContext()).load(R.drawable.chungbuk_health_science).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 26) {
                Glide.with(getContext()).load(R.drawable.chungcheong).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 27) {
                Glide.with(getContext()).load(R.drawable.woosuk).into(holder.profile_icon);
            } else if (festivalEntityObject.school == 00) {
                Glide.with(getContext()).load(R.drawable.whenull).into(holder.profile_icon);
            } else if (festivalEntityObject.school  == 28) {
                Glide.with(getContext()).load(R.drawable.openuniv).into(holder.profile_icon);
            }


            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(owner,PostDetailActivity.class);

                    postkey = festivalEntityObject.postkey;
                    Log.e("postkeylist", postkey+"");
                    intent.putExtra("postkey", postkey);
                    ActivityCompat.startActivity(owner, intent, null);
                }
            });

        }

        @Override
        public int getItemCount() {
            return festivalEntityObjects.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final ImageView contents;
            public final TextView title;
            public final CircleImageView profile_icon;
            public final TextView nickname;


            public ViewHolder(View view) {
                super(view);

                mView = view;
                // contents는 null 처리 필요
                contents = (ImageView) view.findViewById(R.id.contents);
                title = (TextView) view.findViewById(R.id.title);
                profile_icon = (CircleImageView) view.findViewById(R.id.profile_image);
                nickname = (TextView) view.findViewById(R.id.nickname);


            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        new AsyncFestivalJSONList().execute();
    }
}
