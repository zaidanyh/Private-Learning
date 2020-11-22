package com.herokuapp.PrivateLearningApp.ui.login;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.herokuapp.PrivateLearningApp.R;
import com.herokuapp.PrivateLearningApp.model.request.LoginRequest;
import com.herokuapp.PrivateLearningApp.model.response.ErrorResponse;
import com.herokuapp.PrivateLearningApp.model.response.LoginResponse;
import com.herokuapp.PrivateLearningApp.ui.register.RegisterActivity;
import com.herokuapp.PrivateLearningApp.ui.main.MainActivity;
import com.herokuapp.PrivateLearningApp.utils.ApiInterface;
import com.herokuapp.PrivateLearningApp.utils.TokenShared;
import com.herokuapp.PrivateLearningApp.utils.helper.ApiError;
import com.herokuapp.PrivateLearningApp.utils.helper.ServiceGenerator;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    private LoginRequest loginRequest;
    private EditText edtText_email, edtText_password;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TokenShared tokenShared;
    private Dialog dialog;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtText_email = findViewById(R.id.edt_email);
        edtText_password = findViewById(R.id.edt_password);
        radioGroup = findViewById(R.id.group_login);
        Button btnLogin = findViewById(R.id.btn_login);
        TextView register = findViewById(R.id.register_text);

        tokenShared = new TokenShared(this);
        progressDialog = new ProgressDialog(this);
        dialog = new Dialog(this);

        register.setOnClickListener(view -> openChooseRegisterDialog());
        btnLogin.setOnClickListener(view -> {
            int radioId = radioGroup.getCheckedRadioButtonId();
            radioButton = findViewById(radioId);
            if (edtText_email.getText().toString().isEmpty()) {
                edtText_email.setError("Masukkan Email kamu");
            } else if (!Patterns.EMAIL_ADDRESS.matcher(edtText_email.getText().toString()).matches()) {
                edtText_email.setError("Masukkan Email kamu dengan benar");
            } else if (edtText_password.getText().toString().isEmpty()) {
                edtText_password.setError("Masukkan Password kamu");
            } else if (edtText_password.getText().length() < 6) {
                edtText_password.setError("Password minimal 6 karakter");
            } else if (radioId == -1) {
                Toast.makeText(this, "Silahkan pilih status anda", Toast.LENGTH_LONG).show();
            } else {
                if (radioButton.getText().equals("Student")) {
                    loginStudent(edtText_email.getText().toString(), edtText_password.getText().toString());
                } else {
                    loginTeacher(edtText_email.getText().toString(), edtText_password.getText().toString());
                }
                openProgressDialog();
            }
        });
    }

    private void loginTeacher(String email, String password) {
        loginRequest = new LoginRequest(email, password);

        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, null);
        Call<LoginResponse> call = request.loginTeach(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tokenShared.setToken(response.body().getToken());
                    tokenShared.setIdUser(response.body().getId());
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.KEY_USER, "teacher");
                    startActivity(intent);
                    finish();
                } else if (response.errorBody() != null) {
                    ErrorResponse error = ApiError.parseError(response);
                    if (error.getMessage() != null) {
                        openDialog();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Ada kesalahan dengan informasi login kamu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Log.e("Login failed", t.getMessage());
            }
        });
    }

    private void loginStudent(String email, String password) {
        loginRequest = new LoginRequest(email, password);

        ApiInterface request = ServiceGenerator.createService(ApiInterface.class, "");
        Call<LoginResponse> call = request.loginStud(loginRequest);
        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(@NotNull Call<LoginResponse> call, @NotNull Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    tokenShared.setToken(response.body().getToken());
                    tokenShared.setIdUser(response.body().getId());
                    progressDialog.dismiss();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra(MainActivity.KEY_USER, "student");
                    startActivity(intent);
                    finish();
                } else if (response.errorBody() != null) {
                    ErrorResponse error = ApiError.parseError(response);
                    if (error.getMessage() != null) {
                        openDialog();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Ada kesalahan dengan informasi login kamu", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(@NotNull Call<LoginResponse> call, @NotNull Throwable t) {
                Log.e("Login failed", t.getMessage());
            }
        });
    }

    private void openChooseRegisterDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Pilih Registrasi");
        builder.setMessage("Pilihlah sesuai dengan status kamu!!");
        Intent intent = new Intent(this, RegisterActivity.class);
        builder.setPositiveButton("Teacher", (dialog, which) -> {
            intent.putExtra(RegisterActivity.KEY_REGISTER, "teacher");
            startActivity(intent);
        });
        builder.setNegativeButton("Student", (dialog, which) -> {
            intent.putExtra(RegisterActivity.KEY_REGISTER, "student");
            startActivity(intent);
        });
        builder.show();
    }

    private void openDialog() {
        dialog.setContentView(R.layout.failed_login);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        Button btn_ok = dialog.findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(view -> dialog.dismiss());

        dialog.show();
        progressDialog.dismiss();
    }

    private void openProgressDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
    }
}