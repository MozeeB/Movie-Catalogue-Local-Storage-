package com.zeeb.moviecataloguelocalstorage.data.remote.model.movie;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResponseMovie {

    @SerializedName("page")
    private int page;

    @SerializedName("total_pages")
    private int totalPages;

    @SerializedName("results")
    private List<ResultsItemMovie> results;

    @SerializedName("total_results")
    private int totalResults;

    public int getPage() {
        return page;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public List<ResultsItemMovie> getResults() {
        return results;
    }

    public int getTotalResults() {
        return totalResults;
    }
}

