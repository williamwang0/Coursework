package byow.Core;

import byow.InputDemo.InputSource;
import byow.InputDemo.KeyboardInputSource;
import byow.InputDemo.StringInputDevice;
import byow.TileEngine.TERenderer;
import byow.TileEngine.TETile;
import byow.TileEngine.Tileset;

import java.awt.*;
import java.util.*;
import java.io.*;

import static byow.Core.RandomUtils.*;

public class Engine {
    TERenderer ter = new TERenderer();
    /* Feel free to change the width and height. */
    public static final int WIDTH = 80;
    public static final int HEIGHT = 40;
    private static final double HALL_GEO = 0.65;
    // Parameter for geometric distribution of hallway lengths
    private static final double ROOM_GEO = 0.7;
    // Parameter for geometric distribution of room expansions along different directions
    private static final double ROOM_PROB = 0.7;
    // Probability of hallway attempting to end in a new room
    private static final double BRANCH_PROB = 0.3;
    // Probability  of hallway attempting to branch into a new one
    private static final double HALL_GEN = 0.7;
    // Probability that any given side of a room will have a new hallway
    private static final int REPLAY_DELAY_MS = 40;
    // Delay between moves when replaying saves
    private static final Map<Dir, int[]> DXDY = new HashMap<>();
    {
        DXDY.put(Dir.UP, new int[]{0, 1});
        DXDY.put(Dir.DOWN, new int[]{0, -1});
        DXDY.put(Dir.LEFT, new int[]{-1, 0});
        DXDY.put(Dir.RIGHT, new int[]{1, 0});
    }
    private static final Map<Character, Dir> KEY_TO_DIR = new HashMap<>();
    {
        KEY_TO_DIR.put('W', Dir.UP);
        KEY_TO_DIR.put('A', Dir.LEFT);
        KEY_TO_DIR.put('S', Dir.DOWN);
        KEY_TO_DIR.put('D', Dir.RIGHT);
        KEY_TO_DIR.put('I', Dir.UP);
        KEY_TO_DIR.put('J', Dir.LEFT);
        KEY_TO_DIR.put('K', Dir.DOWN);
        KEY_TO_DIR.put('L', Dir.RIGHT);
    }
    private static final Set<Character> P1_KEYS = new HashSet<>();
    {
        P1_KEYS.add('W');
        P1_KEYS.add('A');
        P1_KEYS.add('S');
        P1_KEYS.add('D');
    }
    private static final Set<Character> P2_KEYS = new HashSet<>();
    {
        P2_KEYS.add('I');
        P2_KEYS.add('J');
        P2_KEYS.add('K');
        P2_KEYS.add('L');
    }
    private static final TETile EMPTY = Tileset.NOTHING;
    private static final TETile ROOM = Tileset.FLOOR;
    private static final TETile WALL = Tileset.WALL;
    private static final TETile P1_TILE = new TETile('@', Color.BLUE, Color.WHITE, "p1");
    private static final TETile P2_TILE = new TETile('&', Color.BLUE, Color.WHITE, "p2");
    private static final String SAVE_PATH = "../savefile.txt";
    private static final boolean RENDER_GAME = true;
    private Random rng;
    private Queue<Room> fringe;
    private int currNumRooms = 0;
    private TETile[][] world;
    private String currSave = "";
    private boolean worldReady = false;
    private Room avStartRoom;
    private Avatar player1;
    private Avatar player2;

    /**
     * Method used for exploring a fresh world. This method should handle all inputs,
     * including inputs from the main menu.
     */

    public void interactWithKeyboard() {
        if (RENDER_GAME) {
            ter.initialize(WIDTH, HEIGHT, 0, 1);
        }
        readInputs("", true);
    }

    /**
     * Method used for autograding and testing your code. The input string will be a series
     * of characters (for example, "n123sswwdasdassadwas", "n123sss:q", "lwww". The engine should
     * behave exactly as if the user typed these characters into the engine using
     * interactWithKeyboard.
     *
     * Recall that strings ending in ":q" should cause the game to quite save. For example,
     * if we do interactWithInputString("n123sss:q"), we expect the game to run the first
     * 7 commands (n123sss) and then quit and save. If we then do
     * interactWithInputString("l"), we should be back in the exact same state.
     *
     * In other words, both of these calls:
     *   - interactWithInputString("n123sss:q")
     *   - interactWithInputString("lww")
     *
     * should yield the exact same world state as:
     *   - interactWithInputString("n123sssww")
     *
     * @param input the input string to feed to your program
     * @return the 2D TETile[][] representing the state of the world
     */
    public TETile[][] interactWithInputString(String input) {
        if (RENDER_GAME) {
            ter.initialize(WIDTH, HEIGHT);
        }
        readInputs(input, false);
        return world;
    }

    private void readInputs(String input, boolean replay) {
        InputSource in;
        if (input.isEmpty()) {
            in = new KeyboardInputSource();
        } else {
            in = new StringInputDevice(input);
        }
        while (!worldReady && in.possibleNextInput()) { // listen for (n)ew, (l)oad, (r)eplay
            char inputChar = in.getNextKey();
            switch (inputChar) {
                case 'N':
                    currSave += 'N';
                    if (input.isEmpty()) {
                        ter.seedScreen();
                    }
                    rng = new Random(readSeed(in));
                    generateWorld();
                    player1 = new Avatar(avStartRoom.x + 2, avStartRoom.y + 2, P1_TILE);
                    player2 = new Avatar(avStartRoom.x + 1, avStartRoom.y + 2, P2_TILE);
                    if (replay && RENDER_GAME) {
                        ter.renderFrame(world);
                        renderAvatar(player1);
                        renderAvatar(player2);
                    }
                    worldReady = true;
                    break;
                case 'L': // read previous inputs from file and run them
                    if (RENDER_GAME) {
                        ter.loading();
                    }
                    readInputs(readSave(), false);
                    break;
                case 'R': // replay save
                    readInputs(readSave(), true);
                    break;
                case 'Q': // quit
                    closeWindow();
                    return;
                default:
                    break;
            }
        }
        char inputChar = 'a', prevChar = ' ';
        while (in.possibleNextInput()) { // listens for player actions
            if (inputChar != ' ') {
                prevChar = inputChar;
            }
            inputChar = in.getNextKey();
            if (P1_KEYS.contains(inputChar)) {
                moveAvatar(player1, KEY_TO_DIR.get(inputChar), inputChar, replay);
            } else if (P2_KEYS.contains(inputChar)) {
                moveAvatar(player2, KEY_TO_DIR.get(inputChar), inputChar, replay);
            } else if (inputChar == 'Q' && prevChar == ':') { // quit and save
                try {
                    BufferedWriter w = new BufferedWriter(new FileWriter(SAVE_PATH));
                    w.write(currSave, 0, currSave.length());
                    w.close();
                    System.out.println("Save successful");
                    closeWindow();
                } catch (IOException e) {
                    System.out.println("Error while saving");
                }
                return;
            }
            if (input.isEmpty()) {
                ter.renderFrame(world);
            }
            ter.renderFrame(world);
        }
        // At this point, replays/loads have finished, and the player can act
        if (RENDER_GAME) {
            ter.renderFrame(world);
            renderAvatar(player1);
            renderAvatar(player2);
            if (input.isEmpty()) { // if we were reading keyboard inputs, allow player to act
                readInputs("", true);
            }
        }
    }

    private void closeWindow() {
        if (RENDER_GAME) {
            System.exit(0);
        }
    }

    private String readSave() {
        BufferedReader r;
        try {
            r = new BufferedReader(new FileReader(SAVE_PATH));
            return r.readLine();
        } catch (FileNotFoundException e) {
            System.out.println("Error: save file not found in " + SAVE_PATH);
        } catch (IOException e) {
            System.out.println("Error: save file is empty");
        }
        return "";
    }

    private long readSeed(InputSource in) {
        long seedNum = 0;
        while (in.possibleNextInput()) {
            char inputChar = in.getNextKey();
            if (inputChar != ' ') {
                currSave += inputChar;
                if (inputChar == 'S') {
                    return seedNum;
                }
                seedNum = (seedNum * 10) + Character.getNumericValue(inputChar);
            }
        }
        return seedNum;
    }

    private void generateWorld() {
        int finalNumRooms = uniform(rng, 20, 30);
        while (currNumRooms < finalNumRooms) { // "retrying" when world prematurely terminates
            currNumRooms = 1;
            world = new TETile[WIDTH][HEIGHT];
            for (int i = 0; i < WIDTH; i++) {
                for (int j = 0; j < HEIGHT; j++) {
                    world[i][j] = EMPTY;
                }
            }
            fringe = new LinkedList<>();
            int roomSize = uniform(rng, 3, 7);
            int startX1 = uniform(rng, 7, WIDTH - 7);
            int startY1 = uniform(rng, 7, HEIGHT - 7);
            avStartRoom = new Room(startX1, startX1 + roomSize, startY1, startY1 + roomSize);
            drawRoom(startX1, startX1 + roomSize, startY1, startY1 + roomSize);
            fringe.add(avStartRoom);
            while (!fringe.isEmpty()) { // while there are still rooms to branch from
                Room currRoom = fringe.remove();
                /*
                   Choose placement and length of outgoing hallway
                   If hallway meets another room/hallway, it should join it
                   Choose whether a) hallway ends, b) hallway branches, or c) a room is created
                */
                Dir[] directions = Dir.values();
                shuffle(rng, directions);
                for (Dir d : directions) {
                    /* add chance to add a hallway
                     */
                    boolean makeHall = false;
                    if (uniform(rng) <= HALL_GEN) {
                        makeHall = true;
                    }

                    if (makeHall) {
                        int wallCoord = currRoom.wallLocations.get(d);
                        int[] wallSizeArr = currRoom.wallIntervals.get(d);

                        int wallSize = wallSizeArr[1] - wallSizeArr[0] + 1;
                        int hallStart = wallSizeArr[0] + uniform(rng, (wallSize - 2));

                        if (d.equals(Dir.DOWN) || d.equals(Dir.UP)) {
                            wallCoord += DXDY.get(d)[1];
                            addHallway(hallStart, hallStart + 1,
                                    hallStart + 2, wallCoord, wallCoord, wallCoord, d);
                        } else {
                            wallCoord += DXDY.get(d)[0];
                            addHallway(wallCoord, wallCoord, wallCoord,
                                    hallStart, hallStart + 1, hallStart + 2, d);
                        }
                    }
                }
            }
        }
    }

    /* Creates a room of random size and placement including the point (x, y) in the specified Dir.
       To create it, a 1x1 node is expanded one side at a time, with a chance to expand the side
       one further unit, creating a geometric distribution of expansion lengths along each side.
     */
    private boolean createRoom(int x, int y, Dir dir) {
        int x1 = x - 1;
        int x2 = x + 1;
        int y1 = y - 1;
        int y2 = y + 1;
        if (intersects(x1, x2, y1, y2)) {
            return false;
        }
        Dir[] directions = Dir.values();
        shuffle(rng, directions);
        for (Dir d : directions) {
            if (d != dir) {
                boolean keepExpanding = true;
                while (keepExpanding && uniform(rng) < ROOM_GEO) {
                    // expand this side
                    switch (d) {
                        case UP:
                            y1 -= 1;
                            if (intersects(x1, x2, y1, y1)) {
                                y1 += 1;
                                keepExpanding = false;
                            }
                            break;
                        case DOWN:
                            y2 += 1;
                            if (intersects(x1, x2, y2, y2)) {
                                y2 -= 1;
                                keepExpanding = false;
                            }
                            break;
                        case LEFT:
                            x2 += 1;
                            if (intersects(x2, x2, y1, y2)) {
                                x2 -= 1;
                                keepExpanding = false;
                            }
                            break;
                        case RIGHT:
                            x1 -= 1;
                            if (intersects(x1, x1, y1, y2)) {
                                x1 += 1;
                                keepExpanding = false;
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        drawRoom(x1, x2, y1, y2);
        if (x2 - x1 > 2 && y2 - y1 > 2) { // proper room; does not have a dimension of length 1
            fringe.add(new Room(x1, x2, y1, y2));
            currNumRooms += 1;
        }
        return true;
    }

    /* Draws the room specified by the given x and y bounds in the world.
     */
    private void drawRoom(int x1, int x2, int y1, int y2) {
        // Create room with walls on (x1, y1), (x2, y2)
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                assert world[i][j] == EMPTY;
                if (i == x1 || i == x2 || j == y1 || j == y2) {
                    world[i][j] = WALL;
                } else {
                    world[i][j] = ROOM;
                }
            }
        }
    }

    /* Checks whether any of the tiles in this bound (inclusive) are already occupied.
     */
    private boolean intersects(int x1, int x2, int y1, int y2) {
        if (!valid(x2, y2) || !valid(x1, y1)) {
            return true;
        }
        for (int i = x1; i <= x2; i++) {
            for (int j = y1; j <= y2; j++) {
                if (world[i][j] == WALL) {
                    return true;
                }
            }
        }
        return false;
    }

    /* Adds a hallway in the specified Dir from (floorX, floorY), potentially ending in a room.
       wallX and wallY variables specify the walls surrounding either side of (floorX, floorY).
     */
    private void addHallway(int wall1X, int floorX, int wall2X,
                            int wall1Y, int floorY, int wall2Y, Dir d) {
        int startX = floorX;
        int startY = floorY;
        int dx = DXDY.get(d)[0];
        int dy = DXDY.get(d)[1];
        if (!valid(floorX, floorY) || world[wall1X][wall1Y] != EMPTY
                || world[wall2X][wall2Y] != EMPTY) {
            // if hallway is already outside of world or interferes with an existing hallway
            return;
        }
        world[floorX - dx][floorY - dy] = ROOM; // open the hallway from its parent room
        while (uniform(rng) < HALL_GEO) { // chance to extend the hallway
            if (!valid(floorX, floorY)) { // hallway has reached world's edge
                world[floorX - dx][floorY - dy] = WALL; // close the hallway
                return;
            } else if (world[floorX][floorY] == WALL
                    && world[wall1X][wall1Y] == WALL && world[wall2X][wall2Y] == WALL) {
                world[floorX][floorY] = ROOM; // open up hallway into other hallway/room
                world[floorX + dx][floorY + dy] = ROOM;
                return;
            } else if (world[floorX][floorY] == WALL
                    || world[wall1X][wall1Y] == WALL || world[wall2X][wall2Y] == WALL) {
                world[floorX - dx][floorY - dy] = WALL; // close the hallway in edge cases
                return;
            }
            world[wall1X][wall1Y] = WALL;
            world[floorX][floorY] = ROOM;
            world[wall2X][wall2Y] = WALL;
            wall1X += dx;
            floorX += dx;
            wall2X += dx;
            wall1Y += dy;
            floorY += dy;
            wall2Y += dy;
        }
        if (!valid(floorX, floorY)) { // hallway has reached world's edge
            world[floorX - dx][floorY - dy] = WALL; // close the hallway
            return;
        }
        double rand = uniform(rng);
        if (rand < ROOM_PROB && !closeToEdge(floorX, floorY, true)) { // ends in a new room
            boolean success = createRoom(floorX + dx, floorY + dy, d);
            if (success) {
                world[floorX][floorY] = ROOM;
            } else {
                world[wall1X - dx][wall1Y - dy] = WALL;
                world[floorX - dx][floorY - dy] = WALL;
                world[wall2X - dx][wall2Y - dy] = WALL;
            }
        } else { // dead end or branched hallway
            world[floorX - dx][floorY - dy] = WALL;
            if (rand < ROOM_PROB + BRANCH_PROB && (floorX - dx - startX > 2
                    || floorY - dy - startY > 2)) {
                // hallway branches into a new one
                Dir f;
                if (d == Dir.UP || d == Dir.DOWN) {
                    Dir[] fork = {Dir.LEFT, Dir.RIGHT};
                    f = fork[uniform(rng, 0, 2)];
                } else {
                    Dir[] fork = {Dir.UP, Dir.DOWN};
                    f = fork[uniform(rng, 0, 2)];
                }
                int fdx = DXDY.get(f)[0];
                int fdy = DXDY.get(f)[1];
                addHallway(floorX - dx + fdx, floorX - 2 * dx + fdx, floorX - 3 * dx + fdx,
                        floorY - dy + fdy, floorY - 2 * dy + fdy, floorY - 3 * dy + fdy, f);
            }
        }
    }

    private boolean closeToEdge(int x, int y, boolean willcreateRoom) {
        int margin = 1;
        if (willcreateRoom) {
            margin = 3;
        }
        return !(x > margin && x < WIDTH - margin && y > margin && y < HEIGHT - margin);
    }

    private boolean valid(int x, int y) {
        return (x >= 0 && x < WIDTH && y >= 0 && y < HEIGHT);
    }

    private boolean validPos(int x, int y) { // checks if (x, y) is a valid position for player
        return valid(x, y) && !intersects(x, x, y, y)
                && world[x][y] != P1_TILE && world[x][y] != P2_TILE;
    }

    private void moveAvatar(Avatar a, Dir d, char input, boolean replaying) {
        int newX = a.x + DXDY.get(d)[0];
        int newY = a.y + DXDY.get(d)[1];
        if (validPos(newX, newY)) {
            world[a.x][a.y] = ROOM;
            a.x = newX;
            a.y = newY;
            if (replaying && RENDER_GAME) {
                try {
                    Thread.sleep(REPLAY_DELAY_MS);
                } catch (InterruptedException e) {
                    System.out.println("Unusual interruption of sleep?");
                }
                renderAvatar(a);
            }
        }
        currSave += input;
    }

    private void renderAvatar(Avatar a) {
        world[a.x][a.y] = a.texture;
        ter.renderFrame(world);
    }

    private class Room {
        Map<Dir, Integer> wallLocations;
        Map<Dir, int[]> wallIntervals;
        int x;
        int y;

        Room(int X1, int X2, int Y1, int Y2) {
            wallLocations = new HashMap<>();
            wallIntervals = new HashMap<>();

            wallLocations.put(Dir.UP, Y2);
            wallLocations.put(Dir.DOWN, Y1);
            wallLocations.put(Dir.LEFT, X1);
            wallLocations.put(Dir.RIGHT, X2);

            wallIntervals.put(Dir.UP, new int[]{X1, X2});
            wallIntervals.put(Dir.DOWN, new int[]{X1, X2});
            wallIntervals.put(Dir.LEFT, new int[]{Y1, Y2});
            wallIntervals.put(Dir.RIGHT, new int[]{Y1, Y2});

            this.x = X1;
            this.y = Y1;
        }
    }

    private class Avatar {
        int x;
        int y;
        TETile texture;

        Avatar(int x, int y, TETile texture) {
            this.x = x;
            this.y = y;
            this.texture = texture;
        }
    }

    enum Dir {
        UP, DOWN, LEFT, RIGHT;
    }
}
