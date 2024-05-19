package com.example.kinocastefm_app.ui.fragments;

import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.kinocastefm_app.Constants;
import com.example.kinocastefm_app.GenerateMovie;
import com.example.kinocastefm_app.R;
import com.example.kinocastefm_app.models.Movie;
import com.example.kinocastefm_app.recycle_views.MovieAdapter;
import com.example.kinocastefm_app.recycle_views.MoviesRecycleViewInterf;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RecomendedFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RecomendedFragment extends Fragment implements MoviesRecycleViewInterf {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private int whichPage=1;
    private List<Movie> searchResult=new ArrayList<Movie>();

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public RecomendedFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment RecomendedFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RecomendedFragment newInstance(String param1, String param2) {
        RecomendedFragment fragment = new RecomendedFragment();
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
        View rootView=inflater.inflate(R.layout.fragment_recomended, container, false);
        TextView movieNameTextView=rootView.findViewById(R.id.nameToSearchTB);
        Button searchBtn=rootView.findViewById(R.id.searchBtn);
        FloatingActionButton nextBtn=rootView.findViewById(R.id.nextPage);
        FloatingActionButton previousBtn=rootView.findViewById(R.id.previousPage);
        ConstraintLayout constraintLayout=rootView.findViewById(R.id.searchProgressBarLayout);
        ConstraintLayout nothingToDisplay=rootView.findViewById(R.id.nothingToDisplayLayout);
        RecyclerView rv=rootView.findViewById(R.id.searchRecyclerView);
        rv.setLayoutManager(new LinearLayoutManager(getActivity()));
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout constraintLayout=rootView.findViewById(R.id.searchProgressBarLayout);
                constraintLayout.setVisibility(View.VISIBLE);
                nothingToDisplay.setVisibility(View.INVISIBLE);
                whichPage++;
                rv.setAdapter(new MovieAdapter(getActivity(),new ArrayList<Movie>(),RecomendedFragment.this));
                GenerateMovie.parseSearchedMoviesToListOfMovies(whichPage, movieNameTextView.getText().toString(), getActivity()).thenAccept(result -> {
                    searchResult=new ArrayList<Movie>();
                    searchResult.addAll(result);
                    rv.setAdapter(new MovieAdapter(getActivity(),searchResult,RecomendedFragment.this));
                    Toast.makeText(getActivity(), searchResult.get(0).title, Toast.LENGTH_SHORT).show();
                    constraintLayout.setVisibility(View.INVISIBLE);
                }).exceptionally(throwable -> {
                    constraintLayout.setVisibility(View.INVISIBLE);
                    nothingToDisplay.setVisibility(View.VISIBLE);
                    return null;
                });
            }
        });
        previousBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConstraintLayout constraintLayout=rootView.findViewById(R.id.searchProgressBarLayout);
                constraintLayout.setVisibility(View.VISIBLE);
                whichPage--;
                nothingToDisplay.setVisibility(View.INVISIBLE);
                rv.setAdapter(new MovieAdapter(getActivity(),new ArrayList<Movie>(),RecomendedFragment.this));
                GenerateMovie.parseSearchedMoviesToListOfMovies(whichPage, movieNameTextView.getText().toString(), getActivity()).thenAccept(result -> {
                    searchResult=new ArrayList<Movie>();
                    searchResult.addAll(result);
                    rv.setAdapter(new MovieAdapter(getActivity(),searchResult,RecomendedFragment.this));
                    Toast.makeText(getActivity(), searchResult.get(0).title, Toast.LENGTH_SHORT).show();
                    constraintLayout.setVisibility(View.INVISIBLE);
                }).exceptionally(throwable -> {
                    constraintLayout.setVisibility(View.INVISIBLE);
                    nothingToDisplay.setVisibility(View.VISIBLE);
                    return null;
                });

            }
        });
        searchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                constraintLayout.setVisibility(View.VISIBLE);
                nothingToDisplay.setVisibility(View.INVISIBLE);
                Toast.makeText(getActivity(),movieNameTextView.getText().toString(),Toast.LENGTH_SHORT).show();
                rv.setAdapter(new MovieAdapter(getActivity(),new ArrayList<Movie>(),RecomendedFragment.this));
                GenerateMovie.parseSearchedMoviesToListOfMovies(whichPage, movieNameTextView.getText().toString(), getActivity()).thenAccept(result -> {
                    searchResult=new ArrayList<Movie>();
                    searchResult.addAll(result);
                    rv.setAdapter(new MovieAdapter(getActivity(),searchResult,RecomendedFragment.this));
                    constraintLayout.setVisibility(View.INVISIBLE);

                }).exceptionally(throwable -> {
                    constraintLayout.setVisibility(View.INVISIBLE);
                    nothingToDisplay.setVisibility(View.VISIBLE);
                    return null;
                });
            }
        });
        return rootView;
    }

    @Override
    public void onMovieClick(int position) {
        Movie movie=this.searchResult.get(position);
        Bundle args = new Bundle();
        args.putParcelable("movie", movie);
        args.putString("action", Constants.CALLED_FROM_SEARCH);
        MovieDetailsFragment movieDetailsFragment=new MovieDetailsFragment();
        movieDetailsFragment.setArguments(args);
        movieDetailsFragment.show(getFragmentManager(), "Movie details of "+movie.title);
    }
}