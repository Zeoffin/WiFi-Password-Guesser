package lv.slysoft.wifiguesser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class adapter  extends ArrayAdapter<String>{

    public adapter (Context context, ArrayList<String> groceries) {
        super(context, R.layout.list_view_format, groceries);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater = LayoutInflater.from(getContext());

        //Recycle views
        View view = convertView;
        if(view == null){
            view = inflater.inflate(R.layout.list_view_format, parent, false);
        }

        String item = getItem(position);
        TextView textView = (TextView) view.findViewById(R.id.list_content);

        textView.setText(item);

        return view;

    }

}
