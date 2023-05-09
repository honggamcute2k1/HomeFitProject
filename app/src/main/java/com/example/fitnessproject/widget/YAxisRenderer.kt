package com.example.fitnessproject.widget


import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Paint.Align
import android.graphics.RectF
import com.example.fitnessproject.screenDensity
import com.github.mikephil.charting.components.LimitLine.LimitLabelPosition
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.components.YAxis.AxisDependency
import com.github.mikephil.charting.components.YAxis.YAxisLabelPosition
import com.github.mikephil.charting.renderer.YAxisRenderer
import com.github.mikephil.charting.utils.Transformer
import com.github.mikephil.charting.utils.Utils
import com.github.mikephil.charting.utils.ViewPortHandler

class YAxisRenderer : YAxisRenderer {
    private var xPositionValueCurrent = 0F
    private var yPositionValueCurrent = 0F

    companion object {
        const val COLOR_TEMMA = "#E695A3"
        const val DENSITY_SCREEN_PIXEL_2_XL = 3.5F
    }


    constructor(
        viewPortHandler: ViewPortHandler?,
        yAxis: YAxis?,
        trans: Transformer?,
    ) : super(viewPortHandler, yAxis, trans) {
        mYAxis = yAxis
        if (mViewPortHandler != null) {
            mAxisLabelPaint.color = Color.WHITE
            mAxisLabelPaint.textSize = Utils.convertDpToPixel(10f)
            mZeroLinePaint = Paint(Paint.ANTI_ALIAS_FLAG)
            mZeroLinePaint.color = Color.GRAY
            mZeroLinePaint.strokeWidth = 1f
            mZeroLinePaint.style = Paint.Style.STROKE
        }
    }

    override fun renderGridLines(c: Canvas?) {
        if (!mYAxis.isEnabled) return

        if (mYAxis.isDrawGridLinesEnabled) {
            val clipRestoreCount = c!!.save()
            c.clipRect(gridClippingRect)
            val positions = transformedPositions
            mGridPaint.color = mYAxis.gridColor
            mGridPaint.strokeWidth = mYAxis.gridLineWidth
            mGridPaint.pathEffect = mYAxis.gridDashPathEffect
            val gridLinePath = mRenderGridLinesPath
            gridLinePath.reset()

            // draw the grid
//            var i = 0

            mGridPaint?.color = Color.parseColor("#F2F2F2")
            for (i in positions.indices step 2) {
                val text = mYAxis.getFormattedLabel(i / 2)
                if (text.trim().isNotEmpty()) {
                    c.drawPath(linePath(gridLinePath, i, positions), mGridPaint);
                    gridLinePath.reset();
                }
            }
            c.restoreToCount(clipRestoreCount)
        }

        if (mYAxis.isDrawZeroLineEnabled) {
            drawZeroLine(c)
        }
    }

    fun getExtraOffsetLeft(): Float {
        var offsetLeft = 0F
        val limitLines = mYAxis.limitLines
        if (limitLines.size != 0) {
            val limit = limitLines[0].limit
            val labelLimit =
                if (limit < 10F) " ${limitLines[0].limit.toString()} " else limit.toString() /*.replace(".0","")*/
            val width = Utils.calcTextWidth(mAxisLabelPaint, labelLimit)
            offsetLeft = Utils.convertPixelsToDp(width.toFloat())
        }
        return offsetLeft
    }

    override fun renderAxisLabels(c: Canvas) {
        if (!mYAxis.isEnabled || !mYAxis.isDrawLabelsEnabled) return

        val positions = transformedPositions

        mAxisLabelPaint.typeface = mYAxis.typeface
        mAxisLabelPaint.textSize = mYAxis.textSize
        mAxisLabelPaint.color = Color.WHITE

        val xoffset = mYAxis.xOffset
        val yoffset = Utils.calcTextHeight(
            mAxisLabelPaint,
            "A"
        ) / 2.5f + mYAxis.yOffset

        val dependency = mYAxis.axisDependency
        val labelPosition = mYAxis.labelPosition

        var xPos = 0f
        xPositionValueCurrent = mViewPortHandler.offsetLeft() - xoffset
        if (dependency == AxisDependency.LEFT) {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.textAlign = Align.RIGHT
                xPos = mViewPortHandler.offsetLeft() - xoffset
            } else {
                mAxisLabelPaint.textAlign = Align.LEFT
                xPos = mViewPortHandler.offsetLeft() + xoffset
            }
        } else {
            if (labelPosition == YAxisLabelPosition.OUTSIDE_CHART) {
                mAxisLabelPaint.textAlign = Align.LEFT
                xPos = mViewPortHandler.contentRight() + xoffset
            } else {
                mAxisLabelPaint.textAlign = Align.RIGHT
                xPos = mViewPortHandler.contentRight() - xoffset
            }
        }

        drawYLabels(c, xPos, positions, yoffset)
        val pts = mRenderLimitLinesBuffer
        pts[0] = 0F
        pts[1] = 0F
        val limitLines = mYAxis.limitLines
        if (limitLines.size != 0) {
            val limit = limitLines[0].limit
            val labelLimit =
                if (limit < 10F) " ${limitLines[0].limit.toString()} " else limit.toString()
            val paint = mAxisLabelPaint
            paint.color = Color.parseColor(COLOR_TEMMA)
            // if not add kg change 27.5 to 35F
//            paint.textSize = (25.5F * screenDensity / DENSITY_SCREEN_PIXEL_2_XL)
            val labelLineHeight =
                Utils.calcTextHeight(paint, labelLimit)
                    .toFloat()
            val labelLineWidth = Utils.calcTextWidth(paint, labelLimit).toFloat()
            c.drawRoundRect(
                RectF(
                    0f,
                    yPositionValueCurrent - (labelLineHeight - (8F * screenDensity / DENSITY_SCREEN_PIXEL_2_XL)),
                    mViewPortHandler.contentLeft(),
                    yPositionValueCurrent + labelLineHeight
                ),
                (25F * screenDensity / DENSITY_SCREEN_PIXEL_2_XL),
                (25F * screenDensity / DENSITY_SCREEN_PIXEL_2_XL),
                paint
            )
            paint.color = Color.WHITE
            val xLabel =
                mViewPortHandler.contentLeft() - (mViewPortHandler.contentLeft() - labelLineWidth) / 2F
            c.drawText(
                labelLimit,
                xLabel,
                yPositionValueCurrent + labelLineHeight / 2F + (2F * screenDensity / DENSITY_SCREEN_PIXEL_2_XL),
                paint
            )
        }
    }

    override fun drawYLabels(
        c: Canvas,
        fixedPosition: Float,
        positions: FloatArray,
        offset: Float
    ) {
        super.drawYLabels(c, fixedPosition, positions, offset)
        val label = "kg"
        c.drawText(
            label, fixedPosition,
            mViewPortHandler.contentBottom() + Utils.calcTextHeight(mAxisLabelPaint, label) / 2,
            mAxisLabelPaint
        )

    }

    override fun renderLimitLines(c: Canvas) {
        val limitLines = mYAxis.limitLines
        if (limitLines == null || limitLines.size <= 0) return
        val pts = mRenderLimitLinesBuffer
        pts[0] = 0F
        pts[1] = 0F
        val limitLinePath = mRenderLimitLines
        limitLinePath.reset()
        for (i in limitLines.indices) {
            val l = limitLines[i]
            if (!l.isEnabled) continue
            val clipRestoreCount = c.save()
            mLimitLineClippingRect.set(mViewPortHandler.contentRect)
            mLimitLineClippingRect.inset(0f, -l.lineWidth)
            c.clipRect(mLimitLineClippingRect)
            mLimitLinePaint.style = Paint.Style.STROKE
            mLimitLinePaint.color = l.lineColor
            mLimitLinePaint.strokeWidth = l.lineWidth
            mLimitLinePaint.pathEffect = l.dashPathEffect
            pts[1] = l.limit
            mTrans.pointValuesToPixel(pts)
            limitLinePath.moveTo(mViewPortHandler.contentLeft(), pts[1])
            limitLinePath.lineTo(mViewPortHandler.contentRight(), pts[1])
            mLimitLinePaint.color = Color.parseColor(COLOR_TEMMA)

            c.drawPath(limitLinePath, mLimitLinePaint)

            yPositionValueCurrent = pts[1]
            val paint = mAxisLabelPaint
            paint.color = Color.parseColor(COLOR_TEMMA)
            limitLinePath.reset()
            val label = l.label
            // if drawing the limit-value label is enabled
            if (label != null && label != "") {
                mLimitLinePaint.style = l.textStyle
                mLimitLinePaint.pathEffect = null
                mLimitLinePaint.color = l.textColor
                mLimitLinePaint.typeface = l.typeface
                mLimitLinePaint.strokeWidth = 0.5f
                mLimitLinePaint.textSize = l.textSize
                val labelLineHeight =
                    Utils.calcTextHeight(mLimitLinePaint, label)
                        .toFloat()
                val xOffset =
                    Utils.convertDpToPixel(4f) + l.xOffset
                val yOffset = l.lineWidth + labelLineHeight + l.yOffset
                val position = l.labelPosition
                if (position == LimitLabelPosition.RIGHT_TOP) {
                    mLimitLinePaint.textAlign = Align.RIGHT
                    c.drawText(
                        label,
                        mViewPortHandler.contentRight() - xOffset,
                        pts[1] - yOffset + labelLineHeight, mLimitLinePaint
                    )
                } else if (position == LimitLabelPosition.RIGHT_BOTTOM) {
                    mLimitLinePaint.textAlign = Align.RIGHT
                    c.drawText(
                        label,
                        mViewPortHandler.contentRight() - xOffset,
                        pts[1] + yOffset, mLimitLinePaint
                    )
                } else if (position == LimitLabelPosition.LEFT_TOP) {
                    mLimitLinePaint.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        mViewPortHandler.contentLeft() + xOffset,
                        pts[1] - yOffset + labelLineHeight, mLimitLinePaint
                    )
                } else {
                    mLimitLinePaint.textAlign = Align.LEFT
                    c.drawText(
                        label,
                        mViewPortHandler.offsetLeft() + xOffset,
                        pts[1] + yOffset, mLimitLinePaint
                    )
                }
            }
            c.restoreToCount(clipRestoreCount)
        }
    }

    var labelCountRoot = -1
    override fun computeAxisValues(min: Float, max: Float) {

        val labelCount = if (labelCountRoot > -1) labelCountRoot else mAxis.labelCount
        val range = Math.abs(max - min).toDouble()

        if (labelCount == 0 || range <= 0 || java.lang.Double.isInfinite(range)) {
            mAxis.mEntries = floatArrayOf()
            mAxis.mCenteredEntries = floatArrayOf()
            mAxis.mEntryCount = 0
            return
        }

        // Find out how much spacing (in y value space) between axis values

        // Find out how much spacing (in y value space) between axis values
        val rawInterval = range / labelCount
        var interval = Utils.roundToNextSignificant(rawInterval).toDouble()

        // If granularity is enabled, then do not allow the interval to go below specified granularity.
        // This is used to avoid repeated values when rounding values for display.

        // If granularity is enabled, then do not allow the interval to go below specified granularity.
        // This is used to avoid repeated values when rounding values for display.
        if (mAxis.isGranularityEnabled) interval =
            if (interval < mAxis.granularity) mAxis.granularity.toDouble() else interval

        // Normalize interval

        // Normalize interval
        val intervalMagnitude =
            Utils.roundToNextSignificant(Math.pow(10.0, Math.log10(interval).toDouble())).toDouble()
        val intervalSigDigit = (interval / intervalMagnitude).toInt()
        if (intervalSigDigit > 5) {
            // Use one order of magnitude higher, to avoid intervals like 0.9 or
            // 90
            interval = Math.floor(10 * intervalMagnitude)
        }

        var n = if (mAxis.isCenterAxisLabelsEnabled) 1 else 0

        // force label count

        // force label count
        if (mAxis.isForceLabelsEnabled) {
            interval = range.toFloat() / (labelCount - 1).toDouble()
            mAxis.mEntryCount = labelCount
            if (mAxis.mEntries.size < labelCount) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = FloatArray(labelCount)
            }
            var v = min
            for (i in 0 until labelCount) {
                mAxis.mEntries[i] = v
                v += interval.toFloat()
            }
            n = labelCount

            // no forced count
        } else {
            var first = if (interval == 0.0) 0.0 else Math.ceil(min / interval) * interval
            if (mAxis.isCenterAxisLabelsEnabled) {
                first -= interval
            }
            val last =
                if (interval == 0.0) 0.0 else Utils.nextUp(Math.floor(max / interval) * interval)
            var f: Double
            var i: Int
            if (interval != 0.0) {
                f = first
                while (f <= last) {
                    ++n
                    f += interval
                }
            }
            mAxis.mEntryCount = n
            if (mAxis.mEntries.size < n) {
                // Ensure stops contains at least numStops elements.
                mAxis.mEntries = FloatArray(n)
            }
            f = first
            i = 0
            while (i < n) {
                if (f == 0.0) // Fix for negative zero case (Where value == -0.0, and 0.0 == -0.0)
                    f = 0.0
                mAxis.mEntries[i] = f.toFloat()
                f += interval
                ++i
            }
        }

        // set decimals

        // set decimals
        if (interval < 1) {
            mAxis.mDecimals = Math.ceil(-Math.log10(interval)).toInt()
        } else {
            mAxis.mDecimals = 0
        }

        if (mAxis.isCenterAxisLabelsEnabled) {
            if (mAxis.mCenteredEntries.size < n) {
                mAxis.mCenteredEntries = FloatArray(n)
            }
            val offset = interval.toFloat() / 2f
            for (i in 0 until n) {
                mAxis.mCenteredEntries[i] = mAxis.mEntries[i] + offset
            }
        }
    }
}