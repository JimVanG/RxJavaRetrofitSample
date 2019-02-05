package com.jimvang.rxjavaretrofitsample.api;

import com.jimvang.rxjavaretrofitsample.Model.PostsItem;
import com.jimvang.rxjavaretrofitsample.Model.UserItem;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by James Van Gaasbeck on 2/4/19.
 */
public interface UserPostsApi
{
    @GET("users")
    Observable<List<UserItem>> getUsers();

    @GET("posts?")
    Observable<List<PostsItem>> getUserPosts(@Query("userId") String userId);
}
