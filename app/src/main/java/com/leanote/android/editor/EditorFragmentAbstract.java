package com.leanote.android.editor;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.text.Spanned;

import com.android.volley.toolbox.ImageLoader;
import com.leanote.android.util.MediaFile;
import com.leanote.android.util.helper.MediaGallery;


public abstract class EditorFragmentAbstract extends Fragment {
    public abstract void setTitle(CharSequence text);
    public abstract void setContent(CharSequence text);
    public abstract CharSequence getTitle();
    public abstract CharSequence getContent();
    public abstract void appendMediaFile(MediaFile mediaFile, String imageUrl, ImageLoader imageLoader);
    public abstract void appendGallery(MediaGallery mediaGallery);
    //public abstract boolean hasFailedMediaUploads();
    public abstract void setTitlePlaceholder(CharSequence text);
    public abstract void setContentPlaceholder(CharSequence text);

    // TODO: remove this as soon as we can (we'll need to drop the legacy editor or fix html2spanned translation)
    public abstract Spanned getSpannedContent();

    private static final String FEATURED_IMAGE_SUPPORT_KEY = "featured-image-supported";
    private static final String FEATURED_IMAGE_WIDTH_KEY   = "featured-image-width";

    protected EditorFragmentListener mEditorFragmentListener;
    protected boolean mFeaturedImageSupported;
    protected int mFeaturedImageId;
    protected String mBlogSettingMaxImageWidth;
    protected ImageLoader mImageLoader;
    protected boolean mDebugModeEnabled;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mEditorFragmentListener = (EditorFragmentListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement EditorFragmentListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(FEATURED_IMAGE_SUPPORT_KEY, mFeaturedImageSupported);
        outState.putString(FEATURED_IMAGE_WIDTH_KEY, mBlogSettingMaxImageWidth);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(FEATURED_IMAGE_SUPPORT_KEY)) {
                mFeaturedImageSupported = savedInstanceState.getBoolean(FEATURED_IMAGE_SUPPORT_KEY);
            }
            if (savedInstanceState.containsKey(FEATURED_IMAGE_WIDTH_KEY)) {
                mBlogSettingMaxImageWidth = savedInstanceState.getString(FEATURED_IMAGE_WIDTH_KEY);
            }
        }
    }

    public void setImageLoader(ImageLoader imageLoader) {
        mImageLoader = imageLoader;
    }

    public void setFeaturedImageSupported(boolean featuredImageSupported) {
        mFeaturedImageSupported = featuredImageSupported;
    }

    public void setBlogSettingMaxImageWidth(String blogSettingMaxImageWidth) {
        mBlogSettingMaxImageWidth = blogSettingMaxImageWidth;
    }

    public void setFeaturedImageId(int featuredImageId) {
        mFeaturedImageId = featuredImageId;
    }

    public void setDebugModeEnabled(boolean debugModeEnabled) {
        mDebugModeEnabled = debugModeEnabled;
    }

    /**
     * Called by the activity when back button is pressed.
     */
    public boolean onBackPressed() {
        return false;
    }

    /**
     * The editor may need to differentiate local draft and published articles
     *
     * @param isLocalDraft edited post is a local draft
     */
    public void setLocalDraft(boolean isLocalDraft) {
        // Not unused in the new editor
    }

    /**
     * Callbacks used to communicate with the parent Activity
     */
    public interface EditorFragmentListener {
        void onEditorFragmentInitialized();
        void onSettingsClicked();
        void onAddMediaClicked();
//        void onMediaRetryClicked(String mediaId);
        //void onMediaUploadCancelClicked(String mediaId, boolean delete);
        void onFeaturedImageChanged(int mediaId);
        // TODO: remove saveMediaFile, it's currently needed for the legacy editor
        void saveMediaFile(MediaFile mediaFile);
    }
}
