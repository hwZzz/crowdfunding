package com.crowd.mvc.config;

import com.crowd.constant.CrowdConstant;
import com.crowd.exception.LoginAcctAreadyInUseException;
import com.crowd.exception.LoginAcctAreadyInUseForUpdateException;
import com.crowd.exception.LoginFailedException;
import com.crowd.util.CrowdUtil;
import com.crowd.util.ResultEntity;
import com.google.gson.Gson;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//@ControllerAdvice表示当前类是一个基于注解的异常处理器
@ControllerAdvice
public class CrowdExceptionResolver {

    //空指针异常   @ExceptionHandler将一个具体的异常类型和一个方法关联起来
    @ExceptionHandler(value = NullPointerException.class)
    public ModelAndView resolveNullPointerException(
            //实际捕获到的异常类型
            NullPointerException exception,
            //当前请求对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {

        String viewName =  "system-error";
        return commonResolve(viewName,exception,request,response);
    }

    //登录异常
    @ExceptionHandler(value = LoginFailedException.class)
    public ModelAndView resolveLoginFailedException(
            //实际捕获到的异常类型
            LoginFailedException exception,
            //当前请求对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {

        String viewName =  "admin-login";
        return commonResolve(viewName,exception,request,response);
    }

    // 未登录异常
    @ExceptionHandler(value = Exception.class)
    public ModelAndView resolveException(Exception exception,
                                         HttpServletRequest request,
                                         HttpServletResponse response) throws IOException {
        String viewName = "admin-login";
        return commonResolve(viewName,exception,request,response);
    }

    //账号重复异常
    @ExceptionHandler(value = LoginAcctAreadyInUseException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseException(
            //实际捕获到的异常类型
            LoginAcctAreadyInUseException exception,
            //当前请求对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {

        String viewName =  "admin-add";
        return commonResolve(viewName,exception,request,response);
    }

    //跟新时账号重复异常
    @ExceptionHandler(value = LoginAcctAreadyInUseForUpdateException.class)
    public ModelAndView resolveLoginAcctAlreadyInUseForUpdateException(
            //实际捕获到的异常类型
            LoginAcctAreadyInUseForUpdateException exception,
            //当前请求对象
            HttpServletRequest request,
            //当前响应对象
            HttpServletResponse response) throws IOException {

        String viewName =  "system-error";
        return commonResolve(viewName,exception,request,response);
    }

    // 创建通用方法
    private ModelAndView commonResolve(String viewName, Exception exception, HttpServletRequest request, HttpServletResponse response ) throws IOException {
        // 1. 判断当前请求类型
        boolean judgeResult = CrowdUtil.judgeRequestType(request);
        // 2. 如果为Ajax请求
        if (judgeResult) {
            // 3. 创建 ResultEntity 对象
            ResultEntity<Object> resultEntity = ResultEntity.failed(exception.getMessage());
            // 4. 创建Gson对象
            Gson gson = new Gson();
            // 5. 将ResultEntity对象转换为JSON字符串
            String json = gson.toJson(resultEntity);
            // 6. 将JSON字符作为响应体返回给浏览器
            response.getWriter().write(json);
            // 7. 上面已经通过原生response对象返回了响应，因此不再提供ModelAndView对象
            return null;
        }
        // 8. 如果不是Ajax请求，则创建ModelAndView对象
        ModelAndView modelAndView = new ModelAndView();
        // 9. 将Exception对象存入模型
        modelAndView.addObject(CrowdConstant.ATTR_NAME_EXCEPTION, exception);
        // 10. 设置对应的视图名称
        modelAndView.setViewName(viewName);
        return modelAndView;
    }
}
