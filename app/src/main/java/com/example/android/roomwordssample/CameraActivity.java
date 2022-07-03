package com.example.android.roomwordssample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class CameraActivity extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 101;
    private static final int REQUEST_IMAGE_GALLERY = 40;
    public static final String EXTRA_DATA = "EXTRA_DATA";
    private static final int REQUEST_RESPONSE = 1;
    private ImageView imageView;
    private ImageView imageView2;
    private Context context;
    String pathImg;
    Uri pathUrlGalleryImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        imageView=findViewById(R.id.imageView);
        imageView2=findViewById(R.id.imgView);

        final Button buttonCamera = findViewById(R.id.OpenCameraButton);
        buttonCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                takePicture(view);
            }
        });

        final Button buttonGallery = findViewById(R.id.OpenGalleryButton);
        buttonGallery.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                selectFromPhotoGallery(view);
            }
        });

        final Button buttonCancel = findViewById(R.id.cancelIBackButton);
        buttonCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                onBackPressed();
                finish();
            }
        });
    }

    public void selectFromPhotoGallery(View view) {
        Intent imageGalleryIntent = new Intent(Intent.ACTION_PICK);
        imageGalleryIntent.setType("image/*");
        startActivityForResult(imageGalleryIntent, REQUEST_IMAGE_GALLERY);
    }

    public void takePicture(View view) {
        Intent imageTakeIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(imageTakeIntent.resolveActivity(getPackageManager()) !=null) {
            File imgFile=null;
            try{
                imgFile=createImage();
                Toast.makeText(view.getContext(), "Save correctly the new img in store from camera"+view.getContext(), Toast.LENGTH_SHORT).show();
            } catch (IOException ex) {
                Toast.makeText(view.getContext(), "Error img"+view.getContext(), Toast.LENGTH_SHORT).show();
            }

            if (imgFile!=null){
                Uri urlPhoto=FileProvider.getUriForFile(view.getContext(),"com.example.android.roomwordssample.fileprovider",imgFile);
                imageTakeIntent.putExtra(MediaStore.EXTRA_OUTPUT, urlPhoto);
            }
            startActivityForResult(imageTakeIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private File createImage() throws IOException{
        String nombreImg="Img_";
        File pathDirectory=getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File imgFile=File.createTempFile(nombreImg,".jpg",pathDirectory);
        pathImg=imgFile.getAbsolutePath();
        return imgFile;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            //Bundle extras = data.getExtras();
            //Bitmap imageBitmap = (Bitmap) extras.get("data");
            Bitmap imageBitmap = BitmapFactory.decodeFile(pathImg);
            imageView.setImageBitmap(imageBitmap);
            Intent intent = new Intent();
            //intent.putExtra(EXTRA_DATA,imageBitmap);
            intent.putExtra(EXTRA_DATA,"Foto camara");
            setResult(RESULT_OK, intent);
        }
            else if (requestCode == REQUEST_IMAGE_GALLERY && resultCode == RESULT_OK) {
            pathUrlGalleryImg = data.getData();
            imageView.setImageURI(pathUrlGalleryImg);
            Intent intent = new Intent();
            intent.putExtra(EXTRA_DATA,"Foto gallery");
            //intent.putExtra(EXTRA_DATA,pathUrlGalleryImg);
            setResult(RESULT_OK, intent);

        }
    }
}