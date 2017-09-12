package kr.co.dorandoran.Network;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by LeeHaNeul on 2016-09-21.
 */
public class ParseDataParseHandler {

    public static ArrayList<FestivalEntityObject> getJSONFestivalList(StringBuilder buf) {

        ArrayList<FestivalEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;

        try {
            jsonAllList = new ArrayList<FestivalEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                FestivalEntityObject entity = new FestivalEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.postkey = jData.getInt("post_key");
                entity.img = jData.getString("img");
                entity.nick = jData.getString("nick");
                entity.school = jData.getInt("school");
                entity.title = jData.getString("title");

                entity.userkey = jData.getInt("user_key");
                entity.contents = jData.getString("contents");
                entity.comment = jData.getString("comment");
                entity.category = jData.getInt("category");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<VolunteerEntityObject> getJSONVolunteerList(StringBuilder buf) {

        ArrayList<VolunteerEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;

        try {
            jsonAllList = new ArrayList<VolunteerEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                VolunteerEntityObject entity = new VolunteerEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.postkey = jData.getInt("post_key");
                entity.img = jData.getString("img");
                entity.nick = jData.getString("nick");
                entity.school = jData.getInt("school");
                entity.title = jData.getString("title");

                entity.userkey = jData.getInt("user_key");
                entity.contents = jData.getString("contents");
                entity.comment = jData.getString("comment");
                entity.category = jData.getInt("category");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<ParttimeEntityObject> getJSONParttimeList(StringBuilder buf) {

        ArrayList<ParttimeEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;

        try {
            jsonAllList = new ArrayList<ParttimeEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                ParttimeEntityObject entity = new ParttimeEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.postkey = jData.getInt("post_key");
                entity.img = jData.getString("img");
                entity.nick = jData.getString("nick");
                entity.school = jData.getInt("school");
                entity.title = jData.getString("title");

                entity.userkey = jData.getInt("user_key");
                entity.contents = jData.getString("contents");
                entity.comment = jData.getString("comment");
                entity.category = jData.getInt("category");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<MypostEntityObject> getJSONMypostList(StringBuilder buf) {

        ArrayList<MypostEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;

        try {
            jsonAllList = new ArrayList<MypostEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("mypost");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                MypostEntityObject entity = new MypostEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.postkey = jData.getInt("post_key");
                entity.school = jData.getInt("school");
                entity.title = jData.getString("title");
                entity.userkey = jData.getInt("user_key");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<MymessageEntityObject> getJSONSendMessagetList(StringBuilder buf) {

        ArrayList<MymessageEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;

        try {
            jsonAllList = new ArrayList<MymessageEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                MymessageEntityObject entity = new MymessageEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.messageid = jData.getInt("message_id");
                entity.user_key = jData.getInt("user_key");
                entity.message = jData.getString("message");
                entity.senduserkey = jData.getInt("send_user_key");
                entity.receiveruserkey = jData.getInt("receive_user_key");
                entity.school = jData.getInt("school");
                entity.date = jData.getString("date");
                entity.receiver_nick = jData.getString("receiver_nic");
                entity.send_nick = jData.getString("send_nick");


                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }

    public static ArrayList<MymessageEntityObject> getJSONReceiveMessagetList(StringBuilder buf) {

        ArrayList<MymessageEntityObject> jsonAllList = null;
        JSONObject jsonObject = null;

        try {
            jsonAllList = new ArrayList<MymessageEntityObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("list");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                MymessageEntityObject entity = new MymessageEntityObject();

                JSONObject jData = jsonArray.getJSONObject(i);


                entity.messageid = jData.getInt("message_id");
                entity.user_key = jData.getInt("user_key");
                entity.message = jData.getString("message");
                entity.senduserkey = jData.getInt("send_user_key");
                entity.receiveruserkey = jData.getInt("receive_user_key");
                entity.school = jData.getInt("school");
                entity.receive_school = jData.getInt("school_receive");
                entity.date = jData.getString("date");
//                entity.messageflag = jData.getInt("message_flag");
                entity.receiver_nick = jData.getString("receiver_nic");
                entity.send_nick = jData.getString("send_nick");


                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;
    }


    public static PostEntityObject getJSONExDetailList(StringBuilder buf) {

        PostEntityObject entity = new PostEntityObject();
        JSONObject jsonObject = null;
        try {

            jsonObject = new JSONObject(buf.toString());

            JSONObject jData = jsonObject;

            entity.school = jData.getInt("school");
            entity.category = jData.getInt("category");
            entity.comment = jData.getString("comment");
            entity.contents = jData.getString("contents");
            entity.img = jData.getString("img");
            entity.nick = jData.getString("nick");
            entity.title = jData.getString("title");
            entity.userkey = jData.getInt("user_key");
            entity.postkey = jData.getInt("post_key");


              Log.i("comment", entity.comment);

        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return entity;
    }

    public static MenuObject getMenuObject(StringBuilder buf) {

        MenuObject entity = new MenuObject();
        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(buf.toString());
            JSONObject jData = jsonObject;

            entity.user_key = jData.getInt("user_key");
            entity.school = jData.getInt("school");
            entity.nick_name = jData.getString("nick");


        } catch (JSONException je) {
            Log.e("RequestAllList", "메뉴 JSON파싱 중 에러발생", je);
        }

        return entity;
    }

    public static ArrayList<UserAllObject> getJSONUserList(StringBuilder buf) {

        ArrayList<UserAllObject> jsonAllList = null;
        JSONObject jsonObject = null;

        try {
            jsonAllList = new ArrayList<UserAllObject>();
            jsonObject = new JSONObject(buf.toString());
            int total = jsonObject.getInt("total");
            if (total == 0) {
                return jsonAllList;
            }
            JSONArray jsonArray = jsonObject.getJSONArray("user");
            int jsonObjSize = jsonArray.length();
            for (int i = 0; i < jsonObjSize; i++) {
                UserAllObject entity = new UserAllObject();

                JSONObject jData = jsonArray.getJSONObject(i);

                entity.uuid = jData.getString("uuid");
                entity.school = jData.getInt("school");
                entity.nick = jData.getString("nick");
                entity.userkey = jData.getInt("user_key");

                jsonAllList.add(entity);
            }
        } catch (JSONException je) {
            Log.e("RequestAllList", "JSON파싱 중 에러발생", je);
        }
        return jsonAllList;

    }
}

