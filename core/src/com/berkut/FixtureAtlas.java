package com.berkut;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;


class FixtureAtlas {
    private static final FixtureDef DEFAULT_FIXTURE = new FixtureDef();

    private final Map<String, BodyModel> bodyMap = new HashMap<String, BodyModel>();
    private final PolygonShape shape = new PolygonShape();

    FixtureAtlas(FileHandle shapeFile) {
        if (shapeFile == null)
            throw new NullPointerException("shapeFile is null");

        importFromFile(shapeFile.read());
    }

    // -------------------------------------------------------------------------
    // Public API
    // -------------------------------------------------------------------------

    void createFixtures(Body body, String name, float width, float height) {
        createFixtures(body, name, width, height, null);
    }

    private void createFixtures(Body body, String name, float width, float height, FixtureDef params) {
        BodyModel bm = bodyMap.get(name);
        if (bm == null)
            throw new RuntimeException(name + " does not exist in the fixture list.");

        Vector2[][] polygons = bm.getPolygons(width, height);
        if (polygons == null)
            throw new RuntimeException(name + " does not declare any polygon. "
                    + "Should not happen. Is your shape file corrupted ?");

        for (Vector2[] polygon : polygons) {
            shape.set(polygon);
            FixtureDef fd = params == null ? DEFAULT_FIXTURE : params;
            fd.shape = shape;
            body.createFixture(fd);
        }
    }

    // -------------------------------------------------------------------------
    // Import
    // -------------------------------------------------------------------------

    private void importFromFile(InputStream stream) {
        DataInputStream is = null;

        try {
            is = new DataInputStream(stream);
            while (is.available() > 0) {
                String name = is.readUTF();
                Vector2[][] points = readVec2(is);
                Vector2[][] polygons = readVec2(is);

                BodyModel bm = new BodyModel(polygons);
                bodyMap.put(name, bm);
            }

        } catch (IOException ex) {
            throw new RuntimeException(ex.getMessage());

        } finally {
            if (is != null)
                try {
                    is.close();
                } catch (IOException ex) {
                    System.err.println(ex.getMessage());
                }
        }
    }
    // -------------------------------------------------------------------------

    private Vector2 readVec(DataInputStream is) throws IOException {
        Vector2 v = new Vector2();
        v.x = is.readFloat();
        v.y = is.readFloat();
        return v;
    }

    private Vector2[] readVec1(DataInputStream is) throws IOException {
        int len = is.readInt();
        Vector2[] vs = new Vector2[len];
        for (int i = 0; i < len; i++)
            vs[i] = readVec(is);
        return vs;
    }

    private Vector2[][] readVec2(DataInputStream is) throws IOException {
        int len = is.readInt();
        Vector2[][] vss = new Vector2[len][];
        for (int i = 0; i < len; i++)
            vss[i] = readVec1(is);
        return vss;
    }

    // -------------------------------------------------------------------------
    // BodyModel class
    // -------------------------------------------------------------------------

    private class BodyModel {
        private final Vector2[][] normalizedPolygons;
        private final Vector2[][] polygons;

        BodyModel(Vector2[][] polygons) {
            this.normalizedPolygons = polygons;
            this.polygons = new Vector2[polygons.length][];

            for (int i = 0; i < polygons.length; i++) {
                this.polygons[i] = new Vector2[polygons[i].length];
                for (int ii = 0; ii < polygons[i].length; ii++)
                    this.polygons[i][ii] = new Vector2(polygons[i][ii]);
            }
        }

        Vector2[][] getPolygons(float width, float height) {
            for (int i = 0; i < normalizedPolygons.length; i++) {
                for (int ii = 0; ii < normalizedPolygons[i].length; ii++) {
                    this.polygons[i][ii] = new Vector2(normalizedPolygons[i][ii]);
                    this.polygons[i][ii].x *= width / 100f;
                    this.polygons[i][ii].y *= height / 100f;
                }
            }
            return polygons;
        }
    }
}