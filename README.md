# Opt-Cloud-DC

基于 OpenDaylight 的光网络设备管控平台，面向运营商提供 OTN（光传送网）、WDM/DCI（波分/数据中心互联）设备的统一管理与适配能力。

## 项目概述

Opt-Cloud-DC 是一个企业级网络设备管理系统，核心目标是为电信运营商提供标准化的光网络设备纳管方案。平台通过 NETCONF 和 SNMP 协议实现设备通信，利用 YANG 模型驱动数据定义与 API 生成，支持中国移动（CMCC）和中国电信（CTC）两套运营商规范的多厂商设备适配。

### 核心能力

- **设备自动发现与纳管** — 基于 NETCONF/SSH 和 SNMP 协议的设备注册、心跳监控
- **配置管理** — 设备配置下发、查询与同步
- **告警管理** — 告警上报、屏蔽与查询
- **性能监测** — 历史与实时性能数据采集
- **保护组管理** — 1+1/1:1 保护组配置与切换
- **固件升级** — FTP 文件传输与软件版本管理
- **时钟同步** — 时钟源配置与同步状态监控
- **业务创建** — OTN/ETH 连接业务的端到端创建与管理
- **拓扑发现** — LLDP 邻居发现与网络拓扑构建

## 技术栈

| 类别 | 技术 |
|------|------|
| 语言 | Java 8+ |
| 建模 | YANG (RFC 7950) |
| 框架 | OpenDaylight (MDSAL / NetConf / RESTConf) |
| 运行容器 | Apache Karaf (OSGi) |
| 构建工具 | Maven 多模块 |
| 设备协议 | NETCONF (RFC 6241), SNMP |
| 数据模型 | OpenConfig, 自定义 ACC/YANG 模型 |

## 模块架构

```
opt-cloud-dc
├── parent/                  # 父 POM 与版本管理
│   ├── repo-parent/         # 仓库父 POM
│   ├── api-parent/          # API 模块父 POM
│   ├── impl-parent/         # 实现模块父 POM
│   ├── feature-parent/      # Karaf Feature 父 POM
│   ├── feature-repo-parent/ # Feature 仓库父 POM
│   └── karaf-parent/        # Karaf 打包父 POM
├── common/                  # 公共工具与异常定义
├── odl-source/              # OpenDaylight 定制源码
│   ├── yang-binding/        # YANG Java 绑定生成
│   ├── mdsal-binding-*/     # MDSAL 绑定适配器与编解码
│   ├── sal-netconf-connector/ # NetConf 连接器
│   ├── netconf-topology-impl/ # NetConf 拓扑实现
│   ├── shaded-sshd/         # SSH 守护进程
│   └── yang-parser-impl/    # YANG 解析器
├── general/                 # 核心框架与基础服务
│   ├── base-util/           # 基础工具类
│   ├── controller-service/  # 控制器核心服务
│   ├── nc/                  # NetConf 核心封装
│   ├── otn-yang-cmcc/       # CMCC OTN YANG 模型
│   ├── otn-yang-ctc/        # CTC OTN YANG 模型
│   ├── dci-yang-cmcc/       # CMCC DCI YANG 模型
│   ├── openconfig-yang-ctc/ # CTC OpenConfig YANG 模型
│   └── features/            # 通用 Feature 定义
├── universal-adapter/       # 通用设备适配器
│   ├── api/                 # 通用 YANG API (base-types, cli, route)
│   ├── impl/                # 通用适配实现
│   └── features/            # Feature 定义
├── otn-adapter/             # OTN 设备适配器
│   ├── api/                 # OTN YANG API (device/alarm/clock/connection...)
│   ├── data-api/            # OTN 数据 YANG 模型
│   ├── impl-base/           # OTN 基础实现
│   ├── impl-otn-cmcc/       # 中国移动 OTN 适配
│   ├── impl-otn-ctc/        # 中国电信 OTN 适配
│   └── features/            # Feature 定义
├── adapter/dci/            # WDM/DCI 设备适配器
│   ├── api/                 # DCI YANG API (ne/alarm/edfa/otdr/port...)
│   ├── dci-data-api/        # DCI 数据 YANG 模型 (OpenConfig 映射)
│   ├── impl-dci-base/       # DCI 基础实现
│   ├── impl-dci-cmcc/       # 中国移动 DCI 适配
│   ├── impl-dci-ctc/        # 中国电信 DCI 适配
│   └── features/            # Feature 定义
├── snmp/                    # SNMP 设备管理
│   ├── api/                 # SNMP YANG API
│   ├── impl/                # SNMP 实现 (设备查询/心跳/注册)
│   └── features/            # Feature 定义
├── error/                   # 错误码与 YANG 错误模型
└── dc-package/              # Karaf 发行包打包
    ├── dc-artifacts/        # 依赖制品仓库
    ├── features/            # 聚合 Feature 仓库
    └── karaf/               # Karaf Assembly 构建
```

## 分层架构

```
┌─────────────────────────────────────────────────────────┐
│                  RESTConf / NETCONF  API                 │  北向接口
├─────────────────────────────────────────────────────────┤
│  OTN Adapter  │  WDM/DCI Adapter  │  Universal Adapter  │  设备适配层
├───────────────┴───────────────────┴─────────────────────┤
│       CMCC 实现  │  CTC 实现  │  Base 实现               │  厂商/运营商实现
├─────────────────────────────────────────────────────────┤
│              General 核心框架 (BaseDeviceProvider)        │  框架层
├─────────────────────────────────────────────────────────┤
│           OpenDaylight 定制源码 (MDSAL/NetConf/SSH)      │  ODL 扩展层
├─────────────────────────────────────────────────────────┤
│              Common 公共组件 (异常/工具/转换)              │  基础层
├─────────────────────────────────────────────────────────┤
│              Apache Karaf OSGi 运行容器                   │  运行时
└─────────────────────────────────────────────────────────┘
```

## 多运营商适配

项目采用 **接口-基类-实现** 三层设计模式，为同一设备类型提供多运营商适配：

```
IDeviceService (接口)
    └── BaseOptOtnDeviceServiceImpl (基类 - 通用逻辑)
            ├── CmccOptOtnDeviceServiceImpl (移动 - CMCC 规范)
            └── CtcOptOtnDeviceServiceImpl (电信 - CTC 规范)
```

每套运营商适配使用独立的 YANG 模型与 Feature 包，通过 OSGi Bundle 热插拔实现运行时切换。

## YANG 模型体系

| 模块 | 命名空间前缀 | 用途 |
|------|-------------|------|
| `otn-yang-cmcc` | `acc-*` | CMCC OTN 设备模型 (告警/时钟/连接/保护组...) |
| `otn-yang-ctc` | `acc-*` | CTC OTN 设备模型 (含扩展字段与 RPC) |
| `dci-yang-cmcc` | `openconfig-*` / `miniotn-*` | CMCC DCI OpenConfig 模型 |
| `openconfig-yang-ctc` | `openconfig-*` | CTC OpenConfig 模型 |
| `otn-adapter/api` | `opt-otn-*` | OTN 北向 RPC API |
| `adapter/dci/api` | `opt-dci-*` | DCI 北向 RPC API |
| `universal-adapter/api` | `base-types` / `cli` | 通用类型与 CLI 命令 |
| `snmp/api` | `snmp-config` / `dev-toptel` | SNMP 配置与设备管理 |

## 构建与部署

### 环境要求

- JDK 8+
- Maven 3.5+

### 构建

```bash
# 全量构建
mvn clean install

# 跳过测试（默认已配置）
mvn clean install -Dmaven.test.skip=true
```

### 运行

```bash
# 进入 Karaf 发行目录
cd dc-package/karaf/target/assembly/bin

# 启动 Karaf
./karaf

# 或后台启动
./start
```

### 服务端口

| 服务 | 默认端口 | 说明 |
|------|---------|------|
| RESTConf | 8181 | REST API (HTTP) |
| NETCONF | 830 | NETCONF SSH 服务端 |
| Karaf SSH | 8101 | 管理控制台 |

### Karaf 命令

```bash
# 查看已安装 Feature
feature:list | grep opt

# 安装/卸载 Feature
feature:install opt-otn-cmcc
feature:uninstall opt-otn-cmcc

# 查看 Bundle 状态
bundle:list | grep optel

# 查看日志
log:tail
```

## 关键设计模式

### 设备服务注册

`BaseDeviceProvider` 负责设备服务的生命周期管理：

1. `init()` — 将 `IDeviceService` 实现注册到 `IDeviceRpcProviderService`
2. `close()` — 注销设备服务

运营商实现类继承 `BaseDeviceProvider`，通过 OSGi Blueprint XML 声明式注入。

### MDSAL 数据存储

- **Config DataStore** — 设备配置数据（持久化）
- **Operational DataStore** — 设备运行状态数据（运行时）

所有数据结构由 YANG 模型定义，Maven 编译时自动生成 Java 绑定类。

### 适配器扩展

新增设备适配器的步骤：

1. 在对应 `api` 模块定义 YANG RPC 模型
2. 在 `impl-base` 实现基础逻辑
3. 在 `impl-xxx-cmcc` / `impl-xxx-ctc` 实现运营商特化逻辑
4. 在 `features` 模块注册 Karaf Feature
5. 在 `dc-package` 聚合 Feature 仓库

## 版本

- 当前版本: `9.00-SNAPSHOT`
- GroupId: `com.optel.tmaster.dc`
- 许可证: Eclipse Public License v1.0
