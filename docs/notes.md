OpenGL ES - Android

# Projection

* Calculation which adjusts the coordinates based on width and height of the GLSurfaceView
* Calculation are usually done when view proportions are changed in on onSurfaceChanged() of the renderer

# Camera view

* OpenGL ES doesn't have camera itself
* OpenGL ES has utility methods which can be used to simulate a camera by transforming display of drawn objects
* Camera view calculation might be calculated on creation of GLSurfaceView or when application state get changed in camera effecting way eg. user input

