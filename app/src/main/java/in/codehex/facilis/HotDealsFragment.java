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
import in.codehex.facilis.model.OrderItem;


/**
 * A fragment that is used to display hot deals list in the {@link ViewOrdersFragment} class.
 */
public class HotDealsFragment extends Fragment {

    SwipeRefreshLayout mRefreshLayout;
    RecyclerView mRecyclerView;
    List<OrderItem> mOrderItemList;
    ViewOrdersAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    int mCount = 0;
    boolean isEnded = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int previousTotal = 0;
    boolean loading = true;
    int visibleThreshold = 5;

    public HotDealsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_hot_deals, container, false);

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.hot_deal_list);

        mOrderItemList = new ArrayList<>();
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new ViewOrdersAdapter(getContext(), mOrderItemList);
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
                    processOrders();
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
                processOrders();
            }
        });
        mRefreshLayout.post(new Runnable() {
            @Override
            public void run() {
                resetData();
                mRefreshLayout.setRefreshing(true);
                processOrders();
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
     * Fetch the hot deals list from the server.
     */
    private void processOrders() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_USER, userPreferences.getInt(Config.KEY_PREF_USER_ID, 0));
            jsonObject.put(Config.KEY_API_START, mCount);
            jsonObject.put(Config.KEY_API_END, mCount + 10);
            jsonObject.put(Config.KEY_API_FILTER_BY, "hot_deals");
            jsonObject.put(Config.KEY_API_FILTER_QUERY, "");
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
                    mOrderItemList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                mRefreshLayout.setRefreshing(false);
                try {
                    JSONObject responseObject = new JSONObject(response);
                    JSONArray jsonArray = responseObject.getJSONArray(Config.KEY_API_VIEW_ORDERS);
                    if (!isEnded) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt(Config.KEY_API_ID);
                            int orderId = object.getInt(Config.KEY_API_ORDER_ID);
                            JSONObject postedByObject = object
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
                                    getPostedDate(object.getString(Config.KEY_API_POSTED_DATE));
                            int rating = object.getInt(Config.KEY_API_RATING);
                            boolean creditFacility =
                                    object.getBoolean(Config.KEY_API_CREDIT_FACILITY);
                            int creditPeriod = object.getInt(Config.KEY_API_CREDIT_PERIOD);
                            String biddingDuration =
                                    object.getString(Config.KEY_API_BIDDING_DURATION);
                            int itemCount = object.getInt(Config.KEY_API_ITEM_COUNT);
                            int previousRecord = object.getInt(Config.KEY_API_PREVIOUS_RECORD);
                            int colorIndicator = object.getInt(Config.KEY_API_COLOR_INDICATOR);
                            int statusColor;
                            switch (colorIndicator) {
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
                            mOrderItemList.add(new OrderItem(id, orderId, postedById, rating,
                                    creditPeriod, itemCount, previousRecord, colorIndicator,
                                    statusColor, postedByFirstName, postedByLastName,
                                    postedByUserImage, postedDate, biddingDuration,
                                    creditStatus, creditFacility));
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

        AppController.getInstance().addToRequestQueue(stringRequest, "hot_deals");
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
     * View adapter for the recycler view of the view orders list item.
     */
    private class ViewOrdersAdapter
            extends RecyclerView.Adapter<ViewOrdersAdapter.ViewOrdersHolder> {

        Context context;
        List<OrderItem> mOrderItemList;

        public ViewOrdersAdapter(Context context, List<OrderItem> mOrderItemList) {
            this.context = context;
            this.mOrderItemList = mOrderItemList;
        }

        @Override
        public ViewOrdersHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_order, parent, false);
            return new ViewOrdersHolder(view);
        }

        @Override
        public void onBindViewHolder(ViewOrdersHolder holder, int position) {
            final OrderItem orderItem = mOrderItemList.get(position);
            holder.viewOrderStatus.setBackgroundResource(orderItem.getStatusColor());
            holder.textName.setText(getString(R.string.text_name,
                    orderItem.getPostedByFirstName(), orderItem.getPostedByLastName()));
            holder.textDuration.setText(orderItem.getBiddingDuration());
            holder.textPosted.setText(orderItem.getPostedDate());
            holder.ratingBuyer.setRating((float) orderItem.getRating());
            holder.textPreviousRecords.setText(getString(R.string.text_previous_records,
                    orderItem.getPreviousRecord()));
            holder.textPayment.setText(getString(R.string.text_payment,
                    orderItem.getCreditPeriod(), orderItem.getCreditStatus()));
            holder.textTotalItems.setText(getString(R.string.text_total_items,
                    orderItem.getItemCount()));

            Glide.with(context).load(orderItem.getPostedByUserImage())
                    .placeholder(R.drawable.ic_person_gray)
                    .transform(new CircleTransform(context)).into(holder.imgDp);

            holder.btnViewItems.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: handle button click events
                }
            });

            holder.btnPlaceBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // TODO: handle button click events
                }
            });
        }

        @Override
        public int getItemCount() {
            return mOrderItemList.size();
        }

        protected class ViewOrdersHolder extends RecyclerView.ViewHolder {

            private View viewOrderStatus;
            private TextView textName, textDuration, textPosted, textPreviousRecords,
                    textPayment, textTotalItems;
            private RatingBar ratingBuyer;
            private ImageView imgDp;
            private Button btnViewItems, btnPlaceBid;

            public ViewOrdersHolder(View view) {
                super(view);
                viewOrderStatus = view.findViewById(R.id.view_order_status);
                textName = (TextView) view.findViewById(R.id.text_name);
                textDuration = (TextView) view.findViewById(R.id.text_duration);
                textPosted = (TextView) view.findViewById(R.id.text_posted);
                textPreviousRecords = (TextView) view.findViewById(R.id.text_previous_records);
                textPayment = (TextView) view.findViewById(R.id.text_payment);
                textTotalItems = (TextView) view.findViewById(R.id.text_total_items);
                ratingBuyer = (RatingBar) view.findViewById(R.id.rating_buyer);
                btnViewItems = (Button) view.findViewById(R.id.btn_view_items);
                btnPlaceBid = (Button) view.findViewById(R.id.btn_place_bid);
                imgDp = (ImageView) view.findViewById(R.id.img_dp);
            }
        }
    }
}
