package org.techtown.receiver;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.pedro.library.AutoPermissions;
import com.pedro.library.AutoPermissionsListener;

import org.jetbrains.annotations.NotNull;

public class MainActivity extends AppCompatActivity implements AutoPermissionsListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AutoPermissions.Companion.loadAllPermissions(this,101);
        //모든 위험 권한을 자동 부여하도록 하는 메서드 호출하기
    }

    @Override public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResult){
        super.onRequestPermissionsResult(requestCode, permissions, grantResult);
        AutoPermissions.Companion.parsePermissions(this, requestCode, permissions, this);
    }
    @Override
    public void onDenied(int requestCode, @NotNull String[] permissions){
        Toast.makeText(this, "permissions denied:"+permissions.length,Toast.LENGTH_LONG).show();
    }
    @Override
    public void onGranted(int requestCode, @NotNull String[]permissions){
        Toast.makeText(this, "permissions granted:"+permissions.length, Toast.LENGTH_LONG).show();
    }
}
