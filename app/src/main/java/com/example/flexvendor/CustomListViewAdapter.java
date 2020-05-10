package com.example.flexvendor;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.List;

public class CustomListViewAdapter extends ArrayAdapter<Users> {

    private Context context;
    private List<String> getImageUrl;

    CustomListViewAdapter(Context context, int resourceId,
                          List<Users> items, List<String> getImageUrl) {
        super(context, resourceId, items);
        this.context = context;
        this.getImageUrl=getImageUrl;
    }

    @SuppressLint({"InflateParams", "SetTextI18n"})
    @NonNull
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        Users users=getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        final ViewHolder holder;
        if (convertView == null) {
            assert mInflater != null;
            convertView = mInflater.inflate(R.layout.list_slot, null);
            holder=new ViewHolder();
            holder.txtName=convertView.findViewById(R.id.tvUserName);
            holder.txtPhone=convertView.findViewById(R.id.tvUserPhone);
            holder.txtTimings=convertView.findViewById(R.id.tvTimings);
            holder.tvPaymentStatus=convertView.findViewById(R.id.tvPaymentStatus);
            holder.ivPaymentStatus=convertView.findViewById(R.id.imageViewPayment);
            holder.tvTransactionDateTime=convertView.findViewById(R.id.tvTransactionDate);
            holder.tvTransactionMoney=convertView.findViewById(R.id.tvTransactionMoney);
            holder.txtEmail=convertView.findViewById(R.id.invisibleEmail);
            holder.tvUpiId = convertView.findViewById(R.id.invisibleUpiId);
            holder.tvSlotId = convertView.findViewById(R.id.invisibleSlotId);
            holder.imageView=convertView.findViewById(R.id.ivImage);
            convertView.setTag(holder);
        } else
            holder=(ViewHolder) convertView.getTag();

        assert users != null;
        holder.txtName.setText(users.getName());
        holder.txtPhone.setText("+91-" + users.getPhone());
        holder.txtTimings.setText(users.getTimings());
        holder.txtEmail.setText(users.getEmail());

        Glide.with(context)
                .load(getImageUrl.get(position))
                .apply(RequestOptions.circleCropTransform())
                .into(holder.imageView);

        if(users.getTransactionDateTime().equals("null") && users.getTransactionMoney().equals("null"))
        {
            holder.tvPaymentStatus.setText("Payment Not Done");
            holder.ivPaymentStatus.setImageResource(R.drawable.ic_failed);
            holder.tvTransactionDateTime.setText("Please complete your payment");
            holder.tvTransactionMoney.setText("Tap here to make payment");
            holder.tvUpiId.setText(users.getUpiId());
            holder.tvSlotId.setText(users.getSlotId());
        }
        else if(users.isPaid()) {
            holder.tvPaymentStatus.setText("Payment Done");
            holder.ivPaymentStatus.setImageResource(R.drawable.ic_confirmed_tick);
            holder.tvTransactionDateTime.setText(users.getTransactionDateTime());
            holder.tvTransactionMoney.setText("\u20B9 "+users.getTransactionMoney());
            holder.tvUpiId.setText("");
            holder.tvSlotId.setText("");
        }

        return convertView;
    }

    /*private view holder class*/
    class ViewHolder {
        ImageView imageView;
        TextView txtEmail;
        TextView txtName;
        TextView txtPhone;
        TextView txtTimings;
        TextView tvPaymentStatus;
        TextView tvUpiId;
        ImageView ivPaymentStatus;
        TextView tvTransactionDateTime;
        TextView tvTransactionMoney;
        TextView tvSlotId;
    }


}
