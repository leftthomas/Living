package com.living.adapter;

import java.util.ArrayList;
import java.util.List;
import com.living.ui.R;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * �������񲼾ֵ�������
 * 
 * @author left
 * 
 */
@SuppressLint("InflateParams")
public class GridAdapter extends BaseAdapter {
	public static class Item {
		public String text;
		public int resId;
	}

	private List<Item> mItems = new ArrayList<GridAdapter.Item>();
	private Context mContext;

	//���ϲ����
	public List<Item> getList() {
		return mItems;
	}

	public GridAdapter(Context context) {
		Item object = new Item();
		object.text = "#Ծ��סլ";
		object.resId = R.drawable.yueceng;
		mItems.add(object);

		Item object1 = new Item();
		object1.text = "#��԰ʽסլ";
		object1.resId = R.drawable.huayuan;
		mItems.add(object1);

		Item object2 = new Item();
		object2.text = "#����Ԣ";
		object2.resId = R.drawable.shangwu;
		mItems.add(object2);

		Item object3 = new Item();
		object3.text = "#��ʽסլ";
		object3.resId = R.drawable.fushi;
		mItems.add(object3);

		Item object4 = new Item();
		object4.text = "#����סլ";
		object4.resId = R.drawable.lianpai;
		mItems.add(object4);

		Item object5 = new Item();
		object5.text = "#��Ժʽסլ";
		object5.resId = R.drawable.duyuan;
		mItems.add(object5);

		Item object6 = new Item();
		object6.text = "#��Ԫʽסլ";
		object6.resId = R.drawable.danyuan;
		mItems.add(object6);

		Item object7 = new Item();
		object7.text = "#���߲�סլ";
		object7.resId = R.drawable.chaogao;
		mItems.add(object7);

		Item object8 = new Item();
		object8.text = "#����ʽסլ";
		object8.resId = R.drawable.neilang;
		mItems.add(object8);

		Item object9 = new Item();
		object9.text = "#�Ƶ�ʽ��Ԣ";
		object9.resId = R.drawable.jiudian;
		mItems.add(object9);

		Item object10 = new Item();
		object10.text = "#���ܻ�סլ";
		object10.resId = R.drawable.zhineng;
		mItems.add(object10);

		Item object11 = new Item();
		object11.text = "#�߲�סլ";
		object11.resId = R.drawable.gaoceng;
		mItems.add(object11);

		Item object12 = new Item();
		object12.text = "#����Ԣ";
		object12.resId = R.drawable.dansheng;
		mItems.add(object12);

		Item object13 = new Item();
		object13.text = "#����ʽסլ";
		object13.resId = R.drawable.wailang;
		mItems.add(object13);

		Item object14 = new Item();
		object14.text = "#��̨ʽסլ";
		object14.resId = R.drawable.tuitai;
		mItems.add(object14);
		mContext = context;

		Item object15 = new Item();
		object15.text = "#Ծ��ʽסլ";
		object15.resId = R.drawable.yuelang;
		mItems.add(object15);
		mContext = context;

		Item object16 = new Item();
		object16.text = "#��Ԣʽסլ";
		object16.resId = R.drawable.gongyu;
		mItems.add(object16);
		mContext = context;

		Item object17 = new Item();
		object17.text = "#��ʽסլ";
		object17.resId = R.drawable.ta;
		mItems.add(object17);
		mContext = context;

		Item object18 = new Item();
		object18.text = "#�ݼ�ʽסլ";
		object18.resId = R.drawable.tijian;
		mItems.add(object18);
		mContext = context;

		Item object19 = new Item();
		object19.text = "#���ʽסլ";
		object19.resId = R.drawable.cuoceng;
		mItems.add(object19);
		mContext = context;
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
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.grid_item, null);
		}
		ImageView image = (ImageView) convertView.findViewById(R.id.image_item);
		TextView text = (TextView) convertView.findViewById(R.id.text_item);
		Item item = (Item) getItem(position);
		image.setImageResource(item.resId);
		text.setText(item.text);
		return convertView;
	}
}
