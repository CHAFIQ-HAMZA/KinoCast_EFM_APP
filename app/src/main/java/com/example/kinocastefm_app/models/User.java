package com.example.kinocastefm_app.models;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.kinocastefm_app.Constants;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class User {
    public String uid;
    public String username;
//    public List<Movie> watchLater;

    public User(String userId, Context context) {
        this.uid = userId;
        getUsername(context);
    }

    public CompletableFuture<List<Movie>> getAllMovies(Context context) {
        CompletableFuture<List<Movie>> future = new CompletableFuture<>();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        List<Movie> mMovies = new ArrayList<>();

        FirebaseFirestore.getInstance().collection("users").whereEqualTo("uid", this.uid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                List<String> watchLater = (List<String>) document.get("watchLater");
                                if (watchLater.isEmpty()){
                                    future.complete(mMovies);
                                } else{
                                    for (String movie : watchLater) {
                                        String url = Constants.API_URL+"?i=" + movie + "&apikey="+ Constants.API_KEY;
                                        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
                                            @Override
                                            public void onResponse(JSONObject jsonObject) {
                                                try {
                                                    Movie mMovie = new Movie(movie, jsonObject.getString("Title"), jsonObject.getString("Plot"), jsonObject.getString("Released"), jsonObject.getString("Genre"), jsonObject.getString("Director"), jsonObject.getString("Writer"), jsonObject.getString("Actors"), jsonObject.getString("Poster"), jsonObject.getString("imdbRating"), jsonObject.getString("Runtime"), jsonObject.getString("Year"));
                                                    mMovies.add(mMovie);
                                                    if (mMovies.size() == watchLater.size()) {
                                                        future.complete(mMovies);
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
                                    }
                                }
                            }
                        } else {
                            future.completeExceptionally(task.getException());
                        }
                    }
                });
        return future;
    }

    private void getUsername(Context context) {
        FirebaseAuth mAuth=FirebaseAuth.getInstance();
        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        FirebaseFirestore.getInstance().collection("users").whereEqualTo("uid",this.uid).get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                username=document.getString("username");
                            }
                        }
                    }
                });
    }
}
