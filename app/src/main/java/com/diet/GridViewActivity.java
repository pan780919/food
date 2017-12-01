package com.diet;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GridViewActivity extends AppCompatActivity {
    private int[] image = {
            R.drawable.a1, R.drawable.a2, R.drawable.a3,
            R.drawable.a4, R.drawable.a5, R.drawable.a6,
            R.drawable.a7, R.drawable.a8,R.drawable.a9,
            R.drawable.a10,R.drawable.a11,R.drawable.a12
    };
    private GridView gridView;
    private Context mContext = GridViewActivity.this;
    private static final String TAG = "GridViewActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grid_view);
        List<Map<String, Object>> items = new ArrayList<>();
        for (int i = 0; i < image.length; i++) {
            Map<String, Object> item = new HashMap<>();
            item.put("image", image[i]);
            items.add(item);
        }


        gridView = (GridView)findViewById(R.id.main_page_gridview);
        gridView.setNumColumns(3);
        gridView.setAdapter(new ImageAdapter(this));

        //GridView的點選事件, 傳入int position知user點選哪一個item
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView parent, View v, int position, long id)
            {
                Log.d(TAG, "onItemClick: "+ "" + position);
                MySharedPrefernces.saveUserId(mContext,position);
            }
        });
    }
    class ImageAdapter extends BaseAdapter {
        private Context mContext;

        public ImageAdapter(Context c) {
            mContext = c;
        }

        public int getCount() {
            return image.length;
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return 0;
        }

        // create a new ImageView for each item referenced by the Adapter
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                imageView = new ImageView(mContext);
                imageView.setLayoutParams(new GridView.LayoutParams(300, 450));
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setPadding(8, 20, 8, 8);
            } else {
                imageView = (ImageView) convertView;
            }

            imageView.setImageResource(image[position]);
            return imageView;
        }
    }

}
