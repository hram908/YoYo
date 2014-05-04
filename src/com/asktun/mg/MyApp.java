/**
 * 
 */
package com.asktun.mg;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;

import com.asktun.mg.bean.GameInfo;
import com.asktun.mg.bean.LoginBean;
import com.asktun.mg.bean.UpdateLngLat;
import com.asktun.mg.service.ApkItem;
import com.asktun.mg.service.FileService;
import com.asktun.mg.utils.SoundPlayer;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.chen.jmvc.util.Application;
import com.chen.jmvc.util.IResponseListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MyApp extends Application {

	public static boolean isSoundEnable = true;
	public static boolean isVibrateEnable = false;
	public static boolean isNoticeGroup = true;

	private static MyApp mInstance = null;
	public static final int NUM_PAGE = 6;// 总共有多少页
	public static int NUM = 20;// 每页20个表情,还有最后一个删除button
	private Map<String, Integer> mFaceMap = new LinkedHashMap<String, Integer>();

	private LoginBean loginbean;

	private GameInfo downloadSuccess;
	private GameInfo startDownloadMovieItem; // 需要下载的任务

	public LocationClient mLocationClient = null;
	public MyLocationListenner myListener = new MyLocationListenner();
	public BDLocation mLocation;
	
	private ArrayList<ApkItem> updateList; // 更新列表
	
	
	private String[] gameNameArray = {"我叫MT", "傲世西游", "雪人兄弟", "乐动达人", "天天酷跑",
			"我叫MT Online", "神庙逃亡", "狂野飙车7：极速热力", "开心水族箱", "扩散性百万亚瑟王", "降龙Q传",
			"王者之剑", "神庙逃亡2", "火影大人", "愤怒的小鸟太空高清版HD", "全民英雄", "神庙逃亡：魔境仙踪",
			"悟空救我", "天天连萌", "卧龙传说", "幻仙", "天天爱消除", "3D光速飞车_ LightSpe",
			"极限方程式赛车", "午夜狂飙_ Speed Nigh", "天天幻想", "极品漂移", "石器时代OL", "地铁跑酷",
			"斗三国", "夜店达人", "魔王传记", "部落战争", "滑雪大冒险", "拆那部落", "3D极速摩托", "疯狂出租车",
			"部落守卫战", "飞机假日", "职业狙击手", "摩托艇大赛", "拉力越野赛车", "抢滩登陆", "节奏大师",
			"致命空袭2", "史上最牛的游戏2", "空中打击", "众将听令", "音乐节奏", "看图猜成语", "雷电2013",
			"节奏英雄", "天外飞仙", "玛法传奇", "蛮荒记", "精英部队", "保卫萝卜", "疯狂猜图", "坚韧战车",
			"长耳朵快跑", "王国使命", "音乐游戏", "真实赛车3", "疯狂猜成语", "封神三国", "都市赛车6:火线追击",
			"3D超时空战机", "空战1945", "植物大战僵尸2", "音乐节奏方块", "台球大师(专业版)", "最后的防线",
			"疯狂猜歌", "太平洋空战", "梦想海贼王", "仙境传说 紫罗兰", "太空战机(免费版)", "消灭星星", "大侠传",
			"龙印OL", "大话水浒", "极品飞车17:最高通缉", "空中海军战士", "3D桌球", "最后的防御 HD",
			"英雄战魂", "苍穹之剑3D", "异域战将", "水果射击", "喵将传", "捕鱼之海底捞", "弹壳:地狱佳丽",
			"大掌门", "极限摩托", "王者之剑", "C.H.A.O.S空战直升机", "捕鱼达人", "音乐节奏", "暴力摩托",
			"抒情传奇2音乐游戏", "妖姬三国", "战神直升机", "节奏快打", "节奏", "雷霆赛车2", "迷你英雄:痞子召唤",
			"割绳子", "捕鱼达人2", "osu节奏大师", "傲气雄鹰:Sky Force", "真三国快打", "进击的三国",
			"愤怒的小鸟", "僵尸萝莉", "萌斗罗", "爬坡赛: Hill Climb ", "超级战斗直升机", "前线防御",
			"疯狂沙滩摩托", "危险节奏", "坦克之战", "节奏触碰 RhythmTouch", "都市赛车5",
			"混沌与秩序之英雄战歌", "魔界勇士", "特种部队", "节奏王", "魔法世界", "猎鹿人2014", "找你妹",
			"四国战机", "希腊神话", "Trial X", "节奏猫", "会说话的汤姆猫2", "女王的皇冠", "天空战争",
			"激流快艇", "节奏电子乐", "水果忍者", "忍者天下", "威虎战机", "高速骑士", "泡泡龙", "梦想节奏",
			"结束黑暗", "新版雷电2012", "时空猎人", "地球防卫者", "阳光走私船", "极品钢琴", "节奏拇指",
			"口袋精灵II", "逆天仙魔录", "和风物语", "愤怒的小鸟:星球大战版", "NBA2K14", "神奇宝贝之精灵战纪2",
			"天堂岛", "仙战", "神鬼幻想", "恶魔来了", "英雄杀", "魔法庄园 Magic Tree", "街头篮球",
			"合金弹头3", "刀剑天下", "美女茶餐厅2", "音乐节奏游戏", "罪恶飙车都市", "燃烧的蔬菜2", "大富豪",
			"天天打魔塔", "家园7:贵族", "跑酷小恐龙", "节奏达人", "啪啪三国", "天天酷跑", "封魔", "梦幻小镇",
			"龙将", "音乐游戏之音符爆破", "恐龙岛 Dino Island", "魂斗士", "3D摩托", "魔王养成日志",
			"蛋糕工坊:名厨演示", "猎神OL", "音乐英雄", "街机名将", "我的国家", "燃烧的蔬菜",
			"快节奏游戏 Rocketron(", "玉契OL", "疯狂滑雪", "不朽的神迹", "野兽湾", "真人斗地主",
			"手指游戏", "热血足球经理", "铜雀二桥传", "斩仙录", "疯狂原始人", "乐动舞指", "武侠Q传", "暗夜龙枪",
			"神魔", "小小汽车库", "忘仙", "音乐魔方打击", "酒馆英雄", "我的三国志", "时空猎人", "音乐工坊",
			"LINE我爱咖啡", "街机勇士", "3D跑酷", "暖暖温泉乡", "神将盛世", "摩卡世界3D", "音乐武士",
			"史上最难的100层", "极限滑板", "永恒战士2-无双战神", "巨龙之怒", "模拟农场2012", "节奏广场",
			"霓虹都市", "逆流", "冠军足球物语2", "音乐机器人", "沙滩车闪电战", "Dragon and Shoem",
			"恐龙快打", "烈火西风", "加菲猫餐厅", "街头霸王II", "欢乐斗地主", "钓鱼看漂", "炸弹人", "奇妙动物园",
			"胡来麻将", "暴打盗墓贼", "跑酷游戏(单机版)", "植物保卫战", "拳皇97", "愤怒的老奶奶玩酷跑",
			"豪华咖啡厅", "天天斗牛", "神魔", "铁拳:竞技场", "王者之刃", "连连看(经典版)", "经营酒店",
			"大亨诈金花", "天降奇兵", "三国牛牛", "割绳子:玩穿越", "摩尔庄园", "艾诺迪亚3:卡尼亚传人", "暗影格斗2",
			"斗地主(单机版)", "乌法鲁山 for Kakao", "口袋妖怪红宝石", "会说话的狗狗本", "中国象棋",
			"冒险迷宫村", "皇冠炸金花", "我的世界", "波斯王子:影与火", "空港大亨", "决战时刻", "五子棋",
			"斗地主(单机版)", "极速飞车", "刺客信条2", "老虎机", "三国杀", "极限摩托3",
			"俄勒冈之旅 离线修改版(汉化版)", "鳄鱼小顽皮爱洗澡", "加菲猫餐厅夏威夷版", "爬坡赛车", "凯撒德州扑克",
			"梦想小屋", "阿童木跑酷游戏", "QQ欢乐斗地主", "SpeedCarII", "斗地主欢乐版", "克萊麗酒吧",
			"公路赛车手", "麻将(单机版)", "极品飞车:亡命天涯", "棒打西瓜", "快乐炸金花",
			"最高时速 A Top Speed", "超级玛丽", "泡泡对对碰", "疯狂农场", "十三张", "狂野时速",
			"骷髅小王子", "Kakao决战地下城", "魔法消灭", "Kingdom Builder",
			"极速赛艇 Wave Blazer", "我的英雄", "枪手联盟", "开心水果也疯狂HD", "疯狂十三张", "永恒战士2",
			"TestKnifeKing3-Z", "侠盗飞车", "军棋", "史上最难破解的100道门", "罪恶之地",
			"丫丫欢乐斗地主", "战争故事", "FIFA 14", "成语玩命猜", "蛋糕工坊之主街", "乱斗堂", "酷蛙斗地主",
			"水果老虎机", "迷失之岛", "出租车停车", "格斗之王 5", "海霸天下", "劲乐团4", "QQ中国象棋",
			"3D乒乓球", "婚纱专卖店", "波克棋牌", "猜歌王", "魔境仙踪", "三维保龄球", "QQ斗牛",
			"3D泥路货车 Dirt Road", "欢乐斗牛", "JJ欢乐斗地主", "极速蜗牛", "GT赛车", "音乐无限",
			"五子棋大师", "台球帝国", "狂斩三国", "多酷象棋", "3D赛马", "我要猜歌词", "帝国塔防2", "四川麻将",
			"快乐网球", "魔兽争霸2", "塞尔达传说2", "博雅四川麻将", "流行音乐", "燃烧的轮胎", "麻將天下",
			"排兵布阵", "三国志", "卡通战争", "三国邪魂", "百人牛牛", "真实滑板", "七彩祖玛3", "跳跃忍者",
			"蜡笔小新防御", "猛将风云", "国际足球大联盟12", "滑板少年", "单机欢乐斗地主", "三英战吕布", "涂鸦跳跃",
			"农场保卫战2", "投篮机", "重装机兵", "蘑菇战争", "天天斗地主", "拼图游戏", "火柴人足球", "龙穴塔防",
			"QQ欢乐斗地主", "龙穴守护者 Lair Defen", "魔界勇者2", "篮球梦之队", "魔域猎人", "斗地主",
			"世界防御", "真实拳击", "仙魔战神", "多人诈金花(单机版)", "契约2", "世界乒乓球赛", "醉江山",
			"二战飞机", "防御地带", "波克麻将", "基地保卫战", "火柴人竞速滑雪", "英雄守卫2", "反恐精英",
			"象棋巫师", "手指滑板", "史诗塔防:元素", "小小大战争", "丧尸围城（HD）", "踢足球", "果冻塔防",
			"点球达人", "帝国奇袭", "飞行棋大作战", "炫彩曲棍球", "灌篮高手之全国大赛", "3D疯狂赛车", "瘦身战争",
			"尖峰滑雪 SummitX Sno", "都市赛车7:热度", "Velox Reloaded", "乐奇足球", "火柴人跳跃",
			"极限车挑战" };
	public static List<String> gnList = new ArrayList<String>();

	@Override
	public void onCreate() {
		super.onCreate();
		mInstance = this;
		initFaceMap();
		initGNList();
		initImageLoader(getApplicationContext());
		mLocationClient = new LocationClient(this);
		setLocationOption();
		startLocation();
		SoundPlayer.init(this);
		initPicturesResources();
	}
	
	/**
	 * 初始化图片资源
	 */
	private void initPicturesResources() {
		FileService.loadFileToMap();
	}

	@Override
	public void onTerminate() {
		// TODO Auto-generated method stub
		super.onTerminate();
	}

	public static MyApp getInstance() {
		return mInstance;
	}

	public static void initImageLoader(Context context) {
		// This configuration tuning is custom. You can tune every option, you
		// may tune some of them,
		// or you can create default configuration by
		// ImageLoaderConfiguration.createDefault(this);
		// method.
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).threadPriority(Thread.NORM_PRIORITY - 2)
				.denyCacheImageMultipleSizesInMemory()
				.discCacheFileNameGenerator(new Md5FileNameGenerator())
				.tasksProcessingOrder(QueueProcessingType.LIFO)
				.writeDebugLogs() // Remove for release app
				.build();
		// Initialize ImageLoader with configuration.
		ImageLoader.getInstance().init(config);
	}

	@Override
	public android.app.ProgressDialog getProgressDialog(Activity act) {
		return new ProgressDialog(act);
	}

	private Map<String, Activity> activityList = new HashMap<String, Activity>();

	// 添加Activity 到容器中
	public void addActivity(Activity activity) {
		activityList.put(activity.getLocalClassName(), activity);
	}

	public void removeActivity(Activity activity) {
		if (activityList.containsKey(activity.getLocalClassName()))
			activityList.remove(activity.getLocalClassName());
	};

	// 遍历所存所有Activity 并finish
	public void clearActivity() {
		Iterator<Entry<String, Activity>> iter = activityList.entrySet()
				.iterator();
		while (iter.hasNext()) {
			Entry<String, Activity> entry = iter.next();
			Activity activity = entry.getValue();
			activity.finish();
		}
		activityList.clear();
	}

	private void initGNList() {
		gnList = Arrays.asList(gameNameArray);
	}

	private void initFaceMap() {

		mFaceMap.put("[微笑]", R.drawable.smiley_0);
		mFaceMap.put("[撇嘴]", R.drawable.smiley_1);
		mFaceMap.put("[色]", R.drawable.smiley_2);
		mFaceMap.put("[发呆]", R.drawable.smiley_3);
		mFaceMap.put("[得意]", R.drawable.smiley_4);
		mFaceMap.put("[流泪]", R.drawable.smiley_5);
		mFaceMap.put("[害羞]", R.drawable.smiley_6);
		mFaceMap.put("[闭嘴]", R.drawable.smiley_7);
		mFaceMap.put("[睡]", R.drawable.smiley_8);
		mFaceMap.put("[大哭]", R.drawable.smiley_9);
		mFaceMap.put("[尴尬]", R.drawable.smiley_10);
		mFaceMap.put("[发怒]", R.drawable.smiley_11);
		mFaceMap.put("[调皮]", R.drawable.smiley_12);
		mFaceMap.put("[呲牙]", R.drawable.smiley_13);
		mFaceMap.put("[惊讶]", R.drawable.smiley_14);
		mFaceMap.put("[难过]", R.drawable.smiley_15);
		mFaceMap.put("[酷]", R.drawable.smiley_16);
		mFaceMap.put("[非典]", R.drawable.smiley_17);
		mFaceMap.put("[抓狂]", R.drawable.smiley_18);
		mFaceMap.put("[吐]", R.drawable.smiley_19);
		mFaceMap.put("[偷笑]", R.drawable.smiley_20);
		mFaceMap.put("[可爱]", R.drawable.smiley_21);
		mFaceMap.put("[白眼]", R.drawable.smiley_22);
		mFaceMap.put("[傲慢]", R.drawable.smiley_23);
		mFaceMap.put("[饥饿]", R.drawable.smiley_24);
		mFaceMap.put("[困]", R.drawable.smiley_25);
		mFaceMap.put("[惊恐]", R.drawable.smiley_26);
		mFaceMap.put("[流汗]", R.drawable.smiley_27);
		mFaceMap.put("[憨笑]", R.drawable.smiley_28);
		mFaceMap.put("[大兵]", R.drawable.smiley_29);
		mFaceMap.put("[奋斗]", R.drawable.smiley_30);
		mFaceMap.put("[咒骂]", R.drawable.smiley_31);
		mFaceMap.put("[疑问]", R.drawable.smiley_32);
		mFaceMap.put("[嘘]", R.drawable.smiley_33);
		mFaceMap.put("[晕]", R.drawable.smiley_34);
		mFaceMap.put("[折磨]", R.drawable.smiley_35);
		mFaceMap.put("[衰]", R.drawable.smiley_36);
		mFaceMap.put("[骷髅]", R.drawable.smiley_37);
		mFaceMap.put("[敲打]", R.drawable.smiley_38);
		mFaceMap.put("[再见]", R.drawable.smiley_39);
		mFaceMap.put("[擦汗]", R.drawable.smiley_40);
		mFaceMap.put("[抠鼻]", R.drawable.smiley_41);
		mFaceMap.put("[鼓掌]", R.drawable.smiley_42);
		mFaceMap.put("[糗大了]", R.drawable.smiley_43);
		mFaceMap.put("[坏笑]", R.drawable.smiley_44);
		mFaceMap.put("[左哼哼]", R.drawable.smiley_45);
		mFaceMap.put("[右哼哼]", R.drawable.smiley_46);
		mFaceMap.put("[哈欠]", R.drawable.smiley_47);
		mFaceMap.put("[鄙视]", R.drawable.smiley_48);
		mFaceMap.put("[委屈]", R.drawable.smiley_49);
		mFaceMap.put("[快哭了]", R.drawable.smiley_50);
		mFaceMap.put("[阴险]", R.drawable.smiley_51);
		mFaceMap.put("[亲亲]", R.drawable.smiley_52);
		mFaceMap.put("[吓]", R.drawable.smiley_53);
		mFaceMap.put("[可怜]", R.drawable.smiley_54);
		mFaceMap.put("[菜刀]", R.drawable.smiley_55);
		mFaceMap.put("[西瓜]", R.drawable.smiley_56);
		mFaceMap.put("[啤酒]", R.drawable.smiley_57);
		mFaceMap.put("[篮球]", R.drawable.smiley_58);
		mFaceMap.put("[乒乓]", R.drawable.smiley_59);
		mFaceMap.put("[咖啡]", R.drawable.smiley_60);
		mFaceMap.put("[饭]", R.drawable.smiley_61);
		mFaceMap.put("[猪头]", R.drawable.smiley_62);
		mFaceMap.put("[玫瑰]", R.drawable.smiley_63);
		mFaceMap.put("[凋谢]", R.drawable.smiley_64);
		mFaceMap.put("[男]", R.drawable.smiley_65);
		mFaceMap.put("[爱心]", R.drawable.smiley_66);
		mFaceMap.put("[心碎]", R.drawable.smiley_67);
		mFaceMap.put("[蛋糕]", R.drawable.smiley_68);
		mFaceMap.put("[闪电]", R.drawable.smiley_69);
		mFaceMap.put("[炸弹]", R.drawable.smiley_70);
		mFaceMap.put("[刀]", R.drawable.smiley_71);
		mFaceMap.put("[足球]", R.drawable.smiley_72);
		mFaceMap.put("[瓢虫]", R.drawable.smiley_73);
		mFaceMap.put("[便便]", R.drawable.smiley_74);
		mFaceMap.put("[月亮]", R.drawable.smiley_75);
		mFaceMap.put("[太阳]", R.drawable.smiley_76);
		mFaceMap.put("[礼物]", R.drawable.smiley_77);
		mFaceMap.put("[拥抱]", R.drawable.smiley_78);
		mFaceMap.put("[强]", R.drawable.smiley_79);
		mFaceMap.put("[弱]", R.drawable.smiley_80);
		mFaceMap.put("[握手]", R.drawable.smiley_81);
		mFaceMap.put("[胜利]", R.drawable.smiley_82);
		mFaceMap.put("[抱拳]", R.drawable.smiley_83);
		mFaceMap.put("[勾引]", R.drawable.smiley_84);
		mFaceMap.put("[拳头]", R.drawable.smiley_85);
		mFaceMap.put("[差劲]", R.drawable.smiley_86);
		mFaceMap.put("[爱你]", R.drawable.smiley_87);
		mFaceMap.put("[NO]", R.drawable.smiley_88);
		mFaceMap.put("[OK]", R.drawable.smiley_89);
		mFaceMap.put("[爱情]", R.drawable.smiley_90);
		mFaceMap.put("[飞吻]", R.drawable.smiley_91);
		mFaceMap.put("[跳跳]", R.drawable.smiley_92);
		mFaceMap.put("[发抖]", R.drawable.smiley_93);
		mFaceMap.put("[怄火]", R.drawable.smiley_94);
		mFaceMap.put("[转圈]", R.drawable.smiley_95);
		mFaceMap.put("[磕头]", R.drawable.smiley_96);
		mFaceMap.put("[回头]", R.drawable.smiley_97);
		mFaceMap.put("[跳绳]", R.drawable.smiley_98);
		mFaceMap.put("[挥手]", R.drawable.smiley_99);
		mFaceMap.put("[激动]", R.drawable.smiley_100);
		mFaceMap.put("[街舞]", R.drawable.smiley_101);
		mFaceMap.put("[献吻]", R.drawable.smiley_102);
		mFaceMap.put("[左太极]", R.drawable.smiley_103);
		mFaceMap.put("[右太极]", R.drawable.smiley_104);
	}

	public Map<String, Integer> getFaceMap() {
		if (!mFaceMap.isEmpty())
			return mFaceMap;
		return null;
	}

	/**
	 * 获取登录信息
	 * 
	 * @return
	 */
	public LoginBean getLoginbean() {
		return loginbean;
	}

	public void setLoginbean(LoginBean loginbean) {
		this.loginbean = loginbean;
	}

	public GameInfo getDownloadSuccess() {
		return downloadSuccess;
	}

	public void setDownloadSuccess(GameInfo downloadSuccess) {
		this.downloadSuccess = downloadSuccess;
	}

	public GameInfo getStartDownloadMovieItem() {
		return startDownloadMovieItem;
	}

	public void setStartDownloadMovieItem(GameInfo startDownloadMovieItem) {
		this.startDownloadMovieItem = startDownloadMovieItem;
	}

	// 设置相关参数
	private void setLocationOption() {
		LocationClientOption option = new LocationClientOption();
		option.setCoorType("bd09ll"); // 设置坐标类型
		option.setServiceName("com.baidu.location.service_v2.9");
		option.setPoiExtraInfo(true);
		option.setAddrType("all");
		option.setScanSpan(300000);
		option.setPoiNumber(10);
		option.disableCache(true);
		mLocationClient.setLocOption(option);
	}

	public void startLocation() {
		if (!mLocationClient.isStarted()) {
			mLocationClient.registerLocationListener(myListener);
			mLocationClient.start();
			mLocationClient.requestLocation();
		}
	}

	public void stopLocation() {
		if (mLocationClient.isStarted()) {
			mLocationClient.unRegisterLocationListener(myListener);
			mLocationClient.stop();
		}
	}
	
	/**
	 * 监听函数，又新位置的时候，格式化成字符串，输出到屏幕中
	 */
	public class MyLocationListenner implements BDLocationListener {
		@Override
		public void onReceiveLocation(BDLocation location) {
			if (location == null)
				return;
			String lng = "";
			String lat = "";
			mLocation = location;
			lat = location.getLatitude() + "";
			lng = location.getLongitude() + "";
			if (loginbean != null) {
				HashMap<String, Object> params = new HashMap<String, Object>();
				params.put("user_id", loginbean.user_id);
				params.put("token", loginbean.token);
				params.put("long", lng);
				params.put("lati", lat);
				UIDataProcess.reqData(UpdateLngLat.class, params, null,
						new IResponseListener() {

							@Override
							public void onSuccess(Object arg0) {
								System.out.println("定位结束");
							}

							@Override
							public void onRuning(Object arg0) {

							}

							@Override
							public void onReqStart() {

							}

							@Override
							public void onFinish() {

							}

							@Override
							public void onFailure(Object arg0) {

							}
						});
			}
		}

		@Override
		public void onReceivePoi(BDLocation poiLocation) {
			if (poiLocation == null) {
				return;
			}
			StringBuffer sb = new StringBuffer(256);
			sb.append("Poi time : ");
			sb.append(poiLocation.getTime());
			sb.append("\nerror code : ");
			sb.append(poiLocation.getLocType());
			sb.append("\nlatitude : ");
			sb.append(poiLocation.getLatitude());
			sb.append("\nlontitude : ");
			sb.append(poiLocation.getLongitude());
			sb.append("\nradius : ");
			sb.append(poiLocation.getRadius());
			if (poiLocation.getLocType() == BDLocation.TypeNetWorkLocation) {
				sb.append("\naddr : ");
				sb.append(poiLocation.getAddrStr());
			}
			if (poiLocation.hasPoi()) {
				sb.append("\nPoi:");
				sb.append(poiLocation.getPoi());
			} else {
				sb.append("noPoi information");
			}
		}
	}
	
	
	public ArrayList<ApkItem> getUpdateList() {
		return updateList;
	}

	public void addUpdate(ApkItem item) {
		if (this.updateList == null) {
			updateList = new ArrayList<ApkItem>();
		}
		updateList.add(item);
	}

	public void setUpdateList(ArrayList<ApkItem> updateList) {
		this.updateList = updateList;
	}

}
