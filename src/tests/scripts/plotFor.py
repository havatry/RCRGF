import numpy as np
import matplotlib.pyplot as plt
import sys

class Plot:
    def __init__(self, filename):
        self.filename = filename # csv文件
        self.data = np.loadtxt(open(filename, 'rb'), delimiter=",", skiprows=0)

    def plotSubstrateGroup(self):
        x = [i for i in range(100, 300, 20)]
        rcrg = self.rcrgfDataGroup()
        subgraph = self.subgraphDataGroup()
        greedy = self.greedyDataGroup()
        plt.subplot(3, 1, 1)
        plt.plot(x, rcrg[0], label='RCRGF', color='b', marker='*')
        plt.plot(x, subgraph[0], label='SubgraphIsomorphism', color='k', marker='*')
        plt.plot(x, greedy[0], label='Greedy', color='r', marker='*')
        plt.xlabel('(a)')
        plt.ylabel('Execution Time(s)')
        plt.legend()

        plt.subplot(3, 1, 2)
        plt.plot(x, rcrg[1], label='RCRGF', color='b', marker='*')
        plt.plot(x, subgraph[1], label='SubgraphIsomorphism', color='k', marker='*')
        plt.plot(x, greedy[1], label='Greedy', color='r', marker='*')
        plt.xlabel('(b)')
        plt.ylabel('Acceptance Ratio')
        plt.legend()

        plt.subplot(3, 1, 3)
        plt.plot(x, rcrg[2], label='RCRGF', color='b', marker='*')
        plt.plot(x, subgraph[2], label='SubgraphIsomorphism', color='k', marker='*')
        plt.plot(x, greedy[2], label='Greedy', color='r', marker='*')
        plt.xlabel('(c)')
        plt.ylabel('Revenue to Cost Ratio')
        plt.legend()
        plt.show()


    def plotRatio(self):
        #画关于ratio的接收率情况
        arr = self.ratioForVnodes()
        x = [3, 5, 7, 9, 11, 13, 15, 17, 19, 21]
        plt.subplot(2, 1, 1)
        plt.plot(x, arr[0], label='RCRGF', color='b', marker='*')
        plt.plot(x, arr[1], label='SubgraphIsomorphism', color='k', marker='*')
        plt.plot(x, arr[2], label='Greedy', color='r', marker='*')
        plt.xlabel('(a)')
        plt.ylabel('Acceptance Ration')
        plt.legend()

        arr = self.ratioForRation()
        plt.subplot(2, 1, 2)
        x2 = [0.01, 0.02, 0.03, 0.04, 0.05, 0.06, 0.07, 0.08, 0.09, 0.10]
        plt.plot(x2, arr[0], label='RCRGF', color='b', marker='*')
        plt.plot(x2, arr[1], label='SubgraphIsomorphism', color='k', marker='*')
        plt.plot(x2, arr[2], label='Greedy', color='r', marker='*')
        plt.xlabel('(b)')
        plt.ylabel('Acceptance Ration')
        plt.legend()
        plt.show()

    def rcrgfDataGroup(self):
        # 返回关于rcrgf的三种数据
        rd_time = []
        rd_ratio = []
        rd_revenue = []
        # 计算执行时间
        for i in range(10):
            temp_time = self.data[100 * i : 100 * (i + 1), 0]
            rd_time.append(np.mean(temp_time) / 1000)
            temp_ratio = self.data[100 * i : 100 * (i + 1), 1]
            rd_ratio.append(np.mean(temp_ratio))
            temp_revenue = self.data[100 * i : 100 * (i + 1), 2]
            temp_revenue = temp_revenue[temp_revenue > 0]
            rd_revenue.append(np.mean(temp_revenue))
        return rd_time, rd_ratio, rd_revenue

    def subgraphDataGroup(self):
        sg_time = []
        sg_ratio = []
        sg_revenue = []
        for i in range(10):
            temp_time = self.data[100 * i : 100 * (i + 1), 3]
            sg_time.append(np.mean(temp_time) / 1000)
            temp_ratio = self.data[100 * i : 100 * (i + 1), 4]
            sg_ratio.append(np.mean(temp_ratio))
            temp_revenue = self.data[100 * i : 100 * (i + 1), 5]
            temp_revenue = temp_revenue[temp_revenue > 0]
            sg_revenue.append(np.mean(temp_revenue))
        return sg_time, sg_ratio, sg_revenue


    def greedyDataGroup(self):
        gd_time = []
        gd_ratio = []
        gd_revenue = []
        # 计算执行时间
        for i in range(10):
            temp_time = self.data[100 * i : 100 * (i + 1), 6]
            gd_time.append(np.mean(temp_time) / 1000)
            temp_ratio = self.data[100 * i : 100 * (i + 1), 7]
            gd_ratio.append(np.mean(temp_ratio))
            temp_revenue = self.data[100 * i : 100 * (i + 1), 8]
            # 接收率去除为0的
            temp_revenue = temp_revenue[temp_revenue > 0]
            gd_revenue.append(np.mean(temp_revenue))
        return gd_time, gd_ratio, gd_revenue


    def ratioForVnodes(self):
        # 记下三种算法的
        ratio_rcrgf = []
        ratio_subgraph = []
        ratio_greedy = []
        for j in range(10):
            m = 0
            n = 0
            t = 0
            for i in range(10):
                m = m + np.sum(self.data[100 * i + 10 * j : 100 * i + 10 * (j + 1), 1])
                n = n + np.sum(self.data[100 * i + 10 * j : 100 * i + 10 * (j + 1), 4])
                t = t + np.sum(self.data[100 * i + 10 * j: 100 * i + 10 * (j + 1), 7])
            ratio_rcrgf.append(m / 100)
            ratio_subgraph.append(n / 100)
            ratio_greedy.append(t / 100)
        return ratio_rcrgf, ratio_subgraph, ratio_greedy


    def ratioForRation(self):
        # 记下虚拟网络的拓扑
        alpha_rcrgf = []
        alpha_subgraph = []
        alpha_greedy = []
        for i in range(10):
            rcrgf = []
            subgraph = []
            greedy = []
            for j in range(10):
                rcrgf.append(np.mean(self.data[j::10, 1]))
                subgraph.append(np.mean(self.data[j::10, 4]))
                greedy.append(np.mean(self.data[j::100, 7]))
            alpha_rcrgf.append(np.mean(rcrgf))
            alpha_subgraph.append(np.mean(subgraph))
            alpha_greedy.append(np.mean(greedy))
        return  alpha_rcrgf, alpha_subgraph, alpha_greedy


if __name__ == '__main__':
    path = sys.argv[1]
    plot = Plot(path)
    if sys.argv[2] == 'group':
        plot.plotSubstrateGroup()
    elif sys.argv[2] == 'ration':
        plot.plotRatio()