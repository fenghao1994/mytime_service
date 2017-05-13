package com.example.configurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.controller.UserInfoController;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.reflect.Method;


public class PermissionInterceptor implements HandlerInterceptor {

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception exception) throws Exception {
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if (handler instanceof HandlerMethod) {
			HandlerMethod handlerMethod = (HandlerMethod) handler;
//			Object bean = handlerMethod.getBean();
			Method method = handlerMethod.getMethod();
			if (method.equals(UserInfoController.class.getMethod("rootLogin")))
				return true;
			Object isLogin = request.getSession().getAttribute("isLogin");
			if (isLogin != null){
				if ((boolean)isLogin){
					return true;
				}
			}
			response.sendRedirect("login.html");
			return false;

			/*Permission permissionAnnotation = handlerMethod.getMethodAnnotation(Permission.class);
			if (permissionAnnotation != null) {
				int[] value = permissionAnnotation.value();
				if (value.length == 0) {
					// 已登录用户权限
					boolean isLogin = (boolean) request.getSession().getAttribute("isLogin");
					if (!isLogin) {
						response.sendError(HttpServletResponse.SC_UNAUTHORIZED);
						return false;
					}
				} else if (value.length == 1) {
					// TODO 单角色用户权限
				} else {
					// TODO 多角色用户权限
				}
			}*/
		}
		return true;
	}
}
