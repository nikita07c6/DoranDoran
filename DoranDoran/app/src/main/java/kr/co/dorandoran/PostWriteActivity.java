package kr.co.dorandoran;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import android.Manifest;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.inputmethod.InputMethodManager;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import kr.co.dorandoran.Network.NetworkDefineConstant;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.util.Log.e;
import static android.util.Log.v;

/**
 * Created by hayoung on 2016-09-06
 * 글쓰기 화면
 */

public class PostWriteActivity extends AppCompatActivity {

    private static final int PICK_FROM_ALBUM = 1;
    private final long FINSH_INTERVAL_TIME = 2000;
    private long backPressedTime = 0;

    public ImageView imageView;
    private ImageView attach;
    public String title;
    public String message;
    public String postSay;
    private Button update;
    private ImageView back;
    public Spinner category;
    private int userKey;

    private RelativeLayout postWriteLayout;
    private ArrayList<UpLoadValueObject> upLoadfiles = new ArrayList<>();
    int categoryint;
    public EditText postTitle;
    public EditText postMessage;
    public EditText postSaySome;
    int postkey;
    boolean check;
    int school;
     String nick;

    class UpLoadValueObject {
        File file; //업로드할 파일
        boolean tempFiles; //임시파일 유무

        public UpLoadValueObject(File file, boolean tempFiles) {
            this.file = file;
            this.tempFiles = tempFiles;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_write);
        userKey = PropertyManager.getInstance().getUser();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);

        postWriteLayout = (RelativeLayout) findViewById(R.id.post_write_layout);

/*
        Intent intent = getIntent();
        postkey = intent.getIntExtra("postkeymo", -1);*/


        final ActionBar ab = getSupportActionBar();
        // ab.setHomeAsUpIndicator(R.drawable.main_top_btn03);
        ab.setDisplayHomeAsUpEnabled(true);

        postTitle = (EditText) findViewById(R.id.post_wirte_title);
        postMessage = (EditText) findViewById(R.id.post_wirte_post);
        postSaySome = (EditText) findViewById(R.id.post_wirte_message);


        attach = (ImageView) findViewById(R.id.attach_image);
        imageView = (ImageView) findViewById(R.id.image_area);

        update = (Button) findViewById(R.id.update);

        /*카데고리 스피터*/
        category = (Spinner) findViewById(R.id.category);
        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.category, android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        category.setAdapter(adapter);


        /*이미지 첨부*/
        attach.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                doTakeAlbumAction();

            }
        });

        /*게시글 등록*/
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("modify", "cok1");
                title = postTitle.getText().toString();
                message = postMessage.getText().toString();
                postSay = postSaySome.getText().toString();

                if (category.getSelectedItem().equals("축제")) {
                    categoryint = 100;
                } else if (category.getSelectedItem().equals("봉사")) {
                    categoryint = 200;
                } else {
                    categoryint = 300;
                }
                Log.e("category", category.getSelectedItemPosition() + " : " + categoryint);
                if (((title != null) && (!title.equals(""))) && ((message != null) && (!message.equals("")))) {
                    if (upLoadfiles != null && upLoadfiles.size() > 0) {
                        update.setEnabled(false);
                        attach.setEnabled(false);

                        new FileUpLoadAsyncTask().execute(upLoadfiles);
                        showSendPop(view);

                    } else {
                        update.setEnabled(false);
                        attach.setEnabled(false);

                        new NoFileUpLoadAsyncTask().execute();
                        showSendPop(view);
                    }

                    Log.e("modify", "cok");

                } else {
                    if (title == null || title.equals("")) {
                        hideKeyboard();
                        Snackbar.make(postWriteLayout, "제목이 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                                .show();
                    } else if (message == null || message.equals("")) {
                        hideKeyboard();
                        Snackbar.make(postWriteLayout, "내용이 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                                .show();
                    } else if (category.getSelectedItem() == null || category.getSelectedItem().equals("")) {
                        hideKeyboard();
                        Snackbar.make(postWriteLayout, "카테고리가 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                                .show();
                    } else if ((title == null || title.equals("")) && (message == null || message.equals("")) &&
                            (category == null || category.equals("")) && (postSay == null || postSay.equals(""))) {
                        hideKeyboard();
                        Snackbar.make(postWriteLayout, "내용이 입력되지 않았습니다.", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }

            }
        });


    }

    @Override
    public void onResume() {
        super.onResume();

        if (!isSDCardAvailable()) {
            Snackbar.make(postWriteLayout, "SD 카드가 없어 종료 합니다.", Snackbar.LENGTH_LONG)
                    .show();

            finish();
            return;
        }
        String currentAppPackage = getPackageName();

        myImageDir = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), currentAppPackage);

        checkPermission();

        if (!myImageDir.exists()) {
            if (myImageDir.mkdirs()) {
               // Toast.makeText(getApplication(), " 저장할 디렉토리가 생성 됨", Toast.LENGTH_SHORT).show();
            }
        }
    }

    Uri currentSelectedUri; //업로드할 현재 이미지에 대한 Uri
    File myImageDir; //카메라로 찍은 사진을 저장할 디렉토리
    String currentFileName;  //파일이름

    private void doTakeAlbumAction() {
        // 앨범 호출
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
        startActivityForResult(intent, PICK_FROM_ALBUM);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        currentSelectedUri = data.getData();

        if (currentSelectedUri != null) {
            //실제 Image의 full path name을 얻어온다.
            if (findImageFileNameFromUri(currentSelectedUri)) {
                //ArrayList에 업로드할  객체를 추가한다.
                upLoadfiles.add(0, new UpLoadValueObject(new File(currentFileName), false));

                attach.setImageResource(R.drawable.community_typing_album_icon_02);

                final InputStream imageStream;
                try {
                    imageStream = getContentResolver().openInputStream(currentSelectedUri);
                    final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                    imageView.setVisibility(View.VISIBLE);
                    imageView.setImageBitmap(selectedImage);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

            }
        } else {
            Bundle extras = data.getExtras();
            Bitmap returedBitmap = (Bitmap) extras.get("data");
            if (tempSavedBitmapFile(returedBitmap)) {
                e("임시이미지파일저장", "저장됨");
            } else {
                e("임시이미지파일저장", "실패");
            }
        }


    }

    private boolean tempSavedBitmapFile(Bitmap tempBitmap) {
        boolean flag = false;
        try {
            currentFileName = "testImage_" + (System.currentTimeMillis() / 1000);
            String fileSuffix = ".jpg";
            //임시파일을 실행한다.
            File tempFile = File.createTempFile(
                    currentFileName,            // prefix
                    fileSuffix,                   // suffix
                    myImageDir                   // directory
            );
            final FileOutputStream bitmapStream = new FileOutputStream(tempFile);
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 0, bitmapStream);
            upLoadfiles.add(0, new UpLoadValueObject(tempFile, true));
            if (bitmapStream != null) {
                bitmapStream.close();
            }
            currentSelectedUri = Uri.fromFile(tempFile);
            flag = true;
        } catch (IOException i) {
            e("저장중 문제발생", i.toString(), i);
        }
        return flag;
    }


    private boolean findImageFileNameFromUri(Uri tempUri) {
        boolean flag = false;

        //실제 Image Uri의 절대이름
        String[] IMAGE_DB_COLUMN = {MediaStore.Images.ImageColumns.DATA};
        Cursor cursor = null;
        try {
            //Primary Key값을 추출
            String imagePK = String.valueOf(ContentUris.parseId(tempUri));
            //Image DB에 쿼리를 날린다.
            cursor = getContentResolver().query(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    IMAGE_DB_COLUMN,
                    MediaStore.Images.Media._ID + "=?",
                    new String[]{imagePK}, null);
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();
                currentFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA));
                e("fileName", String.valueOf(currentFileName));
                flag = true;
            }
        } catch (SQLiteException sqle) {
            e("findImage....", sqle.toString(), sqle);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return flag;
    }

    private class FileUpLoadAsyncTask extends AsyncTask<ArrayList<UpLoadValueObject>, Void, String> {
        // NetworkDialog networkDialog = new NetworkDialog(UpdatePostActivity.this);
        private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image");

        public FileUpLoadAsyncTask() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /// networkDialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<UpLoadValueObject>... arrayLists) {
            Response response = null;
            school = PropertyManager.getInstance().getSchool();
            nick = PropertyManager.getInstance().getNick();
            //school = PropertyManager.getInstance().get;
            //nick = PropertyManager.getInstance().get;
            try {
                //업로드는 타임 및 리드타임을 넉넉히 준다.
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("title", title);
                builder.addFormDataPart("user_key", String.valueOf(userKey));
                builder.addFormDataPart("contents", message);
                builder.addFormDataPart("comment", postSay);
                builder.addFormDataPart("school", String.valueOf(school));
                builder.addFormDataPart("nick", nick);
                builder.addFormDataPart("post_key", String.valueOf(postkey));
                builder.addFormDataPart("category", String.valueOf(categoryint));
                int fileSize = arrayLists[0].size();

                // for (int i = 0; i < fileSize; i++) {
                File file = arrayLists[0].get(0).file;

                builder.addFormDataPart("img", file.getName(), RequestBody.create(IMAGE_MIME_TYPE, file));

                Log.e("modify", RequestBody.create(IMAGE_MIME_TYPE, file).toString() + file.getName());
                //  }

                RequestBody fileUploadBody = builder.build();

                Log.e("modify", fileUploadBody.toString());
                //요청 세팅

                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_POST_WRITE)
                        .post(fileUploadBody) //반드시 post로
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    e("response결과file", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }

            } catch (UnknownHostException une) {
                e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bb", uee.toString());
            } catch (Exception e) {
                e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //networkDialog.dismiss();
            if (s.equalsIgnoreCase("Success")) {
                // Toast.makeText(PostModifyActivity.this, "파일업로드에 성공했습니다", Toast.LENGTH_LONG).show();
                int fileSize = upLoadfiles.size();

                for (int i = 0; i < fileSize; i++) {
                    UpLoadValueObject fileValue = upLoadfiles.get(i);
                    if (fileValue.tempFiles) {
                        fileValue.file.deleteOnExit(); //임시파일을 삭제한다
                    }
                }
            } else {
                Snackbar.make(postWriteLayout, "이미지 파일 업로드가 실패되었습니다.", Snackbar.LENGTH_LONG)
                        .show();
            }
            update.setEnabled(true);
        }
    }


    private class NoFileUpLoadAsyncTask extends AsyncTask<ArrayList<UpLoadValueObject>, Void, String> {
        //업로드할 Mime Type 설정
        //   private final MediaType IMAGE_MIME_TYPE = MediaType.parse("image");
        // NetworkDialog networkDialog = new NetworkDialog(UpdatePostActivity.this);

        public NoFileUpLoadAsyncTask() {
        }


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // networkDialog.show();
        }

        @Override
        protected String doInBackground(ArrayList<UpLoadValueObject>... arrayLists) {
            Response response = null;
            school = PropertyManager.getInstance().getSchool();
            nick = PropertyManager.getInstance().getNick();
            //school = PropertyManager.getInstance().get;
            //nick = PropertyManager.getInstance().get;
            try {
                //업로드는 타임 및 리드타임을 넉넉히 준다.
                OkHttpClient toServer = new OkHttpClient.Builder()
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .readTimeout(15, TimeUnit.SECONDS)
                        .build();

                MultipartBody.Builder builder = new MultipartBody.Builder();
                builder.setType(MultipartBody.FORM);
                builder.addFormDataPart("title", title);
                builder.addFormDataPart("user_key", String.valueOf(userKey));
                builder.addFormDataPart("contents", message);
                builder.addFormDataPart("comment", postSay);
                builder.addFormDataPart("school", String.valueOf(school));
                builder.addFormDataPart("nick", nick);
                builder.addFormDataPart("post_key", String.valueOf(postkey));
                builder.addFormDataPart("category", String.valueOf(categoryint));
                RequestBody fileUploadBody = builder.build();

                //요청 세팅
                Request request = new Request.Builder()
                        .url(NetworkDefineConstant.SERVER_URL_POST_WRITE)
                        .post(fileUploadBody) //반드시 post로
                        .build();
                //동기 방식
                response = toServer.newCall(request).execute();

                boolean flag = response.isSuccessful();
                //응답 코드 200등등
                int responseCode = response.code();
                if (flag) {
                    e("response결과", responseCode + "---" + response.message()); //읃답에 대한 메세지(OK)
                    e("response응답바디", response.body().string()); //json으로 변신
                    return "success";
                }

            } catch (UnknownHostException une) {
                e("aa", une.toString());
            } catch (UnsupportedEncodingException uee) {
                e("bb", uee.toString());
            } catch (Exception e) {
                e("cc", e.toString());
            } finally {
                if (response != null) {
                    response.close();
                }
            }
            return "fail";
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //networkDialog.dismiss();
        }
    }


    private final int MY_PERMISSION_REQUEST_STORAGE = 100;

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {

                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Explain to the user why we need to write the permission.
                    //Toast.makeText(this, "Read/Write external storage", Toast.LENGTH_SHORT).show();
                }

                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        MY_PERMISSION_REQUEST_STORAGE);

            } else {
                //사용자가 언제나 허락
            }
        }

    }

    public boolean isSDCardAvailable() {
        String state = Environment.getExternalStorageState();
        if (Environment.MEDIA_MOUNTED.equals(state) || Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
            return true;
        }
        return false;
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSION_REQUEST_STORAGE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {

                    //사용자가 퍼미션을 OK했을 경우

                } else {


                    //사용자가 퍼미션을 거절했을 경우
                }
                break;
        }
    }



    public void showSendPop(View view) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout popll = (RelativeLayout) inflater.inflate(R.layout.messagepopup_layout, null);
        final PopupWindow popupWindow = new PopupWindow(popll, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        Button closeSend = (Button) popll.findViewById(R.id.btn_close_popup);
        TextView popText = (TextView) popll.findViewById(R.id.popup_text);
        popupWindow.showAtLocation(view, Gravity.CENTER_VERTICAL, 0, 0);
        popText.setText("등록되었습니다.");
        closeSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                finish();
            }
        });


    }

    private void hideKeyboard() {

        InputMethodManager inputManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);

        inputManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }

    Button backBtn;

    public void showPopup(View view) {
        LayoutInflater inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        RelativeLayout poprl = (RelativeLayout)inflater.inflate(R.layout.canclepopup_layout,null);
        final PopupWindow popupWindow = new PopupWindow(poprl, WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);

        backBtn = (Button)poprl.findViewById(R.id.msgDelete_d);
        Button close = (Button)poprl.findViewById(R.id.msgRespon_d);
        close.setText("네");
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
                PostWriteActivity.super.onBackPressed();
            }
        });

        // popupWindow.showAsDropDown(popupView, 50, -30);
    }
    @Override
    public void onBackPressed() {
        CoordinatorLayout layout = (CoordinatorLayout)findViewById(R.id.main_content);

        showPopup(layout);


    }
}
