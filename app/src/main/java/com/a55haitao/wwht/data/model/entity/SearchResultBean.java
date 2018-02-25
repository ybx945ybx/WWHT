package com.a55haitao.wwht.data.model.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by 55haitao on 2016/11/2.
 */

public class SearchResultBean {
    public int    count;
    public int    page;
    public double allpage;

    public ArrayList<GroupBean> group;

    public ArrayList<ProductBaseBean> products;

    public ExtendBean extend;

    public static class GroupBean { //   分类  品牌  商家   价格
        public String property;
        public int    document_count;

        public ArrayList<ParentLabelBean> labels;           // 一级标题集合

        public boolean isSelect;

        public boolean isChecked;

    }

    public static class ParentLabelBean {
        public int    document_count;
        public String label;                                //  一级标题名字

        public String parentLabel;                          //  数据处理后 如果该分类是有三级分类取出来的拿了二三级分类，记录一级分类的label

        public boolean isChecked;                          //  是否有子内容被选中
        public boolean isExpanded;

        public boolean               noSecond;                            //  该item是没有二级标签，手动补全的
        public boolean               all;                                 //
        public ArrayList<LabelsBean> sub_labels;           //  一级标题下labe，即二级标题集合

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

        public boolean isExpanded() {
            return isExpanded;
        }

        public void setExpanded(boolean expanded) {
            isExpanded = expanded;
        }

    }

    public static class LabelsBean {
        public int    document_count;
        public String label;                            //  二级标题名字

        public boolean isChecked;
        public boolean noThrid;                         //  该item是没有三级标签，手动补全的

        public String parentLabel;
        public int    type;                                //  1是分类2是品牌3是商家4是价格

        public ArrayList<LabelsBean> sub_labels;       //  二级标题labes即三级集合

        public int[] pos;

        public boolean isChecked() {
            return isChecked;
        }

        public void setChecked(boolean checked) {
            isChecked = checked;
        }

    }

    public static class ExtendBean {  //   同义词  相关热搜

        public boolean  useSynonym;                //  是否使用了同义词(未搜到结果但配置了同义词)
        public boolean  useRelateQuery;            //  相关热搜
        public String[] relateQuery;               //  相关热词
//        public String[] useSynWord;               //  同义词
        public boolean  synonymAvalible;           //  同义词是否可用（有结果有同义词）
//        public String   newWord;                   //  同义词（有结果有同义词）
//        public String   newSearchWord;                   //  同义词（有结果有同义词）

    }


    //    public static class GroupBean implements Parcelable {
    //        public String property;
    //        public int document_count;
    //
    //        public ArrayList<ParentLabelBean> labels;
    //
    //        public boolean  isSelect ;
    //
    //        public boolean isChecked ;
    //
    //        @Override
    //        public int describeContents() {
    //            return 0;
    //        }
    //
    //        @Override
    //        public void writeToParcel(Parcel dest, int flags) {
    //            dest.writeString(this.property);
    //            dest.writeInt(this.document_count);
    //            dest.writeTypedList(this.labels);
    //            dest.writeByte(this.isSelect ? (byte) 1 : (byte) 0);
    //            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    //        }
    //
    //        protected GroupBean(Parcel in) {
    //            this.property = in.readString();
    //            this.document_count = in.readInt();
    //            this.labels = in.createTypedArrayList(ParentLabelBean.CREATOR);
    //            this.isSelect = in.readByte() != 0;
    //            this.isChecked = in.readByte() != 0;
    //        }
    //
    //        public static final Creator<GroupBean> CREATOR = new Creator<GroupBean>() {
    //            @Override
    //            public GroupBean createFromParcel(Parcel source) {
    //                return new GroupBean(source);
    //            }
    //
    //            @Override
    //            public GroupBean[] newArray(int size) {
    //                return new GroupBean[size];
    //            }
    //        };
    //    }
    //
    //    public static class ParentLabelBean implements Parcelable {
    //        public int document_count;
    //        public String label;
    //
    //        public boolean isChecked ;
    //        public boolean isExpanded ;
    //
    //        public ArrayList<LabelsBean> sub_labels ;
    //
    //        public boolean isChecked() {
    //            return isChecked;
    //        }
    //
    //        public void setChecked(boolean checked) {
    //            isChecked = checked;
    //        }
    //
    //        public boolean isExpanded() {
    //            return isExpanded;
    //        }
    //
    //        public void setExpanded(boolean expanded) {
    //            isExpanded = expanded;
    //        }
    //
    //        @Override
    //        public int describeContents() {
    //            return 0;
    //        }
    //
    //        @Override
    //        public void writeToParcel(Parcel dest, int flags) {
    //            dest.writeInt(this.document_count);
    //            dest.writeString(this.label);
    //            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    //            dest.writeByte(this.isExpanded ? (byte) 1 : (byte) 0);
    //            dest.writeTypedList(this.sub_labels);
    //        }
    //
    //        protected ParentLabelBean(Parcel in) {
    //            this.document_count = in.readInt();
    //            this.label = in.readString();
    //            this.isChecked = in.readByte() != 0;
    //            this.isExpanded = in.readByte() != 0;
    //            this.sub_labels = in.createTypedArrayList(LabelsBean.CREATOR);
    //        }
    //
    //        public static final Creator<ParentLabelBean> CREATOR = new Creator<ParentLabelBean>() {
    //            @Override
    //            public ParentLabelBean createFromParcel(Parcel source) {
    //                return new ParentLabelBean(source);
    //            }
    //
    //            @Override
    //            public ParentLabelBean[] newArray(int size) {
    //                return new ParentLabelBean[size];
    //            }
    //        };
    //    }
    //
    //    public static class LabelsBean implements Parcelable {
    //        public int document_count;
    //        public String label;
    //
    //        public boolean isChecked ;
    //
    //        public ArrayList<LabelsBean> sub_labels ;
    //
    //        public boolean isChecked() {
    //            return isChecked;
    //        }
    //
    //        public void setChecked(boolean checked) {
    //            isChecked = checked;
    //        }
    //
    //        @Override
    //        public int describeContents() {
    //            return 0;
    //        }
    //
    //        @Override
    //        public void writeToParcel(Parcel dest, int flags) {
    //            dest.writeInt(this.document_count);
    //            dest.writeString(this.label);
    //            dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
    //        }
    //
    //        protected LabelsBean(Parcel in) {
    //            this.document_count = in.readInt();
    //            this.label = in.readString();
    //            this.isChecked = in.readByte() != 0;
    //        }
    //
    //        public static final Creator<LabelsBean> CREATOR = new Creator<LabelsBean>() {
    //            @Override
    //            public LabelsBean createFromParcel(Parcel source) {
    //                return new LabelsBean(source);
    //            }
    //
    //            @Override
    //            public LabelsBean[] newArray(int size) {
    //                return new LabelsBean[size];
    //            }
    //        };
    //    }

}
