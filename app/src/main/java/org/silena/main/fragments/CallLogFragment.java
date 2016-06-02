package org.silena.main.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.silena.R;
import org.silena.main.MainActivity;
import org.silena.main.adapter.CallAdapter;
import org.silena.main.model.Call;

import java.util.LinkedList;


public class CallLogFragment extends Fragment {

    private View view;
    private MainActivity mainActivity;
    private ListView lvCallLog;
    private CallAdapter adapter;
    private LinkedList<Call> calls;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.fragment_call_log, null);
        mainActivity = (MainActivity) getActivity();
        lvCallLog = (ListView) view.findViewById(R.id.lvCallLog);

        waitCalls();

        lvCallLog.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mainActivity.showCallDialog(calls.get(position).getPhone());
            }
        });

        return view;
    }



    private void waitCalls(){
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mainActivity.getCalls() != null){
                    calls = mainActivity.getCalls();
                    adapter = new CallAdapter(mainActivity, calls);
                    lvCallLog.setAdapter(adapter);
                    view.findViewById(R.id.loadProgress).setVisibility(View.GONE);
                } else {
                    waitCalls();
                }
            }
        }, 500);
    }
}
