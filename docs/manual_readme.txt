手工测试结果上报统计

一，数据及依赖维度
1. 收集数据维度
1) 帧率：Theme页面滑动，emoji页面滑动, 主键盘与符号键盘切换，主键盘与emoji键盘切换，键盘打字弹泡
2）CPU：空闲状态，中等时长状态和长时间运行状态下各自的最大最小值
3) 内存：一定时间内采样点的值
4) 电流：5分钟定时平均和息屏
5) 响应时间：键盘收起，按键响应时间

2. 产品及版本维度
1）Tapany各版本
2）Swiftkey及不同版本
3）TouchPal
4）Gboard
5) Facemoji

3. 测试硬件+运行系统版本维度


二，数据格式，复用改造原apm的设计
每次data上报都带上"deviceID", "timestamp",和"app"信息，其中deviceID可以追溯到connect上报记录(内含硬件和os系统信息)，app含应用包名，版本和应用名，timestamp为日期时间戳
同一个应用版本的数据打包进"measurement"数组，数组中每条数据，scope = "manual"