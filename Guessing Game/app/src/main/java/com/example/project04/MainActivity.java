package com.example.project04;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Random;

import static android.widget.Toast.*;
import static java.lang.Thread.sleep;

public class MainActivity extends AppCompatActivity {

    private Button mButton1;
    private ListView mlistview1;
    private ListView mlistview2;
    private TextView mtextview1;
    private TextView mtextview2;
    private TextView player1_won;
    private TextView player2_won;

    public Handler handler2;
    public Handler handler1;

    player1 p11 = new player1();
    player2 p22= new player2();


    public int flag1 = 0, flag2 = 0;
    private Boolean gameOver = false;
    static int counter1 = 1, counter2 = 1;
    public int getcomb1=0,getcomb2=0;

    public int main_flag=0;

    public int flag_to_generate_comb1=0,flag_to_generate_comb2=0;


    ArrayList<Integer> actual_num_list_1 = new ArrayList<>();
    ArrayList<Integer> actual_num_list_2 = new ArrayList<>();
    ArrayList<Integer> combinations1 = new ArrayList<>();
    ArrayList<Integer> combinations2 = new ArrayList<>();

    ArrayList<Integer> ignore_num1 = new ArrayList<>();
    ArrayList<Integer> ignore_num2 = new ArrayList<>();

    @SuppressLint("HandlerLeak")
    private Handler mhandler = new Handler() {
        @SuppressLint("HandlerLeak")
        @Override
        public void handleMessage(@NonNull Message msg) {
            int what = msg.what;
            switch (what) {
                case SET_RESULT1:
                    String tempp = (String) msg.obj;
                    if(tempp.charAt(1)=='4'){
                        gameOver=true;
                        String win ="Player 2 guess - " + Integer.toString(msg.arg1) + "\n";
                        addItems(mlistview1, win);
                        player2_won.setText("Player 2 Won !!!!!!!");
                        Toast.makeText(MainActivity.this,"Player 2 won the game", LENGTH_LONG).show();

                       break;
                    }
                    else {
                        String temp_string1="";
                        if(tempp.charAt(0)=='4' ||
                                (Integer.parseInt(String.valueOf(tempp.charAt(0))) + Integer.parseInt(String.valueOf(tempp.charAt(1))))==4)
                        {
                            getcomb2=1;
                            if(flag_to_generate_comb2==0) {
                                getCombinations(Integer.toString(msg.arg1), "", 2);
                                flag_to_generate_comb2=1;
                            }
                             temp_string1 = "Player 2 guess - " + Integer.toString(msg.arg1) + "\n" +
                                    "Correct digit in correct position " + tempp.charAt(1) + "\n" +
                                    "Correct digit in wrong position " + tempp.charAt(0) + "\n" +
                                    "No wrong digits";
                        }
                        else {
                            temp_string1 = "Player 2 guess - " + Integer.toString(msg.arg1) + "\n" +
                                    "Correct digit in correct position " + tempp.charAt(1) + "\n" +
                                    "Correct digit in wrong position " + tempp.charAt(0) + "\n" +
                                    tempp.charAt(2) + " is a wrong digit";
                        }
                        addItems(mlistview1, temp_string1);
                        Message message1 = handler2.obtainMessage(RECEIVE_FEEDBACK);
                        message1.arg1 = msg.arg1;
                        handler2.sendMessage(message1);
                    }
                    break;

                case SET_RESULT2:
                    String temppp = (String) msg.obj;
                    if(temppp.charAt(1)=='4'){
                        gameOver=true;
                        String win ="Player 1 guess - " + Integer.toString(msg.arg1) + "\n";
                        addItems(mlistview2, win);
                        player1_won.setText("Player 1 Won !!!!!!!");
                        Toast.makeText(MainActivity.this,"Player 1 won the game", LENGTH_SHORT).show();
                        break;
                    }
                    else {
                        String temp_string2="";
                        if(temppp.charAt(0)=='4' ||
                                (Integer.parseInt(String.valueOf(temppp.charAt(0))) + Integer.parseInt(String.valueOf(temppp.charAt(1))))==4)
                        {
                            getcomb1=1;
                            if(flag_to_generate_comb1==0) {
                                getCombinations(Integer.toString(msg.arg1), "", 1);
                                flag_to_generate_comb1=1;
                            }
                            temp_string2 = "Player 1 guess - " + Integer.toString(msg.arg1) + "\n" +
                                    "Correct digit in correct position " + temppp.charAt(1) + "\n" +
                                    "Correct digit in wrong position " + temppp.charAt(0) + "\n" +
                                    "No wrong digits";
                        }
                        else {
                            temp_string2 = "Player 1 guess - " + Integer.toString(msg.arg1) + "\n" +
                                    "Correct digit in correct position " + temppp.charAt(1) + "\n" +
                                    "Correct digit in wrong position " + temppp.charAt(0) + "\n" +
                                    temppp.charAt(2) + " is a wrong digit";
                        }
                        addItems(mlistview2, temp_string2);

                        Message message2 = handler1.obtainMessage(RECEIVE_FEEDBACK);
                        message2.arg1 = msg.arg1;
                        handler1.sendMessage(message2);
                    }
                    break;
                case SET_NUMBER1:
                    mtextview1.setText("Player 1 number --> " + msg.arg1);
                    String s = Integer.toString(msg.arg1);
                    for (int i = 0; i < s.length(); i++) {
                        actual_num_list_1.add(Integer.parseInt(String.valueOf(s.charAt(i))));
                    }
                    break;
                case SET_NUMBER2:
                    mtextview2.setText("Player 2 number --> " + msg.arg1);
                    String temp = Integer.toString(msg.arg1);
                    for (int i = 0; i < temp.length(); i++) {
                        actual_num_list_2.add(Integer.parseInt(String.valueOf(temp.charAt(i))));
                    }
                    break;
                case HALTING:
                    Toast.makeText(MainActivity.this, "gameover .... exceeded 20 guesses", LENGTH_SHORT).show();
                    endGame();
                    break;
            }
        }
    };


    // "What" Values to be used by handleMessage()
    public static final int SET_RESULT1 = 0;
    public static final int SET_RESULT2 = 1;
    public static final int SET_NUMBER1 = 2;
    public static final int SET_NUMBER2 = 3;
    public static final int HALTING = 4;
    public static final int RECEIVE_GUESS = 5;
    public static final int RECEIVE_FEEDBACK = 6;


    ArrayList<String> listItems1 = new ArrayList<String>();
    ArrayList<String> listItems2 = new ArrayList<String>();

    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButton1 = (Button) findViewById(R.id.start);

        mlistview1 = findViewById(R.id.player1_list);
        mlistview2 = findViewById(R.id.player2_list);
        mtextview1 = findViewById(R.id.choosen1);
        mtextview2 = findViewById(R.id.choosen2);
        player1_won = findViewById(R.id.player1_won);
        player2_won = findViewById(R.id.player2_won);


        adapter1 = new ArrayAdapter<>(MainActivity.this, R.layout.listitem1, listItems1);
        mlistview1.setAdapter(adapter1);
        adapter2 = new ArrayAdapter<>(MainActivity.this, R.layout.listitem2, listItems2);
        mlistview2.setAdapter(adapter2);

        mButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(main_flag==0) {
                    mButton1.setText("Restart Game");

                    Thread p1 = new Thread(p11);
                    p1.start();

                    Thread p2 = new Thread(p22);
                    p2.start();
                    main_flag=1;
                }
                else{
                    endGame();

                }

            }
        });

    }

    public void addItems(View v, String s) {
        if (v == mlistview1) {
            listItems1.add(s);
            adapter1.notifyDataSetChanged();
        } else {
            listItems2.add(s);
            adapter2.notifyDataSetChanged();
        }
    }

    public class player1 implements Runnable {

        private int what;

        @Override
        @SuppressLint("HandlerLeak")
        public void run() {
            Looper.prepare();

            handler1 = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    what = msg.what;
                    switch (what) {
                        //when a guess is received
                        case RECEIVE_GUESS:
                            if (gameOver) {
                                break;
                            }
                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bundle bundle = compare_actual_guess(Integer.toString(msg.arg1), 0);
                            String wrong = Integer.toString(bundle.getInt("wrong"));
                            String correct = Integer.toString(bundle.getInt("correct"));
                            ;
                            String wrong_digit = Integer.toString(bundle.getInt("wrong_digit"));
                            String sending_res = wrong + correct + wrong_digit;

                            Message message = mhandler.obtainMessage(SET_RESULT1);
                            message.arg1 = msg.arg1;
                            message.obj = sending_res;

                            mhandler.sendMessage(message);
                            break;
                        case RECEIVE_FEEDBACK:
                            try {
                                sleep(3000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (gameOver) {
                                break;
                            }

                            //Log.i("feedback", "from thread1");
                            if (counter1 < 20) {
                                int ran;
                                if(getcomb1==1){

                                    int k=new Random().nextInt(combinations1.size());
                                    ran=combinations1.get(k);
                                    combinations1.remove(k);
                                }
                                else {
                                    ran = getOptimalRandomNumber(1);

                                }
                                Message msg1 = handler2.obtainMessage(RECEIVE_GUESS);
                                msg1.arg1 = (ran);
                                handler2.sendMessage(msg1);
                                counter1++;
                            } else {
                                gameOver = true;
                                Message halt = mhandler.obtainMessage(HALTING);
                                mhandler.sendMessage(halt);
                            }
                    }
                }
            };
            //setting the initial guess of player 1
            if (flag1 == 0) {
                Message message = mhandler.obtainMessage(SET_NUMBER1);
                message.arg1 = getRandomNumber();
                mhandler.sendMessage(message);
                flag1 = 1;
            }
            try {
                sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Message initial_guess = handler2.obtainMessage(RECEIVE_GUESS);
            initial_guess.arg1 = getRandomNumber();
            handler2.sendMessage(initial_guess);

            Looper.loop();
        }
    }

    public class player2 implements Runnable {
        private int what;

        @Override
        @SuppressLint("HandlerLeak")
        public void run() {
            Looper.prepare();

            handler2 = new Handler() {
                @Override
                public void handleMessage(@NonNull Message msg) {
                    what = msg.what;

                    switch (what) {
                        //when a guess is received
                        case RECEIVE_GUESS:
                            if (gameOver) {
                                break;
                            }
                            try {
                                sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            Bundle bundle = compare_actual_guess(Integer.toString(msg.arg1), 1);
                            String wrong = Integer.toString(bundle.getInt("wrong"));
                            String correct = Integer.toString(bundle.getInt("correct"));
                            ;
                            String wrong_digit = Integer.toString(bundle.getInt("wrong_digit"));
                            String sending_res = wrong + correct + wrong_digit;
                            Message message = mhandler.obtainMessage(SET_RESULT2);
                            message.arg1 = msg.arg1;
                            message.obj = sending_res;
                            mhandler.sendMessage(message);
                            break;
                        case RECEIVE_FEEDBACK:
                            try {
                                sleep(2000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            if (gameOver) {
                                break;
                            }

                            //Log.i("feedback", "from thread2");
                            if (counter2 < 20) {
                                int ran;
                                if(getcomb2==1){

                                    int k=new Random().nextInt(combinations2.size());
                                    ran=combinations2.get(k);
                                    combinations2.remove(k);
                                }
                                else {
                                     ran = getOptimalRandomNumber(2);

                                }Message msg2 = handler1.obtainMessage(RECEIVE_GUESS);
                                msg2.arg1 = (ran);
                                handler1.sendMessage(msg2);
                                counter2++;
                            } else {
                                gameOver = true;
                                Message halt = mhandler.obtainMessage(HALTING);
                                mhandler.sendMessage(halt);
                            }
                    }
                }
            };
            //setting the initial guess of player 2
            if (flag2 == 0) {
                Message message = mhandler.obtainMessage(SET_NUMBER2);
                message.arg1 = getRandomNumber();
                mhandler.sendMessage(message);
                flag2 = 1;

            }
            try {
                sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            Message initial_guess = handler1.obtainMessage(RECEIVE_GUESS);
            initial_guess.arg1 = getRandomNumber();
            handler1.sendMessage(initial_guess);
            Looper.loop();

        }
    }

    public int getRandomNumber() {
        ArrayList<Integer> generated_number = new ArrayList<>();
        String s = "";
        do {
            int x = new Random().nextInt(10);
            if (x == 0 && generated_number.size() == 0) {
                continue;
            } else {
                if (!generated_number.contains(x)) {
                    generated_number.add(x);
                    s = s + (x);

                }
            }
        } while (generated_number.size() < 4);
        return Integer.parseInt(s);
    }

    public Bundle compare_actual_guess(String s, int flag) {
        if(s.length()==3){
            s="0"+s;

        }
        ArrayList<Integer> actual_num_list = new ArrayList<>();
        ArrayList<Integer> wrong_digits = new ArrayList<>();
        ArrayList<Integer> guessed_num_list = new ArrayList<>();
        int wrong = 0;
        int correct = 0;
        int wrong_digit;
        // Get actual number from player 1
        if (flag == 0) {
            actual_num_list = actual_num_list_1;
        }
        // Get actual number from player 2
        else if (flag == 1) {
            actual_num_list = actual_num_list_2;
        }
        for (int i = 0; i < s.length(); i++) {
            guessed_num_list.add(Integer.parseInt(String.valueOf(s.charAt(i))));
        }

        for (int i = 0; i < 4; i++) {
            if (guessed_num_list.get(i) == actual_num_list.get(i)) {
                correct++;
            } else if (actual_num_list.contains(guessed_num_list.get(i))) {
                wrong++;
            } else {
                wrong_digits.add(guessed_num_list.get(i));
                if (flag == 0) {
                    ignore_num2.add(guessed_num_list.get(i));
                } else {
                    ignore_num1.add(guessed_num_list.get(i));
                }
            }
        }
        if (wrong_digits.size() > 0) {
            wrong_digit = wrong_digits.get(new Random().nextInt(wrong_digits.size()));
        } else {
            wrong_digit = -1;
        }

        Bundle bundle = new Bundle();
        bundle.putInt("wrong", wrong);
        bundle.putInt("correct", correct);
        bundle.putInt("wrong_digit", wrong_digit);
        return bundle;
    }

    private void endGame() {

        handler1.removeCallbacks(p11);
        handler2.removeCallbacks(p22);
        handler1.getLooper().quit();
        handler2.getLooper().quit();
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        mtextview1.setText("");
        mtextview2.setText("");
        player1_won.setText("");
        player2_won.setText("");
        listItems1.removeAll(listItems1);
        listItems2.removeAll(listItems2);
        actual_num_list_1.removeAll(actual_num_list_1);
        actual_num_list_2.removeAll(actual_num_list_2);
        combinations1.removeAll(combinations1);
        combinations2.removeAll(combinations2);
        ignore_num1.removeAll(ignore_num1);
        ignore_num2.removeAll(ignore_num2);
        adapter1.notifyDataSetChanged();
        adapter2.notifyDataSetChanged();
        flag1 = 0;
        flag2 = 0;
        flag_to_generate_comb1 = 0;
        flag_to_generate_comb2 = 0;
        counter1 = 1;
        counter2 = 1;
        getcomb1=0;
        getcomb2=0;
        gameOver = false;
        p11=new player1();
        p22= new player2();

        Thread p1 = new Thread(p11);
        p1.start();

        Thread p2 = new Thread(p22);
        p2.start();

    }

    public int getOptimalRandomNumber(int flag) {
        ArrayList<Integer> generated_number = new ArrayList<>();
        String s = "";
        do {
            int x = new Random().nextInt(10);
            if (x == 0 && generated_number.size() == 0) {
                continue;
            } else {
                if (flag==1) {
                    if (!generated_number.contains(x) && !ignore_num1.contains(x)) {
                        generated_number.add(x);
                        s = s + (x);
                    }
                }
                else if (flag==2){
                    if (!generated_number.contains(x) && !ignore_num2.contains(x)) {
                        generated_number.add(x);
                        s = s + (x);
                    }
                }
            }
        } while (generated_number.size() < 4);
        return Integer.parseInt(s);
    }

    public void getCombinations(String str, String ans,int flag)
    {
        if(str.length()==0){
            if(flag==1) {
                combinations1.add(Integer.parseInt(ans));
            }
            else if(flag ==2){
                combinations2.add(Integer.parseInt(ans));
            }
            return;
        }

        for (int i = 0; i < str.length(); i++) {

            // ith character of str
            char ch = str.charAt(i);

            // Rest of the string after excluding
            // the ith character
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            // Recurvise call
            getCombinations(ros, ans + ch, flag);
        }
    }





}