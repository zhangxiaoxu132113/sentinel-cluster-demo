package com.alibaba.csp.sentinel.dashboard.rule.nacos;

import com.alibaba.csp.sentinel.dashboard.datasource.entity.rule.FlowRuleEntity;
import com.alibaba.csp.sentinel.dashboard.rule.DynamicRuleProvider;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.util.StringUtil;
import com.alibaba.nacos.api.config.ConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component("flowRuleNacosProvider")
public class FlowRuleNacosProvider implements DynamicRuleProvider<List<FlowRuleEntity>> {
 
    private static Logger logger = LoggerFactory.getLogger(FlowRuleNacosProvider.class);
 
    @Autowired
    private NacosConfigProperties nacosConfigProperties;
 
    @Autowired
    private ConfigService configService;
 
    @Autowired
    private Converter<String, List<FlowRuleEntity>> converter;
 
    @Override
    public List<FlowRuleEntity> getRules(String appName) throws Exception {
        String rules = configService.getConfig(appName + NacosConfigConstant.FLOW_DATA_ID_POSTFIX, nacosConfigProperties.getGroupId(), 3000);
        logger.info("从Nacos中拉取到限流规则信息:{}",rules);
        if (StringUtil.isEmpty(rules)) {
            return new ArrayList<>();
        }
        return converter.convert(rules);
    }
}