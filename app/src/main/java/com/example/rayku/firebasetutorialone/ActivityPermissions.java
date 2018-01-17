package com.example.rayku.firebasetutorialone;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import com.anthonycr.grant.PermissionsManager;
import com.anthonycr.grant.PermissionsResultAction;

public class ActivityPermissions extends AppCompatActivity {

    private boolean gotPermissions = false;
    private String[] requiredPermissions = new String[]{
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.INTERNET,
            android.Manifest.permission.GET_ACCOUNTS,
            //android.Manifest.permission.READ_PROFILE, //removed in API 23
            android.Manifest.permission.READ_CONTACTS};;

    @Override
    protected void onStart() {
        super.onStart();
        permissionsSetup();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permissions);
    }


    public void clickPermBtn(View view){
        if(!gotPermissions) permissionsSetup();
    }

    private void permissionsSetup(){
        PermissionsManager.getInstance().requestPermissionsIfNecessaryForResult(this,
                requiredPermissions, new PermissionsResultAction() {

                    @Override
                    public void onGranted() {
                        gotPermissions = true;
                        finish();
                        startActivity(new Intent(getApplicationContext(), ActivityLogin.class));
                    }

                    @Override
                    public void onDenied(String permission) { }
                });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        PermissionsManager.getInstance().notifyPermissionsChange(permissions, grantResults);
    }
}
