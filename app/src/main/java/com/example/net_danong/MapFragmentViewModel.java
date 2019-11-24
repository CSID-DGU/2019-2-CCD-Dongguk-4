package com.example.net_danong;

import androidx.lifecycle.ViewModel;

public class MapFragmentViewModel extends ViewModel {

        private boolean mIsSigningIn;

        public MapFragmentViewModel() {
            mIsSigningIn = false;
        }

        public boolean getIsSigningIn() {
            return mIsSigningIn;
        }

        public void setIsSigningIn(boolean mIsSigningIn) {
            this.mIsSigningIn = mIsSigningIn;
        }

    }