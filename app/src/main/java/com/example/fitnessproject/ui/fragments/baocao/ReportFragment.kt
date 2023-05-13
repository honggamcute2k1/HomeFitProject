package com.example.fitnessproject.ui.fragments.baocao

import android.content.Intent
import android.graphics.Color
import android.graphics.DashPathEffect
import com.applandeo.materialcalendarview.EventDay
import com.bumptech.glide.Glide
import com.example.fitnessproject.R
import com.example.fitnessproject.domain.model.Gender
import com.example.fitnessproject.domain.model.LevelBMI
import com.example.fitnessproject.domain.model.UserInformationModel
import com.example.fitnessproject.ui.BaseFragment
import com.example.fitnessproject.ui.activities.advise.AdviseActivity
import com.example.fitnessproject.ui.fragments.baocao.dialog.DialogAddHeight
import com.example.fitnessproject.ui.fragments.baocao.dialog.DialogAddWeight
import com.example.fitnessproject.ui.fragments.baocao.dialog.DialogListTopicDetailSelected
import com.example.fitnessproject.ui.showOrGone
import com.example.fitnessproject.widget.YAxisRenderer
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.LimitLine
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import kotlinx.android.synthetic.main.fragment_report.*
import java.text.SimpleDateFormat
import java.util.*


class ReportFragment : BaseFragment<ReportViewModel>() {
    override fun getLayoutId() = R.layout.fragment_report

    private var dialogAddWeight: DialogAddWeight? = null
    private var dialogAddHeight: DialogAddHeight? = null
    private var dialogTopicDetails: DialogListTopicDetailSelected? = null
    private var rendererY: YAxisRenderer? = null

    override fun initScreen() {
        setupLineChart()
        Glide.with(this).load(R.drawable.loading).into(loadingBmi)
        btnAddWeight?.setOnClickListener {
            viewModel.getUserInformationInTime(viewModel.year, viewModel.month)
        }

        btnEdiBmi?.setOnClickListener {
            viewModel.getInformation()
        }

        btnMoreAdvise?.setOnClickListener {
            val intent = Intent(activity, AdviseActivity::class.java)
            intent.putExtra(AdviseActivity.KEY_BMI_ITEM, viewModel.bmiLiveData.value?.second)
            startActivity(intent)
        }

        calendarView?.setOnForwardPageChangeListener {
            changeMonthCalendar()
        }
        calendarView?.setOnPreviousPageChangeListener {
            changeMonthCalendar()
        }

        calendarView?.setOnDayClickListener { eventDay ->
            viewModel.getListTopicDetailModel(eventDay.calendar.time)
        }
    }

    private fun changeMonthCalendar() {
        val currentMonth = calendarView.currentPageDate.get(Calendar.MONTH)
        val currentYear = calendarView.currentPageDate.get(Calendar.YEAR)
        viewModel.year = currentYear
        viewModel.month = currentMonth
        viewModel.getDataInTime(viewModel.year, viewModel.month)
    }

    override fun bindData() {
        super.bindData()
        viewModel.userInfoLiveData.observe(viewLifecycleOwner) {
            dialogAddWeight?.dismiss()
            dialogAddWeight = null
            dialogAddHeight = DialogAddHeight.getInstance(it.first, it.second)
            dialogAddHeight?.show(childFragmentManager, "")
            dialogAddHeight?.onSaveInformationClicked = { height, weight ->
                viewModel.updateBMI(height, weight)
            }
        }

        viewModel.weightListLiveData.observe(viewLifecycleOwner) { list ->
            val lightest = list.minByOrNull { it.weight!! }?.weight ?: "--"
            val heaviest = list.maxByOrNull { it.weight!! }?.weight ?: "--"
            val current =
                list.firstOrNull { it.time.toDateString() == viewModel.getCurrentDateString() }?.weight
                    ?: "--"
            txtWeightLightest?.text = "Nhẹ nhất : $lightest"
            txtWeightHeaviest?.text = "Nặng nhất : $heaviest"
            txtWeightNow?.text = "Hiện tại : $current"
            setDataLineChart(entryLineChartList = list, target = null)
        }

        viewModel.isAddWeightLiveData.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                viewModel.getDataInTime(viewModel.year, viewModel.month)
            }
        }

        viewModel.topicDetailSelectedLiveData.observe(viewLifecycleOwner) { list ->
            val events: MutableList<EventDay> = ArrayList()
            list.forEach { item ->
                val calendar = Calendar.getInstance()
                calendar.time = item.timeDoIt
                events.add(
                    EventDay(
                        calendar,
                        if (viewModel.getGenderUser() == Gender.MALE) R.drawable.ic_male else R.drawable.ic_female
                    )
                )
            }
            calendarView?.setEvents(events)
        }

        viewModel.loadingBMI.observe(viewLifecycleOwner) {
            loadingBmi?.showOrGone(it)
            linearBmi?.showOrGone(!it)
        }

        viewModel.noBmiLiveData.observe(viewLifecycleOwner) {
            txtAdvise?.text = "Bạn chưa có chỉ số BMI, vui lòng bấm nút chỉnh sửa!"
            btnMoreAdvise?.showOrGone(isShow = false)
        }

        viewModel.bmiLiveData.observe(viewLifecycleOwner) {
            val bmi = it.first
            val item = it.second
            txtAdvise?.text =
                "Chỉ số BMI của bạn là ${(bmi * 100).toInt() / 100F}. Bạn đang ở tình trạng ${
                    LevelBMI.getLevel(
                        item.level
                    ).detail
                }..."
            btnMoreAdvise?.showOrGone(isShow = true)
        }

        viewModel.updateInfoUser.observe(viewLifecycleOwner) {
            viewModel.getInformationBMI()
        }

        viewModel.topicDetailLiveData.observe(viewLifecycleOwner) {
            dialogTopicDetails?.dismiss()
            dialogTopicDetails = null
            dialogTopicDetails = DialogListTopicDetailSelected.getInstance(it)
            dialogTopicDetails?.show(childFragmentManager, "")
        }

        viewModel.informationTimeLiveData.observe(viewLifecycleOwner) {
            activity?.let { a ->
                dialogAddWeight?.dismiss()
                dialogAddWeight = null
                dialogAddWeight = DialogAddWeight.getInstance(viewModel.month!!, it)
                dialogAddWeight?.onSaveWeightClicked = { day, weight ->
                    val time = Calendar.getInstance()
                    time.set(Calendar.MONTH, viewModel.month!!)
                    time.set(Calendar.DAY_OF_MONTH, day)
                    viewModel.insertOrUpdateWeight(weight, time.time)
                }
                dialogAddWeight?.show(a.supportFragmentManager, "")
            }
        }
    }


    private fun setupLineChart() {
        rendererY = YAxisRenderer(
            lineChart?.viewPortHandler,
            lineChart?.axisLeft,
            lineChart?.getTransformer(YAxis.AxisDependency.LEFT)
        )
        lineChart?.rendererLeftYAxis = rendererY
        lineChart?.axisLeft?.gridColor = Color.WHITE
        lineChart?.axisRight?.gridColor = Color.WHITE
        lineChart?.xAxis?.gridColor = Color.WHITE
        lineChart?.xAxis?.axisLineWidth = 1F
        lineChart?.xAxis?.axisLineColor = Color.WHITE

        lineChart?.description?.isEnabled = false
        lineChart?.axisRight?.isEnabled = true
        lineChart?.xAxis?.position = XAxis.XAxisPosition.BOTTOM
        lineChart?.setScaleEnabled(false)

        lineChart?.axisLeft?.setDrawAxisLine(false)
        lineChart?.axisRight?.setDrawAxisLine(false)
        lineChart?.axisRight?.setDrawLabels(false)
        lineChart?.setDrawGridBackground(false)
        lineChart?.extraLeftOffset = 15F

        lineChart?.axisRight?.enableGridDashedLine(10f, 10f, 2f)
        lineChart?.axisLeft?.enableGridDashedLine(10f, 10f, 2f)
        lineChart?.xAxis?.enableGridDashedLine(10f, 10f, 2f)
    }

    private fun setDataLineChart(
        target: Float?,
        entryLineChartList: List<UserInformationModel>,
        targetDate: String? = null,
        showValueOnLineChart: Boolean = false,
        drawHighLight: Boolean = false
    ) {
        val res: MutableList<Float> = mutableListOf()
        var isNoValue = true
        entryLineChartList.forEach {
            if (it.weight != null && isNoValue) {
                isNoValue = false
            }
            it.weight?.let { it1 -> res.add(it1.toFloat()) }
        }

        val minimum = if (res.isNotEmpty()) res.sortedWith(compareBy { it }).first() else null
        val maximum = if (res.isNotEmpty()) res.sortedWith(compareBy { it }).last() else null

        var min: Int = 0
        var max: Int = 0
        var yGranularity = 0f

        if (target == null && minimum == null && maximum == null) {
            yGranularity = 1f
            min = 55
            max = 56
        } else if (target != null && minimum == null && maximum == null) {
            yGranularity = 1f
            min = target.toInt() - 1
            max = target.toInt() + 1
        } else {
            val minRaw: Float = if (target == null && minimum == null && maximum == null) return
            else if (target == null) minOf(minimum ?: 0f, maximum ?: 0f)
            else minOf(minimum ?: target, target, maximum ?: target)

            val maxRaw: Float = if (target == null) maxOf(minimum ?: 0f, maximum ?: 0f)
            else maxOf(minimum ?: target, target, maximum ?: target)

            val isAbnormalCase = (maxRaw - minRaw < 1f)

            if (isNoValue) {
                if (target == null || target == 0f) {
                    min = 55
                    max = 56
                } else {
                    min = (target - 2).toInt()
                    max = (target + 2).toInt()
                }
            } else if (isAbnormalCase) {
                min = if (minRaw.toInt() == maxRaw.toInt()) minRaw.toInt() - 1 else minRaw.toInt()
                max = maxRaw.toInt() + 1
            } else {
                min =
                    if (minRaw.toInt() == 0) 0 else if (minRaw.toInt() % 2 == 0) minRaw.toInt() - 2 else minRaw.toInt() - 1
                max = if (maxRaw.toInt() % 2 == 0) maxRaw.toInt() + 2 else maxRaw.toInt() + 1
            }
            yGranularity = when {
                isNoValue -> 1f
                isAbnormalCase -> 1f
                (maxRaw - minRaw) >= 10.0f -> max.toFloat()
                else -> 2f
            }
        }

        val xAxis = lineChart?.xAxis
        val yAxis = lineChart?.axisLeft

        xAxis?.textSize = 11F
        yAxis?.textSize = 11F

        yAxis?.gridLineWidth = 1F
        xAxis?.gridLineWidth = 1F

        val labelListY = arrayListOf<Int>()
        when (yGranularity) {
            1F -> {
                for (i in min..max) {
                    labelListY.add(i)
                }
            }
            2F -> {
                for (i in min..max step 2) {
                    if (i % 2 == 0) {
                        labelListY.add(i)
                    }
                }
            }
            else -> {
                for (i in min..max) {
                    if (i == min || i == max) {
                        labelListY.add(i)
                    }
                }
            }
        }

        yAxis?.granularity = 1F
        rendererY?.labelCountRoot = max - min

        val yBuffer = when {
            yGranularity.toInt() == 1 -> {
                0.2F
            }
            yGranularity < 10F -> {
                ((max - min).toFloat() / ((yAxis?.labelCount ?: 1))) * 0.5F
            }
            yGranularity in 10F..30F -> {
                ((max - min).toFloat() / ((yAxis?.labelCount ?: 1))) * 0.6F
            }
            else -> {
                ((max - min).toFloat() / ((yAxis?.labelCount ?: 1))) * 0.3F
            }
        }

        yAxis?.axisMaximum = max.toFloat() + yBuffer
        yAxis?.axisMinimum = min.toFloat() - yBuffer

        xAxis?.spaceMax = 0.5F
        xAxis?.spaceMin = 0.5F

        yAxis?.valueFormatter = ValueFormatterYLabel(min - yBuffer, max + yBuffer, labelListY)

        val labelList = arrayListOf<String>()
        xAxis?.granularity = 1F
        xAxis?.labelCount = entryLineChartList.size
        xAxis?.position = XAxis.XAxisPosition.BOTTOM
        xAxis?.isGranularityEnabled = true
        labelList.clear()
        for (entry in entryLineChartList) {
            labelList.add(entry.getDayInMonth())
        }
        xAxis?.valueFormatter = (object : IndexAxisValueFormatter(
            labelList
        ) {})

        val values = ArrayList<Entry>()
        for (i in entryLineChartList.indices) {
            entryLineChartList[i].weight?.let {
                Entry(i.toFloat(), it.toFloat())
            }?.let {
                values.add(it)
            }
        }

        val valueFakes = ArrayList<Entry>()
        for (i in entryLineChartList.indices) {
            valueFakes.add(Entry(i.toFloat(), (max.toFloat())))
        }

        yAxis?.setDrawAxisLine(false)
        if (target != null) {
            val lineTarget = LimitLine(target, "")
            lineTarget.lineWidth = 1f
            lineTarget.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
            lineTarget.textSize = 11F
            yAxis?.setDrawLimitLinesBehindData(false)
            yAxis?.removeAllLimitLines()
            yAxis?.addLimitLine(lineTarget)
        }
        val valueFormatter: ValueFormatter =
            object : ValueFormatter() {
                override fun getFormattedValue(value: Float): String {
                    return value.toString()
                }
            }

        val set1 = LineDataSet(values, "DataSet 1")
        val set2 = LineDataSet(valueFakes, "DataSet 2")
        lineChart?.legend?.isEnabled = false
        set1.color = Color.YELLOW
        set1.setCircleColor(Color.YELLOW)
        set1.lineWidth = 1f
        set1.circleRadius = 3.8F
        set1.setDrawCircleHole(false)
        set1.formLineWidth = 1f
        set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 1f)
        set1.formSize = 15f
        set1.valueTextSize = 11F
        if (drawHighLight) {
            val offset = lineChart?.minOffset ?: 0F
            lineChart?.minOffset = 0F
            lineChart?.extraRightOffset = offset
            lineChart?.extraLeftOffset = offset
            set1.isHighlightEnabled = true
            set1.highLightColor = Color.WHITE
            set1.setDrawHorizontalHighlightIndicator(false)
        } else {
            set1.isHighlightEnabled = false
        }
        set1.setDrawValues(showValueOnLineChart)
        val dataSets = ArrayList<ILineDataSet>()
        dataSets.add(set1)

        set2.color = Color.TRANSPARENT
        set2.setCircleColor(Color.TRANSPARENT)
        set2.lineWidth = 1f
        set2.circleRadius = 1F
        set2.setDrawCircleHole(false)
        set2.setDrawValues(false)
        set2.formLineWidth = 1F
        set2.formSize = 0F
        set2.valueTextSize = 0F
        set2.setDrawHighlightIndicators(false)
        dataSets.add(set2)
        val data = LineData(dataSets)
        data.setValueFormatter(valueFormatter)
        lineChart?.data = data
        lineChart?.animateY(1000, Easing.EaseInBounce);
        lineChart?.invalidate()
    }
}

fun Date.toDateString(): String {
    val sdf = SimpleDateFormat("yyyy/MM/dd")
    return sdf.format(this)
}

class ValueFormatterYLabel(val min: Float, val max: Float, val labelList: ArrayList<Int>) :
    ValueFormatter() {

    override fun getFormattedValue(value: Float): String {
        if (labelList.firstOrNull { it == value.toInt() } != null) {
            if (min == value) return ""
            return value.toInt().toString()
        } else {
        }
        return "  "
    }
}