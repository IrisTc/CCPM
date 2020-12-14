package com.iris.ccpm.adapter;

        import android.content.Context;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.widget.ArrayAdapter;
        import android.widget.TextView;
        import com.iris.ccpm.R;

public class TaskDetailSpinnerAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private String [] mStringArray;
    private int[] mColorArray;
    private int[] mTextColorArray;
    public TaskDetailSpinnerAdapter(Context context, String[] stringArray, int[] colorArray, int[] textColorArray) {
        super(context, R.layout.task_spinner_item, stringArray);
        mColorArray=colorArray;
        mContext = context;
        mStringArray=stringArray;
        mTextColorArray=textColorArray;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(R.layout.task_spinner_item, parent, false);
        }
        TextView tv = (TextView) convertView.findViewById(R.id.spinnerItem);
        tv.setText(mStringArray[position]);
        tv.setBackgroundColor((mColorArray[position]));
        tv.setTextColor(mTextColorArray[position]);
        return convertView;
    }

}
