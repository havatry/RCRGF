import numpy as np
import matplotlib.pyplot as plt
import sys

class PlotPic:
    '''
    对一阶段算法实验进行绘图展示
    '''
    def __init__(self, filename):
        self.x = np.linspace(100, 300,40, False)
        # 读取文件中内容
        with open(filename) as f:
            content = f.read()
        self.list = []
        for line in content.split('\n'):
            self.list.append(line.split(','))
        self.list = self.list[:-1]


    def plotRunTime(self):
        y1 = [int(self.list[i][0]) for i in range(len(self.list))]
        y2 = [int(self.list[i][3]) for i in range(len(self.list))]
        y3 = [int(self.list[i][6]) for i in range(len(self.list))]
        plt.plot(self.x, y1, color='r', marker='o', label='RCRGF(our algorithm)')
        plt.plot(self.x, y2, color='g', marker='*', label='SubgraphIsomorphism')
        plt.plot(self.x, y3, color='b', marker='+', label='Greedy')
        plt.xlabel('Substrate Nodes')
        plt.ylabel('Execution Time(ms)')
        plt.legend()
        plt.show()

    def plotAcceptanceRatio(self):
        y1 = [float(self.list[i][1]) for i in range(len(self.list))]
        y2 = [float(self.list[i][4]) for i in range(len(self.list))]
        y3 = [float(self.list[i][7]) for i in range(len(self.list))]
        plt.scatter(self.x, y1, color='r', marker='o', label='RCRGF(our algorithm)')
        plt.scatter(self.x, y2, color='g', marker='*', label='SubgraphIsomorphism')
        plt.scatter(self.x, y3, color='b', marker='+', label='Greedy')
        plt.xlabel('Substrate Nodes')
        plt.ylabel('Average Acceptance Ratio')
        plt.legend()
        plt.show()


    def plotRevenueToCost(self):
        y1 = [float(self.list[i][2]) for i in range(len(self.list))]
        y2 = [float(self.list[i][5]) for i in range(len(self.list))]
        y3 = [float(self.list[i][8]) for i in range(len(self.list))]
        plt.scatter(self.x, y1, color='r', marker='o', label='RCRGF(our algorithm)')
        plt.scatter(self.x, y2, color='g', marker='*', label='SubgraphIsomorphism')
        plt.scatter(self.x, y3, color='b', marker='+', label='Greedy')
        plt.xlabel('Substrate Nodes')
        plt.ylabel('Revenue/Cost Ratio')
        plt.legend()
        plt.show()


if __name__ == '__main__':
    if len(sys.argv) < 3:
        print('Usage: python plotOneStage.py filename op(such as: rt, ar, r2c)')
        sys.exit(0)
    # sys.argv = ['', r'C:\Users\huded\Desktop\simulation.txt', 'ar']
    filename = sys.argv[1]
    op = sys.argv[2]
    pc = PlotPic(filename)
    if op == 'rt':
        pc.plotRunTime()
    elif op =='ar':
        pc.plotAcceptanceRatio()
    elif op == 'r2c':
        pc.plotRevenueToCost()
    else:
        print('Unknown operation')
        sys.exit(1)