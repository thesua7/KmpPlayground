package com.thesua7.kmpplayground

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.thesua7.kmpplayground.viewmodel.LoginViewmodel
import kotlin.math.sin


// ----------------------------------------------------
// LOGIN SCREEN
// ----------------------------------------------------

@Composable
fun LoginScreen(
    viewModel: LoginViewmodel
) {

    val uiState by
    viewModel.uiState.collectAsState()

    val focusManager =
        LocalFocusManager.current

    Box(
        modifier = Modifier.fillMaxSize()
    ) {

        ParticleFluidBackground(
            modifier =
                Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    horizontal = 24.dp
                ),

            verticalArrangement =
                Arrangement.Center,

            horizontalAlignment =
                Alignment.CenterHorizontally
        ) {

            LoginHeader()

            Spacer(
                modifier =
                    Modifier.height(30.dp)
            )

            GlassLoginCard {

                GlassTextField(
                    value =
                        uiState.username,

                    onValueChange =
                        viewModel::
                        onUsernameChanged,

                    label = "Username",

                    prefix = "@",

                    imeAction =
                        ImeAction.Next
                )

                GlassTextField(
                    value =
                        uiState.password,

                    onValueChange =
                        viewModel::
                        onPasswordChanged,

                    label = "Password",

                    prefix = "●",

                    isPassword = true,

                    imeAction =
                        ImeAction.Done,

                    onDone = {

                        focusManager
                            .clearFocus()

                        viewModel.login()
                    }
                )

                LoginButton(
                    isLoading =
                        uiState.isLoading,

                    onClick = {

                        focusManager
                            .clearFocus()

                        viewModel.login()
                    }
                )

                AnimatedVisibility(
                    visible =
                        uiState.error != null,

                    enter =
                        fadeIn() +
                                expandVertically(),

                    exit =
                        fadeOut() +
                                shrinkVertically()
                ) {

                    uiState.error?.let {

                        StatusMessage(
                            text = it,
                            success = false
                        )
                    }
                }

                AnimatedVisibility(
                    visible =
                        uiState.isLoggedIn,

                    enter =
                        fadeIn() +
                                scaleIn(),

                    exit =
                        fadeOut()
                ) {

                    StatusMessage(
                        text =
                            "Login successful!",
                        success = true
                    )
                }
            }
        }
    }
}


// ----------------------------------------------------
// HEADER
// ----------------------------------------------------

@Composable
private fun LoginHeader() {

    val transition =
        rememberInfiniteTransition(
            label = "header"
        )

    val scale by
    transition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.05f,

        animationSpec =
            infiniteRepeatable(
                animation =
                    tween(
                        durationMillis =
                            2000,

                        easing =
                            FastOutSlowInEasing
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label = "headerPulse"
    )

    Column(
        horizontalAlignment =
            Alignment.CenterHorizontally
    ) {

        Surface(
            modifier = Modifier
                .size(88.dp)
                .scale(scale),

            shape = CircleShape,

            color =
                Color.White.copy(
                    alpha = 0.08f
                ),

            border =
                BorderStroke(
                    width = 1.dp,

                    color =
                        Color.White.copy(
                            alpha = 0.18f
                        )
                )
        ) {

            Box(
                contentAlignment =
                    Alignment.Center
            ) {

                Text(
                    text = "●",
                    fontSize = 32.sp,
                    color = Color.White
                )
            }
        }

        Spacer(
            modifier =
                Modifier.height(16.dp)
        )

        Text(
            text = "Welcome Back",

            color = Color.White,

            fontSize = 30.sp,

            fontWeight =
                FontWeight.Bold
        )

        Spacer(
            modifier =
                Modifier.height(6.dp)
        )

        Text(
            text =
                "Sign in to continue",

            color =
                Color.White.copy(
                    alpha = 0.60f
                ),

            fontSize = 15.sp
        )
    }
}


// ----------------------------------------------------
// GLASS CARD
// ----------------------------------------------------

@Composable
private fun GlassLoginCard(
    content:
    @Composable () -> Unit
) {

    Surface(
        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                30.dp
            ),

        color =
            Color.White.copy(
                alpha = 0.075f
            ),

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    Color.White.copy(
                        alpha = 0.13f
                    )
            ),

        shadowElevation = 22.dp
    ) {

        Column(
            modifier =
                Modifier.padding(24.dp),

            verticalArrangement =
                Arrangement.spacedBy(
                    18.dp
                )
        ) {

            content()
        }
    }
}


// ----------------------------------------------------
// GLASS INPUT FIELD
// ----------------------------------------------------

@Composable
private fun GlassTextField(

    value: String,

    onValueChange:
        (String) -> Unit,

    label: String,

    prefix: String,

    isPassword:
    Boolean = false,

    imeAction:
    ImeAction =
        ImeAction.Next,

    onDone:
        () -> Unit = {}

) {

    var focused by remember {
        mutableStateOf(false)
    }

    val scale by
    animateFloatAsState(

        targetValue =
            if (focused)
                1.012f
            else
                1f,

        animationSpec =
            tween(
                durationMillis =
                    180
            ),

        label =
            "fieldScale"
    )

    OutlinedTextField(

        modifier = Modifier
            .fillMaxWidth()
            .scale(scale)
            .onFocusChanged {

                focused =
                    it.isFocused
            },

        value = value,

        onValueChange =
            onValueChange,

        singleLine = true,

        label = {

            Text(label)
        },

        leadingIcon = {

            Text(
                text = prefix,

                color =
                    if (focused)
                        Color.White
                    else
                        Color.White.copy(
                            alpha =
                                0.50f
                        ),

                fontWeight =
                    FontWeight.Bold
            )
        },

        visualTransformation =

            if (isPassword)

                PasswordVisualTransformation()

            else

                VisualTransformation.None,

        keyboardOptions =

            KeyboardOptions(

                keyboardType =
                    if (isPassword)

                        KeyboardType.Password

                    else

                        KeyboardType.Text,

                imeAction =
                    imeAction
            ),

        keyboardActions =

            KeyboardActions(

                onDone = {

                    onDone()
                }
            ),

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =

            OutlinedTextFieldDefaults.colors(

                focusedTextColor =
                    Color.White,

                unfocusedTextColor =
                    Color.White,

                focusedLabelColor =
                    Color.White,

                unfocusedLabelColor =
                    Color.White.copy(
                        alpha = 0.55f
                    ),

                cursorColor =
                    Color.White,

                focusedBorderColor =
                    Color.White.copy(
                        alpha = 0.45f
                    ),

                unfocusedBorderColor =
                    Color.White.copy(
                        alpha = 0.10f
                    ),

                focusedContainerColor =
                    Color.White.copy(
                        alpha = 0.12f
                    ),

                unfocusedContainerColor =
                    Color.White.copy(
                        alpha = 0.055f
                    )
            )
    )
}


// ----------------------------------------------------
// LOGIN BUTTON
// ----------------------------------------------------

@Composable
private fun LoginButton(

    isLoading: Boolean,

    onClick: () -> Unit

) {

    val interactionSource =
        remember {
            MutableInteractionSource()
        }

    val pressed by
    interactionSource
        .collectIsPressedAsState()

    val scale by
    animateFloatAsState(

        targetValue =
            when {

                pressed ->
                    0.965f

                isLoading ->
                    0.985f

                else ->
                    1f
            },

        animationSpec =
            tween(120),

        label =
            "loginButtonScale"
    )

    Button(

        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .scale(scale),

        onClick = onClick,

        enabled = !isLoading,

        interactionSource =
            interactionSource,

        shape =
            RoundedCornerShape(
                18.dp
            ),

        colors =
            ButtonDefaults.buttonColors(

                containerColor =
                    Color(0xFF635BFF),

                disabledContainerColor =
                    Color(0xFF635BFF)
                        .copy(
                            alpha = 0.65f
                        )
            )
    ) {

        Row(
            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            if (isLoading) {

                CircularProgressIndicator(
                    modifier =
                        Modifier.size(
                            20.dp
                        ),

                    strokeWidth =
                        2.dp,

                    color =
                        Color.White
                )
            }

            Text(

                text =
                    if (isLoading)
                        "Signing in..."
                    else
                        "Login",

                fontSize =
                    16.sp,

                fontWeight =
                    FontWeight.SemiBold
            )

            if (!isLoading) {

                Text(
                    text = "→",
                    fontSize = 19.sp
                )
            }
        }
    }
}


// ----------------------------------------------------
// STATUS MESSAGE
// ----------------------------------------------------

@Composable
private fun StatusMessage(

    text: String,

    success: Boolean

) {

    val color =

        if (success)

            Color(0xFF65E6A5)

        else

            Color(0xFFFF7A7A)

    Surface(

        modifier =
            Modifier.fillMaxWidth(),

        shape =
            RoundedCornerShape(
                14.dp
            ),

        color =
            color.copy(
                alpha = 0.10f
            ),

        border =
            BorderStroke(
                width = 1.dp,

                color =
                    color.copy(
                        alpha = 0.20f
                    )
            )
    ) {

        Row(
            modifier =
                Modifier.padding(
                    14.dp
                ),

            verticalAlignment =
                Alignment.CenterVertically,

            horizontalArrangement =
                Arrangement.spacedBy(
                    10.dp
                )
        ) {

            Text(
                text =
                    if (success)
                        "✓"
                    else
                        "!",

                color = color,

                fontWeight =
                    FontWeight.Bold
            )

            Text(
                text = text,

                color = color,

                fontSize = 14.sp,

                fontWeight =
                    FontWeight.Medium
            )
        }
    }
}


// ----------------------------------------------------
// PARTICLE MODEL
// ----------------------------------------------------

private data class Particle(

    val x: Float,

    val y: Float,

    val radius: Float,

    val alpha: Float,

    val speed: Float,

    val wave: Float
)


// ----------------------------------------------------
// PARTICLES
// ----------------------------------------------------

private val particleList =
    listOf(

        Particle(
            0.08f,
            0.10f,
            2.5f,
            0.60f,
            0.75f,
            13f
        ),

        Particle(
            0.18f,
            0.28f,
            4f,
            0.35f,
            0.55f,
            18f
        ),

        Particle(
            0.30f,
            0.52f,
            2f,
            0.75f,
            1.0f,
            10f
        ),

        Particle(
            0.42f,
            0.14f,
            3f,
            0.50f,
            0.70f,
            16f
        ),

        Particle(
            0.54f,
            0.72f,
            2.5f,
            0.65f,
            0.90f,
            13f
        ),

        Particle(
            0.66f,
            0.38f,
            5f,
            0.30f,
            0.50f,
            20f
        ),

        Particle(
            0.78f,
            0.82f,
            2f,
            0.80f,
            1.10f,
            9f
        ),

        Particle(
            0.88f,
            0.46f,
            3.5f,
            0.55f,
            0.80f,
            16f
        ),

        Particle(
            0.94f,
            0.17f,
            2.5f,
            0.65f,
            0.95f,
            11f
        ),

        Particle(
            0.13f,
            0.87f,
            4f,
            0.40f,
            0.60f,
            18f
        ),

        Particle(
            0.25f,
            0.68f,
            2f,
            0.80f,
            1.15f,
            9f
        ),

        Particle(
            0.37f,
            0.33f,
            2.5f,
            0.60f,
            0.85f,
            13f
        ),

        Particle(
            0.49f,
            0.92f,
            3.5f,
            0.45f,
            0.72f,
            15f
        ),

        Particle(
            0.60f,
            0.19f,
            2f,
            0.75f,
            1.05f,
            10f
        ),

        Particle(
            0.72f,
            0.59f,
            4f,
            0.40f,
            0.58f,
            18f
        ),

        Particle(
            0.83f,
            0.27f,
            2f,
            0.80f,
            1.12f,
            10f
        ),

        Particle(
            0.91f,
            0.73f,
            3.5f,
            0.50f,
            0.77f,
            15f
        ),

        Particle(
            0.05f,
            0.57f,
            2f,
            0.75f,
            1.20f,
            8f
        )
    )


// ----------------------------------------------------
// PARTICLE + FLUID BACKGROUND
// ----------------------------------------------------

@Composable
private fun ParticleFluidBackground(

    modifier: Modifier =
        Modifier

) {

    val transition =
        rememberInfiniteTransition(
            label =
                "particleBackground"
        )

    // Main particle movement
    val movement by
    transition.animateFloat(

        initialValue = 0f,

        targetValue = 1f,

        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(
                        durationMillis =
                            15000,

                        easing =
                            LinearEasing
                    ),

                repeatMode =
                    RepeatMode.Restart
            ),

        label =
            "particleMovement"
    )

    // Background blobs
    val blobMovement by
    transition.animateFloat(

        initialValue =
            -0.12f,

        targetValue =
            0.17f,

        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(
                        durationMillis =
                            8500,

                        easing =
                            FastOutSlowInEasing
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label =
            "blobMovement"
    )

    // Particle pulse
    val pulse by
    transition.animateFloat(

        initialValue = 0.65f,

        targetValue = 1f,

        animationSpec =
            infiniteRepeatable(

                animation =
                    tween(
                        durationMillis =
                            2200
                    ),

                repeatMode =
                    RepeatMode.Reverse
            ),

        label =
            "particlePulse"
    )

    Canvas(
        modifier =
            modifier.fillMaxSize()
    ) {

        // --------------------------------------
        // BASE BACKGROUND
        // --------------------------------------

        drawRect(

            brush =
                Brush.linearGradient(

                    colors =
                        listOf(

                            Color(
                                0xFF050816
                            ),

                            Color(
                                0xFF0B1026
                            ),

                            Color(
                                0xFF03040C
                            )
                        ),

                    start =
                        Offset.Zero,

                    end =
                        Offset(
                            size.width,
                            size.height
                        )
                )
        )


        // --------------------------------------
        // BLUE FLUID GLOW
        // --------------------------------------

        drawCircle(

            brush =
                Brush.radialGradient(

                    colors =
                        listOf(

                            Color(
                                0xFF4361EE
                            ).copy(
                                alpha =
                                    0.38f
                            ),

                            Color.Transparent
                        )
                ),

            radius =
                size.width * 0.85f,

            center =
                Offset(

                    x =
                        size.width *
                                (
                                        0.30f +
                                                blobMovement
                                        ),

                    y =
                        size.height *
                                0.18f
                )
        )


        // --------------------------------------
        // PURPLE FLUID GLOW
        // --------------------------------------

        drawCircle(

            brush =
                Brush.radialGradient(

                    colors =
                        listOf(

                            Color(
                                0xFF9D4EDD
                            ).copy(
                                alpha =
                                    0.30f
                            ),

                            Color.Transparent
                        )
                ),

            radius =
                size.width * 0.90f,

            center =
                Offset(

                    x =
                        size.width *
                                (
                                        0.72f -
                                                blobMovement
                                        ),

                    y =
                        size.height *
                                0.73f
                )
        )


        // --------------------------------------
        // CYAN GLOW
        // --------------------------------------

        drawCircle(

            brush =
                Brush.radialGradient(

                    colors =
                        listOf(

                            Color(
                                0xFF00C8FF
                            ).copy(
                                alpha =
                                    0.13f
                            ),

                            Color.Transparent
                        )
                ),

            radius =
                size.width * 0.65f,

            center =
                Offset(

                    x =
                        size.width *
                                (
                                        0.30f +
                                                blobMovement
                                        ),

                    y =
                        size.height *
                                0.88f
                )
        )


        // --------------------------------------
        // PARTICLES
        // --------------------------------------

        particleList
            .forEachIndexed {
                    index,
                    particle ->

                // each particle has its own
                // speed
                val travel =
                    movement *
                            particle.speed

                var normalizedY =
                    particle.y -
                            travel

                normalizedY %= 1f

                if (
                    normalizedY < 0
                ) {

                    normalizedY += 1f
                }


                // slight left/right wave
                val wave =

                    sin(
                        (
                                movement *
                                        6.28318f *
                                        2f
                                ) +
                                index
                    ) *

                            particle.wave


                val particleX =

                    size.width *
                            particle.x +
                            wave


                val particleY =

                    size.height *
                            normalizedY


                // Outer glow
                drawCircle(

                    color =
                        Color(
                            0xFF6EDBFF
                        )
                            .copy(
                                alpha =
                                    particle.alpha *
                                            0.12f *
                                            pulse
                            ),

                    radius =
                        particle.radius *
                                5f,

                    center =
                        Offset(
                            particleX,
                            particleY
                        )
                )


                // Medium glow
                drawCircle(

                    color =
                        Color(
                            0xFFBDEFFF
                        )
                            .copy(
                                alpha =
                                    particle.alpha *
                                            0.25f *
                                            pulse
                            ),

                    radius =
                        particle.radius *
                                2.5f,

                    center =
                        Offset(
                            particleX,
                            particleY
                        )
                )


                // Main particle
                drawCircle(

                    color =
                        Color.White.copy(

                            alpha =
                                particle.alpha *
                                        pulse
                        ),

                    radius =
                        particle.radius,

                    center =
                        Offset(
                            particleX,
                            particleY
                        )
                )
            }
    }
}