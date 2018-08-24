package application.battleships.Models;

import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CoordinatesModelTest {
    private CoordinatesModel coordinatesModel = new CoordinatesModel(0, 0);

    @Test
    public void testX(){
        //given
        assertFalse(coordinatesModel.getX() == 1);
        //when
        coordinatesModel.setX(1);
        //then
        assertTrue(coordinatesModel.getX() == 1);
    }

    @Test
    public void testY(){
        //given
        assertFalse(coordinatesModel.getY() == 1);
        //when
        coordinatesModel.setY(1);
        //then
        assertTrue(coordinatesModel.getY() == 1);
    }
}
