package ti.iam.ifi.polytech.unice.myapplication;

import android.app.Activity;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends ArrayAdapter<Integer> {

    private static LayoutInflater inflater = null;
    public List<Integer> selectedImages;
    private Activity context;
    private Integer[] imgs;
    private Integer[] ids;
    private boolean[] states;

    public CustomAdapter(Activity context, Integer[] ids, Integer[] imgs) {
        super(context, R.layout.image_list_view, ids);

        Log.e("nbelem", "construction de Customadapter: ");

        this.context = context;
        this.ids = ids;
        this.imgs = imgs;

        states = new boolean[ids.length];
        selectedImages = new ArrayList<Integer>();
    }

    @Override
    public int getCount() {
        return imgs.length;
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        final View rowView = inflater.inflate(R.layout.image_list_view, null, true);
        if (states[position]) rowView.setBackgroundColor(Color.GREEN);

        ImageView imageView = (ImageView) rowView.findViewById(R.id.imageView1);

        imageView.setImageResource(imgs[position]);

        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!selectedImages.contains(ids[position])) {
                    selectedImages.add(ids[position]);
                    rowView.setBackgroundColor(Color.GREEN);
                    states[position] = true;
                    rowView.invalidate();
                } else {
                    selectedImages.remove(ids[position]);
                    rowView.setBackgroundColor(Color.RED);
                    states[position] = false;
                    rowView.invalidate();
                }
            }
        });

        return rowView;
    }
}
