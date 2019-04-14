package com.babach.beatbox;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BeatBox
{
    private static final String TAG = "BeatBox";

    private static final String SOUNDS_FOLDER = "sample_sounds";
    private static final int MAX_SOUNDS = 5;

    private AssetManager mAssets;
    private List<Sound> mSounds = new ArrayList<>();
    private SoundPool mSoundPool;
    private float minSpeedRate = 0.1f;
    private float speedRate = 1f;

    public BeatBox(Context context)
    {
        mAssets = context.getAssets();
        mSoundPool = new SoundPool(MAX_SOUNDS, AudioManager.STREAM_MUSIC, 0);
        loadSounds();
    }

    private void loadSounds()
    {
        String[] soundNames;
        try
        {
            soundNames = mAssets.list(SOUNDS_FOLDER);
            Log.i(TAG, "Found " + soundNames.length + " sounds");
        }
        catch (IOException ioe)
        {
            Log.e(TAG, "Could not list assets", ioe);
            return;
        }

        for (String filename : soundNames)
        {
            try
            {
                String assetPath = SOUNDS_FOLDER + "/" + filename;
                Sound sound = new Sound(assetPath);
                load(sound);
                mSounds.add(sound);
            }
            catch (IOException e)
            {
                Log.e(TAG, "Could not load sound" + filename, e);
            }

        }
    }

    private void load(Sound sound) throws IOException
    {
        AssetFileDescriptor afd = mAssets.openFd(sound.getAssetPath());
        int soundId = mSoundPool.load(afd, 1);
        sound.setSoundId(soundId);
    }

    public void play(Sound sound)
    {
        Integer soundID = sound.getSoundId();
        if (soundID == null)
        {
            return;
        }
        mSoundPool.play(soundID, 1, 1, 1, 0, speedRate);
    }


    public void setSpeedRate(int rate)
    {
        speedRate = calculateSpeedRate(rate);
        Log.i(TAG, "SET-SPEED-RATE: " + speedRate);
    }


    public float getSpeedRate()
    {
        return speedRate;
    }


    private float calculateSpeedRate(int rate)
    {
        if (rate == 0)
        {
            return minSpeedRate;
        }
        return ((float)rate / 100f) * 2.0f;
    }

    public void release()
    {
        mSoundPool.release();
    }


    public List<Sound> getSounds()
    {
        return mSounds;
    }
}
