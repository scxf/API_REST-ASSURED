package com.test.testcase;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.base.BaseCase;
import com.test.data.GlobalEnvironment;
import com.test.pojo.CaseInfo;
import com.test.utils.Constants;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static io.restassured.RestAssured.given;

public class Login extends BaseCase {
      List<CaseInfo> caseInfoList;

    @BeforeClass
    public void setUp(){
        //从excel读取login需要的case
        caseInfoList = getCaseData(1);
        //参数化替换
        caseInfoList = paramsReplace(caseInfoList);
    }

    @Test(dataProvider="getLoginData")
    public void testLogin(CaseInfo caseInfo)  {
        Map headerMap = jsonToMap(caseInfo.getRequestHeader());
        //获取响应结果
        Response response =
        given().
                headers(headerMap).
        body(caseInfo.getInputParams()).
                when().
                //拼接请求
                post(Constants.BASE_URL+caseInfo.getUrl()).
                then().log().all().
                extract().response();

        assertExpected(caseInfo,response);
        /**登录模块结束后 参数化，将需要参数化的数据保存下来
         * 思路：1.拿到正向用例返回信息中的需要参数化的数据
         *      2.正向用例数据能取到值，反向用例值为null
         *      3.前3case为正向用例，判断id存贮对应的token
         * */
        if (caseInfo.getCaseId()==1){
            GlobalEnvironment.envData.put("token1",response.path("data.token_info.token"));
        }else if (caseInfo.getCaseId()==2){
            GlobalEnvironment.envData.put("token2",response.path("data.token_info.token"));
        }else if (caseInfo.getCaseId()==3){
            GlobalEnvironment.envData.put("token3",response.path("data.token_info.token"));
        }
    }
    @DataProvider
    public Object[] getLoginData(){
        return caseInfoList.toArray();
    }
}
