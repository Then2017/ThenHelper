package com.battery;

import android.graphics.Color;
import android.graphics.Paint;

import org.achartengine.chart.PointStyle;
import org.achartengine.chart.TimeChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.Date;

/**
 * Created by admin on 2015/9/2.
 */
public class DrawableUtil {
    /**获取数据
     * @return 返回XYMultipleSeriesDataset
     * @yData  设置y轴的数据
     * @param yData
     */
    public static XYMultipleSeriesDataset setAchartDataset(String title,Date[] xdate,Double[] yData){
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
        final int nr = yData.length;
        TimeSeries series = new TimeSeries(title);
        for (int k = 0; k < nr; k++){
            series.add(new Date(xdate[k].getTime()+TimeChart.DAY),yData[k]);
        }
        dataset.addSeries(series);
        return dataset;
    }

    public static XYMultipleSeriesRenderer  setAchartRenderer(String Ytitle,double yStart,double yEnd) {
        int i = 1;
        XYMultipleSeriesRenderer renderer = new XYMultipleSeriesRenderer();
        XYSeriesRenderer r = new XYSeriesRenderer();
        r.setColor(0xffEE7600);
        r.setLineWidth(5);  //折线粗细
        r.setPointStyle(PointStyle.CIRCLE);//点的类型是圆形
        r.setFillPoints(true);//设置点是否实心
        renderer.addSeriesRenderer(r);
       // renderer.setXTitle("Time");
      //  renderer.setDisplayValues(true);
        renderer.setYTitle(Ytitle);                           //设置Y标题
        renderer.setMargins(new int[]{50, 140, 140, 20});          //设置图表的外边框(上/左/下/右)
        renderer.setApplyBackgroundColor(true);                    //设置是否显示背景色
        renderer.setBackgroundColor(Color.argb(00, 265, 265, 265));//设置背景颜色
        renderer.setAxisTitleTextSize(80);                         //设置轴标题文字的大小
        renderer.setChartTitleTextSize(10);                        //设置整个图表标题文字大小
        renderer.setLabelsTextSize(30);                            //设置刻度显示文字的大小(XY轴都会被设置)
      //  renderer.setLegendTextSize(30);                            //图例文字大小
        renderer.setPointSize(2);                                   //设置点的大小(图上显示的点的大小和图例中点的大小都会被设置)
       // renderer.setXAxisMin(0);                                   //设置X轴的最小值
      //  renderer.setXAxisMax(8);                                   //设置X轴的最大值
        renderer.setYAxisMin(yStart);                              //设置Y轴的最小值
        renderer.setYAxisMax(yEnd);                                //设置Y轴的最大值
        renderer.setAxesColor(0xff00F5FF);                        //设置X轴的颜色
        renderer.setLabelsColor(0xff00F5FF);                      //设置Y轴的颜色
        renderer.setXLabels(20);                                    //设置x轴显示6个点,根据setChartSettings的最大值和最小值自动计算点的间隔
        renderer.setYLabels(20);                             //设置y轴显示6个点,根据setChartSettings的最大值和最小值自动计算点的间隔
        renderer.setXLabelsPadding(40);                             //设置标签的间距
        renderer.setShowGridY(true);                              //是否显示Y网格
        renderer.setShowGridX(true);                               //是否显示X方向的网格
        renderer.setGridColor(Color.GRAY);                         //设置网格的颜色
        renderer.setXLabelsAlign(Paint.Align.CENTER);                     //刻度线与刻度标注之间的相对位置关系
        renderer.setYLabelsAlign(Paint.Align.RIGHT);                     //刻度线与刻度标注之间的相对位置关系
        renderer.setZoomButtonsVisible(true);                     //是否显示放大缩小按钮
        renderer.setPanEnabled(true, true);                      //设置是否允许XY轴方向移动
        renderer.setShowLegend(false);                             //设置是否显示图例
        renderer.setMarginsColor(Color.WHITE);                     // 设置外框颜色穿透背景色
        renderer.setXLabelsAngle(90);                           // 设置X轴标签倾斜角度(clockwise degree)
        renderer.setExternalZoomEnabled(false);                  //设置是否可以缩放
 //       renderer.setZoomInLimitY(0);                          //设置Y轴最大缩放限
 //      renderer.setZoomInLimitX(1);                           //设置X轴最大缩放限
//        renderer.setXLabels(0);                                    // 设置X轴不显示数字（改用我们手动添加的文字标签）
//        for (String date:dates){
//            renderer.addTextLabel(i, date);                        //修改X轴显示坐标
//            i++;
//        }
        return renderer;
    }
}
//XYMultipleSeriesRenderer renderer = DrawableUtil.getBarDemoRenderer(date,yStart,yEnd);
//layout = (LinearLayout) findViewById(R.id.chart);       //绑定控件
//        layout.removeAllViews();                                //这个方法是用来辅助实现刷新功能
//        mChartView = ChartFactory.getTimeChartView(this, DrawableUtil.getBarDemoDataset(yData), renderer, "MM/dd");
//        layout.addView(mChartView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));