OpenGL ES - Android

# Andoroid coordinate system

* Counter clockwise is the usual way of reading the coordinates
* Float based
* Maximum coordinates of the system on 2D plane (corners)
    * -1.0, -1.0
    * -1.0, 1,0
    * 1.0, 1.0
    * 1.0, -1.0


Vertex Shader - 
Fragment Shader - textures.
# Program

* An OpenGL ES object that contains the shaders you want to use for drawing one or more shapes

# The Graphics pipeline

# Vertex Shaders

* OpenGL ES graphics code for rendering the vertices of a shape

# Shape Assembly

# Rasterization

# Fragment shader

* OpenGL ES code for rendering the face of a shape with colors

# Projection

* Calculation which adjusts the coordinates based on width and height of the GLSurfaceView
* Calculation are usually done when view proportions are changed in on onSurfaceChanged() of the renderer

# Camera view

* OpenGL ES doesn't have camera itself
* OpenGL ES has utility methods which can be used to simulate a camera by transforming display of drawn objects
* Camera view calculation might be calculated on creation of GLSurfaceView or when application state get changed in camera effecting way eg. user input

