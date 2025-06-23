package com.point.chat.pointcommon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

@Builder
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ChatMsgDto", description="用户所有消息DTO")
public class ChatMessageResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 260395165643474068L;

    @Schema(description = "用户或者群聊id")
    private Integer id;

    @Schema(description = "会话id")
    private String chatId;
    /**
     * 聊天室类型(SINGLE:单聊,GROUP:群聊)
     */
    @Schema(description = "聊天室类型(SINGLE:单聊,GROUP:群聊)")
    private String chatType;

    /**
     * 发送用户头像
     */
    @Schema(description = "用户或者群聊头像")
    private String avatar;

    /**
     * 用户或者群聊名字
     */
    @Schema(description = "用户或者群聊名字")
    private String name;

    /**
     * 发送或者接收时间
     */
    @Schema(description = "发送或者接收时间")
    private String sendTime;

    /**
     * 时间戳
     */
    @Schema(description = "时间戳")
    private Long timestamp;

    @Schema(description = "最后的消息")
    private String lastContent;
}
