package com.haoyan.myrxjava.entity;

import java.util.List;

/**
 * Created by haoyan on 2017/6/10.
 */

public class entity {

    /**
     * date : 20170610
     * stories : [{"images":["https://pic1.zhimg.com/v2-a97e36937037de4fef12fb97e07a7e74.jpg"],"type":0,"id":9466950,"ga_prefix":"061008","title":"无印良品也开餐厅了，为何第一家选在了中国而不是日本？"},{"images":["https://pic3.zhimg.com/v2-0c0d55d00f9b0e44673df03092c20fda.jpg"],"type":0,"id":9465948,"ga_prefix":"061007","title":"到底为什么有的职业收入低，有的就很高？"},{"images":["https://pic4.zhimg.com/v2-53235c5b9d4a3f5d31c1579f797c6403.jpg"],"type":0,"id":9464861,"ga_prefix":"061007","title":"少年找回爸爸的魔兽账号后，故事还没有结束"},{"images":["https://pic2.zhimg.com/v2-752152534cea6db9b609f3f821e9ad61.jpg"],"type":0,"id":9467676,"ga_prefix":"061007","title":"乐视北美大溃败：国王永久消失了，王朝也分崩离析了"},{"images":["https://pic3.zhimg.com/v2-bf88a5e295dc0184e0f4f219d1f49b12.jpg"],"type":0,"id":9466915,"ga_prefix":"061006","title":"瞎扯 · 如何正确地吐槽"}]
     * top_stories : [{"image":"https://pic4.zhimg.com/v2-09b04174a84ed247ad9ccd901b03f133.jpg","type":0,"id":9467676,"ga_prefix":"061007","title":"乐视北美大溃败：国王永久消失了，王朝也分崩离析了"},{"image":"https://pic1.zhimg.com/v2-6c5e3e25b33176ce55bffc06384ca2b4.jpg","type":0,"id":9464826,"ga_prefix":"060909","title":"今年，苹果设计奖选出了这 12 个好用又好看的 app"},{"image":"https://pic1.zhimg.com/v2-06bd3e79e958746e937b3875ae706d2c.jpg","type":0,"id":9460779,"ga_prefix":"060916","title":"神奇女侠：蝙蝠侠、超人之后，又一个面临信仰崩塌的英雄"},{"image":"https://pic4.zhimg.com/v2-05160fedfe4c111da503dbb1c6fba957.jpg","type":0,"id":9460835,"ga_prefix":"060907","title":"做好职业生涯规划，房贷可能是一个最佳参照物"},{"image":"https://pic3.zhimg.com/v2-912ed77e8effec5588070848cd5a48c2.jpg","type":0,"id":9465654,"ga_prefix":"060907","title":"「裸贷」事件很糟糕，把学生贷款全部禁掉是个好主意吗？"}]
     */

    private String date;
    private List<StoriesBean> stories;
    private List<TopStoriesBean> top_stories;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public List<StoriesBean> getStories() {
        return stories;
    }

    public void setStories(List<StoriesBean> stories) {
        this.stories = stories;
    }

    public List<TopStoriesBean> getTop_stories() {
        return top_stories;
    }

    public void setTop_stories(List<TopStoriesBean> top_stories) {
        this.top_stories = top_stories;
    }

    public static class StoriesBean {
        /**
         * images : ["https://pic1.zhimg.com/v2-a97e36937037de4fef12fb97e07a7e74.jpg"]
         * type : 0
         * id : 9466950
         * ga_prefix : 061008
         * title : 无印良品也开餐厅了，为何第一家选在了中国而不是日本？
         */

        private int type;
        private int id;
        private String ga_prefix;
        private String title;
        private List<String> images;

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public List<String> getImages() {
            return images;
        }

        public void setImages(List<String> images) {
            this.images = images;
        }
    }

    public static class TopStoriesBean {
        /**
         * image : https://pic4.zhimg.com/v2-09b04174a84ed247ad9ccd901b03f133.jpg
         * type : 0
         * id : 9467676
         * ga_prefix : 061007
         * title : 乐视北美大溃败：国王永久消失了，王朝也分崩离析了
         */

        private String image;
        private int type;
        private int id;
        private String ga_prefix;
        private String title;

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getGa_prefix() {
            return ga_prefix;
        }

        public void setGa_prefix(String ga_prefix) {
            this.ga_prefix = ga_prefix;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }
    }
}
