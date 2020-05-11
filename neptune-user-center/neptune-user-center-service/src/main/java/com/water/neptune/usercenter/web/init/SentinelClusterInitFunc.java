/*
 * Copyright 1999-2018 Alibaba Group Holding Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.water.neptune.usercenter.web.init;

import com.alibaba.csp.sentinel.cluster.ClusterStateManager;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientAssignConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfig;
import com.alibaba.csp.sentinel.cluster.client.config.ClusterClientConfigManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.flow.rule.ClusterParamFlowRuleManager;
import com.alibaba.csp.sentinel.cluster.server.config.ClusterServerConfigManager;
import com.alibaba.csp.sentinel.cluster.server.config.ServerTransportConfig;
import com.alibaba.csp.sentinel.datasource.ReadableDataSource;
import com.alibaba.csp.sentinel.datasource.nacos.NacosDataSource;
import com.alibaba.csp.sentinel.init.InitFunc;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRuleManager;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRuleManager;
import com.alibaba.csp.sentinel.transport.config.TransportConfig;
import com.alibaba.csp.sentinel.util.AppNameUtil;
import com.alibaba.csp.sentinel.util.HostNameUtil;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.nacos.api.PropertyKeyConst;
import com.water.neptune.usercenter.model.ClusterGroupEntity;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Properties;

/**
 * @author Eric Zhao
 */
public class SentinelClusterInitFunc implements InitFunc {

    private static final String APP_NAME = AppNameUtil.getAppName();

    private final String remoteAddress = "localhost:8848";
    private final String groupId = "SENTINEL_GROUP";

    private final String nacosNameSpaceId = "9b702cea-ca45-416a-b545-e4ac3ddc09bc";


    public static final String FLOW_POSTFIX = "-flow-rules";
    public static final String PARAM_FLOW_POSTFIX = "-param-rules";
    public static final String SERVER_NAMESPACE_SET_POSTFIX = "-cs-namespace-set";
    public static final String CLIENT_CONFIG_POSTFIX = "-cc-config";
    public static final String CLUSTER_MAP_POSTFIX = "-cluster-map";

    //    private final String flowDataId = "neptune-user-center-sentinel-flow";
    private final String flowDataId = APP_NAME + FLOW_POSTFIX;
    private final String paramDataId = APP_NAME + PARAM_FLOW_POSTFIX;
    private final String configDataId = APP_NAME + "-cluster-client-config";
    private final String clusterMapDataId = APP_NAME + CLUSTER_MAP_POSTFIX;

    @Override
    public void init() throws Exception {
        // Register client dynamic rule data source.
        initDynamicRuleProperty();

        // Register token client related data source.
        // Token client common config:
//        initClientConfigProperty();
        // Token client assign config (e.g. target token server) retrieved from assign map:
        initClientServerAssignProperty();

        // Register token server related data source.
        // Register dynamic rule data source supplier for token server:
        registerClusterRuleSupplier();
        // Token server transport config extracted from assign map:
        initServerTransportConfigProperty();

        // Init cluster state property for extracting mode from cluster map data source.
//        initStateProperty();
    }

    private void initDynamicRuleProperty() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, remoteAddress);
        properties.put(PropertyKeyConst.NAMESPACE, nacosNameSpaceId);
        ReadableDataSource<String, List<FlowRule>> flowRuleDataSource = new NacosDataSource<>(properties, groupId, flowDataId,
                source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
                }));
        FlowRuleManager.register2Property(flowRuleDataSource.getProperty());

        ReadableDataSource<String, List<ParamFlowRule>> paramRuleSource = new NacosDataSource<>(remoteAddress, groupId,
                paramDataId, source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
        }));
        ParamFlowRuleManager.register2Property(paramRuleSource.getProperty());
    }

    private void initClientConfigProperty() {
        ReadableDataSource<String, ClusterClientConfig> clientConfigDs = new NacosDataSource<>(remoteAddress, groupId,
                configDataId, source -> JSON.parseObject(source, new TypeReference<ClusterClientConfig>() {
        }));
        ClusterClientConfigManager.registerClientConfigProperty(clientConfigDs.getProperty());
    }

    private void initServerTransportConfigProperty() {
        ReadableDataSource<String, ServerTransportConfig> serverTransportDs = new NacosDataSource<>(remoteAddress, groupId,
                clusterMapDataId, source -> {
            List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {
            });
            return Optional.ofNullable(groupList)
                    .flatMap(this::extractServerTransportConfig)
                    .orElse(null);
        });
        ClusterServerConfigManager.registerServerTransportProperty(serverTransportDs.getProperty());
    }

    private void registerClusterRuleSupplier() {
        // Register cluster flow rule property supplier which creates data source by namespace.
        // Flow rule dataId format: ${namespace}-flow-rules
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.SERVER_ADDR, remoteAddress);
        properties.put(PropertyKeyConst.NAMESPACE, nacosNameSpaceId);
        ClusterFlowRuleManager.setPropertySupplier(namespace -> {
            System.out.println("namesapce ----> " + namespace);
            ReadableDataSource<String, List<FlowRule>> ds = new NacosDataSource<>(properties, groupId,
                    namespace + FLOW_POSTFIX, source -> JSON.parseObject(source, new TypeReference<List<FlowRule>>() {
            }));
            return ds.getProperty();
        });
        // Register cluster parameter flow rule property supplier which creates data source by namespace.
//        ClusterParamFlowRuleManager.setPropertySupplier(namespace -> {
//            ReadableDataSource<String, List<ParamFlowRule>> ds = new NacosDataSource<>(remoteAddress, groupId,
//                    namespace + PARAM_FLOW_POSTFIX, source -> JSON.parseObject(source, new TypeReference<List<ParamFlowRule>>() {
//            }));
//            return ds.getProperty();
//        });
    }

    private void initClientServerAssignProperty() {
        // Cluster map format:
        // [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
        // machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
        ReadableDataSource<String, ClusterClientAssignConfig> clientAssignDs = new NacosDataSource<>(remoteAddress, groupId,
                clusterMapDataId, source -> {
            List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {
            });
            return Optional.ofNullable(groupList)
                    .flatMap(this::extractClientAssignment)
                    .orElse(null);
        });
        ClusterClientConfigManager.registerServerAssignProperty(clientAssignDs.getProperty());
    }

    private void initStateProperty() {
        // Cluster map format:
        // [{"clientSet":["112.12.88.66@8729","112.12.88.67@8727"],"ip":"112.12.88.68","machineId":"112.12.88.68@8728","port":11111}]
        // machineId: <ip@commandPort>, commandPort for port exposed to Sentinel dashboard (transport module)
        ReadableDataSource<String, Integer> clusterModeDs = new NacosDataSource<>(remoteAddress, groupId,
                clusterMapDataId, source -> {
            List<ClusterGroupEntity> groupList = JSON.parseObject(source, new TypeReference<List<ClusterGroupEntity>>() {
            });
            return Optional.ofNullable(groupList)
                    .map(this::extractMode)
                    .orElse(ClusterStateManager.CLUSTER_NOT_STARTED);
        });
        ClusterStateManager.registerProperty(clusterModeDs.getProperty());
    }

    private int extractMode(List<ClusterGroupEntity> groupList) {
        // If any server group machineId matches current, then it's token server.
        if (groupList.stream().anyMatch(this::machineEqual)) {
            return ClusterStateManager.CLUSTER_SERVER;
        }
        // If current machine belongs to any of the token server group, then it's token client.
        // Otherwise it's unassigned, should be set to NOT_STARTED.
        boolean canBeClient = groupList.stream()
                .flatMap(e -> e.getClientSet().stream())
                .filter(Objects::nonNull)
                .anyMatch(e -> e.equals(getCurrentMachineId()));
        return canBeClient ? ClusterStateManager.CLUSTER_CLIENT : ClusterStateManager.CLUSTER_NOT_STARTED;
    }

    private Optional<ServerTransportConfig> extractServerTransportConfig(List<ClusterGroupEntity> groupList) {
        return groupList.stream()
                .filter(this::machineEqual)
                .findAny()
                .map(e -> new ServerTransportConfig().setPort(e.getPort()).setIdleSeconds(600));
    }

    private Optional<ClusterClientAssignConfig> extractClientAssignment(List<ClusterGroupEntity> groupList) {
        if (groupList.stream().anyMatch(this::machineEqual)) {
            return Optional.empty();
        }
        // Build client assign config from the client set of target server group.
        for (ClusterGroupEntity group : groupList) {
            if (group.getClientSet().contains(getCurrentMachineId())) {
                String ip = group.getIp();
                Integer port = group.getPort();
                return Optional.of(new ClusterClientAssignConfig(ip, port));
            }
        }
        return Optional.empty();
    }

    private boolean machineEqual(/*@Valid*/ ClusterGroupEntity group) {
        return getCurrentMachineId().equals(group.getMachineId());
    }

    private String getCurrentMachineId() {
        // Note: this may not work well for container-based env.
        return HostNameUtil.getIp() + SEPARATOR + TransportConfig.getRuntimePort();
    }

    private static final String SEPARATOR = "@";
}