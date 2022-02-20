package ru.androidlearning.caranimation

import android.animation.ObjectAnimator
import android.graphics.Path
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.animation.doOnEnd
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import ru.androidlearning.caranimation.databinding.ActivityMainBinding
import kotlin.math.abs

class MainActivity : AppCompatActivity(R.layout.activity_main) {
    private val viewBinding: ActivityMainBinding by viewBinding(ActivityMainBinding::bind)
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    private val carIv by lazy { viewBinding.carIv }
    private val displayMetrics by lazy { DisplayMetrics() }

    private val width: Float by lazy {
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.widthPixels.toFloat()
    }

    private val height: Float by lazy {
        @Suppress("DEPRECATION")
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        displayMetrics.heightPixels.toFloat()
    }

    private val carSizeDelta by lazy { abs(carIv.height - carIv.width + 16) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViews()
        viewModel.getLiveData().observe(this) { uiState -> render(uiState) }
    }

    private fun render(uiState: UiState) {
        if (uiState.isActionInProgress) {
            when (uiState.nextPosition) {
                UiState.Position.TOP_LEFT -> moveToTopLeft()
                UiState.Position.TOP_RIGHT -> moveToTopRight()
                UiState.Position.BOTTOM_RIGHT -> moveToBottomRight()
                UiState.Position.BOTTOM_LEFT -> moveToBottomLeft()
            }
        }
    }

    private fun initViews() {
        viewBinding.carIv.setOnClickListener {
            viewModel.handleOnCarClick()
        }
    }

    // Animations region
    private fun moveToTopRight() {
        ObjectAnimator.ofFloat(carIv, "translationX", width - carIv.height - carIv.width).apply {
            duration = STRAIGHT_ANIMATION_DURATION
            start()
        }.doOnEnd {
            carIv.animate()
                .rotation(180f)
                .setDuration(ARC_ANIMATION_DURATION)
                .start()
            ObjectAnimator.ofFloat(carIv, View.X, View.Y, getArcPathToRight()).apply {
                duration = ARC_ANIMATION_DURATION
                start()
            }.doOnEnd {
                viewModel.onAnimationFinished()
            }
        }
    }

    private fun moveToBottomRight() {
        ObjectAnimator.ofFloat(carIv, "translationY", height - carIv.height - carIv.width - carSizeDelta).apply {
            duration = STRAIGHT_ANIMATION_DURATION
            start()
        }.doOnEnd {
            carIv.animate()
                .rotation(270f)
                .setDuration(ARC_ANIMATION_DURATION)
                .start()
            ObjectAnimator.ofFloat(carIv, View.X, View.Y, getArcPathToBottom()).apply {
                duration = ARC_ANIMATION_DURATION
                start()
            }.doOnEnd {
                viewModel.onAnimationFinished()
            }
        }
    }

    private fun moveToBottomLeft() {
        ObjectAnimator.ofFloat(carIv, "translationX", 0f).apply {
            duration = STRAIGHT_ANIMATION_DURATION
            start()
        }.doOnEnd {
            carIv.animate()
                .rotation(360f)
                .setDuration(ARC_ANIMATION_DURATION)
                .start()
            ObjectAnimator.ofFloat(carIv, View.X, View.Y, getArcPathToLeft()).apply {
                duration = ARC_ANIMATION_DURATION
                start()
            }.doOnEnd {
                viewModel.onAnimationFinished()
            }
        }
    }

    private fun moveToTopLeft() {
        ObjectAnimator.ofFloat(carIv, "translationY", carIv.height.toFloat() * 0.5f).apply {
            duration = STRAIGHT_ANIMATION_DURATION
            start()
        }.doOnEnd {
            ObjectAnimator.ofFloat(carIv, "rotation", 0f, 90f)
                .setDuration(ARC_ANIMATION_DURATION)
                .start()
            ObjectAnimator.ofFloat(carIv, View.X, View.Y, getArcPathToTop()).apply {
                duration = ARC_ANIMATION_DURATION
                start()
            }.doOnEnd {
                viewModel.onAnimationFinished()
            }
        }
    }

    private fun getArcPathToRight(): Path {
        return Path().apply {
            arcTo(
                width - carIv.height - carIv.width,
                0f,
                width - carIv.width,
                carIv.height.toFloat(),
                -90f,
                90f,
                true
            )
        }
    }

    private fun getArcPathToBottom(): Path {
        return Path().apply {
            arcTo(
                width - carIv.height * 2,
                height - carIv.height * 2 - carSizeDelta,
                width - carIv.width,
                height - carIv.height - carSizeDelta,
                0f,
                90f,
                true
            )
        }
    }

    private fun getArcPathToLeft(): Path {
        return Path().apply {
            arcTo(
                0f,
                height - carIv.height * 3 - carSizeDelta,
                carIv.height.toFloat(),
                height - carIv.height - carSizeDelta,
                90f,
                90f,
                true
            )
        }
    }

    private fun getArcPathToTop(): Path {
        return Path().apply {
            arcTo(
                0f,
                0f,
                (carIv.width * 2).toFloat(),
                carIv.height.toFloat(),
                180f,
                90f,
                true
            )
        }
    }
    // end Animations region

    companion object {
        const val STRAIGHT_ANIMATION_DURATION = 2000L
        const val ARC_ANIMATION_DURATION = 1000L
    }
}


