package com.diet;

import java.util.ArrayList;
import java.util.Calendar;
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
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;

//規畫食物清單並計算卡若里
public class sport extends Activity implements OnClickListener
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

	sport mydiet = this;
	String date ;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		//food.xml
		setContentView(R.layout.sport);

		data = new ArrayList<foodStruct>();

		//取得所有food的名稱和卡若里在string.xml
		kind = this.getResources().getStringArray(R.array.kind);

		int index=0;
		//create all data
		while (index<kind.length)
		{
			foodStruct item = new foodStruct();

			if (kind[index].equals("0"))
			{
				index++;
				item.classify = kind[index];
			}

			int i=index+1;
			while (i<kind.length && !kind[i].equals("0"))
			{
				StringTokenizer stoken = null;

				stoken = new StringTokenizer( kind[i], "," );

				int j=0;
				while( stoken.hasMoreTokens() )
				{
					if (j == 0)
					{
						item.foodname.add(stoken.nextToken());
					}
					else
					{
						item.hot.add((int) Integer.valueOf(stoken.nextToken()));
						break;
					}

					j++;
				}
				i++;
			}
			data.add(item);
			index = i;
		}

		classify = new ArrayList<String>();

		for (int i=0; i<data.size(); i++)
		{
			classify.add(data.get(i).classify);
		}


		//Gui
		showlist = (TextView)findViewById(R.id.showlist);

		//四按鈕: 計算, 清除, 離開, food選擇
		Button A = (Button)findViewById(R.id.cal);
		A.setOnClickListener(this);
		Button B = (Button)findViewById(R.id.clear);
		B.setOnClickListener(this);
		Button C = (Button)findViewById(R.id.exit);
		C.setOnClickListener(this);
		Button D = (Button)findViewById(R.id.foodchoice);
		D.setOnClickListener(this);

		Button E = (Button)findViewById(R.id.button);
		E.setOnClickListener(this);

		mdate = (TextView)findViewById(R.id.mdate);

		final Calendar c = Calendar.getInstance();
		myYear = c.get(Calendar.YEAR);
		myMonth = c.get(Calendar.MONTH);
		myDay = c.get(Calendar.DAY_OF_MONTH);
		 date = String.valueOf(myYear) + "-" + String.valueOf(myMonth+1) + "-" + String.valueOf(myDay);
		mdate.setText(date);

		//選單下拉式, 選food類別: 把從string.xml的食物放進去讓使用者選
		Spinner spinner = (Spinner) findViewById(R.id.foodkind);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, classify);
		spinner.setAdapter(spinnerArrayAdapter);
		spinner.setOnItemSelectedListener(new Spinner.OnItemSelectedListener(){
			public void onItemSelected(AdapterView adapterView, View view, int position, long id)
			{
				//記下使用者選的
				kind_id = position;
			}

			public void onNothingSelected(AdapterView arg0) {
			}
		});

	}

	private static final String TAG = "sport";
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
						et[i].setText("0");
						ll.addView(et[i]);
					}

					//顯示出來Dialog
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
											listv = "";
											for(int i = 0; i < item.foodname.size(); i++)
											{
												//若是0代表food沒吃
												if (et[i].getText().toString().equals("0") || et[i].getText().toString().equals("")) continue;

												//顯示用

												listarray.add(et[i].getText().toString()+"份"+item.foodname.get(i) + "(" + item.hot.get(i) *Integer.parseInt(et[i].getText().toString())+ "卡"+"\t"+"日期："+date+")");
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

											//計算總熱量
											hot += total;

											//把所有顯示用的整理出來

											for (int j=0; j<listarray.size(); j++)
											{
												listv = listv + listarray.get(j) + "\n";
											}
											showlist.setText(listv);
										}
									});

					builder.show();


				}

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
			date = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
			mdate.setText(date);
		}
	};



	private void select(int kind_id2) {
		// TODO Auto-generated method stub

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

							}
						}
				)
				.show();
	}
}
