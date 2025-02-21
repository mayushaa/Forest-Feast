package com.example.forestfeast;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.util.Log;

public class MusicService extends Service {
    public MediaPlayer player;
    public boolean isPlaying;
    public MusicService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Log.d("MIXING", "before OnStartCommand music service");

        String action = intent != null ? intent.getAction() : null;

        Log.d("MIXING", "after OnStartCommand music service");

        if ("PAUSE_MUSIC".equals(action)) {
            pauseMusic();
        }
        else if ("RESUME_MUSIC".equals(action)) {
            resumeMusic();
        }
        else if ("STOP_MUSIC".equals(action)) {
            stopMusic();
        }
        else {
            int musicResId = intent.getIntExtra("MUSIC_RES_ID", 0);
            Log.d("maya debugging", "MusicService"+musicResId);
            boolean isLooping = intent.getBooleanExtra("LOOPING", false);
            float volume = intent.getFloatExtra("VOLUME", 1.0f);

            if (musicResId != 0) {
                if (player != null && isPlaying) {
                    Log.d("maya debugging", "MusicService already playing");
                    return START_STICKY;
                }

                if (player != null) {
                    Log.d("maya debugging", "MusicService stop");
                    player.stop();
                    player.release();
                }

                player = MediaPlayer.create(this, musicResId);
                player.setLooping(isLooping);
                player.setVolume(volume, volume);
                Log.d("maya debugging", "MusicService start");
                player.start();
                isPlaying = true;
            } else {
                stopMusic();
            }
        }

        return START_STICKY;
    }

    private void resumeMusic() {
        if (player != null && !player.isPlaying()) {
            player.start();
            isPlaying = true;
        }
    }

    private void pauseMusic() {
        if (player != null && player.isPlaying()) {
            player.pause();
            isPlaying = false;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (player != null) {
            player.stop();
            player.release();
            player = null;
        }
        Intent stopMusicIntent = new Intent(this, MusicService.class);
        stopService(stopMusicIntent);
    }

    private void stopMusic() {
        if (player != null) {
            player.stop();
            player.release();
            player = null;
            isPlaying = false;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}