package xiaokai.gushshit;

import java.util.HashMap;

import cn.nukkit.Player;
import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.command.data.CommandParameter;

/**
 * @author Winfxk
 */
public class MyCommand extends Command {
	private Gushshit ass;

	public MyCommand(Gushshit ass) {
		super("gush", "全服公告指令", "/gush");
		this.ass = ass;
		commandParameters = new HashMap<>();
		commandParameters.put("打开公告操作页面", new CommandParameter[] {});
		commandParameters.put("打开公告操作页面",
				new CommandParameter[] { new CommandParameter("查看扣费详情", false, new String[] { "money", "m" }) });
	}

	@Override
	public boolean execute(CommandSender sender, String commandLabel, String[] args) {
		if (args.length == 1 && args[0].toLowerCase().equals("help")) {
			sender.sendMessage("§9在游戏内执行命令§e“" + Tool.getColorFont("gush") + "§e”§9即可");
			return true;
		} else if (!sender.hasPermission("AssGushshit.Command.Main")) {
			sender.sendMessage("你咩有权限执行这个命令！");
			return true;
		} else if (!sender.isPlayer()) {
			sender.sendMessage("§4？？？在控制台直接用“/say”指令直接发公告不好嘛？");
			return true;
		} else if (args.length == 1 && (args[0].toLowerCase().equals("money") || args[0].toLowerCase().equals("m"))) {
			sender.sendMessage(ass.makeForm.getMoney());
			return true;
		}
		String string = "";
		for (String s : args)
			string += (string.isEmpty() ? "" : " ") + s;
		return ass.makeForm.MakeMain((Player) sender, string);
	}
}
