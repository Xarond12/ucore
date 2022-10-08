package io.xarond.ucore.entities;

import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.IntSet;
import io.xarond.ucore.function.TileCollider;
import io.xarond.ucore.function.TileHitboxProvider;
import io.xarond.ucore.util.Mathf;
import io.xarond.ucore.util.Physics;
import io.xarond.ucore.util.QuadTree;

public class EntityCollisions {
    //range for tile collision scanning
    private static final int r = 2;
    //move in 1-unit chunks
    private static final float seg = 1f;

    //tile collisions
    private float tilesize;
    private Rectangle tmp = new Rectangle();
    private TileCollider collider;
    private TileHitboxProvider hitboxProvider;
    private Vector2 vector = new Vector2();
    private Vector2 l1 = new Vector2();
    private Vector2 l2 = new Vector2();
    private Rectangle r1 = new Rectangle();
    private Rectangle r2 = new Rectangle();
    private Rectangle r3 = new Rectangle();

    //entity collisions
    private IntSet collided = new IntSet();

    public void setCollider(float tilesize, TileCollider collider, TileHitboxProvider hitbox){
        this.tilesize = tilesize;
        this.collider = collider;
        this.hitboxProvider = hitbox;
    }

    public void setCollider(float tilesize, TileCollider collider){
        setCollider(tilesize, collider, (x, y, out) -> out.setSize(tilesize).setCenter(x*tilesize, y*tilesize));
    }

    public void move(SolidEntity entity, float deltax, float deltay){

        while(Math.abs(deltax) > 0){
            moveInternal(entity, Math.min(Math.abs(deltax), seg) * Mathf.sign(deltax), 0, true);

            if(Math.abs(deltax) >= seg) {
                deltax -= seg * Mathf.sign(deltax);
            }else{
                deltax = 0f;
            }
        }

        while(Math.abs(deltay) > 0){
            moveInternal(entity, 0, Math.min(Math.abs(deltay), seg) * Mathf.sign(deltay), false);

            if(Math.abs(deltay) >= seg) {
                deltay -= seg * Mathf.sign(deltay);
            }else{
                deltay = 0f;
            }
        }
    }

    public void moveInternal(SolidEntity entity, float deltax, float deltay, boolean x){
        if(collider == null)
            throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

        Hitbox box = entity.hitboxTile;
        Rectangle rect = box.getRect(entity.x + deltax, entity.y + deltay);

        int tilex = Mathf.scl2(rect.x + rect.width/2, tilesize), tiley = Mathf.scl2(rect.y + rect.height/2, tilesize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx+tilex, wy = dy+tiley;
                if(collider.solid(wx, wy)){

                    hitboxProvider.getHitbox(wx, wy, tmp);

                    if(tmp.overlaps(rect)){
                        Vector2 v = Physics.overlap(rect, tmp);
                        if(x) rect.x += v.x;
                        if(!x) rect.y += v.y;
                    }
                }
            }
        }

        entity.x = rect.x + box.width / 2 - box.offsetx;
        entity.y = rect.y + box.height / 2 - box.offsety;
    }

    public boolean overlapsTile(Rectangle rect){
        if(collider == null)
            throw new IllegalArgumentException("No tile collider specified! Call setCollider() first.");

        rect.getCenter(vector);
        int r = 1;

        //assumes tiles are centered
        int tilex = Mathf.scl2(vector.x, tilesize);
        int tiley = Mathf.scl2(vector.y, tilesize);

        for(int dx = -r; dx <= r; dx++){
            for(int dy = -r; dy <= r; dy++){
                int wx = dx + tilex, wy = dy + tiley;
                if(collider.solid(wx, wy)){
                    hitboxProvider.getHitbox(wx, wy, r2);

                    if(r2.overlaps(rect)){
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public void updatePhysics(EntityGroup<?> group){
        collided.clear();

        QuadTree<SolidEntity> tree = group.tree();

        tree.clear();

        for(Entity entity : group.all()){
            if(entity instanceof SolidEntity){
                SolidEntity s = (SolidEntity)entity;
                s.lastX = s.x;
                s.lastY = s.y;
                tree.insert(s);
            }
        }
    }

    private void checkCollide(Entity entity, Entity other){
        SolidEntity a = (SolidEntity) entity;
        SolidEntity b = (SolidEntity) other;

        Rectangle r1 = a.hitbox.getRect(this.r1, a.lastX, a.lastY);
        Rectangle r2 = b.hitbox.getRect(this.r2, b.lastX, b.lastY);

        float vax = a.x - a.lastX;
        float vay = a.y - a.lastY;
        float vbx = b.x - b.lastX;
        float vby = b.y - b.lastY;

        if(a != b && a.collides(b) && b.collides(a)){
            l1.set(a.x, a.y);
            boolean collide = r1.overlaps(r2) || collide(r1.x, r1.y, r1.width, r1.height, vax, vay,
                    r2.x, r2.y, r2.width, r2.height, vbx, vby, l1);
            if(collide) {
                a.collision(b, l1.x, l1.y);
                b.collision(a, l1.x, l1.y);
            }
        }
    }

    private boolean collide(float x1, float y1, float w1, float h1, float vx1, float vy1,
                            float x2, float y2, float w2, float h2, float vx2, float vy2, Vector2 out){
        float px = vx1, py = vy1;

        vx1 -= vx2;
        vy1 -= vy2;

        float xInvEntry, yInvEntry;
        float xInvExit, yInvExit;

        if (vx1 > 0.0f) {
            xInvEntry = x2 - (x1 + w1);
            xInvExit = (x2 + w2) - x1;
        } else {
            xInvEntry = (x2 + w2) - x1;
            xInvExit = x2 - (x1 + w1);
        }

        if (vy1 > 0.0f) {
            yInvEntry = y2 - (y1 + h1);
            yInvExit = (y2 + h2) - y1;
        } else {
            yInvEntry = (y2 + h2) - y1;
            yInvExit = y2 - (y1 + h1);
        }

        float xEntry, yEntry;
        float xExit, yExit;

        xEntry = xInvEntry / vx1;
        xExit = xInvExit / vx1;

        yEntry = yInvEntry / vy1;
        yExit = yInvExit / vy1;

        float entryTime = Math.max(xEntry, yEntry);
        float exitTime = Math.min(xExit, yExit);

        if (entryTime > exitTime || xExit < 0.0f || yExit < 0.0f || xEntry > 1.0f || yEntry > 1.0f) {
            return false;
        }else{
            float dx = x1 + w1/2f + px * entryTime;
            float dy = y1 + h1/2f + py * entryTime;

            out.set(dx, dy);

            return true;
        }
    }

    public void collideGroups(EntityGroup<?> groupa, EntityGroup<?> groupb){
        collided.clear();

        for(Entity entity : groupa.all()){
            if(!(entity instanceof SolidEntity))
                continue;
            if(collided.contains(entity.id))
                continue;

            SolidEntity solid = (SolidEntity)entity;

            solid.getBoundingBox(r2);
            solid.hitbox.getRect(r1, solid.lastX, solid.lastY);
            r2.merge(r1);

            synchronized (Entities.entityLock) {

                groupb.tree().getIntersect(c -> {
                    if (!collided.contains(c.id))
                        checkCollide(entity, c);
                }, r2);
            }

            collided.add(entity.id);
        }
    }
}
