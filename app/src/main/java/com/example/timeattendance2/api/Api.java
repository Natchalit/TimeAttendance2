package com.example.timeattendance2.api;

import com.example.timeattendance2.model.DoReportResponse1;
import com.example.timeattendance2.model.DoReportResponse2;
import com.example.timeattendance2.model.DoReportResponse3;
import com.example.timeattendance2.model.DoWageResponse;
import com.example.timeattendance2.model.GetImage;
import com.example.timeattendance2.model.LogResponse;
import com.example.timeattendance2.model.LoginResponse;
import com.example.timeattendance2.model.StampResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface Api {

    @FormUrlEncoded
    @POST("login")
    Call<LoginResponse> userLogin(
            @Field("appkey") String appkey,
            @Field("username") String username,
            @Field("password") String password,
            @Field("request_id") float request_id
    );

    @FormUrlEncoded
    @POST("stamp")
    Call<StampResponse> stampUser(
            @Field("token") String token,
            @Field("latitude") float Latitude,
            @Field("longitude") float Longitude,
            @Field("Image") byte[] Image,
            @Field("staff_id") int staff_id,
            @Field("siteid") int siteIndex,
            @Field("timeStamp") float timeStamp,
            @Field("isCheckIn") boolean isCheckIn,
            @Field("request_id") float request_id
    );

    @FormUrlEncoded
    @POST("doLog")
    Call<LogResponse> doLog(
            @Field("token") String token,
            @Field("siteid") int siteIndex,
            @Field("fromTime") float fromTime,
            @Field("toTime") float toTime,
            @Field("request_id") float request_id
    );

    @FormUrlEncoded
    @POST("doReport1")
    Call<DoReportResponse1> doReport1(
            @Field("token") String token,
            @Field("fromTime") float fromTime,
            @Field("toTime") float toTime,
            @Field("request_id") float request_id
    );

    @FormUrlEncoded
    @POST("doReport2")
    Call<DoReportResponse2> doReport2(
            @Field("token") String token,
            @Field("request_id") float request_id
    );

    @FormUrlEncoded
    @POST("doReport3")
    Call<DoReportResponse3> doReport3(
            @Field("token") String token,
            @Field("request_id") float request_id
    );

    @FormUrlEncoded
    @POST("getimages")
    Call<GetImage> images(
            @Field("token") String token,
            @Field("siteid") int siteIndex,
            @Field("fromTime") float fromTime,
            @Field("toTime") float toTime,
            @Field("isCheckin") boolean isCheckin,
            @Field("request_id") float request_id
    );


    @FormUrlEncoded
    @POST("doWage")
    Call<DoWageResponse> doWage(
            @Field("token") String token,
            @Field("siteid") int siteIndex,
            @Field("fromTime") float fromTime,
            @Field("toTime") float toTime,
            @Field("request_id") float request_id
    );


}
