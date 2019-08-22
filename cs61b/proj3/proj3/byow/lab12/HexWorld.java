package byow.lab12;
import org.junit.Test;
import static org.junit.Assert.*;

import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.util.Random;

/**
 * Draws a world consisting of hexagonal regions.
 */
public class HexWorld {
    private static final int WIDTH = 50;
    private static final int HEIGHT = 50;
    private static final long SEED = 28723;
    private static final Random RANDOM = new Random(SEED);

    private static TETile randomTile() {
        int tileNum = RANDOM.nextInt(6);
        switch (tileNum) {
            case 0: return Tileset.GRASS;
            case 1: return Tileset.WATER;
            case 2: return Tileset.FLOWER;
            case 3: return Tileset.SAND;
            case 4: return Tileset.MOUNTAIN;
            case 5: return Tileset.TREE;
            default: return Tileset.NOTHING;
        }
    }

    private static void addHexagon(TETile[][] world, int s, int llX, int llY, TETile t, boolean vary) {
        // (llX, llY) is the lower left coordinate of the hexagon
        TETile staticT = t;
        for (int i = 0; i < s; i++) {
            for (int j = 0; j < s + 2 * i; j++) {
                if (vary) {
                    t = TETile.colorVariant(staticT, 100, 100, 100, RANDOM);
                }
                world[llX + j - i][llY + i] = t;
                world[llX + j - i][llY + 2 * s - 1 - i] = t;
            }
        }
    }

    private static void addHexColumn(TETile[][] world, int s, int llX, int llY,
                                    TETile t, boolean vary, int n, boolean randHex) {
        for (int i = 0; i < n; i++) {
            if (randHex) {
                t = randomTile();
            }
            addHexagon(world, s, llX, llY + 2 * s * i, t, vary);
        }
    }

    private static void fillCatanGrid(TETile[][] world, int s, boolean vary) {
        int[] heights = {3, 4, 5, 4, 3};
        for (int i = 0; i < 5; i++) {
            addHexColumn(world, s, (s - 1) + (2 * s - 1) * i, Math.abs(2 - i) * s, Tileset.WALL,
                    vary, heights[i], true);
        }
    }

    public static void main(String[] args) {
        TERenderer ter = new TERenderer();
        ter.initialize(WIDTH, HEIGHT);

        TETile[][] world = new TETile[WIDTH][HEIGHT];
        for (int x = 0; x < WIDTH; x += 1) {
            for (int y = 0; y < HEIGHT; y += 1) {
                world[x][y] = Tileset.NOTHING;
            }
        }
        fillCatanGrid(world, 5, true);
        ter.renderFrame(world);
    }
}
