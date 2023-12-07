package edu.farmingdale.alrajab.dragdropanimation_sc

import android.animation.ObjectAnimator
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Canvas
import android.graphics.Point
import android.graphics.drawable.AnimationDrawable
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.DragEvent
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.ImageView
import edu.farmingdale.alrajab.dragdropanimation_sc.databinding.ActivityDragAndDropViewsBinding

class DragAndDropViews : AppCompatActivity() {
    lateinit var binding: ActivityDragAndDropViewsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDragAndDropViewsBinding.inflate(layoutInflater)

        setContentView(binding.root)
        binding.holder01.setOnDragListener(arrowDragListener)
        binding.holder02.setOnDragListener(arrowDragListener)
        binding.holder03.setOnDragListener(arrowDragListener)
        binding.holder04.setOnDragListener(arrowDragListener)
        binding.holder05.setOnDragListener(arrowDragListener)

        binding.upMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.downMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.forwardMoveBtn.setOnLongClickListener(onLongClickListener)
        binding.backMoveBtn.setOnLongClickListener(onLongClickListener)

        val rocketImageView: ImageView = findViewById(R.id.rocketImageView)
        rocketImageView.setBackgroundResource(R.drawable.flying_rocket) // Use the drawable resource that contains the information for the 3 different rocket images and how long they appear on screen
        binding.playAnimationButton.setOnClickListener {
            val rocketAnimation = rocketImageView.background as AnimationDrawable
            if (rocketAnimation.isRunning) {
                rocketAnimation.stop()
                // Update the button's text based on the state of the rocket animation
                binding.playAnimationButton.setText("Play Animation")
            } else {
                rocketAnimation.start()
                binding.playAnimationButton.setText("Stop Animation")
            }
        }

        binding.rotateButton.setOnClickListener {
            val rotateAnimator = ObjectAnimator.ofFloat(rocketImageView, "rotation", 0f, 360f)  // Rotate the rocket image view 360 degrees
            rotateAnimator.duration = 1000 // 1 second rotation
            rotateAnimator.interpolator = LinearInterpolator() // For smooth animation
            rotateAnimator.start() // Start the rotation
        }

        binding.translateButton.setOnClickListener {
            val translateAnimator = ObjectAnimator.ofFloat(rocketImageView, "translationX", 0f, 300f, 0f) // Translate the rocket image view to the right
            translateAnimator.duration = 1000 // 1 second translation
            translateAnimator.interpolator = LinearInterpolator() // For smooth animation
            translateAnimator.start() // Start the translation
        }
    }



    private val onLongClickListener = View.OnLongClickListener { view: View ->
        (view as? Button)?.let {

            val item = ClipData.Item(it.tag as? CharSequence)

            val dragData = ClipData( it.tag as? CharSequence,
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN), item)
            val myShadow = ArrowDragShadowBuilder(it)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                it.startDragAndDrop(dragData, myShadow, null, 0)
            } else {
                it.startDrag(dragData, myShadow, null, 0)
            }

            true
        }
        false
    }




    private val arrowDragListener = View.OnDragListener { view, dragEvent ->
        (view as? ImageView)?.let {
            when (dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED -> {
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    // Highlight the border when drag entered
                    view.setBackgroundResource(R.drawable.image_border_highlighted)
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_EXITED-> {
                    // Reset it to a red border when drag exits
                    view.setBackgroundResource(R.drawable.image_border)
                    return@OnDragListener true
                }
                // No need to handle this for our use case.
                DragEvent.ACTION_DRAG_LOCATION -> {
                    return@OnDragListener true
                }

                DragEvent.ACTION_DROP -> {
                    // Read color data from the clip data and apply it to the card view background.
                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    val lbl = item.text.toString()
                    Log.d("BCCCCCCCCCCC", "NOTHING > >  " + lbl)
                   when(lbl.toString()){
                       "UP"->view.setImageResource( R.drawable.ic_baseline_arrow_upward_24)
                       "DOWN"->view.setImageResource(R.drawable.ic_baseline_arrow_downward_24)
                       "FORWARD"->view.setImageResource(R.drawable.ic_baseline_arrow_forward_24)
                       "BACK"->view.setImageResource(R.drawable.ic_baseline_arrow_back_24)
                   }
                    view.setBackgroundResource(R.drawable.image_border)
                    return@OnDragListener true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    return@OnDragListener true
                }
                else -> return@OnDragListener false
            }
        }
        false
    }


    private class ArrowDragShadowBuilder(view: View) : View.DragShadowBuilder(view) {
        private val shadow = view.background
        override fun onProvideShadowMetrics(size: Point, touch: Point) {
            val width: Int = view.width
            val height: Int = view.height
            shadow?.setBounds(0, 0, width, height)
            size.set(width, height)
            touch.set(width / 2, height / 2)
        }
        override fun onDrawShadow(canvas: Canvas) {
            shadow?.draw(canvas)
        }
    }
}