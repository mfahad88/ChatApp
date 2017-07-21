package com.example.bipl.chatapp;

import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    Button btn;
    EditText editText;
    ListView listView;
    Socket s;
    ServerSocket ss;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.listView);
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        new Thread(new Runnable() {
            @Override
            public void run() {


                while(true){
                    try {
                        ss=new ServerSocket(6667);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    getMessage(adapter, listView, ss);

                }
            }
        }).start();


        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            s=new Socket("192.168.214.1",6666);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        sendMessage(editText.getText().toString(),adapter,editText,listView,s);
                    }
                }).start();
            }
        });
    }

    private void sendMessage(final String str, final ArrayAdapter<String> adapter, final EditText editText, final ListView listView,Socket s){
        try{
            //Socket s=new Socket("192.168.214.1",6666);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(str);
            dout.flush();
            dout.close();
            Log.e("Message>>>>>",str);
            if(str!=null) {
               runOnUiThread(new Runnable() {
                   @Override
                   public void run() {
                       adapter.add("Android \n"+str);
                       editText.setText(null);
                       listView.setAdapter(adapter);
                   }
               });
            }
            //s.close();
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    private void getMessage(final ArrayAdapter<String> adapter, final ListView listView,ServerSocket ss){
        String str = null;

        try{
            Socket s=ss.accept();
            DataInputStream dis=new DataInputStream(s.getInputStream());
            str=dis.readLine();
            if(str!=null) {
                System.out.println("PC= " + str);
                final String finalStr = str;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        adapter.add("PC: \n" + finalStr);
                        listView.setAdapter(adapter);
                    }
                });
                //ss.close();
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        System.exit(0);
        try {
            ss.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            s.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

