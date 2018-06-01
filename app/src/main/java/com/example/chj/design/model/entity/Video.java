package com.example.chj.design.model.entity;

import java.util.List;

/**
 * Created by ff on 2018/6/1.
 */

public class Video {

    /**
     * Items : [{"key":"movies/Sunset.mp4","hash":"lnHdafF4l4IkiWYxERfSN9qlX6C4","fsize":8841876,"putTime":15208361307083078,"mimeType":"video/mp4","type":0,"endUser":""},{"key":"movies/apple.mp4","hash":"lqm68Y4DEObT8AmAwBVUEsNxPoCQ","fsize":44593355,"putTime":15211044265892430,"mimeType":"video/mp4","type":0,"endUser":""},{"key":"movies/moon.mp4","hash":"FgE-32EHUutBp6_2ztW2VQ8gyqPY","fsize":2666497,"putTime":15208336261261425,"mimeType":"video/mp4","type":0,"endUser":""},{"key":"movies/qiniu.mp4","hash":"lhsawSRA9-0L8b0s-cXmojaMhGqn","fsize":25538648,"putTime":15203474482177600,"mimeType":"video/mp4","type":0,"endUser":""},{"key":"movies/snowday.mp4","hash":"lvfTCSpNR3t-hiJd0VqYzzK_sJN3","fsize":27058504,"putTime":15208355944118985,"mimeType":"video/mp4","type":0,"endUser":""}]
     * Marker :
     * HasNext : false
     */

    private String Marker;
    private boolean HasNext;
    private List<ItemsBean> Items;

    public String getMarker() {
        return Marker;
    }

    public void setMarker(String Marker) {
        this.Marker = Marker;
    }

    public boolean isHasNext() {
        return HasNext;
    }

    public void setHasNext(boolean HasNext) {
        this.HasNext = HasNext;
    }

    public List<ItemsBean> getItems() {
        return Items;
    }

    public void setItems(List<ItemsBean> Items) {
        this.Items = Items;
    }

    public static class ItemsBean {
        /**
         * key : movies/Sunset.mp4
         * hash : lnHdafF4l4IkiWYxERfSN9qlX6C4
         * fsize : 8841876
         * putTime : 15208361307083078
         * mimeType : video/mp4
         * type : 0
         * endUser :
         */

        private String key;
        private String hash;
        private int fsize;
        private long putTime;
        private String mimeType;
        private int type;
        private String endUser;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getHash() {
            return hash;
        }

        public void setHash(String hash) {
            this.hash = hash;
        }

        public int getFsize() {
            return fsize;
        }

        public void setFsize(int fsize) {
            this.fsize = fsize;
        }

        public long getPutTime() {
            return putTime;
        }

        public void setPutTime(long putTime) {
            this.putTime = putTime;
        }

        public String getMimeType() {
            return mimeType;
        }

        public void setMimeType(String mimeType) {
            this.mimeType = mimeType;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public String getEndUser() {
            return endUser;
        }

        public void setEndUser(String endUser) {
            this.endUser = endUser;
        }
    }
}
