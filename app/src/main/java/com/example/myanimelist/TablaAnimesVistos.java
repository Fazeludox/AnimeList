package com.example.myanimelist;

import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public class TablaAnimesVistos extends AppCompatActivity {


    ArrayAdapter adapter;
    private Spinner Selecciona;
    private Button IniciarConsulta;
    private Connection conexionSQL;
    private String Usuario;
    private String Contra;
    private String IP;
    private String Puerto;
    private String BaseDatos;
    private String TablaSelector;
    private String DatosTabla;
    private ListView Listado;
    private List<DatosAnimesVistos> DatosAnimesV;
    private Adaptador mAdaptador;
    private Context mContext;
    private TextView Info1;
    private TextView Info2;
    private TextView Info3;
    private TextView Info4;
    private String D1;
    private String D2;
    private String D3;
    private String D4;

    private String Admin;
    private int nFila;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabla_animes_vistos);

        Bundle bundle = getIntent().getExtras();
        Usuario = bundle.getString("Usuario");
        Contra = bundle.getString("Contra");
        IP = bundle.getString("IP");
        Puerto = bundle.getString("Puerto");
        BaseDatos = bundle.getString("BaseDatos");

        Admin = bundle.getString("Admin");
        nFila = bundle.getInt("nFila");

        Selecciona = findViewById(R.id.SelectColumn);
        IniciarConsulta = findViewById(R.id.Consultar);
        mContext = this;
        Listado = findViewById(R.id.Listado);

        Info1 = findViewById(R.id.Info1);
        Info2 = findViewById(R.id.Info2);
        Info3 = findViewById(R.id.Info3);
        Info4 = findViewById(R.id.Info4);

        Info1.setVisibility(View.GONE);
        Info2.setVisibility(View.GONE);
        Info3.setVisibility(View.GONE);
        Info4.setVisibility(View.GONE);

        Listado.setVisibility(View.GONE);

        DatosTabla = " Anime, Puntuacion, EpiActual, EpiUltimo ";

        RellenarSpinner();
        new ConsultaRellenar().execute();

        IniciarConsulta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                TablaSelector = Selecciona.getSelectedItem().toString();
                switch (TablaSelector) {
                    case "Anime":
                        Listado.setVisibility(View.VISIBLE);
                        DatosTabla = "Anime";
                        new ConsultaAnimesVistos().execute();
                        break;
                    case "Puntuacion":
                        Listado.setVisibility(View.VISIBLE);
                        DatosTabla = "Puntuacion";
                        new ConsultaAnimesVistos().execute();
                        break;
                    case "Episodio Actual":
                        Listado.setVisibility(View.VISIBLE);
                        DatosTabla = "EpiActual";
                        new ConsultaAnimesVistos().execute();
                        break;
                    case "Ultimo Episodio":
                        Listado.setVisibility(View.VISIBLE);
                        DatosTabla = "EpiUltimo";
                        new ConsultaAnimesVistos().execute();
                        break;
                    default:
                        conexionSQL = null;
                        Toast.makeText(getApplicationContext(), "Selecciona una tabla", Toast.LENGTH_LONG).show();
                }

            }
        });

        Listado.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int pos, long id) {
                Info1.setVisibility(View.VISIBLE);
                Info2.setVisibility(View.VISIBLE);
                Info3.setVisibility(View.VISIBLE);
                Info4.setVisibility(View.VISIBLE);

                Info1.setText("Anime:  " + ((DatosAnimesVistos) a.getItemAtPosition(pos)).getAnimeVisto());
                Info2.setText("Puntacion:  " + ((DatosAnimesVistos) a.getItemAtPosition(pos)).getPuntuacion());
                Info3.setText("Episodio actual:  " + ((DatosAnimesVistos) a.getItemAtPosition(pos)).getEpisodioActual());
                Info4.setText("Ultimo episodio:  #" + ((DatosAnimesVistos) a.getItemAtPosition(pos)).getEpisodioUltimo());
            }
        });
    }

    private void RellenarSpinner() {
        String[] tablas = {
                "Selecciona una tabla", "Anime", "Puntuacion", "Episodio Actual", "Ultimo Episodio"
        };
        /* La capa es la basica pero podemos crear una y modificarla*/
        Selecciona.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tablas));

    }

    private class ConsultaAnimesVistos extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            TablaSelector = " ";
            mAdaptador = null;
            try {

                if (DatosAnimesV == null) {
                    DatosAnimesV = new LinkedList<DatosAnimesVistos>();
                    if (conexionSQL == null) {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                    }

                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT " + DatosTabla + " FROM AnimesVisto";

                    ResultSet rs = st.executeQuery(SQL);

                    while (rs.next()) {
                        DatosAnimesVistos AnimeVistosL = new DatosAnimesVistos();

                        if (DatosTabla.equals("Anime")) {
                            AnimeVistosL.setAnimeVisto(rs.getString("Anime"));
                        }
                        if (DatosTabla.equals("Puntuacion")) {
                            AnimeVistosL.setPuntuacion(rs.getString("Puntuacion"));
                        }

                        if (DatosTabla.equals("EpiActual")) {
                            AnimeVistosL.setEpisodioActual(rs.getString("EpiActual"));
                        }

                        if (DatosTabla.equals("EpiUltimo")) {
                            AnimeVistosL.setEpisodioUltimo(rs.getString("EpiUltimo"));
                        }

                        if (DatosTabla.equals(" Anime, Puntuacion, EpiActual, EpiUltimo ")) {
                            AnimeVistosL.setAnimeVisto(rs.getString("Anime"));
                            AnimeVistosL.setPuntuacion(rs.getString("Puntuacion"));
                            AnimeVistosL.setEpisodioActual(rs.getString("EpiActual"));
                            AnimeVistosL.setEpisodioUltimo(rs.getString("EpiUltimo"));
                        }

                        DatosAnimesV.add(AnimeVistosL);
                    }
                }
            } catch (Exception e) {

                D1 = "OCURRIO UN ERROR";
                D2 = "OCURRIO UN ERROR";
                D3 = "OCURRIO UN ERROR";
                D4 = "OCURRIO UN ERROR";

            }

            return null;
        }

        protected void onPostExecute(Void result) {
            mAdaptador = new Adaptador(mContext, R.layout.mostrar_datos_animes_vistos, DatosAnimesV);
            Listado.setAdapter(mAdaptador);
        }
    }

    private class Adaptador extends ArrayAdapter<DatosAnimesVistos> {
        public Adaptador(Context context, int textViewResourceId, List<DatosAnimesVistos> objects) {
            super(context, textViewResourceId, objects);
        }

        @Override
        public View getView(int pos, View convertView, ViewGroup parent) {
            return getViewOptimize(pos, convertView, parent);
        }

        public View getViewOptimize(int pos, View convertView, ViewGroup parent) {
            Adaptador.ViewHolder viewHolder = null;

            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);

                convertView = inflater.inflate(R.layout.mostrar_datos_animes_vistos, null);
                viewHolder = new Adaptador.ViewHolder();

                if (DatosTabla.equals("Anime")) {
                    viewHolder.mostrarAV = convertView.findViewById(R.id.mostrarAnimeVisto);
                }

                if (DatosTabla.equals("Puntuacion")) {
                    viewHolder.mostrarP = convertView.findViewById(R.id.mostrarPuntuacion);
                }

                if (DatosTabla.equals("EpiActual")) {
                    viewHolder.mostrarEPA = convertView.findViewById(R.id.mostrarEpiActual);
                }

                if (DatosTabla.equals("EpiUltimo")) {
                    viewHolder.mostrarEPU = convertView.findViewById(R.id.mostrarEpiUltimo);
                }

                if (DatosTabla.equals(" Anime, Puntuacion, EpiActual, EpiUltimo ")) {
                    viewHolder.mostrarAV = convertView.findViewById(R.id.mostrarAnimeVisto);
                    viewHolder.mostrarP = convertView.findViewById(R.id.mostrarPuntuacion);
                    viewHolder.mostrarEPA = convertView.findViewById(R.id.mostrarEpiActual);
                    viewHolder.mostrarEPU = convertView.findViewById(R.id.mostrarEpiUltimo);

                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (Adaptador.ViewHolder) convertView.getTag();
            }

            DatosAnimesVistos Valor = getItem(pos);
            if (DatosTabla.equals("Anime")) {
                viewHolder.mostrarAV.setText(Valor.getAnimeVisto());
            }
            if (DatosTabla.equals("Puntuacion")) {
                viewHolder.mostrarP.setText(Valor.getPuntuacion());
            }
            if (DatosTabla.equals("EpiActual")) {
                viewHolder.mostrarEPA.setText(Valor.getEpisodioActual());
            }
            if (DatosTabla.equals("EpiUltimo")) {
                viewHolder.mostrarEPU.setText(Valor.getEpisodioUltimo());
            }
            if (DatosTabla.equals(" Anime, Puntuacion, EpiActual, EpiUltimo ")) {
                viewHolder.mostrarAV.setText(Valor.getAnimeVisto());
                viewHolder.mostrarP.setText(Valor.getPuntuacion());
                viewHolder.mostrarEPA.setText(Valor.getEpisodioActual());
                viewHolder.mostrarEPU.setText(Valor.getEpisodioUltimo());

            }
            return convertView;
        }

        private class ViewHolder {
            public TextView mostrarAV;
            public TextView mostrarP;
            public TextView mostrarEPA;
            public TextView mostrarEPU;

        }
    }

    private class ConsultaRellenar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            TablaSelector = " ";
            mAdaptador = null;

            try {
                if (DatosAnimesV == null) {
                    DatosAnimesV = new LinkedList<DatosAnimesVistos>();

                    if (conexionSQL == null) {
                        DriverManager.setLoginTimeout(4);

                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);

                    }
                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT " + DatosTabla + " FROM AnimesVisto";

                    ResultSet rs = st.executeQuery(SQL);
                    while (rs.next()) {
                        DatosAnimesVistos AnimeVistosL = new DatosAnimesVistos();

                        if (DatosTabla.equals(" Anime, Puntuacion, EpiActual, EpiUltimo ")) {
                            AnimeVistosL.setAnimeVisto(rs.getString("Anime"));
                            AnimeVistosL.setPuntuacion(rs.getString("Puntuacion"));
                            AnimeVistosL.setEpisodioActual(rs.getString("EpiActual"));
                            AnimeVistosL.setEpisodioUltimo(rs.getString("EpiUltimo"));
                        }

                        DatosAnimesV.add(AnimeVistosL);
                    }
                }

            } catch (Exception e) {
                D1 = "Fallo en conexion a BD";
                D2 = "Fallo en conexion a BD";
                D3 = "Fallo en conexion a BD";
                D4 = "Fallo en conexion a BD";
            }
            return null;
        }

        protected void onPostExecute(Void result) {

            mAdaptador = new Adaptador(mContext, R.layout.mostrar_datos_animes_vistos, DatosAnimesV);
            Listado.setAdapter(mAdaptador);

            if (Admin != null) {
                Listado.setVisibility(View.VISIBLE);

                Selecciona.setSelection(1);
                TablaSelector = Selecciona.getSelectedItem().toString();

                if (TablaSelector.equals("Anime")) {
                    DatosTabla = "Anime";
                    Admin = null;
                    new ConsultaAnimesVistos().execute();
                    new ConsultaAdministrador().execute();
                }
            }
        }
    }

    private class ConsultaAdministrador extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                if (conexionSQL == null) {
                    Class.forName("com.mysql.jdbc.Driver").newInstance();
                    conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                }

                Statement st = conexionSQL.createStatement();
                String SQL = "SELECT * FROM AnimesVisto";

                int i = 0;
                int Existe = 0;

                ResultSet rs = st.executeQuery(SQL);

                while (rs.next()) {
                    if ((i + 1) == nFila) {
                        Existe = 1;
                        D1 = rs.getString("Anime");
                        D2 = rs.getString("Puntuacion");
                        D3 = rs.getString("EpiActual");
                        D4 = rs.getString("EpiUltimo");
                    }
                    i = i + 1;
                }

                if (Existe == 0) {
                    D1 = "No existe la fila";
                    D2 = "No existe la fila";
                    D3 = "No existe la fila";
                    D4 = "No existe la fila";
                }
            } catch (Exception e) {

                D1 = "OCURRIO UN ERROR DESDE ADMIN";
                D2 = "OCURRIO UN ERROR DESDE ADMIN";
                D3 = "OCURRIO UN ERROR DESDE ADMIN";
                D4 = "OCURRIO UN ERROR DESDE ADMIN";
                e.printStackTrace();

            }
            return null;
        }

        protected void onPostExecute(Void result) {
            Info1.setVisibility(View.VISIBLE);
            Info2.setVisibility(View.VISIBLE);
            Info3.setVisibility(View.VISIBLE);
            Info4.setVisibility(View.VISIBLE);

            Info1.setText("Anime: " + D1);
            Info2.setText("Puntuacion: " + D2);
            Info3.setText("EpiActual: " + D3);
            Info4.setText("EpiUltimo: " + D4);
        }
    }
}

