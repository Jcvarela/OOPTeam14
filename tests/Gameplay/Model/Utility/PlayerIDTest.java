package Gameplay.Model.Utility;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by zrgam_000 on 4/15/2017.
 */
public class PlayerIDTest {
    public PlayerID p1;
    public PlayerID p2;

    @Before
    public void TestSetup(){
        p1 = new PlayerID();
        p2 = new PlayerID();
    }

    @Test
    public void TestMatches(){
        assertTrue(p1.matches(p1));
        assertTrue(p2.matches(p2));
        assertFalse(p1.matches(p2));
        assertFalse(p2.matches(p1));
    }
}