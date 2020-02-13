import numpy as np
import matplotlib.pyplot as plt
import sys

class PlotFig:
    '''
        基于实验数据进行作图
    '''
    def __init__(self, filenamePrefix):
        self.x = np.linspace(0, 2000, 40, True)
        # 读取文件内容
        self.aefBaseline = np.loadtxt(filenamePrefix + 'aefBaseline.txt')
        self.aefAdvance = np.loadtxt(filenamePrefix + 'aefAdvance.txt')
        # self.subgrap = np.loadtxt(filenamePrefix + 'subgraph.txt')
        # self.ViNE = np.loadtxt(filenamePrefix + 'ViNE.txt')
        self.NRM = np.loadtxt(filenamePrefix + 'NRM.txt')


    # 画运行时间对比图
    def plotRunTime(self, part=True):
        y1 = self.aefAdvance[0]
        plt.plot(self.x, y1, color='r', marker='*', label='AEF_Advance(our algorithm)')
        y2 = self.aefBaseline[0]
        plt.plot(self.x, y2, color='b', marker='o', label='AEF_Baseline(our algoritm)')
        # y3 = self.subgrap[0]
        # plt.plot(self.x, y3, color='c', marker='+', label='subgraphIsomorphism')
        # y4 = self.ViNE[0]
        # if not part:
        #    plt.plot(self.x, y4, color='m', marker='x', label='D-ViNE_SP')
        y5 = self.NRM[0]
        plt.plot(self.x, y5, color='g', marker='|', label='NRM')
        plt.xlabel('Time Unit')
        plt.ylabel('Execution Time(ms)')
        plt.legend()
        plt.show()


    # 画接收率对比图
    def plotAcceptanceRatio(self):
        y1 = self.aefAdvance[1]
        plt.plot(self.x, y1, color='r', marker='*', label='AEF_Advance(our algorithm)')
        y2 = self.aefBaseline[1]
        plt.plot(self.x, y2, color='b', marker='o', label='AEF_Baseline(our algoritm)')
        # y3 = self.subgrap[1]
        # plt.plot(self.x, y3, color='c', marker='+', label='subgraphIsomorphism')
        # y4 = self.ViNE[1]
        # plt.plot(self.x, y4, color='m', marker='x', label='D-ViNE_SP')
        y5 = self.NRM[1]
        plt.plot(self.x, y5, color='g', marker='|', label='NRM')
        plt.xlabel('Time Unit')
        plt.ylabel('Acceptance Ratio')
        plt.legend()
        plt.show()


    # 画收益代价比对比图
    def plotRevenue2Cost(self):
        y1 = self.aefAdvance[2]
        plt.plot(self.x, y1, color='r', marker='*', label='AEF_Advance(our algorithm)')
        y2 = self.aefBaseline[2]
        plt.plot(self.x, y2, color='b', marker='o', label='AEF_Baseline(our algoritm)')
        # y3 = self.subgrap[2]
        # plt.plot(self.x, y3, color='c', marker='+', label='subgraphIsomorphism')
        # y4 = self.ViNE[2]
        # plt.plot(self.x, y4, color='m', marker='x', label='D-ViNE_SP')
        y5 = self.NRM[2]
        plt.plot(self.x, y5, color='g', marker='|', label='NRM')
        plt.xlabel('Time Unit')
        plt.ylabel('Cost to Revenue Ratio')
        plt.legend()
        plt.show()


    # 画单位收益对比图
    def plotUnitRevenue(self):
        y1 = self.aefAdvance[3]
        plt.plot(self.x, y1, color='r', marker='*', label='AEF_Advance(our algorithm)')
        y2 = self.aefBaseline[3]
        plt.plot(self.x, y2, color='b', marker='o', label='AEF_Baseline(our algoritm)')
        # y3 = self.subgrap[3]
        # plt.plot(self.x, y3, color='c', marker='+', label='subgraphIsomorphism')
        # y4 = self.ViNE[3]
        # plt.plot(self.x, y4, color='m', marker='x', label='D-ViNE_SP')
        y5 = self.NRM[3]
        plt.plot(self.x, y5, color='g', marker='|', label='NRM')
        plt.xlabel('Time Unit')
        plt.ylabel('Revenue to Time Unit Ratio')
        plt.legend()
        plt.show()


    # 画链路负载对比图
    def plotLinkLoad(self):
        y1 = self.aefAdvance[4]
        plt.plot(self.x, y1, color='r', marker='*', label='AEF_Advance(our algorithm)')
        y2 = self.aefBaseline[4]
        plt.plot(self.x, y2, color='b', marker='o', label='AEF_Baseline(our algoritm)')
        # y3 = self.subgrap[4]
        # plt.plot(self.x, y3, color='c', marker='+', label='subgraphIsomorphism')
        # y4 = self.ViNE[4]
        # plt.plot(self.x, y4, color='m', marker='x', label='D-ViNE_SP')
        y5 = self.NRM[4]
        plt.plot(self.x, y5, color='g', marker='|', label='NRM')
        plt.xlabel('Time Unit')
        plt.ylabel('Std')
        plt.legend()
        plt.show()


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print('Usage: python plotFor.py filenamePrefix op(such as: rt, ar, r2c, ll)')
        sys.exit(0)
    filenamePrefix = sys.argv[1]
    op = sys.argv[2]
    pf = PlotFig(filenamePrefix)
    if op == 'rt':
        pf.plotRunTime()
    elif op == 'rta':
        pf.plotRunTime(False)
    elif op == 'ar':
        pf.plotAcceptanceRatio()
    elif op == 'r2c':
        pf.plotRevenue2Cost()
    # elif op == 'ur':
    #    pf.plotUnitRevenue()
    elif op == 'll':
        pf.plotLinkLoad()
    else:
        print('Unknown Operation')