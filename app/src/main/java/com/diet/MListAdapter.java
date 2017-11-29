package com.diet;

import java.util.List;

import com.sqlite.sport;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;


public class MListAdapter extends BaseAdapter {

	private Context mContext;
	private List<sport> mItems;
	
	public MListAdapter(Context context,List<sport> mItems) {
		this.mContext = context;
		this.mItems = mItems;
	}
	
	@Override
	public int getCount() { 
		return mItems.size(); 
	}
	
	@Override
	public Object getItem(int position) {
		return mItems.get(position); 
	}
	
	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TxtListView btv;
		if (convertView == null) {
			btv = new TxtListView(mContext, mItems.get(position));
		} else {
			btv = (TxtListView) convertView;
			btv.setDiaryTitle(mItems.get(position).name + " " + mItems.get(position).item);
			btv.setDiaryTime(mItems.get(position).rdate);
		}
		return btv;
	}
}