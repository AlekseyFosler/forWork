package com.example.aleks.forwork;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;


/**
 * Created by aleks on 10.07.2016.
 */

public class FreeFragment extends Fragment {

    private Cursor cursor;
    private int columnIndex;

    public FreeFragment () {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_free, null);
        cursor = getActivity().getContentResolver().query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, null, null, null, null/*MediaStore.Images.Thumbnails._ID*/);
        columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Thumbnails._ID);

        GridView sdcardimage = (GridView) view.findViewById(R.id.gridview);
        ImageAdapter adapter=new ImageAdapter(getActivity());
        sdcardimage.setAdapter(adapter);

        return view;
    }
    private class ImageAdapter extends BaseAdapter {

        private Context context;

        public ImageAdapter(Context localContext) {

            context = localContext;

        }

        public int getCount() {

            return cursor.getCount();

        }

        public Object getItem(int position) {

            return position;

        }

        public long getItemId(int position) {

            return position;

        }

        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                holder.picturesView = new ImageView(context);
                convertView = getLayoutInflater(null).inflate(R.layout.fragment_free, parent, false);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            cursor.moveToPosition(position);
            int imageID = cursor.getInt(columnIndex);
            Uri uri = Uri.withAppendedPath(
                    MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, "" + imageID);
            holder.picturesView = (ImageView) convertView.findViewById(R.id.imageview);
            holder.picturesView.setImageURI(uri);
            holder.picturesView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            return convertView;

        }
        class ViewHolder {
            private ImageView picturesView;
        }
    }
}
