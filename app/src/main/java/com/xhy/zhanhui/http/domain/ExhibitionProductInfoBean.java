package com.xhy.zhanhui.http.domain;

import com.aaron.aaronlibrary.bean.BaseBean;

import java.util.List;

/**
 * 展会-产品信息Bean
 * Created by Aaron on 2017/12/11 0001.
 */
public class ExhibitionProductInfoBean extends BaseBean {

    private Obj data;

    public Obj getData() {
        return data;
    }

    public void setData(Obj data) {
        this.data = data;
    }

    public class Obj {
        private String product_id;
        private String company_name;
        private String product_introduction;
        private String product_name;
        private String focus;
        private List<Video> videos;
        private List<Param> params;
        private List<Image1> images1;
        private List<Image2> images2;

        public String getFocus() {
            return focus;
        }

        public void setFocus(String focus) {
            this.focus = focus;
        }

        public String getCompany_name() {
            return company_name;
        }

        public void setCompany_name(String company_name) {
            this.company_name = company_name;
        }

        public String getProduct_id() {
            return product_id;
        }

        public void setProduct_id(String product_id) {
            this.product_id = product_id;
        }

        public String getProduct_introduction() {
            return product_introduction;
        }

        public void setProduct_introduction(String product_introduction) {
            this.product_introduction = product_introduction;
        }

        public String getProduct_name() {
            return product_name;
        }

        public void setProduct_name(String product_name) {
            this.product_name = product_name;
        }

        public List<Video> getVideos() {
            return videos;
        }

        public void setVideos(List<Video> videos) {
            this.videos = videos;
        }

        public List<Param> getParams() {
            return params;
        }

        public void setParams(List<Param> params) {
            this.params = params;
        }

        public List<Image1> getImages1() {
            return images1;
        }

        public void setImages1(List<Image1> images1) {
            this.images1 = images1;
        }

        public List<Image2> getImages2() {
            return images2;
        }

        public void setImages2(List<Image2> images2) {
            this.images2 = images2;
        }

        public class Video {
            private String video_url;

            public String getVideo_url() {
                return video_url;
            }

            public void setVideo_url(String video_url) {
                this.video_url = video_url;
            }
        }

        public class Param {
            private String product_param;

            public String getProduct_param() {
                return product_param;
            }

            public void setProduct_param(String product_param) {
                this.product_param = product_param;
            }
        }

        public class Image1 {
            private String image_url1;

            public String getImage_url1() {
                return image_url1;
            }

            public void setImage_url1(String image_url1) {
                this.image_url1 = image_url1;
            }
        }

        public class Image2 {
            private String image_url2;

            public String getImage_url2() {
                return image_url2;
            }

            public void setImage_url2(String image_url2) {
                this.image_url2 = image_url2;
            }
        }
    }
}
