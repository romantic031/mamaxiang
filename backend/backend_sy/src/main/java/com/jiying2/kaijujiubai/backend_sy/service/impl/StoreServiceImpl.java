package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.domain.Store;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.mapper.StoreMapper;
import com.jiying2.kaijujiubai.backend_sy.service.StoreService;
import com.jiying2.kaijujiubai.backend_sy.utils.RegexUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service
public class StoreServiceImpl extends ServiceImpl<StoreMapper, Store> implements StoreService {
    @Override
    public ResultVo addStore(Store store) {
        if(StringUtils.isEmpty(store) || store==null){
            return ResultVo.fail(ErrorCode.FILE_PROBLEM.getCode(),"请输入信息");
        }
        if (RegexUtils.isPhoneInvalid(store.getPhone())){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"手机号错误");
        }
        boolean result = save(store);
        return result ? ResultVo.success(null,"添加成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"添加失败");
    }

    @Override
    public ResultVo showAllStore(String name) {
        LambdaQueryWrapper<Store> lqw=new LambdaQueryWrapper<>();
        lqw.like(StrUtil.isNotEmpty(name),Store::getName,name);
        List<Store> list = list(lqw);
        return ResultVo.success(list);
    }


    @Override
    public ResultVo updateStoreInfo(Store store) {
        boolean result = updateById(store);
        return result ? ResultVo.success(null,"修改成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"修改失败");
    }

    @Override
    public ResultVo showStoreById(Long id) {
        Store store = query().eq("id", id).one();
        if(store==null){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"发生错误啦");
        }
        return ResultVo.success(store);
    }
}
