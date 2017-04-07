package cn.shouma.bsytest.adapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.shouma.bsytest.R;
import cn.shouma.bsytest.R.id;
import cn.shouma.bsytest.R.layout;
import cn.shouma.bsytest.bean.Order;
import cn.shouma.bsytest.bean.Product;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class GoodsCollectAdapter extends BaseAdapter {
	private List<Order> data;
    // 上下文  
    private Context context;  
    // 用来导入布局  
    private LayoutInflater inflater = null;    
    // 构造器  
    public GoodsCollectAdapter(List<Order> data, Context context) {  
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
            convertView = inflater.inflate(R.layout.goodscollect_activity_item, null);  
            holder.tv_pCode = (TextView) convertView.findViewById(R.id.tv_collect_pName);  
            holder.tv_pGZ = (TextView) convertView.findViewById(R.id.tv_collect_pCode);
            holder.tv_pName = (TextView) convertView.findViewById(R.id.tv_collect_pGZ); 
            // 为view设置标签  
            convertView.setTag(holder);  
        } else {  
            // 取出holder  
            holder = (ViewHolder) convertView.getTag();  
        }  
        // 设置list中TextView的显示  
        //TODO
        holder.tv_pCode.setText(""+data.get(position).getSendcode());
        holder.tv_pGZ.setText(""+data.get(position).getCustomer());
        holder.tv_pName.setText(""+data.get(position).getpCode());
        return convertView;  
    }  
  
    class ViewHolder {  
        TextView tv_pCode; 
        TextView tv_pGZ;
        TextView tv_pName;
          
    }
}
