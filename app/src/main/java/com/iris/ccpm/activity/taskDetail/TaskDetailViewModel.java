package com.iris.ccpm.activity.taskDetail;

import android.widget.ListView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.iris.ccpm.model.GlobalData;
import com.iris.ccpm.model.Report;
import com.iris.ccpm.utils.NetCallBack;
import com.iris.ccpm.utils.Request;

import java.util.List;

import static com.iris.ccpm.model.GlobalData.getGlobalData;

public class TaskDetailViewModel extends ViewModel {
    MutableLiveData<List<Report>> reportList;
    Integer task_id;

    public TaskDetailViewModel() {
        GlobalData app = (GlobalData) getGlobalData();
        task_id = app.getNow_task_id();
        reportList = new MutableLiveData<>();
        update();
    }

    public void update() {
        Request.clientGet("report?task_uid=" + task_id, new NetCallBack(){
            @Override
            public void onMySuccess(JSONObject result) {
                JSONArray list = result.getJSONArray("list");
                String liststring = JSONObject.toJSONString(list);
                List<Report> reports = JSONObject.parseArray(liststring, Report.class);//把字符串转换成集合
                reportList.setValue(reports);
            }

            @Override
            public void onMyFailure(String error) {

            }
        });
    }

    public MutableLiveData<List<Report>> getReportList() {
        return reportList;
    }
}
