package com.github.epicnose.friendlypenetration.core.patches;

import com.github.epicnose.friendlypenetration.core.UCPCoreMod;
import com.github.epicnose.friendlypenetration.core.patches.base.ModPatcher;
import org.objectweb.asm.tree.AbstractInsnNode;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.TypeInsnNode;
import com.github.epicnose.friendlypenetration.core.utils.ASMUtils;

public class BotaniaPatcher extends ModPatcher {

    public BotaniaPatcher() {
        super("Botania", "Botania");
        this.classes.put("vazkii.botania.common.block.subtile.generating.SubTileKekimurus", (classNode) -> patchKekimurus(classNode));
    }

    private void patchKekimurus(ClassNode classNode) {
        // Not a vanilla classes so no obfuscation.
        MethodNode method = ASMUtils.findMethod(classNode, "onUpdate", "()V");
        if(method == null) return;

        for(AbstractInsnNode node : method.instructions.toArray()) {
            if(node instanceof TypeInsnNode) {
                TypeInsnNode typeNode = (TypeInsnNode) node;

                if(typeNode.desc.equals("net/minecraft/block/BlockCake")) {
                    typeNode.desc = "lotr/common/block/LOTRBlockPlaceableFood";
                    break;
                }
            }
        }

        UCPCoreMod.log.info("Patched the Kekimurus to eat all LOTR cakes.");
    }

}
