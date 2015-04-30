package apps.ssw555.com.itemcheck;

import android.app.Activity;
import android.app.LauncherActivity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by shreyas on 4/30/15.
 */
public class ItemAdapter extends ArrayAdapter<Item> {

    ArrayList<Item> mItems;
    Activity mContext;

    public ItemAdapter (Activity context, ArrayList<Item> items) {
        super(context,android.R.layout.simple_list_item_checked,items);
        mContext = context;
        mItems = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if(view == null)
            view = mContext.getLayoutInflater().inflate(android.R.layout.simple_list_item_checked, null);


        Item item = mItems.get(position);
        CheckedTextView checkedView = (CheckedTextView)view;
        checkedView.setText(item.getName());
        checkedView.setChecked(item.isCompleted());
        return convertView;
    }
}
