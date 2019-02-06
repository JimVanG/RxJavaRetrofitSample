package com.jimvang.rxjavaretrofitsample;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jimvang.rxjavaretrofitsample.Model.PostsItem;
import com.jimvang.rxjavaretrofitsample.api.RetrofitClient;
import com.jimvang.rxjavaretrofitsample.api.UserPostsApi;

import java.util.List;
import java.util.Objects;
import java.util.Random;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment
{
    private static final String TAG = "ItemDetailFragment";

    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The content this fragment is presenting.
     */
    private PostsItem mItem;

    CompositeDisposable disposable = new CompositeDisposable();
    UserPostsApi api;

    TextView userId, titleId, bodyId;

    public ItemDetailFragment()
    {
    }

    public static ItemDetailFragment NewInstance(String userId)
    {
        ItemDetailFragment fragment = new ItemDetailFragment();

        Bundle args = new Bundle();
        args.putString(ARG_ITEM_ID, userId);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (!Objects.requireNonNull(getArguments()).containsKey(ARG_ITEM_ID))
        {
            return;
        }

        String userId = (getArguments().getString(ARG_ITEM_ID));

        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity()
                .findViewById(R.id.toolbar_layout);
        if (appBarLayout != null)
        {
            appBarLayout.setTitle(getActivity().getResources().getString(R.string.posts_toolbar));
        }

        if (TextUtils.isEmpty(userId))
        {
            return;
        }
        RetrofitClient client = RetrofitClient.getInstance();
        api = client.createService(UserPostsApi.class);

        subscribeToData(userId);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the content as text in TextViews.
        userId = ((TextView) rootView.findViewById(R.id.userId));
        titleId = ((TextView) rootView.findViewById(R.id.titleId));
        bodyId = ((TextView) rootView.findViewById(R.id.bodyId));

        return rootView;
    }

    @Override
    public void onDestroy()
    {
        disposable.clear();
        super.onDestroy();
    }

    private void subscribeToData(String userId)
    {
        disposable.add(api.getUserPosts(userId)
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(this::updateUi));
    }

    private void updateUi(List<PostsItem> postsItems)
    {
        Random rn = new Random();
        int range = postsItems.size();
        int randomNum = rn.nextInt(range);

        if (randomNum >= postsItems.size())
        {
            randomNum = 3;
        }
        mItem = postsItems.get(randomNum);

        if (mItem == null)
        {
            return;
        }

        userId.setText(String.format(getString(R.string.name_item),
                                     String.valueOf(mItem.getUserId())));
        titleId.setText(String.format(getString(R.string.title_post), mItem.getTitle()));
        bodyId.setText(String.format(getString(R.string.body_post), mItem.getBody()));
    }
}
