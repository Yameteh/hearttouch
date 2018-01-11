package com.binary.heart.hearttouch.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.im.notify.HeartNotify;
import com.binary.heart.hearttouch.im.notify.NotifyManager;

import java.util.List;

/**
 * Created by yaoguoju on 16-5-6.
 */
public class NotifyAdapter extends BaseAdapter{
    private List<Object> notifys;
    private Context context;

    public NotifyAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        if(notifys == null) {
            return 0;
        }
        return notifys.size();
    }

    public void setList(List<Object> notifys) {
        this.notifys = notifys;
    }

    @Override
    public Object getItem(int position) {
        return notifys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
//        if(convertView == null) {
//            convertView = View.inflate(context, R.layout.item_notify,null);
//            holder = new ViewHolder();
//            holder.icon = (ImageView) convertView.findViewById(R.id.avatar_iv);
//            holder.nick = (TextView) convertView.findViewById(R.id.nickname_tv);
//            //holder.content = (CCPTextView)convertView.findViewById(R.id.last_msg_tv);
//            holder.time = (TextView)convertView.findViewById(R.id.update_time_tv);
//            convertView.setTag(holder);
//        }
//        holder = (ViewHolder) convertView.getTag();
//
//        HeartNotify notify = (HeartNotify)notifys.get(position);
//        if(notify.getType() == NotifyManager.BINDREQ) {
//            holder.icon.setImageResource(R.drawable.ic_bind_req);
//        }
//
//        holder.nick.setText(notify.getSender());
//        holder.content.setText(notify.getContent());
//        String time = DateUtil.getDateString(Long.valueOf(notify.getTime()),
//                DateUtil.SHOW_TYPE_CALL_LOG).trim();
//        holder.time.setText(time);
        return convertView;
    }

    public class ViewHolder {
        public ImageView icon;
        public TextView nick;
        //public CCPTextView content;
        public TextView time;
    }

}
