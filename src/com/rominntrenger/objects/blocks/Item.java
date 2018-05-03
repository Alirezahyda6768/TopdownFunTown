package com.rominntrenger.objects.blocks;

import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;

public class Item extends GameObject {

    /**
     * Constructor for GameObject given position rotation and sprite
     */
    public Item(Vec2 position, Vec2 direction, Sprite sprite) {
        super(position, direction, sprite);
    }

    public Item createNew(Vec2 pos) {
        return new Item(pos, (Vec2)direction.clone(), new Sprite(getSprite().getPath()));
    }
}

