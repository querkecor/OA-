package com.aug.wechat.controller;

import com.alibaba.fastjson2.JSON;
import com.aug.auth.service.SysUserService;
import com.aug.common.jwt.JwtHelper;
import com.aug.common.result.Result;
import com.aug.model.system.SysUser;
import com.aug.vo.wechat.BindPhoneVo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.chanjar.weixin.common.api.WxConsts;
import me.chanjar.weixin.common.bean.WxOAuth2UserInfo;
import me.chanjar.weixin.common.bean.oauth2.WxOAuth2AccessToken;
import me.chanjar.weixin.common.error.WxErrorException;
import me.chanjar.weixin.mp.api.WxMpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

/**
 * @author querkecor
 * @date 2023/9/2
 */
@Slf4j
@Controller
@RequestMapping("/admin/wechat")
@CrossOrigin
public class WechatController {

    @Resource
    private WxMpService wxMpService;

    @Resource
    private SysUserService sysUserService;

    @Value("${wechat.userInfoUrl}")
    private String userInfoUrl;

    @GetMapping("/authorize")
    public String authorize(@RequestParam("returnUrl") String returnUrl, HttpServletRequest request) {
        log.info("【微信网页授权】获取code,returnUrl={}", returnUrl);

        String redirectUrl = null;
        try {
            redirectUrl = wxMpService.getOAuth2Service()
                    .buildAuthorizationUrl(userInfoUrl,
                            WxConsts.OAuth2Scope.SNSAPI_USERINFO,
                            URLEncoder.encode(returnUrl.replace("aug", "#"),"utf-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        log.info("【微信网页授权】获取code,redirectURL={}", redirectUrl);
        return "redirect:" + redirectUrl;
    }

    /**
     * 微信回调函数
     * @param code 微信携带的code，根据code获得openId
     * @param returnUrl 跳转地址
     * @return 结果Url
     */
    @GetMapping("/userInfo")
    public String userInfo(@RequestParam("code") String code,
                           @RequestParam("state") String returnUrl) {
        try {
            // 根据code获取微信openId
            WxOAuth2AccessToken accessToken = wxMpService.getOAuth2Service().getAccessToken(code);
            String openId = accessToken.getOpenId();
            log.info("【微信网页授权】openId={}", openId);

            WxOAuth2UserInfo wxMpUser = wxMpService.getOAuth2Service().getUserInfo(accessToken, null);
            log.info("【微信网页授权】wxMpUser={}", JSON.toJSONString(wxMpUser));

            // 根据openId查数据库，判断当前用户是否绑定微信
            LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(SysUser::getOpenId,openId);
            SysUser sysUser = sysUserService.getOne(wrapper);
            // 如果用户已绑定微信生成token返回
            String token = null;
            if (sysUser != null) {
                token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
            }

            if(returnUrl.indexOf("?") == -1) {
                return "redirect:" + returnUrl + "?token=" + token + "&openId=" + openId;
            } else {
                return "redirect:" + returnUrl + "&token=" + token + "&openId=" + openId;
            }
        } catch (WxErrorException e) {
            throw new RuntimeException(e);
        }
    }

    @PostMapping("bindPhone")
    @ResponseBody
    public Result bindPhone(@RequestBody BindPhoneVo bindPhoneVo) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getPhone, bindPhoneVo.getPhone());
        SysUser sysUser = sysUserService.getOne(wrapper);
        if(null != sysUser) {
            sysUser.setOpenId(bindPhoneVo.getOpenId());
            sysUserService.updateById(sysUser);

            String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
            return Result.success(token);
        } else {
            return Result.fail().message("手机号码不存在，绑定失败");
        }
    }
}
