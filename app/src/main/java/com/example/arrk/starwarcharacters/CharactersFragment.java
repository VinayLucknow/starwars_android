package com.example.arrk.starwarcharacters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

import adapter.CharacterListAdapter;
import http.ApiClient;
import http.WebApi;
import model.Character;
import model.CharactersList;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import util.EmptyRecyclerView;
import util.InternetConnectionListener;
import util.OnLoadMoreListener;
import util.ReachRecyclerView;
import util.SeparatorDecoration;
import util.ServiceListener;

public class CharactersFragment extends Fragment{

    private View convertView;
    private Handler handler;

    private ReachRecyclerView charactersListView;
    private CharacterListAdapter characterListAdapter;
    private List<Character> characters = new ArrayList<>();
    private int page = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        convertView = inflater.inflate(R.layout.fragment_characters, container, false);

        renderForm();
        return convertView;

    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void renderForm(){
        setActionbarTitle();
        handler = new Handler();
        charactersListView = (ReachRecyclerView) convertView.findViewById(R.id.fragment_characters_list);
        charactersListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        // add the decoration to the recyclerView
        /*SeparatorDecoration decoration = new SeparatorDecoration(getActivity(), Color.BLACK, 0.5f);
        charactersListView.addItemDecoration(decoration);*/
        charactersListView.setEmptyView(convertView.findViewById(R.id.empty_view));
        if(isInternetAvailable()){
            getCharactersList();
        }else{
          showAlertDialog();
        }

    }

    private void setActionbarTitle(){
        try{
            MainActivity mainActivity=(MainActivity) getActivity();
            if(mainActivity!=null){
                mainActivity.setToolbarTitle("Star Wars Characters");
            }
        }catch (Exception e){

            e.printStackTrace();
        }
    }

    private ProgressDialog progressDialog;
    private void initProgressDialog() {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
        progressDialog.setIndeterminate(true);
        progressDialog.show();
    }

    private void getCharactersList(){

        WebApi webApi = ApiClient.getAPIClient(getActivity().getApplication()).create(WebApi.class);
        Call<CharactersList> call = webApi.getCharactersList("application/json",page);
        initProgressDialog();

        call.enqueue(new Callback<CharactersList>() {
            @Override
            public void onResponse(Call<CharactersList> call, final Response<CharactersList> response) {
                try{
                    if (progressDialog != null && progressDialog.isShowing()) {
                        try {
                            progressDialog.dismiss();
                        } catch (Exception e) {
                          e.printStackTrace();
                        }

                    }
                    if(response!=null && response.isSuccessful()){
                        Log.d("Server response",response.toString());
                        final CharactersList serevrResponse = response.body();
                        Log.d("serevrResponse",serevrResponse.toString());
                        if(serevrResponse!=null && serevrResponse.getResults()!=null){
                            characters = serevrResponse.getResults();
                            characterListAdapter = new CharacterListAdapter(getActivity(), charactersListView, characters,serevrResponse.getNext(), serevrResponse.getCount());
                            charactersListView.setAdapter(characterListAdapter);
                            characterListAdapter.setCustomerSelectedListener(new ServiceListener<Character>() {
                                @Override
                                public void result(Character result) {
                                    if(result!=null && result.getUrl()!=null){
                                        try{
                                            ObjectMapper mapper = new ObjectMapper();
                                            String jsonStr = mapper.writeValueAsString(result);
                                            CharacterDetailFragment characterDetailFragment = CharacterDetailFragment.newInstance(jsonStr);
                                            addFragment(characterDetailFragment);
                                        }catch (Exception e){
                                          e.printStackTrace();
                                        }
                                    }
                                }
                            });

                            characterListAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
                                @Override
                                public void onLoadMore(final int page) {
                                    if(serevrResponse!=null && serevrResponse.getResults()!=null){
                                        final List<Character> characters = serevrResponse.getResults();
                                        characters.add(null);
                                        characterListAdapter.notifyItemInserted(characters.size() - 1);

                                        handler.postDelayed(new Runnable() {
                                            @Override
                                            public void run() {
                                                characters.remove(characters.size() - 1);
                                                WebApi webApi = ApiClient.getAPIClient(getActivity().getApplication()).create(WebApi.class);
                                                Call<CharactersList> call = webApi.getCharactersList("application/json",page);
                                                call.enqueue(new Callback<CharactersList>() {
                                                    @Override
                                                    public void onResponse(Call<CharactersList> call, final Response<CharactersList> response) {

                                                        if(response!=null && response.isSuccessful()){
                                                            final CharactersList serevrResponse = response.body();
                                                            if(serevrResponse!=null && serevrResponse.getResults()!=null){
                                                                if(characters!=null){
                                                                    characters.addAll(serevrResponse.getResults());
                                                                    characterListAdapter.setLoaded();
                                                                    characterListAdapter.setNextUrl(serevrResponse.getNext());
                                                                    characterListAdapter.notifyDataSetChanged();
                                                                }
                                                            }
                                                        }
                                                    }

                                                    @Override
                                                    public void onFailure(Call<CharactersList> call, Throwable t) {

                                                    }
                                                });
                                            }
                                        },1500);

                                    }
                                }
                            });
                        }
                    }else{
                        switch (response.code()) {
                            case 404:
                                Toast.makeText(getActivity(), "not found", Toast.LENGTH_SHORT).show();
                                break;
                            case 500:
                                Toast.makeText(getActivity(), "server broken", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Toast.makeText(getActivity(), "unknown error", Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<CharactersList> call, Throwable t) {

            }
        });

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

    private boolean isInternetAvailable(){
        ConnectivityManager connectivityManager = (ConnectivityManager)getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            return true;
        }else{
            return false;
        }

    }

    private void showAlertDialog(){

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        //builder.setTitle("AlertDialog with No Buttons");

        builder.setMessage("Network connection error! Please on you internet connection");

        //Yes Button
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                getActivity().finish();
            }
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

}
