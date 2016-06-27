package com.qysports.funfootball.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.boredream.bdcodehelper.entity.city.CityModel;
import com.boredream.bdcodehelper.view.PinnedSectionListView;
import com.qysports.funfootball.R;

import java.util.ArrayList;
import java.util.List;

public class CityLetterAdapter extends BaseAdapter implements
		PinnedSectionListView.PinnedSectionListAdapter {

	public static class LetterGroupCity {
		public String firstLetter;
		public ArrayList<CityModel> groupCity;

		public LetterGroupCity(String firstLetter) {
			this.firstLetter = firstLetter;
		}

		@Override
		public boolean equals(Object o) {
			if(o instanceof LetterGroupCity) {
				return firstLetter.equals(((LetterGroupCity)o).firstLetter);
			}
			return super.equals(o);
		}
	}

	private static final int VIEW_TYPE_LETTER = 0;
	private static final int VIEW_TYPE_CONTENT = 1;

	private Activity context;
	private List<CityModel> datas = new ArrayList<>();

	private ArrayList<LetterGroupCity> letterGroupCityList = new ArrayList<>();
	private ArrayList<String> letterList = new ArrayList<>();
	private boolean isSingleChoose;

	public void setSingleChoose(boolean singleChoose) {
		isSingleChoose = singleChoose;
	}

	public ArrayList<LetterGroupCity> getLetterGroupCityList() {
		return letterGroupCityList;
	}

	public ArrayList<String> getLetterList() {
		return letterList;
	}

	public CityLetterAdapter(Activity context, List<CityModel> datas) {
		this.context = context;
		this.datas = datas;
		initList();
	}

	private void initList() {
		for (int i = 0; i < datas.size(); i++) {
			CityModel city = datas.get(i);
			String firstLetter = city.letter.substring(0, 1);

			LetterGroupCity lgc = new LetterGroupCity(firstLetter);

			int index = letterGroupCityList.indexOf(lgc);
			if(index == -1) {
				ArrayList<CityModel> letterCityList = new ArrayList<>();
				letterCityList.add(city);

				lgc.groupCity = letterCityList;
				letterGroupCityList.add(lgc);
				letterList.add(firstLetter);
			} else {
				lgc = letterGroupCityList.get(index);
				lgc.groupCity.add(city);
			}
		}
	}

	@Override
	public int getViewTypeCount() {
		return 2;
	}
	
	@Override
	public int getItemViewType(int position) {
		return position % 2 == 1 ? VIEW_TYPE_CONTENT : VIEW_TYPE_LETTER;
	}
	
	@Override
	public int getCount() {
		return letterGroupCityList.size() * 2;
	}

	@Override
	public Object getItem(int position) {
		int itemViewType = getItemViewType(position);
		if( itemViewType == VIEW_TYPE_LETTER) {
			String letter = letterGroupCityList.get(position / 2).firstLetter;
			return letter;
		} else {
			ArrayList<CityModel> groupCity = letterGroupCityList.get((position - 1) / 2).groupCity;
			return groupCity;
		}
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup group) {
		int itemViewType = getItemViewType(position);
		return itemViewType == VIEW_TYPE_LETTER ? getLetterView(position,
				convertView) : getContentView(position, convertView);
	}

	private View getLetterView(int position, View convertView) {
		ViewHolder holder = null;
		if (convertView == null || holder == null) {
			convertView = View.inflate(context, R.layout.item_city_header, null);
			holder = new ViewHolder();
			holder.tv_firstletter = (TextView) convertView
					.findViewById(R.id.tv_firstletter);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		String firstLetter = (String) getItem(position);
		holder.tv_firstletter.setText(firstLetter);

		return convertView;
	}

	private View getContentView(int position, View convertView) {
		ViewHolder holder = null;
		if (convertView == null || holder == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_city, null);
			holder.gv_letter_city = (GridView) convertView
					.findViewById(R.id.gv_letter_city);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		// set data
		final ArrayList<CityModel> groupCities = (ArrayList<CityModel>) getItem(position);
		CityGridAdapter adapter = new CityGridAdapter(context, groupCities);
		adapter.setSingleChoose(isSingleChoose);
		holder.gv_letter_city.setAdapter(adapter);

		return convertView;
	}

	@Override
	public boolean isItemViewTypePinned(int viewType) {
		return viewType == VIEW_TYPE_LETTER;
	}
	
	public static class ViewHolder {
		public TextView tv_firstletter;
		public GridView gv_letter_city;
	}

}
