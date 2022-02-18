package crowd.test;

import com.crowd.entity.Admin;
import com.crowd.mapper.AdminMapper;
import com.crowd.service.api.AdminService;
import org.junit.jupiter.api.Test;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;


import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

//在类上标记必要的注解，Spring整合Junit
@SpringJUnitConfig(locations = {"classpath:spring-persist-mybatis.xml","classpath:spring-persist-tx.xml"})
public class CrowdTest {
    @Autowired
    private DataSource dataSource;

    @Autowired
    private AdminMapper adminMapper;

    @Autowired
    private AdminService adminService;

    @Test
    public void testInsertAdmin(){
        Admin admin;
        admin = new Admin(null,"Rachel","123123","rui","rui@qq.com",null);
        int count = adminMapper.insert(admin);
        System.out.println(count);
    }

    @Test
    public void testLogLog(){
        Logger logger = LoggerFactory.getLogger(CrowdTest.class);
        logger.debug("Hello I am Debug level");

    }

    @Test
    public void testTx(){
        Admin admin = new Admin(null,"jerry","123456","杰瑞","jerry@qq.com",null);
        adminService.saveAdmin(admin);
    }

    @Test
    public void  testConnection() throws SQLException {
        Connection connection = dataSource.getConnection();
        System.out.println(connection);
    }
}
