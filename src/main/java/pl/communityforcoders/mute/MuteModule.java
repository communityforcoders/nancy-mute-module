package pl.communityforcoders.mute;

import java.util.List;
import net.dv8tion.jda.core.Permission;
import net.dv8tion.jda.core.entities.ChannelType;
import net.dv8tion.jda.core.entities.Guild;
import net.dv8tion.jda.core.entities.Member;
import net.dv8tion.jda.core.entities.MessageEmbed.Field;
import net.dv8tion.jda.core.entities.Role;
import net.dv8tion.jda.core.entities.TextChannel;
import net.dv8tion.jda.core.entities.User;
import pl.communityforcoders.nancy.Nancy;
import pl.communityforcoders.nancy.command.annotation.CommandManifest;
import pl.communityforcoders.nancy.command.context.CommandContext;
import pl.communityforcoders.nancy.module.annotation.ModuleManifest;
import pl.communityforcoders.nancy.module.annotation.OnDisable;
import pl.communityforcoders.nancy.module.annotation.OnEnable;
import pl.communityforcoders.nancy.util.EmbedUtils;

@ModuleManifest(name = "MuteModule", author = "kacperduras", version = "1.0.0.0")
public class MuteModule {

  private Guild guild;

  @OnEnable
  public void onEnable(Nancy nancy) {
    guild = nancy.getJDA().getGuildById("396018831434186762");
  }

  @OnDisable
  public void onDisable() {

  }

  @CommandManifest(name = "!mute", type = ChannelType.TEXT)
  public void muteCommand(User user, TextChannel channel, CommandContext context) {
    Member member = guild.getMember(user);

    if (!member.hasPermission(Permission.KICK_MEMBERS)) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd", "Nie posiadasz uprawnień do tej komendy!", true))).queue();
      return;
    }

    if (context.getMentionedUsers().size() == 0) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd", "Poprawne użycie: !mute <mentions>!", true))).queue();
      return;
    }

    List<Role> role = guild.getRolesByName("mute", true);
    if (role.size() == 0) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd wewnętrzny", "Skontaktuj się z administracją serwera.", true))).queue();
      return;
    }

    long count = context.getMentionedUsers().stream()
        .map(guild::getMember)
        .filter(target -> !target.getRoles().contains(role.get(0)))
        .peek(target -> guild.getController().addRolesToMember(target, role.get(0)).queue())
        .count();

    channel.sendMessage(EmbedUtils.agree(new Field("Gotowe", "Wyciszenia zostały nałożone na " + (count == 0 ? "osób" : "osoby"), true))).queue();
  }

  @CommandManifest(name = "!unmute", type = ChannelType.TEXT)
  public void unmuteCommand(User user, TextChannel channel, CommandContext context) {
    Member member = guild.getMember(user);

    if (!member.hasPermission(Permission.KICK_MEMBERS)) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd", "Nie posiadasz uprawnień do tej komendy!", true))).queue();
      return;
    }

    if (context.getMentionedUsers().size() == 0) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd", "Poprawne użycie: !unmute <mentions>!", true))).queue();
      return;
    }

    List<Role> role = guild.getRolesByName("mute", true);
    if (role.size() == 0) {
      channel.sendMessage(EmbedUtils.error(new Field("Błąd wewnętrzny", "Skontaktuj się z administracją serwera.", true))).queue();
      return;
    }

    long count = context.getMentionedUsers().stream()
        .map(guild::getMember)
        .filter(target -> target.getRoles().contains(role.get(0)))
        .peek(target -> guild.getController().removeRolesFromMember(target, role.get(0)).queue())
        .count();

    channel.sendMessage(EmbedUtils.agree(new Field("Gotowe", "Wyciszenia zostały zdjęte z " + ((count == 0 || count == 1) ? "osób" : "osoby"), true))).queue();
  }

}
