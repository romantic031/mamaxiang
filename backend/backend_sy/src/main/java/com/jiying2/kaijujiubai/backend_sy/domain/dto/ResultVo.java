package com.jiying2.kaijujiubai.backend_sy.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ResultVo {
    private boolean success;
    private int code;
    private String msg;
    private Object data;

    /**
     *成功无返回
     * @return
     */
    public static ResultVo success(){
        return new ResultVo(true,200,null,null);
    }

    /**
     * 成功返回数据
     * @param data
     * @return
     */
    public static ResultVo success(Object data){
        return new ResultVo(true,200,"success",data);
    }

    /**
     * 成功返回数据和信息
     * @param data
     * @return
     */
    public static ResultVo success(Object data,String msg){
        return new ResultVo(true,200,msg,data);
    }

    /**
     * 失败无返回(状态码)
     * @param code
     * @return
     */
    public static ResultVo fail(int code){
        return new ResultVo(false,code,null,null);
    }

    /**
     *失败返回信息(和状态码)
     * @param code
     * @param msg
     * @return
     */
    public static ResultVo fail(int code,String msg){
        return new ResultVo(false,code,msg,null);
    }


}
