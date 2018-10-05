package com.myfoody.base;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.myfoody.R;
import com.myfoody.utils.CommonLocationListener;
import com.myfoody.utils.ValidationUtil;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

/**
 * Created by tringapps-admin on 3/3/17.
 */

public class BaseActivity extends AppCompatActivity {
    protected Toolbar toolbar;
    private TextView toolbarTitle, doneButton;
    private ImageView backButton, rightImageButton;
    private ProgressDialog mProgressDialog;


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }

    protected void initToolbar() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbarTitle = (TextView) findViewById(R.id.toolbar_title);
        backButton = (ImageView) findViewById(R.id.left_back_icon);
        rightImageButton = (ImageView) findViewById(R.id.right_image_icon);
        doneButton = (TextView) findViewById(R.id.right_done_button);
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        setSupportActionBar(toolbar);

        // Set the padding to match the Status Bar height
        if (ValidationUtil.getInstance().isKitkat()) {
            toolbar.setPadding(0, getStatusBarHeight(), 0, 0);
        }

        hideActionBarTitle();
    }

    public int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public Drawable getVectorDrawable(int drawableId) {
        return AppCompatResources.getDrawable(this, drawableId);
    }
    public void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
        backButton.setImageResource(R.mipmap.ic_launcher);
    }

    public void showCloseIcon() {
        backButton.setVisibility(View.VISIBLE);
        backButton.setImageResource(R.mipmap.ic_launcher);
    }

    public void hideLeftIcon() {
        backButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);
    }
    /**
     * Toolbar left icons helper methods
     * hideRightIcon
     * showRightAddIcon
     * showRightSearchIcon
     * <p>
     * addRightClickListener
     */

    public void hideRightIcon() {
        rightImageButton.setVisibility(View.GONE);
        doneButton.setVisibility(View.GONE);
    }

    public void showRightAddIcon() {
        rightImageButton.setVisibility(View.VISIBLE);
        rightImageButton.setImageResource(R.mipmap.ic_launcher);
        doneButton.setVisibility(View.GONE);
    }
    public void addRightClickListener(View.OnClickListener onClickListener) {
        showRightAddIcon();
        rightImageButton.setOnClickListener(onClickListener);
    }

    /**
     * Show Application Toolbar
     */
    protected void showToolbar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.show();
        }
    }

    /**
     * Hide default ActionBar title
     */
    protected void hideActionBarTitle() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }
    }

    /**
     * Change app toolbar title
     *
     * @param title toolbar page title
     */
    public void setToolbarTitle(String title) {
        if (toolbarTitle != null) {
            toolbarTitle.setVisibility(View.VISIBLE);
            toolbarTitle.setText(title);
        }
    }

    public void showShortToast(String msg) {
        if (!this.isFinishing()) {
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialog(String title, String content) {
        final CustomDialog customDialog = new CustomDialog();
        customDialog.setContent(title, content);
        customDialog.showDialog(getSupportFragmentManager());
    }

    public void showDialog(View.OnClickListener pListener,
                           View.OnClickListener nListener, String... content) {
        final CustomDialog CustomDialog = new CustomDialog();
        CustomDialog.setContent(pListener, nListener, content);
        CustomDialog.showDialog(getSupportFragmentManager());
    }

    public void showInputDialog(int inputType, DialogListener dialogListener, String... content) {
        final CustomDialog CustomDialog = new CustomDialog();
        CustomDialog.setContent(inputType, dialogListener, content[0], content[1], content[2]);
        CustomDialog.showDialog(getSupportFragmentManager());
    }

    public void showSuccessDialog(String content, View.OnClickListener onClickListener) {
        final CustomDialog CustomDialog = new CustomDialog();
        if (onClickListener != null) {
            CustomDialog.setContent(getString(R.string.success), content, onClickListener);
        } else {
            CustomDialog.setContent(getString(R.string.success), content);
        }
        CustomDialog.showDialog(getSupportFragmentManager());
    }

    /**
     * Generic app fragment transaction helper
     *
     * @param toFragment layoutId Frame Layout id should always be base_container
     */
    protected FragmentTransaction getFragmentTransaction(Fragment toFragment, boolean isShowAnim, boolean isReplace) {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        //transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE);
        if (isShowAnim) {
            transaction.setCustomAnimations(R.anim.slide_from_right, R.anim.slide_to_left, R.anim.slide_from_left, R.anim.slide_to_right);
        }

        if (isReplace) {
            transaction.replace(R.id.base_container, toFragment, toFragment.getClass().getSimpleName());
        } else {
            transaction.add(R.id.base_container, toFragment, toFragment.getClass().getSimpleName());
        }
        return transaction;
    }

    /**
     * Replace old fragment from container
     *
     * @param toFragment       replacing fragment
     * @param isAddToBackStack is need to add in stack to handle back navigation
     * @param tagName          fragment tag name
     */
    public void replaceFragment(final Fragment toFragment, boolean isAddToBackStack, String tagName) {
        updateFragmentTitle(toFragment, tagName);

        FragmentTransaction transaction = getFragmentTransaction(toFragment, isAddToBackStack, true);
        if (isAddToBackStack) {
            transaction.addToBackStack(tagName);
            transaction.commit();
        } else {
            transaction.commitNow();
        }

        showToolbar();
        setToolbarTitle(tagName);
    }

    /**
     * Add Fragment method helps to add fragment over to another
     * This should be added into back stack , activity onBackPress() will remove the newly added fragment
     *
     * @param toFragment    add new fragment
     * @param showAnimation is need to show animation while adding new fragment
     * @param tagName       fragment tag name
     */
    public void addFragment(final Fragment toFragment, boolean showAnimation, String tagName) {
        updateFragmentTitle(toFragment, tagName);

        FragmentTransaction transaction = getFragmentTransaction(toFragment, showAnimation, false);
        transaction.addToBackStack(tagName);
        transaction.commit();

        showToolbar();
        setToolbarTitle(tagName);
    }

    private void updateFragmentTitle(Fragment toFragment, String tagName) {
        Bundle bundle = toFragment.getArguments();
        if (bundle != null) {
            bundle.putString(getString(R.string.page_toolbar_title), tagName);
        } else {
            bundle = new Bundle();
            bundle.putString(getString(R.string.page_toolbar_title), tagName);
            toFragment.setArguments(bundle);
        }
    }

    @Override
    public void onBackPressed() {
            FragmentManager fm = getSupportFragmentManager();

            if (fm.getBackStackEntryCount() > 0) {
                Fragment fragment = fm.getFragments().get(fm.getBackStackEntryCount() - 1);

                setToolbarTitle(fragment.getArguments().getString(getString(R.string.page_toolbar_title)));
            }
            super.onBackPressed();
    }


    public Location getLocation() {
        Location location = null;
        try {
            LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

            // getting GPS status
            boolean isGPSEnabled = locationManager
                    .isProviderEnabled(LocationManager.GPS_PROVIDER);

            // getting network status
            boolean isNetworkEnabled = locationManager
                    .isProviderEnabled(LocationManager.NETWORK_PROVIDER);

            if (isGPSEnabled || isNetworkEnabled) {
                location = getLocation(null, locationManager, isNetworkEnabled);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return location;
    }

    private Location getLocation(Location location, LocationManager locationManager, boolean isNetworkEnabled) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {

            if (isNetworkEnabled) {
                locationManager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        10,
                        1000 * 60, new CommonLocationListener());
                Log.d("Network", "Network");
                location = locationManager
                        .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            } else {
                locationManager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        10,
                        1000 * 60, new CommonLocationListener());
                Log.d("GPS Enabled", "GPS Enabled");
                location = locationManager
                        .getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
        return location;
    }

    public void onPermissionGrant(String[] permissions, int PERMISSION_REQUEST_CODE) {

    }

    public void onPermissionDenied(String[] permissions, int PERMISSION_REQUEST_CODE) {
    }

    @TargetApi(Build.VERSION_CODES.M)
    public boolean checkPermission(String[] permissions, int requestCode) {
        PERMISSION_REQUEST_CODE = requestCode;
        String[] requestPermissions = hasPermission(permissions); // List of not granted permission

        if (requestPermissions.length > 0 && requestPermissions[0] != null && requestPermissions[0].length() > 0) {
            this.requestPermissions(requestPermissions, requestCode);
            return false;
        } else {
            onPermissionGrant(permissions, PERMISSION_REQUEST_CODE);
            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        List<String> deniedPermission = new ArrayList<>(), grantPermission = new ArrayList<>();

        for (String permission : permissions) {
            if (checkCallingOrSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
                grantPermission.add(permission);
            } else {
                deniedPermission.add(permission);
            }
        }

        if (deniedPermission.size() > 0) {
            onPermissionDenied(deniedPermission.toArray(new String[deniedPermission.size()]), PERMISSION_REQUEST_CODE);
        } else {
            onPermissionGrant(grantPermission.toArray(new String[grantPermission.size()]), PERMISSION_REQUEST_CODE);
        }
    }

    public String[] hasPermission(String... permissions) {
        String[] denyPermission = new String[]{};
        ArrayList<String> permissionsList = new ArrayList<>();
        for (String permission : permissions) {

            if (checkCallingOrSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsList.add(permission); // Add the permission that is not granted by user
            }
        }

        if (permissionsList.size() > 0) {
            denyPermission = new String[permissionsList.size()]; // Create the string array with size of permissions that is not granted by user
        }

        for (int i = 0; i < permissionsList.size(); i++) {
            denyPermission[i] = permissionsList.get(i); // Add one by one permission from array list to string array object
        }

        return denyPermission; // return not granted permission to request
    }

    private int PERMISSION_REQUEST_CODE;
}
