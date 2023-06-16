package com.example.app_amst_apisuperheroes;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class lista_heroe extends AppCompatActivity {

    private RequestQueue mQueue;
    List<String> listaHeroes = new ArrayList<>();
    ListView listViewHeroes;
    TextView textViewNumero;
    String nombreHeroe;

    CustomAdapter adapter;

    JSONObject Data;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_heroe);

        mQueue = Volley.newRequestQueue(this);

        listViewHeroes = findViewById(R.id.idListViewHeroes);
        textViewNumero = findViewById(R.id.idTextViewNum);
        List<String> dataList = listaHeroes; // Reemplaza "obtenerDatos()" con tu lista de strings
        adapter = new CustomAdapter(this, dataList);
        listViewHeroes.setAdapter(adapter);
        descargarDatos();

        listViewHeroes.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // Obtener el elemento seleccionado
                String heroeSeleccionado = (String) parent.getItemAtPosition(position);
                Log.d("MiApp", heroeSeleccionado);
                Log.d("MiApp", obtenerDatosPersonaje(heroeSeleccionado).toString());

                // Iniciar la nueva actividad, pasando el elemento seleccionado si es necesario
                Intent intent = new Intent(lista_heroe.this, DatosHeroe.class);
                intent.putExtra("heroe", heroeSeleccionado);
                intent.putExtra("datosHeroe", obtenerDatosPersonaje(heroeSeleccionado).toString());


                startActivity(intent);
            }
        });
    }

    private void descargarDatos(){
        nombreHeroe = getIntent().getStringExtra("heroe");
        // Obtener el nombre
        // Buscar en el servidor en el caso de que si
        // https://superheroapi.com/api/3960965630796560/search/name...
        String url_heroe = "https://superheroapi.com/api/3960965630796560/search/" + nombreHeroe;
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET, url_heroe, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Data = response;
                        System.out.println(response);
                        try {
                            Log.d("MiApp", response.toString());
                            JSONArray resultsArray = response.getJSONArray("results");
                            for (int i = 0; i < resultsArray.length(); i++) {
                                JSONObject heroObject = resultsArray.getJSONObject(i);
                                String name = heroObject.getString("name");
                                Log.d("MiApp", name);
                                listaHeroes.add(name);
                            }
                            if(listaHeroes.size()==0){
                                Intent intent = new Intent(lista_heroe.this, MainActivity.class);

                                startActivity(intent);
                            }else{}

                            textViewNumero.setText("Resultado: "+String.valueOf(listaHeroes.size()));
                            adapter.notifyDataSetChanged();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                return params;
            }
        };

        mQueue.add(request);
    }

    private JSONObject obtenerDatosPersonaje(String nombre) {
        try {

            JSONArray results = Data.getJSONArray("results");
            for (int i = 0; i < results.length(); i++) {
                JSONObject personaje = results.getJSONObject(i);
                if (personaje.getString("name").equalsIgnoreCase(nombre)) {
                    return personaje;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

}