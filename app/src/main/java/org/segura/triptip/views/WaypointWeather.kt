package org.segura.triptip.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.util.TypedValue
import android.view.ContextThemeWrapper
import android.view.View
import androidx.appcompat.content.res.AppCompatResources
import org.segura.triptip.R
import org.segura.triptip.model.weather.WeatherIconDaytime

private const val DEFAULT_STYLE_ATTR = R.attr.waypointWeatherViewStyle
private const val DEFAULT_STYLE_RES = R.style.Widget_Theme_TripTip_WaypointWeather

class WaypointWeather : View {

    private var _index: Int = 0
    private var _temperature: Int = 0
    private var _icon_id: Int = 0
    private var _conditionIcon: Drawable? = null
    private var _description: String? = null
    private var _windDegree: Int = -1
    private var _windSpeed: Float = 0f
    private var _windGust: Float? = null
    private var _daytime: WeatherIconDaytime = WeatherIconDaytime.DAY

    private lateinit var indexTextPaint: TextPaint
    private lateinit var temperatureTextPaint: TextPaint
    private lateinit var descriptionTextPaint: TextPaint
    private lateinit var windTextPaint: TextPaint
    private lateinit var windIcon: Drawable
    private lateinit var windDirectionIcon: Drawable
    private var iconRect = Rect()

    var index: Int
        get() = _index
        set(value) {
            _index = value
        }

    var temperature: Int
        get() = _temperature
        set(value) {
            _temperature = value
        }

    var icon: Drawable?
        get() = _conditionIcon
        set(value) {
            _conditionIcon = value
            _conditionIcon?.let {
                it.callback = this
                applyIconTheme()
            }
        }

    private fun applyIconTheme() {
        _conditionIcon?.applyTheme(
            ContextThemeWrapper(
                context, when (_daytime) {
                    WeatherIconDaytime.NIGHT -> R.style.WeatherIcon_Night
                    else -> R.style.WeatherIcon
                }
            ).theme
        )
    }

    var iconId: Int
        get() = _icon_id
        set(value) {
            if (value > 0) {
                _icon_id = value
                icon = AppCompatResources.getDrawable(context, value)
            }
        }

    var description: String?
        get() = _description
        set(value) {
            _description = value
        }

    var windDegree: Int
        get() = _windDegree
        set(value) {
            _windDegree = value
        }

    var windSpeed: Float
        get() = _windSpeed
        set(value) {
            _windSpeed = value
        }

    var windGust: Float?
        get() = _windGust
        set(value) {
            _windGust = value
        }

    var daytime: WeatherIconDaytime
        get() = _daytime
        set(value) {
            _daytime = value
            applyIconTheme()
        }

    val indexText: String
        get() = resources.getString(R.string.weather_index, _index)

    val temperatureText: String
        get() = resources.getString(R.string.weather_temperature, _temperature)

    val windText: String
        get() {
            _windGust?.let {
                return resources.getString(R.string.weather_wind_with_gust, _windSpeed.toInt(), _windGust!!.toInt())
            }
            return resources.getString(R.string.weather_wind, _windSpeed.toInt())
        }


    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val a = context.obtainStyledAttributes(
            attrs, R.styleable.WaypointWeather, DEFAULT_STYLE_ATTR, DEFAULT_STYLE_RES
        )

        index = a.getInteger(R.styleable.WaypointWeather_index, 0)
        temperature = a.getInteger(R.styleable.WaypointWeather_temperature, 0)
        icon = a.getDrawable(R.styleable.WaypointWeather_icon)
        description = a.getString(R.styleable.WaypointWeather_description)
        windDegree = a.getInteger(R.styleable.WaypointWeather_windDegree, -1)
        windSpeed = a.getFloat(R.styleable.WaypointWeather_windSpeed, 0f)
        windGust = a.getFloat(R.styleable.WaypointWeather_windGust, 0f)
        daytime = WeatherIconDaytime.values()[(a.getInteger(R.styleable.WaypointWeather_datetime, WeatherIconDaytime.DAY.ordinal))]
        val defaultColor = resources.getColor(R.color.black, null)
        val textColor = a.getColor(R.styleable.WaypointWeather_textColor, defaultColor)
        val iconColor = a.getColor(R.styleable.WaypointWeather_windIconColor, defaultColor)

        a.recycle()

        indexTextPaint = createTextPaint(Paint.Align.LEFT, textColor, 16f)
        temperatureTextPaint = createTextPaint(Paint.Align.LEFT, textColor, 32f)
        descriptionTextPaint = createTextPaint(Paint.Align.CENTER, textColor, 12f)
        windTextPaint = createTextPaint(Paint.Align.LEFT, textColor, 16f)

        windIcon = AppCompatResources.getDrawable(context, R.drawable.ic_wind)!!.apply {
            this.setTint(iconColor)
        }
        windDirectionIcon = AppCompatResources.getDrawable(context, R.drawable.ic_wind_direction)!!.apply {
            this.setTint(iconColor)
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setMeasuredDimension(widthMeasureSpec, widthMeasureSpec)
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val padding = 0.1f * width

        _conditionIcon?.let {
            it.bounds = iconRect.apply {
                set(0, 0, (width * 0.4f).toInt(), (width * 0.4f).toInt())
                offset(padding.toInt(), (width * 0.2f).toInt())
            }
            it.draw(canvas)
        }

        canvas.drawText(
            indexText,
            padding,
            padding - indexTextPaint.fontMetricsInt.top,
            indexTextPaint
        )
        canvas.drawText(
            temperatureText,
            width / 2.0f,
            width * 0.4f - ((temperatureTextPaint.descent() + temperatureTextPaint.ascent()) / 2),
            temperatureTextPaint
        )
        description?.let {
            canvas.drawText(
                it,
                width * 0.5f,
                width * 0.6f,
                descriptionTextPaint
            )
        }
        if (windDegree >= 0) {
            iconRect.apply {
                set(0, 0, (width * 0.1f).toInt(), (width * 0.1f).toInt())
                offset(padding.toInt(), (width * 0.7f).toInt())
            }
            windIcon.let {
                it.bounds = iconRect
                it.draw(canvas)
            }

            canvas.save()
            iconRect.apply {
                offset((width * 0.15f).toInt(), 0)
            }
            canvas.rotate(_windDegree.toFloat(), iconRect.exactCenterX(), iconRect.exactCenterY())
            windDirectionIcon.let {
                it.bounds = iconRect
                it.draw(canvas)
            }
            canvas.restore()

            canvas.drawText(
                windText,
                padding + width * 0.3f,
                width * 0.75f - ((windTextPaint.descent() + windTextPaint.ascent()) / 2),
                windTextPaint
            )
        }
    }

    private fun createTextPaint(align: Paint.Align, color: Int, textSize: Float): TextPaint {
        return TextPaint().apply {
            this.flags = Paint.ANTI_ALIAS_FLAG
            this.textAlign = align
            this.color = color
            this.textSize = dpToPx(textSize)
        }
    }

    private fun dpToPx(dp: Float): Float {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.displayMetrics)
    }
}
