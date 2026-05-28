package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

public interface IEntityProcessingService {

    // This method is called every game tick (update positions, handle input, move entities)
    void process(GameData gameData, World world);
}