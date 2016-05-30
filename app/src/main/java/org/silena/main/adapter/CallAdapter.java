package org.silena.main.adapter;

import android.content.Context;
import android.provider.CallLog;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.silena.R;
import org.silena.main.model.Call;

import java.util.Date;
import java.util.List;


public class CallAdapter extends GrandAdapter<Call> {





    public CallAdapter(Context mainActivity, List<Call> list) {
        super(mainActivity, list, R.layout.item_call);
    }

    public void filter(String text) {

        workList.clear();
        for (Call call : mList){
            Log.e(call.getName().toLowerCase(), text.toLowerCase());
            if(call.getName().toLowerCase().contains(text.toLowerCase())){
                workList.add(call);
            }
        }
        notifyDataSetChanged();

    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        ViewHolder holder = new ViewHolder();

        if (view == null || convertView.getTag() == null) {
            view = getLayout();
            holder.tvName = (TextView) view.findViewById(R.id.tvName);
            holder.tvPhone = (TextView) view.findViewById(R.id.tvPhone);
            holder.tvDate = (TextView) view.findViewById(R.id.tvDate);
            holder.ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
            view.setTag(holder);
        }

        holder = (ViewHolder) view.getTag();

        String name = getItem(position).getName();

        if(name!= null){
            holder.tvName.setText(name);
            holder.tvPhone.setText(getItem(position).getPhone());
        } else {
            holder.tvName.setText("Unknown");
            holder.tvPhone.setText(getItem(position).getPhone());
        }


        String date = printDifference(getItem(position).getDate(), new Date());
        holder.tvDate.setText(date);
        holder.ivAvatar.setImageBitmap(null);

        int callType = getItem(position).getType();
        switch (callType) {
            case CallLog.Calls.OUTGOING_TYPE:
                Picasso.with(getContext()).load(R.drawable.outgoing).into(holder.ivAvatar);
                break;

            case CallLog.Calls.INCOMING_TYPE:
                Picasso.with(getContext()).load(R.drawable.incoming).into(holder.ivAvatar);
                break;

            case CallLog.Calls.MISSED_TYPE:
                Picasso.with(getContext()).load(R.drawable.missed).into(holder.ivAvatar);
                break;
        }

        return view;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvDate;
        ImageView ivAvatar;
    }

    public String printDifference(Date startDate, Date endDate){
        String response = "";
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : "+ endDate);
        System.out.println("different : " + different);

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        if(elapsedDays >= 1){
            response = elapsedDays + " day(s) ago";
        } else if (elapsedHours >= 1) {
            response = elapsedHours + " hour(s) ago";
        } else if (elapsedMinutes >= 1) {
            response = elapsedMinutes + " minute(s) ago";
        } else if (elapsedSeconds >= 1) {
            response = elapsedSeconds + " second(s) ago";
        }



        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays,
                elapsedHours, elapsedMinutes, elapsedSeconds);

        return response;
    }

}