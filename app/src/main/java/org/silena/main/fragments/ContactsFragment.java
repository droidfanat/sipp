package org.silena.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import org.silena.R;
import org.silena.main.MainActivity;
import org.silena.main.adapter.ContactsAdapter;
import org.silena.main.model.Contact;

import java.util.LinkedList;

public class ContactsFragment extends Fragment {

    private View view;
    private ListView lvContacts;
    private MainActivity mainActivity;
    private EditText etSearch;
    private ContactsAdapter adapter;
    private LinkedList<Contact> contacts;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_contacts, null);

        lvContacts = (ListView) view.findViewById(R.id.lvContacts);
        mainActivity = (MainActivity) getActivity();
        etSearch = (EditText) view.findViewById(R.id.etSearch);
        lvContacts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.showCallDialog(contacts.get(position).getPhone());
            }
        });

        waitContacts();
        return view;
    }


    private void waitContacts(){
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mainActivity.getContacts() != null){
                    contacts = mainActivity.getContacts();
                    adapter = new ContactsAdapter(mainActivity, contacts);
                    lvContacts.setAdapter(adapter);
                    view.findViewById(R.id.loadProgress).setVisibility(View.GONE);
                    initSerach();
                } else {
                    waitContacts();
                }
            }
        }, 500);
    }

    private void initSerach(){
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) { }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                adapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) { }
        });
    }




}
