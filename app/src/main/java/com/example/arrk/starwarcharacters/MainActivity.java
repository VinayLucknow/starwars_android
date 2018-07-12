package com.example.arrk.starwarcharacters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.graphics.drawable.DrawerArrowDrawable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import util.StarWarConstants;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar();
        addFragment(new CharactersFragment());
    }

    @Override
    public void onBackPressed() {
        onBackStackChanged();
    }

    private void setupToolBar(){
        toolbar = (Toolbar)findViewById(R.id.activity_main_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    public void setToolbarTitle(String title){
        TextView tvTitle = toolbar.findViewById(R.id.activity_reach_main_bar_tv_title);
        tvTitle.setText(title);

    }

    private void addFragment (Fragment fragment){
        try{

            FragmentManager manager = getFragmentManager();

            FragmentTransaction ft = manager.beginTransaction();
            ft.add(R.id.MainActivity_FrameContainer, fragment);
            ft.addToBackStack(null);
            ft.commit();
            // }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void onBackStackChanged(){

        FragmentManager manager = getFragmentManager();
        if(manager!=null){
            Fragment currFrag =manager.findFragmentById(R.id.MainActivity_FrameContainer);
            if(currFrag!=null){
                String fragName=currFrag.getClass().getSimpleName();
                if(fragName.equals(StarWarConstants.FragmentsName.CHARACTER_LIST_FRAGMENT)){
                    finish();
                }else if(fragName.equals(StarWarConstants.FragmentsName.CHARACTER_DETAIL_FRAGMENT)){
                    getFragmentManager().popBackStackImmediate();
                    setToolbarTitle("Star Wars Characters");
                }
            }
        }
    }
}
