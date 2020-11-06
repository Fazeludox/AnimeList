package com.example.myanimelist;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class AlertaPersonalizada extends Dialog implements android.view.View.OnClickListener {

    public Activity a;
    public Dialog f;
    public Button Yes, No;

    public AlertaPersonalizada(Activity a) {
        super(a);
        this.a = a;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_alerta_personalizada);

        Yes = findViewById(R.id.btn_yes);
        No = findViewById(R.id.btn_no);

        Yes.setOnClickListener(this);
        No.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_yes:
                Intent i = new Intent(a, SinPrivilegios.class);

                Bundle q = new Bundle();
                q.putString("IP", Loguearse.IP);
                q.putString("Puerto", Loguearse.Puerto);
                q.putString("BaseDatos", Loguearse.BaseDatos);
                q.putString("Usuario", Loguearse.Usuario);
                q.putString("Contra", Loguearse.Contra);

                i.putExtras(q);

                a.startActivity(i);

                a.finish();
                break;
            case R.id.btn_no:
                dismiss();
            default:
                break;
        }
        dismiss();
    }


}
