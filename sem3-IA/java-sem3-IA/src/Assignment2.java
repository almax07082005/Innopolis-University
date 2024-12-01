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
