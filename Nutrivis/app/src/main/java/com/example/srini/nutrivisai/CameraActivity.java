package com.example.srini.nutrivisai;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import com.google.android.gms.tasks.*;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import com.google.firebase.storage.UploadTask;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.firebase.storage.*;

public class CameraActivity extends Activity {

    final static int REQUEST_CODE = 1;
    private ImageView image;
    private String mCurrentPhotoPath;
    private Intent takePictureIntent;
    private String photoDBPath;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        dispatchTakePictureIntent();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        if (resultCode == RESULT_OK) {
            Bundle extras = takePictureIntent.getExtras();
            Bundle userInfo = getIntent().getExtras();
            Bitmap data = (Bitmap) extras.get("data");
            image = (ImageView) findViewById(R.id.img);
            image.setImageBitmap(data);

            uploadPhotoToStorage();

            Toast.makeText(getApplicationContext(),"Image Uploaded " +  userInfo.getString("mUsername"),Toast.LENGTH_SHORT).show();

            Intent i = new Intent(getApplicationContext(),MainActivity.class); // wherever this needs to be redirected

            i.putExtra("imagePath", mCurrentPhotoPath);
            startActivity(i);

        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );

        mCurrentPhotoPath = image.getAbsolutePath();
        Log.d("__photo path", mCurrentPhotoPath);
        return image;
    }

    private void dispatchTakePictureIntent() {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoFile != null) {
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_CODE);
            }
        }
        galleryAddPic();

    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }
    private void uploadPhotoToStorage() {

        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File(mCurrentPhotoPath));

        final StorageReference uploadRef = storageRef.child("images/"+file.getLastPathSegment());
        UploadTask uploadTask = uploadRef.putFile(file);
        photoDBPath = "images/"+file.getLastPathSegment();

        // Register observers to listen for when the download is done or if it fails
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d("__FAIL", "NO PHOTO WAS UPLOADED");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d("__SUCCESS", "PHOTO " + photoDBPath +  " WAS UPLOADED!");
            }
        });

        try {
            Log.d("__RESPONse", GVision.callGVis(mCurrentPhotoPath));
        } catch (Exception ex){
            Log.d("__RESPONSE_FAIL", ex.toString());
        }

    }

}
