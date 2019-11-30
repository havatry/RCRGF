## alevin2

1. summaryResult的输出结果不显示, 在测试用例中已经可以调出结果 Finish
2. summaryResult中的数据不符合预期, 使用小规模网络进行测试 Process
	I) 为什么多次映射不会累计资源 -> 这是因为对于一个对象 其状态变量curNetIt没有重置所致使的
	II) 为什么影视不成功 还在占用资源呢 -> 这和上面是一个原因，需要重置processedLinks和mappedLinks两个变量，因为mappedLinks这个变量受到前面没有映射成功的虚拟网络影响，这样mappedLinks这个变量会比processedLinks要小，从整体上来看
	III) 断言出错 图中没有找到起点和终点
3. 在链路映射方面还需要改进，比如使用断路法结合BFS来找最短路径
4. 加入其他算法比较
5. 咨询下nodeMapping那段代码是否可以那么更改

