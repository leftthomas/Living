package com.living.ui;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.CountListener;
import cn.bmob.v3.listener.FindListener;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.living.adapter.HouseAdapter;
import com.living.bean.House;

/**
 * 每日精选
 * 
 * @author left
 * 
 */
@SuppressLint("SimpleDateFormat")
public class DailyActivity extends ActivityBase {

	private PullToRefreshListView mPullRefreshListView;
	private HouseAdapter mAdapter;
	private List<House> houses = new ArrayList<House>();
	private TextView time;
	private int skipnum = 0;// 默认skip数
	private int totalnum = 0;// 记录总数据量
	private Date date = new Date();
	private Calendar cal = Calendar.getInstance();
	private SimpleDateFormat dft = new SimpleDateFormat("yyyy-MM-dd");
	private boolean ispull = true;

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_daily);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setPullLabel("查看后一天精选房源",
				Mode.PULL_DOWN_TO_REFRESH);
		mPullRefreshListView.setPullLabel("查看前一天精选房源", Mode.PULL_UP_TO_REFRESH);
		time = (TextView) findViewById(R.id.time);
		time.setText(dft.format(date.getTime()));
		/**
		 * 实现 接口 OnRefreshListener2<ListView> 以便于监听 滚动条到顶部和到底部
		 */
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

		ListView actualListView = mPullRefreshListView.getRefreshableView();

		// Need to use the Actual ListView when registering for Context Menu
		registerForContextMenu(actualListView);
		BmobQuery<House> query = new BmobQuery<House>();
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
		// You can also just use setListAdapter(mAdapter) or
		// mPullRefreshListView.setAdapter(mAdapter)
		actualListView.setAdapter(mAdapter);
		mPullRefreshListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				Intent intent = new Intent(DailyActivity.this,
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
			// Simulates a background job.
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
					cal.setTime(date);
					cal.set(Calendar.DATE, cal.get(Calendar.DATE) + 1);
					try {
						date = dft.parse(dft.format(cal.getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
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
					cal.setTime(date);
					cal.set(Calendar.DATE, cal.get(Calendar.DATE) - 1);
					try {
						date = dft.parse(dft.format(cal.getTime()));
					} catch (ParseException e) {
						e.printStackTrace();
					}
					// 重新请求数据，并刷新ListView
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
		query.order("-createdAt");
		query.setSkip(skipnum);
		// 执行查询方法
		query.findObjects(this, new FindListener<House>() {
			@Override
			public void onSuccess(List<House> allhouses) {
				if (allhouses != null && allhouses.size() > 0) {
					// 先清空，再加载数据
					houses.clear();
					// 有数据，更新正确时间
					time.setText(dft.format(date.getTime()));
					for (House house : allhouses) {
						houses.add(house);
					}
					mAdapter.notifyDataSetChanged();
					// 通知RefreshListView 我们已经更新完成
					// Call onRefreshComplete when the list has been refreshed.
					mPullRefreshListView.onRefreshComplete();
				} else {
					// 没有数据，更新正确时间
					ShowToast("没有更多数据了！");
					// 通知RefreshListView 我们已经更新完成
					// Call onRefreshComplete when the list has been refreshed.
					mPullRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int code, String msg) {
				// 无网络，更新正确时间
				ShowToast("网络错误，请重试！");
				// 通知RefreshListView 我们已经更新完成
				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();
			}
		});
	}
}
