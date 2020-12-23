package com.iris.ccpm.ui.my;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.model.Project;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

public class MyViewModel extends ViewModel {
    MutableLiveData<JSONObject> myStatistics;
    MutableLiveData<List<Project>> projectList;
    MutableLiveData<List<TaskModel>> taskList;

    public MyViewModel() {
        myStatistics = new MutableLiveData<>();
        projectList = new MutableLiveData<>();
        taskList = new MutableLiveData<>();

        Request.clientGet("statistics?projectNum=yes&taskNum=yes&ingProject=yes&ingTask=yes", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                myStatistics.setValue(result);
            }

            @Override
            public void onMyFailure(String error) {
            }
        });

        Request.clientGet("project?asManager=yes", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                System.out.println("project:" + result);
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Project> projecs = JSONObject.parseArray(liststring, Project.class);//把字符串转换成集合
                projectList.setValue(projecs);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        Request.clientGet("task?asMember=yes&asManager=no", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<TaskModel> tasks = JSONObject.parseArray(liststring, TaskModel.class);//把字符串转换成集合
                taskList.setValue(tasks);
            }
            @Override
            public void onMyFailure(String error) {
            }
        });
    }

    public MutableLiveData<JSONObject> getMyStatistics() {
        return myStatistics;
    }

    public MutableLiveData<List<Project>> getProjectList() {
        return projectList;
    }

    public MutableLiveData<List<TaskModel>> getTaskList() {
        return taskList;
    }
}