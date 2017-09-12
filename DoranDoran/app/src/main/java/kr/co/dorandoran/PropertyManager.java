package kr.co.dorandoran;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by hayoung on 2016-09-23
 * preference
 */
public class PropertyManager {
    private static PropertyManager instance;

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager();
        }
        return instance;
    }

    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        Context context = MyApplication.getMyContext();
        mPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        mEditor = mPrefs.edit();
    }

    public final String KEY_UUID = "UUID";

    public String getUUID() {
        return mPrefs.getString(KEY_UUID, "");
    }

    public void setUUID(String UUID) {
        mEditor.putString(KEY_UUID, UUID);
        mEditor.commit();
    }

    public final String KEY_NICK = "nick";

    public String getNick() {
        return mPrefs.getString(KEY_NICK, "");
    }

    public void setNick(String nick) {
        mEditor.putString(KEY_NICK, nick);
        mEditor.commit();
    }


    public final String KEY_SCHOOL = "school";

    public int getSchool() {
        return mPrefs.getInt(KEY_SCHOOL, 0);
    }

    public void setSCHOOL(int school) {
        mEditor.putInt(KEY_SCHOOL, school);
        mEditor.commit();
    }


    public final String KEY_USER = "user";

    public int getUser() {
        return mPrefs.getInt(KEY_USER, 0);
    }

    public void setUser(int User) {
        mEditor.putInt(KEY_USER, User);
        mEditor.commit();
    }




}
