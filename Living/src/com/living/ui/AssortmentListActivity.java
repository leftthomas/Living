package com.living.ui;

import java.util.ArrayList;
import java.util.List;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.living.adapter.HouseAdapter;
import com.living.bean.House;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.TextView;

public class AssortmentListActivity extends ActivityBase {
	private PullToRefreshListView mPullRefreshListView;
	private HouseAdapter mAdapter;
	private List<House> houses = new ArrayList<House>();
	private int skipnum = 0;// 默认skip数
	private int totalnum = 0;// 记录总数据量
	private String category = "";// 记录哪种类型
	private TextView name;
	private ImageView back;
	private boolean ispull = true;// 用来判断是下拉刷新还是上拉加载，默认下拉刷新

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assortment_list);
		name = (TextView) findViewById(R.id.name);
		back = (ImageView) findViewById(R.id.back);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setPullLabel("查看最新房源", Mode.PULL_DOWN_TO_REFRESH);
		mPullRefreshListView.setPullLabel("查看更多房源", Mode.PULL_UP_TO_REFRESH);
		mPullRefreshListView
				.setOnRefreshListener(new OnRefreshListener2<ListView>() {
					@Override
					public void onPullDownToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						ispull = true;
						new GetDataTask().execute();
					}

					@Override
					public void onPullUpToRefresh(
							PullToRefreshBase<ListView> refreshView) {
						ispull = false;
						new GetDataTask().execute();
					}
				});
		category = (String) getIntent().getCharSequenceExtra("category");
		name.setText(category);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		ListView actualListView = mPullRefreshListView.getRefreshableView();
		registerForContextMenu(actualListView);
		BmobQuery<House> query = new BmobQuery<House>();
		query.addWhereEqualTo("category", category);
		query.count(this, House.class, new CountListener() {
			@Override
			public void onSuccess(int count) {
				totalnum = count;
			}

			@Override
			public void onFailure(int code, String msg) {
			}
		});
		fillData();
		mAdapter = new HouseAdapter(this, houses);
		actualListView.setAdapter(mAdapter);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(AssortmentListActivity.this,
						HouseDetailActivity.class);
				// 传给房屋详情页具体房屋的id以查询
				intent.putExtra("id", houses.get(arg2 - 1).getObjectId());
				startActivity(intent);
			}
		});

	}

	// 模拟网络加载数据的异步请求类
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		// 子线程请求数据
		@Override
		protected String[] doInBackground(Void... params) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			return null;
		}

		// 主线程更新UI
		@Override
		protected void onPostExecute(String[] result) {

			if (ispull) {
				// 下拉刷新事件，看后一天的内容
				if (skipnum - 5 < 0) {
					ShowToast("已是最新房源信息！");
					mPullRefreshListView.onRefreshComplete();
				} else {
					skipnum -= 5;
					// 重新请求数据，并刷新ListView
					fillData();
				}
			} else if (!ispull) {
				// 上拉加载事件，看前一天的内容
				if (skipnum + 5 >= totalnum) {
					ShowToast("没有更多房源信息了！");
					mPullRefreshListView.onRefreshComplete();
				} else {
					skipnum += 5;
					fillData();
				}
			}
			super.onPostExecute(result);
		}
	}

	// 查询房源，加载网络数据
	public void fillData() {
		// 查询符合条件的房源
		BmobQuery<House> query = new BmobQuery<House>();
		query.setLimit(5);// 每次返回五条数据
		query.addWhereEqualTo("category", category);
		query.order("-createdAt");
		query.setSkip(skipnum);
		// 执行查询方法
		query.findObjects(this, new FindListener<House>() {
			@Override
			public void onSuccess(List<House> allhouses) {
				if (allhouses != null && allhouses.size() > 0) {
					// 先清空，再加载数据
					houses.clear();
					for (House house : allhouses) {
						houses.add(house);
					}
					mAdapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
				} else {
					ShowToast("没有更多数据了！");
					mPullRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int code, String msg) {
				ShowToast("网络错误，请重试！");
				mPullRefreshListView.onRefreshComplete();
			}
		});
	}
}
