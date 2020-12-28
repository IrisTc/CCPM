package com.iris.ccpm.activity;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.adapter.TaskDetailSpinnerAdapter;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import cz.msebera.android.httpclient.entity.StringEntity;

public class ReportCreateActivity extends AppCompatActivity {
    TaskModel task;
    Spinner completeSpinner;
    TextView tvTaskName;
    TextView tvNickname;
    TextView tvStart;
    TextView tvEnd;
    TextView tvMemo;
    TextView tvPrio;
    TextView tvPredictTime;
    TextView tvRestTime;
    EditText etWorkhour;
    EditText etContent;
    EditText etPreHour;
    Button btReport;
    ImageButton ivCamera;
    ImageView ivImage;
    private File file;
    private Uri imageUri;
    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 999;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_add);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        task = (TaskModel) getIntent().getSerializableExtra("task");

        findView();
        initSpinner();
        loadData();

        initPhotoError();
        ivCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("camera");
                //第二个参数是需要申请的权限
                if (ContextCompat.checkSelfPermission(ReportCreateActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(ReportCreateActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    //权限还没有授予
                    ActivityCompat.requestPermissions(ReportCreateActivity.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_CALL_PHONE);
                } else {
                    //权限已经被授予
                    takePhoto();
                }
            }
        });

        btReport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String content = etContent.getText().toString();
                String workHour = etWorkhour.getText().toString();
                String preHour = etPreHour.getText().toString();
                Integer state = 0;
                JSONObject body = new JSONObject();
                body.put("workingContent", content);
                body.put("workingTime", workHour);
                body.put("restWorkingTime", preHour);
                body.put("taskState", state);
                StringEntity entity = new StringEntity(body.toJSONString(), "UTF-8");
                Request.clientPut(ReportCreateActivity.this, "task/" + task.getTask_uid(), entity, new NetCallBack() {

                    @Override
                    public void onMySuccess(JSONObject result) {
                        Toast.makeText(ReportCreateActivity.this, "汇报完成", Toast.LENGTH_LONG).show();
                        finish();
                    }

                    @Override
                    public void onMyFailure(String error) {
                        Toast.makeText(ReportCreateActivity.this, error, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        if (requestCode == MY_PERMISSIONS_REQUEST_CALL_PHONE) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                takePhoto();
            } else {
                // Permission Denied
                Toast.makeText(ReportCreateActivity.this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println(imageUri);

        //0 拍照
            try {
                System.out.println("uri" + getContentResolver().openInputStream(imageUri));
                Bitmap bit = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                ivImage.setImageBitmap(bit);
            } catch (Exception e) {
                System.out.println(e);
                Toast.makeText(this, "程序崩溃", Toast.LENGTH_SHORT).show();
            }
        }

    void takePhoto() {
        System.out.println(Environment.getExternalStorageDirectory().getAbsolutePath());
        File folder = new File(Environment.getExternalStorageDirectory().getAbsolutePath(),"");
        if(!folder.exists()){
            folder.mkdir();
        }

        /**
         * 这里将时间作为不同照片的名称
         */
        file = new File(folder, "cache.jpg");

        /**
         * 如果该文件已经存在，则删除它，否则创建一个
         */
        try {
            if (file.exists()) {
                file.delete();
            }
            file.createNewFile();
        } catch (Exception e) {
            e.printStackTrace();
        }
        /**
         * 隐式打开拍照的Activity，并且传入CROP_PHOTO常量作为拍照结束后回调的标志
         */
        imageUri = Uri.fromFile(file);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, 2);
    }

    private void initPhotoError() {
        // android 7.0系统解决拍照的问题
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        builder.detectFileUriExposure();
    }

    private void loadData() {
        String[] spinnerItems = {"普通", "紧急", "非常紧急"};
        int[] prioColors = {0xFF7FFFAA, 0xFFFF7F50, Color.RED};
        int[] prioTextColors = {Color.BLACK, Color.WHITE, Color.WHITE};

        Integer prio = task.getTaskEmergent();
        tvPrio.setText(spinnerItems[prio]);
        tvPrio.setBackgroundColor((prioColors[prio]));
        tvPrio.setTextColor(prioTextColors[prio]);

        tvTaskName.setText(task.getTaskName());
        tvNickname.setText(task.getClaimNickName());
        tvStart.setText(task.getTaskStartTime());
        tvEnd.setText(task.getTaskEndTime());
        tvMemo.setText(task.getTaskSynopsis());
        tvPredictTime.setText(task.getTaskPredictHours());
        tvRestTime.setText(task.getTaskRestHours());
    }

    private void findView() {
        tvTaskName = findViewById(R.id.tv_taskName);
        tvNickname = findViewById(R.id.tv_nickname);
        tvStart = findViewById(R.id.tv_startTime);
        tvEnd = findViewById(R.id.tv_endTime);
        tvMemo = findViewById(R.id.tv_memo);
        tvPrio = findViewById(R.id.tv_prio);
        tvPredictTime = findViewById(R.id.tv_predictTime);
        tvRestTime = findViewById(R.id.tv_restTime);
        etWorkhour = findViewById(R.id.et_workhour);
        etContent = findViewById(R.id.et_content);
        etPreHour = findViewById(R.id.et_prehour);
        btReport = findViewById(R.id.bt_report);
        completeSpinner = findViewById(R.id.completeSpinner);
        ivCamera = (ImageButton) findViewById(R.id.iv_camera);
        ivImage = findViewById(R.id.iv_image);
    }

    private void initSpinner() {
        completeSpinner.setDropDownWidth(400);
        completeSpinner.setDropDownHorizontalOffset(100);
        completeSpinner.setDropDownVerticalOffset(100);

        String[] completeItems = {"未完成", "已完成"};
        int[] completeColors = {Color.RED, 0xFF7FFFAA};
        int[] completeTextColors = {Color.WHITE, Color.BLACK};

        TaskDetailSpinnerAdapter completeAdapter = new TaskDetailSpinnerAdapter(this, completeItems, completeColors, completeTextColors);
        completeAdapter.setDropDownViewResource(R.layout.task_spinner_item_drop);
        completeSpinner.setAdapter(completeAdapter);

        completeSpinner.setSelection(task.getTaskState());
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }
}
