package com.hmn.youtube.downloader;

import android.content.Context;
import android.util.SparseArray;

import androidx.annotation.Nullable;

@Deprecated
public abstract class YouTubeUriExtractor extends YouTubeExtractor {

    public YouTubeUriExtractor(Context con) {
        super(con);
    }


    @Override
    protected void onExtractionComplete(SparseArray<YtFile> ytFiles, @Nullable VideoMeta videoMeta) {
        assert videoMeta != null;
        onUrisAvailable(videoMeta.getVideoId(), videoMeta.getTitle(), ytFiles);
    }

    public abstract void onUrisAvailable(String videoId, String videoTitle, SparseArray<YtFile> ytFiles);
}
