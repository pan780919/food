package com.diet;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jackpan.libs.mfirebaselib.MfiebaselibsClass;
import com.jackpan.libs.mfirebaselib.MfirebaeCallback;
import com.weather.Model.Main;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity implements View.OnClickListener, MfirebaeCallback {
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authListener;
    private String userUID;
    private FirebaseUser userpassword;

    private LoginButton loginButton;
    CallbackManager callbackManager;
    private ImageView fbImg;

    AccessTokenTracker accessTokenTracker;
    ProfileTracker profileTracker;
    private static final String TAG = "LoginActivity";
    private String userEmail = "";
    MfiebaselibsClass mfiebaselibsClass;
    String email = "";
    String password = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        mfiebaselibsClass = new MfiebaselibsClass(this, LoginActivity.this);
        mfiebaselibsClass.userLoginCheck();





        setContentView(R.layout.activity_login);
        findViewById(R.id.button).setOnClickListener(this);
        findViewById(R.id.button3).setOnClickListener(this);
        fbImg = (ImageView) findViewById(R.id.fdimg);
        fbLogin();


    }


    @Override
    protected void onStart() {
        super.onStart();
        mfiebaselibsClass.setAuthListener();

    }

    @Override
    protected void onStop() {
        super.onStop();
        mfiebaselibsClass.removeAuthListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button:
                email = ((EditText) findViewById(R.id.email))
                        .getText().toString();
                password = ((EditText) findViewById(R.id.password))
                        .getText().toString();
                Log.d("AUTH", email + "/" + password);
                if(email.isEmpty()){
                    Toast.makeText(LoginActivity.this,"請輸入帳號",Toast.LENGTH_SHORT).show();
                    return;
                }if(password.isEmpty()){
                    Toast.makeText(LoginActivity.this,"請輸入密碼",Toast.LENGTH_SHORT).show();
                    return;
                }
                mfiebaselibsClass.userLogin(email, password);
                break;
            case R.id.button2:

                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }
    //臉書登入
    private void fbLogin() {
        List<String> PERMISSIONS_PUBLISH = Arrays.asList("public_profile", "email", "user_friends");
        loginButton = (LoginButton) findViewById(R.id.fb_btn);
        loginButton.setReadPermissions(PERMISSIONS_PUBLISH);
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Log.d(TAG, "onSuccess: " + loginResult.getAccessToken());
                handleFacebookAccessToken(loginResult.getAccessToken());
                setUsetProfile();

                GraphRequest request = GraphRequest.newMeRequest(loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {

                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        Log.i("LoginActivity", response.toString());
                        // Get facebook data from login
                        Bundle bFacebookData = getFacebookData(object);
                    }
                });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id, first_name, last_name, email,gender, birthday, location"); // Parámetros que pedimos a facebook
                request.setParameters(parameters);
                request.executeAsync();
                Log.d(TAG, "onComplete: "+loginResult.getAccessToken().getUserId());
                MySharedPrefernces.saveUserId(LoginActivity.this, loginResult.getAccessToken().getUserId());
                Toast.makeText(LoginActivity.this, "登入成功,將跳到列表", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this, main.class));
                LoginActivity.this.finish();
            }

            @Override
            public void onCancel() {
                Log.d(TAG, "onCancel: ");
            }

            @Override
            public void onError(FacebookException error) {
                Log.d(TAG, "onError: ");
                new AlertDialog.Builder(getApplicationContext())
                        .setTitle("錯誤代碼"+error.hashCode())
                        .setMessage(error.getMessage())
                        .setNegativeButton("確定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        });

            }

        });



    }

    private void handleFacebookAccessToken(AccessToken token) {

        // [START_EXCLUDE silent]

        // [END_EXCLUDE]
        auth = FirebaseAuth.getInstance();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());

                        }

                        // [START_EXCLUDE]

                        // [END_EXCLUDE]
                    }
                });
    }

    private void setUsetProfile() {
        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                if (oldProfile != null) {
                    //登出後
//                    fbName.setText("");
                    fbImg.setImageBitmap(null);

                }

                if (currentProfile != null) {
                    //登入
//                    fbName.setText(currentProfile.getName());
                    loadImage(String.valueOf(currentProfile.getProfilePictureUri(150, 150)), fbImg, LoginActivity.this);
                    MySharedPrefernces.saveUserPic(LoginActivity.this,String.valueOf(currentProfile.getProfilePictureUri(150, 150)));

                }

            }
        };
        profileTracker.startTracking();
        if (profileTracker.isTracking()) {
            Log.d(getClass().getSimpleName(), "profile currentProfile Tracking: " + "yes");
            if (Profile.getCurrentProfile() == null) return;

//            if(Profile.getCurrentProfile().getName()!=null)	fbName.setText(Profile.getCurrentProfile().getName());
            if (Profile.getCurrentProfile().getProfilePictureUri(150, 150) != null)
                loadImage(String.valueOf(Profile.getCurrentProfile().getProfilePictureUri(150, 150)), fbImg, LoginActivity.this);
        } else
            Log.d(getClass().getSimpleName(), "profile currentProfile Tracking: " + "no");

    }

    @Override
    public void getDatabaseData(Object o) {

    }

    @Override
    public void getDeleteState(boolean b, String s ,Object o) {

    }

    @Override
    public void createUserState(boolean b) {
        if (b) {
            Toast.makeText(this, "註冊成功,將跳到列表", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, main.class);
            Bundle bundle= new Bundle();
            bundle.putBoolean("boolean",false);
            intent.putExtras(bundle);
            startActivity(intent);
            LoginActivity.this.finish();
            MySharedPrefernces.saveIsBuyed(LoginActivity.this,false);
        } else {

        }

    }

    @Override
    public void useLognState(boolean b) {
        if (b) {
            Toast.makeText(this, "登入成功,將跳到列表", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent();
            intent.setClass(LoginActivity.this, main.class);
            Bundle bundle= new Bundle();
            bundle.putBoolean("boolean",true);
            intent.putExtras(bundle);
            startActivity(intent);
            LoginActivity.this.finish();
            MySharedPrefernces.saveIsBuyed(LoginActivity.this,true);
        } else {
            new AlertDialog.Builder(LoginActivity.this)
                    .setTitle("登入問題")
                    .setMessage("無此帳號，是否要以此帳號與密碼註冊?")
                    .setPositiveButton("註冊",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    mfiebaselibsClass.createUser(email, password);
                                }
                            })
                    .setNeutralButton("取消", null)
                    .show();

        }

    }

    @Override
    public void getuseLoginId(String s) {
        if(!MySharedPrefernces.getUserId(LoginActivity.this).equals("")){
            startActivity(new Intent(LoginActivity.this,main.class));
            LoginActivity.this.finish();
            return;
        }

        if (!s.equals("")) {
            userUID = s;
            MySharedPrefernces.saveUserId(LoginActivity.this, userUID);
            startActivity(new Intent(LoginActivity.this,UserActivity.class));
            LoginActivity.this.finish();

        } else {
            userUID = "";
            MySharedPrefernces.saveUserId(LoginActivity.this, "");
//            LoginManager.getInstance().logOut();
            fbImg.setImageBitmap(null);

        }

    }

    @Override
    public void getuserLoginEmail(String s) {
        MySharedPrefernces.saveUserMail(this,s);

    }

    @Override
    public void resetPassWordState(boolean b) {

    }

    @Override
    public void getFireBaseDBState(boolean b, String s) {

    }

    @Override
    public void getFirebaseStorageState(boolean b) {

    }

    @Override
    public void getFirebaseStorageType(String s, String s1) {

    }

    @Override
    public void getsSndPasswordResetEmailState(boolean b) {
        if (b) {
            Toast.makeText(this, "已將重設密碼信寄至信箱！！", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(this, "發生錯誤！請檢查email是否註冊", Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void getUpdateUserName(boolean b) {

    }

    @Override
    public void getUserLogoutState(boolean b) {

    }
    public static  void loadImage(final String path,
                                  final ImageView imageView, final Activity activity){

        new Thread(){

            @Override
            public void run() {

                try {
                    URL imageUrl = new URL(path);
                    HttpURLConnection httpCon =
                            (HttpURLConnection) imageUrl.openConnection();
                    InputStream imageStr =  httpCon.getInputStream();
                    final Bitmap bitmap =  BitmapFactory.decodeStream(imageStr);

                    activity.runOnUiThread(new Runnable() {

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
                    Log.e("Howard", "IOException:"+e);
                }



            }


        }.start();

    }
    private Bundle getFacebookData(JSONObject object) {

        try {
            Bundle bundle = new Bundle();
            String id = object.getString("id");

            try {
                URL profile_pic = new URL("https://graph.facebook.com/" + id + "/picture?width=200&height=150");
                Log.i("profile_pic", profile_pic + "");
                bundle.putString("profile_pic", profile_pic.toString());

            } catch (MalformedURLException e) {
                e.printStackTrace();
                return null;
            }

            bundle.putString("idFacebook", id);
            if (object.has("first_name"))
                bundle.putString("first_name", object.getString("first_name"));
            if (object.has("last_name"))
                bundle.putString("last_name", object.getString("last_name"));
            if (object.has("email"))
                bundle.putString("email", object.getString("email"));
            if (object.has("gender"))
                bundle.putString("gender", object.getString("gender"));
            if (object.has("birthday"))
                bundle.putString("birthday", object.getString("birthday"));
            if (object.has("location"))
                bundle.putString("location", object.getJSONObject("location").getString("name"));
            MySharedPrefernces.saveUserMail(LoginActivity.this,object.getString("email"));
            return bundle;
        }
        catch(JSONException e) {
            Log.d(TAG,"Error parsing JSON");
        }
        return null;
    }
}
