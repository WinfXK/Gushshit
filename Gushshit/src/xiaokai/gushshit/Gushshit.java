package xiaokai.gushshit;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import cn.nukkit.Player;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerFormRespondedEvent;
import cn.nukkit.form.response.FormResponseCustom;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;
import cn.nukkit.utils.Utils;

/**
 * @author Winfxk
 */
public class Gushshit extends PluginBase implements Listener {
	private Instant loadTime = Instant.now();
	protected static Config config;
	protected MakeForm makeForm;
	public int FormID;
	public Gushshit PY;

	/**
	 * 表单响应事件
	 * 
	 * @param e
	 */
	@EventHandler
	public void onPlayerForm(PlayerFormRespondedEvent e) {
		if (e.getFormID() != FormID)
			return;
		Player player = e.getPlayer();
		if (player == null || e.wasClosed() || e.getResponse() == null
				|| !(e.getResponse() instanceof FormResponseCustom))
			return;
		try {
			makeForm.Monitor(player, (FormResponseCustom) e.getResponse());
		} catch (Exception e1) {
			player.sendMessage("插件运行出现问题！请联系服务器管理员！" + e1.getMessage());
			e1.printStackTrace();
		}
	}

	/**
	 * 明人不说暗话！这就是插件启动事件
	 */
	@Override
	public void onEnable() {
		Instant EnableTime = Instant.now();
		PY = this;
		if (!getDataFolder().exists())
			getDataFolder().mkdirs();
		File file = new File(getDataFolder(), "Config.yml");
		if (!file.exists())
			try {
				Utils.writeFile(file, this.getClass().getResourceAsStream("/resources/Config.yml"));
			} catch (IOException e) {
				e.printStackTrace();
				getLogger().error("§4资源初始化已经失败！插件数据可能出现问题！请检查！");
			}
		getServer().getPluginManager().registerEvents(this, this);
		config = new Config(file, Config.YAML);
		FormID = config.getInt("表单ID");
		this.getServer().getCommandMap().register(getName(), new MyCommand(this));
		makeForm = new MakeForm(this);
		float entime = ((Duration.between(loadTime, Instant.now()).toMillis()));
		String onEnableString = (entime > 1000 ? ((entime / 1000) + "§6s!(碉堡了) ") : entime + "§6ms");
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "启动！") + "§6总耗时:§9" + onEnableString
				+ " 启动耗时:§9" + ((float) (Duration.between(EnableTime, Instant.now()).toMillis())) + "§6ms");
		if (Tool.getRand(1, 5) == 1)
			getLogger().info(Tool.getColorFont("本插件完全免费，如果你是给钱了的，那你就可能被坑啦~"));
	}

	/**
	 * 返回货币的名称，如“金币”
	 * 
	 * @return
	 */
	public static String getMoneyName() {
		return config.getString("货币单位");
	}

	/**
	 * ????这都看不懂？？这是插件关闭事件
	 */
	@Override
	public void onDisable() {
		this.getServer().getLogger()
				.info(Tool.getColorFont(this.getName() + "关闭！") + TextFormat.GREEN + "本次运行时长" + TextFormat.BLUE
						+ Tool.getTimeBy(((float) (Duration.between(loadTime, Instant.now()).toMillis()) / 1000)));
		super.onDisable();
	}

	/**
	 * PY已准备好！插件加载事件
	 */
	@Override
	public void onLoad() {
		this.getServer().getLogger().info(Tool.getColorFont(this.getName() + "正在加载..."));
	}
}
