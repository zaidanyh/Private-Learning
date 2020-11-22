package com.herokuapp.PrivateLearningApp.ui.profile;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.herokuapp.PrivateLearningApp.R;
import com.herokuapp.PrivateLearningApp.ui.main.MainViewModel;
import com.herokuapp.PrivateLearningApp.utils.TokenShared;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileActivity extends FragmentActivity implements OnMapReadyCallback {

    private TextView name, email, date, gender, phone, education, gpa, address;
    private CircleImageView avatar;
    private String value;
    private double latitude, longitude;
    private ProgressDialog progressDialog;
    public static final String KEY_USER = "key_user";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        Button back = findViewById(R.id.btn_back);
        Button to_edit_profile = findViewById(R.id.btn_edit_profile);

        init();

        TokenShared tokenShared = new TokenShared(this);
        MainViewModel viewModel = new ViewModelProvider(this).get(MainViewModel.class);
        progressDialog = new ProgressDialog(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString(KEY_USER);
            if (value.equals("teacher")) {
                viewModel.getProfileTeacher(tokenShared.getToken(), tokenShared.getIdUser());
                viewModel.getTeacherResponse().observe(this, profile -> {
                    Glide.with(this)
                            .load(profile.getPhoto())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_loading)
                            .error(R.drawable.image_error))
                            .into(avatar);
                    name.setText(profile.getName());
                    email.setText(profile.getEmail());
                    if(profile.getLatitude() == null && profile.getLongitude() == null && profile.getBirth_date() == null) {
                        openDialog();
                    } else {
                        date.setText(getResources().getString(R.string.formatText, changeDateFormat(profile.getBirth_date())));
                        gender.setText(getResources().getString(R.string.formatText, profile.getGender()));
                        phone.setText(getResources().getString(R.string.formatText, profile.getPhone()));
                        education.setText(getResources().getString(R.string.formatText, profile.getEducation()));
                        gpa.setText(getResources().getString(R.string.formatText, profile.getGpa()));
                        address.setText(getResources().getString(R.string.formatText, profile.getAddress()));
                        latitude = profile.getLatitude();
                        longitude = profile.getLongitude();
                    }
                });
            } else {
                LinearLayout linearLayoutGPA = findViewById(R.id.linear_gpa);
                TextView text_education = findViewById(R.id.text_education_or_class);
                linearLayoutGPA.setVisibility(View.GONE);
                text_education.setText(getResources().getString(R.string.grade));
                viewModel.getProfileStudent(tokenShared.getToken(), tokenShared.getIdUser());
                viewModel.getStudentResponse().observe(this, profile -> {
                    Glide.with(this)
                            .load(profile.getPhoto())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_loading)
                            .error(R.drawable.image_error))
                            .into(avatar);
                    name.setText(profile.getName());
                    email.setText(profile.getEmail());
                    if(profile.getLatitude() == null && profile.getLongitude() == null && profile.getBirth_date() == null) {
                        openDialog();
                    } else {
                        date.setText(getResources().getString(R.string.formatText, changeDateFormat(profile.getBirth_date())));
                        gender.setText(getResources().getString(R.string.formatText, profile.getGender()));
                        phone.setText(getResources().getString(R.string.formatText, profile.getPhone()));
                        education.setText(getResources().getString(R.string.formatText, profile.getGradeStudy()));
                        address.setText(getResources().getString(R.string.formatText, profile.getAddress()));
                        latitude = profile.getLatitude();
                        longitude = profile.getLongitude();
                    }
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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.google_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(ProfileActivity.this);
        }

        back.setOnClickListener(view -> finish());
        to_edit_profile.setOnClickListener(view -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra(EditProfileActivity.KEY_USER, value);
            startActivity(intent);
            finish();
        });
    }

    private void init() {
        avatar = findViewById(R.id.avatar_profile);
        name = findViewById(R.id.name_profile);
        email = findViewById(R.id.email_profile);
        date = findViewById(R.id.birthDate_profile);
        gender = findViewById(R.id.gender_profile);
        phone = findViewById(R.id.phone_profile);
        education = findViewById(R.id.education_profile);
        gpa = findViewById(R.id.gpa_profile);
        address = findViewById(R.id.address_profile);
    }

    private String changeDateFormat(String date) {
        String newDate = "";
        if (date == null || date.isEmpty()) {
            openDialog();
        } else {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
            Date oldDate;
            try {
                oldDate = simpleDateFormat.parse(date);
                SimpleDateFormat newDateFormat = new SimpleDateFormat("dd MMMM yyyy", Locale.UK);
                newDate = newDateFormat.format(Objects.requireNonNull(oldDate));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return newDate;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        LatLng location = new LatLng(latitude, longitude);
        googleMap.addMarker(new MarkerOptions().position(location).title("Lokasimu"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(location));

    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Ada kesalahan")
                .setMessage("Anda perlu mengedit Akun anda")
                .setPositiveButton("OK", (dialog, i) -> {
                    Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
                    intent.putExtra(EditProfileActivity.KEY_USER, value);
                    startActivity(intent);
                    finish();
                });
        builder.show();
        builder.setCancelable(false);
    }

    private void openLoadingDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
    }
}