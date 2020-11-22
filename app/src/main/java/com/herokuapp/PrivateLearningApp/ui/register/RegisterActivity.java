package com.herokuapp.PrivateLearningApp.ui.register;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.herokuapp.PrivateLearningApp.R;
import com.herokuapp.PrivateLearningApp.model.request.RegisterRequest;

public class RegisterActivity extends AppCompatActivity {

    public static final String KEY_REGISTER = "key_register";
    private TextInputLayout layout_email, layout_name, layout_password;
    private EditText edtText_email, edtText_name, edtText_password;
    private Button btn_register;
    private String value;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString(KEY_REGISTER);
        }
        btn_register.setOnClickListener(view -> onValidate(value));
    }

    private void init() {
        layout_email = findViewById(R.id.email_textInput_layout);
        edtText_email = findViewById(R.id.edtText_email);
        layout_name = findViewById(R.id.name_textInput_layout);
        edtText_name = findViewById(R.id.edtText_name);
        layout_password = findViewById(R.id.password_textInput_layout);
        edtText_password = findViewById(R.id.edtText_password);
        btn_register = findViewById(R.id.btn_regis);

        progressDialog = new ProgressDialog(this);
    }

    private void  onValidate(String user) {
        if (edtText_name.getText().toString().isEmpty()) {
            layout_name.setError("Nama masih kosong");
        }  else if (edtText_email.getText().toString().isEmpty()) {
            layout_email.setError("Email masih kosong!");
        } else if (!Patterns.EMAIL_ADDRESS.matcher(edtText_email.getText().toString()).matches()) {
            layout_email.setError("Email tidak ditemukan!");
        } else if (edtText_password.getText().toString().isEmpty()) {
            layout_password.setError("Password masih kosong!");
        } else if (edtText_password.getText().toString().length() < 6 && edtText_password.getText().toString().length() > 0) {
            layout_password.setError("Password terlalu pendek!");
        } else {
            onProcessTeacherRegistration(edtText_name.getText().toString().trim(), edtText_email.getText().toString().trim(), edtText_password.getText().toString(), user);
            openProgressDialog();
        }
    }

    private void onProcessTeacherRegistration(String name, String email, String password, String user) {
        RegisterRequest registerRequest = new RegisterRequest(name, email, password);
        RegisterViewModel viewModel = new ViewModelProvider(RegisterActivity.this).get(RegisterViewModel.class);
        if (user.equals("student")) {
            viewModel.registerStudent(registerRequest);
        } else {
            viewModel.registerTeacher(registerRequest);
        }
        viewModel.getMessage().observe(RegisterActivity.this, message-> {
            if (message.equals("registered") || message.equals("Created")) {
                Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(RegisterActivity.this, "Registrasi Gagal!", Toast.LENGTH_SHORT).show();
            }
            progressDialog.dismiss();
        });
    }

    private void openProgressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
    }
}