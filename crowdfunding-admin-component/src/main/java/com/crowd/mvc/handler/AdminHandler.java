package com.crowd.mvc.handler;

import com.crowd.constant.CrowdConstant;
import com.crowd.entity.Admin;
import com.crowd.service.api.AdminService;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

@Controller
public class AdminHandler {
    @Autowired
    private AdminService adminService;

    @RequestMapping("/admin/remove/{adminId}/{pageNum}/{keyword}.html")
    public String remove(@PathVariable("adminId") Integer adminId,
                         @PathVariable("pageNum") Integer pageNum,
                         @PathVariable("keyword") String keyword){
        //执行删除
        adminService.remove(adminId);

        //跳转页面
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }

    @RequestMapping("/admin/save.html")
    public String save(Admin admin){
        adminService.saveAdmin(admin);
        return "redirect:/admin/get/page.html?pageNum="+ Integer.MAX_VALUE;
    }

    @RequestMapping("/admin/to/edit/page.html")
    public String toEditPage(@RequestParam("adminId") Integer adminId,
                             ModelMap modelMap){
        //1.根据adminId查询admin对象
        Admin admin = adminService.getAdminById(adminId);
        //2.将admin对象存入模型
        modelMap.addAttribute("admin",admin);
        return "admin-edit";
    }

    @RequestMapping("/admin/update.html")
    public String update(Admin admin,
                         @RequestParam(value = "pageNum", defaultValue = "1") Integer pageNum,
                         @RequestParam(value = "keyword",defaultValue = "") String keyword){
        adminService.update(admin);
        return "redirect:/admin/get/page.html?pageNum="+pageNum+"&keyword="+keyword;
    }


    @RequestMapping("/admin/get/page.html")
    public String getPageInfo(
            //使用@RequestParam注解的defaultValue属性，指定默认值，在请求中没有携带对应参数是使用默认值
            //keyword默认值使用空字符串，和SQL语句配合实现两种情况适配
            @RequestParam(value = "keyword",defaultValue = "") String keyword,
            //pageNum默认值使用1
            @RequestParam(value = "pageNum",defaultValue = "1") Integer pageNum,
            //pageSize默认值使用5
            @RequestParam(value = "PageSize",defaultValue = "5") Integer pageSize,
            ModelMap modelMap){
        //调用Service方法获取PageInfo对象
        PageInfo<Admin> pageInfo = adminService.getPageInfo(keyword,pageNum,pageSize);
        modelMap.addAttribute(CrowdConstant.ATTR_NAME_PAGE_INFO,pageInfo);

        return "admin-page";
    }

    @RequestMapping("/admin/do/logout.html")
    public String doLogout(HttpSession session){
        //强制seesion失效
        session.invalidate();

        return "redirect:/admin/to/login/page.html";
    }

    @RequestMapping("/admin/do/login.html")
    public String doLogin(@RequestParam("loginAcct") String loginAcct,
                          @RequestParam("userPswd") String userPswd,
                          HttpSession session){
        //调用Service方法执行登录检查
        //这个方法如果能够返回admin对象说明登录成功，如果账号不正确，则会抛出异常
        Admin admin = adminService.getAdminByLoginAcct(loginAcct,userPswd);

        //将登录成功返回对象存入Seesion域
        session.setAttribute(CrowdConstant.ATTR_NAME_LOGIN_ADMIN,admin);
        //为了避免跳转到后台页面后，浏览器刷新页面重新提交表单，采取重定向 web-mvc.xml
        return "redirect:/admin/to/main/page.html";
    }
}
