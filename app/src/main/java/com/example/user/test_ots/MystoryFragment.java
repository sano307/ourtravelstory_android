package com.example.user.test_ots;

import java.io.File;
import java.lang.String;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidquery.AQuery;
import com.loopj.android.http.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MystoryFragment extends Fragment {
    private final static String process_info = "Process : ";

//  public static ArrayList<String> mPlaces = new ArrayList<String>();
//  public static ArrayList<Bitmap> mPlacePictures = new ArrayList<Bitmap>();
    public static ArrayList<MystoryModel> mystory_data = new ArrayList<>();
    public static FragmentTransaction transaction;

    protected AQuery aq;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.i(process_info, "Mystory 프래그먼트 뷰를 생성했습니다.");
        transaction = getActivity().getSupportFragmentManager().beginTransaction();

        RecyclerView recyclerView = (RecyclerView) inflater.inflate(R.layout.recycler_view, container, false);
        ContentAdapter adapter = new ContentAdapter(recyclerView.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setHasFixedSize(true);

        int tilePadding = getResources().getDimensionPixelSize(R.dimen.tile_padding);
        recyclerView.setPadding(tilePadding, tilePadding, tilePadding, tilePadding);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        aq = new AQuery(getActivity(), recyclerView);

        return recyclerView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        Log.i(process_info, "Mystory의 액티비티를 생성했습니다.");
        super.onActivityCreated(savedInstanceState);
    }

    // ViewHolder 클래스 안에는 각 컴포넌트 View를 넣어주면 된다.
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView mystory_picture;
        public TextView mystory_title;
        public static FragmentTransaction fm;

        public ViewHolder(LayoutInflater inflater, ViewGroup parent) {
            super(inflater.inflate(R.layout.fragment_mystory, parent, false));
            mystory_picture = (ImageView) itemView.findViewById(R.id.mystory_picture);
            mystory_title = (TextView) itemView.findViewById(R.id.mystory_title);
        }
    }

    public static class ContentAdapter extends RecyclerView.Adapter<ViewHolder> {
        private static int LENGTH;

        String[] allowedContentTypes = new String[] { "image/png", "image/jpeg" };

        public ContentAdapter(Context context) {
            UrlRestClient.get("myStory/1", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray response) {
                    super.onSuccess(statusCode, headers, response);

                    final BitmapFactory.Options options = new BitmapFactory.Options();
                    options.inSampleSize = 4;
                    LENGTH = response.length();
                    for (int iCount = 0; iCount < response.length(); iCount++) {
                        try {
                            JSONObject obj = response.getJSONObject(iCount);
;
                            final int story_idx = obj.getInt("StoryIdx");
                            final String story_name = obj.getString("StoryName");
                            final String story_represent_image_name = obj.getString("StoryRepresentImage");
                            final String story_represent_image_ext = obj.getString("StoryRepresentImageExt");
                            final String story_represent_image = obj.getString("StoryRepresentImage") + "." + obj.getString("StoryRepresentImageExt");

                            FileDownloadClient.download(story_represent_image, null, new BinaryHttpResponseHandler(allowedContentTypes) {
                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] binaryData) {
                                    System.out.println(story_name);
                                    Bitmap story_represent_image = BitmapFactory.decodeByteArray(binaryData, 0, binaryData.length);
                                    story_represent_image = Bitmap.createScaledBitmap(story_represent_image, 300, 300, true);
                                    //mPlacePictures.add(resized);
                                    mystory_data.add(new MystoryModel(story_idx, story_name, story_represent_image_name, story_represent_image_ext, story_represent_image));
                                    notifyDataSetChanged();
                                }

                                @Override
                                public void onFailure(int statusCode, Header[] headers, byte[] binaryData, Throwable error) {}
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, JSONArray errorResponse) {
                    super.onFailure(statusCode, headers, throwable, errorResponse);
                }
            });

//            Resources resources = context.getResources();
//            mPlaces = resources.getStringArray(R.array.places);
//            TypedArray a = resources.obtainTypedArray(R.array.places_picture);
//            mPlacePictures = new Drawable[a.length()];
//            for (int i = 0; i < mPlacePictures.length; i++) {
//                mPlacePictures[i] = a.getDrawable(i);
//            }
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ViewHolder(LayoutInflater.from(parent.getContext()), parent);
        }

        @Override
        public void onBindViewHolder(ViewHolder holder, final int position) {
            Log.i(process_info, "position = " + position);
            // Log.i(process_info, "imageName = " + mystory_data.get(position).getStoryRepresentImageName());
            // Log.i(process_info, "imageExt = " + mystory_data.get(position).getStoryRepresentImageExt());
            // holder.picture.setImageDrawable(mPlacePictures[position % mPlacePictures.length]);

            holder.mystory_title.setText(mystory_data.get(position % mystory_data.size()).getStoryName());
            holder.mystory_picture.setImageBitmap(mystory_data.get(position % mystory_data.size()).getStoryRepresentImage());
            holder.mystory_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Context context = v.getContext();
                    Intent intent = new Intent(context, MystoryDetailActivity.class);
                    intent.putExtra("StoryName", mystory_data.get(position).getStoryName());
                    intent.putExtra("StoryRepresentImage", mystory_data.get(position).getStoryRepresentImage());
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return LENGTH;
        }
    }
}

class MystoryModel {
    private int story_idx;
    private String story_name;
    private String story_represent_image_name;
    private String story_represent_image_ext;

    private Bitmap story_represent_image;

    public MystoryModel(int story_idx, String story_name, String story_represent_image_name, String story_represent_image_ext, Bitmap story_represent_image) {
        this.story_idx = story_idx;
        this.story_name = story_name;
        this.story_represent_image_name = story_represent_image_name;
        this.story_represent_image_ext = story_represent_image_ext;
        this.story_represent_image = story_represent_image;
    }

    public void setStoryRepresentImage(Bitmap story_represent_image) {
        this.story_represent_image = story_represent_image;
    }

    public int getStoryIdx() { return story_idx; }
    public String getStoryName() { return story_name; }
    public String getStoryRepresentImageName() { return story_represent_image_name; }
    public String getStoryRepresentImageExt() { return story_represent_image_ext; }
    public Bitmap getStoryRepresentImage() { return story_represent_image; }
}