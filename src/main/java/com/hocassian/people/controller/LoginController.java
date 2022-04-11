package com.hocassian.people.controller;

import com.hocassian.people.entity.User;
import com.hocassian.people.transport.AjaxResult;
import com.hocassian.people.transport.CommonResult;
import com.hocassian.people.utils.VerifyCode;
import org.apache.commons.io.output.ByteArrayOutputStream;
import org.springframework.util.Base64Utils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
public class LoginController {
    private String validate_code = null;

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public CommonResult login(@RequestBody User user) {
        if (user.getUsername().equals("admin") && user.getPassword().equals("123456"))
            return CommonResult.success("admin");
        else if ( user.getPassword().equals("123456"))
            return CommonResult.success("user");
        else
            return CommonResult.validateFailed();
    }

    // 生成验证码
    @GetMapping("/getVertifyCodeImage")
    public String getImageCode(HttpServletRequest request) throws IOException {
        // 1. 使用工具类生成验证码
        String code = VerifyCode.generateVerifyCode(4);
        validate_code = code; // 存放生成的验证码
        // 2. 将验证码放入ServletContext作用域
        request.getServletContext().setAttribute("code", code);
        // 3. 将图片转换成base64格式
        // 字节数组输出流在内存中创建一个字节数组缓冲区，所有发送到输出流的数据保存在该字节数组缓冲区中
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        //将得到的验证码，使用工具类生成验证码图片，并放入到字节数组缓存区
        VerifyCode.outputImage(220,60,byteArrayOutputStream,code);
        //使用spring提供的工具类，将字节缓存数组中的验证码图片流转换成Base64的形式
        //并返回给浏览器
        return "data:image/png;base64," + Base64Utils.encodeToString(byteArrayOutputStream.toByteArray());
    }




}
