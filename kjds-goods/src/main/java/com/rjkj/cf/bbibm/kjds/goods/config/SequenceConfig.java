package com.rjkj.cf.bbibm.kjds.goods.config;

import cn.hutool.core.date.DateUtil;
import com.rjkj.cf.common.data.tenant.TenantContextHolder;
import com.rjkj.cf.common.sequence.builder.DbSeqBuilder;
import com.rjkj.cf.common.sequence.properties.SequenceDbProperties;
import com.rjkj.cf.common.sequence.sequence.Sequence;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @描述：示列-设置发号器生成规则
 * @项目：支付模块
 * @公司：软江科技
 * @作者：YiHao
 * @时间：2019/9/11 17:24
 **/
@Configuration
public class SequenceConfig {

	/**
	 * 订单流水号发号器
	 *
	 * @param dataSource
	 * @param properties
	 * @return
	 */
	@Bean
	public Sequence paySequence(DataSource dataSource,
								SequenceDbProperties properties) {
		return DbSeqBuilder
				.create()
				.bizName(() -> String.format("pay_%s_%s", TenantContextHolder.getTenantId(), DateUtil.today()))
				.dataSource(dataSource)
				.step(properties.getStep())
				.retryTimes(properties.getRetryTimes())
				.tableName(properties.getTableName())
				.build();
	}

	/**
	 * 通道编号发号器
	 *
	 * @param dataSource
	 * @param properties
	 * @return
	 */
	@Bean
	public Sequence channelSequence(DataSource dataSource,
									SequenceDbProperties properties) {
		return DbSeqBuilder
				.create()
				.bizName(() -> String.format("channel_%s", TenantContextHolder.getTenantId()))
				.dataSource(dataSource)
				.step(properties.getStep())
				.retryTimes(properties.getRetryTimes())
				.tableName(properties.getTableName())
				.build();
	}
}
