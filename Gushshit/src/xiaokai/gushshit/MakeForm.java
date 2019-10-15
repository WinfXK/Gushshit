package xiaokai.gushshit;

import java.util.Map;
import java.util.UUID;

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import me.onebone.economyapi.EconomyAPI;

/**
 * @author Winfxk
 */
public class MakeForm {
	private Gushshit ass;

	public MakeForm(Gushshit assGushshit) {
		ass = assGushshit;
	}

	public boolean Monitor(Player player, FormResponseCustom data) {
		Config config = Gushshit.config;
		String msg = data.getInputResponse(1);
		double Moneyadd = 1;
		int ColorType = data.getDropdownResponse(2).getElementID();
		int MsgType = data.getDropdownResponse(3).getElementID();
		int MsgTime = Tool.ObjectToInt(data.getSliderResponse(4), Gushshit.config.getInt("Title类型显示时间最小值"));
		if (msg == null || msg.isEmpty() || (ColorType == 0 && TextFormat.clean(msg).isEmpty())) {
			player.sendMessage("请输入想要公告的内容！");
			return false;
		}
		double Money = config.getDouble("每个字的价格") * TextFormat.clean(msg).length() + config.getDouble("每句话的价格");
		switch (ColorType) {
		case 0:
			msg = TextFormat.clean(msg);
			break;
		case 1:
			Moneyadd = Moneyadd + config.getDouble("自定义颜色倍率");
			break;
		case 2:
			Moneyadd = Moneyadd + config.getDouble("随机颜色倍率");
			msg = Tool.getRandColor() + msg;
			break;
		case 3:
			Moneyadd = Moneyadd + config.getDouble("RGB色倍率");
			msg = Tool.getColorFont(msg);
			break;
		}
		switch (MsgType) {
		case 0:
			Moneyadd = Moneyadd + config.getDouble("消息类型价格倍率");
			break;
		case 1:
			Moneyadd = Moneyadd + config.getDouble("Tip类型价格倍率");
			break;
		case 2:
			Moneyadd = Moneyadd + config.getDouble("Popup类型价格倍率");
			break;
		case 3:
			Moneyadd = Moneyadd + config.getDouble("Title类型价格倍率") + MsgTime
					- Gushshit.config.getInt("Title类型显示时间最小值");
			break;
		}
		if (Money * Moneyadd > EconomyAPI.getInstance().myMoney(player)) {
			player.sendMessage("§4本次公告将消耗§5" + (Money * Moneyadd) + "§4" + Gushshit.getMoneyName() + "！您的"
					+ Gushshit.getMoneyName() + "不足！");
			return false;
		}
		EconomyAPI.getInstance().reduceMoney(player, Money * Moneyadd);
		Server server = Server.getInstance();
		if (MsgType != 0) {
			Map<UUID, Player> Players = server.getOnlinePlayers();
			for (UUID id : Players.keySet()) {
				player = Players.get(id);
				if (player.isOnline())
					switch (MsgType) {
					case 1:
						player.sendTitle("§e[§9" + player.getName() + "§e]§f： §r" + msg);
						break;
					case 2:
						player.sendPopup("§e[§9" + player.getName() + "§e]§f： §r" + msg);
						break;
					case 3:
						player.sendTitle(msg, "§e[§9" + player.getName() + "§e]", 20, MsgTime * 20, 20);
						break;
					}
			}
		} else
			server.broadcastMessage("§e[§9" + player.getName() + "§e]§f： §r" + msg);
		player.sendMessage("§6消息发送成功！本次扣费：§e" + Money * Moneyadd);
		return true;
	}

	/**
	 * 创建主页面
	 * 
	 * @param player
	 * @param d
	 * @return
	 */
	public boolean MakeMain(Player player, String d) {
		CustomForm form = new CustomForm(ass.FormID, "公告提示");
		form.addLabel("§e使用前应使用§f“§6/gush money§f”§e来查看相关费用");
		form.addInput("请输入想要公告的内容", d == null ? "" : d, "请输入想要公告的内容");
		form.addDropdown("公告内容的颜色",
				new String[] { "无色彩", "自定义", Tool.getRandColor() + "随机颜色", Tool.getColorFont("RGB即正义！") });
		form.addDropdown("请选择公告方式", new String[] { "Msg", "Tip", "Popup", "Title" });
		form.addSlider("请选择Title显示时间（非Title可忽略该项）", Gushshit.config.getInt("Title类型显示时间最小值"),
				Gushshit.config.getInt("Title类型显示时间最大值"), 1);
		form.sendPlayer(player);
		return true;
	}

	public String getMoney() {
		String string = "";
		Map<String, Object> map = Gushshit.config.getAll();
		for (String ike : map.keySet()) {
			if (ike.equals("货币单位") || ike.equals("表单ID"))
				continue;
			String co = Tool.getRandColor();
			try {
				string += (string.isEmpty() ? "" : "\n") + co + ike + "§f：  " + co + Gushshit.config.getDouble(ike);
			} catch (Exception e) {
			}
		}
		return string;
	}
};