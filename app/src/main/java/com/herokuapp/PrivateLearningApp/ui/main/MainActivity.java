package com.herokuapp.PrivateLearningApp.ui.main;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.herokuapp.PrivateLearningApp.R;
import com.herokuapp.PrivateLearningApp.ui.login.LoginActivity;
import com.herokuapp.PrivateLearningApp.ui.profile.EditProfileActivity;
import com.herokuapp.PrivateLearningApp.ui.profile.ProfileActivity;
import com.herokuapp.PrivateLearningApp.utils.TokenShared;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity {

    private AppBarConfiguration appBarConfiguration;
    private TokenShared tokenShared;
    private ProgressDialog progressDialog;
    private CircleImageView avatar;
    private TextView name, email;
    private MainViewModel viewModel;
    public static final String KEY_USER = "key_user";
    private String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DrawerLayout drawerLayout = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        NavController controller = Navigation.findNavController(this, R.id.nav_host_fragment);
        FloatingActionButton fab = findViewById(R.id.fab);

        tokenShared = new TokenShared(this);
        progressDialog = new ProgressDialog(this);
        viewModel = new ViewModelProvider(this).get(MainViewModel.class);

        avatar = navigationView.getHeaderView(0).findViewById(R.id.avatar_profile);
        name = navigationView.getHeaderView(0).findViewById(R.id.fullname_profile);
        email = navigationView.getHeaderView(0).findViewById(R.id.email_profile);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            value = bundle.getString(KEY_USER);
            Toolbar toolbar;
            Intent toProfile = new Intent(this, ProfileActivity.class);
            if (value.equals("student")) {
                toolbar = findViewById(R.id.toolbar);
                navigationView.inflateMenu(R.menu.student_drawer);
                controller.setGraph(R.navigation.student_navigation);

                fab.setColorFilter(Color.WHITE);
                fab.setOnClickListener(view -> {
                    //add schedule
                });

                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_list_schedule_student)
                        .setDrawerLayout(drawerLayout)
                        .build();

                viewModel.getProfileStudent(tokenShared.getToken(), tokenShared.getIdUser());
                viewModel.getStudentResponse().observe(this, profile -> {
                    Glide.with(this)
                            .load(profile.getPhoto())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_loading)
                            .error(R.drawable.image_error))
                            .into(avatar);
                    name.setText(profile.getName());
                    email.setText(profile.getEmail());
                    navigationView.getHeaderView(0).setOnClickListener(view -> {
                        toProfile.putExtra(ProfileActivity.KEY_USER, value);
                        startActivity(toProfile);
                    });
                });
            } else {
                toolbar = findViewById(R.id.toolbar);
                navigationView.inflateMenu(R.menu.teacher_drawer);
                controller.setGraph(R.navigation.teacher_navigation);

                fab.setVisibility(View.INVISIBLE);

                appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.nav_list_schedule, R.id.nav_schedule, R.id.nav_absen)
                        .setDrawerLayout(drawerLayout)
                        .build();

                viewModel.getProfileTeacher(tokenShared.getToken(), tokenShared.getIdUser());
                viewModel.getTeacherResponse().observe(this, profile -> {
                    Glide.with(this)
                            .load(profile.getPhoto())
                            .apply(RequestOptions.placeholderOf(R.drawable.image_loading)
                            .error(R.drawable.image_error))
                            .into(avatar);
                    name.setText(profile.getName());
                    email.setText(profile.getEmail());
                    navigationView.getHeaderView(0).setOnClickListener(view -> {
                        toProfile.putExtra(ProfileActivity.KEY_USER, value);
                        startActivity(toProfile);
                    });
                });
            }
            setSupportActionBar(toolbar);
            NavigationUI.setupActionBarWithNavController(this, controller, appBarConfiguration);
            NavigationUI.setupWithNavController(navigationView, controller);
        }

        viewModel.getIsLoading().observe(this, loading->{
            if (loading) {
                openLoadingDialog();
            } else {
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        switch (menu.getItemId()) {
            case R.id.action_edit_profile:
                toEditProfile();
                return true;
            case R.id.logout:
                logOut();
                return true;
            default:
                return super.onOptionsItemSelected(menu);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private void toEditProfile() {
        Intent intent = new Intent(MainActivity.this, EditProfileActivity.class);
        intent.putExtra(EditProfileActivity.KEY_USER, value);
        startActivity(intent);
    }

    private void logOut() {
        viewModel.logout(tokenShared.getToken(), tokenShared.getIdUser(), value);
        viewModel.getMessage().observe(this, message ->{
            if (message.equals("success")) {
                tokenShared.removeToken();
                startActivity(new Intent(MainActivity.this, LoginActivity.class));
                finish();
            }
        });
    }

    private void openLoadingDialog() {
        progressDialog.show();
        progressDialog.setContentView(R.layout.progress_dialog);
        progressDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        progressDialog.setCancelable(false);
    }
}