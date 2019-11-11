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
        x = np.linspace(5, 50, 10, True)
        plt.subplot(2, 1, 1)
        plt.plot(x, arr[0], label='RCRGF', color='b', marker='*')
        plt.plot(x, arr[1], label='SubgraphIsomorphism', color='k', marker='*')
        plt.plot(x, arr[2], label='Greedy', color='r', marker='*')
        plt.xlabel('VNodes')
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
            temp_time = self.data[10 * i : 10 * (i + 1), 0]
            rd_time.append(np.mean(temp_time) / 1000)
            temp_ratio = self.data[10 * i : 10 * (i + 1), 1]
            rd_ratio.append(np.mean(temp_ratio))
            temp_revenue = self.data[10 * i : 10 * (i + 1), 2]
            temp_revenue = temp_revenue[temp_revenue > 0]
            rd_revenue.append(np.mean(temp_revenue))
        return rd_time, rd_ratio, rd_revenue


    def subgraphDataGroup(self):
        sg_time = []
        sg_ratio = []
        sg_revenue = []
        for i in range(10):
            temp_time = self.data[10 * i : 10 * (i + 1), 3]
            sg_time.append(np.mean(temp_time) / 1000)
            temp_ratio = self.data[10 * i : 10 * (i + 1), 4]
            sg_ratio.append(np.mean(temp_ratio))
            temp_revenue = self.data[10 * i : 10 * (i + 1), 5]
            temp_revenue = temp_revenue[temp_revenue > 0]
            sg_revenue.append(np.mean(temp_revenue))
        return sg_time, sg_ratio, sg_revenue


    def greedyDataGroup(self):
        gd_time = []
        gd_ratio = []
        gd_revenue = []
        # 计算执行时间
        for i in range(10):
            temp_time = self.data[10 * i : 10 * (i + 1), 6]
            gd_time.append(np.mean(temp_time) / 1000)
            temp_ratio = self.data[10 * i : 10 * (i + 1), 7]
            gd_ratio.append(np.mean(temp_ratio))
            temp_revenue = self.data[10 * i : 10 * (i + 1), 8]
            # 接收率去除为0的
            temp_revenue = temp_revenue[temp_revenue > 0]
            gd_revenue.append(np.mean(temp_revenue))
        return gd_time, gd_ratio, gd_revenue


    def ratioForVnodes(self):
        # 记下三种算法的
        ratio_rcrgf = []
        ratio_subgraph = []
        ratio_greedy = []
        for i in range(10):
            m = np.mean(self.data[i::10, 1])
            n = np.mean(self.data[i::10, 4])
            t = np.mean(self.data[i::10, 7])
            ratio_rcrgf.append(m)
            ratio_subgraph.append(n)
            ratio_greedy.append(t)
        return ratio_rcrgf, ratio_subgraph, ratio_greedy


if __name__ == '__main__':
    path = sys.argv[1]
    plot = Plot(path)
    if sys.argv[2] == 'group':
        plot.plotSubstrateGroup()
    elif sys.argv[2] == 'ration':
        plot.plotRatio()