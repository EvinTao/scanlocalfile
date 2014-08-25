package com.example.scanlocalfolder;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

public class ScanLocalFolderTools {


    private static final String TAG = "zyc_ScanLocalFolderTools";

    public static final String TOP = "0";
    public static final String FOLDER = "1";
    public static final String FILE = "2";

    private static int version = 0;

    private static int newVersion = 0;

    public synchronized static int addNewVersion() {
        return ++ScanLocalFolderTools.newVersion;
    }

    public synchronized static int getNewVersion() {
        return ScanLocalFolderTools.newVersion;
    }

    /**
     * 判定SD卡是否可用
     * 
     * @return
     */
    public static boolean isCanUseSdCard() {
        try {
            return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * 获取listadapter需要的数据
     * 
     * @param path
     * @param key
     * @param type
     * @param paths 将返回adapter数据对应的路径放入
     * @return
     */
    public static void getAdapterList(String rootpath, String path, String key, int type,
            List<Map<String, Object>> listdata, Context context,IProgressListener mListener) {

        if (path == null || rootpath == null || listdata == null) {
            Log.i(TAG, "path rootpath paths listdata have null ");
            return;
        }
        ScanLocalFolderTools.version = ScanLocalFolderTools.newVersion;

        listdata.clear();

        if (type == 0) {
            nomalFilesDatas(rootpath, path, key, listdata);
            // 加载本地书签

        } else if (type <= 7) {// type == 1 || type == 2 || type == 3 || type ==
            // 4 || type == 5 || type == 6||type == 7

            // Map<String, Object> item = null;
            // item = new HashMap<String, Object>();
            // item.put("img", R.drawable.icon_list_return);
            // item.put("title", "返回根目录");
            // item.put("path", rootpath);
            // item.put("type", TOP);
            // listdata.add(item);

            if (type <= 6) {
                // 加载本地书签
                addNeedFilesToList(rootpath, key, type, listdata,mListener);
            } else if (type == 7) {
                // 加载本地书签
                if(path.equals("/")){
                    addNeedFilesToList(rootpath, key, type, listdata,mListener);
                }else{
                    addNeedFilesToList(path, key, type, listdata,mListener);
                }
                Log.e("liyizhe", "rootpath:"+rootpath+",path"+path);
            }
        }

        if (newVersion != version || listdata.size() == 0) {
            return;
        }

        // 首个选项top类型则不需要排序

        // Map<String, Object> top = listdata.get(0);
        // if (TOP.equals(top.get("type").toString())) {
        // listdata.remove(0);
        // } else {
        // top = null;
        // }

        if (newVersion != version) {
            return;
        }

        // 排序
        if (listdata.size() > 0) {

            Collections.sort(listdata, new Comparator<Map<String, Object>>() {
                @Override
                public int compare(Map<String, Object> object1, Map<String, Object> object2) {

                    if (FOLDER.equals(object1.get("type").toString())
                            && FILE.equals(object2.get("type").toString())) {
                        return -100;
                    } else if (FILE.equals(object1.get("type").toString())
                            && FOLDER.equals(object2.get("type").toString())) {
                        return 100;
                    } else {
                        return object1.get("title").toString()
                                .compareToIgnoreCase(object2.get("title").toString());
                    }
                }
            });
        }
        if (newVersion != version) {
            return;
        }
        // if (top != null) {
        // if (type != 0) {
        // top.put("title", "扫描出" + listdata.size() + "个文件");
        // }
        // listdata.add(0, top);
        // }
        connectBookMarks(rootpath, path, type, listdata, context);
        mListener.setFinish(listdata.size());
    }

    /** 关联书签到文件 */
    public static void connectBookMarks(String rootpath, String path, int type,
            List<Map<String, Object>> listdata, Context context) {
        List<BookMarkInfo> localBookMarks = null;// 本地书签
        // TODO
        // if (type == 0) {
        // localBookMarks = BookMarkHelper.getBookMarkList(context,"4");
        // } else if (type <= 6) {
        // localBookMarks = BookMarkHelper.getLocalBookMarks(context,
        // rootpath, true);
        // } else if (type == 7) {
        // localBookMarks = BookMarkHelper.getLocalBookMarks(context, path,
        // true);
        // } else {
        // return;
        // }

        if (localBookMarks != null) {
            for (int j = localBookMarks.size() - 1; j >= 0; j--) {
                if (newVersion != version) {
                    return;
                }
                if (localBookMarks.get(j).getBookName() == null) {
                    continue;
                }
                for (int i = listdata.size() - 1; i >= 0; i--) {
                    if (localBookMarks.get(j).getBookName().equals(listdata.get(i).get("path"))) {
                        listdata.get(i).put("bookmark", localBookMarks.get(j));
                        break;
                    } else if (FOLDER.equals(listdata.get(i).get("type").toString())) {// 如果是文件夹则跳出
                        break;
                    }
                }
            }
        }
    }

    /** 获取文件的大小和类型信息放入map */
    private static void setFileInfo(Map<String, Object> item) {
        if (item == null || item.get("path") == null) {
            return;
        }
        File file = new File((String) item.get("path"));
        if ((!file.exists()) || file.isDirectory()) {// 不存在或者是文件夹则退出
            return;
        }
        String size = "" + (file.length() * 1f / 1024);
        int index = size.indexOf(".");

        if (index >= 0 && (index + 3) < size.length()) {// 需要舍去小数点两位以后的数字
            size = size.substring(0, index + 3);
            if (size.endsWith("00")) {
                size = size.substring(0, size.length() - 3);
            } else if (size.endsWith("0")) {
                size = size.substring(0, size.length() - 1);
            }
        }
        item.put("size", size + "K");
        String path = ((String) item.get("path")).toLowerCase();
        if (path.endsWith(".txt")) {
//            item.put("fileicon", R.drawable.icon_txtfile);
        	item.put("fileicon", R.drawable.icon_txtfile);
        } else if (path.endsWith(".umd")) {
            item.put("fileicon", R.drawable.icon_umdfile);
        } else if (path.endsWith(".epub")) {
            item.put("fileicon", R.drawable.icon_epubfile);
        } else if (path.endsWith(".sqb.zip")) {
            item.put("fileicon", R.drawable.icon_sqbfile);
        } else if (path.endsWith(".sqd.zip")) {
            item.put("fileicon", R.drawable.icon_sqdfile);
        }
    }

    /**
     * 正常浏览，生成的包括该目录下所有文件和文件夹 当type==0时，生成listdata和paths
     * 
     * @param path
     * @param key
     * @param type
     * @param paths
     * @param listdata
     */
    private static void nomalFilesDatas(String rootpath, String path, String key,
            List<Map<String, Object>> listdata) {
        File file = new File(path);
        Map<String, Object> item = null;
        // 加入返回按钮
        // if (file.isDirectory() && file.getParent() != null) {
        // item = new HashMap<String, Object>();
        // item.put("img", R.drawable.icon_list_return);
        // item.put("title", "返回上级目录");
        // item.put("path", file.getParent());
        // item.put("type", TOP);
        //
        // listdata.add(item);
        // }

        // 加入文件和文件夹
        File[] allFile = file.listFiles();// 获取当前文件夹下的所有文件

        if (allFile == null) {
            return;
        }

        for (int i = 0; i < allFile.length; i++) {
            File f = allFile[i];

            if (f.isFile()) {// 如果是文件
                if (isNeedFile(f, null, 0)) {// 如果是支持的文件

                    item = new HashMap<String, Object>();
                    item.put("title", f.getName());
                    item.put("path", f.getPath());
                    item.put("type", FILE);
                    setFileInfo(item);
                    listdata.add(item);
                }
            } else if (f.isDirectory()) {// 如果是文件夹
                // if (isContainNeedFiles(f.getPath())) {// 如果这个文件夹包含支持的文件
                item = new HashMap<String, Object>();
                item.put("img", R.drawable.icon_folder);
                item.put("title", f.getName());
                item.put("path", f.getPath());
                item.put("type", FOLDER);

                listdata.add(item);
                // }
            }
        }
    }

    /**
     * 当type = 1，2，3，4时，生成listdata和paths
     * 
     * @param path 路径
     * @param key 关键字，type=4使用
     * @param type 搜索类型 1，txt;2,umd;3,epub;4,模糊key搜索；5搜索.sqb.zip；6搜索.sqd.zip;
     * @param paths 对应路径；
     * @param listdata 对应数据；
     */
    private static void addNeedFilesToList(String path, String key, int type,
            List<Map<String, Object>> listdata,IProgressListener mListener) {

        if (path == null || "".equals(path)) {
            Log.e(TAG, "path is null or ''");
            return;
        }

        File file = new File(path);
        File[] allFile = file.listFiles();// 获取当前文件夹下的所有文件

        if (allFile == null) {
            Log.e(TAG, path + " listfiles is null");
            return;
        }

        Map<String, Object> item = null;

        for (int i = 0; i < allFile.length; i++) {

            if (newVersion != version) {
                return;
            }

            File f = allFile[i];
            if (f.isFile()) {// 如果是文件
                if (isNeedFile(f, key, type)) {// 如果是支持的文件

                    item = new HashMap<String, Object>();
                    item.put("title", f.getName());
                    item.put("path", f.getPath());
                    item.put("type", FILE);
                    setFileInfo(item);
                    listdata.add(item);
                    if(mListener != null){
                    	mListener.setProgress(listdata.size(), listdata.size());
                    }
                }
            } else if (f.isDirectory()) {// 如果是文件夹
                addNeedFilesToList(f.getPath(), key, type, listdata,mListener);
            }
        }
    }

    /**
     * 判定该文件是否是需要的文件
     * 
     * @param path 文件的绝对路径
     * @param type 需要的类型 type = 0,txt umd epub;type = 1 txt,size>10k;type = 2 umd,size>10k;type = 3
     *        ,size>10k;type = 4 关键字，后缀同时匹配; type = 5 sqb.zip;6 sqd.zip
     * @param key 搜索关键字
     * @return
     */
    private static boolean isNeedFile(File file, String key, int type) {
        if (file == null || !file.exists() || !file.isFile()) {
            return false;
        }
        String name = file.getName().toLowerCase();

        final int SIZE =80;
        boolean re = false;

        switch (type) {
            case 1:// 文件类型搜索
                if (name.endsWith(".txt") && file.length() > SIZE) {
                    re = true;
                }
                break;
            case 2:// 文件类型搜索
                if (name.endsWith(".umd") && file.length() > SIZE) {
                    re = true;
                }
                break;
            case 3:// 文件类型搜索
                if (name.endsWith(".epub") && file.length() > SIZE) {
                    re = true;
                }
                break;
            case 5:// 文件类型搜索
                if (name.endsWith(".sqb.zip") && file.length() > SIZE) {
                    re = true;
                }
                break;
            case 6:// 文件类型搜索
                if (name.endsWith(".sqd.zip") && file.length() > SIZE) {
                    re = true;
                }
                break;
            case 0:// 正常点击文件夹，支持类型全匹配，用于主列表显示
            case 7:// 当前文件夹搜索,搜索出该文件夹下所有支持文件
                if ((name.endsWith(".txt") || name.endsWith(".umd") || name.endsWith(".epub")
                        || name.endsWith(".sqb.zip") || name.endsWith(".sqd.zip"))
                        && file.length() > SIZE) {
                    re = true;
                }
                break;
            case 4:// 模糊搜索
                if ((name.endsWith(".txt") || name.endsWith(".umd") || name.endsWith(".epub")
                        || name.endsWith(".sqb.zip") || name.endsWith(".sqd.zip"))
                        && (name.contains(key.toLowerCase()))) {
                    re = true;
                }
                break;
        }

        return re;
    }
    
    public interface IProgressListener{
    	public void setProgress(final int size,final int total);
    	public void setFinish(final int result);
    }


}
