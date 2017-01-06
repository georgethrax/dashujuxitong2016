#coding=utf-8
filesplit = open('/data/20161002.big.split')
fileindex = open('/data/20161002.index')
filekv = open('20161002.big.kv','w')
count = 0
for line_index in fileindex:
	line_big = filesplit.readline()
	line_k = '/'.join(line_index.strip().split('/')[3:6])
	line_kv = line_k+','+line_big.strip()+'\n'
	filekv.write(line_kv)
	count = count + 1
filekv.close()
fileindex.close()
filesplit.close()
print('count=%d\n' %(count))
'''
健康/果酱/ad68643c88fc4ff22750930fc04e5a34.txt,蜂蜜 柚子茶 什么 时候 喝             蜂蜜 柚子茶 不仅 是 味道 清香 可口  而且 还 据称 有着 有美白 祛斑  嫩肤 养颜  通便 排毒 的 功效  经常 食用 可以 清热 降火  嫩白 皮肤                     蜂蜜 柚子茶 不仅 是 味道 清香 可口  而且 还 据称 有着 有美白 祛斑  嫩肤 养颜  通便 排毒 的 功效  经常 食用 可以 清热 降火  嫩白 皮肤  尤其 适合 每天 都 坐在 办公室  皮肤 遭受 电脑 辐射损伤  气色 暗淡 的 办公室 女性  不过 不少 人 也 一直 有着 一个 疑问  蜂蜜 柚子茶 什么 时候 喝好          不可 空腹 饮用       空腹 饮用 可能 会 对 肠胃 造成 刺激  容易 导致 腹泻 等 肠胃 不适 的 症状          饭后 不可 立即 喝       倘若 吃完饭 后 立即 喝茶 会 影响 铁 的 吸收  时间 长 了 容易 诱发 贫血                   最佳 时间  饭后 一 小时       不会 对 胃肠 和 营养 吸收 造成 任何 不良影响  也 可以 让 身体 更好 地 吸收 蜂蜜 柚子茶 的 营养          另外 必须 注意 的 是  蜂蜜 柚子茶 冲泡 的 时候 不要 使用 滚烫 的 开水  否则 会 破坏 蜂蜜 的 营养成分 也 会 影响 口味  一般 用    左右 的 温开水 冲泡 最为 适宜  如 需作 冷饮  则 加入 冰块 冷却 即可        此外  蜂蜜 柚子茶 还 能 用作 调制 鸡尾酒  当成 果酱 涂抹 面包 和 做成 柚子 果冻 食用    图片 来源
'''