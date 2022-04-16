package com.hotgroup.manage.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * @author Lzw
 * @date 2021/4/27.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class IndexVo implements Serializable {
    private static final long serialVersionUID = 1L;

    private long onlineUser;
    private long userSize;
    private long deptSize;
    private long licenseServiceCount;
    private String startDate;
    private List<NameValueVo> deptStatistics;
    private List<NameValueVo> authorizedInfos;
    private List<HistoryUserInfo> historyUserInfos;


}
