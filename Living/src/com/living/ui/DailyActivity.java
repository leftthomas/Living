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
 * ÿ�վ�ѡ
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
	private int skipnum = 0;// Ĭ��skip��
	private int totalnum = 0;// ��¼��������
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
		mPullRefreshListView.setPullLabel("�鿴��һ�쾫ѡ��Դ",
				Mode.PULL_DOWN_TO_REFRESH);
		mPullRefreshListView.setPullLabel("�鿴ǰһ�쾫ѡ��Դ", Mode.PULL_UP_TO_REFRESH);
		time = (TextView) findViewById(R.id.time);
		time.setText(dft.format(date.getTime()));
		/**
		 * ʵ�� �ӿ� OnRefreshListener2<ListView> �Ա��ڼ��� �������������͵��ײ�
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
				// ������������ҳ���巿�ݵ�id�Բ�ѯ
				intent.putExtra("id", houses.get(arg2 - 1).getObjectId());
				startActivity(intent);
			}
		});

	}

	// ģ������������ݵ��첽������
	private class GetDataTask extends AsyncTask<Void, Void, String[]> {

		// ���߳���������
		@Override
		protected String[] doInBackground(Void... params) {
			// Simulates a background job.
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
			}
			return null;
		}

		// ���̸߳���UI
		@Override
		protected void onPostExecute(String[] result) {
			if (ispull) {
				// ����ˢ���¼�������һ�������
				if (skipnum - 5 < 0) {
					ShowToast("�������·�Դ��Ϣ��");
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
					// �����������ݣ���ˢ��ListView
					fillData();
				}
			} else if (!ispull) {
				// ���������¼�����ǰһ�������
				if (skipnum + 5 >= totalnum) {
					ShowToast("û�и��෿Դ��Ϣ�ˣ�");
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
					// �����������ݣ���ˢ��ListView
					fillData();
				}
			}

			super.onPostExecute(result);
		}
	}

	// ��ѯ��Դ��������������
	public void fillData() {
		// ��ѯ���������ķ�Դ
		BmobQuery<House> query = new BmobQuery<House>();
		query.setLimit(5);// ÿ�η�����������
		query.order("-createdAt");
		query.setSkip(skipnum);
		// ִ�в�ѯ����
		query.findObjects(this, new FindListener<House>() {
			@Override
			public void onSuccess(List<House> allhouses) {
				if (allhouses != null && allhouses.size() > 0) {
					// ����գ��ټ�������
					houses.clear();
					// �����ݣ�������ȷʱ��
					time.setText(dft.format(date.getTime()));
					for (House house : allhouses) {
						houses.add(house);
					}
					mAdapter.notifyDataSetChanged();
					// ֪ͨRefreshListView �����Ѿ��������
					// Call onRefreshComplete when the list has been refreshed.
					mPullRefreshListView.onRefreshComplete();
				} else {
					// û�����ݣ�������ȷʱ��
					ShowToast("û�и��������ˣ�");
					// ֪ͨRefreshListView �����Ѿ��������
					// Call onRefreshComplete when the list has been refreshed.
					mPullRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int code, String msg) {
				// �����磬������ȷʱ��
				ShowToast("������������ԣ�");
				// ֪ͨRefreshListView �����Ѿ��������
				// Call onRefreshComplete when the list has been refreshed.
				mPullRefreshListView.onRefreshComplete();
			}
		});
	}
}
