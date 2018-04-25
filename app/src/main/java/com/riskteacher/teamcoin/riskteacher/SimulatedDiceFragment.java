package com.riskteacher.teamcoin.riskteacher;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.RoundingMode;
import java.util.Timer;
import java.util.TimerTask;


public class SimulatedDiceFragment extends Fragment {

    Handler handler = new Handler();
    int kTimer=1;

    Runnable runnable = new Runnable() {
        public void run() {
            afficher();
        }
    };
    BigDecimal initSize;

    public SimulatedDiceFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_simulated_dice, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SharedPreferences spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
        SharedPreferences.Editor spe = spf.edit();
        spe.putBoolean("auto",false);
        spe.putInt("timerConf", 2);
        spe.commit();
        String user = spf.getString("user","");
        String balance = spf.getString("balance","");
        TextView tv1 = getActivity().findViewById(R.id.tvNBalance);
        tv1.setText(balance);
        TextView tv2 =  getActivity().findViewById(R.id.tvname);
        tv2.setText(user+"  / ");

        Button buyBtn = getActivity().findViewById(R.id.buttonBuy);
        Button sellBtn = getActivity().findViewById(R.id.buttonSell);
        Button autoBtn = getActivity().findViewById(R.id.buttonAuto);

        buyBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                opBuy();
            }
        });
        sellBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                opSell();
            }
        });
        autoBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                opAuto();
            }
        });
    }

    public void logOut(View view) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Exit Confirmation");
        builder.setMessage("Do you want to Exit");
        AlertDialog.OnClickListener listener = new AlertDialog.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(which==dialog.BUTTON_POSITIVE){
                    dialog.dismiss(); // dismiss the dialog
                    getActivity().finish(); // to destroy the activity
                }
                else if(which==dialog.BUTTON_NEGATIVE){
                    dialog.dismiss(); // dismiss the dialog, but activity is still alive
                }
            }
        };
        builder.setPositiveButton("Yes",listener);
        builder.setNegativeButton("No",listener);
        builder.show();
    }

    public void opBuy() {
        BigDecimal BetSize = getBetSize();
        BigDecimal Balance = getBalance();
        BigDecimal newBal = new BigDecimal(0);
        if(validateBetSize()) {
            int N1 = genRandom();
            int N2 = 52;
            TextView tv3 = (TextView) getActivity().findViewById(R.id.tvTCtry);
            tv3.setText("Last Try: " + String.valueOf(N1));
            if (N1 > N2) {
                TextView tv4 = (TextView) getActivity().findViewById(R.id.tvTCresult);
                tv4.setText("You Win");
                tv4.setTextColor(getResources().getColor(R.color.colorWin));
                tv3.setTextColor(getResources().getColor(R.color.colorWin));
                TextView tv1 = (TextView) getActivity().findViewById(R.id.tvNBalance);
                newBal = Balance.add(BetSize);
                tv1.setText(newBal.toString());
                SharedPreferences spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = spf.edit();
                spe.putString("balance",newBal.toString());
                spe.putInt("lastop",1);
                spe.commit();
                String showinput = "1: "+"BUY Win "+" Target>52 Roll="+N1+" profit:"+BetSize.toString();
                rotateOps(showinput);
            } else {
                TextView tv4 = (TextView) getActivity().findViewById(R.id.tvTCresult);
                tv4.setText("You Lose");
                tv4.setTextColor(getResources().getColor(R.color.colorLose));
                tv3.setTextColor(getResources().getColor(R.color.colorLose));
                TextView tv1 = (TextView) getActivity().findViewById(R.id.tvNBalance);
                newBal = Balance.subtract(BetSize);
                tv1.setText(newBal.toString());
                SharedPreferences spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = spf.edit();
                spe.putString("balance",newBal.toString());
                spe.putInt("lastop",-1);
                spe.commit();
                String showinput = "1: "+"BUY Lost "+" Target>52 Roll="+N1+" profit:-"+BetSize.toString();
                rotateOps(showinput);
            }
        }else{
            Toast.makeText(getContext(), "Please insert a valid size number", Toast.LENGTH_LONG).show();
        }
    }

    public void opSell() {
        BigDecimal BetSize = getBetSize();
        BigDecimal Balance = getBalance();
        BigDecimal newBal = new BigDecimal(0);
        if(validateBetSize()) {
            int N1 = genRandom();
            int N2 = 48;
            TextView tv3 = (TextView) getActivity().findViewById(R.id.tvTCtry);
            tv3.setText("Last Try: " + String.valueOf(N1));
            if (N1 < N2) {
                TextView tv4 = (TextView) getActivity().findViewById(R.id.tvTCresult);
                tv4.setText("You Win");
                tv4.setTextColor(getResources().getColor(R.color.colorWin));
                tv3.setTextColor(getResources().getColor(R.color.colorWin));
                TextView tv1 = (TextView) getActivity().findViewById(R.id.tvNBalance);
                newBal = Balance.add(BetSize);
                tv1.setText(newBal.toString());
                SharedPreferences spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = spf.edit();
                spe.putString("balance",newBal.toString());
                spe.putInt("lastop",1);
                spe.commit();
                String showinput = "1: "+"SELL Win "+" Target<48 Roll="+N1+" profit:"+BetSize.toString();
                rotateOps(showinput);
            } else {
                TextView tv4 = (TextView) getActivity().findViewById(R.id.tvTCresult);
                tv4.setText("You Lose");
                tv4.setTextColor(getResources().getColor(R.color.colorLose));
                tv3.setTextColor(getResources().getColor(R.color.colorLose));
                TextView tv1 = (TextView) getActivity().findViewById(R.id.tvNBalance);
                newBal = Balance.subtract(BetSize);
                tv1.setText(newBal.toString());
                SharedPreferences spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
                SharedPreferences.Editor spe = spf.edit();
                spe.putString("balance",newBal.toString());
                spe.putInt("lastop",-1);
                spe.commit();
                String showinput = "1: "+"SELL Lost "+" Target<48 Roll="+N1+" profit:-"+BetSize.toString();
                rotateOps(showinput);
            }
        }else{
            Toast.makeText(getContext(), "Please insert a valid size number", Toast.LENGTH_LONG).show();
        }
    }

    public void opAuto() {
        SharedPreferences spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
        boolean autorunning = spf.getBoolean("auto",false);
        kTimer = spf.getInt("timerConf", 1);
        if(autorunning){
            SharedPreferences.Editor spe = spf.edit();
            spe.putBoolean("auto",false);
            spe.commit();
            Button bt1 = getActivity().findViewById(R.id.buttonAuto);
            bt1.setText("AUTO");
            resetBetSize();
        }else{
            SharedPreferences.Editor spe = spf.edit();
            spe.putBoolean("auto",true);
            spe.commit();
            Button bt1 = getActivity().findViewById(R.id.buttonAuto);
            bt1.setText("STOP");
            setInitSize(getBetSize());
            runnable.run();
        }
    }

    private BigDecimal getBetSize(){
        BigDecimal ans;
        ans = new BigDecimal(0);
        EditText betsize = (EditText) getActivity().findViewById(R.id.etsize);
        String ssize = betsize.getText().toString();
        if(ssize.length()>0){
            if(isNumeric(ssize)){
                ans = new BigDecimal(ssize);
            }else{
                Toast.makeText(getContext(), "Please insert a valid size number", Toast.LENGTH_LONG).show();
            }
        }else {
            Toast.makeText(getContext(), "Please insert a valid size number", Toast.LENGTH_LONG).show();
        }
        return ans;
    }

    private BigDecimal getBalance(){
        TextView tv1 = (TextView) getActivity().findViewById(R.id.tvNBalance);
        BigDecimal ans = new BigDecimal(tv1.getText().toString());
        return ans;
    }

    private boolean validateBetSize(){
        boolean ans = false;
        BigDecimal vmin = new BigDecimal(0);
        BigDecimal currentSize = getBetSize();
        BigDecimal vmax = getBalance();
        if(currentSize.compareTo(vmin)>0){
            if(currentSize.compareTo(vmax)<=0){
                ans=true;
            }
        }
        return ans;
    }

    private boolean isNumeric(String cadena){
        try {
            Double.parseDouble(cadena);
            return true;
        } catch (NumberFormatException nfe){
            return false;
        }
    }

    private void rotateOps(String lineinput){
        TextView tv4 = (TextView) getActivity().findViewById(R.id.tvop4);
        TextView tv3 = (TextView) getActivity().findViewById(R.id.tvop3);
        TextView tv2 = (TextView) getActivity().findViewById(R.id.tvop2);
        TextView tv1 = (TextView) getActivity().findViewById(R.id.tvop1);
        tv4.setText(tv3.getText().toString().replace("3: ","4: "));
        tv3.setText(tv2.getText().toString().replace("2: ","3: "));
        tv2.setText(tv1.getText().toString().replace("1: ","2: "));
        tv1.setText(lineinput);
    }

    public int genRandom(){
        return ((int) (Math.random() * 99) + 1);
    }

    public void afficher()
    {
        SharedPreferences spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
        boolean autorunning = spf.getBoolean("auto",false);
        int TimerDelay = 1000*kTimer;
        if(autorunning) {

            BigDecimal tempval = getBetSize();
            EditText betsize = (EditText) getActivity().findViewById(R.id.etsize);
            spf = getActivity().getSharedPreferences("RiskTeacher", Context.MODE_PRIVATE);
            int pastop = spf.getInt("lastop", 1);
            if (pastop < 0) {
                //TextView txtClicks = (TextView) findViewById(R.id.txtClicks);
                // task to be done every 1000 milliseconds
                betsize.setText(tempval.multiply(new BigDecimal(3)).setScale(2, RoundingMode.HALF_UP).toString());
            } else if (pastop > 0) {
                resetBetSize();
            }
            opBuy();
            handler.postDelayed(runnable, TimerDelay);
        }
    }

    public void setInitSize(BigDecimal x){
        initSize = x;
    }

    public void resetBetSize(){
        EditText betsize = (EditText) getActivity().findViewById(R.id.etsize);
        betsize.setText(getInitSize().toString());
    }

    public BigDecimal getInitSize(){
        return initSize;
    }

}
