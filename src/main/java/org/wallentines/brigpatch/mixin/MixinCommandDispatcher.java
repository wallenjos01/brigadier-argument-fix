package org.wallentines.brigpatch.mixin;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.ParseResults;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.context.CommandContextBuilder;
import com.mojang.brigadier.context.ParsedArgument;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.tree.CommandNode;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Slice;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Mixin(value = CommandDispatcher.class, remap = false)
public class MixinCommandDispatcher<S> {

    // From https://github.com/Mojang/brigadier/pull/142/commits/11036b6345a5b3b5f7a34cadbc1b5e9eefbfddd6
    @Inject(method="parseNodes",
            locals= LocalCapture.CAPTURE_FAILHARD,
            at=@At(value="INVOKE", target="Lcom/mojang/brigadier/CommandDispatcher;parseNodes(Lcom/mojang/brigadier/tree/CommandNode;Lcom/mojang/brigadier/StringReader;Lcom/mojang/brigadier/context/CommandContextBuilder;)Lcom/mojang/brigadier/ParseResults;"),
            slice = @Slice(
                    from=@At(value="INVOKE", target="Lcom/mojang/brigadier/context/CommandContextBuilder;<init>(Lcom/mojang/brigadier/CommandDispatcher;Ljava/lang/Object;Lcom/mojang/brigadier/tree/CommandNode;I)V"),
                    to=@At(value="INVOKE", target="Lcom/mojang/brigadier/context/CommandContextBuilder;withChild(Lcom/mojang/brigadier/context/CommandContextBuilder;)Lcom/mojang/brigadier/context/CommandContextBuilder;")))

    private void onParse(CommandNode<S> node, StringReader originalReader, CommandContextBuilder<S> contextSoFar, CallbackInfoReturnable<ParseResults<S>> cir, Object source, Map<CommandNode<S>, CommandSyntaxException> errors, List<ParseResults<S>> potentials, int cursor, Iterator<CommandNode<S>> var8, CommandNode<S> child, CommandContextBuilder<S> context, StringReader reader, CommandContextBuilder<S> childContext) {
        for(Map.Entry<String, ParsedArgument<S, ?>> ent : context.getArguments().entrySet()) {
            childContext.withArgument(ent.getKey(), ent.getValue());
        }
    }

}
