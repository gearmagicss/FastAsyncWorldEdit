package com.fastasyncworldedit.core.function.pattern;

import com.sk89q.worldedit.WorldEditException;
import com.sk89q.worldedit.extent.Extent;
import com.sk89q.worldedit.extent.clipboard.Clipboard;
import com.sk89q.worldedit.function.pattern.AbstractPattern;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.math.Vector3;
import com.sk89q.worldedit.math.transform.AffineTransform;
import com.sk89q.worldedit.math.transform.Transform;
import com.sk89q.worldedit.session.ClipboardHolder;
import com.sk89q.worldedit.world.block.BaseBlock;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

import static com.google.common.base.Preconditions.checkNotNull;

public class RandomFullClipboardPattern extends AbstractPattern {

    private final Extent extent;
    private final List<ClipboardHolder> clipboards;
    private final boolean randomRotate;
    private final boolean randomFlip;
    private final Vector3 flipVector = Vector3.at(1, 0, 0).multiply(-2).add(1, 1, 1);

    public RandomFullClipboardPattern(Extent extent, List<ClipboardHolder> clipboards, boolean randomRotate, boolean randomFlip) {
        checkNotNull(clipboards);
        this.clipboards = clipboards;
        this.extent = extent;
        this.randomRotate = randomRotate;
        this.randomFlip = randomFlip;
    }

    @Override
    public boolean apply(Extent extent, BlockVector3 get, BlockVector3 set) throws WorldEditException {
        ClipboardHolder holder = clipboards.get(ThreadLocalRandom.current().nextInt(clipboards.size()));
        AffineTransform transform = new AffineTransform();
        if (randomRotate) {
            transform = transform.rotateY(ThreadLocalRandom.current().nextInt(4) * 90);
            holder.setTransform(new AffineTransform().rotateY(ThreadLocalRandom.current().nextInt(4) * 90));
        }
        if (randomFlip) {
            transform = transform.scale(flipVector);
        }
        if (!transform.isIdentity()) {
            holder.setTransform(transform);
        }
        Clipboard clipboard = holder.getClipboard();
        Transform newTransform = holder.getTransform();
        if (newTransform.isIdentity()) {
            clipboard.paste(extent, set, false);
        } else {
            clipboard.paste(extent, set, false, newTransform);
        }
        return true;
    }

    @Override
    public BaseBlock applyBlock(BlockVector3 position) {
        throw new IllegalStateException("Incorrect use. This pattern can only be applied to an extent!");
    }

}
