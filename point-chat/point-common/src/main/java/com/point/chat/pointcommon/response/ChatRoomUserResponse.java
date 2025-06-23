package com.point.chat.pointcommon.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;

/**
 * 用户聊天室返回对象
 * 
 * @author Dao-yang
 * @date: 2024-01-10 09:56:44
 */
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(name = "ChatRoomUserResponse", description="用户聊天室返回对象")
public class ChatRoomUserResponse implements Serializable {

	@Serial
	private static final long serialVersionUID =  7934754433847447124L;

	/**
	 * 主键ID
	 */
	@Schema(description = "主键ID")
	private Integer id;

	/**
	 * 聊天室id
	 */
	@Schema(description = "聊天室id")
	private String chatId;

	/**
	 * 聊天室类型(SINGLE:单聊,GROUP:群聊)
	 */
	@Schema(description = "聊天室类型(SINGLE:单聊,GROUP:群聊)")
	private String chatType;

	/**
	 * 接收消息的用户信息
	 */
	@Schema(description = "接收消息的用户/或者群组信息")
	private ChatUserSimResponse toUser;

	/**
	 * 群组ID,群聊时groupId有值
	 */
	@Schema(description = "群组ID,群聊时groupId有值")
	private Integer groupId;

	/**
	 * 最新一条消息id
	 */
	@Schema(description = "最新一条消息id")
	private Integer lastMsgId;

	/**
	 * 最新一条信息(LatestMsg),如果消息类型是图片,显示(“图片”),商品显示“商品”
	 */
	@Schema(description = "最新一条信息(LatestMsg),如果消息类型是图片,显示(“图片”),商品显示“商品”")
	private String lastMsg;

	/**
	 * 最后消息发送时间
	 */
	@Schema(description = "最后消息发送时间")
	private String lastTime;

	/**
	 * 时间戳
	 */
	@Schema(description = "时间戳")
	private Long timestamp;

	/**
	 * 创建时间
	 */
	@Schema(description = "创建时间")
	private String createTime;

	/**
	 * 用户id
	 */
	@Schema(description = "用户id")
	private Integer userId;

	/**
	 * 是否置顶(true:置顶;false:不置顶)
	 */
	@Schema(description = "是否置顶(true:置顶;false:不置顶)")
	private Boolean isTop;

	/**
	 * 消息免打扰(true:开启免打扰;false:关闭免打扰)
	 */
	@Schema(description = "消息免打扰(true:开启免打扰;false:关闭免打扰)")
	private Boolean msgNoDisturb;

	/**
	 * 未读消息数
	 */
	@Schema(description = "未读消息数")
	private Integer unreadMsgCount;
}
