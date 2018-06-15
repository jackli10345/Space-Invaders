//Bullet
public class Bullet{
	private int x, y, direction, width, height = 10;
	public Bullet(int x, int y, int width, int direction){
		this.x = x;
		this.y = y;
		this.width = width;
		this.direction = direction;
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
	}
	public void move(){
		if(direction == 0){
			y -= 10;
		}
		if(direction == 1){
			y += 10;
		}
	}
	public boolean hit(GoodShip player){
		if(player.getX() < x && player.getX() + player.getWidth() > x){
			if(player.getY() < y && player.getY() + player.getHeight() > y){
				return true;
			}
			if(player.getY() > y && player.getY() < y + height){
				return true;
			}
		}
		if(player.getX() > x && player.getX() < x + width){
			if(player.getY() < y && player.getY() + player.getHeight() > y){
				return true;
			}
			if(player.getY() > y && player.getY() < y + height){
				return true;
			}
		}
		return false;
	}
	public boolean hit(Alien badguy){
		if(badguy.getX() < x && badguy.getX() + badguy.getWidth() > x){
			if(badguy.getY() < y && badguy.getY() + badguy.getHeight() > y){
				return true;
			}
		}
		if(badguy.getX() > x && badguy.getX() < x + width){
			if(badguy.getY() < y && badguy.getY() + badguy.getHeight() > y){
				return true;
			}
			if(badguy.getY() > y && badguy.getY() < y + height){
				return true;
			}
		}
		return false;
	}
	public boolean hit(Boss badguy){
		if(badguy.getX() < x && badguy.getX() + badguy.getWidth() > x){
			if(badguy.getY() < y && badguy.getY() + badguy.getHeight() > y){
				return true;
			}
		}
		if(badguy.getX() > x && badguy.getX() < x + width){
			if(badguy.getY() < y && badguy.getY() + badguy.getHeight() > y){
				return true;
			}
			if(badguy.getY() > y && badguy.getY() < y + height){
				return true;
			}
		}
		return false;
	}
}