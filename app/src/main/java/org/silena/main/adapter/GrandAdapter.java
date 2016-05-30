package org.silena.main.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;

import java.util.LinkedList;
import java.util.List;

public abstract class GrandAdapter<T> extends BaseAdapter {
	
    private Context mContext;
    protected List<T> mList;
    protected List<T> workList;
    private int mLayout;
	
    public GrandAdapter(Context context, List<T> list, int layout) {
        mContext = context;
        mList = list;
        mLayout = layout;
        workList = new LinkedList<T>();
        workList.addAll(mList);
    }


	public int getCount() {
		if (workList!=null) {
			return workList.size();
		}
		return 0;
	}

	public T getItem(int position) {
	     return position >= 0 && position < workList.size() ? workList.get(position) : null;
	}
	
	public long getItemId(int position) {
		return position;
	}
	
    public View getLayout() {
        LayoutInflater inf = LayoutInflater.from(mContext);
        return inf.inflate(mLayout, null);
    }
    
    public Context getContext() {
    	return mContext;
    }

}