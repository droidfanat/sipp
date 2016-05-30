package org.silena.main.adapter;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.silena.R;
import org.silena.main.model.Contact;

import java.util.List;


public final class ContactsAdapter extends GrandAdapter<Contact> {





    public ContactsAdapter(Context mainActivity, List<Contact> list) {
        super(mainActivity, list, R.layout.item_contact);
    }

    public void filter(String text) {

        workList.clear();
        for (Contact contact : mList){
            Log.e(contact.getName().toLowerCase(), text.toLowerCase());
            if(contact.getName().toLowerCase().contains(text.toLowerCase())){
                workList.add(contact);
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
            holder.tvPhoneType = (TextView) view.findViewById(R.id.tvPhoneType);
            holder.ivAvatar = (ImageView) view.findViewById(R.id.ivAvatar);
            view.setTag(holder);
        }

        holder = (ViewHolder) view.getTag();
        holder.tvName.setText(getItem(position).getName());
        holder.tvPhone.setText(getItem(position).getPhone());
        holder.ivAvatar.setImageBitmap(null);
        if(getItem(position).getPhotoUri() != null){
            Picasso.with(getContext()).load(getItem(position).getPhotoUri()).into(holder.ivAvatar);
        } else {
            holder.ivAvatar.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.avatar));
        }

        return view;
    }

    static class ViewHolder {
        TextView tvName;
        TextView tvPhone;
        TextView tvPhoneType;
        ImageView ivAvatar;
    }
}
