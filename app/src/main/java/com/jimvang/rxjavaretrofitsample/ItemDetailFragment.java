package com.jimvang.rxjavaretrofitsample;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.jimvang.rxjavaretrofitsample.Model.PostsItem;

import java.util.Objects;

import androidx.fragment.app.Fragment;

/**
 * A fragment representing a single Item detail screen.
 * This fragment is either contained in a {@link ItemListActivity}
 * in two-pane mode (on tablets) or a {@link ItemDetailActivity}
 * on handsets.
 */
public class ItemDetailFragment extends Fragment
{
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";

    /**
     * The dummy content this fragment is presenting.
     */
    private PostsItem mItem;

    public ItemDetailFragment()
    {
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (Objects.requireNonNull(getArguments()).containsKey(ARG_ITEM_ID))
        {
            String userId = (getArguments().getString(ARG_ITEM_ID));

            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity()
                    .findViewById(R.id.toolbar_layout);
            if (appBarLayout != null)
            {
                appBarLayout.setTitle("Posts");
            }
        }
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.item_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null)
        {
            ((TextView) rootView.findViewById(R.id.userId)).setText(mItem.getUserId());
            ((TextView) rootView.findViewById(R.id.titleId)).setText(mItem.getTitle());
            ((TextView) rootView.findViewById(R.id.bodyId)).setText(mItem.getBody());
        }

        return rootView;
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
    }
}
