package com.binary.heart.hearttouch.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.binary.heart.hearttouch.R;
import com.binary.heart.hearttouch.account.AccountHelper;
import com.binary.heart.hearttouch.conf.WebUrls;
import com.binary.heart.hearttouch.jsons.SearchRsp;
import com.binary.smartlib.io.SmartGraphic;
import com.binary.smartlib.net.SmartImageLoader;

import java.io.File;
import java.util.HashMap;
import java.util.List;

/**
 * Created by yaoguoju on 16-5-3.
 */
public class SearchAdapter extends BaseAdapter{

    private List<SearchRsp> searchs;
    private Context context;
    private SmartImageLoader imageLoader;
    public SearchAdapter(Context context) {
        this.context =  context;
    }

    public void setList(List<SearchRsp> results) {
        searchs = results;
    }

    @Override
    public int getCount() {
        if(searchs != null) {
            return searchs.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int position) {
        if(searchs != null) {
            return searchs.get(position);
        }else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            convertView = inflater.inflate(R.layout.item_search_result,null);
            holder = new ViewHolder();
            holder.nick = (TextView) convertView.findViewById(R.id.tv_item_nick);
            holder.photo = (ImageView)convertView.findViewById(R.id.imgv_item_head);
            holder.sign = (TextView)convertView.findViewById(R.id.tv_item_sign);
            holder.lock = (ImageView)convertView.findViewById(R.id.imgv_lock);
            convertView.setTag(holder);
        }

        holder = (ViewHolder) convertView.getTag();
        if(searchs != null && position < searchs.size()) {
            SearchRsp search = searchs.get(position);
            if(!TextUtils.isEmpty(search.getNick())) {
                holder.nick.setText(search.getNick());
            }else {
                holder.nick.setText(search.getAccount());
            }
            holder.sign.setText(search.getSign());
            loadPhoto(holder.photo,search.getUserid());
            if(search.getUserid() == AccountHelper.getBindUserId(context)) {
                holder.lock.setVisibility(View.VISIBLE);
            }else {
                holder.lock.setVisibility(View.GONE);
            }
        }


        return convertView;
    }


    private void loadPhoto(final ImageView photo,int userid) {
        if (imageLoader == null) {
            imageLoader = new SmartImageLoader.Builder(context).setMemRate(0.5f).setCacheEnable(true).build();
        }
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("authHeader", AccountHelper.getAuthValue(context));
        imageLoader.fillImage(WebUrls.PHOTO + "?user=" + userid, headers, photo, new SmartImageLoader.ImageLoadCallback() {

            @Override
            public void onLoaderStart() {

            }

            @Override
            public void onLoaderSuccess(Bitmap bm) {
                if(bm == null) {
                    photo.setImageResource(R.drawable.icon_head_default);
                }
            }

            @Override
            public void onLoaderError(int code) {

            }
        });
    }

    public class ViewHolder {
        public ImageView photo;
        public TextView nick;
        public TextView sign;
        public ImageView lock;
    }

    public void recycle() {
        if(imageLoader != null) {
            imageLoader.recycleLoader();
        }
    }
}
