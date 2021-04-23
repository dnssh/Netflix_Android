package com.example.devapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.time.ZonedDateTime;

public class Movie {
    public String id;
    public String type;
    public String imgurl;
    public String title;
    public String date;
    public String rating;

    public Movie(String id, String type, String imgurl, String title, String date, String rating){
        this.id=id;
        this.type=type;
        this.imgurl = imgurl;
        this.title=title;
        this.date=date;
        this.rating=rating;
    }

    public String toMyString() {
        return "{ \"id\": \"" + id + "\", \"type\": \"" + type + "\", \"imgurl\": \""
                + imgurl + "\", \"title\": \""+title+"\", \"date\": \""
                + date + "\", \"rating\": \""+rating+"\" }";
    }

//    public static Movie toMovie(String movie) throws JSONException {
//        JSONObject artcle = new JSONObject(movie);
//        String title = artcle.getString("title");
//        String section = artcle.getString("section");
//        String thumbnail = artcle.getString("thumbnail");
//        String articleId = artcle.getString("articleId");
//        //ZonedDateTime publicationDate = ZonedDateTime.parse(artcle.getString("publicationDate"));
//        String webUrl = artcle.getString("webUrl");
//        return new Movie(title, section, thumbnail, articleId, webUrl);
//    }
}
