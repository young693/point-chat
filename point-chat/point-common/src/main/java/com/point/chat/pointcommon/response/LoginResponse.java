package com.point.chat.pointcommon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;


/**
 * 密码登录响应对象
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(name = "LoginResponse", description = "用户登录返回数据")
public class LoginResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Schema(description = "用户登录密钥")
    private String token;

    @Schema(description = "凭证有效时间，单位：秒。目前是10800秒之内的值(3小时)。")
    private Long expiresIn;

    /**
     * 最后访问时间,毫秒时间戳
     */
    @Schema(description = "最后访问时间,毫秒时间戳")
    private Long lastAccessedTime;

    @Schema(description = "登录用户Uid")
    private Integer uid;

    @Schema(description = "登录用户昵称")
    private String nikeName;

    @Schema(description = "用户头像")
    private String avatar;

    @Schema(description = "企业id")
    private Integer enterpriseId;
}
