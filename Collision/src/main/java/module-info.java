import dk.sdu.cbse.common.services.IPostEntityProcessingService;

module Collision {
    requires Common;
    requires spring.context;
    opens dk.sdu.cbse.collisionsystem to spring.core;
    provides IPostEntityProcessingService with dk.sdu.cbse.collisionsystem.CollisionDetector;
}
