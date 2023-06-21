package com.example.materibangkitcanvas

import android.graphics.*
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.res.ResourcesCompat
import com.example.materibangkitcanvas.databinding.ActivityMainBinding
import kotlin.text.Typography.half

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val mBitmap = Bitmap.createBitmap(1000, 1000, Bitmap.Config.ARGB_8888) //mbuat bitmap (bidang kertas gambar)
    private val mCanvas = Canvas(mBitmap) //mbuat canvas (pensil tulis)
    private val mPaint = Paint() //mbuat paint (buat ngewarna)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.imageView.setImageBitmap(mBitmap)
        showText()

        binding.like.setOnClickListener {
            showEars()
            showFace()
            showEyes()
            showMouth(isHappy = true)
            showNose()
            showHair()
        }
        binding.dislike.setOnClickListener{
            showEars()
            showFace()
            showEyes()
            showMouth(isHappy = false)
            showNose()
            showHair()

        }
    }

    //cari titik tengah
    private val halfOfWidth = (mBitmap.width/2).toFloat()
    private val halfOfHeight = (mBitmap.height/2).toFloat()

    private val left = 150F
    private val top = 250F
    private val right = mBitmap.width - left
    private val bottom = mBitmap.height.toFloat() - 50F

    //membuat object wajah
    private fun showFace(){
        val face = RectF(left, top, right, bottom)

        mPaint.color = ResourcesCompat.getColor(resources, R.color.yellow_left_skin, null)
        mCanvas.drawArc(face, 90F, 180F, false, mPaint) //drawArc = membangun lingkaran dgn sudut tertentu

        mPaint.color = ResourcesCompat.getColor(resources, R.color.yellow_right_skin, null)
        mCanvas.drawArc(face, 270F, 180F, false, mPaint)
    }

    //membuat object mata
    private fun showEyes(){
        mPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        mCanvas.drawCircle(halfOfWidth - 100F, halfOfHeight - 10F, 50F, mPaint)
        mCanvas.drawCircle(halfOfWidth + 100F, halfOfHeight - 10F, 50F, mPaint)

        mPaint.color = ResourcesCompat.getColor(resources, R.color.white, null)
        mCanvas.drawCircle(halfOfWidth - 120F, halfOfHeight - 20F, 15F, mPaint)
        mCanvas.drawCircle(halfOfWidth + 80F, halfOfHeight - 20F, 15F, mPaint)
    }

    //membuat object mulut
    private fun showMouth(isHappy: Boolean){
        when (isHappy){
            true -> {
                mPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
                val lip = RectF (halfOfWidth - 200F, halfOfHeight - 100F, halfOfHeight +180F, halfOfHeight + 400F)
                mCanvas.drawArc(lip, 25F, 130F, false, mPaint)

                mPaint.color = ResourcesCompat.getColor(resources, R.color.white, null)
                val mouth = RectF(halfOfWidth - 180F, halfOfHeight, halfOfWidth + 180F, halfOfHeight + 380F)
                mCanvas.drawArc(mouth, 25F, 130F, false, mPaint)
            }
            false -> {
                mPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
                val mouth = RectF(halfOfWidth - 200F, halfOfHeight + 260F, halfOfWidth + 180F, halfOfHeight + 330F)
                mCanvas.drawArc(mouth, 0F, -180F, false, mPaint)
            }
        }
    }

    //membuat object hidung
    private fun showNose(){
        mPaint.color = ResourcesCompat.getColor(resources, R.color.black, null)
        mCanvas.drawCircle(halfOfWidth - 40F, halfOfHeight + 140F, 15F, mPaint)
        mCanvas.drawCircle(halfOfWidth + 40F, halfOfHeight + 140F, 15F, mPaint)
    }

    //membuat obejct telinga
    private fun showEars(){
        mPaint.color = ResourcesCompat.getColor(resources, R.color.brown_left_hair, null)
        mCanvas.drawCircle(halfOfWidth - 300F, halfOfHeight - 100F, 100F, mPaint)

        mPaint.color = ResourcesCompat.getColor(resources, R.color.brown_right_hair, null)
        mCanvas.drawCircle(halfOfWidth + 300F, halfOfHeight - 100F, 100F, mPaint)

        mPaint.color = ResourcesCompat.getColor(resources, R.color.red_ear, null)
        mCanvas.drawCircle(halfOfWidth - 300F, halfOfHeight - 100F, 60F, mPaint)
        mCanvas.drawCircle(halfOfWidth + 300F, halfOfHeight - 100F, 60F, mPaint)
    }

    //membuat object rambut dengan clipping
    private fun showHair(){
        mCanvas.save()

        val path = Path()

        path.addCircle(halfOfWidth - 100F, halfOfHeight - 10F, 150F, Path.Direction.CCW)
        path.addCircle(halfOfWidth + 100F, halfOfHeight - 10F, 150F, Path.Direction.CCW)

        val mouth = RectF(halfOfWidth - 250F, halfOfHeight, halfOfWidth + 250F, halfOfHeight + 500F)
        path.addOval(mouth, Path.Direction.CCW)

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            mCanvas.clipPath(path, Region.Op.DIFFERENCE)
        } else {
            mCanvas.clipOutPath(path)
        }

        val face = RectF(left, top, right, bottom)

        mPaint.color = ResourcesCompat.getColor(resources, R.color.brown_left_hair, null)
        mCanvas.drawArc(face, 90F, 180F, false, mPaint)

        mPaint.color = ResourcesCompat.getColor(resources, R.color.brown_right_hair, null)
        mCanvas.drawArc(face, 270F, 180F, false, mPaint)

        mCanvas.restore()
    }

    private val message = "Apakah kamu suka bermanin?"

    private fun showText(){
        val mPaintText = Paint(Paint.FAKE_BOLD_TEXT_FLAG).apply {
            textSize = 50F
            color = ResourcesCompat.getColor(resources, R.color.black, null)
        }

        val mBounds = Rect()
        mPaintText.getTextBounds(message, 0, message.length, mBounds)

        val x: Float = halfOfWidth - mBounds.centerX()
        val y = 50F
        mCanvas.drawText(message, x, y, mPaintText)
    }


}