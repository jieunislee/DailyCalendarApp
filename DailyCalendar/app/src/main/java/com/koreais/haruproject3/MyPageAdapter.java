package com.koreais.haruproject3;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

public class MyPageAdapter extends FragmentStatePagerAdapter {
    public MyPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int i) {

        switch ( i ) {
            case PageInfo.PAGE1 :
                return new PageFragment_diary();
            case PageInfo.PAGE2 :
                return new PageFragment_image();
            case PageInfo.PAGE3 :
                return new PageFragment_phone();
            case PageInfo.PAGE4 :
                return new PageFragment_place();
        }

        return null;
    }

    @Override
    public int getCount() {
        return PageInfo.PAGES;
    }
}
