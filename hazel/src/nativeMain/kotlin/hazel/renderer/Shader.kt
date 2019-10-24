package hazel.renderer

import copengl.GL_COMPILE_STATUS
import copengl.GL_FALSE
import copengl.GL_FRAGMENT_SHADER
import copengl.GL_LINK_STATUS
import copengl.GL_VERTEX_SHADER
import hazel.Disposable
import hazel.Hazel
import hazel.math.FloatMatrix4x4
import opengl.glAttachShader
import opengl.glCompileShader
import opengl.glCreateProgram
import opengl.glCreateShader
import opengl.glDeleteProgram
import opengl.glDeleteShader
import opengl.glDetachShader
import opengl.glGetProgramInfoLog
import opengl.glGetProgramiv
import opengl.glGetShaderInfoLog
import opengl.glGetShaderiv
import opengl.glGetUniformLocation
import opengl.glLinkProgram
import opengl.glShaderSource
import opengl.glUniformMatrix4fv
import opengl.glUseProgram

class Shader(vertexSource: String, fragmentSource: String) : Disposable {

    private val rendererId: UInt

    init {
        // create an empty vertex shader handle
        val vertexShader = glCreateShader(GL_VERTEX_SHADER)

        // send the vertex shader source code to GL
        glShaderSource(vertexShader, vertexSource)

        // compile the vertex shader
        glCompileShader(vertexShader)


        if (glGetShaderiv(vertexShader, GL_COMPILE_STATUS) == GL_FALSE) {
            rendererId = 0u

            val infoLog = glGetShaderInfoLog(vertexShader)

            // we don't need the shader anymore
            glDeleteShader(vertexShader)

            Hazel.coreCritical { "Vertex shader failed to compile!" }
            Hazel.coreError { infoLog }
        } else {

            // create an empty fragment shader handle
            val fragmentShader = glCreateShader(GL_FRAGMENT_SHADER)

            // send the fragment shader source code to GL
            glShaderSource(fragmentShader, fragmentSource)

            // compile the fragment shader
            glCompileShader(fragmentShader)

            if (glGetShaderiv(fragmentShader, GL_COMPILE_STATUS) == GL_FALSE) {
                rendererId = 0u

                val infoLog = glGetShaderInfoLog(fragmentShader)

                // we don't need either shader anymore
                glDeleteShader(vertexShader)
                glDeleteShader(fragmentShader)

                Hazel.coreCritical { "Fragment shader failed to compile!" }
                Hazel.coreError { infoLog }
            } else {

                // vertex and fragment shaders are successfully compiled
                // now time to link them together into a program
                // get a program object
                val program = glCreateProgram()

                // attach our shaders to our program
                glAttachShader(program, vertexShader)
                glAttachShader(program, fragmentShader)

                // link our program
                glLinkProgram(program)

                if (glGetProgramiv(program, GL_LINK_STATUS) == GL_FALSE) {
                    rendererId = 0u
                    val infoLog = glGetProgramInfoLog(rendererId)

                    // we don't need the program or the shaders anymore
                    glDeleteProgram(program)
                    glDeleteShader(vertexShader)
                    glDeleteShader(fragmentShader)

                    Hazel.coreCritical { "Shaders failed to link!" }
                    Hazel.coreError { infoLog }

                } else {
                    rendererId = program

                    // always detach shaders after a successful link
                    glDetachShader(program, vertexShader)
                    glDetachShader(program, fragmentShader)
                }
            }
        }
    }

    fun bind() {
        glUseProgram(rendererId)
    }

    fun unbind() {
        glUseProgram(0u)
    }

    fun uploadUniform(name: String, matrix: FloatMatrix4x4) {
        val location = glGetUniformLocation(rendererId, name)
        glUniformMatrix4fv(location, false, matrix)
    }

    override fun dispose() {
        glDeleteProgram(rendererId)
    }
}
