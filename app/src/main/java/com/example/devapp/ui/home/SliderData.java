package com.example.devapp.ui.home;

public class SliderData {


        // image url is used to
        // store the url of image
        private String imgUrl,id,media;

        // Constructor method.
        public SliderData(String id,String media,String imgUrl) {
            this.imgUrl = imgUrl;
            this.id = id;
            this.media = media;
        }

        // Getter method
        public String getImgUrl() {
            return imgUrl;
        }

        public String getId() {
            return id;
        }
        public String getMedia() {
            return media;
        }

        // Setter method
        public void setImgUrl(String imgUrl) {
            this.imgUrl = imgUrl;
        }

}
