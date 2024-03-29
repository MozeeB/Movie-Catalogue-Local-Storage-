package com.zeeb.moviecataloguelocalstorage.data.remote.model.tvshow;

import java.util.List;

import com.google.gson.annotations.SerializedName;

public class ResponseTvShow {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<ResultsItemTvShow> results;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<ResultsItemTvShow> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}