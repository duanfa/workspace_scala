package com.st.ufsCluster.etl.recipe

import org.apache.spark.SparkConf
import org.apache.spark.SparkContext
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.sql.SQLContext
import org.apache.spark.rdd.RDD.rddToOrderedRDDFunctions
import org.apache.spark.rdd.RDD.rddToPairRDDFunctions
/**
 * @author socket
 */
object RecipeETLBinary {
  val recipes = "烤鸭,虾饺,水煮鱼,毛血旺,三文鱼,虾滑,羊肉串,冰火菠萝油,虾饺皇,榴莲酥,水煮鲶鱼,馋嘴牛蛙,宫保鸡丁,面包诱惑,钵钵鸡,红烧肉,凤爪,酸菜鱼,滑牛肉,剁椒鱼头,乳鸽,骨头锅,皮蛋瘦肉粥,东坡肉,咖喱皇炒蟹,鸭血,秘制酥皮虾,蜂蜜厚多士,绿茶烤肉,抻面,三杯鸡,鸳鸯鱼头,麻婆豆腐,三文鱼刺身,口水鸡,锅包肉,牛排,手撕包菜,豉汁蒸凤爪,鲜虾云吞,绿茶饼,酸汤肥牛,腊味煲仔饭,土豆泥,蟹黄豆腐,菠萝油,鸳鸯锅底,干锅牛蛙,炭烧猪颈肉,菲力牛排,肠粉,马来咖喱牛腩,提拉米苏,松鼠桂鱼,麻辣小龙虾,精品烤鸭,烧鹅,大盘鸡,芥末鸭掌,馋嘴蛙,茶香鸡,扇贝,鱼头泡饼,炭烧生蚝,龙井虾仁,咖喱牛腩,清炒虾仁,脆皮乳鸽,糖醋小排,西湖醋鱼,脆皮叉烧,毛肚,金牌煎饺,臭豆腐,皇坛子,鸡翅,墨鱼水饺,菠萝饭,煲仔饭,北京烤鸭,夫妻肺片,功夫鱼,熏鱼,厚百叶,生蚝,干炒牛河,狮子头,红糖糍粑,蓝莓山药,香辣蟹,墨鱼胶油条,火焰虾,小吊梨汤,流沙包,烤鱼,香辣烤鱼,酱烧茄子,外婆红烧肉,鹅肝,骨头王锅底,蛋挞,麻辣诱惑蛙,蜜汁叉烧,李家大排,饺子,青年拌菜,港丽炒饭,糖醋排骨,天王烤鸭包,骨头砂锅,水晶虾仁,龙虾,吊龙,片皮鸭,咖喱蟹,盐水鸭肝,烤羊腿,香辣虾,大拌菜,鸳鸯鱼头王,越式牛肉粒,叉烧,羊蝎子,纸包鸡翅,农家小炒肉,鲍鱼,雪花牛肉,羊肉,宫爆鸡丁,烤羊排,奶油猪仔包,烤肉,麻酱油麦菜,红菜汤,冬阴功汤,茴香豆,果木烤鸭,糖醋里脊,豉汁凤爪,清蒸鲥鱼,罐焖牛肉,炸酱面,蟹粉豆腐,烤生蚝,牛肉,蛋黄鸡翅,羊排,响油鳝糊,菠萝包,妈妈大拉皮,油爆虾,秘制串烧虾,桂花山药,黄花鱼水饺,葫芦鸡,麻辣兔头,麻辣牛肉,烤鸭包,肥牛,干炸丸子,马德里薄切生牛肉,菠萝油条虾,烤羊肉,烤鸡翅,红烧乳鸽,水晶虾饺皇,蒸凤爪,川北凉粉,澳门胡椒猪骨煲,蜜汁梅肉,干酪鱼,酸汤鱼,砂锅白肉,肉夹馍,担担面,法式蜗牛汤,鲅鱼水饺,猫耳朵,东北大拉皮,霸王蛙,乾隆白菜,宫保虾球,汽锅鸡,烧饼,小龙虾,清炒河虾仁,莜面,民国美龄粥,千层肚,鲜鸭血,鱼头诱惑,黑米八宝粥,佛跳墙,生煎包,金牌烤蛏,面筋,蜂蜜厚多士配雪糕,窑烤蜜汁梅肉,九转大肠,石锅沸腾饭,火锅,牛仔骨,奶香手撕饼,猪肚鸡,蒜香面包,虾仁馅饼,五花肉,炖汤,刺身,大饼卷牛肉,鸭肠,石锅蛙,沙姜鸡,豉油皇鹅肠,鹅肠,牛肉丸,爆炒腰花,十三香龙虾,素鲍鱼,梨球果仁虾,文昌鸡,老妈蹄花,鲅鱼饺子,鱼蛋粉,贝勒烤肉,小笼,花家白菜,生煎,蟹粉小笼,蒜香排骨,闷骚南瓜,嫩牛肉,宋嫂鱼羹,霸王鸡,牛蛙,五香兔头,酥皮鱼翅汤,黑森林蛋糕,辣炒蛤蜊,黑椒牛仔骨,牛眼粒,海瓜子,东坡肘子,八珍豆腐,豌豆黄,鱼香肉丝,世宁土豆泥,鸡丝凉面,葱烧海参,叫化鸡,虾茸草菇白菜,酥皮虾,八味豆腐,花雕鸡,西杏炸虾卷,咖喱皇炒虾,烤牛舌,酱排骨,老爆三,青豆泥,猪颈肉,上酱肥牛配土豆泥,奶黄包,干炸小丸子,樟茶鸭,辣子鸡,手切羊肉,基围虾,红杏鸡,八爷烤鸭,梨汤,油焖大虾,红米肠,小笼包,火燎鸭心,蒜香河虾,虾仁鸡翅,台塑牛排,酱骨架,海鲜粥,凉面,笔筒色拉,生鱼片,葱烤大排,鸭血粉丝汤,春天虾饺皇,螃蟹,局气豆腐,羊肉泡馍,香茅草烤罗非鱼,点心,罾蹦鲤鱼,水晶鸡,妈妈熏排骨,瑞士牛排,小鸡炖蘑菇,肉蟹煲,椒麻鸡,榴莲披萨,椰子鸡,豆豉清江鱼,炒疙瘩,烤乳鸽,蛋黄子排,飞鱼籽蒸蛋,小炒肉,京酱肉丝,自制豆腐,夸张的新疆大盘鸡,香辣美容蹄,醋溜木须,锅贴,沸腾鱼,手打牛肉丸,黄馍馍,拿破仑,抹茶慕斯,香酥鸭,奶油蘑菇汤,咖喱皇大虾配法式面包,麻辣香锅,小馄饨,碳烧猪颈肉,锡纸鲈鱼,白斩鸡,酸辣土豆丝,羲和烤鸭,鹅肝包,金针菇,八色小笼,麻辣烫,文东海南鸡,玉米烙,牛肉面,江石滚肥牛,铜锣烧,老奶洋芋,餐前面包,想吃土豆,越式牛柳粒,牛肚,长安葫芦鸡,津味油条,老妈红烧肉,鸭血粉丝煲,卤肉饭,小豆凉糕,炸糕,鱼生,玉兰饼,咖喱虾,桃仁剔炒鸡,梭子蟹炒年糕,葱油饼,清江鱼,鸡爪,凤梨虾球,人间豆腐,牛羊杂汤,鱼头,地三鲜,深井烧鹅,酱骨头,烤串,烤鳗鱼,窑烤大羊肉串,奶油烤蘑菇,拉面,黄喉,奶香青豆泥,炸灌肠,十三香小龙虾,咸水鸭肝,蕨根粉,刀削面,小米椒爱上小公鸡,湘西霸王蛙,菠萝炒饭,铁板鲈鱼,叉烧酥,葱油鸡,秘制牛肉,宫爆虾球,酸辣粉,砂锅豆腐,湄公鱼,凉拌素三样,清蒸鲈鱼,榴莲薄饼,西湖雪媚娘,猪手,掌中宝,蜜辣烤翅,嫩肉,红烧牛肉面,雪梅娘,金牌烤鸭,胡桃里烤鸡,炸腐皮,老醋蛰头,杏仁豆腐,疙瘩汤,蒜蓉粉丝蒸扇贝,薯条,牛肉粒,椒盐九肚鱼,金牌烧鹅,烧烤,熏鲳鱼,娜帕里勇,凉皮,珊瑚虾,烧排骨,冬阴功海鲜清汤,酱鸭头,黑三剁,铁勺凤爪,鸡蛋仔,食神咖喱牛腩,汉家果木烤鸭,一桶豆腐,香芒大虾沙律,金牌虾饺皇,全聚德烤鸭,咖喱皇大虾,它似蜜,香肴肉,帝王蟹,糯米糖藕,鸡丝凉米线,奇妙虾球,糖油粑粑,手抓羊肉,酥皮奶油蘑菇汤,杭州熏鱼,甜蜜蜜,芝士蛋糕,脑花,黄油烤红薯,炸豆皮,番茄锅底,油条,金网脆皮虾肠粉,卤煮火烧,包子,你没吃过我的豆腐,蟹黄饺子,寿司,铁板茄子,烤羊肉串,秘制嫩牛肉,雪媚娘,桂花糖芋苗,小炒黄牛肉,珍宝辣椒蟹,陈皮骨,干锅茶树菇,葱焖虾,三鲜包,有机花菜,蟹粉蛋,口水鲶鱼,印度飞饼,爆肚,沙田乳鸽,虾仁,烤牛肉,销魂钢管鸡,干烧鳝段,串串虾,咖喱鱼蛋,局气酥皮虾,香锅牛蛙,芒果粒三文鱼,羊汤,烤翅,咖喱面包鸡,金枪鱼沙拉,樱桃鹅肝,至尊系列pizza,鸳鸯锅,糯米排骨,烤银鳕鱼,金沙红米腸,干煸鱿鱼须,民间烤枣馍,鸭四宝,臭桂鱼,龙虾泡饭,红油抄手,黄豆猪脚汤,海南鸡饭,熏排骨,虾肉小笼,西多士,碧玉妆成,榴莲比萨,猪油捞饭,法式香煎鹅肝沙拉,卤鸡爪,椒盐鸭架,干锅土豆片,麻豆腐,锅边馍,粉蒸肉,黑椒牛仔粒,鲜虾云吞面,重庆小面,千叶豆腐,无锡排骨,排骨,黑椒土豆泥,双椒鱼头,甘薯烤盘披萨,莜面鱼鱼,老醋茼蒿,鲜毛肚,披萨,豆汤豌豆苗,局气炒饭,过油肉,椒盐龙头烤,三凤桥排骨,拾味骨汤,干煎带鱼,pizza,酒香草头,烧味双拼,烧鸡,现制熏鲳鱼,酱鸭,海鲜超市,猪仔包,六秒涮鱼片组合,巴蜀牛肉,鹿儿岛猪软骨,香肠,脆嫩毛肚,酥皮蘑菇汤,鲍汁蔬菜豆腐,炸馒头片,豆豉烤鱼,豆腐脑花,豆皮,盐焗乳鸽,牛扒,水爆肚,馄饨,藕片,春饼,西贝面筋,密制串烧虾,九转肥肠,猪蹄,巧拌豆苗,麻辣烤鱼,大煮干丝,猪肉包,心太软,红豆粥,三色豆泥,泰皇炒饭,武大郎烧饼,鸭血粉丝,松鼠鱼,酱油白米虾,芥末虾球,豆皮刷牛肚,蒸虾,糖醋鲤鱼,鱼丸,梅干菜烧肉,盐烤大虾,葱油拌面,干贝排骨粥,芝士焗红薯,窑烤面包,冬荫功汤,一桶虾,酥肉,金牌红烧肉,烤扇贝,汤丝螺,土豆沙拉,白糖糕,烟熏三文鱼,招牌肉蟹煲,外婆神仙鸡,允指鸡爪,大虾,冬阴功虾汤,客家酿豆腐,石烤虾,蚝仔烙,牛油锅底,烤金针菇,番茄烤翅,鞋底饼,越南春卷,西施玉米,烤玉米,百岁奶豆腐,过桥米线,帝王蟹脚,传香兔腿,酸菜鲈鱼,麦片虾,酱羊骨,果木泥烤叫花鸡,风味辣子猪手,咸蛋黄焗南瓜,盐水鸭,银鱼莼菜汤,捞面,斑鱼,蟹脚,芒果大虾沙律,菩提香袋,臭鳜鱼,醋椒豆腐,蜂窝煤,老满饺子,蒜蓉扇贝,金牌蒜香骨,爆酱榴莲包,南瓜粥,酱骨,牛筋丸,焦溜丸子,招牌盐水鸭,香草烤三黄鸡,菌菇豆腐,涮羊肉,碳烤猪颈肉,鸭舌,虾生,蒜泥白肉,猪肉三鲜饺子,牛气冲天,酒酿圆子,松露野菌饺,糟溜鱼片,北极贝,斑鱼片,麻辣豆腐,芳香排骨,烤包子,兔爷土豆泥,烤茄子,宫保鳕鱼粒,麻球,新疆大盘鸡,北京味的鸭馅饼,酱棒骨,大麻球,椰子饭,兰州酿皮,烤脑花,拉皮,豆花,泉水鸡,顺风虾饺皇,乌鸡卷,酥皮焗牛肝菌春鸡,清汤狮子头,瓦罐汤,妈妈蛋蒸肉,椒盐鸭舌,非一般鲈鱼,漂亮的虾球,潮汕炸腐皮,费尼经典汉堡,清蒸多宝鱼,排骨藕汤,烤馒头,Uncle烤鸡翅,元宝虾,羊肉汤,上汤菠菜,梭边鱼,鸡脆骨,烤培根卷牛里脊,宽粉,虾球,杀猪菜,石锅豆腐,胡椒猪肚鸡,水晶虾饺,酱油炒饭,排骨年糕,鸡鸣汤包,妈妈熏骨,老潮州肉骨茶,德州扒鸡,意大利面,美龄粥,开门红,鱼头汤,鹿肉,金牌水煮鱼,酱脊骨,冰粉,招牌鲈鱼,炸鸡腿,小桥鱼面,全鱼火锅,椒盐排骨,老上海葱油鸡,冻深海蟹钳,原味汽锅鸡,老坛剁椒鱼头,蟹肉意面,培根,酒酿赤豆小元宵,九尺鹅肠,烤羊棒,蛋糕,羊杂汤,化皮烧肉,绿茶桂花糕,有机沙拉吧,芝士焗番薯,干煸四季豆,功德冬菜包,汤包,飘香排骨,素颜烤鱼,蚵仔煎,极品肥牛,拔丝地瓜,果木烤安格斯牛扒,爸爸炒饭,蜜汁鸡翅,酱皇蒸凤爪,咖哩虾,炝锅鱼,酥不腻烤鸭,铁板蛏子,酸辣蕨根粉,土豆丝,热干面,老火汤,六秒涮墨鱼片,豆腐皮,泼辣鱼,鸡油饭,肉沫烧饼,菌王奇香锅,蓝莓土豆泥,高炉烧饼,盐焗鸡,冷面,泰式虾饼,沙拉吧,辣得跳,红烧牛尾,水煮牛肉,肉骨茶,西府一口香,桃花虾,口味牛蛙,花螺,乌鱼蛋汤,涮牛肚,金汤煨凤爪,小锅米线,毛氏红烧肉,极品鹅肠,椒盐猪手,泰国冬阴功汤,蜜汁排骨串,双色鱼头,黄咖喱鸡,西贝大拌菜,响油鳝丝,元贝粥底,大骨煨萝卜,招牌乳鸽,松露酱帕尔马火腿披萨,东山羊,老坑猪手,油焖笋,秘制汁酥皮虾,猪肉三鲜,烤五花肉,干贝虾蟹粥,手撕豆腐,酥香嫩烤鸭,火焰玻璃片,波士顿龙虾,膏蟹粥,黑椒小牛排,烤薯皮,紅米腸,传统北京果木烤鸭,糙米饭,干炸带鱼,骨肉相连,濑尿虾,大拉皮,墨鱼饼,小土豆,鲍鱼牛腩金汤面,窑烤秘制翅中,回锅肉,火焰土豆泥,肘子,蟹黄豆花,芝麻火烧,铁锅靠大鹅,三鲜豆皮,活体豆苗,晾干白肉,外婆花菜,街头拇指水煎包,密制腐乳鸭,大棒骨,麻麻鱼,皮皮虾,靓靓蒸虾,闫府第一福,老上海熏鱼,椒盐虾虎,经典BBQ牛肉汉堡,雪花肥牛,牛舌,老式锅包肉,蒜蓉蒸扇贝,干锅有机花菜,湛江鸡,三鲜饺子,椒盐茶树菇,来自星星的炸鸡,烧味,烤兔腿,一品赛蟹黄,黑椒墨玉,八旗茄子,青咖喱,莲藕排骨汤,烤猪蹄,京城的茄子,耙泥鳅,纠结的大饼,凉米线,蒜茸扇贝,白切鸡,冰窖凉浸带鱼,黄瓜沾酱,百合酱凤爪,现炸酥肉,酸菜鱼锅,眼睛螺炒四角豆,美极鲜虾滑,比萨,白脱小球,煲汤,石锅鱼,赤豆元宵,酸菜,花生酱双层牛肉煲,鸡丝荞面,大漠烤羊排,鹅掌粉丝煲,黑胡椒牛肉粒,泡饼,十三幺,烩面,西红柿炒鸡蛋,酸汤乌江鱼,随缘解馋,棉花糖拉利玛科罗娜,风味茄条,担仔面,酸辣笋尖,烤麸,花厨水,金牌麻婆豆腐,鸭头,越南手卷,鸭下巴,面包,活虾,xo酱萝卜糕,馋嘴江团,龙虾仔,五花趾,糖粥,九格甜品,肉饼,金包咖喱鸡,鸡蓉蘑菇汤,鹌鹑蛋,金针菇牛肉卷,盗汗鸡,斗牛士披萨,招牌酱爆豆角,山水豆腐,经典考伯色拉,鳕鱼,杨府招牌鱼,石锅海胆豆腐,糟熘鱼片,芫爆散丹,同庆小笼包,蒜香龙虾,风味茄子,酱爆洋白菜,十里香牛肉,盐酥鸡,宫保鸡丁披萨,炒花蛤,松子烤鸭烧卖,烤鸭三吃,天鹅叉烧酥,极品毛血旺,范家私房烤鸭,孜然羊肉,厦门炸醋肉,葡挞,砂锅莜面鱼鱼,龙脂猪血,烤虾,冷面鸡,海鲜粉丝沙拉,中8牌玖蘑菇,台湾虾面,酱爆鸡丁,青花椒鱼,老鸭煲,菌汤,老妈牛肉,姐夫小炒肉,鳝鱼粉丝,口味虾,酱爆猪肝,本家盆盆菜,鱼翅鲍鱼羹,蒜蓉粉底虾,烤鲈鱼,脆皮烧乳鸽,羊肉卷,山药龙骨汤,吊炉烧饼,糖醋面筋,香辣猪蹄,葱油膏蟹,灌汤虾球,油炸臭豆腐,千叶豆腐煲,绿茶烤鱼,椒盐濑尿虾,杭椒牛柳,西域囊包肉,上上签,牛肉拉面,药膳,牛肚王,麻香排骨,蛏子,干烧鸭四宝,笃笃笃糖粥,黑椒牛柳,荷叶酱肉,油旋,蟹黄包,金酱蒸凤爪,烤鸭套餐,肉燥饭,蒜茸粉丝虾,兔头,鸭油包,西施泡飯,传统猪肉包,铁锅羊肉,水笋,虾饼,蜜汁排骨,清蒸白鱼,海鲜大咖,班尼迪克蛋,石烹牛柳,鸡丝米线,雪山包,斗碗鱼,甜汁爆鲜虾,石锅鲍鱼,招牌虾饺皇,烤了这只BB鸭,净云吞,豆花烤鱼,紫砂红烧肉,秘制鲈鱼,炒合菜,泡椒牛蛙,老北京煨牛肉,炸馒头,密制小肘,萝卜糕,虎皮凤爪,劲爆牛肉丸,怡煲鹅掌粉丝煲,海鲜泡饭,三鲜水饺,肉筋,眉州香肠,红葱头鸡,雪豆蹄花,土鸡汤,紫阳蒸盆子,培根土豆皮,爽口海草,三角肉,元贝,炭烤猪颈肉,金牌烤茄子,菠萝龟包包,干锅三素,山楂鹅肝,豆腐脑,鱿鱼酱油水,招牌红烧肉,冬阴功锅底,红烧舟山带鱼,云杉泰皇豆腐,麻酱小料,瓦罐凤爪,石烹雪花牛肉,番茄鱼,多宝鱼,筋饼,蟹肉炖饭,孜然羊肉串,大闸蟹,蚝油牛肉,意面,黑椒牛排,石锅有机花菜,手撕鸡,椒盐蘑菇,海南鸡,猪肚鸡锅底,土豆片,干锅手撕包菜,安妮披萨,烧鸭,冬阴功鱼片,酸汤龙利鱼,酱牛肉,玫瑰松阪肉,南乳花蛤,石锅拌饭,麻辣扇贝,豆腐,烧卖,鹅掌,花旗参汤虾饺皇,外婆烤肉,上楼虾饺皇,芝士虾球,招牌有滋有味鱼,黄咖喱牛腩,辣椒炒肉,西域风情,王府泡椒鸡,铁板牛仔骨,顺风乳鸽,弯弯鸡,禾风熏牛肉,炸气饽饽,铁板鳕鱼,萝卜丸子,桂花糖藕,油豆皮,烟熏鲳鱼,脆皮蹄髈,渝利鸳鸯锅底,照烧三文鱼配椰香米饭,水煮小鱿鱼,脖仁,蜜桃烤肉,莲白粉丝,铁板牛肉,碳烧猪排,汤锅,东坡童子鸡,吊烧鸡,梅汁翅中,鲍鱼捞饭,金丝虾球,烙饼卷带鱼,冰脆牛黄喉,猪油拌饭,臊子面,上酱肥牛土豆泥,龙利鱼,三峡石爆脆肠,陕西凉皮,那年秋天的茄子,例汤,香辣美容猪蹄,松茸汤底,云吞面,原味土猪汤,鲜虾芹菜丸,台式三杯鸡,赤豆羹,海鲜疙瘩汤,大赤包捉五魁,大明虾,青笋,气饽饽,鸡汁三丝羹,大酥牛肉,独面筋,蝦餃,石锅凤爪,老鸭汤,建水豆腐,串串香,御品和牛眼肉,糖蒜,墨西哥肉卷,黄桥烧饼,范家义气羊肉串,重庆豆花烤鱼,炝莲白,蒜蓉粉丝虾,红柳羊肉串,牛油红锅,山楂老酒烧肉,米豆腐,无骨鸭舌,奇味虾,火焰黑椒牛肉粒,咖喱大虾,金钱蛋,干锅田鸡,妈妈香熏骨,椒盐大虾,凉粉,一口炸豆腐,葱花饼,响铃芝士什菜虾卷,麻辣鲜鱼片,干锅花菜,鱼豆花,黄河大鲤鱼,毛蟹炒年糕,碳烤鱼,水煮肉片,兔腰,黑胡椒蟹,奇石咕噜鱼,年糕,台湾香肠,绿茶烤鸡,免费凉菜,骨头锅底,魔芋排骨,牛杂,牛百叶,千丝牛杂,关中一品素,红烧猪脚,一指禅,纸牌屋肋排,芝麻酱糖饼,面包片,荔枝柴烧鸡,炸豆腐,香港一品粥,蘑菇汤,烤筋,爆三样,招牌素三样,柠檬鱼,外婆鱼,董氏宫保虾,彩虹排骨,小米粥,鸡汤,老成都酸菜鱼,液氮青椒螺片,鸡毛菜,抓虾,糖醋丸子,火山石烧裙翅,姑妈焖猪手,鲜虾,手撕牛肉,素鸭,空运绿色毛肚,干煸豆角,肘子酥,油泼面,香芒三文鱼沙拉,自制鱼丸,竹荪,三黄鸡,孔门豆腐,德天肥牛,鲍汁豆腐,片儿川,巧克力慕斯,牛奶鸡蛋醪糟,熔岩流沙包,精品毛血旺,皮皮虾饺子,马桥豆腐,烤全羊,大刀拐子肉,芝士野菌焗豆腐,美蛙,金饼地锅鸡,俄罗斯鸡肉大串,芝士年糕,酸菜鱼锅底,清宫烤羊腿,辣炒蛏子,千岛湖鱼头,黄鱼领鲜面,三杯小土豆,麻香鸡,张氏狮子头,极品鲜毛肚,烤奶油杂拌,招牌黄金虾卷,竹蛏,红颜知己,梨香带子,牛仔骨炒年糕,牛油果鸡肉沙拉,酱大骨,叉烧包,小榄农家豆腐,招牌生煎,金牌虾饺王,客家豆腐,石锅老豆腐,和味烤鱿鱼,怪味烤鱼,大蒜牛扒,泰汁凤爪,岐山臊子面,鸡肉串,芝士焗虾,热气羊肉,雷公鸭,道口烧鸡,砂锅粥,会跳舞的茄子,咖喱酱烧虾,红白,香茅草烤鱼,涮毛肚,肉串,金蒜爱上虾,凯撒沙拉,黄鱼馄饨,香兰捞饭,五彩大拉皮,铁板土豆片,葱油海瓜子,西贡咖喱鸡,船夫海螺,退秋鱼,手撕包心菜,蒜蓉小龙虾,金钱虾饼,虾锅,笋尖虾饺皇,鸡仔饼,啤酒龙虾,辣炒花蛤,桂花红烧肉,小酥肉,北京亮沙拉,海参斑剁椒蒸,芒果土豆泥,原生态豆腐,砂锅自磨豆腐,酸菜炒汤圆,高钙羊肉,羊肉片,一鱼两吃,一筐蛤蜊,蒜蓉虾,皇上皇腊味煲仔饭,小煎猪肝,太湖一锅鲜,三鲜炒疙瘩,青芥古法大虾球,大羊肉串,招牌雪山包,馕包肉,一心鸡,香辣牛蛙,普洱香锅鸡,两桶双味虾,白灼章鱼,怪味清江鱼,三花趾,顺德猪杂粥,皇堡,秘制腐乳鸭,麻辣鱼,爆浆甜筒沙拉,扁豆焖面,宝翠虾饺皇,香辣小龙虾,三杯鸡中翅,擂椒炒海参,淮王鱼,招牌辣翅,牛腩粉,品海小巴蛸,堂蒸丹东肥蚬子,排骨串,梁山鸡,真爱排骨,奶香凤尾虾,蜂窝玉米,奇香排骨,糯米藕,拉条子,龙虾片,招牌饺子,香辣土豆丝,砂锅鱼,黄豆煨猪脚,海参捞饭,小确幸全家福,小杨生煎,石锅牛蛙,牛肉汤河粉,花旗參湯四色蝦餃,金沙红米肠,厚多士,粤式牛仔粒,排骨鱿鱼锅,蒜香骨,马来三味鱼,鸡丝冷面,豆瓣鱼,培根卷,上和酱鸭,金沙焗猪手,葱烧武昌鱼,鳝段粉丝,马来炒面,蟹籽虾滑,手抓羊排,飘香榴莲酥,淮安软兜,鸡生蛋蛋生鸡,客语豆腐,蟹棒,金牌脆皮虾,番茄锅,顺风臭豆腐,咖喱鸡,大包子,自家豆腐煲,文昌海南鸡,三十年代熏鱼,戛纳遇上北海道,红烧带鱼,罐罐米线,鸭嘴鱼,鱼豆腐,云吞,塞外皇坛子,熏肉大饼,炭烤三文鱼,丝娃娃,糍粑,小米糕,香烤鲈鱼,素面,猪脑,牛肉烩饭,黄鱼煨面,自酿豆腐煲,五方肉,铁勺爆肚,潮福虾饺皇,捣蛋的鸡翅,一锅出,秦镇米皮,毛豆腐,麻仁芋头,海底捞,海参,意大利肉酱面,四川凉面,酒香花雕鸡,湛江香油鸡,山药炖排骨汤,云杉抓虾,客家黑粉皮,擂辣椒皮蛋,金刚火方,秘制酒香肉,丸滑组合,脆皮鸡,湿炒牛河,红花汁栗子白菜,北京果木烤鸭,雪菜黄鱼煨面,鱼香茄龙,老醋花生,鲍汁凤爪,干煸藕丝,河内熟牛肉汤河粉,干锅包菜,四喜烤麸,骄子肥牛,酱龙骨,蟹黄豆腐煲,芝麻酱,文鱼,纽奥良鸡肉沙拉,奶油烤杂拌,炒面片,三鲜烧卖,生牛肉丸,酱小土豆,脆皮牛肉,上将肥牛土豆泥,花厨沙拉,红果水晶汤,芋儿,牛肉馅饼,生蚝鲍鱼,匙柄,清蒸带鱼,茶壶汤,现烙脆皮饼,沙拉小牛肉,美腿馋嘴蛙,铁板海鲜豆腐,四喜丸子,京城四味烤鸭,老火靓汤,锅中锅,招牌一品豆腐,顺德公炒陈村粉,木瓜番茄汤,孔乙己花生,风花雪月鱼,咸亨臭豆腐,土炉烧饼,麻辣排骨串,酱香芝士猪蹄,姊妹团子,干锅臭桂鱼,鱼片,招牌石锅沸腾饭,口味鱼头,煎饺,豆瓣钳鱼,水煮活鱼,神仙鸡,金牌烧鹅酥,黑蒜子牛肉粒,八斗鸡,琵琶鸭,樱桃肉,苔条小黄鱼,呛香鱼,酿皮,胡椒虾,小米渣,招牌沙拉,钟水饺,水煮三鲜,火锅鸡,金牌香肴肉,鸡包翅,沸腾虾,三鲜伊府面,青梅虾,牛肉粉丝汤,品海小蚆蛸,火焰牛仔腿,烤猪颈肉,金不换三杯鸡,海鲜混搭,鲜虾肠粉,虾皇饺,粢饭糕,沙拉小牛排,虾酱空心菜,鱼头煲,烧羊棒,酒酿小圆子,烤鱿鱼,口水鱼,辣子对虾,鸟巢酥方,三味鱼,手切鲜羊肉,老长沙臭豆腐,番茄牛肉,自磨豆腐,羊肉烩面,青芥三文鱼挞,十三幺小,客家咸鸡,麻辣龙虾,汉堡,碳烤生蚝,山茶花开,焖饼,美极焗铁棍山药,明珠鸡,宫爆大虾球,小馆半条鱼,饷油鳝糊,老实人鱼头,榄菜肉碎四季豆,薄荷牛肉卷,草莓芝士蛋糕,黑松露低温蛋,竹串虾,黄米烙,醋溜苜蓿,轰炸东京,虾蟹粥,十三香,泡椒田鸡,霸道毛血旺,我爱鱼头,清心鸡,卤水鹅肝,阿根廷大虾,黑胡椒牛柳意面,金牌虾饺,冬阴功海鲜汤,至尊冰镇龙虾,十八罗汉,秘制小牛排,豉油鸡,摇滚大拉皮,三鲜老油条,南洋凤梨饭,莫斯科红菜汤,肉馕,经典牛排,黄金嵌白玉,老上海红烧肉,剁椒鸳鸯鱼头王,鸦片鱼头,珍宝蜂蜜厚多士,气锅鸡,铁板土豆,黄焖龙虾,梅菜扣肉,红糖麻糍,脆皮烧鸭,干煎杏鲍菇,凉拌茼蒿,妈妈私房红烧肉,麻辣鲶鱼,红烧羊肉,日本黑猪肉,老陕烩菜,河内牛肉米粉,八宝辣酱,桂花大排,鲜青椒水煮巴沙鱼,一米羊肉,奥巴马牛肉汤,北京烤鸭披萨,安格斯牛柳粒,象鼻蚌,墨西哥牛肉脆饼,烤鸡爪,烤肉筋,冰激凌土豆泥,海鲜菠萝炒饭,干锅土豆,壹心鸡,猪脚箍,拾味鱼扒,特色酸菜鱼,黄焖小龙虾,毛豆,凉菜,咖喱锅巴虾球,黄金流沙包,大腰子,黑椒牛柳粒,芝心丸,山药鸡里蹦,呛炒圆白菜,梨球酥皮虾,带子,青咖喱鸡,巧克力蛋糕,烧味三拼,干锅千叶豆腐,汗炉蒸鸭,笑颜神仙鸡,长脚蟹,桑拿蚝,同庆楼煮干丝,土豆饭,肉皮汤,炒空心菜,功夫土豆片,小龙坎毛肚,招牌牛排,火车头,香辣蹄花,鲜花虾滑,猪肉汤,津味素,黑豆腐,冰镇菠萝油,油淋鲈鱼,蟹柳,至尊鸳鸯锅底,手抓龙虾,千层毛肚,椒盐蹄膀,咖喱皇芝士炒饭,70土汉堡,鲜的跳,醉排骨,四川抄手拌面,朗姆火焰松子鱼,客语土猪汤,法国吉拉多生蚝,芝士牛肉汉堡,鲜虾滑,牛油鸳鸯锅,牛肝菌,鲜羊蝎子,芝士红薯,鸭五件,石锅三角峰,狮麟三蒸,鲶鱼,开心糕,黄金虾卷,簋街麻辣小龙虾,罐焖鹿肉,富贵虾,荔枝肉,叫花鸡,火盆豆腐,骨棒,滇味凉米线,阿婆红烧肉,三杯鸡鲍鱼菇,水上漂嫩牛肉,烤千页豆腐,烤风干肠,黑松露鹅肝包,蟹膏砂锅粥,三味珍宝虾饺,奶油杂拌,烤大虾,河豚,潮汕手打鲜牛肉丸,传统锅包肉,香酥鸡,瓦罐牛排,酸萝卜老鸭汤,火焰牛排,马贝,冒菜,豆豉排骨,招牌锡香鲈鱼,手擀面,卤骨头,精品五花肉,鲍汁蒸凤爪,顶级霜降肥牛,印象红盖,招牌菠萝海鲜炒饭,双色土豆泥,嫩肉片,老妈羊肉,BB鸭,干锅手撕鸡,荷塘小炒,广式蒸多宝鱼,雾凇脆皮豆腐,香辣清江鱼,蒸饺,牛肉千层饼,椒盐虾菇,丰收日红烧肉,玉米饼,招牌汉堡,老坛子,公社拌菜,鸳鸯火锅,田螺塞肉,金丝小枣煨肘子,鸡丝拉皮,醋溜白菜,越式芒果鲜虾沙律,洋葱塔,肉丝拉皮,秘制花甲,脆皮黑叉烧包,一品大排,水饺,金牌蒜蓉明虾,辣炒青岛小花蛤,洛神金沙红米肠,德国式烤猪肘,狗不理包子,长沙臭豆腐,牛柚果油条虾,土笋冻,老坑小炒皇,黄鱼面,草帽饼,菜泡饭,歌乐山辣子鸡,豆汤鱼的爱情,象拔蚌,手撕饼,烤牛排,蒜蓉生蚝,极品牛肉粒,胡桃里烧鸡,骨头汤,沙湾大盘鸡,南煎丸子,辣宴老坛酸菜鱼,锦城嫩牛肉,老板的羊肉串,家常菜,老谭酸菜鱼,考伯沙拉,烤卤牛蛙,锣鼓烤鸭,脆皮猪手,新奥尔良鸡翅,铜锅清江鱼白汤,牛肉卷,麻辣水煮鱼,包浆豆腐,草头饼,炒肝,钢管鸡,番茄火锅,冬荫功海鲜锅,虾尾,招牌冷面鸡,特味肥牛,砂锅番茄牛肉,泰式咖喱皇炒蟹,牛河,凉拌毛豆,黄米凉糕,蛋黄流沙包,海鲜面疙瘩,吊龙伴,冬阴功鲜虾汤,油焖春笋,香茅草烤鲈鱼,黄蘑扣肉,腊八蒜溜肝尖,鸡汁大煮干丝,阿拉斯加帝王蟹,红酒鹅肝果,一桶金钱手,香嫩小牛肉,黑猪肉,文火焖小牛肉,小团圆炒饭,阳春面,墨鱼汤包,奶油炸糕,平桥豆腐,探鱼冰粉,九福特色鸡,灌肠,蟹黄汤包,招牌臭鳜鱼,三杯焗小土豆,唐宫虾饺皇,水煮酸菜牛肉,面片,老豆腐,烤馒头片,红汤梭边鱼,养生黑豆腐,匙仁,牛杂汤,烤肥牛,烤龙虾,鲜肉汤包,蟹黄石榴鸡,枣沫糊,三鲜芸豆,土馒头夹臭豆腐,嫩炒鸡蛋,金牌酱骨架,秘制海鲜酱油,经典鸳鸯锅底,丑鱼,香芒美人鱼,蹄花焖藕,私家酸汤鱼锅,面茶,烤土豆皮,麻酱拉皮,明虾煲,虾仁水饺,新疆香囊,猪肚包鸡,孜然寸骨,老婆饼,凉衣白肉,盐帮干锅蛙,清蒸桂鱼,三文鱼北极贝,清蒸鱼,椒麻鱼,滚石牛肉,糖饺子,酸汤雪花鱼,陶陶居大虾饺,农家手工麻糍,天妇罗,花甲,酱腔骨,江米扣肉,海蟹,茶坊酿皮子,葱饼卷牛肉,肥肠火锅,南瓜羹,锅边馍馍,凤凰流沙包,mini猪蹄,俄罗斯大串系列,沙律牛排,墨鱼汁海鲜意饭,酱油小排,麻酱糖饼,焗红薯,招牌牛骨汤锅,香煎丝丁鱼,虾的诱惑,榴莲蛋糕,宫保水晶虾球,滋补烩面,鸭架汤,招牌米有沙拉,糯香排骨,木瓜炖雪蛤,陕西回族麻酱凉皮,家乡手撕豆腐煲,大汤黄鱼,小猪猪组合,香辣肉丝,手抓饭,泡馍,墨西哥卷,牛滑,蜜汁糖藕,地锅鸡,鱿鱼圈,特色海蛰,手锤茄子卷饼,金沙鲜虾红米肠,虾蚝仔粥,唐府宫保大虾球,烧椒鲈鱼,莎莎排骨,酸菜鴨嘴魚,烤鸡翅中,花江狗肉,秘制龙虾,东北乱炖,油焖虾,椰香爆虾球,太安鱼,莲蓉月饼,红烧鱼唇,吊锅饭,猪肉大葱烧卖,山里红马蹄糕,青柠怪味虾,金线油塔,极品牛肉,白贝,山寨鱼头王,久香鸡翅,蟹黄桂花鱼,甜胚子,水煮小鱿鱼加虾,三色山药泥,老妈卤蛋红烧肉,鲈鱼炒饭,臭鱼,阿根廷红虾,香辣猪手,生吃海胆,花菜,蘑菇包,核桃包,粉丝蒸扇贝,油浸鱼,石锅烤虾,火焰牛柳,烤乳猪,香酥鸭色拉,招牌煎鸡胸肉沙拉,玉米,蒸南瓜,有机菜花,日式青芥三文鱼挞,乐汇鱼头,梅汁排骨,国宴油条,香酥小白龙,松鼠鲈鱼,天目湖鱼头汤,薯片三文鱼,烧饼夹肉,芥茉鸭掌,黄桂粥,臭鲑鱼,原汁牛腩饭,西域凉皮,铁板蒜茸虾,漂亮虾球,李师傅脆肚,老街鱼嘴巴,大酱骨,百叶结红烧肉,菜饭,陈村粉,五香牛肉,手抓饼,红柳木牛肉串,中华炖式牛排,台湾卤肉饭,太雕鸡,秘制梅肉,菲力牛扒,东坡香肠,沙姜猪手,烤猪膝,河虾仁,荷花酥,白吉馍,芝士焗豆腐,状元糕,虾兵蟹将,泉水牛蛙,烤面包,马家沟芹菜,灰豆子,薄烧肥牛,锅魁盐煎肉,蛋黄南瓜,海底总动员,传统奶油芝士焗杂拌,小盆栽,招牌牛肉锅汤底,酸辣笋尖粉,蕉叶烤鱼,扒猪脸,官府豆腐,酸辣鸡杂,铁板虾,烤鲫鱼,德式燻肉腸套餐,顶级黑毛和牛,虾爆鳝面,威哥虾饺皇,花卷,京八件,江团,巴沙鱼片,一品海鲜粥砂锅,手工水饺,拾年私房鱼,特味三角峰,母老虎,碌鹅,明虾,梅园鸡,德国猪肘,蒸三鲜,小象拔蚌拌秋葵,香辣味,跳钢管舞的鸡,唐宫金奖乳鸽,生拌蓬蒿菜,居家凉菜,扯面,茶树菇,小肋卷,啫啫生菜煲,血鸭,牛肉炖柿子,泰式红咖喱,基围虾粥,海蛎煎蛋,西红柿牛腩,沙拉,牛油果薰鸡肉沙拉,炝拌油麦菜,惠灵顿牛排,卤鸭,李雪毛肚,阿五豆腐,大赢家,富春老鸭汤,羊尾,鱿鱼,小厨虾球,小鱼锅贴,双味鱼头,全味虾球,江南红烧肉,面皮捞牛肉,麻辣馋嘴牛蛙,珍宝蟹,咸蛋黄小龙虾,招牌炒饭,肉松蟹味菇,酿豆腐,江湖鸭血,茶树菇牛柳,脆皮妙龄鸽,保真羊肉串,鲍鱼金汤牛腩面,叉烧肠粉,串串,膏蟹海味面,特色嫩牛肉,烤湛江生蚝,双味山药,青椒水煮巴沙鱼,烤肉串,椒盐鸭下巴,咖喱鸡肉饭,至尊海鲜烩饭,芝麻汤圆,香辣脊髓,一品鸡,芋蓉豬仔包,全羊锅,炭烧双Q,三杯汁小土豆,糖醋黄鱼,三鲜锅贴,麻酱,烤培根,荷庭冷面鸡,油焖小龙虾,特色碟,腊肉煲仔饭,好味蒸凤爪,酸辣藕尖,木瓜沙拉,韭菜盒子,麻酱凉皮,宫廷红枣,黑椒肥牛石锅饭,斑鱼刺身,醋木,道味水煮鱼,沸腾清江鱼,西北面筋,油饼,青椒鱼,私家密制小肘,竹笙菠菜饺,虾丸,铁板烧,青红私房鸡,满园春饼,河渡花鲢,罐焖牛腩,古法蒸太湖白鱼,吊龙膀,鱼翅,肉夹膜,四重奏,蒜泥龙虾,探鱼春卷,全家福,马来风光,面条,扣三丝,两江麻辣鱼,极致卤鸡爪,猪扒饭,盐烤虾,炸虾仁,车仔面,白灼虾,海鲜饭,鸭肝,招牌佛跳墙,石锅银鳕鱼,手剥笋,手撕酥皮鸭,普罗旺斯焗鸡排,栗子白菜,金牌白糖糕,老糖糍粑,井冈山豆腐皮,咖喱杂菜,丰茂小串,八宝酱年糕,爆肚儿,海肠饺子,五花肉卷金针菇,椰子冻,状元肉饼,紫气东来,笋干红烧肉,乳酪山药,宽塘牛蛙,浇汁鲈鱼,冰浸鳝片,公主的素蟹粉,海鲜石锅泡饭,冬阴功海鲜,小锅米饭,土豆饼,泰和虾饺皇,可乐鸡翅,烟熏莳萝三文鱼脆饼,鲜肉小笼,泰式酸甜鸡翅,牛肉串,上海生煎包,特色羊腿汤,拍黄瓜,黄花鱼,玫瑰鲜花饼,肉眼牛排,铁板,三不沾,麻香牛排肉,井冈山老俵笋,海鲜焗饭,大董酥不腻烤鸭,扒肉条,窝边草,招牌白菜,鱼头泡面,蒸肠粉,生牛肉河粉,牛蛙砂锅,石锅芋艿,酿皮子,平遥牛肉,米纸卷,煎局海鲜鸡,炸猪排,主厨沙拉,西班牙海鲜饭,炸舌头鱼,羊腿,老太原铜火锅,成都冒菜,红柳烤肉,石蘑庐山石鸡,神仙鲶鱼,素菜王,梨球虾,椒香野生菌,金牌油条虾,干锅系列,斑鱼锅,虾酱豆角丝卷鸭饼,暹罗酸辣锅,鱼丸汤,宅门骨,绝代双椒,冰草,海鲜毛血旺,虾酱大排,腌萝卜,五彩饺子,调味牛五花,酥皮大明虾,金奖豆腐,花雕醉鸡,蒙古粉条,秘制肥牛,茶香虾,醋浇豆腐,古法鱼头王,大刀毛肚,对虾,银丝饼,手工鲜虾滑,盆盆菜,留香孜然小羊排,驴肉火烧,孔门干肉,稻香蛙,粽子,香菜牛肉,招牌叉烧,小黄鱼,响油膳糊,漠北红焖羊肉,极品白肉,老头儿油爆虾,花轿狮子头,百万大拌菜,鳕鱼拼菜卷,小品琵琶鸭,雪梅酿,窝蛋肥牛,蜀香苕粉,肉骨茶锅底,烂糊面,綠茶桂花糕,加东叻沙,冰镇龙虾,干锅虾,悦蓉干锅牛蛙,小咸菜,泰式烤猪颈肉,金牌锅贴,金针菇肥牛,金丝芥末虾球,麻食,火烧,鸡公煲,秘制鸭下巴,串烧基围虾,海鲈鱼,飞饼,小面,招牌口水鸡,芝士牛肉球,木桶肥羊,蹄花,三鲜包子,土匪猪肝,石磨脱骨凤爪,手打鲜虾滑,黄氏牛肉,马来宫保鸡,俄式拌香鸡,白云猪手,春卷,销魂糯香掌,紫苏平锅牛蛙,烧鸡公,铁板肥牛,蓉和葱椒鸡,糯米蒸排骨,水晶虾,汉水酸菜江团鱼,脆皮沙丹虾,夏威夷牛肉汉堡,鲜虾炸云吞,薯小弟,柠檬酸菜鱼,话梅仔排,黄桃锅包肉,脆骨,傣家烤罗非鱼,百姓大馄饨,一四一六烤鸭,粥底,南汇蜜桃走地鸡,条子肉,土豆,金网脆皮红米肠,两面黄,红枣骨,荔湾艇仔粥,米粑粑,八仙鸡汤馄饨,肥牛石锅拌饭,海鲜饼,本帮熏鱼,红烧肉盖饭,小龍蝦,甜虾,泰式手打虾饼,土贡梅煎,冷锅串串,丽湖扣肉,蒸海鲜,万家第一碗,南昌炒米粉,栗子红花汁白菜,老妈茄子,菠萝什果咕咾肉,腐乳通菜,肥肠,星级叉烧,鲜锅兔,虾粥,手抓猪蹄,熔岩芝心肥牛,牛肉诱惑,特色烤肉,熔岩火山蜜糖吐司,腊味饭,精品北京烤鸭,罗非鱼,烤肠,吃货们的菠萝油条,凤香蓉和,椰香脆皮鸡,虾仁炒疙瘩,虾肉汤包,招牌油焖大虾,生煎菜心,山药,冰火菠萝包,白鱼,豉汁蒸鮰鱼,一块豆腐,蒜蓉龙虾,莓心莓肺,茉莉花炒蛋,苗王一味骨,果木烤肉,黑拉皮,葱烤鸦片鱼头,招牌咖喱虾,想吃小土豆,肉龙,铁板蒜香虾,三杯虾,泡菜鱼,柠檬鸡,山丘虾饺皇,版纳傣味烤罗非鱼,70椒麻鸡,番茄牛魔王,乡村米粑,奶皇包,蒜蓉大虾,姥姥家臊子面,极品千层肚,东北拉皮,竹筒鸡,芝麻烧饼,水晶河虾仁,潘金莲咸菜,涮肚,蔬菜色拉,来自星星的提拉米苏,老肉片,杂菌饵块,招牌烧鹅,鸭肉面线,那府干酪鱼,谁动了我的豆腐,蒜蓉粉丝生蚝,麻小,洪湖泡藕尖,金牌鲜虾饺皇,巴沙鱼,韩国牛肉粒,大黄鱼,蛋黄豆腐,海鲜火锅,西冷牛排,孜然酥香羊排,腰花,鲜蔬豆腐,油淋鸡,三少酥皮虾,咖喱黄炒蟹,螺丝,黄猴,芥香虾球,腐乳空心菜,第一鸡,3元土豆丝,旺角菠萝油包,竹荪虾滑,糖饼,炭烤生蚝,藕拖,胡辣汤,极品江石滚肥牛,明湖烤鸭,馒头,金牌厚多士,骨头,稻香馋嘴蛙,赤豆糊小圆子,半只鸡,超级大麻球,白灼板蓝根,鸭溪排骨,岩烤猪蹄,摇滚拉皮,芝士南瓜红薯,油泼扇贝,三杯鸭,民间罐闷鸡,八宝辣酱年糕,功夫面,大丰收,鱼片粥,手撕风味大头菜,手撕鸡拌面,碳烤排骨,绝对鲶鱼,极品嫩牛肉,菌汤气锅鸡,膏蟹干贝粥,潮汕炸豆腐皮,冰与火之歌,葱油螺片,三鲜烧麦,燃面,咖喱酱虾,酱醋鲈鱼,麻辣青春,菠萝虾球,大花卷,野菌汤,蒸排骨,孜然牛排,五彩茄子,鸭汤,牛肉汉堡,木瓜酥,面拖大排骨,烤肘子,蜜汁烤肉,灌汤墨鱼球,传统酱碗,婺江鱼,兔腿,花家料理桂花鱼,酱椒鱼头,莲藕煲,啫啫煲,鲜蘑芝士卷,五花排骨肉,芝士焗龙虾,头菜爆螺片,秘汁鸡排,划小白菜,蓝莲茶香肉,不一样的里脊,拌羊脸,海蛎煎,意式肉酱面,锡蒙特色羊肉串,圣子皇,水煮龙利鱼,牛肉粉,刘家神仙鸡,焖面,精品羊蝎子,脆皮烧鹅,潮州煎蚝烙,粉丝鹅掌,鲍汁鹅掌,桂花糯米藕,一口酥豆腐,鲜羊肉,血肠,主厨秘制鱼头,午餐肉,毛肚锅,鹿儿软猪骨,锦城精排,金牌烧鸭,花椒肥牛,俄式传统大咧吧,肥胼,超越火锅鸡,海鲜卷,番茄牛腩,羊蝎子火锅,砂锅牛肉,田螺,奶香饼,顺溜鸡,牛油鸳鸯锅底,黄鱼春卷,酱爆香螺,麻什,地道剁椒鱼头,干锅肥肠,红糖锅盔,干锅娃娃菜,八府香鸭,白切羊肉,宫保脆皮大虾,招牌鸡,乳鸽焗饭,蒜香油馍,驴打滚,麻辣馋嘴蛙,耗儿鱼,大骨棒,干锅鸭头,炒牛河,稀饭,虾皇肠粉,小镇面包,牛肋条,烧麦,烤面包片,麻酱酿皮,龙虾汤面疙瘩,惊喜路边脆炸鸡,豆汤饭,碧螺手剥河虾仁,野菌排骨汤锅,千层饼,糊涂面,老鸭面,火焰烤酥肉,老干妈荷包蛋,鸡汤捞面线,海鲜饺子,鱼汤小刀面,象形榴莲酥,排骨馅包子,青辣椒水煮巴沙鱼,世宁流沙豆泥,青木瓜沙拉,麻辣冷面鸡,京味龙虾,老陕菜,晾衣架土豆丝,厚切牛舌,豆花鱼,大碗花菜,香油润鱼,珍珠圆子,馅饼,石磨豆腐,黑椒牛肉,葱香豉皇雪花鱼,海鲜叻沙边炉,功夫汤,阳光卷,香叶鸡翅,牛蛙煲,手打牛肉丸汤,巧克力熔岩,三色凉皮,特色花馍,来一桶,焦熘丸子,毛蟹年糕,客家煎酿豆腐,蛋煎糍粑,川霸腰花,炖吊子,香茅烤罗非鱼,招牌牛肉烧饼,糯香鸭掌,泡菜,呛腰花,秘醬三文魚,煎酿豆腐,酱油麻麻鸡,澳洲牛肋肉,羊棒骨,芝士千层豆腐,鱼香茄子煲,小鲍捞饭,竹笙椰子鸡,小城小炒,王婆大虾,酥皮菠萝油,烧鹅饭,木桶蛋挞,南昌炒粉,甜虾刺身,面包的诱惑,鸿运当头,大馇子粥,卜卜甲,必点油鸡饭,灶台鱼,松子鸭蛋黄,台湾三杯鸡,荔枝香辣,秘制烤羔羊排,菊花鱼,鸡油生炒菜心,红柳大串,盐酥九肚鱼,五颜六色,鱼香茄子,小郡肝,三鲜锅巴,秦派葫芦鸡,柠檬鸭,奶酪鱼,铜锅焖饭,苏小妹红烧肉,生烤羊肉串,烤鸭舌,京烧羊肉,抗氧化沙拉,秘制羊肉,酱油饭,榄菜肉末四季豆,开背虾,红烧萝卜,大饼,川串儿,新疆香馕,状元粥,文和友龙虾,重庆水煮鱼片,香煎山地鸡,一哥猪排饭,先吃个豆腐,咸蛋黄排条,一锅鲜,散养鸡,土盆烤肥牛,尚品青椒鱼,茄子,菊花茄子,麻酱牛肚,红油耗儿鱼,蛎虾水饺,大头菜炒螺片,豆沙包,潮州肉骨茶,红烧牛窝骨,碟鱼头,虾贝组合,乡巴佬鱼头,蟹腿,清蒸螃蟹,三丁包,目鱼烤肉,生吃蟹肉,桃之夭夭,妈妈的糍粑,咖喱牛腩饭,妈妈黄米烙,干煸红烧肉,干锅千页豆腐,招牌臭鲑鱼,香芋地瓜丸,蒜蓉粉丝蒸元贝,土鸡,牛尾汤,栗子红烧肉,鲍汁萝卜,海皇自制豆腐,五哥烤翅,香辣龙虾,元年第一罐,蒜香基围虾,葡式蛋挞,铁板芋艿,金牌炭烧猪颈肉,姜母鸭,多士,参汤虾饺皇,碧螺春虾仁,鱼汤,自选蔬菜,香芋蒸排骨,香辣鱿鱼,水豆腐,燃烧吧,肉酱意粉,天麻鱼唇拆烩鱼头,特色肥叉烧,铁板海皇焗豆腐,墨西哥鸡肉烤饼,泰式咖喱味烤鱼,跨炖鱼,萝卜大骨汤,美容蹄,港式菠萝油,凤尾虾,表哥虾饺皇,德国香肠,云腾鸡中翅,原汁牛肉,招牌肉食主义披萨,西班牙鲜虾煲,鲜油菠萝包,新疆羊肉串,囊包肉,水晶鲜虾饺,木屋小串,碳烤牛蛙,上海熏鱼,皇上皇煲仔饭,麻椒鸡,煲蒸饭,嘎巴锅,白切散养鸡,阿五炒饭,草莓酱渍小排,红烧鲤鱼,鲽鱼头,鱼香味烤鱼,鲜青椒味,杂鱼鼎,鸭唇,鲜肉汤圆,任性酸菜鱼,天目湖鱼头,糯米蒸膏蟹,私房鸡,苗家三绝,咖喱炒膏蟹,冰菜,大酱汤,海鲜全家福汤包,阿杜一绝鱼头煲,古法醋烧鸡,左宗棠鸡,三鲜面疙瘩,柠檬黄油加吉鱼,牛肉烧饼,手工饺子,酸辣汤,焦香羊排,龙抄手,古法烧黄鱼,芝士肠,招牌水晶鸡,溪塘鱼片,虾仁独面筋,培根金针菇,贴饼子,木桶豆腐,鸟巢鸭蹼,榴莲阿紫,豉油虾,冰烧三层肉,串烧竹篮虾,大力丸,乌江鱼,鲜花饼,牛首,娃娃菜,上汤娃娃菜,坛子红烧肉,泡泡油糕,糖醋海蛰头,金牌咖喱鸡,煎鹅肝,炉火烤梅肉,咖喱焗薯泥,芥末虾仁,ＸＯ酱炒陈村粉,红柳枝烤肉,法式鹅肝,翡翠豆腐,小鲍鱼,湘宴满堂红,一品大拉皮,虾片,山楂小排,深海龍利魚,食神咖喱牛腩升级,清蒸土鸡,蒜香九肚鱼,深水虾,青椒紫苏干锅,榴芒披萨,榴莲芝士,兰溪煨萝卜,椒盐富贵虾,老汤烩面,金牌靓油条,梅干菜扣肉,东安子鸡,八爪鱼刺身,桂花酒酿园子,番茄一把骨,小米辽参,文思豆腐羹,旺池馋嘴蛙,小炒鸡腿肉,铁板烤虾,松露酱帕尔马火腿,万丽虾饺皇,宁波汤团,蜜汁梅花肉,椒盐皮皮虾,烤鸡,江雪糖醋小排,观音虾,葱油飞鱼,武昌鱼,招牌酸菜鱼,肉炒肉,石锅小三,千页豆腐,深海大虾仁,燒味拼盤,焖锅鱼头,金牌吊烧鸡,湖蟹,胶东一品锅,烤排骨,汉阳黑芝麻热干面,芙蓉全蟹,半份菜,蛋黄焗南瓜,脆肉鲩,贰楼沙拉,XO酱炒萝卜糕,鲜虾饺,咸蛋黄焗虾球,石煮椒香鱼头,牛肉炒河粉,经典京酱肉丝,酱香龙骨,秘制小公鸡,米线,踩姑娘的小蘑菇,骨汤牛肉面,澳洲和牛,芝士焗南瓜,药膳凤爪,香煎鲶鱼,一鸭三吃,生炒糯米饭,目鱼滑,南瓜小汤圆,马来喳喳,鸡汁发芽豆,烤鸭八宝盒,冒鸭肠,妈妈香薰骨,麻辣诱惑,拔丝荔枝,蜂蜜红薯,海鲜烧烤,脆皮叉烧肉,疙瘩面,红烧猪手,宫保鳕鱼球,咸鱼鸡粒茄子煲,新疆沙湾大盘鸡,小吃筐,鲜鸭肠,香草厚多士,酸辣汤配油条,清炒野生大虾仁,土耳其肥牛,扇贝粉丝,牛窝骨,酱板鸭,九层塔杏鲍菇炒培根,咖喱大肉蟹,花蛤,冰粥,大锅鱼,菠萝咕噜肉,慈城年糕烧黄鱼,东阳瓦缸鸡,奥灶面,澳洲雪花和牛,粥底火锅,湘之驴,元年六合鱼,土猪汤,三鲜卤面,双味豆花,豌豆担担面,牛肉河粉,糖藕,黄鳝饭,开胃鱼头,花旗参鸡汤虾饺皇,茅台烹大虾,罗汉面,酱油虾,川味凉粉,爆汁鸡爪,八段小烧肉,锡纸金针菇,薛府一品拉皮,鲜青椒味烤鱼,小锅土豆,在江湖里翻滚的鱼,鱼翅捞饭,炭烧清江鱼,酸汤水饺,手打新鲜生牛肉丸,榄菜四季豆爆鸡脆骨,焦糖茄子牛肉粒,锦府酥虾,甲鱼,宫保鸡丁烤鱼,黑鱼,传统牛肉面,功夫青笋片,优质羊肉串,油焖烟笋,至尊靓靓龙虾,一个锅巴和时蔬,紫薯塔塔,五花腱,鲜牛肉丸,蹄膀,泰式红咖喱味烤鱼,三鲜焖子,涮鲍鱼,剔骨肉拌茄子,蒜蓉粉丝扇贝,金牌猪手,烤秋刀鱼,烤鸭半套,金针菇肥牛卷,潮汕砂锅粥,黑松露鹅肝包生菜,好味酱蒸凤爪,风味凉面鸡,XO酱花菜,椰汁金球,油条菠萝虾,小米蒸三样,三寸菜心,沙参,牛肉火锅,黄油大明虾,金华烧饼,皇牌蝦多士,极品黑毛肚,黑椒牛肉粒,扒汁牛排,洞庭鱼头王,海参黄鱼羹,洋芋叉叉,优质烤鸭,什锦大拉皮,碳烤鳕鱼,老头儿卤鸭,风秘牛肉,怪味大肠,海鲜面,干烧鹅掌,鲜虾刺身,紫苏红烧肉,鱼头王,小笼汤包,越式酸辣凤爪,羊杂,竹筒饭,水煮江团,酸菜饺子,沙茶酱,市井水爆肚,坛香滋味肉,吮指排骨,水库鱼头,生打牛筋丸,天津包子,老屋井水馋嘴蛙,京天红炸糕,榴莲炒饭,津典烤鸭,Q面,功夫麻球,阿拉斯加蟹豆腐,知味虾,芝士焗长茄,温泉蛋咖喱猪扒,招牌靓叉烧,喜饼烤肉,千岛湖鱼面鱼头汤,外婆的红烧肉,水煎包,老式果木烤鸭,老卤合拼,建水香豆腐,鸡骨棒,干锅板筋土豆片,菠菜面,扬州炒饭,小镇烤肉,芝士牛肉丸,盐焗虾,鱼香脆皮虾球,杏鲍菇牛肉粒,清心第一包,奶油豬仔包,牛肋骨,鲥鱼,铁板豆腐,铜锅鱼,盆栽提拉米苏,十满香烤鸡,铁板牛排,万峦猪脚,神马鱼头煲,绍兴老三鲜,秘制烤肉,烤鳕鱼,卤煮,面线糊,草原乳牛排,大碗居烩菜,番茄炖牛腩,大铁锅,小黄鱼烧豆腐,一品鲜虾饺,蜀府炖鱼,皮蒜烤三鲜,炭火干锅龙利鱼,牛仔粒,玫瑰冰粉,混酱猪肠粉,咖喱鸡肉捞饭,老北京的炸酱面,老醋焖草鱼,福茂源麻麻鱼,德莫利炖鱼,狄公面,冻花蟹,葱油蛏子,油碟油,芝麻鸭子,竹蛏皇,印巷红盖,青岛大虾,坨坨牛肉,砂锅烧鱼,铁板三鲜,水果咯吱,秘制小料,烙饼,红丝绒蛋糕,高庄馒头,菠菜锅盔,芝士焗薯角,竹荪鲜虾滑,油焖大龙虾,招牌洋芋,炸河虾,一品碳烤肉,月亮虾饼,砂锅红薯粉,排骨腊味煲仔饭,青椒鲈鱼,桑巴烤菠萝牛堡,碳烤羊肉串,咖喱锅底,活螃蟹,紅燒乳鴿,麻辣淮王鱼,蜜汁酥皮虾,金牌花蛤,宫保牡丹虾球,黑椒猪颈肉,大麻鱼,小銚鸡汤,印尼炒饭,盐烤阿根廷红虾,私房豆腐,糯香掌,墨鱼滑,食神炒饭,蒜香虾,皮蛋粥,爬爬虾,蔬菜沙拉,井冈山豆皮,黑牛肋条肉,洞庭炒藕尖,呆霸王,叉烧菠萝包,干锅杏鲍菇,招牌土豆泥,王品台塑牛排,香煎米耙,一代闷骚南瓜,三更美龄粥,匹夫麻酱,邵式东坡肉,宁乡花猪肉,南瓜汤,话梅秘制牛肉粒,京都排骨,鸡爪煲,黄辣丁,传统猪肉刀削面,鸽肾焗饭,腐皮包黄鱼,新发现鱼头王,小锅牛腩,虾蛟皇,枣泥拉糕,千张筒骨锅,台式三杯虾,石锅烧汁茄饼,屠场毛肚,牛筋腩,黄花鱼汤包,萝卜丝干蛏,老爸牛排三明治,老碗三合一面,冰川咕噜肉,钵子娃娃菜,黑椒牛柳炒意粉,包烤蘑菇,八宝盒,虾,牙签肉,一个馒头,拾年土豆泥,年糕牛仔粒,梭子蟹,调味牛排,石蛙冷锅,跳水鱼,红子鸡,桔子虾饺皇,菌菇野菜豆腐,金蒜牛小排,腊味褒仔饭,烤翅中,桂花冰糖番茄,东来顺羊肉片,草头圈子,干烧大黄鱼,古法狮子头,澳洲雪花牛小排,海鲜豆腐盒,菜包肉,鸽子,新西兰青口面,雪花坛肉,西川厚多士,怡园霸王鸡,瓦片烤肉,糯米鸡,水煮三国,砂锅独元,烧腊,那府果木烤鸭,顺德论教糕,天下第一鸡,黄油焗龙虾,尖叫牛肉,炼乳吐司,厨娘腊味锅巴,高压芋头牛腩,鬼马鲜果虾球,干菜烤汤圆,红烧鳝片,正东担担面,孜然土豆片,鸭脑壳,压肉,至尊肥牛,新派水煮鱼,膳段粉丝,铁板基围虾,唐廊炒饭,肉松焗蟹味菇,青年小炒,酸汤鱼火锅,闷烧葱香大虾,秘制凤爪,免费拉面,芥末酱炸虾球,金牌黄焖龙虾,荷叶饭,BBQ烤翅,橄榄油蓬蒿菜,广式腊味饭,炒饼,红柳肉串,旺炉牛肋筋,一品白菜煲,小茴香拌煎饼,油爆河虾,重庆豆花鱼,喜气洋洋,小池塘串串香,鲶鱼粉皮,牛腩煲,糯米鸭,手打牛筋丸,天下第一包,椒盐牛蛙,龙须菜,炉火烤鸡翅,酱闷黄花鱼,干烧招牌鱼,菜团子,渤海湾虾仁,蛋白饼,吊龙肉,芝士汉堡王,香蕉核桃煎饼,铁板凉粉炒馍,台湾卤肉,皮蛋豆腐,酱烤牛五花,金网脆皮鲜虾肠粉,民间鱼,煎土鸡,黄豆排骨,麻糍,极品虾饺皇,酱爆螺丝,龙凤蝎子汤,概念烤虾,姑妈猪手,灌汤包,桂花糖年糕,古法蒸鸡,玉米片牛油果,油泼榆林老豆腐,小厨土豆丝,四川毛血旺,甜沫,墨鱼仔,四川嫩牛肉,三色面,烧味四宝拼,面包串,精品酸菜鱼,极品雪花肥牛,滑炒里脊丝,罗兰湖炒饭,猫山王榴莲雪砖,鸡扒,蟹钳,燕窝蛋挞,咱家一绝,桑拿虾,瓦罐焖肉,山药炖龙骨,蒜香鸡,铁锅片片鱼,淇淋面包,流沙菠萝塔,咖喱海鲜豆腐,蜜汁莲藕,秘酱土豆,老坛酸菜鱼,咖喱皇酱烧虾,沸腾鲶鱼,大列吧,大骨头,干锅菜花,俄式烤大排,棒子骨,主厨凯撒沙拉,芝麻牛肉,小火锅,木鱼花炭烧豆腐,食为先头道菜,凉拌鱼皮,浇汁莜面,黄金大花卷,烤虹鳟鱼,美极肥牛,藿香鲫鱼,芒果饭,膏蟹,贝勒爷烤肉,大碗菜花,熏肉,招牌牛肉面,越南酸辣火锅,沙姜乳鸽,牛初乳,天使一品锅,藜麦沙拉,火龙鱼,花先生口水鸡,鸡丝豆腐脑,鲍鱼小土豆,海味干捞粉丝,比尔熊掌厚牛排,脆皮烧鹅皇,富贵一品鸡,宫廷秘制烤鸭,陈皮油爆虾,羊腿肉,招牌疙瘩汤,猪肝面,自助沙拉,鲜羊腿,雪山芝士焗长茄,香芋排骨,夏氏一锅鲜,松茸菌,糖醋里几,石河子凉皮,蜜汁烧河鳗鱼,灵隐寺腐衣卷,牦牛肉火烧,鸡鸣酒家汤包,烤蛙,精品肥牛,秘制熏鱼,鲁肉饭,黄骨鱼焖豆腐,黑毛肚,古教水爆肚,八旗小羊排,盐焗鸽,虾仁锅贴,芝士焗大明虾,香煎带鱼,小碗肉,一口怜惜,招牌公司腊味煲仔饭,泰式青柠蒸鲈鱼,碗肉,榕港第一包,炝生菜,农家有机菜花,牡丹虾,鸡血汤,开胃肥牛,黄山臭桂鱼,灯影土豆丝,剁椒鸳鸯鱼头,豉汁蒸鱼腩,鬼马虾球,猪蹄三吃,芝士焗扇贝,张爷爷空心面,鸦片鱼,重庆森林,蹄髈,舒芙蕾,仁和豆花,豌杂面,乳鸽饭,水库鱼,青春饭,软兜,糯香掌翅,芥辣捞鸡,吉士红烧肉,面包秀,煎饼配烤肉,九格麻辣,金汤酸菜鱼,金华千张,吊烧仔鸭,红烧虾,北极甜虾,红糖饼,海胆炒饭,葱烤银鳕鱼,咕噜肉,大串,芝士面包,销魂掌,银鱼炒蛋,招牌秘制捞牛首,猪脚,小馆沸腾鱼,农夫疙瘩汤,手剥河虾仁,秘制烤羊排,民国小磨豆腐,秘制排骨,立方沸腾虾,八爷鸭方,广东腊味煲仔饭,宝轩坛子肉,野生虾仁,油浸蚕豆,醉江湖毛血旺,吸粉,石锅牛仔骨,松饼,四川麻辣虾,牛肉饭,蒜蓉粉丝娃娃菜,糯米鸡翅,火锅粉,老屋精品酸菜鱼,客家土猪汤,烤明虾,酱爆桃仁鸡丁,秘制鸡翅,北极虾,黑糖豆花,关中凉皮,阿庄烤鸭,鄂伦春风情烤鱼,馅满饺子,五谷丰登,羲和烤鸭1套,暹罗小上脑,鱼滑,虾火锅中锅,香妃鸡,炸鸡,竹荪三鲜,招牌焖肉面,鱼露花甲,莫吉托微风,千岛酱鱼排,石锅杏鲍菇,梅猪肉,辣宴秘制牛肉,黄金酸汤鱼片,菠萝咕老肉,海南椰子鸡,儿时味炸猪排,果味山药,一鱼双吃,文鱼汤,盱眙十三香龙虾,芝士石锅年糕,邕城老友鱼,十八香龙虾,青稞黑豆腐,铁板鮰鱼,石锅饭,浓汤鱼饺,南瓜饼,清蒸笋壳鱼,避风塘炒蟹,海鲜烩饭,风味鱼渣,豆腐圆子,避风塘虾饺皇,干烧大明虾,蛋黄龙虾,粉丝扇贝,沸腾香辣蹄,打卤馕,蟹肉煲,招牌流沙包,招牌大虾,招牌茄子,一品拉皮,手切鲜羊肉块,土豆拌莜面,青花椒砂锅鱼,烧羊肉,南翔小笼包,特选头牌烤鸭,食神牛腩,燒鵝,柠檬虾,瓦缸烤串,四城第一罐,茉莉金牌厚多士,烤牛油果,茴香软饼,鸡肉饭,辣小碗,面包篮,客家茄子煲,叉烧肉,凤爪蒸笼,宫爆豆腐,香锅扯面,一品海皇三宝拼,招牌德国猪脚,咸蛋黄蒸豆腐,沂蒙山炒鸡,滋味烤羊排,虾虎,香蕉飞饼,招牌鲜虾小馄饨,武汉臭干子,招牌炸鱼塔克,养生有机时蔬,一鱼三吃,酸梅冰粉,松鼠鳜鱼,飘香水煮鱼,奶沙虾膏酿老油条,排骨压土豆,食客熏排骨,情妹特色鹅掌,牛蹄筋,龙虾汁海鲜捞饭,麻辣酸菜鱼,酱油水竹蛏,金牌烤鸭仔,金牌滋味肉,花溪牛肉粉,草菇蒸凤爪,大盆花菜,海鲜闷面,菜心炒鸡杂,大娘单饼卷两样,餐前包,海胆豆腐,樱桃山药,湘汇烤鱼,云南野菌饺,香煎米粑,招牌虾饺妹,珍珠小马蹄,甜甜小公鸡,怒火ga,红油梭边鱼,绝代双骄,金汤鱼片,酱香味淮王鱼,麻辣牛肉粉,董家极品毛肚,小酥鱼,招牌龙虾,相思鲈鱼,一口西多士,越式酸辣海鲜汤河粉,鱼排,铁板粉丝,手作牛肉丸,牛小排,铁板日本豆腐,水煮肉,蒜香大鱿鱼,金牌干炸丸子,米皮,酱肉包,迷你小份,蓝蛙汉堡,蜜汁叉烧饭,冒脑花,满园烤鸭,大锅蒸海鲜,多嘴蟹肉煲,干锅有机菜花,本初烧,东来顺特色羊肉,干锅排骨虾,斑鱼火锅,海南鸡饭套餐,滋味牛仔骨,重庆辣子鸡,鱼香龙虾,香蕉甩饼,虾籽面,招牌鱼头煲,招牌炸虾仁,折耳根,特色凹锅肉,生抠鲜鸭肠,藤椒鹅肠,铁锅鲜鱼上上签,千丝肚,太阳卷,匹夫麻酱小料,精品酥不腻烤鸭,红油耗儿鱼锅,煎羊扒,蚵仔肉碎泡饭,烩三鲜,麻香嫩牛肉,火车头河粉,凉拌鲫鱼,玫瑰圆子,酱油河虾,爆烤猪蹄,巴西烤肉,碧绿菌皇炒带子,沸腾水煮鱼,桂花酒酿圆子,粗粮拉皮,锅巴,扁豆排骨焖卷子,百花稍梅,鲜牛黄喉,西红柿鸡蛋扯面,鸳鸯海鲜汤,青花椒麻辣鱼,南越春卷,石屏豆腐,让豆腐,麻将油麦菜,椒盐大王蛇,秘制江湖鱼,混椒鸡,江油肥肠,烤牛蛙,嘎巴香三样,一座城池,招牌脆皮鸡,竹荪菌,新封咸肉,晓宇嫩牛肉,眼肉,奶豆腐,牛尾,石头挑鸡蛋,三鲜烩面,水煮臭豆腐,石锅鸡,素馅烧麦,莎莎酱配玉米片,苔菜江白虾,那家特色豆腐,麻酱花卷,美式大薯条,卤蛋,麻花虾球,麻辣排骨,BBQ烤猪排,法国鹅肝酱焗豆腐,腐皮鸡毛菜,粉蒸排骨,肥肠煲,电烤羊肉串,鲜青椒味的鱼,熏鱼面,招牌风干肉,小肠炖百叶结,药膳炒饭,野生黄鱼煨面,鱼汤面,羊肉火锅,湘式剁椒鱼头,招牌柠檬虾,扬州干丝,铁锅鲶鱼,酸辣白菜,糖醋小排骨,挪威三文鱼,清远鸡,坛子牛排,炭火干锅虾,烧汁肥牛,鲈鱼,美蛙锅,酱猪蹄,咖喱肥牛土豆泥,经典牛肉面,榴莲流奶牛角包,金牌蛋豆腐,开片基围虾,黑鱼煲,大骨煲,宫保鳕鱼丁,帝王蟹腿,大饼卷肉,大头菜爆海螺片,赤豆糊,红烧牛舌尾,干贝火腿冬瓜,鸽子饭,香煎猪肝,芝士南瓜,养生核桃包,伊府面,招牌牛肉,小辣望潮,招牌猪手,鸟贝,糯米红枣,素锅贴,童年茶香肉,山药宽粉,奶油蘑菇浓汤,四季鱼头,麻辣空间,蜜汁叉烧皇,锦庐四品,黄粑,老洛阳卤面,金牌魔鬼牛蛙,藜蒿炒腊肉,酱烧笔管鱼,龙虾煲粥,豆油皮,80后有机菜花,半筋半肉面,嘎鱼,海鲜全家福,黑曜石,八宝粥,海味焖面,干锅排骨,炒饭,果木牛扒,德国乳酪肉肠,鲜椒啤酒兔,越南米线,西旗系列,武林厨神虾饺,全爆,太湖三白,特色熏鱼,培根芝士鸡蛋薯条,送的雪花肥牛,桃仁油麦菜,江湖情酸菜鱼,煎饼卷大葱,龍蝦泡飯,金丝沙律虾,鼎鲜招牌斑鱼片,炒鸡杂,韩国小牛肉,翅汤东星斑,养生一品老鸡汤,咱家茄子,摇滚东北大拉皮,鲜虾肠,招牌酱大骨,炸软壳虾,猪肚汤,五谷杂粮蒸牛小排,香烤玫瑰鱼,原创芝士蛋糕,特制豆腐,十秒涮鱼片,肉丸汤,石锅香芋,南美烤肉,石锅鲫鱼,铁板鱿鱼须,牛油锅,天鹅蛋,跳舞钢管鸡,沙盖面筋,坛子肉,燃烧吧蜂窝煤,擂椒茄子,面疙瘩,黄焖鱼肚,马兰干红烧肉,江团鱼,马苏里拉焗金瓜,黄豆猪蹄,金沙豆腐,糊饼,烤大茄,毛氏爆三样,锦绣虾仁肠粉,八爪鱼,芭蕉叶烤肉,安格斯雪花牛扒,海虾,翅汤白菜叶,羊棒,药膳乳鸽,响铃卷,状元及第粥,新加坡特香鸡,粥涮鲈鱼,香草凤尾虾,铁板手撕包菜,牛排套餐配烩饭,铁板锡纸鲈鱼,牛三哥牛肉汤,水煮巴沙鱼,清蒸白水鱼,三不粘,泰式菠萝炒饭,天津卫粘肉,芹菜虾丸,食尖兒秘制小龙虾,胡椒饼,煎饼果子,花雕焖肉,椒盐虾,江南生煎包,老厨白菜,话梅排骨,极品老爆三,鸡煲翅,扒羊肉条,锅盔盐煎肉,风味大头菜,关中四小件,桂花小汤圆,高原毛肚,蛏子王,招牌香辣蟹,乌珠穆沁羊肉,招牌相思葡挞,招牌板烧鸡,转角遇到爱,虾火锅,月牙骨羊肉,淹城鱼头,我爱龙利鱼,蒸美国大凤爪,肉酱千层面,霜降牛排,石锅蝴蝶虾,特价肘子,牛三件,蜜枣银心,华德汤包,美极元宝虾,鸡肉玉米片,金牌蚝饭,妈妈特色年糕,邻居家的蜂窝煤,金枪鱼,凉拌苦菊,考伯色拉,炒圣子,参汤虾饺,鲍鱼鸡,炖排骨,法式蜗牛,红烧牛肉,鲍鱼仔,桂花糕,大饼猪头肉,蟹肉粉丝堡,小米活海参,香辣鱼,一级腊味饭,一品蛋酥,猪五花,猪扒包,炭烧香茅猪扒,沙爹金钱肚,石烹秋葵,偷吃豆腐,牛肉焖饼,牛舌Tacos,江米排骨,酱骨王,巨无霸鲅鱼水饺,酱筋,煎封乌鲳,芒果糯米饭,阿胶烩凤爪,脆皮鸽,翡翠螺,0731鸡煲,香辣味碳烤清江鱼,折罗,新派毛血旺,徐家口口脆,鸭胗,香葱鲈鱼,湘汇烤全鱼,藤椒鸡,土豆炖牛腩,藕海,蜜桃烤鱼,菌汤锅,魔芋,金钱肚,口袋豆腐,老济南甜沫,俺家自制拉皮,包菜炒馕,冻鸳鸯,蛋黄焗海蛎,番茄炖鱼,蚝烙,一品豆腐,香港深井烧鸭,整只波士顿龙虾,一蟹两食,清蒸海鲈鱼,松子鱼,藕夹,松茸炖鸡,卤猪蹄,黄焖羊肉,辣子蒜羊血,蒙古牛肉,秘制牛肋骨,冒烤鸭,霸王排骨虾,比脸大的毛肚,霸王虾,宫保豆腐,大核桃花生仁,杭州小排骨,蟹粉笋丁面,壹米家红烧肉,虾饺王,湘宴手工蒸饺,薯角,十八秒油爆虾,沙拉牛排,牛肋排,慕斯蛋糕,炭舍跳跳蛙,牛排杯,酸汤鲈鱼,酸笋,首创剪刀鸭,绿野仙踪轻盈吐司,酒炙鸡翅,妈妈炒饭,金牌乳鸽,捞派滑牛,酱汁蒸凤爪,宫爆皮蛋,何师烤肉,干煸茶树菇,古法坛子肉,豆角焖面,古井三杯鸡,波士顿大龙虾,海肠茄子,taco,黑椒石锅肥牛饭,脆皮猪肘,油燜大蝦,炸鱼皮,招牌酸辣粉,王献之排骨,野菌圆子,原只鲍鱼烧鹅酥,玉米馒头,香蕉千层蛋糕,招牌厚多士,碳烤建水豆腐,农家大拌菜,桃园芝士烤红薯,水晶粉,水果披萨,色拉,手撕鸭,酸菜鱼米线,生态鸭,千品肝尖,孔府煎包,美蛙鱼头,金银海鲜泡饭,老姑熏排骨,黎蒿炒腊肉,烤猪肘,叻沙,梅香仔排,葱头白切鸡,桂花赤豆小圆子,厨娘小笼包,芝士焗山菌,杂粮海参,现制豆腐,招牌馕坑烤羊蝎子,香辣花蛤蜊,望京小腰,炸豆腐皮,南煎肝,鲶鱼砂锅,牛小肠,酥肉豆腐脑,笋干烧肉,乳鸽局饭,泼辣牛柳,素火腿,瑞士鸡翅,黄糖糍粑,金牌手切肥牛,秘制铁板鲈鱼,梅菜排骨,玫瑰香颂,黄瓜鲜虾水饺,黄鱼羹,粑粑来了,宫保鳕鱼,羔羊肉,稻草鸭,蕃茄长江洄鱼,和味酱皇蒸凤爪,包酱豆腐,桂林山水小刀鸭,黑松露烧麦,张飞牛肉,相思红豆糕,香煎龙利鱼,手撕猪肉汉堡,糖蒜爆羊肉,巧克力熔岩蛋糕,牛油果核炒蛋,酸豆角肉沫,怯勺腰花,木桶豆花,龙虾意大利面,菌菇虾仁锅贴,椒盐鸡翅,酒阳蒸经,酱香烤鱼,玫瑰红糖冰粉,黄油蒜汁沁三文鱼,栗子奶豆腐,超级大捞檬,好吃土豆,董家现炸酥肉,法式野菌奶油蘑菇汤,馋嘴蛙锅,八珍豆腐煲,法式菌菇浓汤,冰球鹅肠,拌面,黑松露浓汤,桂林米粉,宫爆鸡丁年糕,鱼饭,雪花双流星,招牌生煎包,香菇藜麦饭,文杏鸡,芝士榴莲披萨,一把骨,西红柿鸡蛋饺子,香辣蛙,莜面栲栳栳,千叶豆腐排骨,瓦罐鸡,酸辣虾,清酒鹅肝,水盆羊肉,柠桔乳酸菌,舒友笋尖虾饺皇,艇仔粥,台湾双肠,牛肉粒锅巴塔,酸汤酥肉,陶陶居百年烧鹅,鸡丝荞麦面,咖喱时蔬配手抓饼,芥末堆,辣炒年糕,吉士咸鸡,美玲拉面,精选羊排,卤粉,九香牛肉,六运特色有机肠,黄金酸辣鱼,烤鹿腿,菌菇汤,黑椒花蛤,豆腐煲,茶叶虾,大馒头,和风豆腐,港式凤爪,红烧小土豆,斑鱼皮,翡翠蟹肉汤,鲜椒水煮巴沙鱼,五谷杂粮米饭,香辣牛肚面,意粉,西柚牛小排,外婆红糖年糕,藤椒钵钵川串,绝代双椒鱼头,老黄县炸里脊,连汤肉片,老母鸡汤,厚五花,摩登粑粑,老杭热酥鱼,螺狮粉,经典牛仔扒,极品烤鸭,螺蛳粉,六合鱼,腊肠,火爆三鲜,爆煎鱼,港式鸡蛋仔,冰火酥皮菠萝油,红烧鸽,海鲜砂锅粥,TALK虾饺皇,Laksa,芝士番薯,羊拐,雪蟹腿,芝士香肠,盐煨牦牛肉,招牌碳烤鸭,鱼汤及鱼片,香螺,上汤螺蛳,申海第一罐,弄堂里的鸭儿,蔬菜卷,屠场鲜毛肚,牛油鸡,藤椒鱼,调味五花肉,水云丝,牛肉饼,太子墨汁肠,蜀香排骨鸡,柠檬脆皮鸡,宁波汤圆,会上瘾的牛肉,捞拌凤爪,金丝香茅锅巴虾,鸡粥,酒酿园子,老油条炒牛肉,火火迷你烤猪蹄,非一般的腐竹,大屁股烤虾,茶树茹小炒肉,二道桥大盘鸡,红烧青蛙,干贝鲜虾粥,脆皮茄丁,福缘面,海蛎子,自助凉菜,油渣土豆丝,咸蛋黄焗龙虾,香煎牛仔骨,越南榴莲肠粉,至尊虾饺,鲜草莓芝士蛋糕,招牌嫩牛肉,香锅鸡翅,仔姜爆蛙腿,一口西多,招牌米粉,牌九蘑菇,三道鳞,石锅酱萝卜,田式宫保虾,沙锅鱼头,肉汁萝卜,深井烤鹅,烧三丝,签签缺锅,糖醋鱼片,煎饼,梅子小排,凉拌腰花,鸡汤百叶包,老济南油旋,大蝗鱼,独家特制鸡翅,海鲜锅,冬阴功王粉,杂锦米线,鱼羊鲜,油爆脆鳝,一米肠,猪手煲土鸡,鲜虾饼,竹筒第一排,至尊烤鸭,清蒸大龙虾,松仁爆炒黄牛肉,双椒鸡捞面,酸菜白肉血肠,秋葵,三鲜生煎包,头啖汤,松子桂鱼,蒜香小龙虾,焗锅老南瓜,椒盐龙虾,荔湾猪手,京味爆肚,酱骨棒,辣炒海丁,民间桶烤饼,脊骨,椒盐排条,金牌龙虾,金奖黑叉烧,干煸口蘑,八宝饭,草鱼,澳洲肥牛,大肠肺头汤,澳洲M3牛侧边肉,招牌芝麻鸡,现场拉面,香酥排骨,芝麻菠菜,油豆角炖排骨,小磨豆腐,芝士局番薯,印度抛饼,南瓜小米粥,碳烤扇子骨,撒尿牛丸,石锅茄子,鳝糊"

  def seqOp(U: scala.collection.mutable.Map[String, Int], T: (String, Int)): scala.collection.mutable.Map[String, Int] = {

    val oldNum = U.get(T._1)
    if (oldNum != None) {
      U.put(T._1, oldNum.get + T._2)
    } else {
      U.put(T._1, T._2)
    }
    U
  }
  def combop(U1: scala.collection.mutable.Map[String, Int], U2: scala.collection.mutable.Map[String, Int]): scala.collection.mutable.Map[String, Int] = {
    U2.seq.map(T => {
      val oldNum = U1.get(T._1)
      if (oldNum != None) {
        U1.put(T._1, oldNum.get + T._2)
      } else {
        U1.put(T._1, T._2)
      }
    })
    U1
  }

  def main2(args: Array[String]) {
    println(recipes.split(",").size)
  }
  def main(args: Array[String]) {
    println("useage: args(0): inputPath  args(0):recipTopN")
    var recipTopN = 50;
    var inputPath = "/user/hadoop/ufsClusterInput/ufs-cluster.csv"
    if (args.size > 0) {
      inputPath = args(0)
    }
    if (args.size > 1) {
      recipTopN = args(1).toInt
    }

    println("params: inputPath:" + inputPath + " recipTopN:" + recipTopN)
    val conf = new SparkConf().setAppName("RestaurantCluster")
    val sc = new SparkContext(conf)
    val recipesArrayBc = sc.broadcast[Array[String]](recipes.split(","))
    val sqlContext = new SQLContext(sc)
    val allResult = sc.textFile(inputPath).map { line => splitLine(line, recipesArrayBc) }.reduceByKey(_ ++ _).mapValues(list => {
      val recipeNums = scala.collection.mutable.Map.empty[String, Int]
      list.map(recipeNum => {
        val oldNum = recipeNums.get(recipeNum._1)
        if (oldNum != None) {
          recipeNums.put(recipeNum._1, oldNum.get + recipeNum._2)
        } else {
          recipeNums.put(recipeNum._1, recipeNum._2)
        }
      })
      recipeNums
    })
    allResult.cache()
    import org.apache.spark.SparkContext._

    //    val sample = allResult.take(2500)
    val recipeMapAll = allResult.flatMap { x =>
      {
        x._2.toSeq
      }
    }.aggregate(scala.collection.mutable.Map.empty[String, Int])(seqOp, combop).map(x => (x._2, x._1))

    val recipeTop = sc.parallelize(recipeMapAll.toSeq).sortByKey(false).take(recipTopN)
    sc.parallelize(recipeMapAll.toSeq).saveAsTextFile("/user/hadoop/ufsCluster/Dir_recipeMapAll" + recipTopN)
    sc.parallelize(recipeTop).saveAsTextFile("/user/hadoop/ufsCluster/Dir_recipeTop" + recipTopN)
    val top_recipe_orderMap = sc.parallelize(recipeTop).map(f => f._2).zipWithIndex().collect().toMap
    //    val restaurantMap = sample.map { x => x._1 }.aggregate(scala.collection.mutable.Set.empty[String])(_ + _, _ ++ _).zipWithIndex.toMap
    //    val restaurantMapBC = sc.broadcast(restaurantMap)
    val top_recipe_orderMapBC = sc.broadcast(top_recipe_orderMap)
    val formatResult = allResult.map { record =>
      {

        val array = Array.fill(recipTopN)(0.0)
        record._2.map(x => {
          if (x._2 > 0) {

            val recipeId = top_recipe_orderMapBC.value.get(x._1);

            if (recipeId != None) {
              array(recipeId.get.toInt) = 1.0
            } 
          }
        })

        var result = record._1 + ""
        val size = recipTopN - 1
        for (i <- 0 to size) {
          result = result + " " + (i + 1) + ":" + array(i.toInt)
        }
        result
      }
    }
    sc.parallelize(top_recipe_orderMapBC.value.toSeq).saveAsTextFile("/user/hadoop/ufsCluster/Dir_recipeMapTop" + recipTopN)
    formatResult.saveAsTextFile("/user/hadoop/ufsCluster/Dir_formatResult" + recipTopN)
  }

  def splitLine(line: String, recipesArrayBc: Broadcast[Array[String]]): (String, scala.collection.mutable.ListBuffer[(String, Int)]) = {
    val recipeNums = scala.collection.mutable.ListBuffer.empty[(String, Int)]
    val countArray = line.split(",", 4204);
    var i = 0;
    val lenth = recipesArrayBc.value.size
    while (i < lenth) {
      recipeNums.+=((recipesArrayBc.value(i), countArray(i + 4).toInt))
      i = i + 1
    }
    countArray(0) -> recipeNums
  }
}