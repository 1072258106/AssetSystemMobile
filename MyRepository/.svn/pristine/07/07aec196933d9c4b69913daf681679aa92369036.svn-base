package cn.shouma.bsytest.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import cn.shouma.bsytest.R;
import cn.shouma.bsytest.bean.Order;
import cn.shouma.bsytest.bean.ScanNumber;

public class MyLookAdapter extends BaseAdapter {
	private List<ScanNumber> data;
    // 上下文  
    private Context context;  
    // 用来导入布局  
    private LayoutInflater inflater = null;  
    
    // 构造器  
    public MyLookAdapter(List<ScanNumber> data, Context context) {  
        this.context = context;  
        this.data = data;
        
        inflater = LayoutInflater.from(context);  
         
    }  
  
    @Override  
    public int getCount() {  
        return data.size();  
    }  
  
    @Override  
    public Object getItem(int position) {  
        return data.get(position);  
    }  
  
    @Override  
    public long getItemId(int position) {  
        return position;  
    }  
  
    @Override  
    public View getView(int position, View convertView, ViewGroup parent) {  
    	ViewHolder holder = null;
		if (convertView == null) {
			// 获得ViewHolder对象
			holder = new ViewHolder();
			// 导入布局并赋值给convertview
			convertView = inflater.inflate(R.layout.look_item,
					null);
			holder.tv_pCode = (TextView) convertView
					.findViewById(R.id.tv_barcode_item);
			holder.tv_pGZ = (TextView) convertView
					.findViewById(R.id.tv_sendcodeId_item);
			
			// 为view设置标签
			convertView.setTag(holder);
		} else {
			// 取出holder
			holder = (ViewHolder) convertView.getTag();
		}
		// 设置list中TextView的显示
		holder.tv_pCode.setText("" + data.get(position).getBarcode());
		holder.tv_pGZ.setText("" + data.get(position).getSendorderId());
		
		return convertView;
	}

	class ViewHolder {
		TextView tv_pCode;
		TextView tv_pGZ;

	}
}
