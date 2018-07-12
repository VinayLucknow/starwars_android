package com.example.arrk.starwarcharacters;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import http.ApiClient;
import http.WebApi;
import model.Character;
import model.CharactersList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.StarWarConstants;

public class CharacterDetailFragment extends Fragment {

    private View convertView;
    private String detailUrl;
    private Character character = null;

    private TextView tvCharacterName, tvCharacterHeight, tvCharacterMass, tvDate, tvTime;

    public static CharacterDetailFragment newInstance(String jsonStr) {
        Bundle args = new Bundle();
        args.putString("characterDetail", jsonStr);
        CharacterDetailFragment fragment = new CharacterDetailFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null && getArguments().containsKey("characterDetail")){
            detailUrl=getArguments().getString("characterDetail",null);
            if(detailUrl!=null){
                try{
                    ObjectMapper mapper = new ObjectMapper();
                    character = mapper.readValue(detailUrl, Character.class);
                }catch (Exception e){
                  e.printStackTrace();
                }
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        convertView = inflater.inflate(R.layout.fragment_character_detail, container, false);
        renderForm();
        return convertView;
    }

    private void renderForm(){
        setActionbarTitle();
        tvCharacterName = (TextView)convertView.findViewById(R.id.item_character_tv_name);
        tvCharacterHeight = (TextView)convertView.findViewById(R.id.item_character_tv_height);
        tvCharacterMass = (TextView)convertView.findViewById(R.id.item_character_tv_mass);
        tvDate = (TextView)convertView.findViewById(R.id.item_character_tv_date);
        tvTime = (TextView)convertView.findViewById(R.id.item_character_tv_time);
        if(character!=null){
          tvCharacterName.setText(character.getName());
          tvCharacterHeight.setText(character.getHeight());
          tvCharacterHeight.append(" Meters ");
          tvCharacterMass.setText(character.getMass());
          tvCharacterMass.append(" Kg ");
          if(character.getCreated()!=null && dateUtil(character.getCreated())!=null){
              tvDate.setText(dateUtil(character.getCreated()));
          }

        }
    }

    private void setActionbarTitle(){
        try{
            MainActivity mainActivity=(MainActivity) getActivity();
            if(mainActivity!=null){
                mainActivity.setToolbarTitle("Character Details");

            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private String dateUtil(String dateStr){
        final String SERVER_DATE_STR = "yyyy-MM-dd'T'HH:mm";
        final String DISPLAY_DATE_STR = "yyyy-MM-dd";
        final String DISPLAY_TIME_STR = "HH:mm a";
        SimpleDateFormat serverDateFormat = new SimpleDateFormat(SERVER_DATE_STR, Locale.ENGLISH);
        SimpleDateFormat displayDateFormat = new SimpleDateFormat(DISPLAY_DATE_STR, Locale.ENGLISH);
        SimpleDateFormat time = new SimpleDateFormat(DISPLAY_TIME_STR);
        Date date = parse(dateStr, serverDateFormat);
        String timeStr = time.format(date);
        if(timeStr!=null){
            tvTime.setText(timeStr);
        }
        return format(date,displayDateFormat);

    }
    public Date parse(String time, SimpleDateFormat from) {
        try {
            return from.parse(time);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return null;
    }

    public String format(Date date, SimpleDateFormat to) {
        if (date == null)
            return null;
        return to.format(date);
    }
}
