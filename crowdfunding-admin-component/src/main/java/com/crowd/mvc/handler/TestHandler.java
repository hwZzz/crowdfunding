package com.crowd.mvc.handler;

import com.crowd.entity.Admin;
import com.crowd.entity.Student;
import com.crowd.service.api.AdminService;
import com.crowd.util.CrowdUtil;
import com.crowd.util.ResultEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class TestHandler {

    @Autowired
    private AdminService adminService;

    private Logger logger = LoggerFactory.getLogger(TestHandler.class);

    @ResponseBody
    @RequestMapping("/send/array.html")
    public String testReceiveArrayOne(@RequestParam("array[]") List<Integer> array){

        for (Integer number :
                array) {
            System.out.println("number"+number);
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/array/three.html")
    public String testReceiveArrayThree(@RequestBody List<Integer> array){
        for (Integer number :
                array) {
            logger.info("number="+number);
        }

        return "success";
    }

    @ResponseBody
    @RequestMapping("/send/compose/object.json")
    public ResultEntity<Student> testReceiveComposeObject(@RequestBody Student student,HttpServletRequest request){
        boolean judgeRequest = CrowdUtil.judgeRequestType(request);

        logger.info("judgeRequest="+judgeRequest);

        logger.info(student.toString());
       // System.out.println(student.toString());

        //将“查询”到的Student对象封装到ResultEntity中返回
        return ResultEntity.successWithData(student);
    }

    @RequestMapping("/test/ssm.html")
    public String testSsm(ModelMap modelMap, HttpServletRequest request){
        boolean judgeRequest = CrowdUtil.judgeRequestType(request);
        logger.info("judgeRequest="+judgeRequest);
        List<Admin> adminList = adminService.getAll();
        modelMap.addAttribute("adminList",adminList);
        //System.out.println(10/0);
        return "target";
    }
}
