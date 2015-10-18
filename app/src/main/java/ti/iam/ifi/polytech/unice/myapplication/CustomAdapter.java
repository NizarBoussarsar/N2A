package ti.iam.ifi.polytech.unice.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import java.util.logging.Logger;

public class CustomAdapter extends BaseAdapter {

    Context context;
    int[] imgs;

    private static LayoutInflater inflater = null;

    public CustomAdapter(MainActivity mainActivity, int[] images) {
        context = mainActivity;
        imgs = images;
        inflater = (LayoutInflater) context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return imgs.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    public class Holder {
        ImageView img;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = inflater.inflate(R.layout.image_list_view, null);
        Holder holder = new Holder();
        holder.img = (ImageView) rowView.findViewById(R.id.imageView1);
        holder.img.setImageResource(imgs[position]);
        return rowView;
    }
}
