package com.jiying2.kaijujiubai.backend_sy.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jiying2.kaijujiubai.backend_sy.domain.Store;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import org.springframework.transaction.annotation.Transactional;

@Transactional
public interface StoreService extends IService<Store> {
    ResultVo addStore(Store store);

    ResultVo showAllStore(String name);

    ResultVo updateStoreInfo(Store store);


    ResultVo showStoreById(Long id);
}
