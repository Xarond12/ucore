package io.anuke.ucore.entities;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Rectangle;
import io.anuke.ucore.core.Core;
import io.anuke.ucore.entities.trait.DrawTrait;
import io.anuke.ucore.function.Consumer;
import io.anuke.ucore.function.Predicate;

public class EntityDraw {
    private static final Rectangle viewport = new Rectangle();
    private static final Rectangle rect = new Rectangle();

    public static <T extends DrawTrait> void draw(EntityGroup<T> group){
        draw(group, e -> true);
    }

    public static <T extends DrawTrait> void draw(EntityGroup<T> group, Predicate<T> toDraw){
        drawWith(group, toDraw, DrawTrait::draw);
    }

    public static <T extends DrawTrait> void drawWith(EntityGroup<T> group, Predicate<T> toDraw, Consumer<T> cons){
        OrthographicCamera cam = Core.camera;
        viewport.set(cam.position.x - cam.viewportWidth / 2 * cam.zoom, cam.position.y - cam.viewportHeight / 2 * cam.zoom, cam.viewportWidth * cam.zoom, cam.viewportHeight * cam.zoom);

        for(DrawTrait e : group.all()){
            if(!toDraw.test((T)e) || !e.isAdded()) continue;
            rect.setSize(e.drawSize()).setCenter(e.getX(), e.getY());

            if(rect.overlaps(viewport)) {
                cons.accept((T)e);
            }
        }
    }
}
