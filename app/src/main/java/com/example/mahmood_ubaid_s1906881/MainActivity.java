/*  Starter project for Mobile Platform Development in Semester B Session 2018/2019
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Ubaid Mahmood
// Student ID           S1906881
// Programme of Study   BS/c Hons Computing
//

// Update the package name to include your Student Identifier

package com.example.mahmood_ubaid_s1906881;


import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.example.mahmood_ubaid_s1906881.fragments.ListFragment;
import com.example.mahmood_ubaid_s1906881.fragments.MapsFragment;
import com.example.mahmood_ubaid_s1906881.model.Earthquake;
import com.example.mahmood_ubaid_s1906881.viewmodel.ViewModel;

import java.util.ArrayList;
import java.util.List;

/**
 * This class handles the entire application.
 * @author ubaid
 * @apiNote Minimum api level 23
 */
public class MainActivity extends AppCompatActivity {

    private HandlerThread handlerThread;
    private Handler informUIHandler = null;
    private final static int MESSAGE_INFORM_TEXT_CHILD_THREAD = 1;
    private List<Earthquake> earthquakeList = new ArrayList<>();
    private ListFragment fragmentList;
    private MapsFragment fragmentMap;
    private MapsFragment fragmentMap2;
    private ViewModel viewModel;
    private View frameLayout;
    private ViewFlipper viewFlipper;
    private View relLayout1;
    private View relLayout3;
    private ImageView fullScreenIcon;
    private ImageView exitFullScreenIcon;
    private View relLayout4;

    /**
     * This method create the view before the start method is called, one of the first method called
     * upon initialisation of the app.
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Set up the raw links to the graphical components

        viewModel = new ViewModelProvider(this).get(ViewModel.class);

        createUpdateUiHandler();

        frameLayout = findViewById(R.id.mapFragment);
        viewFlipper = (ViewFlipper) findViewById(R.id.viewFlipper);
        relLayout1 = (RelativeLayout) findViewById(R.id.relLayout1);
        relLayout3 = (RelativeLayout) findViewById(R.id.relLayout3);
        fullScreenIcon = (ImageView) findViewById(R.id.fullScreenIcon);
        exitFullScreenIcon = (ImageView) findViewById(R.id.exitFullScreen);
        relLayout4 = (RelativeLayout) findViewById(R.id.relLayout4);

        fullScreenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showNext();
            }
        });

        exitFullScreenIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewFlipper.showPrevious();
            }
        });


    }


    /**
     *
     * After the view is created, this method is called.
     * The method retrieves the RSS data when the internet is available.
     *
     */
    @Override
    protected void onStart() {
        super.onStart();

        NetworkInfo info = (NetworkInfo) ((ConnectivityManager)
                this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();

        if (info == null) {
            Log.d("Internet","no internet connection");
            viewFlipper.setDisplayedChild(viewFlipper.indexOfChild(relLayout4));
        }
        else {
            if(info.isConnected()) {
                Log.d("Internet", " internet connection available...");

                if (viewModel.getEarthquakes() == null) {


                    handlerThread = new HandlerThread("MyHandlerThread");
                    handlerThread.start();
                    Handler handler = new Handler(handlerThread.getLooper());
                    Runnable runnable = () -> {
                        // Async code is entered here
                        try {
                            viewModel.startProgressOfService();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                        earthquakeList = viewModel.getEarthquakes();

                        Message message = new Message();
                        message.what = MESSAGE_INFORM_TEXT_CHILD_THREAD;


                        message.obj = earthquakeList;
                        informUIHandler.sendMessage(message);
                        Log.e("MyTag", "In run");
                    };
                    handler.post(runnable);
                } else {
                    Log.e("VMState", "VM already has data");
                }
            }

        }

    }

    /**
     *
     * This method updated the UI Thread, once the RSS data have been retrieved.
     * The method instantiates the fragments, which display the data
     *
     */
    private void createUpdateUiHandler(){

        if(informUIHandler == null){
            informUIHandler = new Handler(Looper.getMainLooper()){
                @Override public void handleMessage(Message msg){
                    // Means the message is sent from child thread
                     if(msg.what == MESSAGE_INFORM_TEXT_CHILD_THREAD){
                         // Update UI in main thread

                         Runnable runnable = () -> {
                             fragmentList = new ListFragment();
                             fragmentMap = new MapsFragment();
                             fragmentMap2 = new MapsFragment();
                             FragmentManager manager = getSupportFragmentManager();
                             FragmentTransaction transaction = manager.beginTransaction();
                             transaction.replace(R.id.listFragmentContainer, fragmentList);
                             transaction.replace(R.id.mapFragment, fragmentMap);
                             transaction.replace(R.id.mapFragment2, fragmentMap2);
                             transaction.commit();

                         };
                         runnable.run();

                     }
                }
            };
        }
    }

}