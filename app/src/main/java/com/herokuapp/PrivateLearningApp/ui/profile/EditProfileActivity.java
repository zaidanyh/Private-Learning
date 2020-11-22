package com.herokuapp.PrivateLearningApp.ui.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.lifecycle.ViewModelProvider;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.herokuapp.PrivateLearningApp.R;
import com.herokuapp.PrivateLearningApp.model.request.StudentUpdateRequest;
import com.herokuapp.PrivateLearningApp.model.request.TeacherUpdateRequest;
import com.herokuapp.PrivateLearningApp.ui.main.MainViewModel;
import com.herokuapp.PrivateLearningApp.utils.TokenShared;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class EditProfileActivity extends AppCompatActivity {

    public static final String KEY_USER = "key_user";
    private TokenShared tokenShared;
    private LinearLayout linearLayout;
    private TextInputLayout name_layout, phone_layout, date_layout, education_layout, gpa_layout, grade_layout, address_layout;
    private EditText name, phone, date, education, gpa, address, grade;
    private RadioGroup radioGroup;
    private Button dateInput, update, back;
    private ProgressDialog progressDialog;
    private MainViewModel viewModel;
    private SupportMapFragment supportMapFragment;
    private FusedLocationProviderClient client;
    private String value, latitude, longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        init();

        progressDialog = new ProgressDialog(this);
        tokenShared = new TokenShared(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString(KEY_USER);
            if (value.equals("teacher")) {
                grade_layout.setVisibility(View.GONE);
                viewModel.getProfileTeacher(tokenShared.getToken(), tokenShared.getIdUser());
                viewModel.getTeacherResponse().observe(this, profile -> {
                    name.setText(profile.getName());
                    phone.setText(profile.getPhone());
                    if (profile.getBirth_date() != null) {
                        dateInput.setEnabled(false);
                        date.setText(profile.getBirth_date());
                    }

                    if (profile.getGender() == null) {
                        radioGroup.clearCheck();
                    } else if (profile.getGender().equals("Laki-laki")) {
                        radioGroup.check(radioGroup.getChildAt(0).getId());
                    } else if (profile.getGender().equals("Perempuan")) {
                        radioGroup.check(radioGroup.getChildAt(1).getId());
                    } else {
                        radioGroup.clearCheck();
                    }
                    education.setText(profile.getEducation());
                    gpa.setText(profile.getGpa());
                    address.setText(profile.getAddress());
                });
            } else {
                linearLayout.setVisibility(View.INVISIBLE);
                viewModel.getProfileStudent(tokenShared.getToken(), tokenShared.getIdUser());
                viewModel.getStudentResponse().observe(this, profile -> {
                    name.setText(profile.getName());
                    phone.setText(profile.getPhone());
                    if (profile.getBirth_date() != null) {
                        dateInput.setEnabled(false);
                        date.setText(profile.getBirth_date());
                    }

                    if (profile.getGender() == null) {
                        radioGroup.clearCheck();
                    } else if (profile.getGender().equals("Laki-laki")) {
                        radioGroup.check(radioGroup.getChildAt(0).getId());
                    } else if (profile.getGender().equals("Perempuan")) {
                        radioGroup.check(radioGroup.getChildAt(1).getId());
                    }
                    grade.setText(profile.getGradeStudy());
                    address.setText(profile.getAddress());
                });
            }
        }

        viewModel.getIsLoading().observe(this, loading -> {
            if (loading) {
                openLoadingDialog();
            } else {
                progressDialog.dismiss();
            }
        });

        supportMapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        client = LocationServices.getFusedLocationProviderClient(this);
        getCurrentLocation();

        back.setOnClickListener(view -> finish());
        dateInput.setOnClickListener(view -> openDateInput());
        update.setOnClickListener(view -> onProcessUpdate());
    }

    private void init() {
        name_layout = findViewById(R.id.name_textInput_layout);
        phone_layout = findViewById(R.id.phone_textInput_layout);
        date_layout = findViewById(R.id.date_textInput_layout);
        education_layout = findViewById(R.id.education_textInput_layout);
        gpa_layout = findViewById(R.id.gpa_textInput_layout);
        grade_layout = findViewById(R.id.grade_textInput_layout);
        address_layout = findViewById(R.id.address_textInput_layout);
        name = findViewById(R.id.edtText_name);
        phone = findViewById(R.id.edtText_phone);
        date = findViewById(R.id.edtText_date);
        education = findViewById(R.id.edttext_education);
        gpa = findViewById(R.id.edtText_gpa);
        grade = findViewById(R.id.edtText_grade);
        address = findViewById(R.id.edtText_address);
        radioGroup = findViewById(R.id.radioGroup_gender);
        linearLayout = findViewById(R.id.linear3);
        dateInput = findViewById(R.id.btn_inputDate);
        update = findViewById(R.id.btn_save);
        back = findViewById(R.id.btn_back);
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> task = client.getLastLocation();
            task.addOnSuccessListener(location -> {
                if (location != null) {
                    supportMapFragment.getMapAsync(googleMap -> {
                        latitude = String.valueOf(location.getLatitude());
                        longitude = String.valueOf(location.getLongitude());
                        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                        MarkerOptions marker = new MarkerOptions().position(latLng)
                                .title("You're There!");
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                        googleMap.addMarker(marker);
                    });
                }
            });
        } else {
            ActivityCompat.requestPermissions(EditProfileActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 111);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 111) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            }
        }
    }

    private void openLoadingDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
    }

    private void openDateInput() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        int mYear = calendar.get(Calendar.YEAR);
        int mMonth = calendar.get(Calendar.MONTH);
        int mDay = calendar.get(Calendar.DAY_OF_MONTH);

        @SuppressLint("SetTextI18n") DatePickerDialog dialog = new DatePickerDialog(EditProfileActivity.this,
                (datePicker, year, month, day) -> {
                    Calendar newCalendar = Calendar.getInstance();
                    newCalendar.set(year, month, day);
                    date.setText(dateFormat.format(newCalendar.getTime()));
                }, mYear, mMonth, mDay);

        dialog.show();
    }

    private void onProcessUpdate() {
        int radioId = radioGroup.getCheckedRadioButtonId();
        RadioButton radioButton = findViewById(radioId);

        if (value.equals("teacher")) {
            onTeacherValidate(radioButton.getText().toString());
        } else {
            onStudentValidate(radioButton.getText().toString());
        }
    }

    private void onTeacherValidate(String gender) {
        if (name.getText().toString().isEmpty()) {
            name_layout.setError("Nama belum diisi!");
        } else if (phone.getText().toString().isEmpty()) {
            phone_layout.setError("No. Hp belum diisi!");
        } else if (date.getText().toString().isEmpty()) {
            date_layout.setError("Tanggal lahir masih kosong");
        } else if (gender == null || gender.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Pilih jenis kelamin terlebih dahulu!", Toast.LENGTH_SHORT).show();
        } else if (education.getText().toString().isEmpty()) {
            education_layout.setError("Pendidikan Terakhir belum diisi!");
        } else if (gpa.getText().toString().isEmpty()) {
            gpa_layout.setError("IPK belum diisi!");
        } else if (address.getText().toString().isEmpty()) {
            address_layout.setError("Alamat belum diisi!");
        } else if ((latitude.isEmpty() && longitude.isEmpty()) || (latitude == null && longitude == null)) {
            getCurrentLocation();
        } else {
            onUpdate(gender);
        }
    }

    private void onStudentValidate(String gender) {
        if (name.getText().toString().isEmpty()) {
            name_layout.setError("Nama belum diisi!");
        } else if (phone.getText().toString().isEmpty()) {
            phone_layout.setError("No. Hp belum diisi!");
        } else if (date.getText().toString().isEmpty()) {
            date_layout.setError("Tanggal lahir masih kosong");
        } else if (gender == null || gender.isEmpty()) {
            Toast.makeText(EditProfileActivity.this, "Pilih jenis kelamin terlebih dahulu!", Toast.LENGTH_SHORT).show();
        } else if (grade.getText().toString().isEmpty()) {
            grade_layout.setError("Anda kelas berapa?");
        } else if (address.getText().toString().isEmpty()) {
            address_layout.setError("Alamat belum diisi!");
        } else if ((latitude.isEmpty() && longitude.isEmpty()) || (latitude == null && longitude == null)) {
            getCurrentLocation();
        } else {
            onUpdate(gender);
        }
    }

    private void onUpdate(String gender) {
        if (value.equals("teacher")) {
            TeacherUpdateRequest request = new TeacherUpdateRequest(name.getText().toString(), phone.getText().toString(),
                    address.getText().toString(), gender, date.getText().toString(), education.getText().toString(),gpa.getText().toString(),
                    latitude, longitude);

            viewModel.updateAccountTeacher(request, tokenShared.getToken(), tokenShared.getIdUser());
            viewModel.getMessage().observe(this, result -> {
                if (result.equals("updated") || result.equals("success")) {
                    Toast.makeText(EditProfileActivity.this, "Profle berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Profile gagal diperbarui", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            StudentUpdateRequest request = new StudentUpdateRequest(name.getText().toString(), phone.getText().toString(),
                    address.getText().toString(), gender, date.getText().toString(), latitude, longitude,
                    grade.getText().toString());

            viewModel.updateAccountStudent(request, tokenShared.getToken(), tokenShared.getIdUser());
            viewModel.getMessage().observe(this, result -> {
                if (result.equals("updated") || result.equals("success")) {
                    Toast.makeText(EditProfileActivity.this, "Profle berhasil diperbarui", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    Toast.makeText(EditProfileActivity.this, "Profile gagal diperbarui", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}