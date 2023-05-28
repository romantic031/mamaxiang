package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.domain.UserVip;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.ResultVo;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.UserDto;
import com.jiying2.kaijujiubai.backend_sy.mapper.UserVipMapper;
import com.jiying2.kaijujiubai.backend_sy.service.UserService;
import com.jiying2.kaijujiubai.backend_sy.service.UserVipService;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jiying2.kaijujiubai.backend_sy.constants.RedisConstants.LOGIN_USER_TTL;

@Service
public class UserVipServiceImpl extends ServiceImpl<UserVipMapper, UserVip> implements UserVipService {
    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    UserService userService;
    //stringRedisTemplate和RedisTemplate的序列化策略不同  RedisTemplate需要转换 否则看到的不是原数据

    @Override
    public ResultVo getVipInfo() {
        UserDto user = UserHolder.getUser();
        if(user.getIsVip()==0){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "不好意思，您还不是会员");
        }
        Long userId = user.getId();
        String key = "userVip_" + userId;
//        String vipcache = stringRedisTemplate.opsForValue().get(key);
        Map<Object, Object> vipcache = stringRedisTemplate.opsForHash().entries(key);

        if (stringRedisTemplate.hasKey(key)) {
            return ResultVo.success(vipcache);
        }
        UserVip userVip = query().eq("user_id", userId).one();
        if (userVip == null) {
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(), "不好意思，您还不是会员");
        }
//            stringRedisTemplate.opsForValue().set("userVip_"+userId, String.valueOf(userVip));

//            //Hash存储，字段分开(可以分开设置)
            Map<String, Object> map = BeanUtil.beanToMap(userVip, new HashMap<>(),
                    CopyOptions.create()
                            .setIgnoreNullValue(true)
                            // 自定义将值转为字符串
                            // .setFieldValueEditor((fieldName, fieldValue) -> fieldValue.toString())
                            .setFieldValueEditor((fieldName, fieldValue) -> {
                                if (fieldValue == null) {
                                    fieldValue = "";
                                } else {
                                    fieldValue += "";
                                }
                                return fieldValue;
                            })
            );
//            //存入
            stringRedisTemplate.opsForHash().putAll(key, map);
//            //设置到期时间 （注意token的刷新）
            stringRedisTemplate.expire(key, LOGIN_USER_TTL, TimeUnit.MINUTES);
            return ResultVo.success(userVip);

    }


    @Override
    public ResultVo becomeVip(int day) {
        //1.查出要充值的用户基本信息
        Long userId = UserHolder.getUser().getId();
        UserVip userVip = query().eq("user_id", userId).one();
        //如果不存在更改用户表的is_vip列和在user_vip表插入相关信息
//        if(userVip==null){
        if(StringUtils.isEmpty(userVip)){
            UserVip userVip1=new UserVip();
            boolean result1 = userService.update().set("is_vip", 1).eq("id", userId).update();
            userVip1.setEndTime(new Date(LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli()));
            userVip1.setUserId(userId);
            boolean result2 = save(userVip1);
            return result1&&result2 ? ResultVo.success(null,"欢迎您成为会员") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"注册失败");
        }
        if(!StringUtils.isEmpty(userVip)){
            //如果存在则user_vip中在到期时间增加时长
            long time = userVip.getEndTime().getTime();
            time +=(1000L * 60 * 60 * 24 * day);
            boolean result = update().set("end_time", new Date(time)).update();
            return result ? ResultVo.success(null,"续费成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"续费失败");
        }
        return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"出错啦");
    }

    @Override
    public ResultVo rechargeAmount(BigDecimal vipAmount) {
        UserDto user = UserHolder.getUser();
        Long useId = user.getId();
        BigDecimal beforeAmount = query().eq("user_id", useId).one().getVipAmount();
        vipAmount=vipAmount.add(beforeAmount);
        boolean result = update().set("vip_amount", vipAmount).eq("user_id", useId).update();
        return result ? ResultVo.success(null,"充值成功") : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"充值失败");
    }
}
