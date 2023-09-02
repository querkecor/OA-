package com.aug.process.mapper;

import com.aug.model.process.Process;
import com.aug.vo.process.ProcessQueryVo;
import com.aug.vo.process.ProcessVo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * 审批类型 Mapper 接口
 * </p>
 *
 * @author querkecor
 * @since 2023-08-29
 */
@Mapper
public interface ProcessMapper extends BaseMapper<Process> {

    IPage<ProcessVo> selectPageVo(IPage<ProcessVo> page, @Param("vo") ProcessQueryVo processQueryVo);

}
