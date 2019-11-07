import os
import  sys

class FileProcess:
    def __init__(self, directory):
        self.directory = directory


    def readAllFiles(self):
        # 读取目录下的文件
        return os.listdir(self.directory)


    def renameFile(self):
        files = self.readAllFiles()
        os.chdir(self.directory)
        for filename in files:
            index = filename.rindex('.')
            content = filename[:index]
            parts = content.split('_')
            parts[3] = str(round(float(parts[3]),2))
            parts[4] = str(round(float(parts[4]), 1))
            newFilename = '_'.join(parts) + filename[index:]
            os.rename(filename, newFilename)

    def deleteLog(self):
        # 删除xml的文件
        files = self.readAllFiles()
        os.chdir(self.directory)
        for filename in files:
            if 'xml' not in filename:
                os.remove(filename)


if __name__ == '__main__':
    direct = sys.argv[1]
    op = sys.argv[2]
    fp = FileProcess(direct)
    if op == 'mv':
        fp.renameFile()
    elif op == 'rm':
        fp.deleteLog()
    print('Files Process Done')