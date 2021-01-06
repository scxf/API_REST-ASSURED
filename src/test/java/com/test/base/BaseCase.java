package com.test.base;

import cn.afterturn.easypoi.excel.ExcelImportUtil;
import cn.afterturn.easypoi.excel.entity.ImportParams;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.test.data.GlobalEnvironment;
import com.test.pojo.CaseInfo;
import com.test.utils.Constants;
import com.test.utils.JDBCUtils;
import io.restassured.response.Response;
import org.testng.Assert;
import java.io.File;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Description: 公用方法类
 * @ClassName: BaseCase
 * @Author: Ming         // 创建者
 * @Date: 2020/12/24 23:10   // 时间
 * @Version: 1.0     // 版本
 */
public class BaseCase {
    /**
     *  正则替换
     * @author Ming
     * @param sourceStr 原始的字符串
     * @return 查找匹配替换之后的内容
     * @date 2020/12/24 23:12
     */
    public String regexReplace(String sourceStr) {
        //如果参数化的原字符串为空，就不需要正则替换
        if (sourceStr == null) {
            return sourceStr;
        }
        String regex = "\\{\\{(.*?)\\}\\}";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(sourceStr);
        //保存匹配到的整个表达式 比如 {token}
        String findStr = "";
        //匹配()里面的内容   比如 token
        String singleStr="";
        while (matcher.find()) {
            findStr = matcher.group(0);  // {token}
            singleStr = matcher.group(1); // token
            //先找到环境变量中存贮 对应的value    key 即 singleStr
            Object replaceStr =  GlobalEnvironment.envData.get(singleStr);
            //开始替换，替换原始字符串中的内容
            //当存在批量参数化时 如 /member/{{member_id}}/info{{mobile_phone}}
            //需要将第一次替换的值 /member/123456/info{{mobile_phone}}再次赋值给oldStr
            //直到匹配结束  退出while循环
            sourceStr = sourceStr.replace(findStr, replaceStr+"");
        }
            return sourceStr;
    }
    /**
     *  参数化处理
     *  对需要参数化处理的地方进行参数化处理
     *  (请求头，接口地址，参数输入，期望返回结果)
     * @author Ming
     * @param caseInfoList 传入的case对象集合
     * @return  修改后的集合
     * @date 2020/12/24 23:12
     */
    public List<CaseInfo> paramsReplace(List<CaseInfo> caseInfoList){
        for (CaseInfo caseInfo :caseInfoList){
                //对请求头参数化替换并保存到caseInfo对象中
                String requestHeader = regexReplace(caseInfo.getRequestHeader());
                caseInfo.setRequestHeader(requestHeader);
                //对url参数化替换并保存到caseInfo对象中
                String url = regexReplace(caseInfo.getUrl());
                caseInfo.setUrl(url);
                //对输入参数化替换并保存到caseInfo对象中
                String inputParams = regexReplace(caseInfo.getInputParams());
                caseInfo.setInputParams(inputParams);
                //对期望结果参数化替换并保存到caseInfo对象中
                String expected = regexReplace(caseInfo.getExpected());
                caseInfo.setExpected(expected);
                //对sql语句参数化替换并保存到caseInfo对象中
                String sql = regexReplace(caseInfo.getCheckSQL());
                caseInfo.setCheckSQL(sql);
        }
        return caseInfoList;
    }
    /**
     *  Excel中读取所有用例数据
     * @author Ming
     * @param index  sheet的索引 从0开始
     * @date  2020/12/24 23:17
     * @return 读取到的内容存在list集合
     * */
    public List<CaseInfo> getCaseData(int index) {
        ImportParams params = new ImportParams();
        params.setStartSheetIndex(index);
        File excelFile = new File(Constants.FILE_PATH);
        List<CaseInfo> list = ExcelImportUtil.importExcel(excelFile, CaseInfo.class, params);
        return list;
    }

    /**
     *  对单个case进行参数化处理
     * @author Ming
     * @param caseInfo 传入单个case对象
     * @return  修改后的case对象
     * @date 2020/12/24 23:12
     */
    public CaseInfo paramsReplaceCaseInfo(CaseInfo caseInfo){

            //调用regexReplace进行参数化替换
            String requestHeader = regexReplace(caseInfo.getRequestHeader());
            //再保存到caseInfo对象中
            caseInfo.setRequestHeader(requestHeader);

            String url = regexReplace(caseInfo.getUrl());
            caseInfo.setUrl(url);

            String inputParams = regexReplace(caseInfo.getInputParams());
            caseInfo.setInputParams(inputParams);

            String expected = regexReplace(caseInfo.getExpected());
            caseInfo.setExpected(expected);
            //对sql语句参数化替换并保存到caseInfo对象中
            String sql = regexReplace(caseInfo.getCheckSQL());
            caseInfo.setCheckSQL(sql);

        return caseInfo;
    }
    /**
     *  断言
     * @author Ming
    * @param caseInfo case对象
     * @param response 接口响应结果
     * @return
     * @date 2020/12/27 21:57
     * 思路：
     * 1、获取到expected返回结果
      {
         "code": 0,
        "msg": "OK"
         }
     *  2、转为map
     * 3、遍历map拿到key 和value
     * 4、断言 期望值 map.getValue  实际值 用Gpath截取
     *  5、Gpath路径表达式 为 map.getKey
     */
    public void assertExpected (CaseInfo caseInfo, Response response){
        ObjectMapper ojm2 = new ObjectMapper();
        //转为map
        Map expectMap = null;
        try {
            expectMap = ojm2.readValue(caseInfo.getExpected(), Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        //遍历map拿到key和value
        Set<Map.Entry<String,Object>> set = expectMap.entrySet();
        for (Map.Entry<String, Object> map : set){
            /**实际值：response.path(map.getKey())
             * 期望值:map.getValue()
             *rest-assured如果响应返回结果是带小数的json字符串，用Gpath表达式获取值时，存贮类型是float
             * 当位数较多时，会存在丢失进度的问题
             * 解决思路： 转换为BigDecimal
             */
            //判断期望值是 float或者double，是的话就转为BigDecimal
            if (map.getValue() instanceof Float || map.getValue() instanceof Double){
                BigDecimal bigDecimal = new BigDecimal(map.getValue().toString());
                Assert.assertEquals(response.path(map.getKey()),bigDecimal);
            }else {
                Assert.assertEquals(response.path(map.getKey()),map.getValue());
            }
        }
    }

    /**
     *  断言数据库
     * @author Ming
    * @param caseInfo
     * @return void
     * @date 2020/12/29 23:43
     */

    public void assertSql(CaseInfo caseInfo) {
        String checkSql = caseInfo.getCheckSQL();
        if(checkSql!=null){
            Map checkSqlMap = jsonToMap(checkSql);
            Set<Map.Entry<String,Object>> set = checkSqlMap.entrySet();
            for (Map.Entry<String, Object> map : set) {
                String sql = map.getKey();
                //查询数据库
                Object result = JDBCUtils.querySinger(sql);
                if (result instanceof Long){
                    Long expectedValue = new Long(map.getValue().toString());
                    Assert.assertEquals(result,expectedValue);
                }
        }
        }
    }

    /**
     *  JSON格式转为map
     * @author Ming
     * @param jsonStr  json字符串
     * @return Map
     * @date 2020/12/29 22:57
     */
    public Map jsonToMap(String jsonStr)  {
        ObjectMapper ojm = new ObjectMapper();
        Map headerMap = null;
        try {
            headerMap = ojm.readValue(jsonStr, Map.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return headerMap;
    }
}
