package fm.kirtsim.kharos.noteapp.dependencyinjection.controller;

import dagger.Subcomponent;

/**
 * Created by kharos on 27/07/2017
 */

@Subcomponent(modules={ControllerModule.class, ViewMvcModule.class})
public interface ControllerComponent {
}
