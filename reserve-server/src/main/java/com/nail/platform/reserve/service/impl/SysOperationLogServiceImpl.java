package com.nail.platform.reserve.service.impl;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.nail.common.dto.OperationLogDTO;
import com.nail.platform.reserve.entity.SysOperationLog;
import com.nail.platform.reserve.mapper.SysOperationLogMapper;
import com.nail.platform.reserve.service.SysOperationLogService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 系统操作日志服务实现
 *
 * @author nail-platform
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class SysOperationLogServiceImpl implements SysOperationLogService {

    private final SysOperationLogMapper sysOperationLogMapper;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(OperationLogDTO logDTO) {
        SysOperationLog sysOperationLog = new SysOperationLog();

        // 复制属性
        BeanUtils.copyProperties(logDTO, sysOperationLog);

        // 尝试从Sa-Token获取当前用户信息
        try {
            if (StpUtil.isLogin()) {
                Object loginId = StpUtil.getLoginIdDefaultNull();
                if (loginId != null) {
                    sysOperationLog.setOperatorId(Long.valueOf(loginId.toString()));
                    // 可以从扩展信息中获取用户名
                    sysOperationLog.setOperatorName(StpUtil.getTokenInfo().getLoginId().toString());
                }
            }
        } catch (Exception e) {
            // 未登录或获取失败时，操作人为空
            log.debug("获取当前登录用户信息失败，可能为未登录状态", e);
        }

        sysOperationLogMapper.insert(sysOperationLog);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void save(SysOperationLog sysOperationLog) {
        sysOperationLogMapper.insert(sysOperationLog);
    }
}