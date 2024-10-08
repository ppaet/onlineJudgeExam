package com.zeke.filter;

import com.alibaba.fastjson.JSONObject;
import com.zeke.bean.User;
import com.zeke.utils.Code;
import com.zeke.utils.JwtTokenUtil;
import com.zeke.utils.result.ApiResult;
import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.util.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private RedisTemplate<Object, Object> redisTemplate;

    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request,
                                    @NotNull HttpServletResponse response,
                                    @NotNull FilterChain filterChain) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        String token = request.getHeader("token");
        String[] doFilterUrls = {"/api/user/login", "/api/user/register",
                "/api/auth/send_email", "/api/user/updatePassword",
                "/api/role","/api/user/forgetpw"};
        for (String doFilterUrl : doFilterUrls) {
            if (doFilterUrl.contains(request.getRequestURI())) {
                filterChain.doFilter(request, response);
                return;
            }
        }
        if (!StringUtils.hasText(token)) {
            response.getWriter().write(new ApiResult<>(Code.TOKEN_EMPTY_ERROR, null, "令牌为空!").toString());
            return;
        }
        String userInfo;
        try {
            userInfo = jwtTokenUtil.parserToken(token);
        } catch (ExpiredJwtException e) {
            response.getWriter().write(new ApiResult<>(Code.TOKEN_TIMEOUT_ERROR, null, "登录会话过期，请重新登录!")
                    .toString());
            return;
        } catch (Exception e) {
            response.getWriter().write(new ApiResult<>(Code.TOKEN_VALIDATE_ERROR, null, "非法令牌!").toString());
            return;
        }

        JSONObject userJson = JSONObject.parseObject(userInfo);
        String redisKey = "userId:" + userJson.getString("uId");
        User loginUser = new User();
        try {
            loginUser = (User) redisTemplate.opsForValue().get(redisKey);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        if (Objects.isNull(loginUser)) {
            response.getWriter().write(new ApiResult<>(Code.TOKEN_VALIDATE_ERROR, null, "请先登录!").toString());
        } else {
            filterChain.doFilter(request, response);
        }
    }
}
