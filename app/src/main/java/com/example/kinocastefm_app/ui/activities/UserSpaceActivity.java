package com.example.kinocastefm_app.ui.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.example.kinocastefm_app.R;
import com.example.kinocastefm_app.models.Movie;
import com.example.kinocastefm_app.models.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;

public class UserSpaceActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_space);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
//        User mUser=new User(FirebaseAuth.getInstance().getCurrentUser().getUid(), getApplicationContext());
//        List<Movie> watchLater=new ArrayList<Movie>();
//        mUser.getAllMovies(getApplicationContext()).thenAccept(result -> {
//            // Handle the result
//            watchLater.addAll(result);
//            Toast.makeText(getApplicationContext(), watchLater.get(0).year, Toast.LENGTH_LONG).show();
        setUpNavigation();
//        }).exceptionally(throwable -> {
//            // Handle the exception
//            Toast.makeText(getApplicationContext(), throwable.getMessage(), Toast.LENGTH_SHORT).show();
//            return null;
//        });
    }
    public void setUpNavigation() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //NavGraph navGraph = navController.getNavInflater().inflate(R.navigation.app_graph);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNavigationView);
        //navController.setGraph(navGraph);
        NavigationUI.setupWithNavController(bottomNav, navController);
        bottomNav.findViewById(R.id.bottomNavigationView).setOnClickListener(Navigation.createNavigateOnClickListener(R.id.watchLaterFragment, null));
    }
}