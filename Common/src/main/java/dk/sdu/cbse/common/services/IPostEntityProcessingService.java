package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

public interface IPostEntityProcessingService {

    // Called after all entities have moved, will be used for collision detection
    void process(GameData gameData, World world);
}