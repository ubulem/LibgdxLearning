package com.berkut;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

class Heart {
    Body Body;
    Sprite Sprite;
    Vector2 Size;
    String String;
    ParticleEffect ParticleEffect;
    Sound BreakSound;

    private Vector2 stringSize;
    private float deathAngle;
    private Vector2 deathPos;

    float getDeathAngle() {
        return deathAngle;
    }

    Vector2 getDeathPos() {
        return deathPos;
    }

    Heart(Body body, Sprite sprite, Vector2 size,
          String string, BitmapFont font, ParticleEffect particleEffect,
          Sound breakSound) {
        Body = body;
        Sprite = sprite;
        Size = size;
        String = string;
        GlyphLayout layout = new GlyphLayout();
        layout.setText(font, string);
        stringSize = new Vector2(
                layout.width * Size.x * Love.FontSizeHeartSizeFactor.x,
                layout.height * Size.y * Love.FontSizeHeartSizeFactor.y);
        ParticleEffect = particleEffect;
        BreakSound = breakSound;
    }

    Vector2 getStringSize() {
        return stringSize;
    }

    void destroy() {
        deathAngle = Body.getAngle();
        deathPos = Body.getPosition();
    }
}
