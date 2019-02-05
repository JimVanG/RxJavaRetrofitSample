package com.jimvang.rxjavaretrofitsample;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jimvang.rxjavaretrofitsample.Model.UserItem;
import com.jimvang.rxjavaretrofitsample.api.RetrofitClient;
import com.jimvang.rxjavaretrofitsample.api.UserPostsApi;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

/**
 * An activity representing a list of Items. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ItemDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ItemListActivity extends AppCompatActivity
{

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;

    RecyclerView recyclerView;

    CompositeDisposable disposable = new CompositeDisposable();
    List<UserItem> userItems = new ArrayList<>();
    UserPostsApi api;
    private SimpleItemRecyclerViewAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        if (findViewById(R.id.item_detail_container) != null)
        {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }

        RetrofitClient client = RetrofitClient.getInstance();
        api = client.createService(UserPostsApi.class);

        recyclerView = findViewById(R.id.item_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        subscribeToData();
    }

    @Override
    protected void onDestroy()
    {
        disposable.clear();
        super.onDestroy();
    }

    private void subscribeToData()
    {
        disposable.add(api.getUsers()
                               .subscribeOn(Schedulers.io())
                               .observeOn(AndroidSchedulers.mainThread())
                               .subscribe(this::updateRecyclerView));
    }

    private void updateRecyclerView(@Nullable List<UserItem> items)
    {
        if (recyclerView == null)
        {
            return;
        }

        if (recyclerView.getAdapter() == null)
        {
            setupRecyclerView(recyclerView);
        }

        adapter.updateListItems(items);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView)
    {
        adapter = new SimpleItemRecyclerViewAdapter(this, userItems, mTwoPane);
        recyclerView.setAdapter(adapter);
    }

    public static class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>
    {

        private final ItemListActivity mParentActivity;
        private List<UserItem> mValues;
        private final boolean mTwoPane;
        private final View.OnClickListener mOnClickListener = new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                UserItem item = (UserItem) view.getTag();
                if (mTwoPane)
                {
                    Bundle arguments = new Bundle();
                    arguments.putString(ItemDetailFragment.ARG_ITEM_ID,
                                        String.valueOf(item.getId()));
                    ItemDetailFragment fragment = new ItemDetailFragment();
                    fragment.setArguments(arguments);
                    mParentActivity.getSupportFragmentManager().beginTransaction()
                            .replace(R.id.item_detail_container, fragment)
                            .commit();
                }
                else
                {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ItemDetailActivity.class);
                    intent.putExtra(ItemDetailFragment.ARG_ITEM_ID, String.valueOf(item.getId()));

                    context.startActivity(intent);
                }
            }
        };

        SimpleItemRecyclerViewAdapter(
                ItemListActivity parent,
                List<UserItem> items,
                boolean twoPane)
        {
            mValues = items;
            mParentActivity = parent;
            mTwoPane = twoPane;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, int position)
        {
            holder.mIdView.setText(mValues.get(position).getName());
            holder.mContentView.setText(mValues.get(position).getUsername());

            holder.itemView.setTag(mValues.get(position));
            holder.itemView.setOnClickListener(mOnClickListener);
        }

        @Override
        public int getItemCount()
        {
            return mValues.size();
        }

        public void updateListItems(List<UserItem> items)
        {
            mValues = items;
            if (mValues == null)
            {
                notifyDataSetChanged();
                return;
            }
            notifyItemRangeInserted(0, mValues.size());
        }

        class ViewHolder extends RecyclerView.ViewHolder
        {
            final TextView mIdView;
            final TextView mContentView;

            ViewHolder(View view)
            {
                super(view);
                mIdView = (TextView) view.findViewById(R.id.nameId);
                mContentView = (TextView) view.findViewById(R.id.usernameId);
            }
        }
    }
}
