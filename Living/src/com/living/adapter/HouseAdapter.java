package com.living.adapter;

import java.util.List;
import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.living.bean.House;
import com.living.ui.R;
import com.living.util.ImageLoadOptions;
import com.living.util.ViewHolder;

/**
 * ∑ø‘¥µƒ  ≈‰∆˜
 * 
 * @author left
 * 
 */
@SuppressLint("InflateParams")
public class HouseAdapter extends BaseListAdapter<House> {

	public HouseAdapter(Context context, List<House> list) {
		super(context, list);
	}

	@Override
	public View bindView(int arg0, View convertView, ViewGroup arg2) {
		if (convertView == null) {
			convertView = mInflater.inflate(R.layout.house_item, null);
		}
		final House house = getList().get(arg0);
		ImageView piture = ViewHolder.get(convertView, R.id.photo);
		TextView name = ViewHolder.get(convertView, R.id.name);
		TextView detail = ViewHolder.get(convertView, R.id.detail);
		if (house.getPhoto() != null) {
			ImageLoader.getInstance().displayImage(
					"http://file.bmob.cn/" + house.getPhoto().getUrl(), piture,
					ImageLoadOptions.getOptions());
			name.setText(house.getName());
			detail.setText("#" + house.getCategory() + " | " + house.getPrice());
		} else {
			piture.setImageResource(R.drawable.default_house);
		}
		return convertView;
	}

}
