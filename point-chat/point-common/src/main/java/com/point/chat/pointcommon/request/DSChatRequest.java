package com.point.chat.pointcommon.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Size;
import lombok.Data;
import com.point.chat.pointcommon.entity.DSChatRequestMessage;
import java.util.List;

/**
 * DeepSeek AI 接口请求信息对象
 *
 */
@Data
@Schema(name = "DSChatRequest", description = "DeepSeek AI 请求对象")
public class DSChatRequest {

    @Valid
    @Schema(description = "消息对象列表", requiredMode = Schema.RequiredMode.REQUIRED)
    @Size(min = 1, max = 8, message = "消息对象不能少于1个超过8个")
    private List<DSChatRequestMessage> messages;
}
