package com.example.kinocastefm_app;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinocastefm_app.models.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GenerateMovie {
    public static CompletableFuture<Movie> searchMovieById(String imdbId, Context context){
        CompletableFuture<Movie> future = new CompletableFuture<>();
        String url = "http://www.omdbapi.com/?i=" + imdbId + "&apikey="+ Constants.API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    Movie mMovie = new Movie(imdbId, jsonObject.getString("Title"), jsonObject.getString("Plot"), jsonObject.getString("Released"), jsonObject.getString("Genre"), jsonObject.getString("Director"), jsonObject.getString("Writer"), jsonObject.getString("Actors"), jsonObject.getString("Poster"), jsonObject.getString("imdbRating"), jsonObject.getString("Runtime"), jsonObject.getString("Year"));
                    future.complete(mMovie);
                } catch (JSONException e) {
                    future.completeExceptionally(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                future.completeExceptionally(volleyError);
            }
        });
        Volley.newRequestQueue(context).add(request);
        return future;
    }
    public static CompletableFuture<List<Movie>> parseSearchedMoviesToListOfMovies(int page, String movieName, Context context){
        List<Movie> mMovies=new ArrayList<Movie>();
        CompletableFuture<List<Movie>> future = new CompletableFuture<>();
        String url = Constants.API_URL+"?s="+movieName+"&page="+page+"&apikey="+ Constants.API_KEY;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject jsonObject) {
                try {
                    JSONArray moviesSearched=jsonObject.getJSONArray("Search");
                    for (int index=0;index<moviesSearched.length();index++){
                        JSONObject movieSearched=moviesSearched.getJSONObject(index);
                        GenerateMovie.searchMovieById(movieSearched.getString("imdbID"), context).thenAccept(result -> {
                            Movie mMovie = new Movie(result.imdbId, result.title, result.plot, result.released, result.genre, result.director, result.writer, result.actors, result.poster, result.rating, result.duraton, result.year);
                            mMovies.add(mMovie);
                            if (moviesSearched.length() == mMovies.size()) {
                                future.complete(mMovies);
                            }
                        }).exceptionally(throwable -> {
                            Toast.makeText(context, throwable.getMessage(), Toast.LENGTH_SHORT).show();
                            return null;
                        });
                    }
                } catch (JSONException e) {
                    future.completeExceptionally(e);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                future.completeExceptionally(volleyError);
            }
        });
        Volley.newRequestQueue(context).add(request);
        return future;
    }
}
