package com.controller;

import com.qq.connect.QQConnectException;
import com.qq.connect.api.OpenID;
import com.qq.connect.api.qzone.UserInfo;
import com.qq.connect.javabeans.AccessToken;
import com.qq.connect.javabeans.qzone.UserInfoBean;
import com.qq.connect.oauth.Oauth;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by CHLaih on 2018/3/15.
 */
@Controller
public class LoginController {

    @RequestMapping(value = "/login",method = RequestMethod.GET)
    public void login(HttpServletRequest request, HttpServletResponse response){
        try {
            response.sendRedirect(new Oauth().getAuthorizeURL(request));
        } catch (QQConnectException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @RequestMapping(value = "afterlogin")
    public String afterlogin(HttpServletRequest request,HttpServletResponse response){
        String accessToken = null,openID = null;
        try{
            AccessToken accessTokenObj = (new Oauth()).getAccessTokenByRequest(request);
            if(accessTokenObj.getAccessToken().equals("")){
                System.out.print("没有获取到响应参数");
            }else {
                accessToken=accessTokenObj.getAccessToken();
                OpenID openIDObj = new OpenID(accessToken);
                openID =openIDObj.getUserOpenID();
                UserInfo qzoneUserInfo = new UserInfo(accessToken, openID);
                UserInfoBean userInfoBean = qzoneUserInfo.getUserInfo();
                String nickname = userInfoBean.getNickname();
                request.getSession().setAttribute("nickname",nickname);
            }
        } catch (QQConnectException e) {
            e.printStackTrace();
        }
            return "login";
    }
}
