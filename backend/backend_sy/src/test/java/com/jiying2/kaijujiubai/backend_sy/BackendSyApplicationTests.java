package com.jiying2.kaijujiubai.backend_sy;

import com.jiying2.kaijujiubai.backend_sy.domain.Store;
import com.jiying2.kaijujiubai.backend_sy.service.StoreService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class BackendSyApplicationTests {
    @Autowired
    StoreService storeService;

    @Test
    void showAllStore(){
        List<Store> list = storeService.list();
        System.out.println(list);
//        list.forEach(System.out::println);
    }

}
