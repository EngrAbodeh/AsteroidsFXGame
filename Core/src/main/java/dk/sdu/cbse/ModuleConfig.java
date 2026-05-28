package dk.sdu.cbse;

import dk.sdu.cbse.common.services.IEntityProcessingService;
import dk.sdu.cbse.common.services.IGamePluginService;
import dk.sdu.cbse.common.services.IPostEntityProcessingService;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.util.List;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;

@Configuration
public class ModuleConfig {

    public ModuleConfig() {
    }

    @Bean
    public Game game(List<IGamePluginService> gamePluginServices,
                     List<IEntityProcessingService> entityProcessingServiceList,
                     List<IPostEntityProcessingService> postEntityProcessingServices,
                     CollisionEventHandler collisionEventHandler) {
        return new Game(gamePluginServices, entityProcessingServiceList, postEntityProcessingServices, collisionEventHandler);
    }

    @Bean
    public List<IEntityProcessingService> entityProcessingServiceList() {
        return ServiceLoader.load(IEntityProcessingService.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public List<IGamePluginService> gamePluginServices() {
        return ServiceLoader.load(IGamePluginService.class).stream()
                .map(ServiceLoader.Provider::get)
                .collect(toList());
    }

    @Bean
    public List<IPostEntityProcessingService> postEntityProcessingServices(ApplicationEventPublisher publisher) {
        return ServiceLoader.load(IPostEntityProcessingService.class).stream()
                .map(ServiceLoader.Provider::get)
                .peek(service -> {
                    if (service instanceof ApplicationEventPublisherAware aware) {
                        aware.setApplicationEventPublisher(publisher);
                    }
                })
                .collect(toList());
    }

    @Bean
    public CollisionEventHandler collisionEventHandler() {
        return new CollisionEventHandler();
    }
}
