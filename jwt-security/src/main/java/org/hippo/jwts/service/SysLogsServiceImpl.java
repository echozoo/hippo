package org.hippo.jwts.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.hippo.jwts.mapper.SysLogsMapper;
import org.hippo.jwts.po.SysLogs;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author dujf
 * @since 2018-12-29
 */
@Service
public class SysLogsServiceImpl extends ServiceImpl<SysLogsMapper, SysLogs> implements ISysLogsService {

}
