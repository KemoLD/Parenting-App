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

import com.cmpt276.teal.parentingpro.data.AppDataKey;
import com.cmpt276.teal.parentingpro.data.DataUtil;
import com.cmpt276.teal.parentingpro.model.ChildManager;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ChildTab extends AppCompatActivity {

    private int imageID;
    private int position;
    private Bundle extras;
    private String name;
    private Bitmap profilepic;
    private Bitmap newProfilepic;
    private ImageButton exit;
    private TextView display;
    private Button edit;
    private EditText editText;
    private Button takephoto;
    private Button gallery;
    private ImageView profile;
    static final int REQUEST_IMAGE_CAPTURE = 2;
    static final int REQUEST_IMAGE_GALLERY = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_child_tab);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        extras = getIntent().getExtras();

        if (extras != null) {
            name = extras.getString("name");
            position = extras.getInt("pos");
            byte[] byteArray = extras.getByteArray("profile");
            profilepic = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
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
                    edit.setText(R.string.save_button_text);
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
                startActivityForResult(gallery, REQUEST_IMAGE_GALLERY);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.i("tag", "result from camaera or gallery");
        if(requestCode == REQUEST_IMAGE_CAPTURE && resultCode == Activity.RESULT_OK){
            Log.i("tag", "result from camaera");
            Bundle extras = data.getExtras();
            newProfilepic = (Bitmap)extras.get("data");
            profile.setImageBitmap(newProfilepic);
        }
        if(requestCode == REQUEST_IMAGE_GALLERY && resultCode == Activity.RESULT_OK){
            Log.i("tag", "result from gallery");
            Uri imageUri = data.getData();
            try {
                newProfilepic = MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            profile.setImageBitmap(newProfilepic);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent();
            if((!extras.getString("name").equals(name)) || newProfilepic != profilepic){
                if (!extras.getString("name").equals(name)) {
                    intent.putExtra("name", name);
                }
                if( newProfilepic != null && newProfilepic != profilepic){

                    try {
                        int scaleWidth = newProfilepic.getWidth() / 4;
                        int scaleHeight = newProfilepic.getHeight() / 4;
                        byte[] downsizedImageBytes =
                                getDownsizedImageBytes(newProfilepic, scaleWidth, scaleHeight);
                        intent.putExtra("profile",downsizedImageBytes);
                        saveImage(newProfilepic);
                    }
                    catch (IOException ioEx){
                        ioEx.printStackTrace();
                    }
                }
                intent.putExtra("pos",position);
                setResult(Activity.RESULT_OK, intent);
                finish();
            }
            else {
                setResult(Activity.RESULT_CANCELED, intent);
                finish();
            }

        }
        return super.onOptionsItemSelected(item);
    }

    public byte[] getDownsizedImageBytes(Bitmap fullBitmap, int scaleWidth, int scaleHeight) throws IOException {

        Bitmap scaledBitmap = Bitmap.createScaledBitmap(fullBitmap, scaleWidth, scaleHeight, true);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] downsizedImageBytes = baos.toByteArray();

        return downsizedImageBytes;
    }

    // save the bitmap to the disk and also update the child in child manager
    private void saveImage(Bitmap bitmap)
    {
        Log.i("tag", "enter saveImage");
        // get the image name for saving on the disk
        String imageName = AppDataKey.INAME_NAME_BASE + imageID;
        ChildManager childManager = ChildManager.getInstance();

        FileOutputStream out = null;
        try {
            // get the output stream for the file
            out = openFileOutput(imageName, MODE_PRIVATE);

            // cover bitmap to byte array
            ByteArrayOutputStream bStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, bStream);
            byte[] byteArray = bStream.toByteArray();
            childManager.getChild(position).setImageFileName(imageName);
            Log.i("tag", "save image name = " + childManager.getChild(position).getImageFileName());

            // update id for saving next image if need
            imageID++;
            DataUtil.writeOneIntData(ChildTab.this, AppDataKey.IMAGE_ID, imageID);

            out.write(byteArray);
            out.flush();
        } catch (FileNotFoundException e) {
            Log.i("tag", "flie not found" + e.toString());
            e.printStackTrace();
        } catch (IOException e) {
            Log.i("tag", "IO execption " + e.toString());
            e.printStackTrace();
        }
        finally {
            if(out != null){
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
