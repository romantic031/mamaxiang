package com.jiying2.kaijujiubai.backend_sy.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.bean.copier.CopyOptions;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jiying2.kaijujiubai.backend_sy.constants.ErrorCode;
import com.jiying2.kaijujiubai.backend_sy.domain.User;
import com.jiying2.kaijujiubai.backend_sy.domain.dto.*;
import com.jiying2.kaijujiubai.backend_sy.mapper.UserMapper;
import com.jiying2.kaijujiubai.backend_sy.service.UserService;
import com.jiying2.kaijujiubai.backend_sy.utils.RegexUtils;
import com.jiying2.kaijujiubai.backend_sy.utils.UserHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static com.jiying2.kaijujiubai.backend_sy.constants.RedisConstants.*;
import static com.jiying2.kaijujiubai.backend_sy.constants.SystemConstants.USER_NICK_NAME_PREFIX;

@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper,User> implements UserService{
    @Autowired
    private StringRedisTemplate stringRedisTemplate;
    //stringRedisTemplate和RedisTemplate的序列化策略不同  RedisTemplate需要转换 否则看到的不是原数据

    @Override
    public ResultVo register(User user) {
        //1.判断手机号是否已被注册
        Integer phone = query().eq("phone", user.getPhone()).count();
        if(phone>0){
            return ResultVo.fail(ErrorCode.REGISTER_PROBLEM.getCode(),"该手机号已被注册");
        }
        //2.判断账号是否已经存在
        Integer account = query().eq("account", user.getAccount()).count();
        if(account>0){
            return ResultVo.fail(ErrorCode.REGISTER_PROBLEM.getCode(),"该账号已被注册");
        }
        //3.注册
        boolean result = save(user);

        return result? ResultVo.success("注册成功") :ResultVo.fail(ErrorCode.REGISTER_PROBLEM.getCode(),"注册失败");
    }

    @Override
    public ResultVo sendCode(String phone) {
        // 校验手机号
        if (RegexUtils.isPhoneInvalid(phone)) {
            // 不符合，返回错误信息
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"手机号有误");
        }
        String key = LOGIN_CODE_KEY + phone;
        // 符合，生成验证码
        String code = RandomUtil.randomNumbers(6);

        // 保存验证码，并设置有效期
        stringRedisTemplate.opsForValue().set(key, code, LOGIN_CODE_TTL, TimeUnit.MINUTES);

        // 发送验证码
        System.out.println("发送短信验证码成功！验证码：" + code);

        return ResultVo.success(null,"发送验证码成功");
    }

    @Override
    public ResultVo login(LoginFormDto loginFormDto) {
        //判断是手机号登录还是账号登录
        //1.手机号登录
        if(StrUtil.isNotBlank(loginFormDto.getPhone())){
            return phoneLogin(loginFormDto.getPhone(),loginFormDto.getCode());
        }
        if (StrUtil.isNotBlank(loginFormDto.getAccount())) {
            return accountLogin(loginFormDto.getAccount(),loginFormDto.getPassword());
        }
        //其他错误
        return ResultVo.fail(ErrorCode.LOGIN_PROBLEM.getCode(),"出错啦");
    }

    @Override
    public ResultVo logout(String token) {
        //key 根据token组成redis的key 然后删除redis存的缓存和线程里的变量
        String key=LOGIN_USER_KEY+token;
        //删除redis中对应key值的用户信息
        stringRedisTemplate.delete(key);
        //删除线程中的用户信息变量
        UserHolder.removeUser();
        System.out.println("删除了"+token);
        return ResultVo.success();
    }



        @Override
        public ResultVo setPassword(PwdDto pwdDto) {
            // 1. 获取登录用户
            UserDto userDto = UserHolder.getUser();
            // 2. 获取用户id
            Long userId = userDto.getId();
            // 3. 查询用户
            User user = getById(userId);
            // 4. 判断用户是否存在
            if (user == null) {
                // 用户不存在
                UserHolder.removeUser();
                return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"用户不存在！");
            }
            // 5. 判断用户是否设置密码
            if (StrUtil.isBlank(user.getPassword())) {
                // 未设置密码，直接将新密码设置为密码
                boolean result = update().set("password", pwdDto.getNewPassword()).eq("id", userId).update();
                return result ? ResultVo.success() : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"设置密码失败！");
            }
            //判断手机号是否一致
            if(pwdDto.getPhone()!=user.getPhone()){
                return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"手机号和初始手机号不一致！");
            }
            // 6. 判断旧密码是否一致
            if (!user.getPassword().equals(pwdDto.getOldPassword())) {
                // 不一致
                return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"旧密码错误！");
            }
            // 7. 修改密码
            boolean result = update().set("password", pwdDto.getNewPassword()).eq("id", userId).update();
            return result ? ResultVo.success() : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"修改密码失败！");
        }




    @Override
    public ResultVo isPassword() {
        // 1. 获取登录用户
        UserDto userDto = UserHolder.getUser();
        // 2. 获取用户id
        Long userId = userDto.getId();
        // 3. 查询用户
        User user = getById(userId);
        // 4. 判断用户是否设置密码

        if (StrUtil.isNotBlank(user.getPassword())) {
            return ResultVo.success();
        }
        return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"当前用户未设置密码");
    }

    @Override
    public ResultVo updateIcon(String avatar) {
        if(StrUtil.isBlank(avatar)){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"头像不存在");
        }
        Long userId = UserHolder.getUser().getId();
        boolean result = update().set("avatar", avatar).eq("id", userId).update();
        return result ? ResultVo.success() : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"头像修改失败！");
    }

    @Override
    public ResultVo updateAccount(String account) {
        if(StrUtil.isBlank(account)){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"账号不能为空！");
        }
        Long userId = UserHolder.getUser().getId();
        boolean result = update().set("account", account).eq("id", userId).update();
        return result ? ResultVo.success() : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"账号修改失败！");
    }

    @Override
    public ResultVo updateNickName(String nickname) {
        //1判断是否为空
        if(StrUtil.isBlank(nickname)){
            return ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"昵称不能为空！");
        }
        Long userId = UserHolder.getUser().getId();
        boolean result = update().set("nickname", nickname).eq("id", userId).update();
        return result ? ResultVo.success() : ResultVo.fail(ErrorCode.PARAMS_ERROR.getCode(),"昵称修改失败！");
    }

    /**
     * 通过账号密码登录
     * @param account
     * @param password
     * @return
     */
    private ResultVo accountLogin(String account, String password) {
        User user = query().eq("account", account).eq("password",password).one();
        if (user==null){
            return ResultVo.fail(ErrorCode.LOGIN_PROBLEM.getCode(),"账号或密码错误");
        }
        if (user.getStatus()==0){
            return ResultVo.fail(ErrorCode.LOGIN_PROBLEM.getCode(),"您的账号被禁用，请联系相关人员");
        }
        String token = saveUser(user);
        return ResultVo.success(token);
    }

    /**
     * 通过手机号和验证码登录(如果没有则直接注册)
     * @param phone
     * @param code
     * @return
     */
    private ResultVo phoneLogin(String phone, String code) {
        ResultVo resultVo=checkCode(LOGIN_CODE_KEY,phone,code);

        if(StrUtil.isNotBlank(resultVo.getMsg())){
            return resultVo;
        }

        User user = query().eq("phone", phone).one();
        //用户信息不存在，创建用户
        if(user==null){
            user=createUserByPhone(phone);
        }
        if (user.getStatus()==0){
            return ResultVo.fail(ErrorCode.LOGIN_PROBLEM.getCode(),"您的账号被禁用，请联系相关人员");
        }
        //用户信息存在，存入redis
        String token=saveUser(user);

        return ResultVo.success(token);
    }

    private String saveUser(User user) {
        String token= IdUtil.fastSimpleUUID()+"-"+user.getId();

        //Redis的key
        String key=LOGIN_USER_KEY+token;

        //User拷贝成UserDto
        UserDto userDto = BeanUtil.copyProperties(user, UserDto.class);
        //Hash存储，字段分开(可以分开设置)
        Map<String, Object> map = BeanUtil.beanToMap(userDto, new HashMap<>(),
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
        //存入  hash
        stringRedisTemplate.opsForHash().putAll(key,map);
        //设置到期时间 （注意token的刷新）
        stringRedisTemplate.expire(key,LOGIN_USER_TTL,TimeUnit.MINUTES);
        return token;
    }

    private User createUserByPhone(String phone) {
        User user=new User();
        user.setPhone(phone);
        user.setAccount(USER_NICK_NAME_PREFIX+phone);
        user.setNickname(USER_NICK_NAME_PREFIX+RandomUtil.randomString(5));
        save(user);
        return user;
    }

    private ResultVo checkCode(String loginCodeKey, String phone, String code) {
        //手机号校验
        if (RegexUtils.isPhoneInvalid(phone)){
            return ResultVo.fail(ErrorCode.LOGIN_PROBLEM.getCode(),"手机号格式错误");
        }
        //redis键
        String key=loginCodeKey+phone;
        //获取验证码(redis中)
        String redisCode = stringRedisTemplate.opsForValue().get(key);
        if(redisCode==null || !code.equals(redisCode)){
            return ResultVo.fail(ErrorCode.LOGIN_PROBLEM.getCode(),"验证码错误");
        }
        return ResultVo.success();
    }
}
