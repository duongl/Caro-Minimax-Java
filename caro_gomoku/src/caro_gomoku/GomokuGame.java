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

//pvp_chốt
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class GomokuGame extends JFrame implements ActionListener {
    
    //Set kích thước bàn cờ
    private static final int ROWS = 10;
    private static final int COLS = 10;
    private static final int CELL_SIZE = 80;
    
    //Set tiêu đề cho các button
    private JButton[][] board = new JButton[ROWS][COLS];
    private JPanel gameBoard = new JPanel(new GridLayout(ROWS, COLS));
    private JLabel statusLabel = new JLabel("Chào mừng bạn đến với Caro Game!");
    private JButton newGameButton = new JButton("Game Mới");
    private JButton quitButton = new JButton("Thoát Game");
    private JButton aboutButton = new JButton("Luật chơi");
    private JButton undoButton = new JButton("Lùi Lại");

    private int[][] gameBoardData = new int[ROWS][COLS]; //mảng tạo bàn cờ
    private boolean isPlayer1Turn = true; //xác định người chơi
    private boolean gameOver = false; //xác định kết thúc

    private JFrame aboutFrame;

    private ArrayList<int[]> moves = new ArrayList<int[]>(); //thêm giá trị move vào mảng

    public GomokuGame() {

        add(gameBoard, BorderLayout.CENTER); // vtri giua man hinh
        //control panel dùng để thêm tphan dieu khien nằm vtri dưới man hinh
        JPanel controlPanel = new JPanel(new FlowLayout());
        controlPanel.add(statusLabel);
        controlPanel.add(newGameButton);
        controlPanel.add(quitButton);
        controlPanel.add(aboutButton);
        controlPanel.add(undoButton);
        add(controlPanel, BorderLayout.SOUTH); 

        newGameButton.addActionListener(this);
        quitButton.addActionListener(this);
        aboutButton.addActionListener(this);
        undoButton.addActionListener(this);

        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                JButton cell = new JButton("");
                cell.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
                cell.addActionListener(this);
                gameBoard.add(cell);
                board[row][col] = cell;
                gameBoardData[row][col] = 0; // empty cell = ô trống
            }
        }

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(COLS * CELL_SIZE, ROWS * CELL_SIZE + 50);
        setTitle("Caro Game");
        setVisible(true);
        
        // Set khi khởi động app sẽ ở giữa màn hình
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int centerX = (int) screenSize.getWidth() / 2;
        int centerY = (int) screenSize.getHeight() / 2;
        setLocation(centerX - getWidth() / 2, centerY - getHeight() / 2);
    }

    @Override
    public void actionPerformed(ActionEvent event) {
        Object source = event.getSource(); // lay doi tuong ra

        if (source == quitButton) {
            int option = JOptionPane.showConfirmDialog(null, "Bạn có chắc chắn muốn thoát game?", 
                                        "Thoát game", JOptionPane.YES_NO_OPTION);
        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
        } else if (source == newGameButton) {
            newGame();
        } else if (source == aboutButton) {
            showAbout();
        } else if (source == undoButton) {
            undoMove();
        } else if (!gameOver) {
            for (int row = 0; row < ROWS; row++) {
                for (int col = 0; col < COLS; col++) {
                    if (source == board[row][col] && gameBoardData[row][col] == 0) {
                        if (isPlayer1Turn) {
                            board[row][col].setText("X");
                            gameBoardData[row][col] = 1; // 1 đại diện cho player 1
                            statusLabel.setText("Lượt của Player 2");
                        } else {
                            board[row][col].setText("O");
                            gameBoardData[row][col] = 2; // 2 đại diện cho player 2
                            statusLabel.setText("Lượt của Player 1");
                        }

                        moves.add(new int[]{row, col, gameBoardData[row][col]}); //Thêm trạng thái di chuyển mới vào danh sách

                        if (hasWinner(row, col)) {
                            String winner = isPlayer1Turn ? "Player 1 (X)" : "Player 2 (O)";
                            statusLabel.setText(winner + " Đã thắng trò chơi!");
                            gameOver = true;
                        } else if (isBoardFull()) {
                            statusLabel.setText("Trò chơi hòa!");
                            gameOver = true;
                        } else {
                            isPlayer1Turn = !isPlayer1Turn;
                        }
                    }
                }
            }
        }
    }

    private void newGame() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                board[row][col].setText(""); // đặt ô trên ban co thành trống
                gameBoardData[row][col] = 0;
            }
        }

        moves.clear(); // Xóa danh sách di chuyển
        isPlayer1Turn = true;
        gameOver = false;
        statusLabel.setText("Lượt của Player 1");
    }

    private void showAbout() {
        JOptionPane.showMessageDialog(this,"Bàn cờ gồm 100 ô (10 dòng và 10 cột)"
                        + "\nNgười chơi sẽ chiến thắng khi có đường thẳng theo chiều dọc,"
                        + "\nchiều ngang hoặc chéo với chính xác 5 con cờ của mình." 
                        + "\n                                             Nhóm 7", 
                    "Luật chơi", JOptionPane.INFORMATION_MESSAGE);
    }

    private boolean hasWinner(int row, int col) {
    int player = isPlayer1Turn ? 1 : 2; // xác định lượt người chơi 1 or 2
    int count = 0;
    //count = 5 tức là chiến thắng
    // Kiểm tra chiều dọc
    for (int i = row - 4; i <= row + 4; i++) {
        if (i < 0 || i >= ROWS) {
            continue;
        }

        if (gameBoardData[i][col] == player) {
            count++;

            if (count >= 5) {
                JOptionPane.showMessageDialog(this, "Player " + player + " Đã thắng trò chơi!");
                return true;
            }
        } else {
            count = 0;
        }
    }

    // Kiểm tra chiều ngang
    count = 0;
    for (int j = col - 4; j <= col + 4; j++) {
        if (j < 0 || j >= COLS) {
            continue;
        }

        if (gameBoardData[row][j] == player) {
            count++;

            if (count >= 5) {
                JOptionPane.showMessageDialog(this, "Player " + player + " Đã thắng trò chơi!");
                return true;
            }
        } else {
            count = 0;
        }
    }

    // Kiểm tra hướng chéo (từ dưới cùng bên trái sang trên cùng bên phải)
    count = 0;
    for (int i = row - 4, j = col - 4; i <= row + 4 && j <= col + 4; i++, j++) {
        if (i < 0 || i >= ROWS || j < 0 || j >= COLS) {
            continue;
        }

        if (gameBoardData[i][j] == player) {
            count++;

            if (count >= 5) {
                JOptionPane.showMessageDialog(this, "Player " + player + " Đã thắng trò chơi!");
                return true;
            }
        } else {
            count = 0;
        }
    }

    // Kiểm tra hướng chéo (từ trên-trái xuống dưới-phải)
    count = 0;
    for (int i = row - 4, j = col + 4; i <= row + 4 && j >= col - 4; i++, j--) {
        if (i < 0 || i >= ROWS || j < 0 || j >= COLS) {
            continue;
        }

        if (gameBoardData[i][j] == player) {
            count++;

            if (count >= 5) {
                JOptionPane.showMessageDialog(this, "Player " + player + " Đã thắng trò chơi!");
                return true;
            }
        } else {
            count = 0;
        }
    }

    // Kiểm tra trận hòa
    if (isBoardFull()) {
        JOptionPane.showMessageDialog(this, "Trò chơi hòa!");
        return true;
    }

    return false;
}

    // kiểm tra bàn cờ đã đầy hay chưa
    private boolean isBoardFull() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                if (gameBoardData[row][col] == 0) {
                    return false;
                }
            }
        }
        return true;
    }

    private void undoMove() {
        if (moves.size() > 0) {
            int[] move = moves.remove(moves.size() - 1); // Lấy bước đi cuối cùng từ danh sách
            int row = move[0];
            int col = move[1];
            int player = move[2];

            board[row][col].setText(""); // sau đó làm trống dòng cột cuối cùng đó
            gameBoardData[row][col] = 0;

            isPlayer1Turn = (player == 1);
            statusLabel.setText((isPlayer1Turn ? "Player 1" : "Player 2") + "'s turn");
            gameOver = false;
        }
    }

    public static void main(String[] args) {
        new GomokuGame();
    }
    
}
