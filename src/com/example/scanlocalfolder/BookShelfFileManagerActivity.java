package com.example.scanlocalfolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class BookShelfFileManagerActivity extends Activity {

	private List<Map<String, Object>> listdata;
	private List<Map<String, Object>> listdataTemp;
	private ListView listView;

	private ShelfScanAdapter listAdapter;
	private String rootpath;// 总路径
	private String currentpath;// 当前路径
	private int searchType; // 0，正常浏览；1搜索txt;2搜索umd;3搜索epub;4模糊搜索；5搜索.sqb.zip；6搜索.sqd.zip;7当前文件夹下搜索
	private String needFileSuffix;// 关键字

	private boolean canusesdcard = true;
	
	private Button scanFile = null;
	private TextView scanResultTv = null;
	private final static String RESULT_STRING = "扫描结果: d%本";
	private final static String SCANPROGRESS_STRING = "正在扫描 d%/d%";
	 private Set<String> mAlreadyMarksSet = new HashSet<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_scan);
		scanFile = (Button)findViewById(R.id.scanfile);
		scanResultTv = (TextView)findViewById(R.id.scanresult_layout);
		listView = (ListView)findViewById(R.id.scan_listview);
		init();
		listAdapter = new ShelfScanAdapter(this, listdata, mAlreadyMarksSet);
		listView.setAdapter(listAdapter);
		refresh();
	}

	/**
	 * 初始化数据
	 */
	public void init(){
		
        if (listdata == null) {
            listdata = new ArrayList<Map<String, Object>>();
        } else {
            listdata.clear();
        }
        if (listdataTemp == null) {
            listdataTemp = new ArrayList<Map<String, Object>>();
        } else {
            listdataTemp.clear();
        }
        if (TextUtils.isEmpty(rootpath)) {
            rootpath = Environment.getExternalStorageDirectory().getPath();
        }
        if (TextUtils.isEmpty(currentpath)) {
            currentpath = rootpath;
        }
		
	}
	
	/**
	 * 通知更新list
	 */
	public void refresh() {
		if (listAdapter == null || rootpath == null || listdata == null) {
            return;
        }
		if(searchType != 0){
			new Thread(){
				public void run(){
                    Runnable r1 = new Runnable() {
                        @Override
                        public void run() {
                            listdata.clear();
                            listdata.addAll(listdataTemp);
                            listdataTemp.clear();
                            if (checkHasFile()) {
                                listAdapter.notifyDataSetChanged();
                                listView.setSelection(0);
                            }
                            changeTitleMode(searchType);
                            try {
                                dismissDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    int version = ScanLocalFolderTools.addNewVersion();

                    synchronized (listdataTemp) {
                        ScanLocalFolderTools.getAdapterList(rootpath, currentpath, needFileSuffix,
                                searchType, listdataTemp, BookShelfFileManagerActivity.this);
                    }
                    if (version == ScanLocalFolderTools.getNewVersion()) {
                        BookShelfFileManagerActivity.this.runOnUiThread(r1);
                    }
                
				}
			}.start();
		}
		
	}
	
	/**
	 * 扫描结果
	 * @param count
	 */
	public void setScanResult(int count) {
		String result = String.format(RESULT_STRING, count);
		scanResultTv.setText(result);
	}

	/**
	 * 扫描进度
	 * @param progress
	 * @param total
	 */
	public void setScanProgress(int progress, int total) {
		String result = String.format(SCANPROGRESS_STRING, progress, total);
		scanResultTv.setText(result);
	}
	
	/**
     * 
     * @return true 则有文件,false没有文件
     */
    private boolean checkHasFile() {
        if (listdata.size() == 0) {
        	setScanResult(0);
            return false;
        } else {
            return true;
        }
    }

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

}
