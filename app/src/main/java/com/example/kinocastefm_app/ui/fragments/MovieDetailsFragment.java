package com.example.kinocastefm_app.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.kinocastefm_app.Constants;
import com.example.kinocastefm_app.R;
import com.example.kinocastefm_app.models.Movie;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Document;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends DialogFragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MovieDetailsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MovieDetailsFragment newInstance(String param1, String param2) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);
        ImageView posterImageView = rootView.findViewById(R.id.movieImage);
        Bundle mArgs = getArguments();
        Movie movie=mArgs.getParcelable("movie");
        Glide.with(this)
                .load(movie.poster)
                .into(posterImageView);
        TextView textViewMovieTitle=rootView.findViewById(R.id.movieTitle);
        TextView textViewMovieType=rootView.findViewById(R.id.movieType);
        TextView textViewMovieRating=rootView.findViewById(R.id.movieRating);
        TextView textViewMovieDuration=rootView.findViewById(R.id.movieDuration);
        TextView textViewMovieReleased=rootView.findViewById(R.id.movieReleased);
        TextView textViewMoviePlot=rootView.findViewById(R.id.moviePlot);
        TextView textViewMovieActors=rootView.findViewById(R.id.movieActors);
        TextView textViewMovieDirectors=rootView.findViewById(R.id.movieDirectors);
        TextView textViewMovieWriters=rootView.findViewById(R.id.movieWriters);

        textViewMovieTitle.setText(movie.title);
        textViewMovieType.setText(movie.genre);
        textViewMovieRating.setText("   Rating ("+movie.rating+")");
        textViewMovieDuration.setText("   Duration ("+movie.duraton+")");
        textViewMovieReleased.setText("   Released at "+movie.released);
        textViewMoviePlot.setText(movie.plot);
        textViewMovieActors.setText(movie.actors);
        textViewMovieDirectors.setText(movie.director);
        textViewMovieWriters.setText(movie.writer);

        Button btnRemove=rootView.findViewById(R.id.btnRemove);
        Button btnAddveWL=rootView.findViewById(R.id.btnAddWatchlater);
        Button btnAddL=rootView.findViewById(R.id.btnAddToList);

        if (mArgs.getString("action")== Constants.CALLED_FROM_WATCHLATER){
            btnAddveWL.setVisibility(View.GONE);
            btnAddL.setVisibility(View.GONE);
        } else{
            btnRemove.setVisibility(View.GONE);
        }
        FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
        btnRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseFirestore.collection("users").whereEqualTo("uid", FirebaseAuth.getInstance().getUid()).get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                List<String> array = (List<String>) document.get("watchLater");
                                array.remove(movie.imdbId);
                                document.getReference().update("watchLater", array)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getActivity(), "Movie ("+movie.title+") has been deleted", Toast.LENGTH_LONG).show();
                                            try {
                                                NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment);
                                                navController.navigate(R.id.watchLaterFragment);
                                                dismiss();
                                            } catch (Exception e) {
                                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                                            }
                                        })
                                        .addOnFailureListener(e -> {
                                            Toast.makeText(getActivity(), "Something went wrong", Toast.LENGTH_LONG).show();
                                        });
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    }
                });
            }
        });
        return rootView;
    }


}