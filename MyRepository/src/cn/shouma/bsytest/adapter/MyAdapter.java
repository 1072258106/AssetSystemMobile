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

public class MyAdapter extends BaseAdapter {
	private List<Product> data;
    // 用来控制CheckBox的选中状况  
    private static HashMap<Integer, Boolean> isSelected;  
    // 上下文  
    private Context context;  
    // 用来导入布局  
    private LayoutInflater inflater = null;    
    // 构造器  
    public MyAdapter(List<Product> data, Context context) {  
        this.context = context;  
        this.data = data;  
        inflater = LayoutInflater.from(context);  
        isSelected = new HashMap<Integer, Boolean>();  
        // 初始化数据  
        initDate();  
    }  
  
    // 初始化isSelected的数据  
    private void initDate() {  
        for (int i = 0; i < data.size(); i++) {  
            getIsSelected().put(i, false);  
        }  
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
            convertView = inflater.inflate(R.layout.goodslist_activity_item, null);  
            holder.tv_pName = (TextView) convertView.findViewById(R.id.tv_goodsNumbers);  
            holder.tv_pCode = (TextView) convertView.findViewById(R.id.tv_clientName);
            holder.tv_pGz = (TextView) convertView.findViewById(R.id.tv_saveName);
            holder.cb = (CheckBox) convertView.findViewById(R.id.checkBox1);  
            // 为view设置标签  
            convertView.setTag(holder);  
        } else {  
            // 取出holder  
            holder = (ViewHolder) convertView.getTag();  
        }  
        // 设置list中TextView的显示  
        holder.tv_pCode.setText(""+data.get(position).getpCode());  
        holder.tv_pGz.setText(""+data.get(position).getpGZ());
        holder.tv_pName.setText(""+data.get(position).getpName());
        // 根据isSelected来设置checkbox的选中状况  
        holder.cb.setChecked(getIsSelected().get(position));  
        return convertView;  
    }  
  
    public static HashMap<Integer, Boolean> getIsSelected() {  
        return isSelected;  
    }  
  
    public static void setIsSelected(HashMap<Integer, Boolean> isSelected) {  
        MyAdapter.isSelected = isSelected;  
    }  
  
    public static class ViewHolder {  
        public TextView tv_pName; 
        public TextView tv_pCode;
        public TextView tv_pGz;
        public CheckBox cb;  
    }
}
