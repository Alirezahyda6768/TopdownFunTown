package com.rominntrenger.objects.player;

import com.bluebook.audio.AudioPlayer;
import com.bluebook.engine.GameEngine;
import com.bluebook.graphics.Sprite;
import com.bluebook.util.GameObject;
import com.bluebook.util.Vec2;
import com.rominntrenger.objects.Projectile;
import com.rominntrenger.objects.enemy.Enemy;

/**
 * Weapon class is used for weapons that the Player can equip and shoot with
 */
public abstract class Weapon extends GameObject {

    public Vec2 offset;
    double speed = 800;
    int dmg = 10;
    protected AudioPlayer audioPlayer = new AudioPlayer(testFil1);
    private static String testFil1 = "./assets/audio/scifi002.wav";
    protected String projectilePath = "/projectiles/projectile_gold_00";

    private long previousShotTime = 0;
    protected double shootInterval = 0.5;


    private Player holder;

    /**
     * Constructor for Weapon
     *
     * @param offset is the offset compared to the player.
     */
    public Weapon(Vec2 direction, Sprite sprite, Vec2 offset) {
        super(new Vec2(0, 23), direction, sprite);
        this.offset = offset;
    }

    public Vec2 getOffset() {
        return offset;
    }

    public void setOffset(Vec2 offset) {
        this.offset = offset;
    }

    @Override
    public void setDirection(Vec2 direction) {
        super.setDirection(Vec2.Vector2FromAngleInDegrees(direction.getAngleInDegrees() + 90));
    }


    /**
     * Shoot will shoot a projectile constructed in this class at the direction the player is faced
     */
    public boolean shoot() {
        if(System.currentTimeMillis() - previousShotTime >  shootInterval * 1000) {

            previousShotTime = System.currentTimeMillis();

            audioPlayer.setSpatial(this);
            audioPlayer.playOnce();
            // score -= 50;
            Vec2 angle = Vec2
                .Vector2FromAngleInDegrees(transform.getGlobalRotation().getAngleInDegrees() - 90);

            Vec2 spawnPosition = transform.getWorldPosition();

            Projectile p = new Projectile(spawnPosition,
                Vec2
                    .Vector2FromAngleInDegrees(
                        transform.getGlobalRotation().getAngleInDegrees() - 90),
                new Sprite(projectilePath));
            p.setSpeed(speed);
            p.setSource(holder);
            p.getCollider().addInteractionLayer("Block");
            p.getCollider().addInteractionLayer("Hittable");
            p.getCollider().addInteractionLayer("Walk");
            p.setOnCollisionListener(other -> {
                if (other.getGameObject() instanceof Player && other.getGameObject() != p
                    .getSource()) {
                    Player player = (Player) other.getGameObject();
                    player.hit(dmg);
                    if (GameEngine.DEBUG) {
                        System.out.println("Bullet Hit " + other.getName());
                    }
//                player.destroy();
                } else if (other.getGameObject() instanceof Enemy) {
                    Enemy e = ((Enemy)other.getGameObject());
                    e.hit(dmg);
                    if(e.isAlive()) {
                        ((Player) p.getSource()).killedEnemy();
                    }
                }
                p.destroy();

            });
            return true;
        }else
            return false;
    }

    public Player getHolder() {
        return holder;
    }

    public void setHolder(Player holder) {
        this.holder = holder;
    }
}