package com.iris.ccpm.activity.projectDetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.model.Dynamic;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Member;
import com.iris.ccpm.model.TaskModel;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

import static com.iris.ccpm.model.GlobalData.getGlobalData;

public class ProjectDetailViewModel extends ViewModel {
    MutableLiveData<List<TaskModel>>  taskList;
    MutableLiveData<JSONObject> statistics;
    MutableLiveData<List<Dynamic>> dynamicList;
    MutableLiveData<List<Member>> memberList;
    String project_id;

    public ProjectDetailViewModel() {
        GlobalData app = (GlobalData) getGlobalData();
        project_id = app.getNow_project_id();

        taskList = new MutableLiveData<>();
        dynamicList = new MutableLiveData<>();
        statistics = new MutableLiveData<>();
        memberList = new MutableLiveData<>();

        update();
    }

    public void update() {
        Request.clientGet("task?claimState=3&project_uid=" + project_id , new NetCallBack() {
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

        Request.clientGet("dynamic?project_uid=" + project_id, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Dynamic> dynamics = JSONObject.parseArray(liststring, Dynamic.class);//把字符串转换成集合
                dynamicList.setValue(dynamics);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        Request.clientGet("statistics?ingTaskPro=yes&doneTaskPro=yes&hasOverdue=yes&noClaimTask&expireToday=yes&proMemNum=yes&project_uid=" + project_id, new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                statistics.setValue(result);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });

        Request.clientGet("project/" + project_id + "/member", new NetCallBack() {
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Member> members = JSONObject.parseArray(liststring, Member.class);//把字符串转换成集合
                memberList.setValue(members);
            }

            @Override
            public void onMyFailure(String error) {
            }
        });
    }

    public MutableLiveData<List<TaskModel>> getTaskList() {
        return taskList;
    }

    public MutableLiveData<JSONObject> getStatistics() {
        return statistics;
    }

    public MutableLiveData<List<Dynamic>> getDynamicList() {
        return dynamicList;
    }

    public MutableLiveData<List<Member>> getMemberList() {
        return memberList;
    }
}
