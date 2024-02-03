package Assign4;

import java.io.*;
import java.util.*;

/**
 * The Main class represents the entry point of the program.
 * It initializes the game board, reads input from a file, and performs game logic.
 * The main method throws various exceptions related to invalid input or game rules.
 */
public class Main {
    static Board gameBoard;
    static BufferedReader input;
    static BufferedWriter output;
    public static void main(String[] args) throws IOException, InvalidBoardSizeException, InvalidNumberOfFoodPointsException, InvalidNumberOfInsectsException, InvalidInsectColorException, InvalidInsectTypeException, InvalidEntityPositionException, TwoEntitiesOnSamePositionException, DuplicateInsectException {

        input = new BufferedReader(new FileReader("src//Assign4//input.txt"));
        output = new BufferedWriter(new FileWriter("src//Assign4//output.txt"));

        int insectsNumber;
        int foodPointsNumber;

        try {
            gameBoard = new Board(Board.readBoardSize());
            insectsNumber = Insect.readInsectsNumber();
            foodPointsNumber = FoodPoint.readFoodPointsNumber();

            for (int i = 0; i < insectsNumber; i++) {
                gameBoard.addEntity(Insect.readInsect());
            }
            for (int i = 0; i < foodPointsNumber; i++) {
                gameBoard.addEntity(FoodPoint.readFoodPoint());
            }

            List<EntityPosition> keySet = new ArrayList<>(gameBoard.getBoardData().keySet());
            for (int i = 0; i < insectsNumber; i++) {
                EntityPosition key = keySet.get(i);
                BoardEntity value = gameBoard.getBoardData().get(key);

                Insect insect = (Insect) value;
                int amountOfFoodEaten = insect.travelDirection();
                output.write(insect.color + " " + insect.getClass().getSimpleName() + " " + gameBoard.getDirection(insect) + " " + amountOfFoodEaten);
                output.newLine();
            }

            input.close();
            output.close();
        }
        catch (Exception exception) {
            output.write(exception.getMessage());
            output.newLine();
            input.close();
            output.close();
        }
    }
}

/**
 * The abstract class representing a board entity.
 */
abstract class BoardEntity {
    protected EntityPosition entityPosition;

    /**
     * Makes a move for the given entity at the current position.
     * @param currentPosition The current position of the entity.
     * @param entity The entity to make a move for.
     * @return The amount of food eaten during the move.
     */
    public static int makeMove(EntityPosition currentPosition, BoardEntity entity) {
        int currentAmountOfFoodEaten = 0;
        BoardEntity currentEntity = Main.gameBoard.getEntity(currentPosition);
        if (currentEntity == null) return currentAmountOfFoodEaten;

        if (currentEntity.getClass().getSimpleName().equals("FoodPoint")) {
            currentAmountOfFoodEaten += ((FoodPoint) currentEntity).value;
            Main.gameBoard.addEntityToDelete(currentPosition);
        }
        else if (((Insect) currentEntity).color != ((Insect) entity).color) {
            Main.gameBoard.addEntityToDelete(entity.entityPosition);
            return -1;
        }

        return currentAmountOfFoodEaten;
    }
}

/**
 * Represents the position of an entity on a game board.
 */
class EntityPosition {
    private final int x, y;

    /**
     * Constructs a new EntityPosition object with the specified coordinates.
     *
     * @param x the x-coordinate of the position
     * @param y the y-coordinate of the position
     */
    public EntityPosition(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Throws an InvalidEntityPositionException if the position is invalid.
     *
     * @throws InvalidEntityPositionException if the position is invalid
     */
    public void throwIfInvalid() throws InvalidEntityPositionException {
        if (this.x < 0 || this.x >= Main.gameBoard.getSize() || this.y < 0 || this.y >= Main.gameBoard.getSize()) throw new InvalidEntityPositionException();
    }

    /**
     * Returns the x-coordinate of the position.
     *
     * @return the x-coordinate of the position
     */
    public int getX() {
        return x;
    }

    /**
     * Returns the y-coordinate of the position.
     *
     * @return the y-coordinate of the position
     */
    public int getY() {
        return y;
    }

    /**
     * Checks if this EntityPosition is equal to another object.
     *
     * @param o the object to compare with
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EntityPosition that = (EntityPosition) o;
        return x == that.x && y == that.y;
    }

    /**
     * Returns the hash code of this EntityPosition.
     *
     * @return the hash code of this EntityPosition
     */
    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    /**
     * Finds the direction from this EntityPosition to another position based on the type of moving.
     *
     * @param objectPosition the position of the object to move towards
     * @param typeOfMoving   the type of moving (OrthogonalMoving or DiagonalMoving)
     * @return the direction from this EntityPosition to the objectPosition
     */
    public Direction findDirection(EntityPosition objectPosition, String typeOfMoving) {
        Direction resultDirection = null;

        for (int i = 0; i < 1 && !typeOfMoving.equals(DiagonalMoving.class.getSimpleName()); i++) {
            if (objectPosition.x == this.x && objectPosition.y > this.y) resultDirection = Direction.E;
            else if (objectPosition.x == this.x && objectPosition.y < this.y) resultDirection = Direction.W;
            else if (objectPosition.y == this.y && objectPosition.x > this.x) resultDirection = Direction.S;
            else if (objectPosition.y == this.y && objectPosition.x < this.x) resultDirection = Direction.N;
        }

        if (typeOfMoving.equals(OrthogonalMoving.class.getSimpleName())) return resultDirection;
        int xDif = objectPosition.x - this.x;
        int yDif = objectPosition.y - this.y;

        if (Math.abs(xDif) == Math.abs(yDif)) {
            if (objectPosition.x < x && objectPosition.y > this.y) resultDirection = Direction.NE;
            else if (objectPosition.x > x && objectPosition.y > this.y) resultDirection = Direction.SE;
            else if (objectPosition.x < x && objectPosition.y < this.y) resultDirection = Direction.NW;
            else if (objectPosition.x > x && objectPosition.y < this.y) resultDirection = Direction.SW;
        }

        return resultDirection;
    }
}

/**
 * Represents the cardinal and ordinal directions.
 */
enum Direction {
    N("North"),
    E("East"),
    S("South"),
    W("West"),
    NE("North-East"),
    SE("South-East"),
    SW("South-West"),
    NW("North-West");

    private final String textRepresentation;

    /**
     * Constructs a new Direction with the given text representation.
     * @param textRepresentation the text representation of the direction
     */
    Direction(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    /**
     * Returns the text representation of the direction.
     * @return the text representation of the direction
     */
    @Override
    public String toString() {
        return this.textRepresentation;
    }

    /**
     * Checks if the direction is orthogonal (North, East, South, or West).
     * @return true if the direction is orthogonal, false otherwise
     */
    public boolean isOrthogonal() {
        return this == N || this == E || this == W || this == S;
    }

    /**
     * Checks if the direction is diagonal (North-East, South-East, South-West, or North-West).
     * @return true if the direction is diagonal, false otherwise
     */
    public boolean isDiagonal() {
        return this == NE || this == SE || this == SW || this == NW;
    }
}

/**
 * Represents the color of an insect.
 */
enum InsectColor {
    RED("Red"),
    GREEN("Green"),
    BLUE("Blue"),
    YELLOW("Yellow");

    private final String textRepresentation;

    /**
     * Constructs an InsectColor enum with the specified text representation.
     *
     * @param textRepresentation the text representation of the insect color
     */
    InsectColor(String textRepresentation) {
        this.textRepresentation = textRepresentation;
    }

    /**
     * Converts a string representation of an insect color to the corresponding InsectColor enum value.
     *
     * @param color the string representation of the insect color
     * @return the InsectColor enum value corresponding to the given color
     * @throws InvalidInsectColorException if the given color is not a valid insect color
     */
    public static InsectColor toColor(String color) throws InvalidInsectColorException {
        switch (color) {
            case "Red":
                return RED;
            case "Green":
                return GREEN;
            case "Blue":
                return BLUE;
            case "Yellow":
                return YELLOW;
            default:
                throw new InvalidInsectColorException();
        }
    }

    /**
     * Returns the text representation of the insect color.
     *
     * @return the text representation of the insect color
     */
    @Override
    public String toString() {
        return textRepresentation;
    }
}

/**
 * Represents a food point on the game board.
 * Extends the BoardEntity class.
 */
class FoodPoint extends BoardEntity {
    protected int value;

    /**
     * Constructs a FoodPoint object with the given position and value.
     *
     * @param position the position of the food point on the game board
     * @param value    the value of the food point
     */
    public FoodPoint(EntityPosition position, int value) {
        this.entityPosition = position;
        this.value = value;
    }

    /**
     * Reads a FoodPoint object from the input stream.
     *
     * @return the read FoodPoint object
     * @throws InvalidEntityPositionException if the entity position is invalid
     * @throws IOException                   if an I/O error occurs
     */
    public static FoodPoint readFoodPoint() throws InvalidEntityPositionException, IOException {
        String[] arguments = Main.input.readLine().split(" ");
        int foodAmount = Integer.parseInt(arguments[0]);
        int xCoordinate = Integer.parseInt(arguments[1]) - 1;
        int yCoordinate = Integer.parseInt(arguments[2]) - 1;

        EntityPosition position = new EntityPosition(xCoordinate, yCoordinate);
        position.throwIfInvalid();
        return new FoodPoint(position, foodAmount);
    }

    /**
     * Reads the number of food points from the input stream.
     *
     * @return the number of food points
     * @throws IOException                        if an I/O error occurs
     * @throws InvalidNumberOfFoodPointsException if the number of food points is invalid
     */
    public static int readFoodPointsNumber() throws IOException, InvalidNumberOfFoodPointsException {
        int amount = Integer.parseInt(Main.input.readLine());
        if (amount < 1 || amount > 200) throw new InvalidNumberOfFoodPointsException();
        return amount;
    }
}

/**
 * The abstract class representing an insect on the game board.
 */
abstract class Insect extends BoardEntity {
    protected InsectColor color;
    protected Direction bestDirection;

    /**
     * Constructs a new Insect object with the given position and color.
     *
     * @param position The position of the insect on the game board.
     * @param color    The color of the insect.
     */
    public Insect(EntityPosition position, InsectColor color) {
        this.entityPosition = position;
        this.color = color;
        this.bestDirection = null;
    }

    /**
     * Updates the value in the sums map for the given direction.
     * If the direction is not present in the map, it adds it with a value of 0.
     * Then it replaces the value with the sum of the current value and the given value.
     *
     * @param sums      The map containing the sums for each direction.
     * @param direction The direction to update the value for.
     * @param value     The value to add to the sum.
     */
    public static void updateValueInSums(Map<Direction, Integer> sums, Direction direction, Integer value) {
        sums.putIfAbsent(direction, 0);
        sums.replace(direction, sums.get(direction) + value);
    }

    /**
     * Finds the direction with the maximum value in the sums map.
     *
     * @param sums The map containing the sums for each direction.
     * @return The direction with the maximum value, or null if the map is empty.
     */
    public static Direction findMaxInSums(Map<Direction, Integer> sums) {
        int maxValue = -1;
        for (int value : sums.values()) {
            if (value > maxValue) maxValue = value;
        }

        List<Direction> maxDirections = new ArrayList<>();
        for (Map.Entry<Direction, Integer> element : sums.entrySet()) {
            if (element.getValue() == maxValue) maxDirections.add(element.getKey());
        }

        return (maxDirections.isEmpty() ? null : Collections.max(maxDirections));
    }

    /**
     * Reads an insect from the input stream.
     *
     * @return The read insect.
     * @throws IOException                   If an I/O error occurs.
     * @throws InvalidInsectColorException   If the insect color is invalid.
     * @throws InvalidInsectTypeException    If the insect type is invalid.
     * @throws InvalidEntityPositionException If the entity position is invalid.
     */
    public static Insect readInsect() throws IOException, InvalidInsectColorException, InvalidInsectTypeException, InvalidEntityPositionException {
        String[] arguments = Main.input.readLine().split(" ");
        String insectColor = arguments[0];
        String insectType = arguments[1];
        int xCoordinate = Integer.parseInt(arguments[2]) - 1;
        int yCoordinate = Integer.parseInt(arguments[3]) - 1;

        InsectColor color = InsectColor.toColor(insectColor);
        EntityPosition position = new EntityPosition(xCoordinate, yCoordinate);
        position.throwIfInvalid();
        Insect insect;

        switch (insectType) {
            case "Butterfly":
                insect = new Butterfly(position, color);
                break;
            case "Ant":
                insect = new Ant(position, color);
                break;
            case "Spider":
                insect = new Spider(position, color);
                break;
            case "Grasshopper":
                insect = new Grasshopper(position, color);
                break;
            default:
                throw new InvalidInsectTypeException();
        }

        return insect;
    }

    /**
     * Reads the number of insects from the input stream.
     *
     * @return The number of insects.
     * @throws IOException                      If an I/O error occurs.
     * @throws InvalidNumberOfInsectsException If the number of insects is invalid.
     */
    public static int readInsectsNumber() throws IOException, InvalidNumberOfInsectsException {
        int amount = Integer.parseInt(Main.input.readLine());
        if (amount < 1 || amount > 16) throw new InvalidNumberOfInsectsException();
        return amount;
    }

    /**
     * Finds the first valid direction for the insect to move in.
     *
     * @param step             The step size for the movement.
     * @param orthogonalMoving Whether orthogonal movement is allowed.
     * @param diagonalMoving   Whether diagonal movement is allowed.
     * @return The first valid direction, or null if no valid direction is found.
     */
    public Direction findFirstDirection(int step, boolean orthogonalMoving, boolean diagonalMoving) {
        boolean N = (this.entityPosition.getX() - step) >= 0;
        boolean E = (this.entityPosition.getY() + step) < Main.gameBoard.getSize();
        boolean S = (this.entityPosition.getX() + step) < Main.gameBoard.getSize();
        boolean W = (this.entityPosition.getY() - step) >= 0;

        if (orthogonalMoving) {
            if (N) return Direction.N;
            if (E) return Direction.E;
            if (S) return Direction.S;
            if (W) return Direction.W;
        }
        if (diagonalMoving) {
            if (N && E) return Direction.NE;
            if (S && E) return Direction.SE;
            if (S && W) return Direction.SW;
            if (N && W) return Direction.NW;
        }
        return null;
    }

    /**
     * Calculates the best direction for the insect to move in.
     * This method should be implemented by subclasses.
     */
    public abstract void getBestDirection();

    /**
     * Travels in the calculated best direction and returns the number of steps taken.
     * This method should be implemented by subclasses.
     *
     * @return The number of steps taken.
     */
    public abstract int travelDirection();
}

/**
 * Represents a game board.
 */
class Board {
    private final Map<EntityPosition, BoardEntity> boardData;
    private final int size;
    private final List<EntityPosition> entitiesToDelete;

    /**
     * Constructs a Board object with the specified size.
     *
     * @param boardSize the size of the board
     */
    public Board(int boardSize) {
        this.boardData = new LinkedHashMap<>();
        this.entitiesToDelete = new ArrayList<>();
        this.size = boardSize;
    }

    /**
     * Reads the board size from the input.
     *
     * @return the board size
     * @throws IOException              if an I/O error occurs
     * @throws InvalidBoardSizeException if the board size is invalid
     */
    public static int readBoardSize() throws IOException, InvalidBoardSizeException {
        int size = Integer.parseInt(Main.input.readLine());
        if (size < 4 || size > 1000) throw new InvalidBoardSizeException();
        return size;
    }

    /**
     * Gets the size of the board.
     *
     * @return the size of the board
     */
    public int getSize() {
        return this.size;
    }

    /**
     * Gets the board data.
     *
     * @return the board data
     */
    public Map<EntityPosition, BoardEntity> getBoardData() {
        return boardData;
    }

    /**
     * Adds an entity to the board.
     *
     * @param entity the entity to add
     * @throws TwoEntitiesOnSamePositionException if there are two entities on the same position
     * @throws DuplicateInsectException           if there is a duplicate insect on the board
     */
    public void addEntity(BoardEntity entity) throws TwoEntitiesOnSamePositionException, DuplicateInsectException {
        for (Map.Entry<EntityPosition, BoardEntity> entry : this.boardData.entrySet()) {
            if (entry.getKey().equals(entity.entityPosition)) throw new TwoEntitiesOnSamePositionException();
            if (entity instanceof FoodPoint) continue;
            if (((Insect) entry.getValue()).color == ((Insect) entity).color && entry.getValue().getClass() == entity.getClass()) throw new DuplicateInsectException();
        }
        this.boardData.put(entity.entityPosition, entity);
    }

    /**
     * Adds an entity position to the list of entities to delete.
     *
     * @param position the position of the entity to delete
     */
    public void addEntityToDelete(EntityPosition position) {
        this.entitiesToDelete.add(position);
    }

    /**
     * Deletes the entities marked for deletion.
     */
    public void deleteEntities() {
        for (EntityPosition position : entitiesToDelete) {
            this.boardData.remove(position);
        }
        entitiesToDelete.clear();
    }

    /**
     * Gets the entity at the specified position.
     *
     * @param position the position of the entity
     * @return the entity at the specified position, or null if no entity exists at that position
     */
    public BoardEntity getEntity(EntityPosition position) {
        if (this.boardData.containsKey(position)) {
            return this.boardData.get(position);
        }
        return null;
    }

    /**
     * Gets the direction of the insect.
     *
     * @param insect the insect
     * @return the direction of the insect
     */
    public Direction getDirection(Insect insect) {
        return insect.bestDirection;
    }

    /**
     * Gets the sum of the directions of the insect.
     *
     * @param insect the insect
     * @return the sum of the directions of the insect
     */
    public int getDirectionSum(Insect insect) {
        return 0;
    }
}

/**
 * The OrthogonalMoving interface represents a behavior for entities that can move orthogonally on a game board.
 */
interface OrthogonalMoving {
    /**
     * Returns the visible value in the orthogonal direction for a given direction and entity position.
     *
     * @param direction      the direction of movement
     * @param entityPosition the current position of the entity
     * @return the visible value in the orthogonal direction
     */
    default int getOrthogonalDirectionVisibleValue(Direction direction, EntityPosition entityPosition) {
        return 0;
    }

    /**
     * Moves the entity orthogonally in the specified direction on the game board.
     *
     * @param direction the direction of movement
     * @param entity    the entity to be moved
     * @param step      the step size for each movement
     * @return the amount of food eaten during the movement
     */
    default int travelOrthogonally(Direction direction, BoardEntity entity, int step) {
        int amountOfFoodEaten = 0;

        switch (direction) {
            case E: {
                for (int i = entity.entityPosition.getY() + step; i < Main.gameBoard.getSize(); i += step) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(entity.entityPosition.getX(), i), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
            case W: {
                for (int i = entity.entityPosition.getY() - step; i >= 0; i -= step) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(entity.entityPosition.getX(), i), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
            case S: {
                for (int i = entity.entityPosition.getX() + step; i < Main.gameBoard.getSize(); i += step) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(i, entity.entityPosition.getY()), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
            case N: {
                for (int i = entity.entityPosition.getX() - step; i >= 0; i -= step) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(i, entity.entityPosition.getY()), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
        }

        Main.gameBoard.addEntityToDelete(entity.entityPosition);
        Main.gameBoard.deleteEntities();
        return amountOfFoodEaten;
    }
}

/**
 * This interface represents a diagonal moving behavior for a board entity.
 */
interface DiagonalMoving {
    /**
     * Returns the visible value in the diagonal direction for the given direction and entity position.
     *
     * @param direction      the direction of movement
     * @param entityPosition the position of the entity
     * @return the visible value in the diagonal direction
     */
    default int getDiagonalDirectionVisibleValue(Direction direction, EntityPosition entityPosition) {
        return 0;
    }

    /**
     * Travels diagonally in the given direction and updates the entity's position on the game board.
     * Returns the amount of food eaten during the diagonal movement.
     *
     * @param direction the direction of diagonal movement
     * @param entity    the board entity
     * @return the amount of food eaten during the diagonal movement
     */
    default int travelDiagonally(Direction direction, BoardEntity entity) {
        int amountOfFoodEaten = 0;

        switch (direction) {
            case SE: {
                for (int i = entity.entityPosition.getX() + 1, j = entity.entityPosition.getY() + 1; i < Main.gameBoard.getSize() && j < Main.gameBoard.getSize(); i++, j++) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(i, j), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
            case NE: {
                for (int i = entity.entityPosition.getX() - 1, j = entity.entityPosition.getY() + 1; i >= 0 && j < Main.gameBoard.getSize(); i--, j++) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(i, j), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
            case SW: {
                for (int i = entity.entityPosition.getX() + 1, j = entity.entityPosition.getY() - 1; i < Main.gameBoard.getSize() && j >= 0; i++, j--) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(i, j), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
            case NW: {
                for (int i = entity.entityPosition.getX() - 1, j = entity.entityPosition.getY() - 1; i >= 0 && j >= 0; i--, j--) {
                    int currentAmountOfFoodEaten = BoardEntity.makeMove(new EntityPosition(i, j), entity);
                    if (currentAmountOfFoodEaten == -1) break;
                    amountOfFoodEaten += currentAmountOfFoodEaten;
                }
                break;
            }
        }

        Main.gameBoard.addEntityToDelete(entity.entityPosition);
        Main.gameBoard.deleteEntities();
        return amountOfFoodEaten;
    }
}

/**
 * Represents a Butterfly, a type of Insect that can move orthogonally on a game board.
 * Extends the Insect class and implements the OrthogonalMoving interface.
 */
class Butterfly extends Insect implements OrthogonalMoving {
    /**
     * Constructs a Butterfly object with the specified entity position and color.
     *
     * @param entityPosition The position of the Butterfly on the game board.
     * @param color          The color of the Butterfly.
     */
    public Butterfly(EntityPosition entityPosition, InsectColor color) {
        super(entityPosition, color);
    }

    /**
     * Calculates the best direction for the Butterfly to move based on the current game board state.
     * Updates the bestDirection field of the Butterfly.
     */
    @Override
    public void getBestDirection() {
        Map<Direction, Integer> sums = new HashMap<>();

        for (Map.Entry<EntityPosition, BoardEntity> element : Main.gameBoard.getBoardData().entrySet()) {
            if (!(element.getValue() instanceof FoodPoint)) continue;

            Direction currentDirection = this.entityPosition.findDirection(element.getKey(), OrthogonalMoving.class.getSimpleName());
            if (currentDirection == null || currentDirection.isDiagonal()) continue;

            updateValueInSums(sums, currentDirection, ((FoodPoint) element.getValue()).value);
        }

        this.bestDirection = findMaxInSums(sums);
        if (this.bestDirection != null) return;
        this.bestDirection = findFirstDirection(1, true, false);
    }

    /**
     * Travels in the calculated best direction and returns the number of steps taken.
     * @return The number of steps taken.
     */
    @Override
    public int travelDirection() {
        getBestDirection();
        if (this.bestDirection == null) return 0;
        return travelOrthogonally(this.bestDirection, this, 1);
    }
}

/**
 * Represents an Ant, a type of Insect that can move orthogonally and diagonally.
 */
class Ant extends Insect implements OrthogonalMoving, DiagonalMoving {
    /**
     * Constructs a new Ant with the specified position and color.
     *
     * @param position the position of the Ant
     * @param color the color of the Ant
     */
    public Ant(EntityPosition position, InsectColor color) {
        super(position, color);
    }

    /**
     * Determines the best direction for the Ant to move based on the available food points.
     * Updates the bestDirection field of the Ant.
     */
    @Override
    public void getBestDirection() {
        Map<Direction, Integer> sums = new HashMap<>();

        for (Map.Entry<EntityPosition, BoardEntity> element : Main.gameBoard.getBoardData().entrySet()) {
            if (!(element.getValue() instanceof FoodPoint)) continue;

            Direction currentDirection = this.entityPosition.findDirection(element.getKey(), "All");
            if (currentDirection == null) continue;

            updateValueInSums(sums, currentDirection, ((FoodPoint) element.getValue()).value);
        }

        this.bestDirection = findMaxInSums(sums);
        if (this.bestDirection != null) return;
        this.bestDirection = findFirstDirection(1, true, true);
    }

    /**
     * Moves the Ant in the best direction determined by getBestDirection() method.
     *
     * @return the distance traveled by the Ant in the chosen direction
     */
    @Override
    public int travelDirection() {
        getBestDirection();
        if (this.bestDirection == null) return 0;
        return (this.bestDirection.isOrthogonal() ? travelOrthogonally(this.bestDirection, this, 1) : travelDiagonally(this.bestDirection, this));
    }
}

/**
 * Represents a Spider, a type of Insect that can move diagonally.
 * Inherits from the Insect class and implements the DiagonalMoving interface.
 */
class Spider extends Insect implements DiagonalMoving {

    /**
     * Constructs a Spider object with the given position and color.
     * 
     * @param position the position of the Spider on the game board
     * @param color the color of the Spider
     */
    public Spider(EntityPosition position, InsectColor color) {
        super(position, color);
    }

    /**
     * Calculates the best direction for the Spider to move towards the FoodPoints on the game board.
     * The best direction is determined by finding the direction that leads to the FoodPoint with the highest value.
     * If no such direction is found, the Spider will move in the first available direction.
     */
    @Override
    public void getBestDirection() {
        Map<Direction, Integer> sums = new HashMap<>();

        for (Map.Entry<EntityPosition, BoardEntity> element : Main.gameBoard.getBoardData().entrySet()) {
            if (!(element.getValue() instanceof FoodPoint)) continue;

            Direction currentDirection = this.entityPosition.findDirection(element.getKey(), DiagonalMoving.class.getSimpleName());
            if (currentDirection == null || currentDirection.isOrthogonal()) continue;

            updateValueInSums(sums, currentDirection, ((FoodPoint) element.getValue()).value);
        }

        this.bestDirection = findMaxInSums(sums);
        if (this.bestDirection != null) return;
        this.bestDirection = findFirstDirection(1, false, true);
    }

    /**
     * Determines the travel direction for the Spider based on the best direction calculated by getBestDirection().
     * If no best direction is found, returns 0.
     * 
     * @return the travel direction for the Spider
     */
    @Override
    public int travelDirection() {
        getBestDirection();
        if (this.bestDirection == null) return 0;
        return travelDiagonally(this.bestDirection, this);
    }
}

/**
 * Represents a Grasshopper, a type of Insect that can move orthogonally on a game board.
 * Implements the OrthogonalMoving interface.
 */
class Grasshopper extends Insect implements OrthogonalMoving {
    /**
     * Constructs a Grasshopper object with the specified position and color.
     *
     * @param position the position of the Grasshopper on the game board
     * @param color the color of the Grasshopper
     */
    public Grasshopper(EntityPosition position, InsectColor color) {
        super(position, color);
    }

    /**
     * Calculates the best direction for the Grasshopper to move towards food points on the game board.
     * Updates the bestDirection field of the Grasshopper.
     */
    @Override
    public void getBestDirection() {
        Map<Direction, Integer> sums = new HashMap<>();

        for (Map.Entry<EntityPosition, BoardEntity> element : Main.gameBoard.getBoardData().entrySet()) {
            if (!(element.getValue() instanceof FoodPoint)) continue;

            Direction currentDirection = this.entityPosition.findDirection(element.getKey(), OrthogonalMoving.class.getSimpleName());
            if (currentDirection == null || currentDirection.isDiagonal()) continue;
            if ((currentDirection == Direction.N || currentDirection == Direction.S) && Math.abs(element.getKey().getX() - this.entityPosition.getX()) % 2 != 0 || (currentDirection == Direction.W || currentDirection == Direction.E) && Math.abs(element.getKey().getY() - this.entityPosition.getY()) % 2 != 0) continue;

            updateValueInSums(sums, currentDirection, ((FoodPoint) element.getValue()).value);
        }

        this.bestDirection = findMaxInSums(sums);
        if (this.bestDirection != null) return;
        this.bestDirection = findFirstDirection(2, true, false);
    }

    /**
     * Moves the Grasshopper in the best direction determined by the getBestDirection() method.
     * Returns the number of steps the Grasshopper traveled.
     *
     * @return the number of steps traveled by the Grasshopper
     */
    @Override
    public int travelDirection() {
        getBestDirection();
        if (this.bestDirection == null) return 0;
        return travelOrthogonally(this.bestDirection, this, 2);
    }
}

class Exception extends java.lang.Exception {
    public String getMessage() {
        return "Parent exception was raised";
    }
}

class InvalidBoardSizeException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid board size";
    }
}

class InvalidNumberOfInsectsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of insects";
    }
}

class InvalidNumberOfFoodPointsException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid number of food points";
    }
}

class InvalidInsectColorException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid insect color";
    }
}

class InvalidInsectTypeException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid insect type";
    }
}

class InvalidEntityPositionException extends Exception {
    @Override
    public String getMessage() {
        return "Invalid entity position";
    }
}

class DuplicateInsectException extends Exception {
    @Override
    public String getMessage() {
        return "Duplicate insects";
    }
}

class TwoEntitiesOnSamePositionException extends Exception {
    @Override
    public String getMessage() {
        return "Two entities in the same position";
    }
}
