package com.example.scanlocalfolder;

import android.text.TextUtils;

import com.j256.ormlite.field.DatabaseField;

public class BookMarkInfo {


    public static final int LOCAL_IMAGE_BLUE = R.drawable.book_shelf_local_random_blue;
    public static final int LOCAL_IMAGE_GRENN = R.drawable.book_shelf_local_random_green;
    public static final int LOCAL_IMAGE_BROWN = R.drawable.book_shelf_local_random_brown;
    public static final int LOCAL_IMAGE_BROWN_RED = R.drawable.book_shelf_local_random_brownish_red;

    /** 在线书签(老书签) （书旗） */
    public static final int TYPE_OLD_ONLINE_BOOKMARK = 1;

    /** 书包书签 */
    public static final int TYPE_OLD_BAG_BOOKMARK = 3;

    /** 本地书签 */
    public static final int TYPE_LOCAL_BOOKMARK = 4;

    /** web书签 */
    public static final int TYPE_OLD_WEB_BOOKMARK = 7;

    /** 神马书签APP */
    public static final int TYPE_NEW_SHENMA_BOOKMARK = 8;

    /** 收费书签（书旗） */
    public static final int TYPE_NEW_PAY_BOOKMARK = 9;

    /** 神马网页书签 */
    public static final int TYPE_NEW_SHENMA_WEB_BOOKMARK = 10;

    /** 书签被删除标志 */
    public static final int FLAG_DELETE = 2;

    /** 书签未删除标志 */
    public static final int FLAG_RETAIN = 1;

    /** 书签需要更新的标志 */
    public static final int UPDATE_FLAG = 1;

    /** 书签不需要更新的标志 */
    public static final int UPDATE_FLAG_NOT = 0;

    // /** 书签等待下载的标志 */
    // public static final int DOWNLOAD_FLAG_DOWNREADY = 99;
    //
    // /** 书签正在下载的标志 */
    // public static final int DOWNLOAD_FLAG_DOWNLOADING = 100;
    //
    // /** 书签已购买,未下载的标志 */
    // public static final int DOWNLOAD_FLAG_NOT_DOWN = 101;
    //
    // /** 书签下载完成的标志 */
    // public static final int DOWNLOAD_FLAG_FINISH = 102;
    //
    // /** 书签下载失败的标志 */
    // public static final int DOWNLOAD_FLAG_ERROR = 103;

    /** 数据库自增主键 */
    @DatabaseField(generatedId = true)
    private int _id;

    /** 书签id */
    @DatabaseField(columnName = "book_id")
    private String bookId;

    /** 书签名称 */
    @DatabaseField(columnName = "book_name")
    private String bookName;

    /** 书签阅读的百分比 */
    @DatabaseField(columnName = "percent")
    private float percent;

    /** 书签来源id */
    @DatabaseField(columnName = "source_id")
    private String sourceId;

    /** 书签章节id */
    @DatabaseField(columnName = "chapter_id")
    private String chapterId;

    /** 书签章节名称 */
    @DatabaseField(columnName = "chapter_name")
    private String chapterName;

    /** 书签类型 1 表示在线书签(老书签) 3 表示书包书签 4 表示本地书签 7 表示web书签 8 神马书签APP 9 收费书签 10 神马网页书签 */
    @DatabaseField(columnName = "book_type")
    private int bookType;

    /** 书签是否删除的标志位 2 表示书签被删除 1表示书签未被删除 */
    @DatabaseField(columnName = "delete_flag")
    private int deleteFlag = FLAG_RETAIN;

    /** 书签的封面url */
    @DatabaseField(columnName = "bookcover_img_url")
    private String bookCoverImgUrl;

    /** 书签的更新时间 13位 */
    @DatabaseField(columnName = "update_time")
    private long updateTime;

    /** 本地书签对应的文件路径, 书包对应的文件 .sqb 或者神马网页书签的url */
    @DatabaseField(columnName = "file_path")
    private String filePath;

    /** 书签正在读的章节的已读字节 */
    @DatabaseField(columnName = "book_read_byte")
    private int bookReadByte;

    /** 书签总共的字节 */
    @DatabaseField(columnName = "book_total_byte")
    private int bookTotalByte;

    /** 书签总共的章节 书籍检查更新使用 */
    @DatabaseField(columnName = "total_chapter")
    private int totalChapter;

    /** 书签所属的账号id */
    @DatabaseField(columnName = "user_id")
    private String userId;

    /** 书签是否更新的标志位 1 表示书签需要显示有更新 0 表示书签不需要显示更新 */
    @DatabaseField(columnName = "update_flag")
    private int updateFlag;

    /** 书签下载的标志位 */
    @DatabaseField(columnName = "download_flag")
    private int downloadFlag;

    /** 书签付费的标志位 */
    @DatabaseField(columnName = "pay_mode")
    private String payMode;

    /** 书签已下载的章节 */
    @DatabaseField(columnName = "download_count")
    private int downCount;

    public String getBookId() {
        return bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public float getPercent() {
        return percent;
    }

    public String getSourceId() {
        return sourceId;
    }

    public String getChapterId() {
        return chapterId;
    }

    public String getChapterName() {
        return chapterName;
    }

    public int getBookType() {
        return bookType;
    }

    public int getDeleteFlag() {
        return deleteFlag;
    }

    public String getBookCoverImgUrl() {
        return bookCoverImgUrl;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public String getFilePath() {
        return filePath;
    }

    public int getBookReadByte() {
        return bookReadByte;
    }

    public int getBookTotalByte() {
        return bookTotalByte;
    }

    public int getTotalChapter() {
        return totalChapter;
    }

    public String getUserId() {
        return userId;
    }

    public int getUpdateFlag() {
        return updateFlag;
    }

    public int getDownloadFlag() {
        return downloadFlag;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public void setPercent(float percent) {
        this.percent = percent;
    }

    public void setPercent(String percent) {
        if (!TextUtils.isEmpty(percent)) {
            try {
                this.percent = Float.valueOf(percent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public void setChapterId(String chapterId) {
        this.chapterId = chapterId;
    }

    public void setChapterName(String chapterName) {
        this.chapterName = chapterName;
    }

    public void setBookType(int bookType) {
        this.bookType = bookType;
    }

    public void setDeleteFlag(int deleteFlag) {
        this.deleteFlag = deleteFlag;
    }

    public void setBookCoverImgUrl(String bookCoverImgUrl) {
        this.bookCoverImgUrl = bookCoverImgUrl;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public void setBookReadByte(int bookReadByte) {
        this.bookReadByte = bookReadByte;
    }

    public void setBookTotalByte(int bookTotalByte) {
        this.bookTotalByte = bookTotalByte;
    }

    public void setTotalChapter(int totalChapter) {
        this.totalChapter = totalChapter;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setUpdateFlag(int updateFlag) {
        this.updateFlag = updateFlag;
    }

    public void setDownloadFlag(int downloadFlag) {
        this.downloadFlag = downloadFlag;
    }

    public int getDownCount() {
        return downCount;
    }

    public void setDownCount(int downCount) {
        this.downCount = downCount;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    /** 获取由 type_sourceId_bookId的主键 */
    public String getUniqueKey() {
        if (TYPE_LOCAL_BOOKMARK == bookType) {
            return bookType + "_" + filePath;
        } else {
            String sId = TextUtils.isEmpty(sourceId) ? "null" : sourceId;
            String bId = TextUtils.isEmpty(bookId) ? "null" : bookId;
            return bookType + "_" + sId + "_" + bId;
        }
    }

    /** 获取由 type_bookId的主键 */
    public String getSynKey() {
        String bId = TextUtils.isEmpty(bookId) ? "null" : bookId;
        if (bookType == TYPE_NEW_SHENMA_BOOKMARK || bookType == TYPE_NEW_SHENMA_WEB_BOOKMARK) {
            // 为了排除神马书签中：阅读器和wap中油重复书籍，这样做可以让这两种书签相互覆盖
            return TYPE_NEW_SHENMA_BOOKMARK + "_" + bId;
        }
        return bookType + "_" + bId;
    }

    // 书架页面下载数据回调使用的id
    public static String getDownBookMarkId(String bid) {
        return TYPE_NEW_PAY_BOOKMARK + "_" + "null" + "_" + bid;
    }

    public void setLocalImageUrl() {
        if (TYPE_LOCAL_BOOKMARK == bookType) {
            if (TextUtils.isEmpty(bookCoverImgUrl)) {
                int i = (int) (Math.random() * 100);
                if (i % 4 == 0) {
                    bookCoverImgUrl = String.valueOf(LOCAL_IMAGE_BLUE);
                } else if (i % 4 == 1) {
                    bookCoverImgUrl = String.valueOf(LOCAL_IMAGE_BROWN);
                } else if (i % 4 == 2) {
                    bookCoverImgUrl = String.valueOf(LOCAL_IMAGE_BROWN_RED);
                } else if (i % 4 == 3) {
                    bookCoverImgUrl = String.valueOf(LOCAL_IMAGE_GRENN);
                }
            }
        }
    }

//    // 判断书签是否需要下载
//    public boolean isNeedDown(int state) {
//        return state == DownloadStatus.STATE_RUN || state == DownloadStatus.STATE_STOP
//                || state == DownloadStatus.STATE_WAIT || state == DownloadStatus.STATE_UNZIP_STOP
//                || state == DownloadStatus.STATE_ERR_CONN || state == DownloadStatus.STATE_ERR_DB;
//    }
//
//    /**
//     * 判断是否需要弹出2G网络提示
//     * 
//     * @return
//     */
//    public int isNeedDownChangeNet(int state) {
//        if (state == DownloadStatus.STATE_STOP) {
//            return DownloadStatus.STATE_STOP;
//        } else {
//            return DownloadStatus.STATE_RUN;
//        }
//    }

    @Override
    public String toString() {
        return "BookMarkInfo [_id=" + _id + ", bookId=" + bookId + ", bookName=" + bookName
                + ", percent=" + percent + ", sourceId=" + sourceId + ", chapterId=" + chapterId
                + ", chapterName=" + chapterName + ", bookType=" + bookType + ", deleteFlag="
                + deleteFlag + ", bookCoverImgUrl=" + bookCoverImgUrl + ", updateTime="
                + updateTime + ", filePath=" + filePath + ", bookReadByte=" + bookReadByte
                + ", bookTotalByte=" + bookTotalByte + ", totalChapter=" + totalChapter
                + ", userId=" + userId + ", updateFlag=" + updateFlag + ", downloadFlag="
                + downloadFlag + ", payMode=" + payMode + ", downCount=" + downCount + "]";
    }

    @Override
    public boolean equals(Object o) {
        BookMarkInfo other = (BookMarkInfo) o;
        return (filePath != null && filePath.equals(other.filePath))
                || (bookId != null && bookId.equals(other.bookId));
    }


}
