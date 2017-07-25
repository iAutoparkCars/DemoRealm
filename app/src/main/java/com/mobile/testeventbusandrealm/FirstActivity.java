package com.mobile.testeventbusandrealm;

//problem is that I can't move onto the next activity when counting. Probably
//need another thread to do the actual counting, and then

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.realm.Realm;

public class FirstActivity extends AppCompatActivity {

    //start, cancel an Async task using Realm
    Button startRealmAsync;
    Button cancelRealmAsync;
    TestRealmAsync realmAsync;

    //start, interrupt a thread using Realm
    Button startRealmThread;
    Button interruptRealmThread;
    Thread realmThread;

    //call delete Realm file
    Button deleteRealmButton;
    Realm realm = null;

    /*isn't used, but what is SUPPOSED to be used with Thread.currentThread().isInterrupted() to return
    from a method involving Realm                   */
    static boolean interruptLoop = false;
    final String TAG = getClass().getName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //the normal
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_activity);
    }

    @Override
    public void onStart()
    {
        //sets up all the Buttons
        super.onStart();

        //setting the Buttons for Realm with AsyncTask
        initStartRealmAsync();
        initCancelRealmAsync();

        //setting the Buttons for Realm with Thread
        initStartRealmThread();
        initInterruptRealmThread();
    }

    private void initInterruptRealmThread() {
        interruptRealmThread = (Button) findViewById(R.id.interruptButton);
        interruptRealmThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                realmThread.interrupt();
                Toast.makeText(FirstActivity.this, "Stopped the for loop", Toast.LENGTH_SHORT);
            }
        });

        deleteRealmButton = (Button) findViewById(R.id.delete_realm);
        deleteRealmButton.setOnClickListener(new View.OnClickListener(){
             @Override
            public void onClick(View view) {
                 realm.close();
                 Realm.deleteRealm(Realm.getDefaultConfiguration());
             }
    });
    }

    private void initStartRealmThread() {
        startRealmThread = (Button) findViewById(R.id.startButtonThread);
        startRealmThread.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                realmThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try
                        {
                            realm.init(getApplicationContext());
                            realm = Realm.getDefaultInstance();
                            realm.beginTransaction();

                            //some nonsense operation while realm is open
                            for (int i = 0; i < 2500; i++) {

                                if (Thread.currentThread().isInterrupted())
                                {
                                    realm.close();
                                    Log.e(TAG, "Tried to interrupt Realm");
                                    break;
                                }

                                for (int j = -5000; j < 100000; j++)
                                {Integer res = fib(50);}

                                Log.d(TAG, "Subthread counter: " + i);
                            }
                        }

                        /*the catch block is unable to catch InterruptedException, therefore unable to close
                          the Realm when calling Thread.currentThread().interrupt(). Must use isInterrupted() instead. But large inconvenience*/
                        catch(Exception e) {Log.e(TAG, "Tried to interrupt Realm");}

                    }
                });
                realmThread.start();
            }
        });
    }

    private void initStartRealmAsync() {
        startRealmAsync = (Button) findViewById(R.id.start_realm_async);

        //start loop counter to get broken, or start thread to epen realm
        startRealmAsync.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v)
            {
               /* t1 = new Thread(new TestRealm());
                t1.start();*/

                realmAsync = new TestRealmAsync();
                realmAsync.execute();
                Toast.makeText(FirstActivity.this, "Started Realm in Async Task", Toast.LENGTH_SHORT);
            }
        });
    }

    private void initCancelRealmAsync() {
        cancelRealmAsync = (Button) findViewById(R.id.cancel_realm_button);
        //can also be the button to stop/interrupt/kill the thread
        cancelRealmAsync.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Intent move = new Intent(FirstActivity.this, SecondActivity.class);
                startActivity(move);*/

              /*  Log.d(TAG, "status of thread before interrupt: " + t1.getState().toString());
              t1.interrupt();
                Log.d(TAG, "status of thread after interrupt: " + t1.getState().toString());
                */

                Log.d(TAG, "status of thread before cancelling: " + realmAsync.getStatus().toString());
                realmAsync.cancel(true);
                Log.d(TAG, "status of thread after canceling: " + realmAsync.getStatus().toString());
            }
        });
    }


    //Class to open a Realm and do some nonsense operation. Try interrupting -- it won't work.
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

                //some nonsense operation while realm is open
                for (int i = 0; i < 2500; i++) {

                for (int j = -5000; j < 100000; j++)
                {Integer res = fib(50);}

                Log.d(TAG, "Subthread counter: " + i);
                }
            }
            catch (Exception e) {Log.e(TAG, "Realm cannot be caught with an InterruptedException");}
            return null;
        }

        @Override
        protected void onProgressUpdate(Void...params) { }  //do something on UI here

    }

    public Integer fib(Integer n)
    {
        /*if (n.equals(1))
            return 1;
        return fib(n-1) + fib(n-2);*/
        return 10313*2*3 *21;
    }

}


