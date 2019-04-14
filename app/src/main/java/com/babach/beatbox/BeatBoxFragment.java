package com.babach.beatbox;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import com.babach.beatbox.databinding.FragmentBeatBoxBinding;
import com.babach.beatbox.databinding.ListItemSoundBinding;

import java.util.List;

public class BeatBoxFragment extends Fragment
{
    private BeatBox mBeatBox;

    public static BeatBoxFragment newInstance()
    {
        return new BeatBoxFragment();
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        mBeatBox = new BeatBox(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState)
    {
        final FragmentBeatBoxBinding binding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_beat_box, container, false);
        setPlaybackSpeedTxt(binding.speedRateValueTxt, mBeatBox.getSpeedRate());
        binding.recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        binding.recyclerView.setAdapter(new SoundAdapter(mBeatBox.getSounds()));
        binding.speedRateSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener()
        {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
            {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar)
            {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar)
            {
                mBeatBox.setSpeedRate(seekBar.getProgress());
                setPlaybackSpeedTxt(binding.speedRateValueTxt, mBeatBox.getSpeedRate());
            }
        });
        return binding.getRoot();
    }

    private void setPlaybackSpeedTxt(TextView textView, float playbackSpeed)
    {
        textView.setText("Playback speed: " + playbackSpeed + "x");
    }

    @Override
    public void onDestroy()
    {
        super.onDestroy();
        mBeatBox.release();
    }

    private class SoundHolder extends RecyclerView.ViewHolder
    {
        private ListItemSoundBinding mBinding;

        public SoundHolder(ListItemSoundBinding binding)
        {
            super(binding.getRoot());
            mBinding = binding;
            mBinding.setViewModel(new SoundViewModel(mBeatBox));
        }

        public void bind(Sound sound)
        {
            mBinding.getViewModel().setSound(sound);
            mBinding.executePendingBindings();
        }
    }

    private class SoundAdapter extends RecyclerView.Adapter
    {
        private List<Sound> mSounds;


        public SoundAdapter(List<Sound> sounds)
        {
            mSounds = sounds;
        }

        @NonNull
        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
        {
            LayoutInflater inflater = LayoutInflater.from(getActivity());
            ListItemSoundBinding binding = DataBindingUtil.inflate(inflater,
                    R.layout.list_item_sound, parent, false);
            return new SoundHolder(binding);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int i)
        {
            SoundHolder holder = (SoundHolder) viewHolder;
            Sound sound = mSounds.get(i);
            holder.bind(sound);
        }

        @Override
        public int getItemCount()
        {
            return mSounds.size();
        }
    }
}
