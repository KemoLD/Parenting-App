package com.cmpt276.teal.parentingpro;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.model.ChildManager;
import com.cmpt276.teal.parentingpro.ui.ChildManagerUI;
import com.cmpt276.teal.parentingpro.ui.ChildUI;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChildTab extends AppCompatActivity {

    private int imageID;
    private int position;
    private Bundle extras;
    private String name;
    private String newName;
    private Bitmap profilepic;
    private Bitmap newProfilepic;
    private ImageButton exit;
    private TextView display;
    private Button edit;
    private EditText editText;
    private Button takephoto;
    private Button gallery;
    private ImageView profile;
    private ChildManagerUI manager;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_IMAGE_GALLERY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_tab);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        extras = getIntent().getExtras();
        manager = ChildManagerUI.getInstance(this);

        if (extras != null) {
            name = extras.getString("name");
            position = extras.getInt("pos");
//            byte[] byteArray = extras.getByteArray("profile");
//            profilepic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            profilepic = manager.getChild(position).getProfile();
        }
        imageID = DataUtil.getIntData(ChildTab.this, AppDataKey.IMAGE_ID);
        imageID = imageID == DataUtil.DEFAULT_INT_VALUE ? 0 : imageID;

        profile = findViewById(R.id.popup_profile_pic);
        display = findViewById(R.id.popup_text);
        edit = findViewById(R.id.popup_edit);
        editText = findViewById(R.id.popup_edit_name);
        takephoto = findViewById(R.id.popup_take_photo);
        gallery = findViewById(R.id.popup_gallery);

        display.setText(name);
        editText.setText(name);
        profile.setImageBitmap(profilepic);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (display.getVisibility() == View.VISIBLE) {
                    display.setVisibility(View.INVISIBLE);
                    editText.setVisibility(View.VISIBLE);
                    edit.setText(R.string.set);
                } else {
                    if (editText.getText().toString().length() == 0 ) {
                        return;
                    }
                    else {
                        name = editText.getText().toString();
                        display.setText(name);
                        editText.setText(name);
                        edit.setText(R.string.edit_button_text);
                        editText.setVisibility(View.INVISIBLE);
                        display.setVisibility(View.VISIBLE);
                    }
                }
            }
        });

        takephoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent takepicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                try {
                    startActivityForResult(takepicture, REQUEST_IMAGE_CAPTURE);
                } catch (ActivityNotFoundException e) {
                    Toast.makeText(getBaseContext(), "Camera is not connected", Toast.LENGTH_SHORT).show();
                }
            }
        });

        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                gallery.setType("image/*");
                startActivityForResult(gallery, REQUEST_IMAGE_GALLERY);

            }
        });

        Button save = (Button)findViewById(R.id.toolbar_save);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newName = name;
                newProfilepic = profilepic;
                Toast.makeText(getBaseContext(), "saved", Toast.LENGTH_SHORT).show();
            }
        });

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            if((!extras.getString("name").equals(name)) || newProfilepic != profilepic){
                if (!extras.getString("name").equals(name)) {
                    intent.putExtra("name", name);
                }
                if( newProfilepic != null && newProfilepic != profilepic){

//                        ByteArrayOutputStream bStream = new ByteArrayOutputStream();
//                        newProfilepic.compress(Bitmap.CompressFormat.PNG, 100, bStream);
//                        byte[] downsizedImageBytes = bStream.toByteArray();;
//                        intent.putExtra("profile",downsizedImageBytes);

                    manager.getChild(position).setProfile(newProfilepic);
                    saveImageBytes(newProfilepic);
                }
                intent.putExtra("pos",position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else {
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag", "result from camera or gallery");
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Log.i("tag", "result from camera");
            Bundle extras = data.getExtras();
            profilepic = (Bitmap)extras.get("data");
            profile.setImageBitmap(profilepic);
        }
        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK){
            Log.i("tag", "result from gallery");
            Uri imageUri = data.getData();
            try {
                profilepic = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profile.setImageBitmap(profilepic);
        }
    }

    // save the bitmap to the disk and also update the child in child manager
    private void saveImageBytes(final Bitmap image)
    {
        Log.i("tag", "enter saveImage");
        // get the image name for saving on the disk
        final String imageName = AppDataKey.INAME_NAME_BASE + imageID;
        ChildManagerUI childManager = ChildManagerUI.getInstance(this);
        imageID++;
        DataUtil.writeOneIntData(ChildTab.this, AppDataKey.IMAGE_ID, imageID);
        childManager.getChild(position).setImageFileName(imageName);

        Thread writeImageTask = new Thread(new Runnable() {
            @Override
            public void run() {
                byte[] imageBytes = ChildUI.coverBitmapToBytes(image);
                DataUtil.writeToInternalStorage(ChildTab.this, imageName, imageBytes);
            }
        });

        writeImageTask.start();

    }
}
