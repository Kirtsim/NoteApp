package fm.kirtsim.kharos.noteapp.dependencyinjection.application;

import javax.inject.Singleton;

import dagger.Component;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerComponent;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ControllerModule;
import fm.kirtsim.kharos.noteapp.dependencyinjection.controller.ViewMvcModule;

/**
 * Created by kharos on 27/07/2017
 */

@Singleton
@Component(modules={ApplicationModule.class})
public interface ApplicationComponent {

    ControllerComponent plus(ControllerModule controllerModule, ViewMvcModule viewMvcModule);
}
