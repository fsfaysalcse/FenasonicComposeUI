package me.fsfaysalcse.fanasonic

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import fanasonic.composeapp.generated.resources.Res
import fanasonic.composeapp.generated.resources.ic_battery
import fanasonic.composeapp.generated.resources.ic_power
import fanasonic.composeapp.generated.resources.ic_propeller
import fanasonic.composeapp.generated.resources.ic_speed
import fanasonic.composeapp.generated.resources.ic_temperature
import org.jetbrains.compose.resources.painterResource

@Composable
fun FenasonicScreen() {
    var speedAnimation by remember { mutableFloatStateOf(0f) }

    var isFanOn by remember { mutableStateOf(false) }
    var fanSpeed by remember { mutableStateOf(70f) } // Fan speed as percentage out of 100

    val animatedSpeed = animateFloatAsState(
        targetValue = fanSpeed,
        animationSpec = androidx.compose.animation.core.tween(
            durationMillis = 300,
            easing = LinearEasing
        )
    )

    LaunchedEffect(fanSpeed) {
        speedAnimation = animatedSpeed.value
    }

    // Adjust the rotation speed directly to fan speed value
    val rotationAngle by animateFloatAsState(
        targetValue = if (isFanOn) (speedAnimation * 3.6f) else 0f, // Multiply by a factor to convert speed to angle (0-100) -> 0-360 degrees
        animationSpec = if (isFanOn) {
            androidx.compose.animation.core.infiniteRepeatable(
                animation = androidx.compose.animation.core.tween(
                    durationMillis = (100 - (fanSpeed / 100 * 60).toInt()), // Duration changes with fan speed
                    easing = LinearEasing
                ),
                repeatMode = RepeatMode.Restart
            )
        } else {
            androidx.compose.animation.core.tween(
                durationMillis = 0
            )
        }
    )

    Column(
        modifier = Modifier.fillMaxSize()
            .background(MaterialTheme.colors.background)
    ) {
        Box(
            modifier = Modifier
                .padding(top = 50.dp)
                .fillMaxWidth()
                .height(360.dp)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Speedometer(
                currentSpeed = animatedSpeed.value,
                modifier = Modifier
                    .padding(90.dp)
                    .requiredSize(300.dp)
            )

            Image(
                painter = painterResource(Res.drawable.ic_propeller),
                contentDescription = "Propeller",
                modifier = Modifier
                    .size(180.dp)
                    .align(Alignment.Center)
                    .graphicsLayer(
                        rotationZ = rotationAngle
                    )
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .fillMaxWidth()
                    .padding(horizontal = 30.dp)
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = { if (fanSpeed > 10f) fanSpeed -= 10f },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Gray
                    ),
                    shape = RoundedCornerShape(
                        topStart = 30.dp,
                        bottomEnd = 30.dp,
                    )
                ) {
                    Text(
                        text = " – ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Button(
                    onClick = { if (fanSpeed < 100f) fanSpeed += 10f },
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.Transparent
                    ),
                    elevation = ButtonDefaults.elevation(
                        defaultElevation = 0.dp,
                        pressedElevation = 0.dp
                    ),
                    border = BorderStroke(
                        width = 1.dp,
                        color = Color.Gray
                    ),
                    shape = RoundedCornerShape(
                        topEnd = 30.dp,
                        bottomStart = 30.dp,
                    )
                ) {
                    Text(
                        text = " + ",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }



        Spacer(modifier = Modifier.height(50.dp))

        /*Button(
            onClick = { isFanOn = !isFanOn },
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth()
        ) {
            val text = if (isFanOn) "Stop" else "Start"
            Text(text)
        }*/

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .dashedBorder(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Gray,
                                Color.Black,
                            )
                        ),
                        shape = RoundedCornerShape(
                            topStart = 20.dp,
                        )
                    )
                    .clickable {
                        isFanOn = !isFanOn
                    }
                    .padding(vertical = 30.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_power),
                    contentDescription = "power",
                    tint = Color.Black,
                    modifier = Modifier.size(40.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = if (isFanOn) "OFF" else "ON",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .dashedBorder(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Gray,
                                Color.Black,
                            )
                        ),
                        shape = RoundedCornerShape(
                            topEnd = 20.dp,
                        )
                    )
                    .padding(vertical = 30.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_temperature),
                    contentDescription = "power",
                    tint = Color.Black,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "21°C",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .dashedBorder(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Gray,
                                Color.Black,
                            )
                        ),
                        shape = RoundedCornerShape(
                            bottomStart = 20.dp,
                        ),
                    )
                    .padding(vertical = 30.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_speed),
                    contentDescription = "power",
                    tint = Color.Black,
                    modifier = Modifier.size(40.dp),
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "120 rpm",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .dashedBorder(
                        width = 2.dp,
                        brush = Brush.linearGradient(
                            colors = listOf(
                                Color.Gray,
                                Color.Black,
                            )
                        ),
                        shape = RoundedCornerShape(
                            bottomEnd = 20.dp,
                        ),
                    )
                    .padding(vertical = 30.dp, horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_battery),
                    contentDescription = "power",
                    tint = Color.Black,
                    modifier = Modifier.size(40.dp)
                )
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "78%",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        }


    }

}

