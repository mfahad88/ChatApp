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
    Socket s=null;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        s=createSocket();
        btn=(Button)findViewById(R.id.button);
        editText=(EditText)findViewById(R.id.editText);
        listView=(ListView)findViewById(R.id.listView);
        final ArrayAdapter<String> adapter=new ArrayAdapter<>(this,android.R.layout.simple_list_item_1);
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(getMessage(s)!=null) {
                        str = getMessage(s);
                    }
                }
            }
        }).start();
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true){
                    if(str!=null){
                        Log.e("Message>>>",str);
                    }
                }
            }
        }).start();
       /* listView.post(new Runnable() {
            @Override
            public void run() {
                if(getMessage(s)!=null) {
                    listView.setAdapter(adapter);
                }
            }
        });*/



    }
    private String sendMessage(String str){
        try{
            Socket s=new Socket("192.168.214.1",6666);
            DataOutputStream dout=new DataOutputStream(s.getOutputStream());
            dout.writeUTF(str);
            dout.flush();
            dout.close();
            Log.e("Message>>>>>",str);
            s.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "Android: \n"+str;
    }
    private Socket createSocket(){
        ServerSocket ss;
        Socket s=null;
        try{
            ss=new ServerSocket(5555);
            s=ss.accept();

        }catch (Exception e){
            e.printStackTrace();
        }
        return s;
    }
    private String getMessage(Socket s){
        String str=null;

        try{
            DataInputStream dis=new DataInputStream(s.getInputStream());
            str=dis.readLine();
            System.out.println("PC= "+str);
            //ss.close();
        }catch(Exception e){
            e.printStackTrace();
        }
        return "PC: \n"+str;
    }


}

