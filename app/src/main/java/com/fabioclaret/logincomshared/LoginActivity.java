package com.fabioclaret.logincomshared;

import android.content.Intent;
import android.content.SharedPreferences;
import android.icu.text.SimpleDateFormat;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Date;
import java.util.Locale;

public class LoginActivity extends AppCompatActivity {
    EditText email, senha;
    Button entrar, novo;
    CheckBox box;
    SharedPreferences preferences;

    public static final String PREF_NAME = "login";
    public static final String KEY_EMAIL = "Email";
    public static final String KEY_SENHA = "Senha";
    public static final String REMEMBER = "Remember";
    public static final String KEY_DATA = "Data";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        initComponents();

        // Verificar se 'remember' é verdadeiro para ir direto para a HomeActivity
        preferences       = getSharedPreferences("login", MODE_PRIVATE);
        boolean sRemember = preferences.getBoolean("remember", false);
        SharedPreferences.Editor dados = preferences.edit();

        if (sRemember) {
            String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());
            dados.putString("Data", data);
            dados.apply();
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish(); // Fecha a LoginActivity para não voltar nela ao pressionar o botão voltar
        }

        entrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarDados()) {
                    // 1. Buscar os dados cadastrados no SharedPreferences usando as chaves corretas ("Email" e "Senha")
                    String emailCadastrado = preferences.getString("Email", "");
                    String senhaCadastrada = preferences.getString("Senha", "");

                    // 2. Pegar o que o usuário digitou nos campos
                    String emailDigitado = email.getText().toString();
                    String senhaDigitada = senha.getText().toString();

                    // 3. Comparar o digitado com o cadastrado
                    if (emailDigitado.equals(emailCadastrado) && senhaDigitada.equals(senhaCadastrada)) {
                        // Se marcou "Lembrar", podemos guardar um booleano ou atualizar as credenciais

                        String data = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault()).format(new Date());


                        if (box.isChecked()) {
                            dados.putBoolean("remember", true);
                            dados.putString("Email", emailDigitado);
                            dados.putString("Senha", senhaDigitada);
                        } else {
                            dados.putBoolean("remember", false);
                        }
                        dados.putString("Data", data);
                        dados.apply();

                        Toast.makeText(LoginActivity.this, "Login Efetuado", Toast.LENGTH_SHORT).show();

                        // Ir para a tela principal
                        Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                        startActivity(intent);
                        finish(); // Fecha a tela de login para não voltar nela ao apertar o botão "Voltar"
                    } else {
                        // Se as credenciais não baterem
                        Toast.makeText(LoginActivity.this, "Email ou Senha incorretos", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Digite todos os dados", Toast.LENGTH_SHORT).show();
                }
            }
        });

        novo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

    }

    private void initComponents() {
        email = findViewById(R.id.edt_email);
        senha = findViewById(R.id.edt_senha);
        entrar = findViewById(R.id.btn_entrar);
        novo = findViewById(R.id.btn_novo);
        box = findViewById(R.id.box);
    }
    private boolean validarDados() {
        boolean retorno = true;
        if (email.getText().toString().isEmpty()) {
            retorno = false;
            email.setError("Campo email não pode ficar vazio");
        }
        if (senha.getText().toString().isEmpty()) {
            retorno = false;
            senha.setError("Campo senha não pode ficar vazio");
        }
        return retorno;
    }
}