package http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.example.arrk.starwarcharacters.R;

import java.util.concurrent.TimeUnit;

import interceptor.NetworkConnectionInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import util.InternetConnectionListener;
import util.StarWarConstants;


public class ApiClient {
    
    private static Retrofit retrofit = null;
    
    public static Retrofit getAPIClient(Context context) {
        
        if (retrofit==null) {
            
            retrofit = new Retrofit.Builder()
                               .baseUrl(context.getString(R.string.base_url))
                               .client(provideOkHttpClient())
                               .addConverterFactory(JacksonConverterFactory.create(StarWarConstants.jsonObjMapper))
                               .build();
            
        }
        
        return retrofit;
        
    }

    private static OkHttpClient provideOkHttpClient() {

        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

        okhttpClientBuilder.connectTimeout(30, TimeUnit.SECONDS);

        okhttpClientBuilder.readTimeout(30, TimeUnit.SECONDS);

        okhttpClientBuilder.writeTimeout(30, TimeUnit.SECONDS);
        okhttpClientBuilder.addInterceptor(new NetworkConnectionInterceptor() {
            @Override
            public boolean isInternetAvailable() {
                return false;
            }

            @Override
            public void onInternetUnavailable() {
                if (mInternetConnectionListener != null) {

                    mInternetConnectionListener.onInternetUnavailable();

                }
            }
        });

        return okhttpClientBuilder.build();

    }

    private static InternetConnectionListener mInternetConnectionListener;

    public static void setInternetConnectionListener(InternetConnectionListener listener) {

        mInternetConnectionListener = listener;

    }

    public static void removeInternetConnectionListner(){
        if (mInternetConnectionListener != null) {

            mInternetConnectionListener =null;

        }
    }

}


