package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import com.github.epicnose.friendlypenetration.core.patches.base.ModPatcher;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.FieldInsnNode;
import org.objectweb.asm.tree.MethodInsnNode;
import org.objectweb.asm.tree.MethodNode;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;
import net.minecraftforge.classloading.FMLForgePlugin;

public class ScreenshotEnhancedPatcher extends ModPatcher {

    public ScreenshotEnhancedPatcher() {
        super("Screenshots Enhanced", "screenshots");
        this.classes.put("lotr.client.render.entity.LOTRRenderScrapTrader", (classNode) -> patchScrapTraderRender(classNode));
    }

    private void patchScrapTraderRender(ClassNode classNode) {
        MethodNode method = ASMUtils.findMethod(classNode, "doRender", "func_76986_a", "(Lnet/minecraft/entity/EntityLiving;DDDFF)V");
        if(method == null) return;

        // Find getKeyCode Method Call
        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node.getOpcode() == Opcodes.INVOKEVIRTUAL) {
                MethodInsnNode methodNode = (MethodInsnNode) node;

                if(methodNode.name.equals(FMLForgePlugin.RUNTIME_DEOBF ? "func_151463_i" : "getKeyCode") && methodNode.desc.equals("()I")) {
                    // Remove the vanilla key bind call
                    ASMUtils.removePreviousNodes(method.instructions, methodNode, 3);

                    FieldInsnNode keyCodeField = new FieldInsnNode(Opcodes.GETSTATIC, "net/undoredo/screenshots/KeyScreenshotListener", "screenshotKeyBinding", "Lnet/minecraft/client/settings/KeyBinding;");
                    method.instructions.insertBefore(methodNode, keyCodeField);
                    
                    break;
                }
            }
        }


        UCPCoreMod.log.info("Patched the Oddment Collector render to be compatible with Screenshots Enhanced.");
    }

}
