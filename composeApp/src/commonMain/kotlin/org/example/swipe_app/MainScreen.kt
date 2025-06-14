package org.example.swipe_app

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun MainScreen() {
    //for basic text show
    //val cards = remember { mutableStateListOf("User A", "User B", "User C", "User D", "User E", "User F", "User G", "User H", "User I", "User J", "User K", "User L", "User M", "User N", "User O") }

    var isSwipeBlocked by remember { mutableStateOf(false) }
    var dislikeCount by remember { mutableStateOf(0) }

    val cards = remember {
        mutableStateListOf(
            PersonCard("Alice", "https://picsum.photos/id/237/400/600"),
            PersonCard("Bob", "https://picsum.photos/id/238/400/600"),
            PersonCard("Charlie", "https://picsum.photos/id/239/400/600"),
            PersonCard("Diana", "https://picsum.photos/id/240/400/600"),
            PersonCard("Eve", "https://picsum.photos/id/241/400/600"),
            PersonCard("Frank", "https://picsum.photos/id/242/400/600"),
            PersonCard("Grace", "https://picsum.photos/id/243/400/600"),
            PersonCard("Hank", "https://picsum.photos/id/244/400/600"),
            PersonCard("Ivy", "https://picsum.photos/id/245/400/600"),
            PersonCard("Jack", "https://picsum.photos/id/246/400/600"),
            PersonCard("Kelly", "https://picsum.photos/id/247/400/600"),
            PersonCard("Liam", "https://picsum.photos/id/248/400/600"),

        )
    }

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        cards.asReversed().forEachIndexed { index, person ->
            SwipeableCard(
                //name = cards[index].name,
                person = person,
                index = index,
                onSwipedLeft = {
                    println("Swiped left: $person")
                },
                onSwipedRight = {
                    dislikeCount++
                    println("Dislike count: $dislikeCount")
                    if (dislikeCount >= 10) {
                        dislikeCount = 0
                        println("Sending batched dislike to API")
                        // call batchDislike API here
                    }
                },
                onSwipeCompleted = {
                    cards.remove(person)
                    isSwipeBlocked = false
                },
                isTop = index == cards.lastIndex,
                isSwipeBlocked = isSwipeBlocked,
                blockSwipe = { isSwipeBlocked = true }
            )
        }
    }
}

@Composable
fun SwipeableCard(
    person: PersonCard,
    index: Int,
    isTop: Boolean,
    isSwipeBlocked: Boolean,
    blockSwipe: () -> Unit,
    onSwipedLeft: () -> Unit,
    onSwipedRight: () -> Unit,
    onSwipeCompleted: () -> Unit,
) {
    val swipeThreshold = with(LocalDensity.current) { 100.dp.toPx() }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    val rotation by derivedStateOf { (offsetX.value / 20f).coerceIn(-30f, 30f) }
    val scale = if (!isTop) 0.96f else 1f

    val dragState = rememberDraggableState { delta ->
        scope.launch {
            offsetX.snapTo(offsetX.value + delta) // smoother than animate inside drag
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(0.75f)
            .graphicsLayer {
                translationX = offsetX.value
                rotationZ = rotation
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(16.dp))
            .background(Color.LightGray)
            .then(
                if (isTop && !isSwipeBlocked) Modifier.draggable(
                    orientation = Orientation.Horizontal,
                    state = dragState,
                    onDragStopped = {
                        scope.launch {
                            val target = when {
                                offsetX.value > swipeThreshold -> {
                                    blockSwipe()
                                    offsetX.animateTo(1000f, tween(250))
                                    onSwipedRight()
                                    1000f
                                }

                                offsetX.value < -swipeThreshold -> {
                                    blockSwipe()
                                    offsetX.animateTo(-1000f, tween(250))
                                    onSwipedLeft()
                                    -1000f
                                }

                                else -> {
                                    offsetX.animateTo(0f, tween(250))
                                    0f
                                }
                            }

                            if (target != 0f) {
                                delay(300)
                                onSwipeCompleted()
                            }
                        }
                    }
                ) else Modifier
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = person.imageUrl,
            contentDescription = person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            Modifier
                .fillMaxWidth()
                .background(Color.Black.copy(alpha = 0.5f))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(person.name, color = Color.White, fontSize = 20.sp)
        }
    }
}


//have some lag issue
/*@Composable
fun SwipeableCard(
    person: PersonCard,
    index: Int,
    isTop: Boolean,
    isSwipeBlocked: Boolean,
    blockSwipe: () -> Unit,
    onSwipedLeft: () -> Unit,
    onSwipedRight: () -> Unit,
    onSwipeCompleted: () -> Unit
) {
    val swipeThreshold = with(LocalDensity.current) { 120.dp.toPx() }
    val scope = rememberCoroutineScope()
    val offsetX = remember { Animatable(0f) }

    val rotationnZ by derivedStateOf { (offsetX.value / 30f).coerceIn(-30f, 30f) }
    val scale = if (!isTop) 0.95f else 1f

    Box(
        modifier = Modifier
            .fillMaxWidth(0.85f)
            .aspectRatio(0.75f)
            .graphicsLayer {
                translationX = offsetX.value
                rotationZ = rotationnZ
                scaleX = scale
                scaleY = scale
            }
            .clip(RoundedCornerShape(20.dp))
            .background(Color.LightGray)
            .draggable(
                orientation = Orientation.Horizontal,
                state = rememberDraggableState { delta ->
                    if (isTop && !isSwipeBlocked) {
                        scope.launch {
                            offsetX.snapTo(offsetX.value + delta)
                        }
                    }
                },
                onDragStopped = { velocity ->
                    if (isTop && !isSwipeBlocked) {
                        when {
                            offsetX.value > swipeThreshold -> {
                                blockSwipe()
                                scope.launch {
                                    offsetX.animateTo(1000f, tween(250))
                                    onSwipedRight()
                                    delay(300)
                                    onSwipeCompleted()
                                }
                            }

                            offsetX.value < -swipeThreshold -> {
                                blockSwipe()
                                scope.launch {
                                    offsetX.animateTo(-1000f, tween(250))
                                    onSwipedLeft()
                                    delay(300)
                                    onSwipeCompleted()
                                }
                            }

                            else -> {
                                scope.launch {
                                    offsetX.animateTo(0f, tween(300))
                                }
                            }
                        }
                    }
                }
            ),
        contentAlignment = Alignment.BottomCenter
    ) {
        AsyncImage(
            model = person.imageUrl,
            contentDescription = person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            Modifier
                .fillMaxWidth()
                .background(Color(0xAA000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(person.name, color = Color.White, fontSize = 24.sp)
        }
    }
}*/


//have some lag issue
/*
@Composable
fun SwipeableCard(
    person: PersonCard,
    index: Int,
    onSwipedLeft: () -> Unit,
    onSwipedRight: () -> Unit,
    onSwipeCompleted: () -> Unit,
    isTop: Boolean,
    isSwipeBlocked: Boolean,
    blockSwipe: () -> Unit
) {
    val offsetX = remember { Animatable(0f) }
    val swipeThreshold = with(LocalDensity.current) { 120.dp.toPx() }
    val scope = rememberCoroutineScope()

    val rotationDegrees by derivedStateOf { (offsetX.value / 60).coerceIn(-40f, 40f) }

    // Back card scale (slightly smaller)
    val scale = if (!isTop) 0.95f else 1f

    Box(
        Modifier
            .fillMaxWidth(0.8f)
            .aspectRatio(0.75f)
                /*------ swipe-style animations -------*/
            .graphicsLayer {
                translationX = offsetX.value
                rotationZ = rotationDegrees
                scaleX = scale
                scaleY = scale
            }

                /*----- basic animations ----*/
            //.offset { IntOffset(offsetX.value.toInt(), 0) }
            .clip(RoundedCornerShape(20.dp))
            .background(Color(0xFFBB86FC))
            .pointerInput(isTop, isSwipeBlocked) {
                if (isTop && !isSwipeBlocked) {
                    detectDragGestures(
                        onDrag = { _, dragAmount ->
                            scope.launch {
                                offsetX.snapTo(offsetX.value + dragAmount.x)
                            }
                        },
                        onDragEnd = {
                            when {
                                offsetX.value < -swipeThreshold -> {
                                    blockSwipe()
                                    scope.launch {
                                        offsetX.animateTo(-1000f, tween(300))
                                        onSwipedLeft()
                                        delay(400)
                                        onSwipeCompleted()
                                    }
                                }
                                offsetX.value > swipeThreshold -> {
                                    scope.launch {
                                        offsetX.animateTo(1000f, tween(300))
                                        onSwipedRight()
                                        delay(200)
                                        onSwipeCompleted()
                                    }
                                }
                                else -> {
                                    scope.launch {
                                        offsetX.animateTo(0f, tween(300))
                                    }
                                }
                            }
                        }
                    )
                }
            },
        contentAlignment = Alignment.BottomCenter
    ) {
      AsyncImage(
            model =  person.imageUrl,
            contentDescription = person.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            Modifier
                .fillMaxWidth()
                .background(Color(0xAA000000))
                .padding(16.dp),
            contentAlignment = Alignment.Center
        )

    {
        Text(person.name, color = Color.White, fontSize = 24.sp)
    }
    }
}*/


