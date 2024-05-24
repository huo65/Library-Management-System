package com.example.demo.controller;

import com.example.demo.commom.Result;
import com.example.demo.entity.Book;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.tencentcloudapi.common.CommonClient;
import com.tencentcloudapi.common.Credential;
import com.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.tencentcloudapi.common.profile.ClientProfile;
import com.tencentcloudapi.common.profile.HttpProfile;
import org.springframework.web.bind.annotation.*;

/**
 * 1. @ClassName BarCodeController
 * 2. @Description 条形码信息识别
 * 3. @Author huo
 * 4. @Date 2024/5/24 8:43
 */
@RestController
@RequestMapping("/barCode")
public class BarCodeController {
//TODO 之后替换对应类
    @PostMapping("/getId")
    public Result<String> scanBarCode(@RequestBody String base64){
        try {
            Credential cred = new Credential("", "");

            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ocr.tencentcloudapi.com");

            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);

            CommonClient client = new CommonClient("ocr", "2018-11-19", cred, "ap-beijing", clientProfile);
            String params = "{\"ImageBase64\":\"" + base64 + "\"}";

            String resp = client.call("QrcodeOCR", params);
            System.out.println(resp);
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(resp);
            JsonNode codeResultsNode = rootNode.path("Response").path("CodeResults").get(0);
            String urlValue = codeResultsNode.path("Url").asText();
            System.out.println("Url对应的值为: " + urlValue);
            return Result.success(urlValue);
        } catch (TencentCloudSDKException e) {
            System.out.println(e.toString());
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return null;
    }
}
