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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import in.codehex.facilis.app.AppController;
import in.codehex.facilis.app.Config;
import in.codehex.facilis.helper.CircleTransform;
import in.codehex.facilis.model.ActiveBidItem;


/**
 * A fragment that is used to display active bids list in the {@link SellerActivity} class.
 */
public class ActiveBidsFragment extends Fragment {

    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    List<ActiveBidItem> mActiveBidItemList;
    ActiveBidsAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mCount = 0;
    boolean isEnded = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int previousTotal = 0;
    boolean loading = true;
    int visibleThreshold = 5;

    public ActiveBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_active_bids, container, false);

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.active_bid_list);

        mActiveBidItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ActiveBidsAdapter(getContext(), mActiveBidItemList);
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
     * Fetch the active bid list from the server.
     */
    private void processBids() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_USER, userPreferences.getInt(Config.KEY_PREF_USER_ID, 0));
            jsonObject.put(Config.KEY_API_START, mCount);
            jsonObject.put(Config.KEY_API_END, mCount + 10);
            jsonObject.put(Config.KEY_API_FILTER_BY, "");
            jsonObject.put(Config.KEY_API_FILTER_QUERY, "");
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_ACTIVE_BIDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mCount == 0) {
                    mActiveBidItemList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshLayout.setRefreshing(false);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray jsonArray = responseObject.getJSONArray(Config.KEY_API_ACTIVE_BIDS);
                    if (!isEnded) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt(Config.KEY_API_ID);
                            int bidCost = object.getInt(Config.KEY_API_BID_COST);
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
                            String postedByUserImage =
                                    postedByObject.getString(Config.KEY_API_USER_IMAGE);
                            String postedDate =
                                    getPostedDate(orderObject.getString(Config.KEY_API_POSTED_DATE));
                            int orderRating = orderObject.getInt(Config.KEY_API_RATING);
                            boolean creditFacility =
                                    orderObject.getBoolean(Config.KEY_API_CREDIT_FACILITY);
                            int orderCreditPeriod = orderObject.getInt(Config.KEY_API_CREDIT_PERIOD);
                            int orderItemCount = orderObject.getInt(Config.KEY_API_ITEM_COUNT);
                            int orderPreviousRecord = orderObject.getInt(Config.KEY_API_PREVIOUS_RECORD);
                            int orderColorIndicator = orderObject.getInt(Config.KEY_API_COLOR_INDICATOR);
                            int statusColor;
                            switch (orderColorIndicator) {
                                case 0:
                                    statusColor = R.color.status_red;
                                    break;
                                case 1:
                                    statusColor = R.color.status_yellow;
                                    break;
                                case 2:
                                    statusColor = R.color.status_green;
                                    break;
                                default:
                                    statusColor = R.color.primary;
                            }
                            String creditStatus;
                            if (creditFacility)
                                creditStatus = "Credit";
                            else creditStatus = "No Credit";
                            mActiveBidItemList.add(new ActiveBidItem(id, bidCost, orderId,
                                    orderOrderId, postedById, orderRating, orderCreditPeriod,
                                    orderPreviousRecord, orderItemCount, orderColorIndicator,
                                    statusColor, postedByFirstName, postedByLastName,
                                    postedByUserImage, postedDate, creditStatus, creditFacility));
                            mAdapter.notifyItemInserted(mCount + i);
                        }
                        mCount = mCount + 10;
                    }
                    if (jsonArray.length() < 10)
                        isEnded = true;
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

        AppController.getInstance().addToRequestQueue(stringRequest, "active_bids");
    }

    /**
     * Get the datetime string value and converts it to the format DD, MON YEAR.
     *
     * @param postedDate the original string got from the server
     * @return formatted date string
     */
    private String getPostedDate(String postedDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.getDefault()).parse(postedDate);
            return String.format(Locale.getDefault(), "%td, %tb %tY", date, date, date);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Date parse error - " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return postedDate;
    }

    /**
     * View adapter for the recycler view of the active bids list item.
     */
    private class ActiveBidsAdapter
            extends RecyclerView.Adapter<ActiveBidsAdapter.ActiveBidsHolder> {

        Context context;
        List<ActiveBidItem> mActiveBidItemList;

        public ActiveBidsAdapter(Context context, List<ActiveBidItem> mActiveBidItemList) {
            this.context = context;
            this.mActiveBidItemList = mActiveBidItemList;
        }

        @Override
        public ActiveBidsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_active_bids, parent, false);
            return new ActiveBidsHolder(view);
        }

        @Override
        public void onBindViewHolder(ActiveBidsHolder holder, int position) {
            final ActiveBidItem activeBidItem = mActiveBidItemList.get(position);
            holder.viewOrderStatus.setBackgroundResource(activeBidItem.getStatusColor());
            holder.textName.setText(getString(R.string.text_name,
                    activeBidItem.getPostedByFirstName(), activeBidItem.getPostedByLastName()));
            holder.textBidAmount.setText(getString(R.string.text_bid_amount,
                    activeBidItem.getBidCost()));
            holder.textPosted.setText(activeBidItem.getOrderPostedDate());
            holder.ratingBuyer.setRating((float) activeBidItem.getOrderRating());
            holder.textPreviousRecords.setText(getString(R.string.text_previous_records,
                    activeBidItem.getOrderPreviousRecord()));
            holder.textPayment.setText(getString(R.string.text_payment,
                    activeBidItem.getOrderCreditPeriod(), activeBidItem.getCreditStatus()));
            holder.textTotalItems.setText(getString(R.string.text_total_items,
                    activeBidItem.getOrderItemCount()));

            Glide.with(context).load(activeBidItem.getPostedByUserImage())
                    .placeholder(R.drawable.ic_person_gray)
                    .transform(new CircleTransform(context)).into(holder.imgDp);

            holder.btnViewItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((SellerActivity) getActivity()).showOrderedItems(Config.KEY_FRAGMENT_ACTIVE,
                            activeBidItem.getOrderId());
                }
            });
        }

        @Override
        public int getItemCount() {
            return mActiveBidItemList.size();
        }

        protected class ActiveBidsHolder extends RecyclerView.ViewHolder {

            private View viewOrderStatus;
            private TextView textName, textBidAmount, textPosted, textPreviousRecords,
                    textPayment, textTotalItems;
            private RatingBar ratingBuyer;
            private ImageView imgDp;
            private Button btnViewItems;

            public ActiveBidsHolder(View view) {
                super(view);
                viewOrderStatus = view.findViewById(R.id.view_order_status);
                textName = (TextView) view.findViewById(R.id.text_name);
                textBidAmount = (TextView) view.findViewById(R.id.text_bid_amount);
                textPosted = (TextView) view.findViewById(R.id.text_posted);
                textPreviousRecords = (TextView) view.findViewById(R.id.text_previous_records);
                textPayment = (TextView) view.findViewById(R.id.text_payment);
                textTotalItems = (TextView) view.findViewById(R.id.text_total_items);
                ratingBuyer = (RatingBar) view.findViewById(R.id.rating_buyer);
                btnViewItems = (Button) view.findViewById(R.id.btn_view_items);
                imgDp = (ImageView) view.findViewById(R.id.img_dp);
            }
        }
    }
}
