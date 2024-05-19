package com.example.kinocastefm_app.ui.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDirections;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.kinocastefm_app.Constants;
import com.example.kinocastefm_app.R;
import com.example.kinocastefm_app.models.Movie;
import com.example.kinocastefm_app.models.User;
import com.example.kinocastefm_app.recycle_views.MovieAdapter;
import com.example.kinocastefm_app.recycle_views.MoviesRecycleViewInterf;
import com.google.firebase.auth.FirebaseAuth;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WatchLaterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WatchLaterFragment extends Fragment implements MoviesRecycleViewInterf {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private List<Movie> movies=new ArrayList<Movie>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public WatchLaterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment WatchLaterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static WatchLaterFragment newInstance(String param1, String param2) {
        WatchLaterFragment fragment = new WatchLaterFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_watch_later, container, false);
        User mUser=new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), getActivity());
        ConstraintLayout progressBarLayout=rootView.findViewById(R.id.watchLaterLoadingLayout);
        ConstraintLayout emptyResultLayout=rootView.findViewById(R.id.watchLaterNothingToDisplayMessage);
        mUser.getAllMovies(getActivity()).thenAccept(result -> {
            this.movies.addAll(result);
            RecyclerView rv=rootView.findViewById(R.id.recyclerview);
            rv.setLayoutManager(new LinearLayoutManager(getActivity()));
            rv.setAdapter(new MovieAdapter(getActivity(), this.movies, this));
            progressBarLayout.setVisibility(View.INVISIBLE);
            if (this.movies.isEmpty()){
                emptyResultLayout.setVisibility(View.VISIBLE);
            }
        }).exceptionally(throwable -> {
            Toast.makeText(getActivity(), "Empty", Toast.LENGTH_SHORT).show();
            return null;
        });
        return rootView;
        //return inflater.inflate(R.layout.fragment_watch_later, container, false);
    }

    @Override
    public void onMovieClick(int position) {
        Movie movie=this.movies.get(position);
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        args.putString("action", Constants.CALLED_FROM_WATCHLATER);
        MovieDetailsFragment movieDetailsFragment=new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        movieDetailsFragment.show(getFragmentManager(), "Movie details of "+movie.title);
    }
}