package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.zxing.Result;

import ca.ualberta.CMPUT3012019T02.alexandria.R;
import me.dm7.barcodescanner.zxing.ZXingScannerView;
/**
 * Activity for scanning isbns
 */
public class ISBNLookup extends AppCompatActivity implements ZXingScannerView.ResultHandler {

    private ZXingScannerView mScannerView;
    private final int REQUEST_PERMISSION_PHONE_STATE = 1;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        checkCameraPermissions();

        setContentView(R.layout.activity_isbn_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZXingScannerView(this);
        contentFrame.addView(mScannerView);

        Toolbar toolbar = findViewById(R.id.isbn_scanner_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);    // remove default title

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> finish());

    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
    }

    @Override
    public void handleResult(Result rawResult) {
        String isbn = rawResult.getText();
        Log.e("ISBNLookup", "Scanned ISBN: " + isbn);
        if (isbn.length() == 10 || isbn.length() == 13) {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isbn", isbn);
            setResult(-1, resultIntent);
            finish();
        } else {
            Toast.makeText(this, "ISBN code not found", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(() -> mScannerView.resumeCameraPreview(ISBNLookup.this), 1000);
        }

    }

    /*
    Permission code implemented from :
    https://stackoverflow.com/questions/35484767/activitycompat-requestpermissions-not-showing-dialog-box
     */

    private void checkCameraPermissions() {
        int permissionCheck = ContextCompat.checkSelfPermission(
                this, Manifest.permission.CAMERA);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                showExplanation("Permission Needed", "Camera is required to scan the ISBN", Manifest.permission.CAMERA, REQUEST_PERMISSION_PHONE_STATE);
            } else {
                requestPermission(Manifest.permission.CAMERA, REQUEST_PERMISSION_PHONE_STATE);
            }
        } else {
            Toast.makeText(ISBNLookup.this, "Permission already granted!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            String permissions[],
            int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_PHONE_STATE:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(ISBNLookup.this, "Permission Granted!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(ISBNLookup.this, "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void showExplanation(String title,
                                 String message,
                                 final String permission,
                                 final int permissionRequestCode) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        requestPermission(permission, permissionRequestCode);
                    }
                });
        builder.create().show();
    }

    private void requestPermission(String permissionName, int permissionRequestCode) {
        ActivityCompat.requestPermissions(this,
                new String[]{permissionName}, permissionRequestCode);
    }
}
