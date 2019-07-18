package com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.SerializedName;

@Entity(tableName = "tb_tvshow")
public class ResultsItemTvShow implements Parcelable {

    @ColumnInfo(name = "first_air_date")
    @SerializedName("first_air_date")
    private String firstAirDate;

    @ColumnInfo(name = "overview")
    @SerializedName("overview")
    private String overview;

    @ColumnInfo(name = "original_language")
    @SerializedName("original_language")
    private String originalLanguage;

    @ColumnInfo(name = "poster_path")
    @SerializedName("poster_path")
    private String posterPath;


    @ColumnInfo(name = "backdrop_path")
    @SerializedName("backdrop_path")
    private String backdropPath;

    @ColumnInfo(name = "original_name")
    @SerializedName("original_name")
    private String originalName;

    @ColumnInfo(name = "popularity")
    @SerializedName("popularity")
    private double popularity;

    @ColumnInfo(name = "vote_average")
    @SerializedName("vote_average")
    private double voteAverage;

    @ColumnInfo(name = "name")
    @SerializedName("name")
    private String name;

    @PrimaryKey
    @ColumnInfo(name = "id")
    @SerializedName("id")
    @NonNull private int id;

    @ColumnInfo(name = "vote_count")
    @SerializedName("vote_count")
    private int voteCount;

    public String getFirstAirDate() {
        return firstAirDate;
    }

    public String getOverview() {
        return overview;
    }

    public String getOriginalLanguage() {
        return originalLanguage;
    }


    public String getPosterPath() {
        return posterPath;
    }

    public String getBackdropPath() {
        return backdropPath;
    }

    public String getOriginalName() {
        return originalName;
    }

    public double getPopularity() {
        return popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public void setFirstAirDate(String firstAirDate) {
        this.firstAirDate = firstAirDate;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public void setOriginalLanguage(String originalLanguage) {
        this.originalLanguage = originalLanguage;
    }


    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }


    public void setBackdropPath(String backdropPath) {
        this.backdropPath = backdropPath;
    }

    public void setOriginalName(String originalName) {
        this.originalName = originalName;
    }

    public void setPopularity(double popularity) {
        this.popularity = popularity;
    }

    public void setVoteAverage(double voteAverage) {
        this.voteAverage = voteAverage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setVoteCount(int voteCount) {
        this.voteCount = voteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstAirDate);
        dest.writeString(this.overview);
        dest.writeString(this.originalLanguage);
        dest.writeString(this.posterPath);
        dest.writeString(this.backdropPath);
        dest.writeString(this.originalName);
        dest.writeDouble(this.popularity);
        dest.writeDouble(this.voteAverage);
        dest.writeString(this.name);
        dest.writeInt(this.id);
        dest.writeInt(this.voteCount);
    }

    public ResultsItemTvShow() {
    }

    protected ResultsItemTvShow(Parcel in) {
        this.firstAirDate = in.readString();
        this.overview = in.readString();
        this.originalLanguage = in.readString();
        this.posterPath = in.readString();
        this.backdropPath = in.readString();
        this.originalName = in.readString();
        this.popularity = in.readDouble();
        this.voteAverage = in.readDouble();
        this.name = in.readString();
        this.id = in.readInt();
        this.voteCount = in.readInt();
    }

    public static final Parcelable.Creator<ResultsItemTvShow> CREATOR = new Parcelable.Creator<ResultsItemTvShow>() {
        @Override
        public ResultsItemTvShow createFromParcel(Parcel source) {
            return new ResultsItemTvShow(source);
        }

        @Override
        public ResultsItemTvShow[] newArray(int size) {
            return new ResultsItemTvShow[size];
        }
    };
}
