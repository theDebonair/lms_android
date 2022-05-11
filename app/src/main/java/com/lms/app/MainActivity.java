package com.lms.app;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.lms.app.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        FloatingActionButton fab = binding.appBarMain.fab;
        Vibrator vibrate = (Vibrator) getSystemService(VIBRATOR_SERVICE);

        setSupportActionBar(binding.appBarMain.toolbar);
        getSupportActionBar().hide();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Build.VERSION.SDK_INT >= 26) {
                    vibrate.vibrate(VibrationEffect.createOneShot(100, VibrationEffect.DEFAULT_AMPLITUDE));
                }
                else {
                    vibrate.vibrate(100);
                }
                drawer.openDrawer(GravityCompat.START);
            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @SuppressLint({"NonConstantResourceId", "SetJavaScriptEnabled"})
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                WebView webView = findViewById(R.id.web_view_main);
                ProgressBar progressBar = findViewById(R.id.progressBar);

                webView.setWebViewClient(new WebViewController() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                        webView.setVisibility(View.GONE);
                        progressBar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        super.onPageFinished(view, url);
                        webView.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
                        super.onReceivedError(view, request, error);
                        Toast.makeText(MainActivity.this, "Unable to reach LMS server", Toast.LENGTH_SHORT).show();
                        Toast.makeText(MainActivity.this, "Please retry after sometime", Toast.LENGTH_SHORT).show();
                        System.exit(0);
                    }
                });
                webView.getSettings().setJavaScriptEnabled(true);
                webView.getSettings().setLoadsImagesAutomatically(true);
                webView.getSettings().setDomStorageEnabled(true);
                webView.getSettings().setSupportZoom(false);
                webView.getSettings().setBuiltInZoomControls(false);
                webView.getSettings().setDisplayZoomControls(false);
                webView.setScrollbarFadingEnabled(true);
                switch (id) {
                    case R.id.nav_main:
                        webView.loadUrl(getString(R.string.main));
                        break;
                    case R.id.nav_admin:
                        webView.loadUrl(getString(R.string.main) + getString(R.string.admin));
                        break;
                    case R.id.nav_student:
                        webView.loadUrl(getString(R.string.main) + getString(R.string.student));
                        break;
                    case R.id.nav_logout:
                        webView.loadUrl(getString(R.string.main) + getString(R.string.logout));
                        break;
                    case R.id.nav_signup:
                        webView.loadUrl(getString(R.string.main) + getString(R.string.signup));
                        break;
                    case R.id.nav_support:
                        webView.loadUrl(getString(R.string.main) + getString(R.string.support));
                        break;
                    case R.id.nav_about:
                        webView.loadUrl(getString(R.string.main) + getString(R.string.about));
                        break;
                    default:
                        return true;
                }
                drawer.closeDrawers();

                webView.setOnKeyListener((view, i, keyEvent) -> {
                    if (keyEvent.getAction() == KeyEvent.ACTION_DOWN) {
                        if (i == KeyEvent.KEYCODE_BACK) {
                            if (webView.canGoBack()) {
                                webView.goBack();
                            }
                        }
                    }
                    return true;
                });
                return false;
            }
        });

        drawer.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {
            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                fab.hide();
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                fab.show();
            }

            @Override
            public void onDrawerStateChanged(int newState) {
            }
        });
    }
}