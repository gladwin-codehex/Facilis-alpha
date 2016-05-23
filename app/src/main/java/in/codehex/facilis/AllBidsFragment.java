package in.codehex.facilis;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import in.codehex.facilis.helper.DividerItemDecoration;
import in.codehex.facilis.model.AllBidItem;


/**
 * A fragment that is used to display bid list for the {@link CurrentOrdersFragment} class.
 */
public class AllBidsFragment extends Fragment {

    RecyclerView mRecyclerView;
    List<AllBidItem> mAllBidItemList;
    AllBidsAdapter mAdapter;
    SharedPreferences userPreferences;
    LinearLayoutManager mLayoutManager;
    ProgressDialog mProgressDialog;
    int mOrderId;
    int mCount = 0;
    boolean isEnded = false;
    int firstVisibleItem, visibleItemCount, totalItemCount;
    int previousTotal = 0;
    boolean loading = true;
    int visibleThreshold = 5;

    public AllBidsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_bids, container, false);

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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.all_bids_list);

        mAllBidItemList = new ArrayList<>();
        mProgressDialog = new ProgressDialog(getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        mAdapter = new AllBidsAdapter(getContext(), mAllBidItemList);
        userPreferences = getActivity().getSharedPreferences(Config.PREF_USER,
                Context.MODE_PRIVATE);
    }

    /**
     * Implement and manipulate the objects.
     */
    private void prepareObjects() {
        Bundle bundle = getArguments();
        // TODO: handle null value of arguments
        if (bundle != null) {
            mOrderId = bundle.getInt(Config.KEY_BUNDLE_ORDER_ID);
        }

        mProgressDialog.setMessage("Accepting bid..");

        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getContext(),
                DividerItemDecoration.VERTICAL_LIST));

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

        processBids();
    }

    /**
     * Fetch the all the bid list from the server.
     */
    private void processBids() {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_USER, userPreferences.getInt(Config.KEY_PREF_USER_ID, 0));
            jsonObject.put(Config.KEY_API_ORDER, mOrderId);
            jsonObject.put(Config.KEY_API_FILTER_QUERY, ""); // TODO: use filter query
            jsonObject.put(Config.KEY_API_FILTER_BY, ""); // TODO: use filter by
            jsonObject.put(Config.KEY_API_START, mCount);
            jsonObject.put(Config.KEY_API_END, mCount + 10);
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_ALL_BIDS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                if (mCount == 0) {
                    mAllBidItemList.clear();
                    mAdapter.notifyDataSetChanged();
                }
                try {
                    JSONObject bidsObjects = new JSONObject(response);
                    JSONArray jsonArray = bidsObjects.getJSONArray(Config.KEY_API_BID_DETAILS);
                    if (!isEnded) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            int id = object.getInt(Config.KEY_API_ID);
                            int order = object.getInt(Config.KEY_API_ORDER);
                            JSONObject bidByObject = object.getJSONObject(Config.KEY_API_BID_BY);
                            int bidById = bidByObject.getInt(Config.KEY_API_ID);
                            String bidByFirstName =
                                    bidByObject.getString(Config.KEY_API_FIRST_NAME);
                            String bidByLastName =
                                    bidByObject.getString(Config.KEY_API_LAST_NAME);
                            String bidByUserImage =
                                    bidByObject.getString(Config.KEY_API_USER_IMAGE);
                            String bidTime = getBidDate(object.getString(Config.KEY_API_BID_TIME));
                            int bidCost = object.getInt(Config.KEY_API_BID_COST);
                            mAllBidItemList.add(new AllBidItem(id, order, bidById, bidCost,
                                    bidByFirstName, bidByLastName, bidByUserImage, bidTime));
                            mAdapter.notifyItemInserted(i);
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

        AppController.getInstance().addToRequestQueue(stringRequest, "view_all_bids");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * Send the request to the server for the accept bid json object.
     * @param allBidItem an object which contains all the bid details.
     */
    private void acceptBid(AllBidItem allBidItem) {
        final JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put(Config.KEY_API_USER, userPreferences.getInt(Config.KEY_PREF_USER_ID, 0));
            jsonObject.put(Config.KEY_API_ORDER, allBidItem.getOrder());
            jsonObject.put(Config.KEY_API_BID, allBidItem.getId());
        } catch (JSONException e) {
            // TODO: remove toast
            Toast.makeText(getContext(),
                    "Error occurred while generating data - "
                            + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
        showProgressDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST,
                Config.API_ACCEPT_BID, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressDialog();
                try {
                    JSONObject responseObject = new JSONObject(response);
                    String message = responseObject.getString(Config.KEY_API_MESSAGE);
                    Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();

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
                hideProgressDialog();
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

        AppController.getInstance().addToRequestQueue(stringRequest, "accept_bid");
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(50000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    /**
     * Get the datetime string value and converts it to the format DD, MON YEAR.
     *
     * @param bidDate the original string got from the server
     * @return formatted date string
     */
    private String getBidDate(String bidDate) {
        try {
            Date date = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'",
                    Locale.getDefault()).parse(bidDate);
            return String.format(Locale.getDefault(), "%td, %tb %tY", date, date, date);
        } catch (ParseException e) {
            Toast.makeText(getContext(), "Date parse error - " + e.getMessage(),
                    Toast.LENGTH_SHORT).show();
        }
        return bidDate;
    }

    /**
     * Display an alert when accept bid button is clicked.
     *
     * @param allBidItem an object which contains all the bid details.
     */
    private void showAlertAcceptBid(final AllBidItem allBidItem) {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(getContext());
        alertDialog.setTitle("Accept Bid");
        alertDialog.setMessage("Are you sure you want to accept this bid?");
        alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                acceptBid(allBidItem);
            }
        });
        alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.show();
    }

    /**
     * Display the progress dialog if the dialog is not being shown.
     */
    private void showProgressDialog() {
        if (!mProgressDialog.isShowing()) {
            mProgressDialog.show();
        }
    }

    /**
     * Hide the progress dialog if the dialog is being shown.
     */
    private void hideProgressDialog() {
        if (mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    /**
     * View adapter for the recycler view of the ordered item list.
     */
    private class AllBidsAdapter
            extends RecyclerView.Adapter<AllBidsAdapter.AllBidsHolder> {

        Context context;
        List<AllBidItem> mAllBidItemList;

        public AllBidsAdapter(Context context, List<AllBidItem> mAllBidItemList) {
            this.context = context;
            this.mAllBidItemList = mAllBidItemList;
        }

        @Override
        public AllBidsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_all_bids, parent, false);
            return new AllBidsHolder(view);
        }

        @Override
        public void onBindViewHolder(AllBidsHolder holder, int position) {
            final AllBidItem allBidItem = mAllBidItemList.get(position);
            holder.textName.setText(getString(R.string.text_name,
                    allBidItem.getBidByFirstName(), allBidItem.getBidByLastName()));
            holder.textBidAmount.setText(getString(R.string.text_bid_amount,
                    allBidItem.getBidCost()));
            holder.textBidDate.setText(allBidItem.getBidTime());

            Glide.with(context).load(allBidItem.getBidByUserImage())
                    .placeholder(R.drawable.ic_person_gray)
                    .transform(new CircleTransform(context)).into(holder.imgDp);

            holder.btnAcceptBid.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showAlertAcceptBid(allBidItem);
                }
            });
        }

        @Override
        public int getItemCount() {
            return mAllBidItemList.size();
        }

        protected class AllBidsHolder extends RecyclerView.ViewHolder {

            private TextView textName, textBidAmount, textBidDate;
            private ImageView imgDp;
            private Button btnAcceptBid;

            public AllBidsHolder(View view) {
                super(view);
                textName = (TextView) view.findViewById(R.id.text_name);
                textBidAmount = (TextView) view.findViewById(R.id.text_bid_amount);
                textBidDate = (TextView) view.findViewById(R.id.text_bid_date);
                imgDp = (ImageView) view.findViewById(R.id.img_dp);
                btnAcceptBid = (Button) view.findViewById(R.id.btn_acccept_bid);
            }
        }
    }
}
