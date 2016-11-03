package com.berkut;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;

public class Heart {
    public Body Body;
    public Sprite Sprite;
    public Vector2 Size;
    public String String;
    public BitmapFont Font;
    public ParticleEffect ParticleEffect;
    public Sound BreakSound;

    protected Vector2 stringSize;
    protected float deathAngle;
    protected Vector2 deathPos;

    public float getDeathAngle() {
        return deathAngle;
    }

    public Vector2 getDeathPos() {
        return deathPos;
    }

    public Heart(Body body, Sprite sprite, Vector2 size,
                 String string, BitmapFont font, ParticleEffect particleEffect,
                 Sound breakSound) {
        Body = body;
        Sprite = sprite;
        Size = size;
        String = string;
        Font = font;
        GlyphLayout layout = new GlyphLayout();
        layout.setText(Font, string);
        stringSize = new Vector2(
                layout.width * Size.x * Love.FontSizeHeartSizeCoef.x,
                layout.height * Size.y * Love.FontSizeHeartSizeCoef.y);
        ParticleEffect = particleEffect;
        BreakSound = breakSound;
    }

    public Vector2 getStringSize() {
        return stringSize;
    }

    public void destroy() {
        deathAngle = Body.getAngle();
        deathPos = Body.getPosition();
    }
}
