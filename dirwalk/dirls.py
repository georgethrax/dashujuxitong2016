import sys
#if len(sys.argv) < 2:
#       print("dirwalk.py rootpath savepath")
#traversrootpath = sys.argv[1]
#filesavepath = sys.argv[2]
print("dirlist.py rootpath savepathfile")
rootpath = "/data/20161010/20161002/"
filesavepath = "20161002.list"
if len(sys.argv) > 2:
    rootpath = sys.argv[1]
    filesavepath = sys.argv[2]
if len(sys.argv) < 2:
    rootpath = "."
import datetime
now = datetime.datetime.now()
print now.strftime('%Y-%m-%d %H:%M:%S')

import os
dir_L1 = os.listdir(rootpath) #'''top level'''
dict = {} #Python dictionary
f = open(filesavepath,"w")
for dir in dir_L1:
    dir_L2 = os.listdir(dir)
    dict[dir]= len (dir_L2)
    dir_name = os.path.split(dir)
    if not os.path.isdir(dir_name):
        dir_name = os.path.split(dir_name)
    f.write("%s,%d\n" %(dir_name,len(dir_L2)))
    #if os.path.isfile(os.path.join(root,dir)):
    #dict[dir]=0
f.close()

travers(traversrootpath,filesavepath)
                            
                            now = datetime.datetime.now()
                                print now.strftime('%Y-%m-%d %H:%M:%S')

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
          
