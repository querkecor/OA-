package com.aug.security.filter;

import com.alibaba.fastjson2.JSON;
import com.aug.common.jwt.JwtHelper;
import com.aug.common.result.Result;
import com.aug.common.result.ResultCodeEnum;
import com.aug.common.utils.ResponseUtil;
import com.aug.constant.OAConstant;
import com.aug.model.system.SysUser;
import com.aug.security.custom.CustomUser;
import com.aug.security.custom.LoginUserInfoHelper;
import com.aug.security.custom.UserDetailsService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author querkecor
 * @date 2023/8/19
 */

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    private RedisTemplate redisTemplate;

    public TokenAuthenticationFilter(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        logger.info("uri :" + request.getRequestURI());

        if (OAConstant.LOGIN_URI.equals(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }
        UsernamePasswordAuthenticationToken authentication = getAuthentication(request);
        if (null != authentication) {
            SecurityContextHolder.getContext().setAuthentication(authentication);
            filterChain.doFilter(request, response);

        } else {
            ResponseUtil.out(response, Result.build(null, ResultCodeEnum.PERMISSION));
        }
    }

    private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        // 从header中取出token
        String token = request.getHeader(OAConstant.AUTH_TOKEN);
        logger.info("token:" + token);
        if (!StringUtils.isEmpty(token)) {
            String username = JwtHelper.getUsername(token);
            if (!StringUtils.isEmpty(username)) {
                // 保存用户信息到本地线程
                LoginUserInfoHelper.setUserId(JwtHelper.getUserId(token));
                LoginUserInfoHelper.setUsername(JwtHelper.getUsername(token));

                // 从redis中取出token
                String authoritiesString = (String) redisTemplate.opsForValue().get(username);
                if (!StringUtils.isEmpty(authoritiesString)) {
                    List<Map> mapList = JSON.parseArray(authoritiesString, Map.class);
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    for (Map map : mapList) {
                        authorities.add(new SimpleGrantedAuthority((String) map.get("authority")));
                    }
                    return new UsernamePasswordAuthenticationToken(username, null, authorities);
                } else {
                    return new UsernamePasswordAuthenticationToken(username, null, new ArrayList<>());
                }
            }
        }
        return null;
    }


}
