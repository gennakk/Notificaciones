package com.example.notificaciones;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.icu.text.UnicodeSetSpanner;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private EditText edNum1,edNum2,edRes;
    private Button btnComprobar;
    private TextView tvCorrectas,tvIncorrectas,textViewOperacion;
    private int aciertos,errores;


    //notificacion
    private NotificationManager notificationManager;
    static final String CANAL_ID ="mi_canal";
    static final int NOTIFICACION_ID =1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        aciertos=0;
        errores=0;
        edNum1 = findViewById(R.id.edNum1);
        edNum2 = findViewById(R.id.edNum2);
        textViewOperacion = findViewById(R.id.textViewOperacion);
        edRes = findViewById(R.id.edRes);
        tvCorrectas = findViewById(R.id.tvCorrectas);
        tvIncorrectas = findViewById(R.id.tvIncorrectas);
        generarAleatorios();



        btnComprobar = findViewById(R.id.btnComprobar);
        btnComprobar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String num1 = edNum1.getText().toString();
                String num2 = edNum2.getText().toString();
                String res = edRes.getText().toString();
                if(res.equals("")){
                    Toast.makeText(getApplicationContext(),"Introduce resultado",Toast.LENGTH_LONG).show();
                }else {

                    String operacion = textViewOperacion.getText().toString();
                    int numero1 = Integer.parseInt(num1);
                    int numero2 = Integer.parseInt(num2);
                    int resultado=0;

                    if(operacion.equals("+")){
                        resultado = numero1+numero2;
                    }else if(operacion.equals("-")){
                        resultado = numero1-numero2;
                    }else if(operacion.equals("x")){
                        resultado = numero1*numero2;
                    }

                    if(Integer.parseInt(res)==resultado){
                        Toast.makeText(getApplicationContext(),"Correcto", Toast.LENGTH_SHORT).show();
                        aciertos++;
                        tvCorrectas.setText(Integer.toString(aciertos));
                    }else{
                        Toast.makeText(getApplicationContext(),"Incorrecto", Toast.LENGTH_SHORT).show();
                        errores++;
                        tvIncorrectas.setText(Integer.toString(errores));
                    }


                    //Crear notificacion
                    if(aciertos==10){
                        //Creamos notificacion
                        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                        //Creamos el canal. SOLO puede hacerse en dispositivos con ver.8 o más.
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel notificationChannel = new NotificationChannel(CANAL_ID, "Mis notificaciones",NotificationManager.IMPORTANCE_HIGH);
                            notificationChannel.setDescription("Descripción del canal");

                            notificationManager.createNotificationChannel(notificationChannel);
                        }
                        NotificationCompat.Builder notificacion = new NotificationCompat.Builder(MainActivity.this,CANAL_ID).setSmallIcon(R.drawable.ic_acierto).setContentTitle("10 Aciertos").setContentText("Has acertado 10 veces!!");
                        notificacion.setPriority(NotificationCompat.PRIORITY_HIGH);
                        notificacion.setTicker("Has acertado 10!!");
                        notificacion.setDefaults(NotificationCompat.DEFAULT_ALL);
                        notificationManager.notify(NOTIFICACION_ID, notificacion.build());
                    }

                    generarAleatorios();
                }
            }
        });


    }

    private void generarAleatorios(){

        edNum1.setText(((int)(Math.random()*101))+"");
        edNum2.setText(((int)(Math.random()*101))+"");

        int operacion = (int)((Math.random()*3)+1);
        String operacionString="";

        switch (operacion){

            case 1:
                operacionString="+";
                break;

            case 2:
                operacionString="-";
                break;

            case 3:
                operacionString="x";
                break;



        }

        textViewOperacion.setText(operacionString);

    }

    @Override
    protected void onActivityResult (int requestCode, int resultCode,Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1234 && resultCode == RESULT_OK) {

            Boolean boolean_resultado;

            boolean_resultado = data.getExtras().getBoolean("resultado");

            if(boolean_resultado)
                tvCorrectas.setText(Integer.parseInt(tvCorrectas.getText().toString())+1+"");
            else{
                tvIncorrectas.setText(Integer.parseInt(tvIncorrectas.getText().toString())+1+"");
            }


        }
    }
}
