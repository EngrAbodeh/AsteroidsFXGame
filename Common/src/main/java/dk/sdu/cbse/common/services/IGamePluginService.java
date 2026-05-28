package dk.sdu.cbse.common.services;

import dk.sdu.cbse.common.data.GameData;
import dk.sdu.cbse.common.data.World;

public interface IGamePluginService {

    // This method is called once when the module is loaded.
    void start (GameData gameData, World world);

    // This one is called when the module is unloaded.
    void stop(GameData gameData, World world);
}