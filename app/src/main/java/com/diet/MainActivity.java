package com.diet;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.applinks.AppLinkData;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import bolts.AppLinks;

public class MainActivity extends Activity {
    private ListView petlist;
    //	private ArrayAdapter<String>petadp;
    //	private List<String>listpet =new ArrayList<String>();
    private ArrayList<ResultData> mAllData = new ArrayList<ResultData>();
    private TextView numtext;
    MyAdapter mydapter = null;
    private boolean isCencel = false;
    private ProgressDialog progressDialog;

    private com.facebook.ads.AdView adView, googleads;
    private InterstitialAd interstitial;
    private Spinner mSpinner, mSpinner2;
    HashMap<String, ArrayList<ResultData>> mKind;
    HashMap<String, ArrayList<String>> mCity;
    private MyAdapter mAdapter;
    private ArrayAdapter<String> mAdapter2 = null;
    private DatabaseReference mDatabase;
    private Button mInviteBtn;
    ImageView imageView;
    private static final String TAG = "MainActivity";
    private ArrayList<ResultData> list = new ArrayList<>();

    private AdView admobAd, admobAd2;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//		 //開啟全螢幕
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	
//                             WindowManager.LayoutParams.FLAG_FULLSCREEN);	
//        //設定隱藏APP標題	
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main_list);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//		MyGAManager.sendScreenName(this,"搜尋頁面");

        FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);

        mInviteBtn = (Button) findViewById(R.id.inviteBtn);
        mInviteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setClass(MainActivity.this, ForIdeaAndShareActivity.class);
                startActivity(i);
            }
        });

        Uri targetUrl =
                AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
        if (targetUrl != null) {
            Log.i("Activity", "App Link Target URL: " + targetUrl.toString());
        } else {
            AppLinkData.fetchDeferredAppLinkData(
                    getApplication(),
                    new AppLinkData.CompletionHandler() {
                        @Override
                        public void onDeferredAppLinkDataFetched(AppLinkData appLinkData) {
                            //process applink data
                        }
                    });
        }
        progressDialog = ProgressDialog.show(MainActivity.this, "讀取中", "目前資料量比較龐大，請耐心等候！！", false, false, new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialog) {
                //
                isCencel = true;
                finish();
            }
        });
        petlist = (ListView) findViewById(R.id.listView1);
        //		petadp= new ArrayAdapter<String>(this, android.R.layout.simple_expandable_list_item_1,android.R.id.text1);
        petlist.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Intent petint = new Intent(MainActivity.this, TwoActivity.class);
                petint.putExtra("json", new Gson().toJson(mAdapter.mDatas.get(position)));

                startActivity(petint);


            }
        });


        mAdapter = new MyAdapter(list);

        petlist.setAdapter(mAdapter);

        setFireBase();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }



    public void selectSpinner(String kinds) {
        ArrayList<String> kindList = mCity.get(kinds);
        mAdapter2.clear();

        mAdapter2.addAll(kindList);

    }

    public void selectSpinner2(String kinds) {

        ArrayList<ResultData> kindList = mKind.get(kinds);

//		mAdapter.updateData(kindList);

    }

    public class MyAdapter extends BaseAdapter {
        //		private ArrayList<ResultData> mDatas;
        private ArrayList<ResultData> mDatas;

        public MyAdapter(ArrayList<ResultData> datas) {
            mDatas = datas;
        }

        public void updateData(ArrayList<ResultData> datas) {
            mDatas = datas;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return mDatas.size();
        }

        @Override
        public Object getItem(int position) {
            return mDatas.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null)
                convertView = LayoutInflater.from(MainActivity.this).inflate(
                        R.layout.mylayout, null);
//			ResultData data = mDatas.get(position);
            ResultData taipeiZoo = mDatas.get(position);
            TextView textname = (TextView) convertView.findViewById(R.id.name);
            TextView list = (TextView) convertView.findViewById(R.id.txtengname);
            TextView bigtext = (TextView) convertView.findViewById(R.id.bigtext);
            bigtext.setVisibility(View.GONE);
            TextView place = (TextView) convertView.findViewById(R.id.palace);//
            textname.setText("主題："+taipeiZoo.tittle);
            list.setText("內容："+taipeiZoo.message);

            place.setText("發文者："+taipeiZoo.name);

            return convertView;
        }

    }


    private void loadImage(final String path,
                           final ImageView imageView) {

        new Thread() {

            @Override
            public void run() {

                try {
                    URL imageUrl = new URL(path);
                    HttpURLConnection httpCon =
                            (HttpURLConnection) imageUrl.openConnection();
                    InputStream imageStr = httpCon.getInputStream();
                    final Bitmap bitmap = BitmapFactory.decodeStream(imageStr);

                    runOnUiThread(new Runnable() {

                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            imageView.setImageBitmap(bitmap);
                        }
                    });


                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    Log.e("Howard", "MalformedURLException:" + e);
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.e("Howard", "IOException:" + e);
                }


            }


        }.start();

    }

    public class User {

        public String username;
        public String email;

        public User() {
            // Default constructor required for calls to DataSnapshot.getValue(User.class)
        }

        public User(String username, String email) {
            this.username = username;
            this.email = email;
        }

    }

    @Override
    protected void onDestroy() {
//		adView.destroy();

        super.onDestroy();
    }


    private void setFireBase() {
        Firebase.setAndroidContext(this);
        String url = "https://food-4997e.firebaseio.com/foodList";

        Firebase mFirebaseRef = new Firebase(url);


//		if(Firebase.getDefaultConfig().isPersistenceEnabled()==false)mFirebaseRef.getDefaultConfig().setPersistenceEnabled(true);
        mFirebaseRef.orderByChild("date").addChildEventListener(new com.firebase.client.ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d(TAG, "onChildAdded: " + dataSnapshot.getValue().toString());
//				Log.d(TAG, "onChildAdded: "+ (String) dataSnapshot.child("tittle").getValue());
//				Log.d(TAG, "onChildAdded: "+ (Long) dataSnapshot.child("message").getValue());
//				TaipeiZoo taipeiZoo = new TaipeiZoo();
//				taipeiZoo.setName((String)dataSnapshot.child("name").getValue());
//                for (DataSnapshot alert: dataSnapshot.getChildren()) {
//                    Log.d(TAG, "onChildAdded: "+alert.getValue().toString());
//                    list.add(0,gayPlace);
////                    for (DataSnapshot recipient: alert.child("formsg").getChildren()) {
////                        Log.d(TAG, "onChildAdded: "+recipient.getValue().toString());
////                    }
//
                ResultData resultData = dataSnapshot.getValue(ResultData.class);
                list.add(0, resultData);


                mAdapter.notifyDataSetChanged();
                progressDialog.dismiss();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                Log.d(TAG, "onChildChanged: " + "onChildChanged");

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }

        });


    }

    private void setFireBaseDB(String uri) {
        String url = "https://sevenpeoplebook.firebaseio.com/GayPlace";
        Firebase mFirebaseRef = new Firebase(url);
//		Firebase userRef = mFirebaseRef.child("user");
//		Map newUserData = new HashMap();
//		newUserData.put("age", 30);
//		newUserData.put("city", "Provo, UT");
        Firebase newPostRef = mFirebaseRef.child("posts").push();
        String newPostKey = newPostRef.getKey();
        Log.d(TAG, "setFireBaseDB: " + newPostKey);
        Map newPost = new HashMap();
        newPost.put("tittle", "hello");
        newPost.put("message", 21);
        newPost.put("pic", uri);
        Map updatedUserData = new HashMap();
//		updatedUserData.put("3/posts/" + newPostKey, true);
        updatedUserData.put(newPostKey, newPost);
        mFirebaseRef.updateChildren(updatedUserData, new Firebase.CompletionListener() {
            @Override
            public void onComplete(FirebaseError firebaseError, Firebase firebase) {
                if (firebaseError != null) {
                    Log.d(TAG, "onComplete: " + "Error updating data: " + firebaseError.getMessage());
                }
            }
        });
    }

    private void upLoad() {

//				sharePicWithUri(uri);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReferenceFromUrl("gs://sevenpeoplebook.appspot.com");
        StorageReference mountainsRef = storageRef.child("file.jpg");
        // Get the data from an ImageView as bytes
        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        Bitmap bitmap = imageView.getDrawingCache();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = mountainsRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }


    //縮放照片
    private void ScalePic(Bitmap bitmap, int phone) {
        //縮放比例預設為1
        float mScale = 1;

        //如果圖片寬度大於手機寬度則進行縮放，否則直接將圖片放入ImageView內
        if (bitmap.getWidth() > phone) {
            //判斷縮放比例
            mScale = (float) phone / (float) bitmap.getWidth();

            Matrix mMat = new Matrix();
            mMat.setScale(mScale, mScale);

            Bitmap mScaleBitmap = Bitmap.createBitmap(bitmap,
                    0,
                    0,
                    bitmap.getWidth(),
                    bitmap.getHeight(),
                    mMat,
                    false);
//			mImg.setImageBitmap(mScaleBitmap);
        }
//		else mImg.setImageBitmap(bitmap);
    }

    //儲存圖片
    public Uri savePicture(Bitmap bitmap) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/req_images");
        myDir.mkdirs();
        String fname = "temp.jpg";
        File file = new File(myDir, fname);
        if (file.exists()) file.delete();

        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Uri.fromFile(file);
    }
}