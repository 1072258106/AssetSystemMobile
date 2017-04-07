package com.capitalcode.assetsystemmobile;

import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import com.capitalcode.assetsystemmobile.utils.SerializableList;

import android.graphics.Color;
import android.graphics.Paint.Align;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class ChartActivity extends BaseActivity {

	private GraphicalView mChartView;
	private CategorySeries mCategorySeries ;
	private XYMultipleSeriesRenderer mRenderer ;
	private XYMultipleSeriesDataset mDataset;
	
	
	private GraphicalView mChartView2;
	private CategorySeries mCategorySeries2 ;
	private XYMultipleSeriesRenderer mRenderer2 ;
	private XYMultipleSeriesDataset mDataset2;

	@Override
	protected void Init(Bundle paramBundle) {
		
	}

	@Override
	protected void AppInit() {
		
	}

	@Override
	protected void DataInit() {
		
	}

	@Override
	protected void Destroy() {
		
	}
	
	double max(double[] d)
	{
		double max=d[0];
		
		for( int i=0;i<d.length;i++)
		{
			if(d[i]>max)
			{
				max=d[i];
			}
		}
		
		return max;
		
	}
	

	@Override
	protected void ViewInit() {
		setContentView(R.layout.activity_chart);
		
		TextView tv = (TextView)this.findViewById(R.id.tv_title_name);
		tv.setText("图形报表");
		
        Button btn = (Button)this.findViewById(R.id.btn_title_left);
        btn.setVisibility(View.GONE);
        
        btn = (Button)this.findViewById(R.id.btn_title_right);
        btn.setVisibility(View.GONE);
		
		Bundle bundle = getIntent().getExtras();
		if (bundle != null ) 
		{
			String title1 = bundle.getString("title1");
			String title2 = bundle.getString("title2");
			
			String primary = bundle.getString("primary");
			String attach1 = bundle.getString("attach1");
			String attach2 = bundle.getString("attach2");
			
			
			SerializableList listprimary = (SerializableList) bundle
					.get("listprimary");
			SerializableList listattach1 = (SerializableList) bundle
					.get("listattach1");
			SerializableList listattach2 = (SerializableList) bundle
					.get("listattach2");
			
			List<Object> primarys = listprimary.getList();
			List<Object> attach1s = listattach1.getList();
			List<Object> attach2s = listattach2.getList();
			
			
			double[] v1 = new double[attach1s.size()];
			int count=0;
			for(Object number:attach1s)
			{
				String str = (String)number;
				double no = Float.valueOf(str);
				v1[count++]=no;
			}
			double max1=max(v1);
			
			double[] v2 = new double[attach2s.size()];
			count=0;
			for(Object number:attach2s)
			{
				String str = (String)number;
				double no = Float.valueOf(str);
				v2[count++]=no;
			}
			double max2=max(v2);
			
			SimpleSeriesRenderer mSeriesRenderer1 = new SimpleSeriesRenderer();
			mSeriesRenderer1.setColor(Color.RED);
			
			LinearLayout mLinear = (LinearLayout) findViewById(R.id.chart1);
			mLinear.setBackgroundColor(Color.BLACK);
			int seriesLength = v1.length;
			mCategorySeries = new CategorySeries(attach1);
			for (int k = 0; k < seriesLength; k++) {
				mCategorySeries.add(v1[k]);
			}
			mDataset = new XYMultipleSeriesDataset();
			mDataset.addSeries(mCategorySeries.toXYSeries());


			mRenderer = new XYMultipleSeriesRenderer();
			mRenderer.addSeriesRenderer(mSeriesRenderer1);
			
			mRenderer.setAxisTitleTextSize(16);
			mRenderer.setChartTitleTextSize(20);
			mRenderer.setLabelsTextSize(15);
			mRenderer.setLegendTextSize(15);
			mRenderer.setXLabelsAlign(Align.CENTER);
			mRenderer.setYLabelsAlign(Align.CENTER);
			mRenderer.setPanEnabled(true, false);
			mRenderer.setZoomEnabled(true);
			mRenderer.setZoomButtonsVisible(true);// ��ʾ�Ŵ���С���ܰ�ť
			mRenderer.setZoomRate(1.1f);
			mRenderer.setBarSpacing(0.5f);
			
			mChartView = ChartFactory.getBarChartView(context, mDataset, mRenderer,
					Type.DEFAULT);
			mRenderer.setClickEnabled(true);

			//mRenderer.setXLabels(5);
			mRenderer.setChartTitle( title1 );//设置柱图名称
			mRenderer.setXTitle( primary );//设置X轴名称
			mRenderer.setYTitle( attach1 );//设置Y轴名称
			mRenderer.setXAxisMin(0.5);//设置X轴的最小值为0.5
			mRenderer.setXAxisMax((double)primarys.size()+0.5);//设置X轴的最大值为5
			mRenderer.setYAxisMin(0);//设置Y轴的最小值为0
			mRenderer.setYAxisMax(max1+max1/3);//设置Y轴最大值为500
			mRenderer.setDisplayChartValues(true); //设置是否在柱体上方显示值
			mRenderer.setShowGrid(true);//设置是否在图表中显示网格
			mRenderer.setXLabels(0);//设置X轴显示的刻度标签的个数

			
			for(int i=0;i<primarys.size();i++)
			{
				mRenderer.addTextLabel(i+1,(String)primarys.get(i));
			}

			mLinear.addView(mChartView, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			mChartView.repaint();
			
			
			
			SimpleSeriesRenderer mSeriesRenderer = new SimpleSeriesRenderer();
			mSeriesRenderer.setColor(Color.GREEN);
			LinearLayout mLinear2 = (LinearLayout) findViewById(R.id.chart2);
			mLinear2.setBackgroundColor(Color.BLACK);
			
			int seriesLength2 = v2.length;
			mCategorySeries2 = new CategorySeries(attach2);
			for (int k = 0; k < seriesLength2; k++) {
				mCategorySeries2.add(v2[k]);
			}
			mDataset2 = new XYMultipleSeriesDataset();
			mDataset2.addSeries(mCategorySeries2.toXYSeries());


			mRenderer2 = new XYMultipleSeriesRenderer();
			mRenderer2.addSeriesRenderer(mSeriesRenderer);
			
			mRenderer2.setAxisTitleTextSize(16);
			mRenderer2.setChartTitleTextSize(20);
			mRenderer2.setLabelsTextSize(15);
			mRenderer2.setLegendTextSize(15);
			mRenderer2.setXLabelsAlign(Align.CENTER);
			mRenderer2.setYLabelsAlign(Align.CENTER);
			mRenderer2.setPanEnabled(true, false);
			mRenderer2.setZoomEnabled(true);
			mRenderer2.setZoomButtonsVisible(true);// ��ʾ�Ŵ���С���ܰ�ť
			mRenderer2.setZoomRate(1.1f);
			mRenderer2.setBarSpacing(0.5f);
			
			mChartView2 = ChartFactory.getBarChartView(context, mDataset2, mRenderer2,
					Type.DEFAULT);
			mRenderer2.setClickEnabled(true);
			
			//mRenderer.setXLabels(5);
			mRenderer2.setChartTitle( title2 );//设置柱图名称
			mRenderer2.setXTitle( primary );//设置X轴名称
			mRenderer2.setYTitle( attach2 );//设置Y轴名称
			mRenderer2.setXAxisMin(0.5);//设置X轴的最小值为0.5
			mRenderer2.setXAxisMax((double)primarys.size()+0.5);//设置X轴的最大值为5
			mRenderer2.setYAxisMin(0);//设置Y轴的最小值为0
			mRenderer2.setYAxisMax(max2+max2/3);//设置Y轴最大值为500
			mRenderer2.setDisplayChartValues(true); //设置是否在柱体上方显示值
			mRenderer2.setShowGrid(true);//设置是否在图表中显示网格
			mRenderer2.setXLabels(0);//设置X轴显示的刻度标签的个数

			for(int i=0;i<primarys.size();i++)
			{
				mRenderer2.addTextLabel(i+1,(String)primarys.get(i));
			}

			mLinear2.addView(mChartView2, new LayoutParams(
					LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
			
			mChartView2.repaint();
			
			
		}
		

	}

	@Override
	protected void ViewListen() {
		// TODO Auto-generated method stub

	}

}
