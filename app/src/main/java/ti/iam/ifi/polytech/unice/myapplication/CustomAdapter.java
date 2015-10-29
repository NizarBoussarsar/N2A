package ti.iam.ifi.polytech.unice.myapplication;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<Integer> {

    private static LayoutInflater inflater = null;
    Activity context;
    Integer[] imgs;
    Integer[] ids;

    public CustomAdapter(Activity context, Integer[] ids, Integer[] imgs) {
        super(context, R.layout.image_list_view, ids);
        this.context = context;
        this.ids = ids;
        this.imgs = imgs;
    }

    @Override
    public int getCount() {
        return imgs.length;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.image_list_view, null, true);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);
        TextView textView = (TextView) rowView.findViewById(R.id.textView);

        textView.setText("Number " + ids[position]);
        imageView.setImageResource(imgs[position]);
        return rowView;
    }
}
