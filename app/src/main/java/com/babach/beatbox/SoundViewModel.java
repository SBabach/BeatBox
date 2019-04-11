package com.babach.beatbox;

import android.databinding.BaseObservable;
import android.databinding.Bindable;

public class SoundViewModel extends BaseObservable
{
    private Sound mSound;
    private BeatBox mBeatBox;

    public SoundViewModel(BeatBox beatBox)
    {
        mBeatBox = beatBox;
    }

    public void setSound(Sound sound)
    {
        notifyChange();
        this.mSound = sound;
    }

@Bindable
    public String getTitle()
    {
        return mSound.getName();
    }

    public Sound getSound()
    {
        return mSound;
    }
}
