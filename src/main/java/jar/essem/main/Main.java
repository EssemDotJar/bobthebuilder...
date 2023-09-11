package jar.essem.main;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.*;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.Vector;

import java.util.Random;

public class Main extends JavaPlugin implements Listener, CommandExecutor {

	@Override
	public void onEnable() {
		Bukkit.getConsoleSender().sendMessage("Started!");

		PluginManager pm = Bukkit.getPluginManager();
		pm.registerEvents(this, this);
	}

	@EventHandler
	public void onEntityAttackEntity(EntityDamageByEntityEvent event) {
		if (event.getEntity() instanceof Zombie) {
			if (!(event.getDamager() instanceof Player)) return;

			summonBarrageFromAbove((Player) event.getDamager());
		}
	}


	public void summonBarrageFromAbove(Player player) {

		Location location = player.getLocation();

		World world = location.getWorld();
		double x = location.getX();
		double y = location.getY() + 100;
		double z = location.getZ();

		EntityType type = EntityType.WARDEN;

		for (int i = 0; i < 10; i++) {
			world.spawnEntity(new Location(world, x, y, z), type);
			world.spawnEntity(new Location(world, x + 1, y, z), type);
			world.spawnEntity(new Location(world, x + 1, y, z + 1), type);
			world.spawnEntity(new Location(world, x, y, z + 1), type);
			world.spawnEntity(new Location(world, x - 1, y, z), type);
			world.spawnEntity(new Location(world, x - 1, y, z - 1), type);
			world.spawnEntity(new Location(world, x, y, z - 1), type);
			world.spawnEntity(new Location(world, x + 1, y, z - 1), type);
			world.spawnEntity(new Location(world, x - 1, y, z + 1), type);
		}
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		Location location = player.getLocation();

		if (totem) {
			event.getPlayer().getInventory().setItemInOffHand(new ItemStack(Material.TOTEM_OF_UNDYING));
			event.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.TOTEM_OF_UNDYING));
		}
		if (on) {
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();


			if (x <= loc1.getX() && x >= loc2.getX()) {
				if (y <= loc1.getY() && y >= loc2.getY()) {
					if (z <= loc1.getZ() && z >= loc2.getZ()) {
						return;
					}
				}
			}


			loc1 = new Location(player.getWorld(), x + amount, y + amount, z + amount);
			loc2 = new Location(player.getWorld(), x - amount, y - amount, z - amount);

			int i = 0;
			for (i = 0; i <= times; i++) {
				spawnEntity(location);
			}

		}
	}

	public void spawnEntity(Location location) {
		EntityType[] types = EntityType.values();

		Random random = new Random();
		int i = random.nextInt(types.length - 1);

		if (spawncrazy && !spawnwarden) {
			EntityType[] crazytypes = {EntityType.WARDEN, EntityType.WITHER, EntityType.BLAZE,
					EntityType.CREEPER, EntityType.ENDER_CRYSTAL};
			int j = random.nextInt(crazytypes.length - 1);
			location.getWorld().spawnEntity(location, crazytypes[j]);
		} else if (spawnwarden) {
			location.getWorld().spawnEntity(location, EntityType.WARDEN);
		} else {
			location.getWorld().spawnEntity(location, types[i]);
		}
	}

	@EventHandler
	public void onDamage(EntityDamageEvent event) {

		if (event.getEntity() instanceof EnderDragon) {
			((EnderDragon) event.getEntity()).setHealth(0);
		}


		if (!(event.getEntity() instanceof Player)) return;

		Player player = (Player) event.getEntity();

		if (player.getHealth() - event.getDamage() < 1) {

		}
		if (event.getEntity() instanceof Fox) {
			Location location = event.getEntity().getLocation();
			Bukkit.getConsoleSender().sendMessage("fuck");
			location.getWorld().createExplosion(location, 100);
		}
		Bukkit.getConsoleSender().sendMessage("fuck");
	}

	public static Location loc1 = new Location(Bukkit.getWorld("world"), 0, 0, 0);
	public static Location loc2 = new Location(Bukkit.getWorld("world"), 0, 0, 0);

	public static boolean on = false;
	public static boolean spawncrazy = false;
	public boolean spawnwarden = false;

	public static boolean totem = false;

	public static double amount = 1.5;

	public static int times = 1;

	@EventHandler
	public void onChat(AsyncPlayerChatEvent event) {
		Player player = event.getPlayer();
		if (event.getMessage().equals("on")) {
			on = true;
		} else if (event.getMessage().equals("off")) {
			on = false;
		} else if (event.getMessage().equals("location")) {
			locationiti = player.getLocation();
		} else if (event.getMessage().equals("paper")) {
			itemSpawnFromPaperHaHa(player);
		} else if (event.getMessage().equals("crazy")) {
			spawncrazy = !spawncrazy;
		} else if (event.getMessage().equals("warden")) {
			spawnwarden = !spawnwarden;
		} else if (event.getMessage().equals("totem")) {
			totem = !totem;
		} else if (event.getMessage().equals("here")) {
			Location location = player.getLocation();
			double x = location.getX();
			double y = location.getY();
			double z = location.getZ();
			loc1 = new Location(player.getWorld(), x + amount, y + amount, z + amount);
			loc2 = new Location(player.getWorld(), x - amount, y - amount, z - amount);
		} else if (event.getMessage().equals("up")) {
			times += 1;
		} else if (event.getMessage().equals("down")) {
			times -= 1;
		} else if (event.getMessage().equals("times")) {
			player.sendMessage("- " + times);
		}
	}

	public void itemSpawnFromPaperHaHa(Player player) {

		Location location = player.getLocation();
		location.setX(location.getX() + 3);
		location.setZ(location.getZ() + 0.5);
		location.setY(location.getY() + 5);
		World world = location.getWorld();
		new BukkitRunnable() {
			int cunt = 0;

			@Override
			public void run() {
				cunt++;
				if (cunt == 3) {
					ItemStack item = new ItemStack(Material.BEDROCK, 64);
					world.dropItem(location, item);
					item = new ItemStack(Material.NETHERITE_CHESTPLATE);
					item.addEnchantment(Enchantment.MENDING, 1);
					item.addEnchantment(Enchantment.DURABILITY, 1);
					item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					world.dropItem(location, item);

					item = new ItemStack(Material.TOTEM_OF_UNDYING);
					world.dropItem(location, item);

					item = new ItemStack(Material.NETHERITE_BOOTS);
					item.addEnchantment(Enchantment.MENDING, 1);
					item.addEnchantment(Enchantment.DURABILITY, 1);
					item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					item.addEnchantment(Enchantment.PROTECTION_FALL, 1);
					item.addEnchantment(Enchantment.DEPTH_STRIDER, 1);
					world.dropItem(location, item);
					item = new ItemStack(Material.NETHERITE_LEGGINGS);
					item.addEnchantment(Enchantment.MENDING, 1);
					item.addEnchantment(Enchantment.DURABILITY, 1);
					item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					world.dropItem(location, item);
					item = new ItemStack(Material.NETHERITE_HELMET);
					item.addEnchantment(Enchantment.MENDING, 1);
					item.addEnchantment(Enchantment.DURABILITY, 1);
					item.addEnchantment(Enchantment.PROTECTION_ENVIRONMENTAL, 1);
					item.addEnchantment(Enchantment.OXYGEN, 1);
					world.dropItem(location, item);

					item = new ItemStack(Material.BEDROCK, 64);
					world.dropItem(location, item);
					world.dropItem(location, item);
					world.dropItem(location, item);
					item = new ItemStack(Material.TOTEM_OF_UNDYING);
					world.dropItem(location, item);
					world.dropItem(location, item);
					item = new ItemStack(Material.ARROW, 64);
					world.dropItem(location, item);
					world.dropItem(location, item);
					world.dropItem(location, item);
					item = new ItemStack(Material.BOW);
					item.addEnchantment(Enchantment.MENDING, 1);
					item.addEnchantment(Enchantment.DURABILITY, 1);
					item.addEnchantment(Enchantment.ARROW_DAMAGE, 1);
					item.addEnchantment(Enchantment.ARROW_FIRE, 1);
					world.dropItem(location, item);
					item = new ItemStack(Material.SHIELD);
					item.addEnchantment(Enchantment.MENDING, 1);
					item.addEnchantment(Enchantment.DURABILITY, 1);
					world.dropItem(location, item);
					item = new ItemStack(Material.NETHERITE_PICKAXE);
					item.addEnchantment(Enchantment.MENDING, 1);
					item.addEnchantment(Enchantment.DURABILITY, 1);
					item.addEnchantment(Enchantment.DIG_SPEED, 1);
					world.dropItem(location, item);
					item = new ItemStack(Material.ENCHANTED_GOLDEN_APPLE, 64);
					world.dropItem(location, item);
					world.dropItem(location, item);
					world.dropItem(location, item);
					world.dropItem(location, item);
				}
			}
		}.runTaskTimer(this, 0, 20);
	}

	@EventHandler
	public void onDrop(EntityDeathEvent event) {
		Entity entity = event.getEntity();
		if (entity instanceof Enderman) {
			Location location = entity.getLocation();
			World world = location.getWorld();
			world.dropItemNaturally(location, new ItemStack(Material.ENDER_PEARL, 2048));
		}
		if (entity instanceof Blaze) {
			Location location = entity.getLocation();
			World world = location.getWorld();
			world.dropItemNaturally(location, new ItemStack(Material.BLAZE_ROD, 2048));
		}
	}


	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

		if (!(sender instanceof Player player)) return false;

		if (label.equalsIgnoreCase("buy")) {

		}

		return false;
	}

	@EventHandler
	public void onEntitySpawn(EntitySpawnEvent event) {
		if (event.getEntity() instanceof Pig pig) {

			Bukkit.getConsoleSender().sendMessage("fuck");
			Player player = Bukkit.getPlayer("EssemCSH");

			pig.setArrowsInBody(3);
			pig.setCustomName("Brian");
			pig.setGlowing(true);
			pig.setRotation(30, 0);

		} else if (event.getEntity() instanceof Goat goat) {
			goat.setAI(false);
			goat.setArrowsInBody(3);
			goat.setCustomName("Alberto Einstein");
			goat.setGlowing(true);
			goat.setRotation(360, 90);
		}
	}

	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (event.getItem().getType() == Material.TOTEM_OF_UNDYING) {

		}
	}

	public static Location locationiti;

	public void velocityFuckery(Player player) {
		new BukkitRunnable() {
			int cunt = 0;
			World world = player.getWorld();
			EntityType[] entities = EntityType.values();
			Random random = new Random();

			@Override
			public void run() {
				Location location = player.getLocation();

				if (cunt <= 100) {
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);
					location.getWorld().spawnEntity(location, entities[random.nextInt(entities.length - 1)]);

					world.createExplosion(location, 10);
					player.addPotionEffect(new PotionEffect(PotionEffectType.CONFUSION, 10, 100));

					launchCrazyStupidRandomVelocity(player);
				} else {
					cancel();
				}
				cunt++;
			}
		}.runTaskTimer(this, 0, 1);
	}

	public void launchCrazyStupidRandomVelocity(Player player) {

		Random random = new Random();

		double x = random.nextInt(1, 10);
		double y = random.nextInt(1, 10);
		double z = random.nextInt(1, 10);

		if (!player.isOnGround()) {
			int y_ = random.nextInt(1, 3);

			if (y_ == 1) {
				y *= -1;
			}
		}

		int x_ = random.nextInt(1, 3);
		if (x_ == 1) {
			x *= -1;
		}

		int z_ = random.nextInt(1, 3);
		if (z_ == 1) {
			z *= -1;
		}

		Vector vector = new Vector(x, y, z);
		player.setVelocity(vector);
	}

	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		event.setDeathMessage("fuck you");
	}
}
