import os
def travers(path,save):
    dict = {} #Python字典类型dictionary
    filecount =
    fsave = open(save,"w")
    for root, dirs, files in os.walk( path ):
        for fn in files:
            fsave.write("%s,%s\n" %(root,fn))
                filecount = filecount + 1
        fsave.close()
    print("filecount=%d" %filecount)
    print(
import sys
#if len(sys.argv) < 2:
#       print("dirwalk.py rootpath savepath")
#traversrootpath = sys.argv[1]
#filesavepath = sys.argv[2]
traversrootpath = "/data/20161010/20161002/"
filesavepath = "20161002.walk"
import datetime
now = datetime.datetime.now()
print now.strftime('%Y-%m-%d %H:%M:%S')

travers(traversrootpath,filesavepath)

now = datetime.datetime.now()
print now.strftime('%Y-%m-%d %H:%M:%S')

