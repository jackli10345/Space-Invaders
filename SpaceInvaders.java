import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.awt.image.*;
import javax.imageio.*;
import java.util.*;

public class SpaceInvaders extends JFrame implements ActionListener,KeyListener{
	javax.swing.Timer myTimer;
	GamePanel game;

    public SpaceInvaders() {
		super("SpaceInvaders");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(1000,600);

		myTimer = new javax.swing.Timer(20, this);
		myTimer.start();

		game = new GamePanel();
		add(game);
		addKeyListener(this);
		setResizable(false);
		
		new MyMenu(this);
		
    }
    public void start(){
		myTimer.start();
		setVisible(true);
    }

	public void actionPerformed(ActionEvent evt){
		if(game != null){
			game.refresh();
			game.repaint();
		}
	}

    public void keyTyped(KeyEvent e) {}

    public void keyPressed(KeyEvent e) {
    	game.setKey(e.getKeyCode(),true);
    }

    public void keyReleased(KeyEvent e) {
    	game.setKey(e.getKeyCode(),false);
    }

    public static void main(String[] arguments) {
		SpaceInvaders frame = new SpaceInvaders();
    }
}
//MENU
class MyMenu extends JFrame implements ActionListener{
	private SpaceInvaders space;
	JButton play = new JButton("play");
	
	public MyMenu(SpaceInvaders s){
		super("Menu");
		setSize(1000, 600);
		space = s;
		play.addActionListener(this);
		ImageIcon menu = new ImageIcon("images/background.jpg");
		ImageIcon logo = new ImageIcon("images/logo.png");
		JLabel backLabel = new JLabel(menu);
		JLabel logoLabel = new JLabel(logo);
		JLayeredPane mPage = new JLayeredPane();
		mPage.setLayout(null);
		backLabel.setSize(1000, 800);
		backLabel.setLocation(0, -100);
		mPage.add(backLabel, 1);
		logoLabel.setSize(200, 100);
		logoLabel.setLocation(300, 200);
		mPage.add(logoLabel, 2);					
		play.setSize(100, 30);
		play.setLocation(450, 300);
		mPage.add(play, 3);
		
		add(mPage);
		setVisible(true);
	}
	
    public void actionPerformed(ActionEvent evt) {
    	space.start();
    	setVisible(false);
	}
}
//MENU
class GamePanel extends JPanel{
	private boolean []keys;
	private Image mainBack, back1, back2, back3, playerShip, alienShip, life, GameOver, laserbeam, doubleScore, extraLife, boss;
	private GoodShip player;
	private PowerUp powerup;
	private boolean limit = false, down = false, laser = false, gameOver = false, doubleShot = false, twoScore = false, tagged2 = false;
	private int delay = 100, time = 0, score = 0, by = 0, by2 = 0, by3 = 0, maxWave, level = 0, laserCount = 0, doubleCount = 0, doubleTimer = 0, powerupCount = 3000, minCount = 1000;
	ArrayList<Bullet>bullets = new ArrayList<Bullet>();
	ArrayList<Bullet>badBullets = new ArrayList<Bullet>();
	ArrayList<Alien>aliens = new ArrayList<Alien>();
	ArrayList<Alien>dead = new ArrayList<Alien>();
	ArrayList<PowerUp>powerups = new ArrayList<PowerUp>();
	ArrayList<Boss>bosses = new ArrayList<Boss>();

	public GamePanel(){
		keys = new boolean[KeyEvent.KEY_LAST+1];
		player = new GoodShip(500);
		//Load Images
		back1 = new ImageIcon("Images/OuterSpace.png").getImage();
		back2 = new ImageIcon("Images/OuterSpace1.png").getImage();
		back3 = new ImageIcon("Images/OuterSpace2.png").getImage();
		playerShip= new ImageIcon("Images/playerShip.png").getImage();
		alienShip = new ImageIcon("Images/alienShip.png").getImage();
		GameOver = new ImageIcon("Images/GameOver.png").getImage();
		laserbeam = new ImageIcon("Images/laserbeam.png").getImage();
		doubleScore = new ImageIcon("Images/doubleScore.png").getImage();
		extraLife = new ImageIcon("Images/extraLife.png").getImage();
		//Scale Images
		playerShip = playerShip.getScaledInstance(60, 60, playerShip.SCALE_SMOOTH);
		life = playerShip.getScaledInstance(30, 30, playerShip.SCALE_SMOOTH);
		back1 = back1.getScaledInstance(1000, 600, back1.SCALE_SMOOTH);
		back2 = back2.getScaledInstance(1000, 600, back2.SCALE_SMOOTH);
		back3 = back3.getScaledInstance(1000, 600, back3.SCALE_SMOOTH);
		alienShip = alienShip.getScaledInstance(40, 40, alienShip.SCALE_SMOOTH);
		boss = alienShip.getScaledInstance(100, 100, alienShip.SCALE_SMOOTH);
		GameOver = GameOver.getScaledInstance(1000, 600, GameOver.SCALE_SMOOTH);
		laserbeam = laserbeam.getScaledInstance(20, 20, laserbeam.SCALE_SMOOTH);
		doubleScore = doubleScore.getScaledInstance(20, 20, doubleScore.SCALE_SMOOTH);
		extraLife = extraLife.getScaledInstance(20, 20, extraLife.SCALE_SMOOTH);
			
		setSize(1000, 600);
	}

    public void setKey(int k, boolean v) {
    	keys[k] = v;
    }

	public void refresh(){
		Random random = new Random();
		if(keys[KeyEvent.VK_LEFT] ){
			player.move(0);
		}
		if(keys[KeyEvent.VK_RIGHT] ){
			player.move(1);
		}
		if(keys[KeyEvent.VK_SPACE]){
			if(doubleShot){
				if(time < 0){
					bullets.add(new Bullet(player.getX(), 480, 10, 0));
					bullets.add(new Bullet(player.getX() + 58, 480, 10, 0));
					doubleCount -= 1;
					time = delay;
				}
			}
			else{
				if(time < 0){
					bullets.add(player.shoot());
					time = delay;
				}	
			}
			if(doubleCount <= 0){
				doubleShot = false;
			}
		}
		if(keys[KeyEvent.VK_Z]){
			if(time < 0){
				if(laser){
					for(int j = 0; j < 30; j++){
						bullets.add(new Bullet(player.getX() + 29, 480 + j * 10, 10, 0));
					}
					laserCount -= 1;
				}
				if(laserCount <= 0){
					laser = false;
				}
				time = delay;
			}
		}
		if(laserCount <= 10){
			powerupCount = powerupCount - level * 200;
			if(powerupCount < minCount){
				powerupCount = minCount;
			}
			int x = random.nextInt(powerupCount);
			if(x <= 2){
				powerups.add(new PowerUp("laser", random.nextInt(800), 0));
			}
			if(x > 2 && x <= 4){
				powerups.add(new PowerUp("life", random.nextInt(800), 0));
			}
			if(x > 4 && x <= 6){
				powerups.add(new PowerUp("double", random.nextInt(800), 0));
			}
			if(x == 7){
				powerups.add(new PowerUp("doubleScore", random.nextInt(800), 0));
			}
		}
		if(aliens.isEmpty() && bosses.isEmpty()){
			level += 1;
			if(level < 6){
				spawnWave();
			}
			else{
				spawnBoss();
			}
		}
		for(int i = powerups.size() - 1; i >= 0; i--){
			powerups.get(i).move();
			if(powerups.get(i).collide(player) == true){
				if(powerups.get(i).getName().equals("laser")){
					laser = true;
					laserCount += 1;
				}
				if(powerups.get(i).getName().equals("life")){
					player.addLife(1);
				}
				if(powerups.get(i).getName().equals("double")){
					doubleShot = true;
					doubleCount += 10;
				}
				if(powerups.get(i).getName().equals("doubleScore")){
					twoScore = true;
					doubleTimer = 500;
				}
				powerups.remove(i);
			}
		}
		for(int i = 0; i < bullets.size(); i++){
			bullets.get(i).move();
			for(int j = 0; j < aliens.size(); j++){
				boolean tagged = false;
				tagged = bullets.get(i).hit(aliens.get(j));
				if(tagged){
					score += aliens.get(j).getScore();
					if(twoScore){
						score += aliens.get(j).getScore();
					}
					aliens.get(j).damage();
					if(aliens.get(j).getHp() == 0){
						dead.add(aliens.get(j));
						aliens.remove(j);
					}
					bullets.remove(i);
					j = aliens.size();
				}
			}
			for(int j = 0; j < bosses.size(); j++){
				boolean tagged2 = false;
				tagged2 = bullets.get(i).hit(bosses.get(j));
				if(tagged2){
					score += bosses.get(j).getScore();
					if(twoScore){
						score += bosses.get(j).getScore();
					}
					bosses.get(j).damage();
					if(bosses.get(j).getHp() == 0){
						bosses.remove(j);
					}
					bullets.remove(i);
					j = bosses.size();
				}
			}
		}
		for(int i = 0; i < badBullets.size(); i++){
			badBullets.get(i).move();
			boolean tagged = false;
			tagged = badBullets.get(i).hit(player);
			if(tagged){
				player.loseLife();
				badBullets.remove(i);
				i = badBullets.size();
			}
		}
		for(int i = 0; i < aliens.size(); i++){
			int n = random.nextInt(1000 - dead.size());
			if(n == 0){
				badBullets.add(aliens.get(i).shoot());
			}
			limit = aliens.get(i).move(dead.size());
			if(aliens.get(i).getY() > 500){
				down = true;
			}
			if(limit == true){
				for(int j = 0; j < aliens.size(); j++){
					aliens.get(j).shift();
				}
				limit = false;
			}	
		}
		for(int i = 0; i < bosses.size(); i++){
			int n = random.nextInt(10);
			if(n == 0){
				badBullets.add(bosses.get(i).shoot());
			}
			limit = bosses.get(i).move();
			if(bosses.get(i).getY() > 500){
				down = true;
			}
			if(limit == true){
				for(int j = 0; j < bosses.size(); j++){
					bosses.get(j).shift();
				}
				limit = false;
			}
		}
		if(player.getLives() <= 0){
			gameOver = true;
		}
		time -= 8;
		if(doubleTimer > 0){
			doubleTimer -= 1;
		}
		if(doubleTimer <= 0){
			twoScore = false;
		}
	}
	public void spawnWave(){
		player.addLife(level);
		for(int i = 0; i < dead.size(); i++){
			dead.remove(i);
		}
    	for(int i = 0; i < level; i++){
    		for(int j = 0; j < 10; j++){
    			aliens.add(new Alien(300 + 50 * j, 100 + i * 50, 50));
    		}
    	}
    }
    public void spawnBoss(){
    	int num = 0;
    	player.addLife(level);
    	for(int i = 0; i < dead.size(); i++){
			dead.remove(i);
		}
		if(level < 9){
			num = level;
		}
		else{
			num = 9;
		}
		for(int i = 0; i < num - 5; i++){
			bosses.add(new Boss(200 + i * 150, 50, 100));
		}
    }

    public void paintComponent(Graphics g){
    	
    	g.fillRect(0, 0, 1000, 600);
    	g.drawImage(back1, 0, by, this);
    	g.drawImage(back1, 0, by - 600, this);
    	g.drawImage(back2, 0, by2, this);
    	g.drawImage(back2, 0, by2 - 600, this);
  		g.drawImage(back3, 0, by3, this);
  		g.drawImage(back3, 0, by3 - 600, this);
		g.drawImage(playerShip, player.getX(), 480, this);
		
		g.setColor(new Color(255, 255, 255));
		g.setFont(new Font("Comic Sans MS", Font.PLAIN, 25));
		g.drawString("LIVES: ", 10, 35);
		g.drawString(Integer.toString(player.getLives()), 105, 35);
		g.drawString("SCORE: ", 800, 35);
		g.drawString(Integer.toString(score), 900, 35);
		g.drawString("LASERS: ", 10, 85);
		g.drawString(Integer.toString(laserCount), 120, 85);
		g.drawString("DOUBLESHOT: ", 500, 35);
		g.drawString(Integer.toString(doubleCount), 710, 35);
		if(twoScore){
			g.drawString("DOUBLESCORE!!!", 750, 85);
		}
		g.drawString("LEVEL: ", 500, 85);
		g.drawString(Integer.toString(level), 600, 85);
		
		for(int i = 0; i < bullets.size(); i++){
			g.setColor(new Color(222, 0, 0));
			g.fillRect(bullets.get(i).getX(), bullets.get(i).getY(), 5, 10);	
		}
		for(int i = 0; i < badBullets.size(); i++){
			g.setColor(new Color(0, 222, 0));
			g.fillRect(badBullets.get(i).getX(), badBullets.get(i).getY(), 5, 10);
		}
		for(int i = 0; i < powerups.size(); i++){
			PowerUp powerup = powerups.get(i);
			if(powerup.getName().equals("laser")){
				g.drawImage(laserbeam, powerup.getX(), powerup.getY(), this);
			}
			if(powerup.getName().equals("life")){
				g.drawImage(extraLife, powerup.getX(), powerup.getY(), this);
			}
			if(powerup.getName().equals("double")){
				g.setColor(new Color(0, 0, 255));
				g.fillRect(powerup.getX(), powerup.getY(), powerup.getWidth(), powerup.getHeight());
			}
			if(powerup.getName().equals("doubleScore")){
				g.drawImage(doubleScore, powerup.getX(), powerup.getY(), this);
			}
		}
		for(int i = 0; i < aliens.size(); i++){
			g.drawImage(alienShip, aliens.get(i).getX(), aliens.get(i).getY(), this);
			g.setColor(new Color(255, 255, 255));
			g.fillRect(aliens.get(i).getX(), aliens.get(i).getY(), aliens.get(i).getWidth(), 5);
			g.setColor(new Color(255, 0, 0));
			g.fillRect(aliens.get(i).getX() + 3, aliens.get(i).getY() + 1, aliens.get(i).getHp() * 12, 3);
		}
		for(int i = 0; i < bosses.size(); i++){
			g.drawImage(boss, bosses.get(i).getX(), bosses.get(i).getY(), this);
			g.setColor(new Color(255, 255, 255));
			g.fillRect(bosses.get(i).getX() - 3, bosses.get(i).getY(), bosses.get(i).getWidth(), 10);
			g.setColor(new Color(255, 0, 0));
			g.fillRect(bosses.get(i).getX(), bosses.get(i).getY() + 3, bosses.get(i).getHp() * 3, 3);
		}
		for(int i = 0; i < player.getLives(); i++){
			g.drawImage(life, 140 + i * 35, 13, this);
		}
		if(down){
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, 1000, 600);
			g.drawImage(GameOver, 0, 0, this);
		}
		if(gameOver == true){
			g.setColor(new Color(0, 0, 0));
			g.fillRect(0, 0, 1000, 600);
			g.drawImage(GameOver, 0, 0, this);
		}
		by += 5;
		by2 += 4;
		by3 += 3;
		if(by > 600){
			by = 0;
		}
		if(by2 > 600){
			by2 = 0;
		}
		if(by3 > 600){
			by3 = 0;
		}
    }
}





