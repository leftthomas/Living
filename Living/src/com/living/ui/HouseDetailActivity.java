package com.living.ui;

import com.living.bean.House;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.GetListener;
import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class HouseDetailActivity extends ActivityBase {
	private String id;
	private TextView name;
	private TextView detail;
	private TextView describe;
	private ImageView photo;
	private ImageView back;
	private String content;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_house_detail);
		id = getIntent().getStringExtra("id");
		name = (TextView) findViewById(R.id.name);
		detail = (TextView) findViewById(R.id.detail);
		describe = (TextView) findViewById(R.id.describe);
		photo = (ImageView) findViewById(R.id.photo);
		back = (ImageView) findViewById(R.id.back);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		BmobQuery<House> query = new BmobQuery<House>();
		query.getObject(this, id, new GetListener<House>() {

			@Override
			public void onSuccess(House object) {
				name.setText(object.getName());
				detail.setText(object.getCategory() + " | " + object.getPrice());
				object.getPhoto().loadImage(HouseDetailActivity.this, photo);
				content = object.getDescribe();
				// 加载富文本
				CharSequence charSequence = Html.fromHtml(content);
				describe.setText(charSequence);
				describe.setMovementMethod(ScrollingMovementMethod
						.getInstance());
			}

			@Override
			public void onFailure(int code, String arg0) {
			}
		});
	}
}
