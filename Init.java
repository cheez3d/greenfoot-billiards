import greenfoot.Greenfoot;
import greenfoot.World;

public class Init extends GameWorld {
    public Init() {
        super(1, 1, 1);
        
        Game game = Game.getInstance();
        game.init();
    }
}
