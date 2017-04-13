package com.act_ex.a1221.serchtesthh;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by brost_host17 on 13.04.2017.
 */

public interface ApiInterface {
    @GET("/vacancies")
    Call<BasePaginationDto<VacancyDto>> searchVacancy(@Query("text") String text,
                                                      @Query("per_page") int perPage,
                                                      @Query("page") int page);
}