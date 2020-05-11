//package com.water.neptune.usercenter.core;
//
//import com.alibaba.csp.sentinel.datasource.Converter;
//import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
//import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
//import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
//import com.alibaba.fastjson.JSON;
//import com.alibaba.fastjson.TypeReference;
//import com.alibaba.nacos.api.PropertyKeyConst;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.boot.ApplicationArguments;
//import org.springframework.boot.ApplicationRunner;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//import java.util.Properties;
//
///**
// * @author : zhangmiaojie
// * @date : 2020/5/7 15:05
// */
//@Slf4j
//@Component
//public class ApplicationRunnerImpl implements ApplicationRunner {
//
//    private final String REMOTE_ADDRESS = "localhost:8848";
//    private final String groupId = "SENTINEL_GROUP";
//    private String dataId = "neptune-user-center-sentinel";
//    private String nacosNameSpaceId = "9b702cea-ca45-416a-b545-e4ac3ddc09bc";
//
//    @Override
//    public void run(ApplicationArguments args) throws Exception {
//        log.info("sentinel register nacos datasource...");
//        Properties properties = new Properties();
//        properties.put(PropertyKeyConst.SERVER_ADDR, REMOTE_ADDRESS);
//        properties.put(PropertyKeyConst.NAMESPACE, nacosNameSpaceId);
//
//        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(properties, groupId, dataId,
//                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
//                }));
//        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());
//    }
//}
