package com.jiying2.kaijujiubai.backend_sy.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.jiying2.kaijujiubai.backend_sy.domain.Employee;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.transaction.annotation.Transactional;

@Mapper
public interface EmployeeMapper extends BaseMapper<Employee> {
}
