package com.calibrage.payzanconsumer.framework.networkservices;


import com.calibrage.payzanconsumer.framework.adapters.AgentSplit;
import com.calibrage.payzanconsumer.framework.model.AddressAddResponceModel;
import com.calibrage.payzanconsumer.framework.model.AgentResponseModel;
import com.calibrage.payzanconsumer.framework.model.BusinessCategoryModel;
import com.calibrage.payzanconsumer.framework.model.ChangePasswordResponseModel;
import com.calibrage.payzanconsumer.framework.model.DistrictModel;
import com.calibrage.payzanconsumer.framework.model.EditAddressModel;
import com.calibrage.payzanconsumer.framework.model.LoginResponseModel;
import com.calibrage.payzanconsumer.framework.model.MandalModel;
import com.calibrage.payzanconsumer.framework.model.NetBanksModel;
import com.calibrage.payzanconsumer.framework.model.OperatorModel;
import com.calibrage.payzanconsumer.framework.model.RefreshResponseModel;
import com.calibrage.payzanconsumer.framework.model.ResponseModel;
import com.calibrage.payzanconsumer.framework.model.SendMoneyResponseModel;
import com.calibrage.payzanconsumer.framework.model.StatesModel;
import com.calibrage.payzanconsumer.framework.model.TransactionResponseModel;
import com.calibrage.payzanconsumer.framework.model.UserAddressModel;
import com.calibrage.payzanconsumer.framework.model.UserProfileInfoResponseModel;
import com.calibrage.payzanconsumer.framework.model.UserProfileInfoSucessModel;
import com.calibrage.payzanconsumer.framework.model.VillageModel;
import com.calibrage.payzanconsumer.framework.model.WalletBalanceResponce;
import com.calibrage.payzanconsumer.framework.model.WalletResponse;
import com.google.gson.JsonObject;

import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Url;
import rx.Observable;


public interface MyServices {


    // @Headers("Accept: application/json")
    @POST(ApiConstants.REGISTER)
    Observable<ResponseModel> userRegister(@Body JsonObject data);

    @POST(ApiConstants.LOGIN)
    Observable<LoginResponseModel> UserLogin(@Body JsonObject data);

    @POST(ApiConstants.WALLET)
    Observable<WalletResponse> UserAddWallet(@Body JsonObject data);

    @GET
    Observable<StatesModel> getProvinance(@Url String url);

    @GET
    Observable<DistrictModel> getDistricts(@Url String url);

    @GET
    Observable<MandalModel> getMandals(@Url String url);

    @GET
    Observable<BusinessCategoryModel> getBusinessRequest(@Url String url);

    @GET
    Observable<VillageModel> getVillages(@Url String url);

    @GET
    Observable<OperatorModel> getOperator(@Url String url);

    @POST(ApiConstants.AGENT_REQUEST)
    Observable<AgentResponseModel> agentRequest(@Body JsonObject data);

    @POST(ApiConstants.CHANGE_PASSWORD)
    Observable<ChangePasswordResponseModel> changePassword(@Body JsonObject data);

    @POST(ApiConstants.SEND_MONEY_WALLET)
    Observable<SendMoneyResponseModel> sendMoneyRequest(@Body JsonObject data);

    @GET
    Observable<TransactionResponseModel> myTransactions(@Url String url);

    @GET
    Observable<AgentSplit> GetAgentCategorySplitByAgentId(@Url String url);

    @GET
    Observable<WalletBalanceResponce> GetUserBalanceByID(@Url String url);

    @GET
    Observable<NetBanksModel> getBankRequest(@Url String url);

    @GET
    Observable<UserProfileInfoResponseModel> getUserProfileInfo(@Url String url);

    @POST(ApiConstants.UpdateUserPersonalInfo)
    Observable<UserProfileInfoSucessModel> sendUserProfileInfo(@Body JsonObject data);

    @POST(ApiConstants.ADDUSER_NEW_ADDRESS)
    Observable<AddressAddResponceModel> addNewAddress(@Body JsonObject data);

    @GET
    Observable<UserAddressModel> getUserAddress(@Url String url);

    @GET
    Observable<EditAddressModel> editUserAddress(@Url String url);

    @DELETE
    Observable<EditAddressModel> editDeleteAddress(@Url String url);

    @PUT(ApiConstants.Update_Address)
    Observable<EditAddressModel> updateAddress(@Body JsonObject data);


    @POST(ApiConstants.REFRESH_TOKEN)
    Observable<RefreshResponseModel>getRefreshToken(@Body JsonObject data);

}
