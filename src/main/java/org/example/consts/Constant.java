package org.example.consts;

import java.text.SimpleDateFormat;

public interface Constant {

    SimpleDateFormat SDFDATE = new SimpleDateFormat("yyyy-MM-dd");

    interface STATUS {
        String STATUS_0 = "0";
        String STATUS_1 = "1";
    }

    interface SESSION {
        String USER_ID = "session-userId";

    }

    interface TOKEN {
        String TOKEN_USERID = "userId";
        String TOKEN_PASSWORD = "passWord";
    }

    //    审批状态
    interface APPROVAL_STATUS {
        Integer STATUS_0 = 0;//待审批
        Integer STATUS_1 = 1;//通过
        Integer STATUS_2 = 2;//驳回
    }

    //    流程状态
    interface TASK_STASUS {
        String TO_BE_SUBMITTED = "toBeSubmitted";//待提交
        String APPROVING = "approving";//审批中
        String REJECT = "reject";//已退回
        String PASS = "pass";//通过
    }

    //流程类型
    interface FLOW_TYPE {
        //'流程类型（1请假2加班3差旅）',4资料审批
        String QJ = "1";
        String JB = "2";
        String CL = "3";
        String ZL = "4";

    }

    //请假类型
    interface LEAVE_TYPE {
        //'请假类型（1事假2调休假3年假）'
        String SHIJIA = "1";
        String TIAOXIU = "2";
        String NINAJIA = "3";

    }
}
