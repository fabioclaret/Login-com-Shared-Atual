package com.fabioclaret.logincomshared;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class HomeActivity extends AppCompatActivity {

    private Button logoff;
    private TextView txtBoasVindas, txtPerfilEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);

        initComponents();

        // 1. Carrega o arquivo SharedPreferences "login"
        SharedPreferences preferences = getSharedPreferences("login", MODE_PRIVATE);

        // 2. Busca os valores salvos (se não existirem, assume o valor padrão da direita)
        String nomeSalvo = preferences.getString("Nome", "Usuário");
        String emailSalvo = preferences.getString("Email", "Não informado");

        // 3. Coloca os valores vindos do arquivo diretamente na tela
        txtBoasVindas.setText(String.format("Olá, %s!", nomeSalvo));
        txtPerfilEmail.setText(String.format("E-mail: %s", emailSalvo));

        // Configuração simplificada do botão com Lambda
        logoff.setOnClickListener(v -> {
            SharedPreferences.Editor dados = preferences.edit();
            dados.putBoolean("remember", false);
            dados.apply();

            Toast.makeText(HomeActivity.this, "Logoff feito com sucesso!", Toast.LENGTH_SHORT).show();
            finishAffinity();
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initComponents() {
        logoff         = findViewById(R.id.btn_logoff);
        txtBoasVindas  = findViewById(R.id.txt_boas_vindas);
        txtPerfilEmail = findViewById(R.id.txt_perfil_email);
    }
}