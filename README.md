# RCRGF
虚拟网络映射一阶段算法

--帮助文档
1. 配置文件，文件为results/conf/rcrgf.properties

   ```properties
   rcrgf.snode.min = 100 # 测试组最小底层节点数
   rcrgf.snode.max = 300 # 测试组最大底层节点数
   rcrgf.snode.step = 5 # 测试组之间底层节点的差，或者称为步长
   rcrgf.ratio = 0.2 # 虚拟网络需求和底层网络资源的比值， 底层网络的CPU资源和带宽资源默认都是10000。在确定最大值后，采用均匀分布U[0,10000]来分配资源和需求
   rcrgf.alpha = 0.1 # 虚拟网络waxman模型的alpha参数，该值越大，对应的边越多
   ```

   ​

2. 拓扑生成, 使用类vnreal.algorithms.myRCRGF.test.toGen
  点击运行后，生成的文件在results/fileRcrgf目录下，有多少组使用就有多少个网络拓扑文件，文件命名包含网络结构参数。

3. 仿真测试，使用类vnreal.algorithms.myRCRGF.test.Simulation
  直接运行后，结果文件保存在results/outputRcrgf/simulation.txt中，注意多次运行会覆盖。可以使用该目录下的python脚本plotOneStage.py可视化结果。使用命令为python plotOneStage simulation.txt ar|rt|r2c

4. 对比算法，其中filename不需要显示指定，默认即可

    | 算法                  | 处理                   |
    | ------------------- | -------------------- |
    | RCRGF               | doRCRGF(filename)    |
    | SubgraphIsomorphism | doSubgraph(filename) |
    | Greedy              | doGreedy(filename)   |