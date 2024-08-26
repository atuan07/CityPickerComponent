package com.atuan.citypicker.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.atuan.citypicker.R;
import com.atuan.citypicker.model.CityListBean;
import com.atuan.citypicker.model.LocateState;
import com.atuan.citypicker.utils.PinyinUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CityListAdapter extends BaseAdapter {
    private static final int VIEW_TYPE_COUNT = 2;

    private Context mContext;
    private LayoutInflater inflater;
    private List<CityListBean> mCities;
    private HashMap<String, Integer> letterIndexes;
    private String[] sections;
    private OnCityClickListener onCityClickListener;
    private int locateState = LocateState.LOCATING;
    private String locatedCity;

    public CityListAdapter(Context mContext, List<CityListBean> mCities) {
        this.mContext = mContext;
        this.mCities = mCities;
        this.inflater = LayoutInflater.from(mContext);
    }

    public void setData(List<CityListBean> mCities) {

        if (mCities == null) {
            mCities = new ArrayList<>();
        }
        CityListBean locationCity = new CityListBean();
        locationCity.setPinyin("0");
        locationCity.setName("定位");
        mCities.add(0, locationCity);
        int size = mCities.size();
        this.mCities = mCities;
        letterIndexes = new HashMap<>();
        sections = new String[size];
        for (int index = 0; index < size; index++) {
            //当前城市拼音首字母
            String currentLetter = PinyinUtils.getFirstLetter(mCities.get(index).getPinyin());
            //上个首字母，如果不存在设为""
            String previousLetter = index >= 1 ? PinyinUtils.getFirstLetter(mCities.get(index - 1).getPinyin()) : "";
            if (!TextUtils.equals(currentLetter, previousLetter)) {
                letterIndexes.put(currentLetter, index);
                sections[index] = currentLetter;
            }
        }
    }

    /**
     * 更新定位状态
     *
     * @param state
     */
    public void updateLocateState(int state, String city) {
        this.locateState = state;
        this.locatedCity = city;
        notifyDataSetChanged();
    }

    /**
     * 获取字母索引的位置
     *
     * @param letter
     * @return
     */
    public int getLetterPosition(String letter) {
        Integer integer = letterIndexes.get(letter);
        return integer == null ? -1 : integer;
    }

    @Override
    public int getViewTypeCount() {
        return VIEW_TYPE_COUNT;
    }

    @Override
    public int getItemViewType(int position) {
        return position < VIEW_TYPE_COUNT - 1 ? position : VIEW_TYPE_COUNT - 1;
    }

    @Override
    public int getCount() {
        return mCities == null ? 0 : mCities.size();
    }

    @Override
    public CityListBean getItem(int position) {
        return mCities == null ? null : mCities.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {
        CityViewHolder holder;
        int viewType = getItemViewType(position);
        switch (viewType) {
            case 0://定位
                view = inflater.inflate(R.layout.cp_view_locate_city, parent, false);
                ViewGroup container = (ViewGroup) view.findViewById(R.id.layout_locate);
                TextView state = (TextView) view.findViewById(R.id.tv_located_city);
                switch (locateState) {
                    case LocateState.LOCATING:
                        state.setText(mContext.getString(R.string.cp_locating));
                        break;
                    case LocateState.FAILED:
                        state.setText(R.string.cp_located_failed);
                        break;
                    case LocateState.SUCCESS:
                        state.setText(locatedCity);
                        break;
                    default:
                        break;
                }
                break;
            case 1://所有
                if (view == null) {
                    view = inflater.inflate(R.layout.cp_item_city_listview, parent, false);
                    holder = new CityViewHolder();
                    holder.letter = (TextView) view.findViewById(R.id.tv_item_city_listview_letter);
                    holder.name = (TextView) view.findViewById(R.id.tv_item_city_listview_name);
                    holder.line = view.findViewById(R.id.view_line);
                    view.setTag(holder);
                } else {
                    holder = (CityViewHolder) view.getTag();
                }
                if (position >= 1) {
                    final String city = mCities.get(position).getName();
                    holder.name.setText(city);
                    String currentLetter = PinyinUtils.getFirstLetter(mCities.get(position).getPinyin());
                    String previousLetter = position >= 1 ? PinyinUtils.getFirstLetter(mCities.get(position - 1).getPinyin()) : "";
                    if (!TextUtils.equals(currentLetter, previousLetter)) {
                        holder.letter.setVisibility(View.VISIBLE);
                        holder.line.setVisibility(View.GONE);
                        holder.letter.setText(currentLetter);
                    } else {
                        holder.letter.setVisibility(View.GONE);
                        holder.line.setVisibility(View.VISIBLE);
                    }
                    holder.name.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (onCityClickListener != null) {
                                onCityClickListener.onCityClick(mCities.get(position));
                            }
                        }
                    });
                }
                break;
            default:
                break;
        }
        return view;
    }

    public static class CityViewHolder {
        TextView letter;
        TextView name;
        View line;
    }

    public void setOnCityClickListener(OnCityClickListener listener) {
        this.onCityClickListener = listener;
    }

    public interface OnCityClickListener {
        /**
         * 确定选中的是哪个城市
         *
         * @param cityListBean listview当前点击的城市msg
         * @createAuthor Lee
         */
        void onCityClick(CityListBean cityListBean);

        /**
         * 定位城市 状态都为LocateState.LOCATING，调不到这个方法
         *
         * @createAuthor Lee
         */
        void onLocateClick();
    }
}
