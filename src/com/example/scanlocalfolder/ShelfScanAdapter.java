package com.example.scanlocalfolder;

import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShelfScanAdapter extends BaseAdapter {

	private LayoutInflater inflater;
    public List<Map<String, Object>> list;
//    private ToastDialog mDialog;
    private Activity mContext;

    private Set<String> mAlreadyMarksSet;

    public ShelfScanAdapter(Activity context, List<Map<String, Object>> list,
            Set<String> bookMarkSet) {
        this.mContext = context;
        this.inflater = LayoutInflater.from(context);
        this.list = list;
        this.mAlreadyMarksSet = bookMarkSet;
    }

    @Override
    public int getCount() {
        return list != null ? list.size() : 0;
    }

    @Override
    public Object getItem(int position) {
        if (list != null && list.get(position) != null) {
            return list.get(position);
        } else {
            return null;
        }
    }

    public void setList(List<Map<String, Object>> list) {
        this.list = list;
    }

    public List<Map<String, Object>> getList() {
        return list;
    }

    /**
     * 设置选中状态
     * 
     * @param position
     * @return true 表示已存书签 false表示未存
     */
    public boolean setChecked(int position) {
        final Map<String, Object> info = list.get(position);
        if (ScanLocalFolderTools.FILE.equals(info.get("type"))) {
            if (info.get("bookmark") instanceof BookMarkInfo) {// 已存在书签
                return true;
            }
            if ("ischecked".equals(info.get("bookmark"))) {
                info.put("bookmark", null);
            } else {
                info.put("bookmark", "ischecked");
            }
        }
        return false;
    }

    /**
     * 得到被标记书签数量
     * 
     * @return
     */
    public int getCheckedBooks() {
        int count = 0;
        for (int i = 0; i < list.size(); i++) {
            if ("ischecked".equals(list.get(i).get("bookmark"))) {
                count++;
            }
        }
        return count;
    }

    // 判断是否以保存
    public boolean isAlreadMarkToBookShelf(int index) {
        if (mAlreadyMarksSet != null && list != null && list.size() > index) {
            return (mAlreadyMarksSet != null && mAlreadyMarksSet.contains(list.get(index)
                    .get("path").toString()));
        }
        return false;
    }

    /**
     * 取消书签
     */
    public void cancelBooks() {
        for (int i = 0; i < list.size(); i++) {
            if ("ischecked".equals(list.get(i).get("bookmark"))) {
                list.get(i).put("bookmark", null);
            }
        }
    }

    /**
     * 导入书签
     */
    public void importBooks() {
    	
    	
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // 获取当前Item
        View view = null;
        if (convertView != null) {
            view = convertView;
        } else {
            view = inflater.inflate(R.layout.item_shelfscan, parent, false);
        }

        // 保存当前Item中控件的引用, 每个View对象里面可以通过setTag方法保存一个对象
        BbsHolder holder = (BbsHolder) view.getTag();
        if (holder == null) {
            holder = new BbsHolder();
            holder.rootView = view.findViewById(R.id.item_scan_root);
            holder.layoutFolder = (LinearLayout) view.findViewById(R.id.item_scan_folder);
            holder.title = (TextView) view.findViewById(R.id.scan_item_title);
            holder.icon = (ImageView) view.findViewById(R.id.scan_item_img);

            holder.layoutFile = (RelativeLayout) view.findViewById(R.id.item_scan_file);
            holder.filename = (TextView) view.findViewById(R.id.scan_item_filename);
            holder.fileinfo = (TextView) view.findViewById(R.id.scan_item_fileinfo);
            holder.check = (TextView) view.findViewById(R.id.scan_item_check);
            holder.fileicon = (ImageView) view.findViewById(R.id.scan_item_fileicon);

            view.setTag(holder);
        }

        final Map<String, Object> taskInfo = list.get(position);

        if ((position == 0 && ScanLocalFolderTools.TOP.equals(taskInfo.get("type"))) || // 显示返回键
                ScanLocalFolderTools.FOLDER.equals(taskInfo.get("type"))) {// 显示文件夹
            holder.layoutFolder.setVisibility(View.VISIBLE);
            holder.layoutFile.setVisibility(View.GONE);
            holder.icon.setBackgroundResource((Integer) taskInfo.get("img"));
            holder.title.setText((String) taskInfo.get("title"));
            holder.rootView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
//                    ((BookShelfFileMangementAcitvity) mContext).onItemClickDispose(position);
                }
            });
        } else if (ScanLocalFolderTools.FILE.equals(taskInfo.get("type"))) {// 显示文件
            holder.layoutFolder.setVisibility(View.GONE);
            holder.layoutFile.setVisibility(View.VISIBLE);

            holder.filename.setText((String) taskInfo.get("title"));
            holder.fileinfo.setText(taskInfo.get("size").toString());
            holder.fileicon.setBackgroundResource((Integer) taskInfo.get("fileicon"));
            if (taskInfo.get("bookmark") instanceof BookMarkInfo
                    || (mAlreadyMarksSet != null && mAlreadyMarksSet.contains(list.get(position)
                            .get("path").toString()))) {// 已存在书签
                holder.check.setText("已导入");
                holder.check.setBackgroundColor(Color.TRANSPARENT);
            } else if ("ischecked".equals(taskInfo.get("bookmark"))) {
                holder.check.setText("");
                holder.check.setBackgroundResource(R.drawable.checkbox_item_c);
            } else {
                holder.check.setText("");
                holder.check.setBackgroundResource(R.drawable.checkbox_item_n);
            }

            final BbsHolder holderf = holder;
            holder.rootView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isAlreadMarkToBookShelf(position)
                            || taskInfo.get("bookmark") instanceof BookMarkInfo) {// 已存在书签
                        // 跳转到书籍页面
//                        ((BookShelfFileMangementAcitvity) mContext).gotoReadActivity(position);
                        return;
                    } else if ("ischecked".equals(taskInfo.get("bookmark"))) {// 等待存书签
                        holderf.check.setText("");
                        holderf.check.setBackgroundResource(R.drawable.checkbox_item_n);
                        taskInfo.put("bookmark", null);
                    } else {

                        holderf.check.setText("");
                        holderf.check.setBackgroundResource(R.drawable.checkbox_item_c);

                        taskInfo.put("bookmark", "ischecked");

                    }
                    int checkedBooks = getCheckedBooks();
//                    ((BookShelfFileMangementAcitvity) mContext).showBottom(checkedBooks, position);
                }
            });
        }

        return view;
    }

    @Override
    public int getViewTypeCount() {
        // TODO Auto-generated method stub
        return super.getViewTypeCount();
    }

    /**
     * 导入书签dialog
     * 
     * @param isShow
     */
    private void showProgressDialog(boolean isShow) {
//        if (isShow) {
//            if (mDialog == null) {
//                mDialog = new ToastDialog(mContext);
//                mDialog.setDissmisOnClickOutSide(false);
//            }
//            mDialog.loading("导入中...");
//        } else {
//            if (mDialog != null) {
//                mDialog.dismiss();
//            }
//        }
    }

    public void dismissLoadingDialog() {
//        if (mDialog != null) {
//            mDialog.dismiss();
//        }
    }

    private static class BbsHolder {
        public View rootView;
        public LinearLayout layoutFolder;
        public ImageView icon;
        public TextView title;

        public RelativeLayout layoutFile;
        public TextView filename;
        public TextView fileinfo;
        public TextView check;
        public ImageView fileicon;
    }
}
