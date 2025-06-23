package com.point.chat.pointcommon.entity;


import com.point.chat.pointcommon.constants.CommConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


/**
 * 分页参数对象
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Schema(name = "PageParam", description = "分页参数对象")
public class PageParam {

    @Schema(description = "当前页码", defaultValue = "1")
    private int page = CommConstant.DEFAULT_PAGE;

    @Schema(description = "每页数量", defaultValue = "20")
    private int limit = CommConstant.DEFAULT_LIMIT;

}
