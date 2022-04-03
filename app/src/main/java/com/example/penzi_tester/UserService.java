package com.example.penzi_tester;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

interface UserService {

    @GET("search_query/{age1}/{age2}/{county}/{gender}")
    Call<List<MatchUserResponse>> MatchUser(@Path("age1") int age1, @Path("age2") int age2, @Path("county") String county, @Path("gender") String gender);

    @GET("search_number_of_genders_matched/{age1}/{age2}/{county}/{gender}")
    Call<String> UserNo(@Path("age1") int age1, @Path("age2") int age2, @Path("county") String county, @Path("gender") String gender);





}
