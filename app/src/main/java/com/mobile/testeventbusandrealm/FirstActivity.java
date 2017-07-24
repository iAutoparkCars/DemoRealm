package com.mobile.testeventbusandrealm;

//problem is that I can't move onto the next activity when counting. Probably
//need another thread to do the actual counting, and then

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.InterruptedIOException;

import io.realm.Realm;
import io.realm.RealmObject;

public class FirstActivity extends AppCompatActivity {

    TextView boxOne;
    Button nextButton;
    Button startCountButton;
    Realm realm = null;

    //Thread t1;

    TestRealmAsync realmThread;

    static Boolean interruptLoop = false;
    final String TAG = getClass().getName();

    //main thread starts a subthread; subthread has is counting in a loop gets broken

    //works when listening on MAIN
    //trying BACKGROUND -- it works
    //trying POSTING -- also works

    @Subscribe(threadMode = ThreadMode.POSTING)
    public void onEvent(InterruptSignal signal)
    {
        interruptLoop = true;
        Log.d(TAG, "Received signal to interrupt");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
        EventBus.getDefault().register(this);


    }


    @Override
    public void onStart()
    {
        super.onStart();

        nextButton = (Button) findViewById(R.id.next_button);

        //can also be the button to stop/interrupt/kill the thread
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent move = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(move);*/

              /*  Log.d(TAG, "status of thread before interrupt: " + t1.getState().toString());
              t1.interrupt();
                Log.d(TAG, "status of thread after interrupt: " + t1.getState().toString());
                */

                Log.d(TAG, "status of thread before cancelling: " + realmThread.getStatus().toString());
                realmThread.cancel(true);
                Log.d(TAG, "status of thread after canceling: " + realmThread.getStatus().toString());


            }
        });

        startCountButton = (Button) findViewById(R.id.count_button);

        //start loop counter to get broken, or start thread to epen realm
       startCountButton.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View v)
           {
              /* t1 = new Thread(new TestRealm());
               t1.start();*/
              realmThread = new TestRealmAsync();
               realmThread.execute();

           }



           /*@Override
           public void onClick(View v) {
               new Thread(new Runnable() {
                   @Override
                   public void run()
                   {
                       for (int i = 0; i <= 400; i++)
                       {
                           //boxOne = (TextView) findViewById(R.id.display_text);

                           if (interruptLoop) {
                               //Toast.makeText(FirstActivity.this, "Stopped the for loop", Toast.LENGTH_SHORT);
                               Log.d(TAG, "broke the loop");
                               return;
                           }

                           Log.d(TAG, String.valueOf(i));

                           try {Thread.sleep(50);}
                           catch (InterruptedException e) {e.printStackTrace();}

                           //boxOne = (TextView) findViewById(R.id.display_text);
                           //boxOne.append(String.valueOf(i) + " ");
                       }

                   }
               }).start();
           }*/
       });

         //running on the UI thread; the 2nd activity is unable to break this loop...WHY?
        /*startCountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runOnUiThread (new Runnable() {
                    @Override
                    public void run()
                    {
                        Intent move = new Intent(FirstActivity.this, SecondActivity.class);
                        startActivity(move);

                        try {Thread.sleep(1000);}
                        catch (InterruptedException e) {e.printStackTrace();}

                        for (int i = 0; i <= 400; i++)
                        {
                            //boxOne = (TextView) findViewById(R.id.display_text);

                            if (interruptLoop) {
                                //Toast.makeText(FirstActivity.this, "Stopped the for loop", Toast.LENGTH_SHORT);
                                Log.d(TAG, "broke the loop");
                                return;
                            }

                            Log.d(TAG, String.valueOf(i));

                            try {Thread.sleep(50);}
                            catch (InterruptedException e) {e.printStackTrace();}

                           *//*boxOne = (TextView) findViewById(R.id.display_text);
                           boxOne.append(String.valueOf(i) + " ");*//*
                        }

                    }
                });
            }
        });*/



    }//end onStart






    public void displayText()
    {

        for (int i = 0; i <= 200; i++)
        {
            //boxOne = (TextView) findViewById(R.id.display_text);

            if (interruptLoop) {
                Toast.makeText(FirstActivity.this, "Stopped the for loop", Toast.LENGTH_SHORT);
                return;
            }

            Log.d(TAG, String.valueOf(i));

            try {Thread.sleep(100);}
            catch (InterruptedException e) {e.printStackTrace();}

            boxOne = (TextView) findViewById(R.id.display_text);
            boxOne.append(String.valueOf(i) + " ");

            //String str = String.valueOf(i);
            //boxOne.setText(boxOne.getText() + " " + str);

          /*  if (interruptLoop) {
                Toast.makeText(FirstActivity.this, "Stopped the for loop", Toast.LENGTH_SHORT);
                return;
            }

            boxOne.setText(i);

            try {Thread.sleep(500);}
            catch (InterruptedException e) {e.printStackTrace();}*/
        }

    }

    class TestRealmAsync extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected Void doInBackground(Void... params)
        {
            realm = null;
            try
            {
                realm.init(getApplicationContext());
                realm = Realm.getDefaultInstance();

                realm.beginTransaction();

                for (int i = 0; i < 2800; i++) {

                    for (int j = -5000; j < 100000; j++)
                    {
                        Integer res = fib(50);
                    }
                    Log.d(TAG, "Subthread counter: " + i);


            }
        }
            finally {
            if (realm!= null) {
                realm.close();
                Log.e(TAG, "Realm was closed from finally block");
                }
            }

            return null;
        }
    }

    /*class TestRealm implements Runnable
    {
        public void run()
        {
            realm = null;
            try
            {
                realm.init(getApplicationContext());
                realm = Realm.getDefaultInstance();

                realm.beginTransaction();

                for (int i = 0; i < 2800; i++) {

                    for (int j = -5000; j < 100000; j++)
                    {
                        Integer res = fib(50);
                    }
                    Log.d(TAG, "Subthread counter: " + i);


                    *//*try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Log.d(TAG, "Interrupted from thread");
                    }*//*

                }
            }
            finally
            {
                if (realm!= null) {
                    realm.close();
                    Log.e(TAG, "Realm was closed from finally block");
                }
            }

            realm = null;

        }

    }*/

    public Integer fib(Integer n)
    {
        /*if (n.equals(1))
            return 1;
        return fib(n-1) + fib(n-2);*/
        return 10313*2*3 *21;
    }





}


