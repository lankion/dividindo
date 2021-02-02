package com.example.dividindo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity implements TextWatcher, View.OnClickListener, TextToSpeech.OnInitListener {

    EditText valorDigitado, quantidadePessoas;
    TextView valorFinal;
    FloatingActionButton fala, compartilha;
    TextToSpeech converteFala;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        valorDigitado =(EditText)findViewById(R.id.valorConta);
        quantidadePessoas = (EditText)findViewById(R.id.nPessoas);
        valorFinal = (TextView)findViewById(R.id.valorDivisao);
        valorDigitado.addTextChangedListener(this);
        quantidadePessoas.addTextChangedListener(this);
        fala = (FloatingActionButton)findViewById(R.id.falaTexto);
        compartilha = (FloatingActionButton)findViewById(R.id.compartilhaTexto);
        fala.setOnClickListener(this);
        compartilha.setOnClickListener(this);

        //Verifica o chamaneto de TTS data
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, 1122);

    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1122){
            if( resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                converteFala = new TextToSpeech(this, this);
            }else{
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    @Override
    public void afterTextChanged(Editable s) {

        try {
            double res = Double.parseDouble(valorDigitado.getText().toString());
            double qtn = Double.parseDouble(quantidadePessoas.getText().toString());
            res = (res /qtn);
            DecimalFormat df = new DecimalFormat("#.00");
            valorFinal.setText("R$" + df.format(res));

        }catch(Exception e){
            valorFinal.setText("R$ 0.00");
        }

    }

    @Override
    public void onClick(View v) {
        if (v == compartilha){
            Intent intent =new Intent(Intent.ACTION_SEND);
            intent.setType("text/plain");
            intent.putExtra(android.content.Intent.EXTRA_TEXT, "O valor para cada pessoa deu" + valorFinal.getText().toString());
            startActivity(Intent.createChooser(intent, "Compartilhando a conta!"));

        }
        if (v == fala){
            if (converteFala!=null){
                converteFala.speak("O valor por pessoa é de " + valorFinal.getText().toString() + "reais", TextToSpeech.QUEUE_FLUSH, null, "ID1");
            }
        }
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS){
            Toast.makeText(this, "TTS ativado", Toast.LENGTH_LONG).show();
        } else if(status == TextToSpeech.ERROR){
            Toast.makeText(this, "Sem TTS habilitado", Toast.LENGTH_LONG).show();
        }
    }
}