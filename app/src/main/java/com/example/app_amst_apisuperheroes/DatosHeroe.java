package com.example.app_amst_apisuperheroes;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;


import org.json.JSONException;
import org.json.JSONObject;




public class DatosHeroe extends AppCompatActivity {
    String datosheroe;
    String heroe;

    ImageView fotoHeroe;

    TextView NombreCompleto;
    TextView Nombre;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datos_heroe);

        heroe = getIntent().getSerializableExtra("heroe").toString();
        datosheroe = (String) getIntent().getSerializableExtra("datosHeroe");

        fotoHeroe = findViewById(R.id.idImageViewHeroe);
        NombreCompleto = findViewById(R.id.idNombreCompleto);
        Nombre = findViewById(R.id.idNombre);

        Log.d("datosheroe", heroe);
        Log.d("datosheroe", datosheroe);
        loadImage();
        loadNombres();
    }

    private void loadNombres(){
        Nombre.setText(heroe);
        try {
            JSONObject jsonObject = new JSONObject(datosheroe);
            JSONObject biographyObject = jsonObject.getJSONObject("biography");
            String fullName = biographyObject.getString("full-name");
            NombreCompleto.setText(fullName);
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void loadImage() {
        JSONObject json = null;
        try {
            json = new JSONObject(datosheroe);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        String imageUrl = null;
        try {
            imageUrl = json.getJSONObject("image").getString("url");
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
        Picasso.get()
                .load(imageUrl)
                .into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        // Aquí puedes hacer algo con el bitmap si lo necesitas
                        fotoHeroe.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
                        // Aquí puedes manejar un fallo en la carga de la imagen
                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {
                        // Aquí puedes mostrar un placeholder mientras se carga la imagen
                    }
                });
    }

}