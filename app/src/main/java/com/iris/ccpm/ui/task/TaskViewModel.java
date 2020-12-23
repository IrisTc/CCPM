package com.iris.ccpm.ui.task;

import android.content.Intent;
import android.view.View;
import android.widget.AdapterView;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.R;
import com.iris.ccpm.TaskDetailActivity;
import com.iris.ccpm.adapter.TaskAdapter;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

public class TaskViewModel extends ViewModel {

    private MutableLiveData<List<TaskModel>> myTaskList;
    private MutableLiveData<List<TaskModel>> managerTaskList;

    public TaskViewModel() {
        myTaskList = new MutableLiveData<>();
        managerTaskList = new MutableLiveData<>();

        Request.clientGet( "task?asMember=yes&asManager=no", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<TaskModel> taskList = JSONObject.parseArray(liststring, TaskModel.class);//把字符串转换成集合
                myTaskList.setValue(taskList);
            }
            @Override
            public void onMyFailure(String error) {
            }
        });

        Request.clientGet("task?asManager=yes&asMember=no", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<TaskModel> taskList = JSONObject.parseArray(liststring, TaskModel.class);//把字符串转换成集合
                managerTaskList.setValue(taskList);
            }
            @Override
            public void onMyFailure(String error) {
            }
        });
    }

    public MutableLiveData<List<TaskModel>> getMyTaskList() {
        return myTaskList;
    }

    public MutableLiveData<List<TaskModel>> getManagerTaskList() {
        return managerTaskList;
    }
}