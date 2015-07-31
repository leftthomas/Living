package com.living.ui;

import com.living.adapter.GridAdapter;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

/**
 * 房屋分类
 * 
 * @author left
 * 
 */
public class AssortmentActivity extends ActivityBase {
	private GridView gridView;
	private GridAdapter gridAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assortment);
		gridView = (GridView) findViewById(R.id.mygridview);
		gridAdapter = new GridAdapter(getBaseContext());
		gridView.setAdapter(gridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AssortmentActivity.this,
						AssortmentListActivity.class);
				//向后续activity传值
				intent.putExtra("category",
						gridAdapter.getList().get(arg2).text
								.substring(1,
										gridAdapter.getList().get(arg2).text
												.length()));
				startActivity(intent);
			}
		});
	}
}
