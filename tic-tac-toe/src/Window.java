import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Line2D;

public class Window extends JFrame {
    private Color color;
    private Color wcolor;
    private GameField field;
    private int cross_id = -1;
    private int circle_id = 1;
    private int cell_size;


    public Window(int cell_size){
        this.cell_size = cell_size;
        this.field = new GameField(5,10);
        setSize(cell_size*field.size+100,cell_size*field.size+120);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        color = Color.BLACK;
        wcolor = Color.WHITE;
        JPanel panel = new JPanel();
        getContentPane().add(panel);

        JButton new_game_button = new JButton("Start new game!");
        panel.add(new_game_button);

        new_game_button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                field.reset();
                Window.super.repaint();
            }
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                PointerInfo a = MouseInfo.getPointerInfo();
                Point b = a.getLocation();
                int x = (int) b.getX();
                int y = (int) b.getY();
                int x_cell = (x-50)/cell_size;
                int y_cell = (y-120)/cell_size;
                field.humanTurn(x_cell,y_cell);
                if (field.in_progress) {
                    if (field.checkWin(cross_id)) {
                        System.out.println("You won!");
                    } else {
                        field.aiTurn();
                        if (field.checkWin(circle_id)) System.out.println("AI won!");
                    }
                }
                Window.super.repaint();
            }
        });

    }

    public void paint(Graphics g){

        int width = field.size*cell_size+1;
        int height = field.size*cell_size+1;

        Image offscreen =  createImage(width, height);
        Graphics2D g2d = (Graphics2D) offscreen.getGraphics();
        g2d.setColor(getBackground());
        g2d.fillRect(0, 0, width, height);
        g2d.setColor(Color.BLACK);

        for(int i = 0; i <= field.size*cell_size; i+=cell_size){
            Line2D lin  = new Line2D.Float(i, 0, i, cell_size*field.size);
            g2d.draw(lin);

        }
        for(int i = 0; i <= field.size*cell_size ; i+=cell_size){
            Line2D lin  = new Line2D.Float(0, i, field.size*cell_size, i);
            g2d.draw(lin);

        }
        for(int i = 0; i < field.size; i++){
            for (int j = 0; j < field.size; j++){
                if(field.game_field[i][j] == 1){
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawOval(i*cell_size+1, j*cell_size+1, cell_size-1-3,cell_size-1-3);
                }else if (field.game_field[i][j] == -1) {
                    g2d.setColor(Color.BLACK);
                    g2d.setStroke(new BasicStroke(4));
                    g2d.drawLine(i*cell_size+2, j*cell_size+2, (i+1)*cell_size-2, (j+1)*cell_size-2);
                    g2d.drawLine(i*cell_size+2, (j+1)*cell_size-2, (i+1)*cell_size-2, j*cell_size+2);
                }else if (field.game_field[i][j] == 0){
                    g2d.setColor(Color.WHITE);
                    g2d.fillRect(i*cell_size+1, j*cell_size+1, cell_size-1,cell_size-1);
                    g2d.setColor(Color.BLACK);
                }

            }

        }

        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setColor(color);
        g2.drawImage( offscreen, 50, 100, this );
    }
}