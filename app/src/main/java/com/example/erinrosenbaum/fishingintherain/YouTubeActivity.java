package com.example.erinrosenbaum.fishingintherain;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class YouTubeActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener{

    static final String GOOGLE_API_KEY = "AIzaSyC4Gy5UMz-8O1gUNCsWV-tYYe7fyFn7fPw";
    static final String YOUTUBE_VIDEO_ID = "iUXAHc-ABoY";
    static final String YOUTUBE_PLAYLIST = "RDiUXAHc-ABoY#t=3";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_you_tube);
        //ConstraintLayout constraintLayout = (ConstraintLayout) findViewById(R.id.activity_you_tube);
        ConstraintLayout layout = (ConstraintLayout) getLayoutInflater().inflate(R.layout.activity_you_tube, null);
        // https://stackoverflow.com/questions/7378636/setting-background-colour-of-android-layout-element
        layout.setBackgroundColor(Color.BLACK);
        setContentView(layout);

        //Button button1 = new Button(this);
        //button1.setLayoutParams(new ConstraintLayout.LayoutParams(300, 80));
        //button1.setText("Button added");
        //layout.addView(button1);

        YouTubePlayerView playerView = new YouTubePlayerView(this);
        playerView.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        layout.addView(playerView);
        playerView.initialize(GOOGLE_API_KEY, this);
    }


    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {

        Toast.makeText(this, "Video Player initialized", Toast.LENGTH_LONG).show();

        if(!wasRestored){
            youTubePlayer.cueVideo(YOUTUBE_VIDEO_ID);
        }


    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        final int REQUEST_CODE = 1;

        if(youTubeInitializationResult.isUserRecoverableError()) {
            youTubeInitializationResult.getErrorDialog(this, REQUEST_CODE).show();

        } else {
            String errorMessage = String.format("Error upon YouTubePlayer Initialization (%1$s", youTubeInitializationResult.toString());
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show();
        }

    }
}
