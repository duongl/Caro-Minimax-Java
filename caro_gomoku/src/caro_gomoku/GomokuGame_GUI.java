/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package caro_gomoku;
/**
 *
 * @author Lê Thành Thái Dương
 */
//pve chốt
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
//pve_chốt
public class GomokuGame_GUI extends JFrame implements ActionListener {

    private final int BOARD_SIZE = 5;
    private final int WIN_CONDITION = 5;
    private final int MAX_DEPTH = 3;

    private JPanel boardPanel;
    private JButton[][] boardButtons;
    private boolean currentPlayerX;
    private boolean endGame;
    private boolean newGame;

    //Constructor
    public GomokuGame_GUI() {
        setTitle("Gomoku Game");
        setSize(700, 700);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

        boardPanel = new JPanel();
        boardPanel.setLayout(new GridLayout(5, 5, 3, 3));
        boardPanel.setPreferredSize(new Dimension(600, 600));

        boardButtons = new JButton[5][5];

        // Tạo các ô vuông và thêm chúng vào bảng
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardButtons[i][j] = new JButton();
                boardButtons[i][j].setBackground(Color.WHITE);
                boardButtons[i][j].addActionListener(this);
                boardPanel.add(boardButtons[i][j]);
            }
        }

        // Tạo các nút mới và đặt chúng bên dưới bảng
        //Chơi mới
        JButton newGameBtn = new JButton("Chơi Mới");
        newGameBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                newGame();
            }
        });
        //Thoát Game
        JButton exitBtn = new JButton("Thoát Game");
        exitBtn.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                exitGame();
            }
        });

        JPanel controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(1, 2, 30, 5));
        controlPanel.setPreferredSize(new Dimension(600, 50));
        controlPanel.add(newGameBtn);
        controlPanel.add(exitBtn);

        // Đặt bảng và bảng kiểm soát vào chính giữa trên giao diện
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(null);
        boardPanel.setBounds(50, 50, 600, 600);
        controlPanel.setBounds(50, 0, 600, 50);
        mainPanel.setPreferredSize(new Dimension(700,700));
        mainPanel.add(boardPanel);
        mainPanel.add(controlPanel);
        add(mainPanel);

        // Khởi động game mới
        newGame();
        setVisible(true);
    }

    //Tạo game mới
    public void newGame() {

        currentPlayerX = true;
        endGame = false;
        newGame = false;

        // Xóa bảng và đổi màu ô vuông thành màu trắng
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                boardButtons[i][j].setText("");
                boardButtons[i][j].setBackground(Color.WHITE);
            }
        }

        //Nếu máy đánh trước giữ giá trị O
        if (getCurrentPlayer().equals("O")) {
            computerMove();
        }

    }

    // Gửi thông báo kết quả game
    public void displayGameOver(String message) {
        JOptionPane.showMessageDialog(this, message);
    }

    //Thoát game
    public void exitGame() {
    int confirmed = JOptionPane.showConfirmDialog(this,
        "Bạn có chắc chắn muốn thoát game không?",
        "Exit",
        JOptionPane.YES_NO_OPTION);

    if (confirmed == JOptionPane.YES_OPTION) {
        System.exit(0);
    }
}

//    public void exitGame() {
//        System.exit(0);
//    }

    // Lấy người chơi hiện tại
    public String getCurrentPlayer() {
        if (currentPlayerX) {
            return "X";
        } else {
            return "O";
        }
    }

    //Thực hiện thao tác khi người chơi được chọn
    public void actionPerformed(ActionEvent event) {
        JButton clickedButton = (JButton) event.getSource();
        int row = -1;
        int col = -1;

        // Tìm thứ tự hàng và cột của ô được chọn
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (clickedButton == boardButtons[i][j]) {
                    row = i;
                    col = j;
                    break;
                }
            }
        }

        // Kiểm tra ô vuông có sẵn
        if (!endGame && clickedButton.getText().equals("")) {
            clickedButton.setText(getCurrentPlayer());

            // Kiểm tra trạng thái game
            if (checkWin(row, col)) {
                displayGameOver("Player " + getCurrentPlayer() + " thắng!");
                endGame = true;
            } else if (checkTie()) {
                displayGameOver("Hòa");
                endGame = true;
            } else {
                currentPlayerX = !currentPlayerX;
                computerMove();
                currentPlayerX = !currentPlayerX;
            }

        }
    }

    //Kiểm tra chiến thắng
    public boolean checkWin(int row, int col) {
        String player = getCurrentPlayer();

        //Kiểm tra hàng
        for (int i = 0; i < 5; i++) {
            if (!boardButtons[row][i].getText().equals(player)) {
                break;
            }
            if (i == 4) {
                return true;
            }
        }

        //Kiểm tra cột
        for (int i = 0; i < 5; i++) {
            if (!boardButtons[i][col].getText().equals(player)) {
                break;
            }
            if (i == 4) {
                return true;
            }
        }

        //Kiểm tra đường chéo từ trái sang phải
        if (row == col) {
            for (int i = 0; i < 5; i++) {
                if (!boardButtons[i][i].getText().equals(player)) {
                    break;
                }
                if (i == 4) {
                    return true;
                }
            }
        }

        //Kiểm tra đường chéo từ phải sang trái
        if (row + col == 4) {
            for (int i = 0; i < 5; i++) {
                if (!boardButtons[i][4 - i].getText().equals(player)) {
                    break;
                }
                if (i == 4) {
                    return true;
                }
            }
        }

        //Nếu không có chiến thắng
        return false;
    }

    //Kiểm tra hòa
    public boolean checkTie() {
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                if (boardButtons[i][j].getText().equals("")) {
                    return false;
                }
            }
        }
        return true;
    }
public void computerMove() {
    int[] move;
    int row, col;

    // Kiểm tra xem máy tính có thể thắng ở nước tiếp theo không
    move = minimax(boardButtons, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
    if (move[0] == 10) {
        row = move[1];
        col = move[2];
    }
    // Nếu máy không thắng được thì kiểm tra xem con người có thắng được không và chặn
    else {
        move = minimax(boardButtons, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
        // Nếu nước đi là người thì đặt isMaximizingPlayer là true
        if (move[0] == -10) {
            row = move[1];
            col = move[2];
        }
        // Ngược lại, chọn nước đi với điểm đánh giá cao nhất.
        else {
            move = minimax(boardButtons, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, true);
            row = move[1];
            col = move[2];
        }
    }

    boardButtons[row][col].setText(getCurrentPlayer());
    boardButtons[row][col].setBackground(Color.RED);

    if (checkWin(row, col)) {
        displayGameOver("Máy thắng!");
        endGame = true;
    } else if (checkTie()) {
        displayGameOver("Hòa");
        endGame = true;
    }
}

//    public void computerMove() {
//        int[] move = minimax(boardButtons, MAX_DEPTH, Integer.MIN_VALUE, Integer.MAX_VALUE, false);
//        int row = move[1];
//        int col = move[2];
//
//        boardButtons[row][col].setText(getCurrentPlayer());
//        boardButtons[row][col].setBackground(Color.RED);
//
//        if (checkWin(row, col)) {
//            displayGameOver("Máy thắng!");
//            endGame = true;
//        } else if (checkTie()) {
//            displayGameOver("Hòa");
//            endGame = true;
//        }
//    }

    public int[] minimax(JButton[][] board, int depth, int alpha, int beta, boolean isMaximizingPlayer) {
        if (depth == 0 || checkTie()) {
            int score = evaluateBoard(board);
            return new int[] {score, -1, -1};
        }

        int bestValue = isMaximizingPlayer ? Integer.MIN_VALUE : Integer.MAX_VALUE;
        int bestRow = -1;
        int bestCol = -1;

        for (int row = 0; row < BOARD_SIZE; row++) {
            for (int col = 0; col < BOARD_SIZE; col++) {
                if (board[row][col].getText().equals("")) {
                    board[row][col].setText(isMaximizingPlayer ? "O" : "X");
                    int[] currentValue = minimax(board, depth - 1, alpha, beta, !isMaximizingPlayer);
                    board[row][col].setText("");

                    if (isMaximizingPlayer && currentValue[0] > bestValue) {
                        bestValue = currentValue[0];
                        bestRow = row;
                        bestCol = col;
                        alpha = Math.max(alpha, bestValue);
                    } else if (!isMaximizingPlayer && currentValue[0] < bestValue) {
                        bestValue = currentValue[0];
                        bestRow = row;
                        bestCol = col;
                        beta = Math.min(beta, bestValue);
                    }

                    if (beta <= alpha) {
                        break;
                    }
                }
            }
        }

        return new int[] {bestValue, bestRow, bestCol};
    }

    public int evaluateBoard(JButton[][] board) {
        // Xác định giá trị của bảng dựa trên số lượng hàng, cột và đường chéo chứa các quân cờ liên tiếp
        // Điều này có thể được tùy chỉnh để đánh giá hiệu quả hơn
        int score = 0;

        // Kiểm tra hàng và cột
        for (int i = 0; i < BOARD_SIZE; i++) {
            int rowScore = 0;
            int colScore = 0;
            for (int j = 0; j < BOARD_SIZE; j++) {
                rowScore += evaluateCell(board[i][j]);
                colScore += evaluateCell(board[j][i]);
            }
            score += rowScore + colScore;
        }

        // Kiểm tra đường chéo từ trái sang phải
        int diagonalScore1 = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            diagonalScore1 += evaluateCell(board[i][i]);
        }
        score += diagonalScore1;

        // Kiểm tra đường chéo từ phải sang trái
        int diagonalScore2 = 0;
        for (int i = 0; i < BOARD_SIZE; i++) {
            diagonalScore2 += evaluateCell(board[i][BOARD_SIZE - 1 - i]);
        }
        score += diagonalScore2;

        return score;
    }

    public int evaluateCell(JButton cell) {
        String cellText = cell.getText();
        if (cellText.equals("O")) {
            return 1;
        } else if (cellText.equals("X")) {
            return -1;
        } else {
            return 0;
        }
    }

    public static void main(String[] args) {
        GomokuGame_GUI game = new GomokuGame_GUI();
        game.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        game.pack();
        game.setVisible(true);
    }
}