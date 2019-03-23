package ca.ualberta.CMPUT3012019T02.alexandria.activity;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;
import android.widget.Toast;

import java9.util.concurrent.CompletableFuture;

import ca.ualberta.CMPUT3012019T02.alexandria.controller.SearchController;
import ca.ualberta.CMPUT3012019T02.alexandria.model.Book;
import me.dm7.barcodescanner.zbar.Result;
import me.dm7.barcodescanner.zbar.ZBarScannerView;

import ca.ualberta.CMPUT3012019T02.alexandria.R;



public class ISBNLookup extends AppCompatActivity implements ZBarScannerView.ResultHandler {
    private ZBarScannerView mScannerView;
    private final int REQUEST_PERMISSION_PHONE_STATE = 1;

    private static final SearchController searchController = SearchController.getInstance();

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);

        checkCameraPermissions();

        setContentView(R.layout.activity_isbn_scanner);
        ViewGroup contentFrame = (ViewGroup) findViewById(R.id.content_frame);
        mScannerView = new ZBarScannerView(this);
        contentFrame.addView(mScannerView);

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
        String format = rawResult.getBarcodeFormat().getName();
        String isbn = rawResult.getContents();
        // Note:
        // * Wait 2 seconds to resume the preview.
        // * On older devices continuously stopping and resuming camera preview can result in freezing the app.
        // * I don't know why this is the case but I don't have the time to figure out.
        if (!format.equals("ISBN10") && !format.equals("ISBN13")) {
            Toast.makeText(this, "ISBN code not found", Toast.LENGTH_SHORT).show();
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScannerView.resumeCameraPreview(ISBNLookup.this);
                }
            }, 1000);
        } else {
            Intent resultIntent = new Intent();
            resultIntent.putExtra("isbn", isbn);
            setResult(-1, resultIntent);
            finish();
        }

    }

    public static Book searchISBN(String isbn) {
        Book book = searchController.searchIsbn(isbn);

        return book;
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
