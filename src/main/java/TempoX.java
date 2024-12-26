package main.java;
// 1308 박재영. 내가 왜 이걸 만든다고 해서...

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

public class TempoX extends JFrame {


    private SqlContorller sqler = new SqlContorller("jdbc:mysql://localhost:3306", "root", "M73046497ac!");
    private Graphics screenGraphics;

    private Image image[] = new Image[ 4 ];
    private ImageIcon imageIconsBasic[] = new ImageIcon[ 10 ];
    private ImageIcon imageIconsEntered[] = new ImageIcon[ 10 ];
    private JButton Button[] = new JButton[ 10 ];
    private int XyWh[][] = new int[][] { { 1245, 0, 30, 30 }, { 850, 450, 400, 100 }, { 850, 580, 400, 100 }, { 850, 450, 400, 100 },
            { 850, 580, 400, 100 }, { 140, 310, 60, 60 }, { 1180, 310, 60, 60 },
            { 375, 580, 250, 67 }, { 665, 580, 250, 67 }, { 20, 50, 50, 46 } };
    private int menu[] = new int[] { 2, 8, 9, 1, 2, 3, 4, 5, 6, 7 };
    private boolean isSetVisible[] = new boolean[] { true, true, true, false, false, false, false, false, false, false };

    private JLabel menuBar = new JLabel( new ImageIcon( getClass().getResource("../Resources/images/menuBar.png" ) ) );

    private int mouseX, mouseY;

    private String[] MusicTitle = new String[] { "Ado - Bocca Della Verita", "DECO*27 - Rabbit Hole", "CLTH - Kakera" };

    private boolean isMainScreen = false;
    private boolean isGameScreen = false;

    ArrayList<Track> trackList = new ArrayList< Track >();

    private Music selectedMusic;
    private int nowSelected = 0;

    private JTextField idField = new JTextField();
    private JPasswordField passwordField = new JPasswordField();
    private JLabel loginStatusLabel = new JLabel("" );

    public static Game game;
    UserManager userManager = new UserManager();

    Music introMusic = new Music("introMusic.mp3", true );

    public TempoX() {

        createImage();
        assignmentImage();

        TrackList();

        System.out.println( trackList.get( nowSelected ).getStartMusic() );

        setUndecorated( true );
        setTitle( "TempoX" );
        setSize( Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT );
        setResizable( false );
        setIconImage( image[ 1 ] );
        setLocationRelativeTo( null );
        setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        setVisible( true );
        setBackground( new Color(0,0,0,0 ) );
        setLayout( null );

        addKeyListener( new KeyListener() );

        introMusic.start();

        ButtonSetting();

        menuBar.setBounds(0, 0, 1280, 30 );
        menuBar.addMouseListener( new MouseAdapter() {
            @Override
            public void mousePressed( MouseEvent e ) {
                mouseX = e.getX();
                mouseY = e.getY();
            }
        } );
        menuBar.addMouseMotionListener( new MouseMotionAdapter() {
            @Override
            public void mouseDragged( MouseEvent e ) {
                int x = e.getXOnScreen();
                int y = e.getYOnScreen();
                setLocation(x - mouseX, y - mouseY );
            }
        } );
        add( menuBar );

        setupLoginUI();
    }

    public void paint( Graphics g ) {
        image[ 2 ] = createImage( Main.SCREEN_WIDTH, Main.SCREEN_HEIGHT );
        screenGraphics = image[ 2 ].getGraphics();
        screenDraw( ( Graphics2D ) screenGraphics );
        g.drawImage( image[ 2 ], 0, 0, null );
    }

    public void screenDraw( Graphics2D g ) {
        g.drawImage( image[ 0 ], 0, 0, null );
        if ( isMainScreen ) {
            g.drawImage(image[ 3 ], 340, 100, null );
        }
        if ( isGameScreen ) {
            game.screenDraw( g );
        }
        paintComponents( g );
        try {
            Thread.sleep(5 );
        } catch ( Exception e ) {
            e.printStackTrace();
        }
        this.repaint();
    }

    public void selectTrack( int nowSelected ) {
        if ( selectedMusic != null )
            selectedMusic.close();

        image[ 3 ] = new ImageIcon( getClass().getResource("../Resources/images/" + trackList.get( nowSelected ).getStartImage() ) ).getImage();
        selectedMusic = new Music( trackList.get( nowSelected ).getStartMusic(), true );
        selectedMusic.start();
    }

    public void selectLeft() {
        if( nowSelected == 0 )
            nowSelected = trackList.size() - 1;
        else
            nowSelected--;
        selectTrack( nowSelected );
    }
    public void selectRight() {
        if( nowSelected == trackList.size() - 1 )
            nowSelected = 0;
        else
            nowSelected++;
        selectTrack( nowSelected );
    }

    public void gameStart( int nowSelected, String difficulty ) {
        if ( selectedMusic != null )
            selectedMusic.close();
        isMainScreen = false;
        LREH_SetVisTrue(false );
        image[ 0 ] = new ImageIcon( getClass().getResource("../Resources/images/" + trackList.get( nowSelected ).getGameImage() )).getImage();
        Button[ 9 ].setVisible( true );
        isGameScreen = true;
        game = new Game( trackList.get( nowSelected ).getTitleName(), difficulty, trackList.get( nowSelected ).getGameMusic() );
        game.start();
        setFocusable( true );
    }

    public void backMain() {
        isMainScreen = true;
        LREH_SetVisTrue(true );
        image[ 0 ] = new ImageIcon( getClass().getResource("../Resources/images/mainBackground.jpg" ) ).getImage();
        Button[ 9 ].setVisible( false );
        selectTrack( nowSelected );
        isGameScreen = false;
        game.close();
    }

    public void enterMain() {
        image[ 0 ] = new ImageIcon( getClass().getResource("../Resources/images/mainBackground.jpg" ) ).getImage();
        isMainScreen = true;
        gameStartSetVisible(false );
        LREH_SetVisTrue(true );
        introMusic.close();
        selectTrack(0 );
    }
    public void LREH_SetVisTrue( boolean isVisible ) {
        Button[ 5 ].setVisible( isVisible );
        Button[ 6 ].setVisible( isVisible );
        Button[ 7 ].setVisible( isVisible );
        Button[ 8 ].setVisible( isVisible );
    }

    public void buttonSetting( JButton button, int x, int y, int width, int height, ImageIcon EnteredImage, ImageIcon BasicImage, int menu, boolean isSetVisible ) {
        if ( !isSetVisible ) button.setVisible( false );
        button.setBounds( x, y, width, height );
        button.setBorderPainted( false );
        button.setContentAreaFilled( false );
        button.setFocusPainted( false );
        button.addMouseListener( new MouseAdapter() {
            @Override
            public void mouseEntered( MouseEvent e ) {
                button.setIcon( EnteredImage );
                button.setCursor( new Cursor( Cursor.HAND_CURSOR ) );
                Music buttonEnteredMusic = new Music("buttonEnteredMusic.mp3", false );
                buttonEnteredMusic.start();
            }
            @Override
            public void mouseExited( MouseEvent e ) {
                button.setIcon( BasicImage );
                button.setCursor( new Cursor( Cursor.DEFAULT_CURSOR ) );
            }
            @Override
            public void mousePressed( MouseEvent e ) {
                Music buttonPressedMusic = new Music("buttonPressedMusic.mp3", false );
                buttonPressedMusic.start();
                ButtonEvent( menu );
            }
        });
        add( button );
    }

    public void ButtonEvent( int menu ) {
        if ( menu == 1 ) {
            enterMain();
        } else if ( menu == 2 ) {
            try {
                Thread.sleep(750 );
            } catch ( InterruptedException e1 ) {
                e1.printStackTrace();
            }
            System.exit(0 );
        } else if ( menu == 3 ) {
            selectLeft();
        } else if ( menu == 4 ) {
            selectRight();
        } else if ( menu == 5 ) {
            gameStart(nowSelected, "Easy" );
        } else if ( menu == 6) {
            gameStart(nowSelected, "Hard" );
        } else if ( menu == 7 ) {
            backMain();
        } else if ( menu == 8 ) {
            Button[ 1 ].addActionListener(e -> {
                String id = idField.getText().trim();
                String password = new String( passwordField.getPassword() ).trim();
                if ( sqler.checkData("select * from javadb.usertable where userId = '" + id + "' and userPassword = '" + password + "';")) {
                    loginSetVisible(false );
                    gameStartSetVisible(true );
                } else {
                    loginStatusLabel.setText( "로그인 실패! 다시 시도해 주세요" );
                    loginStatusLabel.setForeground( Color.RED );
                }
            });
        } else if ( menu == 9 ) {
            Button[ 2 ].addActionListener(e -> {
                String id = idField.getText().trim();
                String password = new String( passwordField.getPassword() ).trim();
                sqler.insert("insert into javadb.usertable(userId, userPassword) values ('" + id + "','" + password + "');");
                if ( sqler.checkData("select * from javadb.usertable where userId = '" + id + "' and userPassword = '" + password + "';")) {
                    loginStatusLabel.setText( "회원가입이 완료되었습니다 로그인을 해주십시오!" );
                    loginStatusLabel.setForeground( Color.GREEN );
                } else {
                    sqler.delete("delete from javadb.usertable\nwhere userId = '" + id + "' and userPassword = '" + password + "';");
                    loginStatusLabel.setText( "회원가입이 실패했습니다. 아이디를 확인해 주세요" );
                    loginStatusLabel.setForeground( Color.RED );
                }
            });
        }
    }

    public void gameStartSetVisible( boolean isVisible ) {
        Button[ 3 ].setVisible( isVisible );
        Button[ 4 ].setVisible( isVisible );
    }
    public void loginSetVisible( boolean isVisible ) {
        idField.setVisible( isVisible);
        passwordField.setVisible( isVisible );
        Button[ 1 ].setVisible( isVisible );
        Button[ 2 ].setVisible( isVisible );
        loginStatusLabel.setVisible( isVisible );
    }

    private void setupLoginUI() {
        idField.setBounds(450, 400, 300, 50 );
        passwordField.setBounds(450, 460, 300, 50 );
        loginStatusLabel.setBounds(500, 520, 300, 30 );
        loginStatusLabel.setForeground( Color.WHITE );

        add( idField );
        add( passwordField );
        add( loginStatusLabel );
    }

    public void createImage() {
        try {
            for (int i = 0; i < 10; i++) {
                imageIconsEntered[i] = new ImageIcon(getClass().getResource("../Resources/images/ButtonEntered_" + ( i + 1 ) + ".png"));
                imageIconsBasic[i] = new ImageIcon(getClass().getResource("../Resources/images/ButtonBasic_" + ( i + 1 ) + ".png"));
            }
            for (int i = 0; i < 2; i++) {
                image[i] = new ImageIcon(getClass().getResource("../Resources/images/Image_" + ( i + 1 ) + ".png")).getImage();
            }
        } catch ( NullPointerException e ) {
            System.out.println(e.getMessage());
        }
    }

    public void assignmentImage() {
        for (int i = 0; i < Button.length ; i++) {
            Button[ i ] = new JButton( imageIconsBasic[ i ] );
        }
    }

    public void ButtonSetting() {
        for ( int i = 0; i < Button.length; i++ ) {
            buttonSetting( Button[ i ], XyWh[ i ][ 0 ], XyWh[ i ][ 1 ], XyWh[ i ][ 2 ], XyWh[ i ][ 3 ], imageIconsEntered[ i ], imageIconsBasic[ i ], menu[ i ], isSetVisible[ i ] );
        }
    }

    public void TrackList() {
        for ( int i = 1; i <= 3 ; i++ ) {
            trackList.add( new Track( "Start Image_" + i + ".jpg", "Game Image_" + i + ".jpg",
                    "Start Music_" + i + ".mp3", "Game Music_" + i + ".mp3", MusicTitle[ i-1 ] ) );
        }
    }
}