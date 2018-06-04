package com.example.chj.design.video.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.chj.design.App;
import com.example.chj.design.MainActivity;
import com.example.chj.design.R;
import com.example.chj.design.model.entity.VideoItem;
import com.pili.pldroid.player.widget.PLVideoTextureView;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ff on 2018/6/1.
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private Context mContext;
    private List<VideoItem> list;
    private LayoutInflater inflater;
    private App app;

    public VideoAdapter(Activity activity, List<VideoItem> list) {
        mContext = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
        app = ((MainActivity) activity).app;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.item_movie_video, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_texture_view)
        PLVideoTextureView videoTextureView;
        @BindView(R.id.cover_image)
        ImageView coverImage;
        @BindView(R.id.cover_stop_play)
        ImageButton coverStopPlay;
        @BindView(R.id.loading_view)
        LinearLayout loadingView;
        @BindView(R.id.detail_text)
        TextView detailText;
        @BindView(R.id.name_text)
        TextView nameText;
        @BindView(R.id.ll_detail)
        LinearLayout llDetail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initSize();
        }

        private void initSize() {
            app.setMLayoutParam(videoTextureView, 1f, 0.56f);
            app.setMLayoutParam(llDetail, 1f, 0.06f);
        }
    }
}
