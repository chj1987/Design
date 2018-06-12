package com.example.chj.design.video.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.chj.design.App;
import com.example.chj.design.MainActivity;
import com.example.chj.design.R;
import com.example.chj.design.model.entity.VideoItem;
import com.example.chj.design.widget.MediaController;
import com.pili.pldroid.player.AVOptions;
import com.pili.pldroid.player.PLOnInfoListener;
import com.pili.pldroid.player.widget.PLVideoTextureView;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ff on 2018/6/1.
 */

public class VideoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final int TYPE_HEAD = 0;
    private static final int TYPE_VIDEO = 1;

    /*
   * 推荐位数据
   */
    private ArrayList<Integer> images = new ArrayList<>();
    private Context mContext;
    private List<VideoItem> list;
    private LayoutInflater inflater;
    private App app;
    private ViewHolder mCurViewHolder;

    public VideoAdapter(Activity activity, List<VideoItem> list) {
        mContext = activity;
        this.list = list;
        inflater = LayoutInflater.from(activity);
        app = ((MainActivity) activity).app;
        images.add(R.mipmap.movie5);
        images.add(R.mipmap.movie6);
        images.add(R.mipmap.movie7);
        images.add(R.mipmap.movie8);
        images.add(R.mipmap.movie9);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEAD) {
            View view = inflater.inflate(R.layout.item_movie_head, parent, false);
            return new HeadHolder(view);
        } else {
            View view = inflater.inflate(R.layout.item_movie_video, parent, false);
            return new ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeadHolder) {
            ((HeadHolder) holder).banner.setPages(images, new MZHolderCreator<BannerViewHolder>() {
                @Override
                public BannerViewHolder createViewHolder() {
                    return new BannerViewHolder();
                }

            });
            ((HeadHolder) holder).banner.start();
        } else {
            ((ViewHolder) holder).videoPath = list.get(position - 1).getVideoPath();
            Glide.with(mContext)
                    .load(list.get(position - 1).getCoverPath())
                    .into(((ViewHolder) holder).coverImage);
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEAD;
        } else {
            return TYPE_VIDEO;
        }
    }

    @Override
    public void onViewDetachedFromWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof ViewHolder) {
            ((ViewHolder) holder).videoTextureView.pause();
            ((ViewHolder) holder).loadingView.setVisibility(View.GONE);
            ((ViewHolder) holder).coverImage.setVisibility(View.VISIBLE);
            ((ViewHolder) holder).coverStopPlay.setVisibility(View.VISIBLE);
        } else {
            ((HeadHolder) holder).banner.pause();
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        if (holder instanceof HeadHolder) {
            ((HeadHolder) holder).banner.start();
        }
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : (list.size() + 1);
    }

    public class HeadHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.banner)
        MZBannerView banner;

        public HeadHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.video_texture_view)
        PLVideoTextureView videoTextureView;
        @BindView(R.id.controller_stop_play)
        ImageButton controllerStopPlay;
        @BindView(R.id.controller_current_time)
        TextView controllerCurrentTime;
        @BindView(R.id.controller_progress_bar)
        SeekBar controllerProgressBar;
        @BindView(R.id.controller_end_time)
        TextView controllerEndTime;
        @BindView(R.id.media_controller)
        MediaController mediaController;
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
        @BindView(R.id.video_root)
        FrameLayout videoRoot;
        String videoPath;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            initSize();

            videoTextureView.setAVOptions(createAVOptions());
            videoTextureView.setBufferingIndicator(loadingView);
            videoTextureView.setMediaController(mediaController);
            videoTextureView.setTag(null);
            videoTextureView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
            videoTextureView.setLooping(false);

            videoTextureView.setOnInfoListener(new PLOnInfoListener() {
                @Override
                public void onInfo(int i, int i1) {
                    if (i == PLOnInfoListener.MEDIA_INFO_VIDEO_RENDERING_START) {
                        coverImage.setVisibility(View.GONE);
                        coverStopPlay.setVisibility(View.GONE);
                        mediaController.hide();
                    }
                }
            });
            coverImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    stopCurVideoView();
                    mCurViewHolder = ViewHolder.this;
                    startCurVideoView();
                }
            });
        }

        private void initSize() {
            app.setMLayoutParam(videoRoot, 1f, 0.56f);
            app.setMLayoutParam(coverImage, 1f, 0.56f);
        }
    }

    public static AVOptions createAVOptions() {
        AVOptions options = new AVOptions();
        // the unit of timeout is ms
        options.setInteger(AVOptions.KEY_PREPARE_TIMEOUT, 10 * 1000);
        options.setInteger(AVOptions.KEY_LIVE_STREAMING, 0);
        // 1 -> hw codec enable, 0 -> disable [recommended]
        options.setInteger(AVOptions.KEY_MEDIACODEC, AVOptions.MEDIA_CODEC_SW_DECODE);
        options.setInteger(AVOptions.KEY_PREFER_FORMAT, AVOptions.PREFER_FORMAT_MP4);
        boolean disableLog = false;
        options.setInteger(AVOptions.KEY_LOG_LEVEL, disableLog ? 5 : 0);
        return options;
    }

    public void stopCurVideoView() {
        if (mCurViewHolder != null) {
            resetConfig();
            mCurViewHolder.videoTextureView.stopPlayback();
            mCurViewHolder.loadingView.setVisibility(View.GONE);
            mCurViewHolder.coverImage.setVisibility(View.VISIBLE);
            mCurViewHolder.coverStopPlay.setVisibility(View.VISIBLE);
        }
    }

    private void startCurVideoView() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoTextureView.setVideoPath(mCurViewHolder.videoPath);
            mCurViewHolder.videoTextureView.start();
            mCurViewHolder.loadingView.setVisibility(View.VISIBLE);
            mCurViewHolder.coverStopPlay.setVisibility(View.GONE);
        }
    }

    private void resetConfig() {
        if (mCurViewHolder != null) {
            mCurViewHolder.videoTextureView.setRotation(0);
            mCurViewHolder.videoTextureView.setMirror(false);
            mCurViewHolder.videoTextureView.setDisplayAspectRatio(PLVideoTextureView.ASPECT_RATIO_PAVED_PARENT);
        }
    }

    private class BannerViewHolder implements MZViewHolder<Integer> {
        ImageView mImageView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.banner_image);
            return view;
        }

        @Override
        public void onBind(Context context, int position, Integer integer) {
            mImageView.setImageResource(integer);
        }
    }
}
