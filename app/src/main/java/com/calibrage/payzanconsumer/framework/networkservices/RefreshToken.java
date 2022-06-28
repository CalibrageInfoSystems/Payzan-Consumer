package com.calibrage.payzanconsumer.framework.networkservices;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.calibrage.payzanconsumer.R;
import com.calibrage.payzanconsumer.framework.model.RefreshResponseModel;
import com.calibrage.payzanconsumer.framework.model.RefreshTokenModel;
import com.calibrage.payzanconsumer.framework.util.CommonUtil;
import com.calibrage.payzanconsumer.framework.util.MyCounter;
import com.calibrage.payzanconsumer.framework.util.SharedPrefsData;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_ID;
import static com.calibrage.payzanconsumer.framework.util.CommonConstants.CLIENT_SECRET;

/**
 * Created by Calibrage11 on 12/28/2017.
 */

public class RefreshToken extends BroadcastReceiver {
    private Context context;
    private Subscription  refrshSubscription;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        getRefreshToken();
    }


    private void getRefreshToken() {
        if (CommonUtil.isNetworkAvailable(context)) {
            JsonObject object = getRefreshObject();
            MyServices service = ServiceFactory.createRetrofitService(context, MyServices.class);
            refrshSubscription = service.getRefreshToken(object)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Subscriber<RefreshResponseModel>() {
                        @Override
                        public void onCompleted() {
                            //   Toast.makeText(context, "check", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onError(Throwable e) {
                            if (e instanceof HttpException) {
                                ((HttpException) e).code();
                                ((HttpException) e).message();
                                ((HttpException) e).response().errorBody();
                                try {
                                    ((HttpException) e).response().errorBody().string();
                                } catch (IOException e1) {
                                    e1.printStackTrace();
                                }
                                e.printStackTrace();
                            }
                            Toast.makeText(context, context.getString(R.string.toast_fail), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void onNext(RefreshResponseModel refreshResponseModel) {
                            SharedPrefsData.getInstance(context).updateStringValue(context, "Token", refreshResponseModel.getResult().getTokenType() + " " + refreshResponseModel.getResult().getAccessToken());
                            SharedPrefsData.getInstance(context).updateStringValue(context, "RefreshToken",refreshResponseModel.getResult().getRefreshToken());

                            CommonUtil.timer.cancel();
                            CommonUtil.timer = new MyCounter(refreshResponseModel.getResult().getExpiresIn()*1000,1000,context);
                            CommonUtil.timer.start();
                        }
                    });
        }
    }

    private JsonObject getRefreshObject() {
        RefreshTokenModel refreshTokenModel = new RefreshTokenModel();
        refreshTokenModel.setClientId(CLIENT_ID);
        refreshTokenModel.setClientSecret(CLIENT_SECRET);
        refreshTokenModel.setRefreshToken(SharedPrefsData.getInstance(context).getStringFromSharedPrefs("RefreshToken"));
        Log.e("RefreshToken", "" + new Gson().toJsonTree(refreshTokenModel)
                .getAsJsonObject());
        return new Gson().toJsonTree(refreshTokenModel)
                .getAsJsonObject();
    }
}
