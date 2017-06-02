package com.example.configurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.example.annotations.PermissionAnno;
import com.example.controller.*;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.lang.annotation.Annotation;
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
			if (method.getAnnotation(PermissionAnno.class) != null){
				return true;
			}
			Boolean isLogin = (Boolean) request.getSession().getAttribute("isLogin");
			if (isLogin != null && isLogin){
				return true;
			}
			response.sendRedirect("login.html");
			return false;
		}
		return true;
	}
}
