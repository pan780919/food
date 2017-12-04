package com.diet;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.ads.AdSize;
import com.facebook.share.Sharer;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.MutableData;
import com.firebase.client.Transaction;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.gson.Gson;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;


//import com.adlocus.AdLocusLayout$ErrorCode;
//import com.google.analytics.tracking.android.EasyTracker;

public class TwoActivity extends Activity {
	private TextView textview,textview2,textview3,textview4;
	private Bitmap bitmap=null;
	CallbackManager callbackManager;
	ShareDialog shareDialog;
	private  Button toUserMsg;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
//		 //開啟全螢幕
//		        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,	
//		                             WindowManager.LayoutParams.FLAG_FULLSCREEN);	
//		        //設定隱藏APP標題	
//		        requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_two);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		initLayout();
		loadIntent();
//		Like();
		FacebookSdk.sdkInitialize(getApplicationContext());
		callbackManager = CallbackManager.Factory.create();
		shareDialog = new ShareDialog(this);
		// this part is optional
		shareDialog.registerCallback(callbackManager, new FacebookCallback<Sharer.Result>() {
			@Override
			public void onSuccess(Sharer.Result result) {

			}

			@Override
			public void onCancel() {

			}

			@Override
			public void onError(FacebookException error) {

			}
		});


	}
	private  void initLayout(){
		textview = (TextView) findViewById(R.id.textView1);
		textview2 = (TextView) findViewById(R.id.textView2);
		textview3 = (TextView) findViewById(R.id.textView3);
		textview4 = (TextView) findViewById(R.id.textView4);
		toUserMsg = (Button) findViewById(R.id.b_button_message);

	}

	private static final String TAG = "TwoActivity";
	private void loadIntent() {
		String json = getIntent().getStringExtra("json");
		final ResultData data = new Gson().fromJson(json, ResultData.class);
		textview.setText("主題："+data.tittle);
		textview2.setText("內容："+data.message);
		textview3.setText("作者："+data.name);
		textview4.setText("發布時間："+data.date);

//		likeid = data.getId()+data.getDate();
//		loadImage(data.getPic(), img);
//		textview.setText("標題:"+data.getTittle());
//		textview2.setText("內容:"+data.getMessage());
//		textview3.setText("作者:"+data.getName());
//		if(!data.getUrl().equals(""))textview4.setText("點擊觀看影片");
//		else textview4.setText("無影片可觀看");
//		 textview4.setOnClickListener(new View.OnClickListener() {
//			 @Override
//			 public void onClick(View v) {
//				 if(data.getUrl().equals("")) return;
//				 Intent i = new Intent(TwoActivity.this, VideoViewActivity.class);
//				 Bundle bundle = new Bundle();
//				 bundle.putString("video",data.getUrl());
//				 i.putExtras(bundle);
//				 startActivity(i);
//			 }
//		 });
//
//		textview5.setText("發文時間:"+data.getDate());
//				mShareBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				if (ShareDialog.canShow(ShareLinkContent.class)) {
//					ShareLinkContent linkContent = new ShareLinkContent.Builder()
//							.setContentTitle("我要推薦好文章")
//							.setContentDescription(data.getMessage())
//							.setContentUrl(Uri.parse("https://play.google.com/store/apps/details?id=com.jackpan.GayPlace"))
//							.setImageUrl(Uri.parse(data.getPic()))
//							.build();
//					shareDialog.show(linkContent);
//				}
//				MyApi.copyToClipboard(getApplication(),data.getMessage());
//			}
//		});
//		mUserLikeBtn.setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//				String Time =MySharedPrefernces.getMyCardTime(TwoActivity.this);
//
//				if (Time.equals("")) Time = "0";
//				if (System.currentTimeMillis() - Long.valueOf(Time) > 24*60 * 60 * 1000) {
//					MySharedPrefernces.saveMyCardTime(TwoActivity.this, System.currentTimeMillis() + "");
//					Like();
//					Toast.makeText(TwoActivity.this,"已經對他按讚囉！！",Toast.LENGTH_SHORT).show();
//
//				}else{
//					Toast.makeText(TwoActivity.this,"你今天已經按過讚囉！明天再按吧",Toast.LENGTH_SHORT).show();
//				}
//
//			}
//		});
		Log.d(TAG, "loadIntent: "+data.id+data.date);
		toUserMsg.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(data.tomsg ==null) return;
				Intent i = new Intent(TwoActivity.this, UserMessgeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("id",data.id+data.date);
				bundle.putString("name",data.name);
				bundle.putString("msg",data.tomsg);
				i.putExtras(bundle);
				startActivity(i);
			}
		});

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();



	}
	private void loadImage(final String path,
			final ImageView imageView){



		new Thread(){

			@Override
			public void run() {

				try {
					URL imageUrl = new URL(path);
					HttpURLConnection httpCon = 
							(HttpURLConnection) imageUrl.openConnection();
					InputStream imageStr =  httpCon.getInputStream();
					bitmap = decodeSampledBitmapFromResourceMemOpt(imageStr, 640, 640);
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							imageView.setImageBitmap(bitmap);

						}
					});


				} catch (Exception e) {
					e.printStackTrace();

				}



			}


		}.start();

	}
	public static int calculateInSampleSize(
			BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight
					&& (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}
		}

		return inSampleSize;
	}

	public Bitmap decodeSampledBitmapFromResourceMemOpt(
			InputStream inputStream, int reqWidth, int reqHeight) {

		byte[] byteArr = new byte[0];
		byte[] buffer = new byte[1024];
		int len;
		int count = 0;

		try {
			while ((len = inputStream.read(buffer)) > -1) {
				if (len != 0) {
					if (count + len > byteArr.length) {
						byte[] newbuf = new byte[(count + len) * 2];
						System.arraycopy(byteArr, 0, newbuf, 0, count);
						byteArr = newbuf;
					}

					System.arraycopy(buffer, 0, byteArr, count, len);
					count += len;
				}
			}

			final BitmapFactory.Options options = new BitmapFactory.Options();
			options.inJustDecodeBounds = true;
			BitmapFactory.decodeByteArray(byteArr, 0, count, options);

			options.inSampleSize = calculateInSampleSize(options, reqWidth,
					reqHeight);
			options.inPurgeable = true;
			options.inInputShareable = true;
			options.inJustDecodeBounds = false;
			options.inPreferredConfig = Bitmap.Config.ARGB_8888;

			//            int[] pids = { android.os.Process.myPid() };
			//            MemoryInfo myMemInfo = mAM.getProcessMemoryInfo(pids)[0];
			//            Log.e(TAG, "dalvikPss (decoding) = " + myMemInfo.dalvikPss);

			return BitmapFactory.decodeByteArray(byteArr, 0, count, options);

		} catch (Exception e) {
			e.printStackTrace();

			return null;
		}
	}

	@Override
	protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		callbackManager.onActivityResult(requestCode, resultCode, data);
	}
}
