package com.example.scanlocalfolder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.example.scanlocalfolder.ScanLocalFolderTools.IProgressListener;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
	
	private Handler mHandler = new Handler();
	
	private Button scanFile = null;
	private TextView scanResultTv = null;
	private final static String RESULT_STRING = "扫描结果: %d本";
	private final static String SCANPROGRESS_STRING = "正在扫描 %d//%d";
	 private Set<String> mAlreadyMarksSet = new HashSet<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.bookshelf_scan);
		scanFile = (Button)findViewById(R.id.scanfile);
		scanResultTv = (TextView)findViewById(R.id.scanresult_tv);
		listView = (ListView)findViewById(R.id.scan_listview);
		init();
		listAdapter = new ShelfScanAdapter(this, listdata, mAlreadyMarksSet);
		listView.setAdapter(listAdapter);
//		refresh();
		
		scanFile.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				searchType = 1;
				refresh();
			}
		});
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
                            setScanResult(listdata.size());
//                            changeTitleMode(searchType);
                            try {
//                                dismissDialog();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    int version = ScanLocalFolderTools.addNewVersion();

                    synchronized (listdataTemp) {
                        ScanLocalFolderTools.getAdapterList(rootpath, currentpath, needFileSuffix,
                                searchType, listdataTemp, BookShelfFileManagerActivity.this,mListener);
                    }
                    if (version == ScanLocalFolderTools.getNewVersion()) {
                        BookShelfFileManagerActivity.this.runOnUiThread(r1);
                    }
                
				}
			}.start();
		}
		
	}
	
	IProgressListener mListener = new IProgressListener() {
		@Override
		public void setProgress(final int size, final int total) {
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					setScanProgress(size, total);
				}
			});
		}

		@Override
		public void setFinish(final int result) {
			// TODO Auto-generated method stub
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					setScanResult(result);
				}
			});
		}
	};
	
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
		listAdapter.notifyDataSetChanged();
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
