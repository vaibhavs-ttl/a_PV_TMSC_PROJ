package com.ttl.communication;

import java.io.File;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.util.Log;

/**
 * Created by vaibhav on 9/21/2016.
 */
public class AppRemover extends BroadcastReceiver {
    private final String folder_path= Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/CustomerSocialAppDocument/";
    @Override
    public void onReceive(Context context, Intent intent) {


     /*   Log.d("inside app remover","reached");
        Log.d("action name",intent.getAction());

        final File removeFolder=new File(folder_path);

        if(removeFolder.isDirectory())
        {



        new Thread(new Runnable() {
            @Override
           synchronized public void run() {

                deleteDirectory(removeFolder);

            }
        }).start();
        }
        else
        {

           
        
        }*/
   
    
    
    
    
    
    
    
    
    
    
    }

    private void deleteDirectory(File file)
    {


        if (file.isDirectory()) {
            for (File sub : file.listFiles()) {
                deleteDirectory(sub);
            }
        }
        file.delete();


    }




    }





