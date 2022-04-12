package com.example.praxisprojekt.SendNotificationPack;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;
public interface APIService {
    @Headers(
            {
                    "Content-Type:application/json",
                    "AAAAWE9QoL8:APA91bFkMnjMjHNCAWwNtAZZL-6WKe8e2_eHV6t4Xyk-XGJtlzEf0RaD6zt9_uanuHsqeBn1XwHixDqAlnU4IEXzMvRAWBpKuxKk9H42WcxTGqmM1QZZWbve4vMG9s4Jd_DYgrVVbvGh" // Your server

            }
    )
    @POST("fcm/send")
    Call<MyResponse> sendNotifcation(@Body NotificationSender body);
}