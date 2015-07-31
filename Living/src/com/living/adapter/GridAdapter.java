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
 * 方形网格布局的适配器
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

	//给上层调用
	public List<Item> getList() {
		return mItems;
	}

	public GridAdapter(Context context) {
		Item object = new Item();
		object.text = "#跃层住宅";
		object.resId = R.drawable.yueceng;
		mItems.add(object);

		Item object1 = new Item();
		object1.text = "#花园式住宅";
		object1.resId = R.drawable.huayuan;
		mItems.add(object1);

		Item object2 = new Item();
		object2.text = "#商务公寓";
		object2.resId = R.drawable.shangwu;
		mItems.add(object2);

		Item object3 = new Item();
		object3.text = "#复式住宅";
		object3.resId = R.drawable.fushi;
		mItems.add(object3);

		Item object4 = new Item();
		object4.text = "#联排住宅";
		object4.resId = R.drawable.lianpai;
		mItems.add(object4);

		Item object5 = new Item();
		object5.text = "#独院式住宅";
		object5.resId = R.drawable.duyuan;
		mItems.add(object5);

		Item object6 = new Item();
		object6.text = "#单元式住宅";
		object6.resId = R.drawable.danyuan;
		mItems.add(object6);

		Item object7 = new Item();
		object7.text = "#超高层住宅";
		object7.resId = R.drawable.chaogao;
		mItems.add(object7);

		Item object8 = new Item();
		object8.text = "#内廊式住宅";
		object8.resId = R.drawable.neilang;
		mItems.add(object8);

		Item object9 = new Item();
		object9.text = "#酒店式公寓";
		object9.resId = R.drawable.jiudian;
		mItems.add(object9);

		Item object10 = new Item();
		object10.text = "#智能化住宅";
		object10.resId = R.drawable.zhineng;
		mItems.add(object10);

		Item object11 = new Item();
		object11.text = "#高层住宅";
		object11.resId = R.drawable.gaoceng;
		mItems.add(object11);

		Item object12 = new Item();
		object12.text = "#单身公寓";
		object12.resId = R.drawable.dansheng;
		mItems.add(object12);

		Item object13 = new Item();
		object13.text = "#外廊式住宅";
		object13.resId = R.drawable.wailang;
		mItems.add(object13);

		Item object14 = new Item();
		object14.text = "#退台式住宅";
		object14.resId = R.drawable.tuitai;
		mItems.add(object14);
		mContext = context;

		Item object15 = new Item();
		object15.text = "#跃廊式住宅";
		object15.resId = R.drawable.yuelang;
		mItems.add(object15);
		mContext = context;

		Item object16 = new Item();
		object16.text = "#公寓式住宅";
		object16.resId = R.drawable.gongyu;
		mItems.add(object16);
		mContext = context;

		Item object17 = new Item();
		object17.text = "#塔式住宅";
		object17.resId = R.drawable.ta;
		mItems.add(object17);
		mContext = context;

		Item object18 = new Item();
		object18.text = "#梯间式住宅";
		object18.resId = R.drawable.tijian;
		mItems.add(object18);
		mContext = context;

		Item object19 = new Item();
		object19.text = "#错层式住宅";
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
