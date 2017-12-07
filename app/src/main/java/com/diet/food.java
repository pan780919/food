package com.diet;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.StringTokenizer;

import com.sqlite.foodStruct;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//規畫食物清單並計算卡若里
public class food extends Activity implements OnClickListener
{
	//食物清單
	public static ArrayList<String> listarray = new ArrayList<String>();
	public int kind_id = -1;
	static String listv = "";

	//食物消耗卡若里對應的值
	public String kind[] = null;
	ArrayList<String> classify;
	ArrayList<foodStruct> data;

	int myYear, myMonth, myDay;

	private TextView mdate;


	foodStruct item = null;

	//GUI: 輸入食物的量
	private TextView showlist;
	private TextView cb[];
	private EditText et[];

	//總熱量
	public static int hot = 0;

	food mydiet = this;

	EditText ddate;

	ArrayList<String> namelist = new ArrayList<String>();
	ArrayList<String> unitlist = new ArrayList<String>();
	ArrayList<String> numlist = new ArrayList<String>();

	ImageView iv;

	final Item[] items = {
			new Item("", R.drawable.a1),
			new Item("", R.drawable.a2),
			new Item("", R.drawable.a2),
			new Item("", R.drawable.a3),
			new Item("", R.drawable.a4),
			new Item("", R.drawable.a5),
			new Item("", R.drawable.a6),
			new Item("", R.drawable.a7),
			new Item("", 0),//no icon for this one
	};

	private SharedPreferences settings;
	private static final String mydata = "DATA";
	private static final String nameField = "NAME";

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//food.xml
		setContentView(R.layout.food);

		iv = (ImageView)findViewById(R.id.imageView1);

		ListAdapter adapter = new ArrayAdapter<Item>(
				this,
				android.R.layout.select_dialog_item,
				android.R.id.text1,
				items){
			public View getView(int position, View convertView, ViewGroup parent) {
				//Use super class to create the View
				View v = super.getView(position, convertView, parent);
				TextView tv = (TextView)v.findViewById(android.R.id.text1);

				//Put the image on the TextView
				//tv.setCompoundDrawablesWithIntrinsicBounds(, 0, 0, 0);

				tv.setBackgroundResource(items[position].icon);

				//Add margin between image and text (support various screen densities)
				int dp5 = (int) (1 * getResources().getDisplayMetrics().density + 0.5f);
				tv.setCompoundDrawablePadding(dp5);


				return v;
			}
		};


		new AlertDialog.Builder(this)
				.setTitle("CHOICE")
				.setAdapter(adapter, new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						iv.setImageResource(items[item].icon);

						settings = getSharedPreferences(mydata,0);
						settings.edit()
								.putInt(nameField, items[item].icon)
								.commit();
					}
				}).show();

	}

	private static final String TAG = "food";
	public void onClick(View v)
	{
		switch (v.getId()) {
			case R.id.cal:

				//選了計算
				//listv = showlist.getText().toString();

				//輸出結果

				openOptionsDialog("總熱量 ＝ " + hot + "卡");

				break;
			case R.id.clear:
				//清除選單和總熱量
				listarray.clear();
				showlist.setText("");
				hot = 0;
				break;
			case R.id.exit:
				//離開
				finish();
				break;
			case R.id.foodchoice:
				if (kind_id != -1)
				{
					item = data.get(kind_id);

					//製作Dialog: 讓使用者可依食物去分配自己的量, 幫忙加入總food表單
					ScrollView sv = new ScrollView(this);
					LinearLayout ll = new LinearLayout(this);
					ll.setOrientation(LinearLayout.VERTICAL);
					sv.addView(ll);

					//顯示用
					cb = new TextView[item.foodname.size()];
					//自己的量輸入
					et = new EditText[item.foodname.size()];
					//加入Dialog
					for(int i = 0; i < item.foodname.size(); i++) {
						cb[i] = new TextView(this);
						cb[i].setText(item.foodname.get(i));
						ll.addView(cb[i]);
						et[i] = new EditText(this);
						et[i].setInputType(InputType.TYPE_CLASS_NUMBER);
						et[i].setText("0");
						ll.addView(et[i]);
					}

//					//顯示出來Dialog
					AlertDialog.Builder builder = new AlertDialog.Builder(this)
							.setTitle("請選擇")
							.setMessage("")
							.setView(sv)
							.setPositiveButton("Ok",
									new DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which)
										{
											//用迴圈把所有使用者輸入的東西整理出來: 食物, 依量來計算總熱量
											int total = 0;
											for(int i = 0; i < item.foodname.size(); i++)
											{

												//若是0代表food沒吃
												if (et[i].getText().toString().equals("0") || et[i].getText().toString().equals("")) continue;
												//顯示用
												Log.d(TAG, "onClick: "+item.foodname.get(i));
												listarray.add(item.foodname.get(i) + "(" + item.hot.get(i) + ")");

												//計算熱量
												try
												{
													total = total + Integer.parseInt(et[i].getText().toString()) * item.hot.get(i);

												}
												catch (Exception x)
												{
													x.printStackTrace();
												}
											}

											for (int i=0; i<namelist.size(); i++)
											{
												Log.d(TAG, "onClick: "+item.foodname.get(i));
												listarray.add(namelist.get(i) + "(" + unitlist.get(i) + "x" + numlist.get(i) + ")");

												//計算熱量
												try
												{
													total = total + Integer.parseInt(unitlist.get(i)) * Integer.parseInt(numlist.get(i));

												}
												catch (Exception x)
												{
													x.printStackTrace();
												}
											}

											//計算總熱量
											hot += total;

											//把所有顯示用的整理出來
											listv = "";
											for (int j=0; j<listarray.size(); j++)
											{
												listv = listv + listarray.get(j) + "\n";
											}
											showlist.setText(listv);
										}
									});

					builder.show();


				}
//				input();
				//若找不到類別, 就返回
				//if (msg == null)  return;

				break;
			case R.id.button:
				final Calendar c = Calendar.getInstance();
				myYear = c.get(Calendar.YEAR);
				myMonth = c.get(Calendar.MONTH);
				myDay = c.get(Calendar.DAY_OF_MONTH);
				showDialog(1);
				break;

		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		// TODO Auto-generated method stub
		switch(id){
			case 1:

				return new DatePickerDialog(this,
						myDateSetListener,
						myYear, myMonth, myDay);
		}

		return null;
	}

	private DatePickerDialog.OnDateSetListener myDateSetListener = new DatePickerDialog.OnDateSetListener(){

		@Override
		public void onDateSet(DatePicker view, int year,
							  int monthOfYear, int dayOfMonth) {
			// TODO Auto-generated method stub
			String date = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
			mdate.setText(date);
		}
	};



	private void select(int kind_id2) {
		// TODO Auto-generated method stub



	}

	public void input()
	{
		AlertDialog.Builder alert = new AlertDialog.Builder(this);

		alert.setTitle("input");
		alert.setMessage("熱量輸入(eg: 米,220,2)?");

		ScrollView sv = new ScrollView(this);
		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);
		sv.addView(ll);

		TextView tdate = new TextView(this);
		tdate.setText("熱量輸入: ");
		ddate = new EditText(this);
		ddate.setText("");
		ll.addView(tdate);
		ll.addView(ddate);

		alert.setView(sv);

		alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
				String data = ddate.getText().toString();

				if (data.equals(""))
				{
					openOptionsDialog("有值沒填");
					return;
				}
				else
				{
					String mydata[] = data.split(",");
					namelist.add(mydata[0]);
					unitlist.add(mydata[1]);
					numlist.add(mydata[2]);
					Toast.makeText(food.this, "請按計算更新!!", Toast.LENGTH_LONG).show();
				}
			}
		});

		alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton)
			{
			}
		});

		alert.show();

	}

	//Dialog用
	private void openOptionsDialog(String info)
	{
		new AlertDialog.Builder(this)
				.setTitle("message")
				.setMessage(info)
				.setPositiveButton("OK",
						new DialogInterface.OnClickListener()
						{
							public void onClick(DialogInterface dialoginterface, int i)
							{
								new AlertDialog.Builder(mydiet)
										.setTitle("問題")
										.setMessage("是否要繼續計算運動耗量")
										.setNegativeButton("NO",
												new DialogInterface.OnClickListener() {

													public void onClick(DialogInterface dialoginterface, int i) {

													}
												}
										)

										.setPositiveButton("YES",
												new DialogInterface.OnClickListener() {
													public void onClick(DialogInterface dialoginterface, int i)
													{
														Intent  intent = new Intent();
														intent.setClass(food.this, sport.class);
														Bundle bData = new Bundle();

														// 寫入資料到 Bundle 中
														bData.putString( "fhot", Integer.toString(hot));
														listv = "";
														for (int j=0; j<listarray.size(); j++)
														{
															listv = listv + listarray.get(j) + "|";
														}

														Log.i("TAG", listv);
														bData.putString( "flist", listv);
														intent.putExtras( bData );
														startActivity(intent);
														finish();
													}

												}
										)
										.show();
							}
						}
				)
				.show();
	}
}
