package com.test.testcase;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.base.BaseCase;
import com.test.data.GlobalEnvironment;
import com.test.pojo.CaseInfo;
import com.test.utils.Constants;
import com.test.utils.PhoneRandom;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;
import java.util.Set;


import static io.restassured.RestAssured.given;

public class Register extends BaseCase {
    List<CaseInfo> caseInfoList;

    @BeforeClass
    public void setUp() {
        caseInfoList = getCaseData(0);
    }
    @Test(dataProvider="getRegisterData")
    public void testRegister(CaseInfo caseInfo)  {
        if(caseInfo.getCaseId()==1){
            GlobalEnvironment.envData.put("mobile_phone1", PhoneRandom.getRandomPhone());
        }else if(caseInfo.getCaseId()==2){
            GlobalEnvironment.envData.put("mobile_phone2", PhoneRandom.getRandomPhone());
        }else if (caseInfo.getCaseId()==3){
            GlobalEnvironment.envData.put("mobile_phone3", PhoneRandom.getRandomPhone());
        }
        //对单个case进行参数化替换
         caseInfo= paramsReplaceCaseInfo(caseInfo);

        Map headerMap = jsonToMap(caseInfo.getRequestHeader());
        Response response = given().
                headers(headerMap).
                body(caseInfo.getInputParams()).
                when().
                post(Constants.BASE_URL + caseInfo.getUrl()).
                then().log().all().extract().response();
        assertExpected(caseInfo,response);

        assertSql(caseInfo);
        //拿到密码
        Map inputParamsMap= jsonToMap(caseInfo.getInputParams());
        Object pwd = inputParamsMap.get("pwd");

        if(caseInfo.getCaseId()==1){
            //拿到手机号
            GlobalEnvironment.envData.put("mobile_phone1",response.path("data.mobile_phone"));
            GlobalEnvironment.envData.put("member_id1",response.path("data.id"));
            GlobalEnvironment.envData.put("pwd1",pwd+"");

        }
        else if(caseInfo.getCaseId()==2){
            //拿到手机号
            GlobalEnvironment.envData.put("mobile_phone2",response.path("data.mobile_phone"));
            GlobalEnvironment.envData.put("member_id2",response.path("data.id"));
            GlobalEnvironment.envData.put("pwd2",pwd+"");
        }
        else if(caseInfo.getCaseId()==3){
            //拿到手机号
            GlobalEnvironment.envData.put("mobile_phone3", response.path("data.mobile_phone"));
            GlobalEnvironment.envData.put("member_id3",response.path("data.id"));
            GlobalEnvironment.envData.put("pwd3",pwd+"");
        }
    }


    @DataProvider
    public Object[] getRegisterData() {
        return caseInfoList.toArray();
    }
}
