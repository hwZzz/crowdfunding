package com.crowd.mvc.config;

import com.crowd.entity.Admin;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

/**
 * hw
 */
public class SecurityAdmin extends User {

    private static final long serialVersionUID = 1L;
    // 原始的 Admin 对象， 包含 Admin 对象的全部属性
    private Admin originalAdmin;

    public SecurityAdmin(Admin originalAdmin, List<GrantedAuthority> authorities){
        super(originalAdmin.getLogin(),originalAdmin.getUserPswd(),authorities);
        // 给本类的 this.originalAdmin 赋值
        this.originalAdmin = originalAdmin;

        // 将原始 Admin 对象中的密码擦除，密码已经传给父类User构造器，因此这里擦除不影响
        this.originalAdmin.setUserPswd(null);
    }

    // 对外提供的获取原始 Admin 对象的 getXxx()方法
    public Admin getOriginalAdmin() {
        return originalAdmin;
    }
}
