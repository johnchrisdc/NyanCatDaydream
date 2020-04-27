package xyz.jcdc.nyancatdaydream;

import android.content.Context;
import android.service.dreams.DreamService;
import android.view.LayoutInflater;

import androidx.databinding.DataBindingUtil;

import xyz.jcdc.nyancatdaydream.databinding.DaydreamNyanCatBinding;

public class NyanCatDayDream extends DreamService {

    private DaydreamNyanCatBinding binding;



    @Override
    public void onDreamingStarted() {
        super.onDreamingStarted();

        binding = DataBindingUtil.inflate(
                (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE),
                R.layout.daydream_nyan_cat,
                null,
                true);

        setContentView(binding.getRoot());

        binding.textClock.setFormat12Hour("E, MMM dd   K:mm a");
        binding.textClock.setFormat24Hour("E, MMM dd   hh:mm");
    }

    @Override
    public void onDreamingStopped() {
        super.onDreamingStopped();
    }
}
