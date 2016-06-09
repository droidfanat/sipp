package org.silena.main.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.silena.R;
import org.silena.main.MainActivity;

public class DialerFragment extends Fragment implements View.OnClickListener {

    private View view;
    private EditText etNumber;
    private MainActivity mainActivity;
    private TextView Balance;
    private Button call_button;
    private Button callback_button;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_dealer, null);
        mainActivity = (MainActivity) getActivity();
        mainActivity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        Balance = (TextView) view.findViewById(R.id.des);
//        Balance.setText(mainActivity.getBalnceString());



        call_button = (Button) view.findViewById(R.id.call_button);
        call_button.setOnClickListener(this);

        callback_button = (Button) view.findViewById(R.id.callback_button);
        callback_button.setOnClickListener(this);


        etNumber = (EditText) view.findViewById(R.id.regphone);

        InputMethodManager inputMethodMgr = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodMgr.hideSoftInputFromWindow(etNumber.getWindowToken(), 0);

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        etNumber.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InputMethodManager inputmethodmgr= (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputmethodmgr.hideSoftInputFromWindow(etNumber.getWindowToken(), 0);
            }
        });


        //init Dial pad
        initNumberBtn(R.id.btn1, "1");
        initNumberBtn(R.id.btn2, "2");
        initNumberBtn(R.id.btn3, "3");
        initNumberBtn(R.id.btn4, "4");
        initNumberBtn(R.id.btn5, "5");
        initNumberBtn(R.id.btn6, "6");
        initNumberBtn(R.id.btn7, "7");
        initNumberBtn(R.id.btn8, "8");
        initNumberBtn(R.id.btn9, "9");
        initNumberBtn(R.id.btn0, "0");
        initNumberBtn(R.id.btnStar, "*");
        initNumberBtn(R.id.btnReshotka, "#");

        Button btnClear = (Button) view.findViewById(R.id.btnClear);
        btnClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNumber.setText(removeLastNumber(etNumber.getText().toString()));
                etNumber.setSelection(etNumber.getText().length());
            }
        });

        btnClear.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                etNumber.setText("");
                return false;
            }
        });
        waitBalance();
        return view;
    }

    public String removeLastNumber(String str) {
        if (str != null && str.length() > 0) {
            str = str.substring(0, str.length()-1);
        }
        return str;
    }

    private void initNumberBtn(int btnResource, final String addingNumber) {
        Button button = (Button) view.findViewById(btnResource);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                etNumber.setText(etNumber.getText().toString() + addingNumber);
                etNumber.setSelection(etNumber.getText().length());
            }
        });

        if(btnResource == R.id.btn0){
            button.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    etNumber.setText(etNumber.getText().toString() + "+");
                    etNumber.setSelection(etNumber.getText().length());
                    return false;
                }
            });
        }
    }

    private void waitBalance(){
        view.postDelayed(new Runnable() {
            @Override
            public void run() {
                if(mainActivity.getBalnceString() != null && !mainActivity.getBalnceString().equalsIgnoreCase("Balance: null")){
                    Balance.setText(mainActivity.getBalnceString());
                } else {
                    waitBalance();
                }
            }
        }, 500);
    }

    @Override
    public void onClick(View v) {
        String Phone;
        EditText Regphone;
        if (v.getId() == R.id.call_button) {
            Regphone = (EditText) view.findViewById(R.id.regphone);
            Phone = Regphone.getText().toString();
            mainActivity.call_menu(Phone);
        }
        if (v.getId() == R.id.callback_button) {
            Regphone = (EditText) view.findViewById(R.id.regphone);
            Phone = Regphone.getText().toString();
            mainActivity.Callback(Phone);
        }
    }
}
