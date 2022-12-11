package com.hotgroup.test;

import com.hotgroup.commons.core.domain.model.IUser;
import com.hotgroup.manage.api.IHgUserService;
import com.hotgroup.manage.api.IHotgroupUserLoginService;
import com.hotgroup.manage.domain.entity.HgUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

/**
 * @author Lzw
 * @date 2022/12/2.
 */

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class Test1 {

    @Autowired
    IHgUserService hgUserService;
    @Resource
    IHotgroupUserLoginService userLoginService;


    @Test
    public void test1(){
        HgUser hgUser = new HgUser();
//                .phone(phoneNoInfo.getPurePhoneNumber())


        IUser login = userLoginService.login(hgUser);

        System.out.println(login);
    }

}
