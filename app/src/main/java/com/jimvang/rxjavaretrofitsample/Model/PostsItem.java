package com.jimvang.rxjavaretrofitsample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;

/**
 * Created by James Van Gaasbeck on 2/4/19.
 */

public class PostsItem
{

    @SerializedName("userId")
    @Expose
    private int userId;
    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("body")
    @Expose
    private String body;

    /**
     * No args constructor for use in serialization
     */
    public PostsItem()
    {
    }

    /**
     * @param id
     * @param body
     * @param title
     * @param userId
     */
    public PostsItem(int userId, int id, String title, String body)
    {
        super();
        this.userId = userId;
        this.id = id;
        this.title = title;
        this.body = body;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public PostsItem withUserId(int userId)
    {
        this.userId = userId;
        return this;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public PostsItem withId(int id)
    {
        this.id = id;
        return this;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public PostsItem withTitle(String title)
    {
        this.title = title;
        return this;
    }

    public String getBody()
    {
        return body;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public PostsItem withBody(String body)
    {
        this.body = body;
        return this;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        PostsItem postsItem = (PostsItem) o;
        return userId == postsItem.userId &&
                id == postsItem.id &&
                Objects.equals(title, postsItem.title) &&
                Objects.equals(body, postsItem.body);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(userId, id, title, body);
    }

    @Override
    public String toString()
    {
        return "PostsItem{" +
                "userId=" + userId +
                ", id=" + id +
                ", title='" + title + '\'' +
                ", body='" + body + '\'' +
                '}';
    }
}