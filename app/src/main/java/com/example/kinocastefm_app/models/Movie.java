package com.example.kinocastefm_app.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class Movie implements Parcelable {
    public String imdbId;
    public String title;
    public String plot;
    public String released;
    public String genre;
    public String director;
    public String writer;
    public String actors;
    public String poster;
    public String rating;
    public String year;
    public String duraton;

    public Movie(){}

    public Movie(String imdbId, String title, String plot, String released, String genre, String director, String writer, String actors, String poster, String rating, String duration, String year) {
        this.imdbId = imdbId;
        this.title = title;
        this.plot = plot;
        this.released = released;
        this.genre = genre;
        this.director = director;
        this.writer = writer;
        this.actors = actors;
        this.poster = poster;
        this.rating = rating;
        this.duraton=duration;
        this.year=year;
    }

    protected Movie(Parcel in) {
        imdbId = in.readString();
        title = in.readString();
        plot = in.readString();
        released = in.readString();
        genre = in.readString();
        director = in.readString();
        writer = in.readString();
        actors = in.readString();
        poster = in.readString();
        rating = in.readString();
        year = in.readString();
        duraton = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(imdbId);
        dest.writeString(title);
        dest.writeString(plot);
        dest.writeString(released);
        dest.writeString(genre);
        dest.writeString(director);
        dest.writeString(writer);
        dest.writeString(actors);
        dest.writeString(poster);
        dest.writeString(rating);
        dest.writeString(year);
        dest.writeString(duraton);
    }
}
