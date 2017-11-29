package com.diet;

import com.sqlite.sport;

import android.content.Context;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TxtListView extends LinearLayout {
	
	private TextView mTitle, mTime;
	
	public TxtListView(Context context, sport sport) {
		super(context);
		this.setOrientation(VERTICAL);
		this.setPadding(5, 5, 5, 5);
		this.setBackgroundColor(0xff888888);
		
		mTitle = new TextView(context);
		mTitle.setText(sport.name);
		mTitle.setTextSize(20);
		addView(mTitle, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		
		mTime = new TextView(context);
		mTime.setText(sport.item);
		mTime.setTextSize(12);
		mTime.setGravity(Gravity.RIGHT);
		addView(mTime, new LinearLayout.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
	}

	public void setDiaryTitle(String name) {
		mTitle.setText(name);
	}
	
	public void setDiaryTime(String name) {
		mTime.setText(name);
	}
}