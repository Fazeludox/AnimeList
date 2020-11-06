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

public class TablaAnimesPendientes extends AppCompatActivity {

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
    private List<DatosAnimesPendientes> DatosAnimesP;
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
        setContentView(R.layout.activity_tabla_animes_pendientes);

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

        DatosTabla = " Anime, EpiActual, Temporada, Año ";

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
                        new ConsultaAnimesPendientes().execute();
                        break;
                    case "Episodio Actual":
                        Listado.setVisibility(View.VISIBLE);
                        DatosTabla = "EpiActual";
                        new ConsultaAnimesPendientes().execute();
                        break;
                    case "Temporada":
                        Listado.setVisibility(View.VISIBLE);
                        DatosTabla = "Temporada";
                        new ConsultaAnimesPendientes().execute();
                        break;
                    case "Año":
                        Listado.setVisibility(View.VISIBLE);
                        DatosTabla = "Año";
                        new ConsultaAnimesPendientes().execute();
                        break;
                    default:
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

                Info1.setText("Anime:  " + ((DatosAnimesPendientes) a.getItemAtPosition(pos)).getAnimePendiente());
                Info2.setText("Episodio actual:  " + ((DatosAnimesPendientes) a.getItemAtPosition(pos)).getEpisodioActual());
                Info3.setText("Temporada:  " + ((DatosAnimesPendientes) a.getItemAtPosition(pos)).getTemporada());
                Info4.setText("Año:  #" + ((DatosAnimesPendientes) a.getItemAtPosition(pos)).getAño());
            }
        });
    }

    private void RellenarSpinner() {
        String[] tablas = {
                "Selecciona una tabla", "Anime", "Episodio Actual", "Temporada", "Año"
        };

        Selecciona.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, tablas));

    }

    private class ConsultaAnimesPendientes extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            TablaSelector = " ";
            mAdaptador = null;
            try {

                if (DatosAnimesP == null) {
                    DatosAnimesP = new LinkedList<DatosAnimesPendientes>();
                    if (conexionSQL == null) {
                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);
                    }

                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT " + DatosTabla + " FROM AnimesPendientes";

                    ResultSet rs = st.executeQuery(SQL);

                    while (rs.next()) {
                        DatosAnimesPendientes AnimePendientesL = new DatosAnimesPendientes();

                        if (DatosTabla.equals("Anime")) {
                            AnimePendientesL.setAnimePendiente(rs.getString("Anime"));
                        }
                        if (DatosTabla.equals("EpiActual")) {
                            AnimePendientesL.setEpisodioActual(rs.getString("EpiActual"));
                        }

                        if (DatosTabla.equals("Temporada")) {
                            AnimePendientesL.setTemporada(rs.getString("Temporada"));
                        }

                        if (DatosTabla.equals("Año")) {
                            AnimePendientesL.setAño(rs.getString("Año"));
                        }

                        if (DatosTabla.equals(" Anime, EpiActual, Temporada, Año ")) {
                            AnimePendientesL.setAnimePendiente(rs.getString("Anime"));
                            AnimePendientesL.setEpisodioActual(rs.getString("EpiActual"));
                            AnimePendientesL.setTemporada(rs.getString("Temporada"));
                            AnimePendientesL.setAño(rs.getString("Año"));
                        }

                        DatosAnimesP.add(AnimePendientesL);
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
            mAdaptador = new Adaptador(mContext, R.layout.mostrar_datos_animes_pendientes, DatosAnimesP);
            Listado.setAdapter(mAdaptador);
        }
    }

    private class Adaptador extends ArrayAdapter<DatosAnimesPendientes> {
        public Adaptador(Context context, int textViewResourceId, List<DatosAnimesPendientes> objects) {
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

                convertView = inflater.inflate(R.layout.mostrar_datos_animes_pendientes, null);
                viewHolder = new Adaptador.ViewHolder();

                if (DatosTabla.equals("Anime")) {
                    viewHolder.mostrarAP = convertView.findViewById(R.id.mostrarAnimePendiente);
                }

                if (DatosTabla.equals("EpiActual")) {
                    viewHolder.mostrarEPA = convertView.findViewById(R.id.mostrarAPActual);
                }

                if (DatosTabla.equals("Temporada")) {
                    viewHolder.mostraT = convertView.findViewById(R.id.mostrarTemporada);
                }

                if (DatosTabla.equals("Año")) {
                    viewHolder.mostrarY = convertView.findViewById(R.id.mostrarAño);
                }

                if (DatosTabla.equals(" Anime, EpiActual, Temporada, Año ")) {
                    viewHolder.mostrarAP = convertView.findViewById(R.id.mostrarAnimePendiente);
                    viewHolder.mostrarEPA = convertView.findViewById(R.id.mostrarAPActual);
                    viewHolder.mostraT = convertView.findViewById(R.id.mostrarTemporada);
                    viewHolder.mostrarY = convertView.findViewById(R.id.mostrarAño);

                }
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (Adaptador.ViewHolder) convertView.getTag();
            }

            DatosAnimesPendientes Valor = getItem(pos);
            if (DatosTabla.equals("Anime")) {
                viewHolder.mostrarAP.setText(Valor.getAnimePendiente());
            }
            if (DatosTabla.equals("EpiActual")) {
                viewHolder.mostrarEPA.setText(Valor.getEpisodioActual());
            }
            if (DatosTabla.equals("Temporada")) {
                viewHolder.mostraT.setText(Valor.getTemporada());
            }
            if (DatosTabla.equals("Año")) {
                viewHolder.mostrarY.setText(Valor.getAño());
            }
            if (DatosTabla.equals(" Anime, EpiActual, Temporada, Año ")) {
                viewHolder.mostrarAP.setText(Valor.getAnimePendiente());
                viewHolder.mostrarEPA.setText(Valor.getEpisodioActual());
                viewHolder.mostraT.setText(Valor.getTemporada());
                viewHolder.mostrarY.setText(Valor.getAño());


            }
            return convertView;
        }

        private class ViewHolder {
            public TextView mostrarAP;
            public TextView mostrarEPA;
            public TextView mostraT;
            public TextView mostrarY;

        }
    }

    private class ConsultaRellenar extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            TablaSelector = " ";
            mAdaptador = null;

            try {
                if (DatosAnimesP == null) {
                    DatosAnimesP = new LinkedList<DatosAnimesPendientes>();

                    if (conexionSQL == null) {
                        DriverManager.setLoginTimeout(4);

                        Class.forName("com.mysql.jdbc.Driver").newInstance();
                        conexionSQL = DriverManager.getConnection("jdbc:mysql://" + IP + ":" + Puerto + "/" + BaseDatos, Usuario, Contra);

                    }
                    Statement st = conexionSQL.createStatement();
                    String SQL = "SELECT " + DatosTabla + " FROM AnimesPendientes";

                    ResultSet rs = st.executeQuery(SQL);
                    while (rs.next()) {
                        DatosAnimesPendientes AnimePendientesL = new DatosAnimesPendientes();

                        if (DatosTabla.equals(" Anime, EpiActual, Temporada, Año ")) {
                            AnimePendientesL.setAnimePendiente(rs.getString("Anime"));
                            AnimePendientesL.setEpisodioActual(rs.getString("EpiActual"));
                            AnimePendientesL.setTemporada(rs.getString("Temporada"));
                            AnimePendientesL.setAño(rs.getString("Año"));
                        }

                        DatosAnimesP.add(AnimePendientesL);
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

            mAdaptador = new Adaptador(mContext, R.layout.mostrar_datos_animes_pendientes, DatosAnimesP);
            Listado.setAdapter(mAdaptador);

            if (Admin != null) {
                Listado.setVisibility(View.VISIBLE);

                Selecciona.setSelection(1);
                TablaSelector = Selecciona.getSelectedItem().toString();

                if (TablaSelector.equals("Anime")) {
                    DatosTabla = "Anime";
                    Admin = null;
                    new ConsultaAnimesPendientes().execute();
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
                String SQL = "SELECT * FROM AnimesPendientes";

                int i = 0;
                int Existe = 0;

                ResultSet rs = st.executeQuery(SQL);

                while (rs.next()) {
                    if ((i + 1) == nFila) {
                        Existe = 1;
                        D1 = rs.getString("Anime");
                        D2 = rs.getString("EpiActual");
                        D3 = rs.getString("Temporada");
                        D4 = rs.getString("Año");
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
            Info2.setText("Episodio Actual: " + D2);
            Info3.setText("Temporada: " + D3);
            Info4.setText("Año: " + D4);
        }
    }
}
