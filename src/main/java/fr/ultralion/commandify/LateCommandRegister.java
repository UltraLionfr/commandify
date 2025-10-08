package fr.ultralion.commandify;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.tree.RootCommandNode;
import fr.ultralion.commandify.commands.chat.CommandMsg;
import fr.ultralion.commandify.commands.chat.CommandReply;
import net.minecraft.commands.CommandSourceStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.RegisterCommandsEvent;

@EventBusSubscriber(modid = Commandify.MOD_ID)
public class LateCommandRegister {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        CommandDispatcher<CommandSourceStack> dispatcher = event.getDispatcher();
        RootCommandNode<CommandSourceStack> root = dispatcher.getRoot();

        // Remove vanilla /msg, /tell and /w commands to replace them with custom ones
        root.getChildren().removeIf(node -> {
            String name = node.getName();
            return name.equalsIgnoreCase("msg") || name.equalsIgnoreCase("tell") || name.equalsIgnoreCase("w");
        });


        CommandMsg.register(dispatcher);
        CommandReply.register(dispatcher);

        System.out.println("[Commandify] Vanilla commands /msg, /tell, and /w removed; replaced with Commandify versions.");
    }
}
