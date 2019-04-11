package com.eci.taskplanner;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthService {
    @POST("/auth")
    Call<Token> sighIn(@Body LoginWrapper loginWrapper);
}
