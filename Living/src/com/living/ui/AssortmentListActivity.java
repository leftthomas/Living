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
	private int skipnum = 0;// Ĭ��skip��
	private int totalnum = 0;// ��¼��������
	private String category = "";// ��¼��������
	private TextView name;
	private ImageView back;
	private boolean ispull = true;// �����ж�������ˢ�»����������أ�Ĭ������ˢ��

	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_assortment_list);
		name = (TextView) findViewById(R.id.name);
		back = (ImageView) findViewById(R.id.back);
		mPullRefreshListView = (PullToRefreshListView) findViewById(R.id.pull_refresh_list);
		mPullRefreshListView.setPullLabel("�鿴���·�Դ", Mode.PULL_DOWN_TO_REFRESH);
		mPullRefreshListView.setPullLabel("�鿴���෿Դ", Mode.PULL_UP_TO_REFRESH);
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
		query.addWhereEqualTo("category", category);
		query.order("-createdAt");
		query.setSkip(skipnum);
		// ִ�в�ѯ����
		query.findObjects(this, new FindListener<House>() {
			@Override
			public void onSuccess(List<House> allhouses) {
				if (allhouses != null && allhouses.size() > 0) {
					// ����գ��ټ�������
					houses.clear();
					for (House house : allhouses) {
						houses.add(house);
					}
					mAdapter.notifyDataSetChanged();
					mPullRefreshListView.onRefreshComplete();
				} else {
					ShowToast("û�и��������ˣ�");
					mPullRefreshListView.onRefreshComplete();
				}
			}

			@Override
			public void onError(int code, String msg) {
				ShowToast("������������ԣ�");
				mPullRefreshListView.onRefreshComplete();
			}
		});
	}
}
