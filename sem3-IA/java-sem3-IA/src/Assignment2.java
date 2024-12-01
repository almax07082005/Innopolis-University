import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.function.Consumer;
import java.util.stream.Stream;

@FunctionalInterface
interface FitnessFunction {
    Integer calculate(Board board);
}

interface InitGeneration {
    void generate(Board board);
}

interface Crossover {
    Board run(Board board1, Board board2);
}

interface Mutation {
    void run(Board board, float probability);
}

/**
 * The FitnessFunctions interface provides a set of predefined fitness functions
 * for evaluating the fitness of a board configuration in a puzzle game.
 * 
 * <p>
 * The interface includes a quadratic fitness function that calculates the fitness
 * of a board based on the number of repeated values in rows, columns, and subgrids.
 * The fitness value is computed by summing the squares of the differences between
 * the actual counts and the expected counts of values in each row, column, and subgrid.
 * </p>
 * 
 * <p>
 * The quadratic fitness function is defined as follows:
 * <ul>
 * <li>For each row, the function counts the occurrences of each value and computes
 * the sum of the squares of the differences between the actual counts and the expected counts.</li>
 * <li>For each column, the function performs the same computation as for rows.</li>
 * <li>For each 3x3 subgrid, the function counts the occurrences of each value and computes
 * the sum of the squares of the differences between the actual counts and the expected counts.</li>
 * </ul>
 * The final fitness value is the sum of the fitness values for all rows, columns, and subgrids.
 * </p>
 * 
 * <p>
 * The fitness function assumes that the board is represented by a {@code Board} object
 * with a method {@code getCell(int row, int col)} that returns a {@code Cell} object.
 * The {@code Cell} object has a method {@code getValue()} that returns the value of the cell.
 * A value of -1 indicates an empty cell.
 * </p>
 * 
 * <p>
 * The {@code Board.BOARD_SIZE} constant is used to determine the size of the board.
 * </p>
 */
interface FitnessFunctions {
    FitnessFunction quadraticFitnessFunction = board -> {
        Integer fitness = 0;

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            List<Integer> rowAmounts = new ArrayList<>(Collections.nCopies(Board.BOARD_SIZE, 0));
            List<Integer> colAmounts = new ArrayList<>(Collections.nCopies(Board.BOARD_SIZE, 0));

            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                int rowValue = board.getCell(i, j).getValue();
                if (rowValue != -1) {
                    rowAmounts.set(rowValue - 1, rowAmounts.get(rowValue - 1) + 1);
                }

                int colValue = board.getCell(j, i).getValue();
                if (colValue != -1) {
                    colAmounts.set(colValue - 1, colAmounts.get(colValue - 1) + 1);
                }
            }

            fitness += rowAmounts.stream()
                    .map(amount -> (int) Math.pow(amount - 1, 2))
                    .reduce(0, Integer::sum);
            fitness += colAmounts.stream()
                    .map(amount -> (int) Math.pow(amount - 1, 2))
                    .reduce(0, Integer::sum);
        }

        for (int i = 0; i < Board.BOARD_SIZE; i += 3) {
            for (int j = 0; j < Board.BOARD_SIZE; j += 3) {
                List<Integer> subgridAmounts = new ArrayList<>(Collections.nCopies(Board.BOARD_SIZE, 0));
                for (int k = i; k < i + 3; k++) {
                    for (int l = j; l < j + 3; l++) {
                        subgridAmounts.set(
                                board.getCell(k, l).getValue() - 1,
                                subgridAmounts.get(board.getCell(k, l).getValue() - 1) + 1
                        );
                    }
                }
                fitness += subgridAmounts.stream()
                        .map(amount -> (int) Math.pow(amount - 1, 2))
                        .reduce(0, Integer::sum);
            }
        }

        return fitness;
    };
}

/**
 * The Assignment2 class is the entry point for the application.
 * It initializes a board and uses an algorithm to solve a problem.
 * The main method sets up the board and runs the algorithm.
 * 
 * <p>Usage:</p>
 * <pre>
 * {@code
 * public static void main(String[] args) {
 *     Assignment2.main(args);
 * }}
 * </pre>
 * 
 * <p>Methods:</p>
 * <ul>
 *   <li>{@link #main(String[])} - The main method that initializes the board and runs the algorithm.</li>
 *   <li>{@link #initializeBoard()} - Initializes the board with user input.</li>
 *   <li>{@link #cloneInitBoard()} - Clones the initial board.</li>
 * </ul>
 * 
 * <p>Dependencies:</p>
 * <ul>
 *   <li>{@code Board} - Represents the game board.</li>
 *   <li>{@code Algorithm} - Represents the algorithm used to solve the problem.</li>
 *   <li>{@code CleverCrossover} - A crossover strategy for the algorithm.</li>
 *   <li>{@code CleverMutation} - A mutation strategy for the algorithm.</li>
 *   <li>{@code FitnessFunctions} - Contains fitness functions for the algorithm.</li>
 *   <li>{@code CleverInitGeneration} - Initializes the generation for the algorithm.</li>
 *   <li>{@code Cell} - Represents a cell on the board.</li>
 *   <li>{@code MutableCell} - Represents a mutable cell on the board.</li>
 *   <li>{@code ImmutableCell} - Represents an immutable cell on the board.</li>
 * </ul>
 * 
 * <p>Note:</p>
 * <ul>
 *   <li>The board size is determined by {@code Board.BOARD_SIZE}.</li>
 *   <li>User input is required to initialize the board.</li>
 * </ul>
 */
public class Assignment2 {
    private static Board initBoard;

    public static void main(String[] args) {
        initializeBoard();
        Algorithm algorithm = new Algorithm(
                new CleverCrossover(),
                new CleverMutation(),
                FitnessFunctions.quadraticFitnessFunction,
                new CleverInitGeneration()
        );
        System.out.println(algorithm.solve());
    }

    public static void initializeBoard() {
        if (initBoard != null) {
            return;
        }
        initBoard = new Board();
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < Board.BOARD_SIZE; i++) {
            List<Cell> row = new ArrayList<>();
            String[] line = scanner.nextLine().split(" ");
            for (int j = 0; j < Board.BOARD_SIZE; j++) {
                if (line[j].equals("-")) {
                    row.add(new MutableCell(i, j));
                } else {
                    row.add(new ImmutableCell(i, j, Integer.parseInt(line[j])));
                }
            }
            initBoard.getBoard().add(row);
        }
    }

    static Board cloneInitBoard() {
        return initBoard.clone();
    }
}

/**
 * The Algorithm class implements a genetic algorithm to solve a problem represented by the Board class.
 * It uses crossover, mutation, and fitness functions to evolve a population of solutions.
 */
class Algorithm {
    public static final Random random = new Random();
    private static final int POPULATION_SIZE = 1000;
    private final Crossover crossover;
    private final Mutation mutation;
    private final FitnessFunction fitnessFunction;
    private final InitGeneration initGeneration;
    private final float probability;

    public Algorithm(Crossover crossover, Mutation mutation, FitnessFunction fitnessFunction, InitGeneration initGeneration) {
        this.crossover = crossover;
        this.mutation = mutation;
        this.fitnessFunction = fitnessFunction;
        this.initGeneration = initGeneration;
        this.probability = 0;
    }

    public Algorithm(
            Crossover crossover,
            Mutation mutation,
            FitnessFunction fitnessFunction,
            InitGeneration initGeneration,
            float probability
    ) {
        this.crossover = crossover;
        this.mutation = mutation;
        this.fitnessFunction = fitnessFunction;
        this.initGeneration = initGeneration;
        this.probability = probability;
    }

    public Board solve() {
        List<Board> population = new ArrayList<>(Stream.generate(Assignment2::cloneInitBoard)
                .limit(POPULATION_SIZE)
                .toList()
        );

        int i = 0;
        do {
            generation(population);
            if (++i % 100 == 0) {
                System.out.println("Generation " + i + ": " + fitnessFunction.calculate(population.getFirst()));
            }
        } while (fitnessFunction.calculate(population.getFirst()) != 0);

        return population.getFirst();
    }

    public void generation(List<Board> population) {
        for (Board board : population) {
            initGeneration.generate(board);
        }

        List<Board> newPopulation = new ArrayList<>();
        for (int i = 0; i < POPULATION_SIZE - 1; i += 2) {
            newPopulation.add(crossover.run(population.get(i), population.get(i + 1)));
        }

        for (Board board : newPopulation) {
            mutation.run(board, probability);
        }

        population.addAll(newPopulation);
        population.sort((board1, board2) -> Integer.compare(
                fitnessFunction.calculate(board1),
                fitnessFunction.calculate(board2)
        ));

        population.subList(POPULATION_SIZE, population.size()).clear();
    }
}

/**
 * The CleverInitGeneration class implements the InitGeneration interface and provides
 * a method to generate initial values for a game board. It assigns random values to 
 * mutable cells in the board.
 */
class CleverInitGeneration implements InitGeneration {
    @Override
    public void generate(Board board) {
        for (List<Cell> row : board.getBoard()) {
            int mutableAmount = (int) row.stream()
                    .filter(cell -> cell instanceof MutableCell)
                    .count();
            List<Integer> randomNumbers = Algorithm.random
                    .ints(mutableAmount, 1, 10)
                    .boxed()
                    .toList();
            int index = 0;

            for (Cell cell : row) {
                if (cell instanceof MutableCell) {
                    ((MutableCell) cell).setValue(randomNumbers.get(index++));
                }
            }
        }
    }
}

/**
 * The CleverCrossover class implements the Crossover interface and provides
 * a method to perform a crossover operation on two given boards.
 * 
 * <p>The crossover operation creates a new board by combining cells from
 * two parent boards. For each cell in the new board, it randomly selects
 * the value from one of the corresponding cells in the parent boards.
 * 
 * <p>This class uses the {@link Assignment2#cloneInitBoard()} method to
 * initialize the new board and the {@link Algorithm#random} instance to
 * generate random values for the crossover decision.
 * 
 * <p>Note: Only mutable cells in the board are considered for crossover.
 * 
 * @see Crossover
 * @see Board
 * @see MutableCell
 * @see Assignment2#cloneInitBoard()
 * @see Algorithm#random
 */
class CleverCrossover implements Crossover {
    @Override
    public Board run(Board board1, Board board2) {
        Board board = Assignment2.cloneInitBoard();

        board.executeOperations(cell -> {
            if (cell instanceof MutableCell) {
                ((MutableCell) cell).setValue(Algorithm.random.nextFloat() > 0.5
                        ? (board1.getCell(cell.getRow(), cell.getCol())).getValue()
                        : (board2.getCell(cell.getRow(), cell.getCol())).getValue()
                );
            }
        });

        return board;
    }
}

/**
 * The CleverMutation class implements the Mutation interface and provides
 * a specific mutation strategy for a Board. The mutation strategy involves
 * randomly selecting two cells on the board and swapping their values if
 * both cells are instances of MutableCell. The mutation process stops after
 * one successful swap.
 */
class CleverMutation implements Mutation {
    @Override
    public void run(Board board, float probability) {
        int count = 0;

        while (true) {
            int row1 = Algorithm.random.nextInt(Board.BOARD_SIZE);
            int col1 = Algorithm.random.nextInt(Board.BOARD_SIZE);
            int row2 = Algorithm.random.nextInt(Board.BOARD_SIZE);
            int col2 = Algorithm.random.nextInt(Board.BOARD_SIZE);

            Cell cell1 = board.getCell(row1, col1);
            Cell cell2 = board.getCell(row2, col2);

            if (cell1 instanceof MutableCell && cell2 instanceof MutableCell) {
                ((MutableCell) cell1).setValue(cell2.getValue());
                if (count++ == 1) {
                    break;
                }
            }
        }
    }
}

/**
 * The RandomInitGeneration class implements the InitGeneration interface
 * and provides a method to generate a random initial state for a given Board.
 * 
 * <p>This class is responsible for initializing the board by setting random values
 * to the cells that are instances of MutableCell. The values are generated using
 * a random number generator and are in the range of 1 to 9 inclusive.</p>
 * 
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * Board board = new Board();
 * InitGeneration initGen = new RandomInitGeneration();
 * initGen.generate(board);
 * }
 * </pre>
 * 
 * @see InitGeneration
 * @see Board
 * @see MutableCell
 */
class RandomInitGeneration implements InitGeneration {
    @Override
    public void generate(Board board) {
        board.executeOperations(cell -> {
            if (cell instanceof MutableCell) {
                ((MutableCell) cell).setValue(Algorithm.random.nextInt(9) + 1);
            }
        });
    }
}

/**
 * The SimpleCrossover class implements the Crossover interface and provides
 * a method to perform a simple crossover operation on two given boards.
 * 
 * <p>This class overrides the run method from the Crossover interface to
 * create a new board by combining cells from two parent boards. The value
 * of each cell in the new board is randomly selected from the corresponding
 * cells of the two parent boards.</p>
 * 
 * <p>The crossover operation is applied only to mutable cells, which are
 * instances of the MutableCell class. Immutable cells retain their original
 * values.</p>
 * 
 * <p>Usage example:</p>
 * <pre>
 * {@code
 * Board parent1 = ...; // Initialize parent board 1
 * Board parent2 = ...; // Initialize parent board 2
 * SimpleCrossover crossover = new SimpleCrossover();
 * Board offspring = crossover.run(parent1, parent2);
 * }
 * </pre>
 * 
 * @see Crossover
 * @see Board
 * @see MutableCell
 */
class SimpleCrossover implements Crossover {
    @Override
    public Board run(Board board1, Board board2) {
        Board board = Assignment2.cloneInitBoard();

        board.executeOperations(cell -> {
            if (cell instanceof MutableCell) {
                ((MutableCell) cell).setValue(Algorithm.random.nextInt(2) == 0
                        ? (board1.getCell(cell.getRow(), cell.getCol())).getValue()
                        : (board2.getCell(cell.getRow(), cell.getCol())).getValue()
                );
            }
        });

        return board;
    }
}

/**
 * The SimpleMutation class implements the Mutation interface and provides
 * a method to perform mutation on a given Board object. The mutation is
 * applied to cells of the board based on a specified probability.
 */
class SimpleMutation implements Mutation {
    @Override
    public void run(Board board, float probability) {
        board.executeOperations(cell -> {
            if (cell instanceof MutableCell && Algorithm.random.nextFloat() < probability) {
                ((MutableCell) cell).setValue(Algorithm.random.nextInt(9) + 1);
            }
        });
    }
}

/**
 * The {@code Board} class represents a 9x9 board consisting of cells.
 * It provides methods to clone the board, execute operations on each cell,
 * and retrieve the board or specific cells.
 * <p>
 * This class implements the {@code Cloneable} interface to allow cloning of the board.
 * </p>
 */
class Board implements Cloneable {
    public static final int BOARD_SIZE = 9;
    private final List<List<Cell>> board;

    public Board() {
        board = new ArrayList<>();
    }

    @Override
    public Board clone() {
        try {
            return (Board) super.clone();
        } catch (CloneNotSupportedException exception) {
            throw new RuntimeException(exception);
        }
    }

    void executeOperations(Consumer<Cell> consumer) {
        for (List<Cell> row : board) {
            for (Cell cell : row) {
                consumer.accept(cell);
            }
        }
    }

    public List<List<Cell>> getBoard() {
        return board;
    }

    public Cell getCell(int row, int col) {
        return board.get(row).get(col);
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        board.forEach(row -> {
            row.forEach(cell -> stringBuilder.append(cell.getValue()).append(" "));
            stringBuilder.append("\n");
        });
        return stringBuilder.toString();
    }
}

/**
 * The MutableCell class represents a cell in a grid that can have its value changed.
 * It extends the Cell class and adds a mutable value property.
 */
class MutableCell extends Cell {
    private int value;

    public MutableCell(int row, int col) {
        super(row, col);
        value = -1;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

/**
 * The {@code ImmutableCell} class represents a cell in a grid that has an immutable value.
 * This class extends the {@code Cell} class and adds a final value that cannot be changed
 * once the cell is created.
 * <p>
 * Instances of this class are immutable and thread-safe.
 * </p>
 * 
 * @see Cell
 */
class ImmutableCell extends Cell {
    private final int value;

    public ImmutableCell(int row, int col, int value) {
        super(row, col);
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}

/**
 * The {@code Cell} class represents an abstract cell with a specific row and column.
 * This class is intended to be extended by other classes that will provide specific
 * implementations for the {@code getValue} method.
 */
abstract class Cell {
    private final int row;
    private final int col;

    public Cell(int row, int col) {
        this.row = row;
        this.col = col;
    }

    public abstract int getValue();

    public int getRow() {
        return row;
    }

    public int getCol() {
        return col;
    }
}
