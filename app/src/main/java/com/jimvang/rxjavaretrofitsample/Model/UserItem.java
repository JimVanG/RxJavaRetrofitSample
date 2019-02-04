package com.jimvang.rxjavaretrofitsample.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserItem
{

    @SerializedName("id")
    @Expose
    private int id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("username")
    @Expose
    private String username;

    /**
     * No args constructor for use in serialization
     */
    public UserItem()
    {
    }

    /**
     * @param id
     * @param username
     * @param name
     */
    public UserItem(int id, String name, String username)
    {
        super();
        this.id = id;
        this.name = name;
        this.username = username;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public UserItem withId(int id)
    {
        this.id = id;
        return this;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public UserItem withName(String name)
    {
        this.name = name;
        return this;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public UserItem withUsername(String username)
    {
        this.username = username;
        return this;
    }
}