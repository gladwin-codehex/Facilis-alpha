package in.codehex.facilis;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.codehex.facilis.app.AppController;
import in.codehex.facilis.app.Config;
import in.codehex.facilis.app.ItemClickListener;
import in.codehex.facilis.helper.CircleTransform;
import in.codehex.facilis.model.BidItem;


/**
 * A fragment that is used to display previous bids list in the {@link SellerActivity} class.
 */
public class PreviousBidsFragment extends Fragment {

    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    List<BidItem> mBidItemList;
    PreviousBidsAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mCount = 0;
    boolean isEnded = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int previousTotal = 0;
    boolean loading = true;
    int visibleThreshold = 5;

    public PreviousBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_previous_bids, container, false);

        initObjects(view);
        prepareObjects();

        return view;
    }

    /**
     * Initialize the objects.
     *
     * @param view the root view of the layout.
     */
    private void initObjects(View view) {
        mRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.previous_bid_list);

        mBidItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new PreviousBidsAdapter(getContext(), mBidItemList);
        userPreferences = getActivity().getSharedPreferences(Config.PREF_USER,
                Context.MODE_PRIVATE);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                visibleItemCount = mRecyclerView.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                firstVisibleItem = mLayoutManager.findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    processBids();
                    loading = true;
                }
            }
        });

        mRefreshLayout.setColorSchemeColors(R.color.primary, R.color.primary_dark, R.color.accent);
        mRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                resetData();
                mRefreshLayout.setRefreshing(true);
                processBids();
            }
        });
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                resetData();
                mRefreshLayout.setRefreshing(true);
                processBids();
            }
        });
    }

    /**
     * Reset the count for all the integers and values of booleans to default.
     */
    private void resetData() {
        mCount = 0;
        previousTotal = 0;
        loading = true;
        isEnded = false;
    }

    /**
     * Fetch the previous bid list from the server.
     */
    private void processBids() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_KEY, "previous_bids");
            jsonObject.put(Config.KEY_API_USER, userPreferences.getInt(Config.KEY_PREF_USER_ID, 0));
            jsonObject.put(Config.KEY_API_START, mCount);
            jsonObject.put(Config.KEY_API_END, mCount + 10);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_VIEW_ORDERS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mCount == 0) {
                    mBidItemList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshLayout.setRefreshing(false);
                try {
                    JSONArray jsonArray = new JSONArray(response);
                    if (jsonArray.length() < 10)
                        isEnded = true;

                    if (!isEnded) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt(Config.KEY_API_ID);
                            JSONObject orderObject = object.getJSONObject(Config.KEY_API_ORDER);
                            int orderId = orderObject.getInt(Config.KEY_API_ID);
                            int orderOrderId = orderObject.getInt(Config.KEY_API_ORDER_ID);
                            JSONObject postedByObject = orderObject
                                    .getJSONObject(Config.KEY_API_POSTED_BY);
                            int postedById = postedByObject
                                    .getInt(Config.KEY_API_POSTED_BY_ID);
                            String postedByFirstName = postedByObject
                                    .getString(Config.KEY_API_POSTED_BY_FIRST_NAME);
                            String postedByLastName = postedByObject
                                    .getString(Config.KEY_API_POSTED_BY_LAST_NAME);
                            String biddingTime = orderObject.getString(Config.KEY_API_BIDDING_TIME);
                            String days = object.getString(Config.KEY_API_DAYS);
                            int bidCost = object.getInt(Config.KEY_API_BID_COST);
                            boolean bidStatus = object.getBoolean(Config.KEY_API_BID_STATUS);
                            int statusColor;
                            if (bidStatus)
                                statusColor = R.color.status_green;
                            else statusColor = R.color.status_red;
                            String postedDate = object.getString(Config.KEY_API_POSTED_DATE);
                            String userImg = object.getString(Config.KEY_API_USER_IMAGE);
                            mBidItemList.add(mCount + i, new BidItem(id, orderId,
                                    orderOrderId, postedById, bidCost, postedByFirstName,
                                    postedByLastName, biddingTime, days, postedDate,
                                    userImg, bidStatus, statusColor));
                            mAdapter.notifyItemInserted(mCount + i);
                        }
                        mCount = mCount + 10;
                    }
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(),
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                mRefreshLayout.setRefreshing(false);
                NetworkResponse response = error.networkResponse;
                try {
                    byte[] bytes = response.data;
                    String data = new String(bytes);
                    if (response.statusCode == 400) {
                        JSONObject errorObject = new JSONObject(data);
                        int mError = errorObject.getInt(Config.KEY_API_ERROR);
                        String mMessage = errorObject.getString(Config.KEY_API_MESSAGE);
                        if (mError == 400)
                            isEnded = true;
                        // TODO: remove toast
                        Toast.makeText(getContext(), mMessage, Toast.LENGTH_SHORT).show();
                    } else if (response.statusCode == 401) {
                        JSONObject errorObject = new JSONObject(data);
                        String errorData = errorObject.getString(Config.KEY_API_DETAIL);
                        // TODO: remove toast
                        Toast.makeText(getContext(), errorData, Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(),
                            "Error occurred while parsing data - "
                                    + e.getMessage(), Toast.LENGTH_SHORT).show();
                } catch (NullPointerException e) {
                    // TODO: remove toast
                    Toast.makeText(getContext(), "Network error - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Authorization", "Token " +
                        userPreferences.getString(Config.KEY_PREF_TOKEN, null));
                return headers;
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                return jsonObject.toString().getBytes();
            }

            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }
        };

        AppController.getInstance().addToRequestQueue(stringRequest, "previous_bids");
    }

    /**
     * View adapter for the recycler view of the previous bids list item.
     */
    private class PreviousBidsAdapter
            extends RecyclerView.Adapter<PreviousBidsAdapter.PreviousBidsHolder> {

        Context context;
        List<BidItem> mBidItemList;

        public PreviousBidsAdapter(Context context, List<BidItem> mBidItemList) {
            this.context = context;
            this.mBidItemList = mBidItemList;
        }

        @Override
        public PreviousBidsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_previous_bid, parent, false);
            return new PreviousBidsHolder(view);
        }

        @Override
        public void onBindViewHolder(PreviousBidsHolder holder, int position) {
            final BidItem bidItem = mBidItemList.get(position);
            String name = bidItem.getPostedByFirstName() + " "
                    + bidItem.getPostedByLastName();
            String dp = bidItem.getUserImg();
            String duration = String.valueOf(bidItem.getDays()) + " left";
            String posted = bidItem.getPostedDate();
            String amount = "\u20B9 " + String.valueOf(bidItem.getBidCost());

            holder.viewStatus.setBackgroundResource(bidItem.getStatusColor());
            holder.textName.setText(name);
            Glide.with(context).load(dp)
                    .placeholder(R.drawable.ic_person_gray)
                    .transform(new CircleTransform(context)).into(holder.imgDp);
            holder.textDuration.setText(duration);
            holder.textPosted.setText(posted);
            holder.textAmount.setText(amount);

            holder.setClickListener(new ItemClickListener() {
                @Override
                public void onClick(View view, int position, boolean isLongClick) {
                    ((SellerActivity) getActivity()).showBidItems(Config.KEY_FRAGMENT_PREVIOUS,
                            bidItem.getOrderId(), bidItem.getId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mBidItemList.size();
        }

        protected class PreviousBidsHolder extends RecyclerView.ViewHolder
                implements View.OnClickListener {

            private View viewStatus;
            private TextView textName, textDuration, textPosted, textAmount;
            private ImageView imgDp;
            private ItemClickListener itemClickListener;

            public PreviousBidsHolder(View view) {
                super(view);
                viewStatus = view.findViewById(R.id.status);
                textName = (TextView) view.findViewById(R.id.text_name);
                textDuration = (TextView) view.findViewById(R.id.text_duration);
                textPosted = (TextView) view.findViewById(R.id.text_posted);
                textAmount = (TextView) view.findViewById(R.id.text_amount);
                imgDp = (ImageView) view.findViewById(R.id.img_dp);
                view.setOnClickListener(this);
            }

            public void setClickListener(ItemClickListener itemClickListener) {
                this.itemClickListener = itemClickListener;
            }

            @Override
            public void onClick(View view) {
                itemClickListener.onClick(view, getAdapterPosition(), false);
            }
        }
    }
}
