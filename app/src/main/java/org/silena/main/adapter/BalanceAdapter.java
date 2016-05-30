package org.silena.main.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.silena.R;

import java.util.List;

/**
 * Created by дима on 25.05.2016.
 */
public class BalanceAdapter extends GrandAdapter<String> {





    public BalanceAdapter(Context mainActivity, List<String> list) {
        super(mainActivity, list, R.layout.item_balance);
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = new ViewHolder();

        if (view == null || convertView.getTag() == null) {
            view = getLayout();
            holder.tvBalance = (TextView) view.findViewById(R.id.tvBalance);
            view.setTag(holder);
        }

        holder = (ViewHolder) view.getTag();
        holder.tvBalance.setText(getItem(position));



        return view;
    }

    static class ViewHolder {
        TextView tvBalance;
    }

}